package com.geekworkx.oho.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.oho.BuildConfig;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Drawer.Ride_later_dates;
import com.geekworkx.oho.FCM.NotificationUtils;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.Model.Book;
import com.geekworkx.oho.R;
import com.geekworkx.oho.Services.SensorService;
import com.geekworkx.oho.Activities.Splash_screen;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.Album;
import com.geekworkx.oho.helper.HttpHandler;
import com.geekworkx.oho.owner.Owner_driver_add;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import in.shadowfax.proswipebutton.ProSwipeButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.google.android.gms.maps.model.JointType.ROUND;

/**
 * Created by parag on 31/12/16.
 */
public class GooglemapApp extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private ArrayList<LatLng> markerPoints;
    private static final String TAG = GooglemapApp.class.getSimpleName();
    private String Unique_id;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private TextView txtName;
    private ImageView Edit_profile;
    private CircleImageView profile_image;
    private Toolbar toolbar;
    private TextView noOwner;
    private String _PhoneNo;
    private PrefManager pref;
    private String USER;
    private int navItemIndex = 0;
    private GoogleMap googleMap;
    double My_lat, My_long;
    private CustomMapFragment mapFragment;
    private ProgressBar progressBar;
    private final int REQUEST_LOCATION = 200;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final int M_MAX_ENTRIES = 5;
    private CameraPosition mCameraPosition;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String resultText;
    private String mLastUpdateTime;
    private double MyLat = 0, MyLong = 0;
    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private ImageView myLocationButton;
    private String Driver_name, Driver_image;
    private Marker marker;
    private Bitmap mMarkerIcon;
    private LinearLayout Ride_later,Ride_now;
    private int mIndexCurrentPoint=0;
    private boolean hasBeenInitialized;
    private FirebaseApp apptt;
    private DatabaseReference mDatabase;
    private Book data_book=new Book();
    private String vehicle_type;
    private String Name;
    private boolean clicK=false;
    private ArrayList<Album> mItems=new ArrayList<Album>();
    private ArrayList<String> mTaxes=new ArrayList<String>();
    private String Owner_mobile,Owner_name,Owner_image;
    private int Identification=0;
    private String Unique_owner;
    private ImageView Owner_photo;
    private TextView Owner_nam;
    private int OTP;
    private String Driver_phone;
    private String UNIQUE_RIDE;
    private Book data_f=new Book();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String regId;
    private ArrayList<String> CancelRide=new ArrayList<String>();
    private static final LatLng[] ROUTE = new LatLng[2];
    private static final float CAMERA_OBLIQUE_ZOOM = 14;
    private static final float CAMERA_OBLIQUE_TILT = 60;
    private static final long CAMERA_HEADING_CHANGE_RATE = 5;
    private List<LatLng> mPathPolygonPoints=new ArrayList<LatLng>() ;
    private boolean Ride=false;
    private String User_from,User_to,u_PhoneNo;
    private double from_lat=0,from_long=0,to_lat=0,to_long=0;
    private Button Confirm_ride,Cancel_ride;
    private LinearLayout Rl;
    private LinearLayout Lr;
    private DotProgressBar Amb;
    private boolean Start_ride=false;
    private boolean first=false;
    private boolean again=false;
    private String filePath;
    private Boolean ask=false,wait=false,go=false,stop=false,Accepted=false;
    private String con;
    private Button Stop_ride;
    private PolylineOptions lineOptions = null;
    private Polyline polylineFinal;
    private boolean animate=false;
    private LatLng To_latLong;
    private ImageView MyStar;
    private boolean offline=false;
    private Marker markerUser;
    private boolean cancelled=false;
    private SensorService mSensorService;
    private Intent mServiceIntent;
    private Matrix matrix;
    private int Driver_id=0;
    private String vehicle_No;
    private Marker markerd;
    private Animation animslideD,animslideU;
    private double Distance=0;
    private double Cost=0;
    private int Minimum_Distance_in_Meter_for_Tracking=0;
    private String Coupon_code="";
    private double Coupon_value=0;
    private boolean got=false;
    private int Minimum_fare=0,Hourly_fare=0;
    DecimalFormat dft=new DecimalFormat("0.000000");
    DecimalFormat dfto=new DecimalFormat("0.00");
    private double Tota_taxes=0;
    private String Tax_applied;
    private double Tax_percentage=0;
    private String mobileIp;
    private boolean Minimum_Balance=false;
    private float bearing=0;
    private double Distance_travel=0;
    private ProSwipeButton proSwipeBtn;
    private String User_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.dialogmap);
        Rl=findViewById(R.id.ride_detail);
        Confirm_ride=findViewById(R.id.confirm_ride);
        Cancel_ride=findViewById(R.id.cancel_ride);
        Lr=findViewById(R.id.linearLayout);
        Amb=findViewById(R.id.dot_progress_bar_amb);
        Stop_ride=findViewById(R.id.stop_ride);
        Stop_ride.setOnClickListener(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cor_home_main);

        proSwipeBtn = (ProSwipeButton) findViewById(R.id.awesome_btn);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name_profile);
        MyStar = navHeader.findViewById(R.id.my_star);
        Edit_profile = (ImageView) navHeader.findViewById(R.id.edit_profile);
        profile_image = (CircleImageView) navHeader.findViewById(R.id.img_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        noOwner=findViewById(R.id.textnoOwner);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpNavigationView();
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        Name = user.get(PrefManager.KEY_NAME);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3_map);
        myLocationButton = (ImageView) findViewById(R.id.myLocationCustomButton);
        cd = new ConnectionDetector(getApplicationContext());
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        rebuildGoogleApiClient();
        mapFragment = (CustomMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(GooglemapApp.this);
        Owner_photo=findViewById(R.id.item_owner);
        Owner_nam=findViewById(R.id.desc_owner);
        // TODO: This next IF statement temporarily deals with an issue where autoManage doesn't
        // call the onConnected callback after a Builder.build() when re-connecting after a
        // rotation change. Will remove when fixed.
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            onConnected(null);
        }

        drawer.closeDrawers();
        Ride_later=findViewById(R.id.ride_later);
        Ride_now=findViewById(R.id.ride_now);

        myLocationButton.setOnClickListener(this);
        Ride_later.setOnClickListener(this);
        Ride_now.setOnClickListener(this);
        Confirm_ride.setOnClickListener(this);
        Cancel_ride.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config_URL.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config_URL.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config_URL.PUSH_NOTIFICATION)) {

                    String message = intent.getStringExtra("message");

                    //addNotification(message);

                }
            }
        };
        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
        displayFirebaseRegId();
        matrix = new Matrix();

        Intent i = getIntent();
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        UNIQUE_RIDE=i.getStringExtra("UNIQUE_RIDE");
        animslideD = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down1);
        animslideU = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up1);
        mobileIp = get_PhoneNoIPAddress();
        if(TextUtils.isEmpty(mobileIp)){
            mobileIp= getWifiIPAddress();
        }

        proSwipeBtn.setVisibility(View.GONE);

        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      if(User_mobile!=null){
                          new getFCM().execute(User_mobile);

                      }
                    }
                }, 2000);
            }
        });
    }


    class getFCM extends AsyncTask<String, Void, Void> {

        private String _Car;
        private String fcm_token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg0) {



            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_SETTINGS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray user = jsonObj.getJSONArray("User");

                    for (int j = 0; j < user.length(); j++) {
                        JSONObject d = user.getJSONObject(j);
                        if(!d.isNull("Phone_No") ) {

                            if(d.getString("Phone_No").contains(arg0[0])) {

                                fcm_token=d.getString("Firebase_Token");
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
                        Snackbar snackbar = Snackbar
                                .make(getWindow().getDecorView().getRootView(), "No internet connection!", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        recreate();
                                    }
                                });
                        snackbar.setActionTextColor(Color.YELLOW);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(fcm_token!=null){

                new CreateFCM().execute(fcm_token);

            }


        }
    }

    class CreateFCM extends AsyncTask<String, Integer, String> {
        private boolean success=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            String fcm=args[0];
            return uploadFile(fcm);
        }

        private String uploadFile(String fcm) {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Request for ride")
                        .addFormDataPart("message","Your Ride has arraived")
                        .addFormDataPart("push_type", "individual")
                        .addFormDataPart("regId", fcm)
                        .addFormDataPart("include_image","FALSE")
                        .addFormDataPart("image","")
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.FCM_SENT)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars1 = res.split(",");
                if (pars1[1].contains("1")) {
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
            if(success){
                proSwipeBtn.showResultIcon(true);
                Toast.makeText(getApplicationContext(),"User notified",Toast.LENGTH_SHORT).show();


            }else{
                proSwipeBtn.showResultIcon(false);

            }

        }

    }

    public static String get_PhoneNoIPAddress() {
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
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config_URL.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            if (_PhoneNo != null) {
                new Update_FCM().execute();
            }
        }
    }

    class Update_FCM extends AsyncTask<Void, Integer, String> {


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
                        .addFormDataPart("mobile", _PhoneNo)
                        .addFormDataPart("token", regId)


                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_FCM_DRIVER)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
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

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            //outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            outState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
            super.onSaveInstanceState(outState);
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
            }
        };
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected synchronized void rebuildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and connection failed
        // callbacks should be returned, which Google APIs our app uses and which OAuth 2.0
        // scopes our app requests. When using enableAutoManage to register the failed connection
        // listener it will only be called back when auto-resolution attempts were not
        // successful or possible. A normal ConnectionFailedListener is also registered below to
        // notify the activity when it needs to stop making API calls.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        0 /* googleApiClientId used when auto-managing multiple googleApiClients */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this /* ConnectionCallbacks */)
                // Register a connection listener that will notify on disconnect (including ones
                // caused by calling disconnect on the GoogleApiClient).
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        googleApiClientConnectionStateChange(true);
                    }
                })


                .addApi(LocationServices.API)
                // TODO(developer): Specify any additional API Scopes or APIs you need here.
                // The GoogleApiClient will ensure these APIs are available, and the Scopes
                // are approved before invoking the onConnected callbacks.
                .build();
        mGoogleApiClient.connect();
    }

    private void googleApiClientConnectionStateChange(final boolean connected) {
        final Context appContext = this.getApplicationContext();

        // TODO(developer): Kill AsyncTasks, or threads using the GoogleApiClient.

        // Display Toast that isn't dependent on the current activity (in case of a rotation).
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appContext, "Google Api Client has connected:" + connected,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Indicate API calls to Google Play services APIs should be halted.
        googleApiClientConnectionStateChange(false);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {

            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }


    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((GooglemapApp) getActivity()).onDialogDismissed();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates mState = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.

                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(GooglemapApp.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
            break;
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mRequestingLocationUpdates) {
                        startLocationUpdates();
                    }
                } else {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showSnackbar(R.string.permission_denied_explanation,
                            R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
            }
            break;
        }


    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }



    }


    private void startLocationUpdat() {
        // Begin by checking if the device has the necessary location settings.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper());

        updateUI();

    }
    private void updateUI() {

        updateLocationUI();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    private void updateLocationUIMap(GoogleMap mMap) {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }

        Ride=false;

    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
    //    stopLocationUpdates();
        super.onStop();
    }

    @Override
    protected void onPause() {
        // stopLocationUpdates();
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
       // stopService(mServiceIntent);
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (UNIQUE_RIDE != null) {
                String[] pars = UNIQUE_RIDE.split("\\.");
                con = TextUtils.join("", pars);
                mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_got());
            }
            new GetSettings().execute();
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config_URL.REGISTRATION_COMPLETE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config_URL.PUSH_NOTIFICATION));
            NotificationUtils.clearNotifications(getApplicationContext());

        } else {
            Snackbar.make(coordinatorLayout, "No Internet,Connect to internet", Snackbar.LENGTH_INDEFINITE).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ride_later:

                if (_PhoneNo != null) {

                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Offline").setValue("YES");
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                    offline=true;
                    if(marker!=null) {
                        marker.setVisible(false);
                    }


                }
                break;
            case R.id.ride_now:
                offline=false;
                Owner_mobile=null;
                new GetOwnerPhone().execute();
                break;

            case R.id.myLocationCustomButton:
                clicK = true;
                if (My_lat != 0) {

                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(My_lat, My_long)) // Sets the new camera position
                            .zoom(18) // Sets the zoom
                            .bearing(360) // Rotate the camera
                            // Set the camera tilt
                            .build(); // Crea
                    GooglemapApp.this.googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(position));
                    clicK = false;
                    //new StoreLatLong().execute();

                }
                break;
            case R.id.cancel_ride:
                new PostDeleteData().execute();
                break;


            case R.id.confirm_ride:

               // mDatabase.child("Driver_Online").child(_PhoneNo).removeValue();
                if(UNIQUE_RIDE!=null) {
                    proSwipeBtn.setVisibility(View.GONE);
                    open_otp();
                }
                break;

            case R.id.stop_ride:
                    Amb.setAnimation(animslideD);
                    Amb.setVisibility(View.GONE);
                    go=false;
                    ask=false;
                    wait=false;
                    stop=true;
                    googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                        public void onSnapshotReady(Bitmap bitmap) {
                            // Write image to disk

                            final File mediaStorageDir = new File(
                                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(new Date());
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            File destination = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            FileOutputStream fo;
                            try {
                                destination.createNewFile();
                                fo = new FileOutputStream(destination);
                                fo.write(bytes.toByteArray());
                                fo.close();
                                filePath = destination.getPath();
                                new PostBookdata().execute();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //stopLocationUpdates();


                break;
        }

    }

    private void open_otp() {

        final Dialog dialog = new Dialog(GooglemapApp.this);


        dialog.setContentView(R.layout.dialog_otp);
        final EditText Otp=dialog.findViewById(R.id.inputOtp_ride);
        Button Start=dialog.findViewById(R.id.start_ride);
        dialog.setCancelable(true);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(Otp.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please input OTP",Toast.LENGTH_SHORT).show();
                }else {
                    if (Otp.getText().toString().equals(String.valueOf(OTP))) {
                        new PostOtpdata().execute();
                    }else{
                        Toast.makeText(getApplicationContext(),"Wrong OTP",Toast.LENGTH_SHORT).show();
                    }
                        /*
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                                "saddr=" + From_lat + "," + From_long + "&daddr=" + To_lat + "," + To_long + "&sensor=false&units=metric&mode=driving"));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);*/
                }
                dialog.cancel();
            }
        });


        dialog.show();

    }

    private class PostDeleteData  extends AsyncTask<Void, Integer, String> {


        private boolean success;

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
                        .addFormDataPart("mobile", _PhoneNo)
                        .addFormDataPart("unique_id", UNIQUE_RIDE)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.DELETE_RIDE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;

                }else {
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

                ask=false;
                wait=false;
                go=false;
                stop=false;
                animate=false;
                if (UNIQUE_RIDE != null) {
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    String con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).removeValue();
                    if(Driver_phone!=null) {
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                    }
                }
                recreate();

            } else {
                Snackbar.make(coordinatorLayout, "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

            }

        }

    }

    private class PostOtpdata  extends AsyncTask<Void, Integer, String> {


        private boolean success;

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
                        .addFormDataPart("mobile", _PhoneNo)
                        .addFormDataPart("unique_id", UNIQUE_RIDE)
                        .addFormDataPart("OTP", String.valueOf(OTP))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.START_RIDE_AFTER_OTP)
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
                Confirm_ride.setAnimation(animslideD);
                Confirm_ride.setVisibility(View.GONE);
                Stop_ride.setAnimation(animslideU);
                Stop_ride.setVisibility(View.VISIBLE);
                Amb.setAnimation(animslideD);
                Amb.setVisibility(View.GONE);
                Start_ride = true;
                ask=false;
                wait=false;
                got=false;
                go=true;
                if (markerUser != null) {
                    markerUser.setVisible(false);
                }
                if(googleMap!=null){
                    googleMap.setTrafficEnabled(true);
                }
                String[] pars = UNIQUE_RIDE.split("\\.");
                String con = TextUtils.join("", pars);
                if(mTaxes.size()==0){
                    new GetSettings().execute();
                }else{
                    Tax_applied=TextUtils.join("+", mTaxes);
                }
                mDatabase.child("Accepted_Ride").child(con).child("Tax_names").setValue(Tax_applied);
                mDatabase.child("Accepted_Ride").child(con).child("Tax_percentage").setValue(dfto.format(Tota_taxes));
                mDatabase.child("Accepted_Ride").child(con).child("START").setValue("YES");
                mDatabase.child("Accepted_Ride").child(con).child("Distance_travel").setValue(dfto.format(0));
                mDatabase.child("Accepted_Ride").child(con).child("Cost").setValue(String.valueOf(Math.round(0)));
            }else{
                Toast.makeText(getApplicationContext(),"Error! Please try again.",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class PostBookdata  extends AsyncTask<Void, Integer, String> {


        private boolean success;

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
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            try {
                if(filePath!=null && !TextUtils.isEmpty(filePath)) {
                    File sourceFile = new File(filePath);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("mobile", _PhoneNo)
                            .addFormDataPart("Unique_id", UNIQUE_RIDE)
                            .addFormDataPart("image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                            .addFormDataPart("distance", String.valueOf(Distance))
                            .addFormDataPart("cost", String.valueOf(Cost))
                            .addFormDataPart("coupon", Coupon_code)
                            .addFormDataPart("coupon_value", String.valueOf(Coupon_value))
                            .addFormDataPart("Minimum_fare", String.valueOf(Minimum_fare))
                            .addFormDataPart("Hourly_fare", String.valueOf(Hourly_fare))
                            .addFormDataPart("IP", mobileIp)
                            .build();

                    Request request = new Request.Builder()
                            .url(Config_URL.STOP_RIDE)
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
                }
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        protected void onPostExecute(String file_url) {
            if (success) {
                if (UNIQUE_RIDE != null) {

                        Cost=Cost-Coupon_value;
                        double Taotal_cost=Cost+((Cost*Tax_percentage)/100);
                        mDatabase.child("Accepted_Ride").child(con).child("Bill").setValue(dfto.format(Math.round(Taotal_cost)));
                        mDatabase.child("Accepted_Ride").child(con).child("START").setValue("NO");

                    ask=true;wait=false;go=false;stop=false;Accepted=false;
                        Intent i = new Intent(GooglemapApp.this, Success.class);
                        i.putExtra("my_lat", My_lat);
                        i.putExtra("my_long", My_long);
                        i.putExtra("UNIQUEID", UNIQUE_RIDE);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();


                } else {
                    Snackbar.make(coordinatorLayout, "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

                }

            }
        }
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(GooglemapApp.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(GooglemapApp.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }




    @Override
    public void onLocationChanged(Location location) {
        if (location != null && !offline) {
            mCurrentLocation = location;
            My_lat = mCurrentLocation.getLatitude();
            My_long = mCurrentLocation.getLongitude();
            //bearing = location.getBearing();

            if (!first && googleMap != null) {
                first = true;
                CameraPosition googlePlex;
                googlePlex = CameraPosition.builder()
                        .target(new LatLng(My_lat, My_long))
                        .zoom(18) // Sets the zoom
                        .bearing(bearing) // Rotate the camera
                        // Set the camera tilt
                        .build(); // Crea
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

            }


            if (!animate) {

                if (!cancelled) {
                    if (UNIQUE_RIDE != null) {
                        String[] pars = UNIQUE_RIDE.split("\\.");
                        con = TextUtils.join("", pars);

                        mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                        if (marker != null) {
                            if(mPathPolygonPoints.size()>1){
                                //mDatabase.child("Accepted_Ride").child(con).child("Driver_Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                //mDatabase.child("Accepted_Ride").child(con).child("Driver_Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                    //mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                   // mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                    animate = true;
                                    animateCarMove(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                    ROUTE[0] = mPathPolygonPoints.get(1);
                                    if(go){
                                        markerPoints = new ArrayList<LatLng>();
                                        markerPoints.clear();
                                        markerPoints.add(mPathPolygonPoints.get(0));
                                        markerPoints.add(mPathPolygonPoints.get(1));
                                        if (markerPoints.size() == 2 ) {
                                            LatLng origin = markerPoints.get(0);
                                            LatLng dest = markerPoints.get(1);
                                            String url = getDirectionsUrl(origin, dest);
                                            new DownloadTask().execute(url);

                                        }
                                    }

                                    mPathPolygonPoints.clear();
                                    mPathPolygonPoints.add(ROUTE[0]);

                                }
                            }
                        }else{
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .position(mPathPolygonPoints.get(0))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                            mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));

                        }

                        if (stop) {

                            ask = false;
                            wait = false;
                            go = false;
                            stop = false;
                            Accepted = false;
                            Intent i = new Intent(GooglemapApp.this, Success.class);
                            i.putExtra("my_lat", My_lat);
                            i.putExtra("my_long", My_long);
                            i.putExtra("UNIQUEID", UNIQUE_RIDE);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            finish();
                        }
                    } else {

                        if (!Ride) {


                            if (Owner_mobile != null) {
                                    if (!offline) {

                                        mPathPolygonPoints.add(new LatLng(My_lat, My_long));

                                        if (marker != null) {
                                            if(mPathPolygonPoints.size()>1){
                                                // mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                                //mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                                if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                                    animate = true;
                                                    animateCarMove(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                                    ROUTE[0] = mPathPolygonPoints.get(1);
                                                    mPathPolygonPoints.clear();
                                                    mPathPolygonPoints.add(ROUTE[0]);
                                                }
                                            }
                                        }else{
                                            marker = googleMap.addMarker(new MarkerOptions()
                                                    .position(mPathPolygonPoints.get(0))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                        }
                                    }
                            }
                        } else {
                            if (!again ) {
                                if(Minimum_Balance) {
                                    again = true;
                                    noOwner.setText("LOW BALANCE");
                                    noOwner.setVisibility(View.VISIBLE);
                                }else {
                                    noOwner.setVisibility(View.GONE);
                                    again = true;
                                    Intent i = new Intent(GooglemapApp.this, GetRide.class);
                                    if (vehicle_type != null) {
                                        i.putExtra("Vehicle", vehicle_type);
                                    }
                                    i.putExtra("mylat", My_lat);
                                    i.putExtra("mylong", My_long);
                                    startActivity(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private void updateLocationUI() {
        if (mCurrentLocation != null && !offline) {
            My_lat = mCurrentLocation.getLatitude();
            My_long = mCurrentLocation.getLongitude();
            if (!first && googleMap != null) {
                first = true;
                CameraPosition googlePlex;
                googlePlex = CameraPosition.builder()
                        .target(new LatLng(My_lat, My_long))
                        .zoom(18) // Sets the zoom
                        .bearing(360) // Rotate the camera
                        // Set the camera tilt
                        .build(); // Crea
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            }

            if (!animate) {

                if (!cancelled) {
                    if (UNIQUE_RIDE != null) {
                        String[] pars = UNIQUE_RIDE.split("\\.");
                        con = TextUtils.join("", pars);
                        mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                        if (marker != null) {
                            if(mPathPolygonPoints.size()>1){
                                mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                //mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                //mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                //mDatabase.child("Accepted_Ride").child(con).child("Driver_Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                //mDatabase.child("Accepted_Ride").child(con).child("Driver_Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                    animate = true;
                                    animateCarMove(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                    ROUTE[0] = mPathPolygonPoints.get(1);
                                    if(go){
                                        markerPoints = new ArrayList<LatLng>();
                                        markerPoints.clear();
                                        markerPoints.add(mPathPolygonPoints.get(0));
                                        markerPoints.add(mPathPolygonPoints.get(1));
                                        if (markerPoints.size() == 2 ) {
                                            LatLng origin = markerPoints.get(0);
                                            LatLng dest = markerPoints.get(1);
                                            String url = getDirectionsUrl(origin, dest);
                                            new DownloadTask().execute(url);

                                        }
                                    }

                                    mPathPolygonPoints.clear();
                                    mPathPolygonPoints.add(ROUTE[0]);

                                }
                            }
                        }else{
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .position(mPathPolygonPoints.get(0))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                        }

                        if (stop) {

                            ask = false;
                            wait = false;
                            go = false;
                            stop = false;
                            Accepted = false;
                            Intent i = new Intent(GooglemapApp.this, Success.class);
                            i.putExtra("my_lat", My_lat);
                            i.putExtra("my_long", My_long);
                            i.putExtra("UNIQUEID", UNIQUE_RIDE);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            finish();
                        }
                    } else {

                        if (!Ride) {


                            if (Owner_mobile != null) {
                                if (!offline) {

                                    mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                                    if (marker != null) {
                                        if(mPathPolygonPoints.size()>1){
                                            mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                            mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                            // mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                            //mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                            if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                                animate = true;
                                                animateCarMove(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                                ROUTE[0] = mPathPolygonPoints.get(1);
                                                mPathPolygonPoints.clear();
                                                mPathPolygonPoints.add(ROUTE[0]);
                                            }
                                        }
                                    }else{
                                        marker = googleMap.addMarker(new MarkerOptions()
                                                .position(mPathPolygonPoints.get(0))
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                    }
                                }
                            }
                        } else {
                            if (!again ) {
                                if(Minimum_Balance) {
                                    again = true;
                                    noOwner.setText("LOW BALANCE");
                                    noOwner.setVisibility(View.VISIBLE);
                                }else {
                                    noOwner.setVisibility(View.GONE);
                                    again = true;
                                    Intent i = new Intent(GooglemapApp.this, GetRide.class);
                                    if (vehicle_type != null) {
                                        i.putExtra("Vehicle", vehicle_type);
                                    }
                                    i.putExtra("mylat", My_lat);
                                    i.putExtra("mylong", My_long);
                                    startActivity(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private class FirebaseDataListener_ride implements ValueEventListener {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getChildrenCount()!=0) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child!=null) {
                        String key = child.getKey();
                        Object value = child.getValue();
                        if(((Map) value).get("Vehicle")!=null) {
                            String vehicle = ((Map) value).get("Vehicle").toString();
                            if (vehicle_type != null&& vehicle!=null && !offline)
                                if (vehicle.contains(vehicle_type)) {
                                    mDatabase.child("Cancel").child(_PhoneNo).child(key).addValueEventListener(new FirebaseDataListener_cancel());
                                }else{
                                    Ride=false;
                                    if(again) {
                                        again=false;
                                        Intent i = new Intent(getApplicationContext(), GooglemapApp.class);
                                        startActivity(i);

                                    }

                        }
                    }
                }

            }
            }else {
                Ride = false;
                if(again) {
                    again=false;
                    Intent i = new Intent(getApplicationContext(), GooglemapApp.class);
                    startActivity(i);

                }
            }


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    private class FirebaseDataListener_offer implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            if(dataSnapshot.getChildrenCount()!=0) {

                if(dataSnapshot.child("Owner_Phone_no").getValue()!=null){
                    Owner_mobile=(String)dataSnapshot.child("Owner_Phone_no").getValue();
                }else{
                    Owner_mobile=null;
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

         if(UNIQUE_RIDE == null) {
             if (dataSnapshot.getChildrenCount() != 0) {
                 if (dataSnapshot.child("Owner_Phone_no").getValue() != null) {
                     Owner_mobile = (String) dataSnapshot.child("Owner_Phone_no").getValue();
                     if (dataSnapshot.child("Driver_Vehicle").getValue() != null) {
                         vehicle_type = (String) dataSnapshot.child("Driver_Vehicle").getValue();
                         vehicle_No = (String) dataSnapshot.child("Driver_Vehicle_no").getValue();
                         if (dataSnapshot.child("Offline").getValue() != null) {
                             String off = (String) dataSnapshot.child("Offline").getValue();
                             if (marker != null && off.contains("NO")) {
                                 if (marker.getTag() != null) {
                                     offline = false;
                                     mDatabase.child("Ride_Request").addValueEventListener(new FirebaseDataListener_ride());
                                 }
                             } else {
                                 if (off.contains("YES")) {
                                     offline = true;
                                     if (marker != null) {
                                         marker.setVisible(false);
                                     }
                                     noOwner.setVisibility(View.VISIBLE);
                                     noOwner.setText("Offline");
                                 }
                             }
                         }
                     } else {
                         vehicle_type = null;
                         noOwner.setText("No Vehicle");
                         noOwner.setVisibility(View.VISIBLE);
                     }
                 } else {
                     if (marker != null) {
                         marker.setVisible(false);
                     }
                     noOwner.setText("You are not appointed by any owner");
                     noOwner.setVisibility(View.VISIBLE);
                     mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener_offer());
                 }


             } else {
                 vehicle_type = null;
                 Owner_mobile = null;
                 noOwner.setText("You are not appointed by any owner");
                 noOwner.setVisibility(View.VISIBLE);
                 mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener_offer());

             }


         }else{
             String[] pars = UNIQUE_RIDE.split("\\.");
             con = TextUtils.join("", pars);
             mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_got());

         }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    private class FirebaseDataListener_cancel implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(dataSnapshot.getChildrenCount()!=0) {
            }else{
                ask=true;
                Ride=true;
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    private class FirebaseDataListener_got implements ValueEventListener {

        private String Driver_accept,User_accept;
        private double Driver_lat=0,Driver_long=0,User_From_lat=0,User_From_long=0;
        private Marker markerd;
        private LatLng To_latLong;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (UNIQUE_RIDE != null) {
            if (dataSnapshot.getChildrenCount() != 0) {

                        mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(My_lat));
                        mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(My_long));
                        User_mobile = (String) dataSnapshot.child("UserMobile").getValue();
                        Driver_accept = (String) dataSnapshot.child("START").getValue();
                    User_accept = (String) dataSnapshot.child("UserAccept").getValue();
                    if(data_f.getDistance_travel() != null && Distance_travel==0){
                        Distance_travel=Double.parseDouble(data_f.getDistance_travel());
                    }
                    if (Driver_accept != null) {
                        if (Driver_accept.contains("YES")) {
                            wait = false;
                            Start_ride = false;
                            go = true;
                            ask = false;
                            stop = false;
                            got = false;
                            Rl.setAnimation(animslideU);
                            Rl.setVisibility(View.VISIBLE);
                            Confirm_ride.setAnimation(animslideD);
                            Confirm_ride.setVisibility(View.GONE);
                            Stop_ride.setAnimation(animslideU);
                            Stop_ride.setVisibility(View.VISIBLE);
                            Amb.setVisibility(View.GONE);
                            if (markerUser != null) {
                                markerUser.setVisible(false);
                            }
                            if (marker != null) {
                                marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                marker.setVisible(true);

                            } else {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Driver_lat, Driver_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                marker.setVisible(true);
                            }
                            if (googleMap != null) {
                                googleMap.setTrafficEnabled(true);
                            }

                        }  else if (Driver_accept.contains("NO")) {
                            stop=true;
                        }
                    }else{
                        if (User_accept != null) {
                            Rl.setAnimation(animslideU);
                            Rl.setVisibility(View.VISIBLE);
                            Confirm_ride.setText(R.string.startrace);
                            Confirm_ride.setVisibility(View.VISIBLE);
                            Amb.setVisibility(View.GONE);
                            wait = false;
                            Start_ride = true;
                            go = false;
                            ask = false;
                            stop = false;
                            got = false;
                            if (marker != null) {
                                marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                marker.setVisible(true);

                            } else {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Driver_lat, Driver_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                marker.setVisible(true);
                            }

                            if (markerd != null) {
                                markerd.setVisible(true);
                                markerd.setPosition(new LatLng(Double.parseDouble((String) dataSnapshot.child("Book_To_Latitude").getValue()), Double.parseDouble((String) dataSnapshot.child("Book_To_Longitude").getValue())));
                            } else {
                                markerd = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble((String) dataSnapshot.child("Book_To_Latitude").getValue()), Double.parseDouble((String) dataSnapshot.child("Book_To_Longitude").getValue())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_red)));
                                markerd.setVisible(true);
                            }

                            if (markerUser != null) {
                                markerUser.setVisible(true);
                                markerUser.setPosition(new LatLng(User_From_lat, User_From_long));
                            } else {
                                markerUser = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(User_From_lat, User_From_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person)));
                                markerUser.setVisible(true);
                            }
                            SetZoomGot(markerUser, marker);
                            markerPoints = new ArrayList<LatLng>();
                            markerPoints.clear();
                            markerPoints.add(new LatLng(User_From_lat, User_From_long));
                            markerPoints.add(new LatLng(Driver_lat, Driver_long));
                            if (markerPoints.size() == 2 && polylineFinal == null) {
                                LatLng origin = markerPoints.get(0);
                                LatLng dest = markerPoints.get(1);
                                String url = getDirectionsUrl(origin, dest);
                                new DownloadTask().execute(url);

                            }
                        }
                    }
                    if (got) {
                        if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                                dataSnapshot.child("Driver_First_Longitude").getValue() != null &&
                                dataSnapshot.child("User_Latitude").getValue() != null &&
                                dataSnapshot.child("User_Longitude").getValue() != null &&
                                dataSnapshot.child("Book_To_Latitude").getValue() != null &&
                                dataSnapshot.child("Book_To_Longitude").getValue() != null &&
                                dataSnapshot.child("Book_From_Latitude").getValue() != null &&
                                dataSnapshot.child("Book_From_Longitude").getValue() != null) {
                            Driver_lat = Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue());
                            Driver_long = Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue());
                            User_From_lat = Double.parseDouble((String) dataSnapshot.child("User_Latitude").getValue());
                            User_From_long = Double.parseDouble((String) dataSnapshot.child("User_Longitude").getValue());

                            if (!go) {
                                if (marker != null) {
                                    marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                    marker.setVisible(true);

                                } else {
                                    marker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Driver_lat, Driver_long))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                    marker.setVisible(true);
                                }
                                ask = false;
                                wait = true;
                                got = false;
                                go = false;
                                stop = false;
                                Ride = false;
                            }

                        } else {
                            cancelled = true;


                        }
                    }
                    if (wait) {
                        if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                                dataSnapshot.child("Driver_First_Longitude").getValue() != null &&
                                dataSnapshot.child("User_Latitude").getValue() != null &&
                                dataSnapshot.child("User_Longitude").getValue() != null &&
                                dataSnapshot.child("Book_To_Latitude").getValue() != null &&
                                dataSnapshot.child("Book_To_Longitude").getValue() != null &&
                                dataSnapshot.child("Book_From_Latitude").getValue() != null &&
                                dataSnapshot.child("Book_From_Longitude").getValue() != null) {
                            Driver_lat = Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue());
                            Driver_long = Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue());
                            User_From_lat = Double.parseDouble((String) dataSnapshot.child("User_Latitude").getValue());
                            User_From_long = Double.parseDouble((String) dataSnapshot.child("User_Longitude").getValue());

                                if (marker != null) {
                                    marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                    marker.setVisible(true);

                                }



                        } else {
                            cancelled = true;
                        }
                    }
                    if (Start_ride) {
                        if (dataSnapshot.child("Driver_First_Latitude").getValue() != null &&
                                dataSnapshot.child("Driver_First_Longitude").getValue() != null &&
                                dataSnapshot.child("User_Latitude").getValue() != null &&
                                dataSnapshot.child("User_Longitude").getValue() != null) {
                            Driver_lat = Double.parseDouble((String) dataSnapshot.child("Driver_First_Latitude").getValue());
                            Driver_long = Double.parseDouble((String) dataSnapshot.child("Driver_First_Longitude").getValue());
                            User_From_lat = Double.parseDouble((String) dataSnapshot.child("User_Latitude").getValue());
                            User_From_long = Double.parseDouble((String) dataSnapshot.child("User_Longitude").getValue());
                            Driver_accept = (String) dataSnapshot.child("START").getValue();
                            Confirm_ride.setText(R.string.startrace);
                            Confirm_ride.setVisibility(View.VISIBLE);
                            Confirm_ride.setClickable(true);
                            proSwipeBtn.setArrowColor(R.color.driver_type);
                            proSwipeBtn.setVisibility(View.VISIBLE);
                            getWindow().getDecorView().findViewById(R.id.awesome_btn).invalidate();


                            if (!go) {
                                if (marker != null) {
                                    marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                    marker.setVisible(true);

                                }
                                if (markerUser != null) {
                                    markerUser.setVisible(true);
                                    markerUser.setPosition(new LatLng(User_From_lat, User_From_long));
                                }

                            }

                        } else {
                            cancelled = true;
                        }

                        /*markerPoints = new ArrayList<LatLng>();
                        markerPoints.clear();
                        markerPoints.add(new LatLng(User_From_lat, User_From_long));
                        markerPoints.add(new LatLng(Driver_lat, Driver_long));
                        if (markerPoints.size() == 2 ) {
                            LatLng origin = markerPoints.get(0);
                            LatLng dest = markerPoints.get(1);
                            String url = getDirectionsUrl(origin, dest);
                            new DownloadTask().execute(url);

                        }*/

                    }
                    if (go) {
                        data_f = dataSnapshot.getValue(Book.class);
                        assert data_f != null;
                        if (data_f.getDriver_First_Longitude() != null
                                && data_f.getDriver_First_Latitude() != null
                                && data_f.getBook_To_Latitude() != null &&
                                data_f.getBook_To_Longitude() != null &&
                                data_f.getTax_names() != null ) {
                            // Toast.makeText(getApplicationContext(),"4",Toast.LENGTH_SHORT).show();
                            proSwipeBtn.setVisibility(View.GONE);
                            googleMap.setTrafficEnabled(true);

                            Confirm_ride.setVisibility(View.GONE);

                            if (data_f.getCost() != null && data_f.getEstimated_fare() != null) {
                                if (Double.parseDouble(data_f.getEstimated_fare()) < Double.parseDouble(data_f.getCost())) {
                                    Cost = Double.parseDouble(data_f.getCost());
                                } else if (Double.parseDouble(data_f.getEstimated_fare()) > Double.parseDouble(data_f.getCost())) {
                                    Cost = Double.parseDouble(data_f.getEstimated_fare());
                                }
                            }
                            if(data_f.getTax_percentage()!=null) {
                                Tax_percentage = Double.parseDouble(data_f.getTax_percentage());
                                if (data_f.getCoupon_code() != null && data_f.getCoupon_value() != null) {
                                    Coupon_code = data_f.getCoupon_code();
                                    Coupon_value = Double.parseDouble(data_f.getCoupon_value());
                                }
                            }
                            double Taotal_cost =0;
                            Cost = Cost - Coupon_value;

                            Taotal_cost = Cost + ((Cost * Tax_percentage) / 100);


                            mDatabase.child("Accepted_Ride").child(con).child("Bill").setValue(dfto.format(Taotal_cost));

                            if(Distance_travel==0 && mPathPolygonPoints.size()>1){
                                markerPoints = new ArrayList<LatLng>();
                                markerPoints.clear();
                                markerPoints.add(mPathPolygonPoints.get(0));
                                markerPoints.add(mPathPolygonPoints.get(1));
                                if (markerPoints.size() == 2 ) {
                                    LatLng origin = markerPoints.get(0);
                                    LatLng dest = markerPoints.get(1);
                                    String url = getDirectionsUrl(origin, dest);
                                    new DownloadTask().execute(url);

                                }
                            }


                        }
                    }
                }else {
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                    UNIQUE_RIDE = null;
                    Intent i = new Intent(GooglemapApp.this, Splash_screen.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("cencelled", 1);
                    startActivity(i);
                    finish();
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    public void SetZoomGot(final Marker markeruser,final Marker markerCar) {
        if ( googleMap!=null) {
            if (markeruser != null && markerCar != null) {
                ArrayList<Marker> markerAll = new ArrayList<Marker>();
                markerAll.add(markeruser);
                markerAll.add(markerCar);
                if (markerAll.size() != 0 && got) {
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (Marker marker : markerAll) {
                        builder1.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder1.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 120);
                    googleMap.animateCamera(cu);

                }
                LatLng origin = markeruser.getPosition();
                LatLng dest = markerCar.getPosition();
                String url = getDirectionsUrl(origin, dest);
                new DownloadTask().execute(url);
            }
        }
        else{
            mapFragment.getMapAsync(GooglemapApp.this);

        }
    }


    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private int Online=0;
        private String New_Version;
        private int New_Version_Importance=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Tota_taxes=0;
            mTaxes.clear();
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
                    JSONArray Settings = jsonObj.getJSONArray("Settings");
                    JSONArray Version = jsonObj.getJSONArray("Version_driver");
                    JSONArray Taxes = jsonObj.getJSONArray("TAXES");

                    for (int i = 0; i < Taxes.length(); i++) {
                        JSONObject c = Taxes.getJSONObject(i);
                        mTaxes.add(c.getString("Tax_Name"));
                        Tota_taxes = Tota_taxes+(c.getDouble("Tax_Percentage"));
                    }
                    for (int i = 0; i < Settings.length(); i++) {
                        JSONObject c = Settings.getJSONObject(i);
                        Online = c.getInt("Service_Online");
                        Minimum_Distance_in_Meter_for_Tracking=c.getInt("Minimum_Distance_in_Meter_for_Tracking");

                    }
                    for (int i = 0; i < Version.length(); i++) {
                        JSONObject c = Version.getJSONObject(i);
                        New_Version = c.getString("Version");
                        New_Version_Importance=c.getInt("Importance");

                    }

                    JSONArray Master_Vehicle_Type = jsonObj.getJSONArray("Vehicle_Rate");
                    for (int i = 0; i < Master_Vehicle_Type.length(); i++) {
                        JSONObject c = Master_Vehicle_Type.getJSONObject(i);
                        if(vehicle_type!=null) {
                            if (!c.isNull("Vehicle_Type") && c.getString("Vehicle_Type").contains(vehicle_type)) {
                                Minimum_fare = c.getInt("Minimum_Rate");
                                Hourly_fare = c.getInt("Hourly_Rate");
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
                        Snackbar snackbar = Snackbar
                                .make(getWindow().getDecorView().getRootView(), "No internet connection!", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        recreate();
                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isInternetPresent) {
                if (pref.getNewVersion() == null) {
                    pref.setNewVersion(New_Version);
                    if (Online == 1) {
            normal();
                    }
                } else {
                    if (!pref.getNewVersion().matches(New_Version)) {
                        if (New_Version_Importance == 1) {
                            if(!(GooglemapApp.this.isFinishing())) {
                                new AlertDialog.Builder(GooglemapApp.this, R.style.AlertDialogTheme)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("Update HelloCab")
                                        .setMessage("A new version of Hellocab is available!")
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                pref.deleteState();
                                                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(url));
                                                startActivity(i);
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        } else {
                            if(!(GooglemapApp.this.isFinishing())) {
                                new AlertDialog.Builder(GooglemapApp.this, R.style.AlertDialogTheme)
                                        .setTitle("Update HelloCab")
                                        .setMessage("A new version of hellocab is available!")
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Online == 1) {
                                                    normal();
                                                }
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            }

                        }
                    } else {
                        if (Online == 1) {
                       normal();
                        }
                    }
                }

            }else{
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().getRootView(), "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recreate();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        }
    }

    private void normal() {
        Ride=false;
        if(UNIQUE_RIDE!=null){
            Accepted=true;
            ask=false;
            wait=true;
            go=false;
            stop=false;
            Ride=false;
            String[] pars = UNIQUE_RIDE.split("\\.");
            con = TextUtils.join("", pars);
        }

        if (_PhoneNo != null) {
            new GetCustomer().execute();
            if (checkPermissions()) {

                if (mGoogleApiClient.isConnected()) {
                    startLocationUpdates();

                } else {
                    startLocationUpdat();

                }
            } else if (!checkPermissions()) {
                requestPermissions();
            }
            mSensorService = new SensorService(GooglemapApp.this);
            mServiceIntent = new Intent(GooglemapApp.this, mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
              startService(mServiceIntent);
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!isMyServiceRunning(mSensorService.getClass())) {
         stopService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    private class GetCustomer extends AsyncTask<Void, Void, Void> {


        private double Driver_review = 0;
        private double User_From_lat=0,User_From_long=0,User_To_lat=0,User_To_long=0;

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
                    JSONArray Driver = jsonObj.getJSONArray("Driver_Details");
                    JSONArray vehicle = jsonObj.getJSONArray("Vehicle_detail");
                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Now");
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Owner = jsonObj.getJSONArray("Owner_Details");
                    // looping through All Contacts

                    for (int i = 0; i < Driver.length(); i++) {
                        JSONObject c = Driver.getJSONObject(i);

                        if (!c.isNull("Phone_No") && !TextUtils.isEmpty(c.getString("Name"))) {
                            String relation = c.getString("Phone_No");
                            if (relation.matches(_PhoneNo)) {
                                Driver_name = c.getString("Name");
                                Driver_image = c.getString("Photo");
                                Driver_id = c.getInt("ID");
                                Driver_phone = c.getString("Phone_No");
                                Driver_review = c.getDouble("Rating");
                                if(!c.isNull("Owner_ID") ) {

                                            for (int j= 0; j < Owner.length(); j++) {
                                                JSONObject d = Owner.getJSONObject(j);

                                                if (c.getInt("Owner_ID")==d.getInt("ID")) {
                                                    Owner_name = d.getString("Name");
                                                    Owner_image = d.getString("Photo");
                                                    Owner_mobile=d.getString("Phone_No");
                                                    break;
                                                }
                                            }


                                }
                            }

                        }
                    }
                    for (int i = 0; i < ride.length(); i++) {
                        JSONObject c = ride.getJSONObject(i);
                        if (!c.isNull("Driver_ID")) {

                            if (c.getInt("Driver_ID")==(Driver_id)) {
                                OTP = c.getInt("OTP");

                                UNIQUE_RIDE = c.getString("Unique_Ride_Code");
                                User_from = c.getString("From_Address");
                                User_to = c.getString("To_Address");
                                User_From_lat=c.getDouble("From_Latitude");
                                User_From_long=c.getDouble("From_Longitude");
                                User_To_lat=c.getDouble("To_Latitude");
                                User_To_long=c.getDouble("To_Longitude");
                                for (int j = 0; j < User.length(); j++) {
                                    JSONObject d = User.getJSONObject(i);
                                    if (d.getInt("ID")==(c.getInt("User_ID"))) {
                                        u_PhoneNo = d.getString("Phone_No");
                                    }
                                }

                            }

                        }
                    }


                        for (int i = 0; i < vehicle.length(); i++) {
                            JSONObject c = vehicle.getJSONObject(i);
                            if (!c.isNull("Vehicle_No") && c.getInt("Driver_ID")==(Driver_id) ) {
                                    if(c.getInt("Minimum_Balance_Status")==0) {
                                        Minimum_Balance=false;

                                    }else{
                                        Minimum_Balance=true;

                                    }
                                vehicle_type = c.getString("Vehicle_Type");
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
            if (Driver_name != null && !TextUtils.isEmpty(Driver_name)) {
                if (Driver_review != 0) {
                    MyStar.setVisibility(View.VISIBLE);
                    txtName.setText(String.valueOf(Driver_review));
                } else {
                    MyStar.setVisibility(View.GONE);
                    txtName.setText(Driver_name);
                }
            } else {
                MyStar.setVisibility(View.GONE);
                txtName.setText(_PhoneNo);
            }
            Picasso.Builder builder = new Picasso.Builder(GooglemapApp.this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });
            builder.build().load(Driver_image).into(profile_image);

            if(Owner_mobile!=null) {
                    noOwner.setAnimation(animslideD);
                    noOwner.setVisibility(View.GONE);
                    if (UNIQUE_RIDE != null) {
                        String[] pars = UNIQUE_RIDE.split("\\.");
                        con = TextUtils.join("", pars);
                        ask = false;
                        wait = false;
                        go = false;
                        stop = false;
                        Ride = false;
                        got = true;
                        first=true;
                        Lr.setAnimation(animslideD);
                        Lr.setVisibility(View.GONE);
                        Rl.setAnimation(animslideU);
                        Rl.setVisibility(View.VISIBLE);
                        Confirm_ride.setText("Please wait for confirmation");
                        Confirm_ride.setClickable(false);
                        Amb.setVisibility(View.VISIBLE);
                        if(Minimum_fare==0){
                            new GetSettings().execute();
                        }
                        markerPoints = new ArrayList<LatLng>();
                        markerPoints.clear();
                        markerPoints.add(new LatLng(User_From_lat, User_From_long));
                        markerPoints.add(new LatLng(User_To_lat, User_To_long));

                        if (markerPoints.size() == 2) {
                            LatLng origin = markerPoints.get(0);
                            LatLng dest = markerPoints.get(1);
                            String url = getDirectionsUrl(origin, dest);
                            new DownloadTask().execute(url);

                        }
                        mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_got());
                    }else {
                        if (vehicle_type != null) {
                            if (marker != null) {
                                marker.setVisible(true);
                                marker.setPosition(new LatLng(My_lat, My_long));
                                marker.setTag("Driver");
                            } else {
                                if (vehicle_type.contains("SEDAN")) {
                                    mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                                    marker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(My_lat, My_long))

                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                    marker.setTag("Driver");
                                } else if (vehicle_type.contains("PICKUP")) {
                                    mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_truck);
                                    marker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(My_lat, My_long))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck)));
                                    marker.setTag("Driver");
                                } else if (vehicle_type.contains("SUV")) {
                                    mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_suv);
                                    marker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(My_lat, My_long))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_suv)));
                                    marker.setTag("Driver");

                                } else if (vehicle_type.contains("MINI")) {
                                    mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                                    marker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(My_lat, My_long))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                    marker.setTag("Driver");

                                } else if (vehicle_type.contains("MICRO")) {
                                    mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                                    marker = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(My_lat, My_long))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                    marker.setTag("Driver");

                                }
                            }

                        }

                        mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener());
                        //mDatabase.child("Ride_Request").addValueEventListener(new FirebaseDataListener_ride());
                    }
            }
            else {
                        noOwner.setText("You are not appointed by any owner");
                        noOwner.setVisibility(View.VISIBLE);
                        mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener_offer());
                    }
            }
        }


    @SuppressLint("StaticFieldLeak")
    private class GetOwnerPhone extends AsyncTask<Void, Void, Void> {




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



                    JSONArray drivers = jsonObj.getJSONArray("Driver_Details");
                    JSONArray vehicle = jsonObj.getJSONArray("Vehicle_detail");
                    JSONArray Owner = jsonObj.getJSONArray("Owner_Details");
                    for (int i = 0; i < drivers.length(); i++) {
                        JSONObject c = drivers.getJSONObject(i);
                        if(c.getString("Phone_No").contains(_PhoneNo)) {
                            if (!c.isNull("Owner_ID")) {
                                for (int j = 0; j < Owner.length(); j++) {
                                    JSONObject d = Owner.getJSONObject(j);

                                    if (c.getInt("Owner_ID") == d.getInt("ID")) {
                                        Owner_name = d.getString("Name");
                                        Owner_image = d.getString("Photo");
                                        Owner_mobile = d.getString("Phone_No");
                                        Driver_id=c.getInt("ID");
                                        break;
                                    }
                                }

                            }

                        }

                    }
                    for (int i = 0; i < vehicle.length(); i++) {
                        JSONObject c = vehicle.getJSONObject(i);
                        if (!c.isNull("Vehicle_No") && c.getInt("Driver_ID")==(Driver_id) ) {
                            if(c.getInt("Minimum_Balance_Status")==0) {
                                Minimum_Balance=false;

                            }else{
                                Minimum_Balance=true;

                            }
                            vehicle_type = c.getString("Vehicle_Type");
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


            if (Owner_mobile != null) {
                    noOwner.setAnimation(animslideD);
                    noOwner.setVisibility(View.GONE);
                    if (My_lat != 0 && UNIQUE_RIDE == null && vehicle_type != null) {

                        if (marker != null) {
                            marker.setVisible(true);
                            marker.setPosition(new LatLng(My_lat, My_long));
                            marker.setTag("Driver");
                        } else {
                            if (vehicle_type.contains("SEDAN")) {
                                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(My_lat, My_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            } else if (vehicle_type.contains("PICKUP")) {
                                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_truck);
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(My_lat, My_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck)));
                            } else if (vehicle_type.contains("SUV")) {
                                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_suv);
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(My_lat, My_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_suv)));

                            } else if (vehicle_type.contains("MINI")) {
                                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(My_lat, My_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));

                            } else if (vehicle_type.contains("MICRO")) {
                                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(My_lat, My_long))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));

                            }
                        }


                        mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(My_lat));
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(My_long));
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(My_lat));
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(My_long));

                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle").setValue(vehicle_type);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle_no").setValue(vehicle_No);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(_PhoneNo);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Photo").setValue(Driver_image);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Offline").setValue("NO");
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Name").setValue(Driver_name);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(c.getTime());
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Online_Date").setValue(formattedDate);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(Owner_mobile);
                        if (marker != null) {
                            marker.setVisible(true);
                            marker.setPosition(new LatLng(My_lat, My_long));
                        }
                        CameraPosition googlePlex = CameraPosition.builder()
                                .target(new LatLng(My_lat, My_long))
                                .zoom(16) // Sets the zoom
                                .bearing(360) // Rotate the camera
                                // Set the camera tilt
                                .build(); // Crea


                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                    } else {
                        if (checkPermissions()) {

                            if (mGoogleApiClient.isConnected()) {
                                startLocationUpdates();

                            } else {
                                startLocationUpdat();

                            }
                        } else if (!checkPermissions()) {
                            requestPermissions();
                        }
                    }
                    mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener());

            if (Minimum_Balance)  {
                if(!(GooglemapApp.this.isFinishing())) {
                    new AlertDialog.Builder(GooglemapApp.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Low Balance")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String phone = "917002608241";
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
                }

                } else {
                    noOwner.setText("You are not appointed by any owner");
                    noOwner.setVisibility(View.VISIBLE);
                    mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener_offer());


            }
        }

    }

    public void onMapReady(final GoogleMap map) {
        if (map == null) {
            return;
        } else {
                googleMap = map;
                getLocationPermission();
                updateLocationUIMap(googleMap);
                CameraPosition googlePlex;

                if (My_lat != 0) {
                    googlePlex = CameraPosition.builder()
                            .target(new LatLng(My_lat, My_long))
                            .zoom(18) // Sets the zoom
                            .bearing(360) // Rotate the camera
                            // Set the camera tilt
                            .build(); // Crea


                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                }else{
                    clicK=true;
                    startLocationUpdates();
                }

                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    private void setUpNavigationView() {

        //Setting Navigation View Item Selected Listener to handle the bean click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            // This method will trigger on bean Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which bean was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        Intent j = new Intent(GooglemapApp.this, GooglemapApp.class);
                        j.putExtra("my_lat", My_lat);
                        j.putExtra("my_long", My_long);
                        startActivity(j);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_gallery:
                        navItemIndex = 1;
                        stopLocationUpdates();
                        Intent o = new Intent(GooglemapApp.this, Ride_later_dates.class);
                        o.putExtra("mylat", My_lat);
                        o.putExtra("mylong", My_long);
                        startActivity(o);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_documents:
                        drawer.closeDrawers();
                        Intent o1 = new Intent(GooglemapApp.this,Owner_driver_add.class);
                        o1.putExtra("Driver","DRIVER");
                        o1.putExtra("Driver_no",_PhoneNo);
                        o1.putExtra("my_lat",My_lat);
                        o1.putExtra("my_long",My_long);
                        o1.putExtra("reg",regId);
                        startActivity(o1);
                        finish();
                        break;
                    case R.id.nav_refer:
                        drawer.closeDrawers();

                    break;
                    case R.id.nav_rate:
                        drawer.closeDrawers();

                    break;

                    case R.id.logout:

                        drawer.closeDrawers();
                       break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the bean is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notification, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_notify:



            default:
                return super.onOptionsItemSelected(item);
        }
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);



    }



   private String getDirectionsUrl(LatLng origin,LatLng dest){



        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }



    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{


        private float distance=0;
        private double Total_cost=0;

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String duration = "";
            Random rand = new Random();

            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        String pars=(String)point.get("distance");
                        String[]pars1=pars.split(" ");
                        distance = Float.parseFloat(pars1[0])/1000;

                        if(go ) {
                            if(con!=null) {
                                Distance_travel =Distance_travel +distance;
                                Total_cost = (Minimum_fare + (Distance_travel) * Hourly_fare);
                                mDatabase.child("Accepted_Ride").child(con).child("Distance_travel").setValue(dfto.format(Distance_travel));
                                mDatabase.child("Accepted_Ride").child(con).child("Cost").setValue(dfto.format(Total_cost));
                            }
                        }
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        if(wait||got){
                            if(marker!=null) {
                                //marker.showInfoWindow();
                                marker.setTitle(duration);
                            }
                        }else{
                            marker.hideInfoWindow();
                        }
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }



                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.startCap(new SquareCap());
                lineOptions.endCap(new SquareCap());
                lineOptions.jointType(ROUND);
                lineOptions.color(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256) ));



            }
            if(lineOptions!=null&& !got) {
                if(polylineFinal==null) {
                    polylineFinal = googleMap.addPolyline(lineOptions);
                }
            }


        }
        public double roundTwoDecimals(double d)
        {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Double.valueOf(twoDForm.format(d));
        }
        public void SetZoomlevel(final ArrayList<LatLng> listLatLng) {
            if ( googleMap!=null && marker!=null && markerUser!=null) {
                ArrayList<Marker> markerAll = new ArrayList<Marker>();
                if (listLatLng != null && listLatLng.size() > 1) {
                        markerAll.add(marker);
                        markerAll.add(markerUser);

                    if(markerAll.size()!=0) {
                        LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                        for (Marker marker : markerAll) {
                            builder1.include(marker.getPosition());
                        }

                        LatLngBounds bounds = builder1.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
                        googleMap.animateCamera(cu);
                    }
                }

            }else{
                mapFragment.getMapAsync(GooglemapApp.this);

            }

        }
    }
    protected Marker createMarker1(double latitude, double longitude) {
        Marker Marker_;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude,longitude));
        Marker_=googleMap.addMarker(markerOptions);
        Marker_.setVisible(false);
        return Marker_;
    }

    private void animateCarMove(final Marker anmarker, final LatLng beginLatLng, final LatLng endLatLng) {
         final Handler handler = new Handler();
         final long startTime = SystemClock.uptimeMillis();
         final Interpolator interpolator = new LinearInterpolator();
        float angleDeg1 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng,endLatLng));
        float direction = (angleDeg1 > 0) ? 180 : 0;
        Location location = new Location("Test");
        location.setLatitude(beginLatLng.latitude);
        location.setLongitude(beginLatLng.longitude);
        Location location1 = new Location("Test");
        location1.setLatitude(endLatLng.latitude);
        location1.setLongitude(endLatLng.longitude);
        bearing=location.bearingTo(location1);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(endLatLng)
                .bearing(direction)
                .zoom(18)
                .build(); // Crea
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
        handler.post(new Runnable() {



            @Override
             public void run() {
                 // calculate phase of animation
                     long elapsed = SystemClock.uptimeMillis() - startTime;
                     float t = interpolator.getInterpolation((float) elapsed / 3000);
                     // calculate new position for marker
                     double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                     double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                     if (Math.abs(lngDelta) > 180) {
                         lngDelta -= Math.signum(lngDelta) * 360;
                     }
                 double lng = lngDelta * t + beginLatLng.longitude;
                 matrix.setRotate(anmarker.getRotation());
                 float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng,new LatLng(lat,lng)));
                 float angleDeg1 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng,endLatLng));
                 float direction = (angleDeg1 > 0) ? angleDeg+180 : angleDeg;
                 matrix.postRotate(direction);
                 anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
                 animate=true;
                 anmarker.setPosition(new LatLng(lat,lng));
                 anmarker.setAnchor(0.5f, 0.5f);
                    if (t < 1.0) {
                         // Post again 16ms later.
                         handler.postDelayed(this,16);
                     } else {
                        animate=false;
                        float angleDeg2 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng,new LatLng(lat,lng)));
                        float angleDeg3 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng,endLatLng));
                        float direction1 = (angleDeg3 > 0) ? angleDeg2+180 : angleDeg2;
                        //matrix.postRotate(direction1);
                        //anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
                    }

             }
         });
    }




}
