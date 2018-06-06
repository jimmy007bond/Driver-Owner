package com.geekworkx.oho.Drawer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geekworkx.oho.Adapters.After_owner_login_copy;
import com.geekworkx.oho.Adapters.GooglemapApp;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.Model.Later;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.HttpHandler;
import com.geekworkx.oho.helper.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by parag on 27/01/18.
 */

public class Ride_later_dates extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView laterRv;
    private ArrayList<Later> mItems=new ArrayList<Later>();
    private static final String TAG = Ride_later_dates.class.getSimpleName();
    private PrefManager pref;
    private String _PhoneNo;
    private double My_lat=0,My_long=0;
    private Button Datesadd;
    private Toolbar toolbar;
    private ConnectionDetector cd;
    private TextView no_rides;
    private String WHO;
    private int Owner_ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        toolbar = (Toolbar) findViewById(R.id.toolbar_later_tabs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cd = new ConnectionDetector(getApplicationContext());
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        WHO = user.get(PrefManager.KEY_WHO);
        laterRv=findViewById(R.id.laterRv);
        no_rides=findViewById(R.id.no_rides);
        progressBar = findViewById(R.id.progressBar21);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WHO.contains("OWNER")) {

                    Intent o = new Intent(Ride_later_dates.this, After_owner_login_copy.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                } else {
                    Intent o = new Intent(Ride_later_dates.this, GooglemapApp.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mItems.clear();
        new GetpastDate().execute();

    }

    private class GetpastDate extends AsyncTask<Void, Void, Void> {


        private int Driver_ID=0;

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



                    // looping through All Contacts
                    if (_PhoneNo != null) {
                        JSONArray Owner = jsonObj.getJSONArray("Owner_Details");
                        JSONArray rides = jsonObj.getJSONArray("Book_Ride_Past");
                        JSONArray driver = jsonObj.getJSONArray("Driver_Details");
                        JSONArray gari = jsonObj.getJSONArray("Vehicle_detail");

                        for (int i = 0; i < Owner.length(); i++) {
                            JSONObject c = Owner.getJSONObject(i);
                            String relation = c.getString("Phone_No");


                            if (relation.matches(_PhoneNo)) {
                                Owner_ID = c.getInt("ID");
                            }
                        }


                        for (int i = 0; i < driver.length(); i++) {
                            JSONObject e = driver.getJSONObject(i);
                            String relation = e.getString("Phone_No");
                            if(Owner_ID==0) {
                                if (!relation.contains("null")) {

                                    if (relation.matches(_PhoneNo)) {
                                        Driver_ID = e.getInt("ID");
                                        for (int j = 0; j < rides.length(); j++) {
                                            JSONObject c = rides.getJSONObject(j);
                                            if (!c.isNull("Driver_ID") ) {


                                                    if (c.getInt("Driver_ID")==Driver_ID) {
                                                        Later item = new Later();
                                                        item.setDate(c.getString("Start_Date"));
                                                        item.setTime(c.getString("Start_time"));
                                                        item.setFrom(c.getString("From_Address"));
                                                        item.setTo(c.getString("To_Address"));
                                                        item.setSnapshot(c.getString("Map_Snapshot"));
                                                        item.setDriverImage(e.getString("Photo"));
                                                        item.setUnique_id(c.getString("Unique_Ride_Code"));
                                                        for (int jkl = 0; jkl < gari.length(); jkl++) {
                                                            JSONObject f = gari.getJSONObject(jkl);

                                                            if (!f.isNull("Driver_ID") &&Driver_ID == c.getInt("Driver_ID")) {
                                                                item.setVehicle(f.getString("Vehicle_Type"));
                                                                break;
                                                            }
                                                        }
                                                        item.setDrivername(e.getString("Name"));
                                                        item.setDriverRate(c.getString("Driver_Rating_By_User"));
                                                        item.setCost(c.getString("Cost"));
                                                        item.setStart_time(c.getString("Start_time"));
                                                        item.setEnd_time(c.getString("End_time"));
                                                        item.setReview(c.getString("User_Review"));
                                                        mItems.add(item);

                                                    }

                                            }
                                        }
                                    }

                                }
                            }else{
                                    if (e.getInt("Owner_ID") == Owner_ID) {
                                                Driver_ID = e.getInt("ID");
                                        for (int j = 0; j < rides.length(); j++) {
                                            JSONObject c = rides.getJSONObject(j);
                                            if (!c.isNull("Driver_ID") ) {

                                                    if (c.getInt("Driver_ID")==Driver_ID) {
                                                        Later item = new Later();
                                                        item.setDate(c.getString("Start_Date"));
                                                        item.setTime(c.getString("Start_time"));
                                                        item.setFrom(c.getString("From_Address"));
                                                        item.setTo(c.getString("To_Address"));
                                                        item.setSnapshot(c.getString("Map_Snapshot"));
                                                        item.setDriverImage(e.getString("Photo"));
                                                        item.setUnique_id(c.getString("Unique_Ride_Code"));
                                                        for (int jkl = 0; jkl < gari.length(); jkl++) {
                                                            JSONObject f = gari.getJSONObject(jkl);

                                                            if (!f.isNull("Driver_ID") &&Driver_ID == c.getInt("Driver_ID")) {
                                                                item.setVehicle(f.getString("Vehicle_Type"));
                                                                break;
                                                            }
                                                        }
                                                        item.setDrivername(e.getString("Name"));
                                                        item.setDriverRate(c.getString("Driver_Rating_By_User"));
                                                        item.setCost(c.getString("Cost"));
                                                        item.setStart_time(c.getString("Start_time"));
                                                        item.setEnd_time(c.getString("End_time"));
                                                        item.setReview(c.getString("User_Review"));
                                                        mItems.add(item);

                                                    }
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
            if (mItems.size() != 0) {
                laterRv.setVisibility(View.VISIBLE);
                no_rides.setVisibility(View.GONE);
                Later_Adapter sAdapter = new Later_Adapter(Ride_later_dates.this, mItems);
                sAdapter.notifyDataSetChanged();
                sAdapter.setMobile(_PhoneNo);
                sAdapter.setMyLat(My_lat,My_long);
                laterRv.setAdapter(sAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Ride_later_dates.this);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                laterRv.setLayoutManager(mLayoutManager);
                laterRv.addOnItemTouchListener(
                        new RecyclerTouchListener(Ride_later_dates.this, laterRv,
                                new RecyclerTouchListener.OnTouchActionListener() {
                                    public boolean Me;

                                    @Override
                                    public void onClick(View view, final int position) {

                                        if (mItems.size() != 0) {
                                            Intent i = new Intent(Ride_later_dates.this, PastRides.class);
                                            i.putExtra("my_lat", My_lat);
                                            i.putExtra("my_long", My_long);
                                            i.putExtra("UNIQUEID", mItems.get(position).getUnique_id(position));
                                            startActivity(i);
                                            Ride_later_dates.this.finish();
                                        }

                                    }

                                    @Override
                                    public void onRightSwipe(View view, int position) {

                                    }

                                    @Override
                                    public void onLeftSwipe(View view, int position) {

                                    }
                                }));
            }else{
                laterRv.setVisibility(View.GONE);
                no_rides.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (WHO.contains("OWNER")) {

            Intent o = new Intent(Ride_later_dates.this, After_owner_login_copy.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
        } else {
            Intent o = new Intent(Ride_later_dates.this, GooglemapApp.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
        }
    }
}
