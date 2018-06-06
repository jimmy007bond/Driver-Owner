package com.geekworkx.oho.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.Model.Book;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.HttpHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 20/09/17.
 */

public class Success extends Activity implements  View.OnClickListener{

    private static final String TAG = Success.class.getSimpleName();
    private TextView Total_m,Uname;
    private Button Yes;
    private double KM;
    private double COST;
    private String UNIQUEID;
    private PrefManager pref;
    private String USER;
    private String _PhoneNo;
    private String WHO;
    private ProgressBar progressBar;
    private ImageView Profile;
    private Button Submit;
    private Book data_f = new Book();
    private String Paid;
    private String Rider_mobile;
    private TextView rrr;
    private String User_pic,User_mobile;
    private String Opp_mobile;
    private FirebaseDataListener mFirebaseDataChanged;
    private String Rider_name,User_name;
    private float rate=0;
    private double My_lat=0,My_long=0;
    private String Driver_mobile;
    private String Rate_user;
    private RelativeLayout F1;
    private Button Go;
    private DatabaseReference mDatabase;
    private String mobileIp;
    private String con;
    private Button Bill;
    private RatingBar Bill_rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        Total_m=  findViewById(R.id.bill_generated);
        Uname=  findViewById(R.id.uname);
        progressBar=(ProgressBar)findViewById(R.id.progressBar3_last);
        Bill_rate=findViewById(R.id.ratingBarbill);
        LayerDrawable stars = (LayerDrawable) Bill_rate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        Profile=findViewById(R.id.driver_bill);
        Submit=(Button)findViewById(R.id.success_ride);
        Bill=findViewById(R.id.button_bill);
        Bill.setOnClickListener(this);
        Intent i=getIntent();
        UNIQUEID=i.getStringExtra("UNIQUEID");
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        WHO= user.get(PrefManager.KEY_WHO);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Submit.setOnClickListener(this);
        mobileIp = getMobileIPAddress();
        if(TextUtils.isEmpty(mobileIp)){
            mobileIp= getWifiIPAddress();
        }
    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return null;
    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return  Formatter.formatIpAddress(ip);
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UNIQUEID != null) {
            String[] pars = UNIQUEID.split("\\.");
            con = TextUtils.join("", pars);
        }
        mFirebaseDataChanged = new FirebaseDataListener();
        new GetRide().execute();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.success_ride:
                if (UNIQUEID != null) {
                    String[] pars = UNIQUEID.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).child("Paid").setValue("YES");
                    Paid="YES";
                    new PostConfirmation().execute();
                }
                break;

            case R.id.button_bill:
                if (UNIQUEID != null) {
                    String[] pars = UNIQUEID.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_continue());
                   }
                break;

            default:
                    break;

        }

    }

    private class GetRide extends AsyncTask<Void, Void, Void>

    {

        private int Driver_id=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_SETTINGS);


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    // Getting JSON Array node
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Now");
                    JSONArray Driver = jsonObj.getJSONArray("Driver_Details");

                    for (int i = 0; i < Driver.length(); i++) {
                        JSONObject c = Driver.getJSONObject(i);
                        String relation = c.getString("Phone_No");
                        if (!c.isNull("Phone_No") && !TextUtils.isEmpty(c.getString("Name"))) {

                            if (relation.matches(_PhoneNo)) {
                                Driver_id = c.getInt("ID");

                            }

                        }
                    }

                    if(UNIQUEID!=null) {
                        for (int x = 0; x < ride.length(); x++) {
                            JSONObject d = ride.getJSONObject(x);
                            if (d.getString("Unique_Ride_Code").matches(UNIQUEID)) {
                                if (d.getInt("Driver_ID")==Driver_id) {

                                    for (int i = 0; i < User.length(); i++) {
                                        JSONObject c = User.getJSONObject(i);

                                        if (c.getInt("ID")==(d.getInt("User_ID"))) {
                                            User_pic = c.getString("Photo");
                                            User_name = c.getString("Name");
                                            Driver_mobile=_PhoneNo;
                                            User_mobile = c.getString("Phone_No");
                                        }

                                    }


                                }

                            }
                        }


                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);



                    if(User_pic!=null){
                        Picasso.Builder builder = new Picasso.Builder(Success.this);
                        builder.listener(new Picasso.Listener()
                        {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                            {
                                exception.printStackTrace();
                            }
                        });
                        builder.build().load(User_pic).into(Profile);
                        Uname.setText(User_name);

                    }


            mDatabase.child("Accepted_Ride").child(con).addValueEventListener(mFirebaseDataChanged);
            Bill_rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Rate_user = String.valueOf(rating);
                    rate=rating;
                }
            });


        }
    }


    private class FirebaseDataListener_continue implements ValueEventListener {


        private double Cost=0;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.getChildrenCount()!=0) {

                if (dataSnapshot.child("Bill").getValue() != null &&
                        dataSnapshot.child("Tax_names").getValue() != null) {
                    if (!Success.this.isFinishing()) {

                        final Dialog dialog = new Dialog(Success.this);

                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.dialog_bill);


                        COST=Double.parseDouble((String) dataSnapshot.child("Bill").getValue());
                        DecimalFormat dft=new DecimalFormat("0.00");

                        TextView total_cost = (TextView) dialog.findViewById(R.id.total_cost);
                        TextView total_km_cost = (TextView) dialog.findViewById(R.id.total_km_cost);
                        TextView total_coupon = (TextView) dialog.findViewById(R.id.total_coupon);
                        TextView total_taxes = (TextView) dialog.findViewById(R.id.total_taxes);
                        TextView total_fare = (TextView) dialog.findViewById(R.id.total_fare);
                        TextView tax_name = (TextView) dialog.findViewById(R.id.tax_name);
                        LinearLayout C_applied = dialog.findViewById(R.id.c_applied);

                        if ((String) dataSnapshot.child("Cost").getValue() != null && (String) dataSnapshot.child("Estimated_fare").getValue() != null) {
                            if (Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue()) < Double.parseDouble((String) dataSnapshot.child("Cost").getValue())) {
                                Cost = Double.parseDouble((String) dataSnapshot.child("Cost").getValue());
                            } else if (Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue()) > Double.parseDouble((String) dataSnapshot.child("Cost").getValue())) {
                                Cost = Double.parseDouble((String) dataSnapshot.child("Estimated_fare").getValue());
                            }
                        }
                        total_cost.setText("\u20B9 " + (dft.format(COST)));
                        total_km_cost.setText("\u20B9 " +dft.format(Cost));
                        if (dataSnapshot.child("Coupon_value").getValue() != null) {
                            total_coupon.setText("-" + "\u20B9 " +dft.format(Double.parseDouble( (String) dataSnapshot.child("Coupon_value").getValue())));
                        } else {
                            C_applied.setVisibility(View.GONE);
                        }
                        tax_name.setText((String) dataSnapshot.child("Tax_names").getValue());
                        double tx = Double.parseDouble((String) dataSnapshot.child("Bill").getValue()) * Double.parseDouble((String) dataSnapshot.child("Tax_percentage").getValue()) / 100;
                        total_taxes.setText("\u20B9 " + dft.format(tx));
                        total_fare.setText("\u20B9 " + dft.format(Double.parseDouble((String) dataSnapshot.child("Bill").getValue())));

                        dialog.show();
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    private class FirebaseDataListener implements ValueEventListener {



        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.child("Distance_travel").getValue()!=null) {
                KM = Double.parseDouble((String) dataSnapshot.child("Distance_travel").getValue());
            }else{
                if(dataSnapshot.child("Distance").getValue()!=null) {
                    KM = Double.parseDouble((String) dataSnapshot.child("Distance").getValue());
                }
            }
            COST=Double.parseDouble((String) dataSnapshot.child("Bill").getValue());
            DecimalFormat dft=new DecimalFormat("0.00");
            Total_m.setText("\u20B9 " +dft.format(COST));

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    private class PostConfirmation extends AsyncTask<Void, Integer, String> {


        private boolean success=false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


        }

        protected String doInBackground(Void... args) {
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("unique_id",UNIQUEID)
                        .addFormDataPart("driver_mobile",_PhoneNo)
                        .addFormDataPart("user_rating",Rate_user)
                        .addFormDataPart("cost", String.valueOf(COST))
                        .addFormDataPart("distance", String.valueOf(KM))
                        .addFormDataPart("paid", Paid)
                        .addFormDataPart("IP", mobileIp)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.STOP_BOOKING)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars = res.split("error");
                if (pars[1].contains("false")) {
                    success = true;

                } else {
                    success = false;
                }
                Log.e("TAG", "Response : " + res);

                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());


            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());

            }


            return res;

        }

        protected void onPostExecute(String file_url) {
            if (success) {
                if (UNIQUEID != null) {
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Ride_ID").removeValue();
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                }
                if (WHO != null) {
                    if (WHO.contains("OWNER")) {
                        Intent i = new Intent(Success.this, After_owner_login_copy.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();
                    } else {
                        Intent o = new Intent(Success.this, GooglemapApp.class);
                        o.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        o.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(o);
                        finish();
                    }
                }
            }

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
