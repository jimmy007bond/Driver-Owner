package com.geekworkx.oho.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.oho.Model.User;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by parag on 22/09/16.
 */
public class Ride_adapter extends RecyclerView.Adapter<Ride_adapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<User> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private DatabaseReference mDatabase;
    private String Driver_image;
    private String con,u_PhoneNo,Vehicle;
    private double from_lat=0,from_long=0,to_lat=0,to_long=0;
    private String OTP;
    private String WHO;
    private MediaPlayer Player;
    DecimalFormat dft=new DecimalFormat("0.000000");


    public Ride_adapter(Context aContext, ArrayList<User> mItems) {
        this.mItems = mItems;
        layoutInflater = LayoutInflater.from(aContext);
        this.mContext=aContext;

    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setMobile(String mobilet) {
        _PhoneNo=mobilet;
    }

    public void setMyLat(double my_lat, double my_long) {
        My_lat=my_lat;
        My_long=my_long;
    }

    public void setDriverImage(String driver_image) {
        Driver_image=driver_image;
    }

    public void setWho(String who) {
        WHO=who;
    }

    public void setPlayer(MediaPlayer player) {
        Player=player;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView laterDate,laterTime;
        private EditText inputFrom, inputTo;
        private TextInputLayout inputFromA, inputFromT;
        private Button laterEdit,laterCancel;
        public ViewHolder(View itemView) {
            super(itemView);

            laterDate=itemView.findViewById(R.id.ride_date_later);
            laterTime=itemView.findViewById(R.id.ride_time_later);
            inputFrom=itemView.findViewById(R.id.input_from_address);
            inputTo=itemView.findViewById(R.id.input_to_address);
            inputFromA=itemView.findViewById(R.id.input_from);
            inputFromT=itemView.findViewById(R.id.input_to);
            laterEdit=itemView.findViewById(R.id.ride_later_edit);
            laterCancel=itemView.findViewById(R.id.ride_later_cancel);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.later_rv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final User album_pos = mItems.get(position);
        if (!TextUtils.isEmpty(album_pos.getUser_from(position))) {
            viewHolder.inputFrom.setText(album_pos.getUser_from(position));

        }
        if (!TextUtils.isEmpty(album_pos.getUser_to(position))) {
            viewHolder.inputTo.setText(album_pos.getUser_to(position));
            viewHolder.inputFromT.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(album_pos.getVehicle(position))) {
            viewHolder.laterDate.setText("Vehicle :"+album_pos.getVehicle(position));
            Vehicle=album_pos.getVehicle(position);
        }
        if (!TextUtils.isEmpty(album_pos.getUser_mobile(position))) {
            u_PhoneNo=album_pos.getUser_mobile(position);
        }
        if (!TextUtils.isEmpty(album_pos.getOTP(position))) {
            OTP=album_pos.getOTP(position);
        }

        viewHolder.laterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(position);
                String[] pars = album_pos.getUnique_ride(position).split("\\.");
                con = TextUtils.join("", pars);
                mDatabase.child("Cancel").child(_PhoneNo).child(con).setValue("1");
                if(mItems.size()==0){
                    if(!WHO.contains("OWNER")) {
                        Intent o = new Intent(mContext, GooglemapApp.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        mContext.startActivity(o);
                        ((Activity)mContext).finish();

                    }else{
                        Intent o = new Intent(mContext, After_owner_login_copy.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        mContext.startActivity(o);
                        ((Activity)mContext).finish();

                    }

                }

            }
        });
        viewHolder.laterEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (album_pos.getUnique_ride(position) != null) {
                    String[] pars = album_pos.getUnique_ride(position).split("\\.");
                    con = TextUtils.join("", pars);
                        from_lat = album_pos.getUser_from_lat(position);
                        from_long = album_pos.getUser_from_long(position);
                        to_lat = album_pos.getUser_to_lat(position);
                        to_long = album_pos.getUser_to_long(position);
                    new PostBookdata().execute(album_pos.getUnique_ride(position));

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());

    }



    private class PostBookdata  extends AsyncTask<String, Integer, String> {


        private boolean success;
        private String UNIQUE_ID;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            UNIQUE_ID=args[0];
            return uploadFile(args[0]);
        }

        private String uploadFile(String args) {
            // TODO Auto-generated method stub
            String res = null;

            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("drivermobile", _PhoneNo)
                        .addFormDataPart("usermobile",u_PhoneNo)
                        .addFormDataPart("otp",OTP)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.BOOKING_RIDE_DRIVER)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;

                }else{
                    success=false;
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

            if (success == true) {
                if(Player!=null && (Player.isPlaying() || Player.isLooping())){
                    Player.stop();

                }
                mDatabase.child("Accepted_Ride").child(con).child("DriverMobile").setValue(_PhoneNo);
                mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(My_lat));
                mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(My_long));
                mDatabase.child("Accepted_Ride").child(con).child("User_Latitude").setValue(dft.format(from_lat));
                mDatabase.child("Accepted_Ride").child(con).child("User_Longitude").setValue(dft.format(from_long));
                mDatabase.child("Accepted_Ride").child(con).child("UserMobile").setValue(u_PhoneNo);
                mDatabase.child("Accepted_Ride").child(con).child("Book_To_Latitude").setValue(dft.format(to_lat));
                mDatabase.child("Accepted_Ride").child(con).child("Book_To_Longitude").setValue(dft.format(to_long));
                mDatabase.child("Accepted_Ride").child(con).child("Vehicle_Choosen").setValue(Vehicle);
                mDatabase.child("Accepted_Ride").child(con).child("DriverImage").setValue(Driver_image);
                mDatabase.child("Accepted_Ride").child(con).child("driverAccept").setValue("YES");
                mDatabase.child("Accepted_Ride").child(con).child("Book_From_Latitude").setValue(dft.format(from_lat));
                mDatabase.child("Accepted_Ride").child(con).child("Book_From_Longitude").setValue(dft.format(from_long));
                mDatabase.child("Ride_Request").child(con).removeValue();
                mDatabase.child("Driver_Online").child(_PhoneNo).child("Ride_ID").setValue(UNIQUE_ID);
                mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("YES");
                if(!WHO.contains("OWNER")) {
                    Intent o = new Intent(mContext, GooglemapApp.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    mContext.startActivity(o);
                    ((Activity)mContext).finish();

                }else{
                    Intent o = new Intent(mContext, After_owner_login_copy.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    mContext.startActivity(o);
                    ((Activity)mContext).finish();

                }

            } else {
                Toast.makeText(mContext,"Please try again",Toast.LENGTH_SHORT).show();
               }
        }

    }

}





