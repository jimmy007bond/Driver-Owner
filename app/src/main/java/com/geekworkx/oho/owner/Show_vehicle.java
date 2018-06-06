package com.geekworkx.oho.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.geekworkx.oho.Adapters.After_owner_login_copy;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by parag on 08/01/18.
 */

public class Show_vehicle extends AppCompatActivity {

    private Button Change_vehicle,Okbutton;
    private ImageView Owner_image1;
    private ImageView Owner_image2;
    private String Driver_image,Vehicle_image;
    private ProgressBar progreeBar;
    private String Vehicle_no;
    private String Driver_selected;
    private String UNIQUE_ID;
    private PrefManager pref;
    private String _PhoneNo;
    private DatabaseReference mDatabase;
    private String Driver_name;
    private double My_lat=0,My_long=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_driver_change_vehicle);
        Change_vehicle=findViewById(R.id.change_vehicle);
        Owner_image1=findViewById(R.id.profile_driver);
        Owner_image2=findViewById(R.id.imagecar);
        progreeBar=findViewById(R.id.progressBardel);
        Okbutton=findViewById(R.id.buttonok);

        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        Driver_image = i.getStringExtra("Driver_image");
        Driver_name = i.getStringExtra("Driver_name");
        Vehicle_image= i.getStringExtra("Vehicle_image");
        Vehicle_no=i.getStringExtra("vehicle");
        Driver_selected=i.getStringExtra("driver");
        UNIQUE_ID=i.getStringExtra("unique_id");
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        Picasso.Builder builder = new Picasso.Builder(Show_vehicle.this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build().load(Driver_image).into(Owner_image1);
        Picasso.Builder builder2 = new Picasso.Builder(Show_vehicle.this);
        builder2.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder2.build().load(Vehicle_image).into(Owner_image2);

        Change_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.support.v7.app.AlertDialog.Builder(Show_vehicle.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Remove vehicle!")
                        .setMessage("Vehicle no "+Vehicle_no+"from  "+Driver_name)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteVehicle().execute();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();



            }
        });


        Okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(Show_vehicle.this, After_owner_login_copy.class);
                o.putExtra("WHERE",true);
                o.putExtra("my_lat",My_lat);
                o.putExtra("my_long",My_long);
                startActivity(o);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent o = new Intent(Show_vehicle.this, After_owner_login_copy.class);
        o.putExtra("WHERE",true);
        o.putExtra("my_lat",My_lat);
        o.putExtra("my_long",My_long);
        startActivity(o);
        finish();
    }


    class DeleteVehicle extends AsyncTask<Void, Integer, String> {


        private boolean success=false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progreeBar.setVisibility(View.VISIBLE);
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
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("driver",Driver_selected)
                        .addFormDataPart("vehicle",Vehicle_no)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_CAR_DELETE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);
                String[] pars = res.split("error");
                if (pars[1].contains("false")) {
                    success = true;


                } else {
                    success = false;

                }
                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        protected void onPostExecute(String file_url) {
            progreeBar.setVisibility(View.GONE);
            if(success) {
                //mDatabase.child("Driver_Online").child(Driver_selected).removeValue();
                mDatabase.child("Driver_Online").child(Driver_selected).child("Driver_Vehicle").removeValue();
                Intent o = new Intent(Show_vehicle.this, After_owner_login_copy.class);
                o.putExtra("my_lat",My_lat);
                o.putExtra("my_long",My_long);
                startActivity(o);
                finish();
            }

        }

    }
}
