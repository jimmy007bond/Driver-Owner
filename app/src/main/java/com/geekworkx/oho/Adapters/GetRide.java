package com.geekworkx.oho.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.Model.User;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.HttpHandler;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by parag on 29/01/18.
 */

public class GetRide  extends AppCompatActivity {
    private static final String TAG = GetRide.class.getSimpleName();
    private ProgressBar progressBar;
    private RecyclerView laterRv;
    private PrefManager pref;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private Toolbar toolbar;
    private String Driver_image;
    private Location mCurrentLocation;
    private DatabaseReference mDatabase;
    private String Vehicle;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    private Ringtone r;
    private String WHO;
    private MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.later_dates);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        WHO=user.get(PrefManager.KEY_WHO);
        progressBar = findViewById(R.id.progressBar21);
        //laterRv=findViewById(R.id.laterRv);
        toolbar=findViewById(R.id.toolbar_later_dates);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitle("Ride Request");
        //toolbar.setTitleTextColor(getResources().getColor(R.color.top));
        laterRv=findViewById(R.id.laterRv);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        Vehicle=i.getStringExtra("Vehicle");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!WHO.contains("OWNER")) {
                    Intent o = new Intent(GetRide.this, GooglemapApp.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                }else{
                    Intent o = new Intent(GetRide.this, After_owner_login_copy.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if(player!=null && (player.isPlaying() || player.isLooping())){
            player.stop();

        }
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        new GetRide1().execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player!=null && (player.isPlaying() || player.isLooping())){
            player.stop();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null && (player.isPlaying() || player.isLooping())){
            player.stop();

        }
    }

    private class GetRide1 extends AsyncTask<Void, Void, Void> {

        private ArrayList<User> mItems=new ArrayList<User>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                    JSONArray vehicle = jsonObj.getJSONArray("Vehicle_detail");
                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Now");
                    JSONArray User = jsonObj.getJSONArray("User");





                    for (int i = 0; i < ride.length(); i++) {
                        JSONObject c = ride.getJSONObject(i);
                        if(!c.isNull("Vehicle_ID") ) {
                            User item = new User();
                            item.setOTP(c.getString("OTP"));
                            item.setVehicle(Vehicle);
                            item.setUnique_ride(c.getString("Unique_Ride_Code"));
                            item.setUser_from(c.getString("From_Address"));
                            item.setUser_to(c.getString("To_Address"));

                            for (int j = 0; j < User.length(); j++) {
                                JSONObject d = User.getJSONObject(j);
                                if (c.getInt("User_ID")==d.getInt("ID")) {
                                    item.setUser_mobile(d.getString("Phone_No"));
                                    break;
                                }
                            }
                            item.setUser_from_lat(c.getDouble("From_Latitude"));
                            item.setUser_from_long(c.getDouble("From_Longitude"));
                            item.setUser_to_lat(c.getDouble("To_Latitude"));
                            item.setUser_to_long(c.getDouble("To_Longitude"));
                            mItems.add(item);
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
            if (mItems.size() != 0) {
                    RegisterAlarmBroadcast();
                    progressBar.setVisibility(View.GONE);
                    Ride_adapter sAdapter = new Ride_adapter(GetRide.this, mItems);
                    sAdapter.notifyDataSetChanged();
                    sAdapter.setMobile(_PhoneNo);
                    sAdapter.setDriverImage(Driver_image);
                    sAdapter.setMyLat(My_lat, My_long);
                    sAdapter.setPlayer(player);
                    sAdapter.setWho(WHO);
                    laterRv.setAdapter(sAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(GetRide.this);
                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    laterRv.setLayoutManager(mLayoutManager);

                } else {

                    if (!WHO.contains("OWNER")) {
                        if(player!=null && (player.isPlaying() || player.isLooping())){
                            player.stop();

                        }
                        Intent o = new Intent(GetRide.this, GooglemapApp.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        startActivity(o);
                        finish();
                    } else {
                        if(player!=null && (player.isPlaying() || player.isLooping())){
                            player.stop();

                        }
                        Intent o = new Intent(GetRide.this, After_owner_login_copy.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        startActivity(o);
                        finish();
                    }

                }


        }
    }
    private void RegisterAlarmBroadcast() {
        Uri path =  Uri.parse("android.resource://"
                + getPackageName() + "/" + R.raw.car);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
         player = MediaPlayer.create(this, notification);
        player.setLooping(true);
        player.start();
    }

    private void UnregisterAlarmBroadcast() {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(mReceiver);
    }
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            if(!WHO.contains("OWNER")) {
                Intent o = new Intent(GetRide.this, GooglemapApp.class);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                startActivity(o);
                finish();
            }else{
                Intent o = new Intent(GetRide.this, After_owner_login_copy.class);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                startActivity(o);
                finish();
            }

        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Press Back again to Exit.", Snackbar.LENGTH_LONG).show();

            doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            },  1000);

        }

    }
}
