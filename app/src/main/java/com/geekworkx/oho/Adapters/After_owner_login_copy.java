package com.geekworkx.oho.Adapters;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.oho.BuildConfig;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Drawer.Ride_later_dates;
import com.geekworkx.oho.FCM.NotificationUtils;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.Model.Book;
import com.geekworkx.oho.Model.Vehicle;
import com.geekworkx.oho.R;
import com.geekworkx.oho.Services.SensorService;
import com.geekworkx.oho.Splash_screen.Splash_screen;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.Album;
import com.geekworkx.oho.helper.HttpHandler;
import com.geekworkx.oho.helper.RecyclerTouchListener;
import com.geekworkx.oho.owner.Add_vehicle_details;
import com.geekworkx.oho.owner.Owner_driver_add;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import static java.lang.System.out;

/**
 * Created by parag on 05/01/18.
 */

public class After_owner_login_copy extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener, Animation.AnimationListener, View.OnClickListener {

    private static final String TAG = GooglemapApp.class.getSimpleName();
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final int M_MAX_ENTRIES = 5;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static final LatLng[] ROUTE = new LatLng[2];
    double My_lat, My_long;
    DecimalFormat dft = new DecimalFormat("0.0000000000");
    DecimalFormat dfto = new DecimalFormat("0.00");
    private GoogleApiClient mGoogleApiClient;
    private boolean Got_amb = false, Got_suv = false, Got_pickup = false, Got_mini = false, Got_sedan = false, Got_micro = false;
    private String Unique_id;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private TextView txtName;
    private ImageView Edit_profile;
    private CircleImageView profile_image;
    private Toolbar toolbar;
    private Boolean ask = false, wait = false, go = false, stop = false, Accepted = false;
    private String _PhoneNo;
    private PrefManager pref;
    private String USER;
    private int navItemIndex = 0;
    private GoogleMap googleMap;
    private CustomMapFragment mapFragment;
    private ProgressBar progressBar;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private boolean Ride = false;
    private String resultText;
    private String mLastUpdateTime;
    private double MyLat = 0, MyLong = 0;
    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private String Appversion = "3";
    private Book data_f = new Book();
    private ImageView myLocationButton;
    private String Driver_name, Driver_image;
    private Marker marker;
    private Bitmap mMarkerIcon;
    private LinearLayout Ride_later, Ride_now;
    private int mIndexCurrentPoint = 0;
    private boolean hasBeenInitialized;
    private FirebaseApp apptt;
    private DatabaseReference mDatabase;
    private Book data_book = new Book();
    private String vehicle_type;
    private String Name;
    private boolean clicK = false;
    private RecyclerView Drivers;
    private RecyclerView Vehicles;
    private String Owner_name, Owner_image, Owner_mobile;
    private String IDENTIFICATION;
    private ArrayList<Album> mItems = new ArrayList<Album>();
    private ArrayList<Vehicle> mItems2 = new ArrayList<Vehicle>();
    private String WHO;
    private BottomNavigationView bottomNavigationView;
    private Animation animFadein, animFadeout;
    private String Driver_selected;
    private String Vehicle_no;
    private String Driver_identity;
    private TextView Heading_;
    private String Vehicle_image;
    private String Vehicle_selected;
    private boolean Owner_is_driver = false;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private boolean WHERE = false;
    private ArrayList<LatLng> Longs = new ArrayList<LatLng>();
    private RelativeLayout R1;
    private Handler handler;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String regId;
    private int Online = 0, New_Version_Importance = 0;
    private String New_Version;
    private int Owner_ID = 0;
    private int Driver_ID = 0;
    private String Owner_Pan_Card, Owner_Verified_By;
    private String mobileIp;
    private boolean new_driver = false;
    private TextView noOwner, noOffline;
    private String UNIQUE_RIDE;
    private String con;
    private LinearLayout Rl, Lr;
    private Button Confirm_ride, Stop_ride;
    private DotProgressBar Amb;
    private boolean offline = false;
    private String filePath;
    private Animation animslideD;
    private Animation animslideU;
    private Marker markerUser;
    private boolean Start_ride = false, cancelled = false;
    private Polyline polylineFinal;
    private PolylineOptions lineOptions = null;
    private List<LatLng> mPathPolygonPoints = new ArrayList<LatLng>();
    private List<LatLng> mPathPolygonPoint = new ArrayList<LatLng>();
    private ArrayList<LatLng> markerPoints;
    private String vehicle_no;
    private Marker markerd;
    private int OTP = 0;
    private double Distance_travel = 0;
    private int Minimum_fare = 0;
    private int Hourly_fare = 0;
    private double Cost = 0, Taotal_cost = 0;
    private boolean first = false;
    private boolean again = false;
    private String Coupon_code = "";
    private double Coupon_value = 0;
    private SensorService mSensorService;
    private Intent mServiceIntent;
    private int Minimum_Distance_in_Meter_for_Tracking = 0;
    private boolean got = false;
    private boolean removeMarker = false;
    private ArrayList<String> mTaxes = new ArrayList<String>();
    private double Tota_taxes = 0;
    private String Tax_applied;
    private double Tax_percentage = 0;
    private boolean Minimum_Balance = false;
    private boolean animate = false;
    private Matrix matrix1;
    private String User_mobile;
    private boolean msg = false;
    private ProSwipeButton proSwipeBtn;
    private boolean drawn = false;

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        UNIQUE_RIDE = i.getStringExtra("UNIQUE_RIDE");
        setContentView(R.layout.after_owner_login);
        Ride_later = findViewById(R.id.ride_later);
        Ride_now = findViewById(R.id.ride_now);
        Rl = findViewById(R.id.ride_detail);
        Confirm_ride = findViewById(R.id.confirm_ride);
        Lr = findViewById(R.id.linearLayout);
        Amb = findViewById(R.id.dot_progress_bar_amb);
        Stop_ride = findViewById(R.id.stop_ride);
        Stop_ride.setOnClickListener(this);
        Ride_later.setOnClickListener(this);
        Ride_now.setOnClickListener(this);
        Confirm_ride.setOnClickListener(this);
        noOwner = findViewById(R.id.textnoOwner);
        noOffline = findViewById(R.id.textnoOffline);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cor_home_owner);
        proSwipeBtn = (ProSwipeButton) findViewById(R.id.awesome_btn);
        R1 = findViewById(R.id.rlMapLayout_owner);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_owner);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name_profile);
        Edit_profile = (ImageView) navHeader.findViewById(R.id.edit_profile);
        profile_image = (CircleImageView) navHeader.findViewById(R.id.img_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_owner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpNavigationView();
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        Name = user.get(PrefManager.KEY_NAME);
        WHO = user.get(PrefManager.KEY_WHO);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3_owner);
        Heading_ = findViewById(R.id.d2);
        Drivers = findViewById(R.id.owner_drivers);
        Vehicles = findViewById(R.id.owner_vehicle);
        cd = new ConnectionDetector(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myLocationButton = (ImageView) findViewById(R.id.myLocationCustomButton);
        myLocationButton.setOnClickListener(this);
        drawer.closeDrawers();

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadein.setAnimationListener(this);

        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animFadeout.setAnimationListener(this);

        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
        bottomNavigationView.setVisibility(View.GONE);

        handler = new Handler();
        mapFragment = (CustomMapFragment) getFragmentManager()
                .findFragmentById(R.id.map_owner);

        mapFragment.getMapAsync(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        rebuildGoogleApiClient();
        //mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_car);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
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

        displayFirebaseRegId();
        mobileIp = getMobileIPAddress();
        if (TextUtils.isEmpty(mobileIp)) {
            mobileIp = getWifiIPAddress();
        }
        animslideD = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down1);
        animslideU = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up1);
        matrix1 = new Matrix();

        mDatabase.child("Driver_Online").child(_PhoneNo).addValueEventListener(new FirebaseDataListener());
        mDatabase.child("Driver_Online").addValueEventListener(new FirebaseDataListener_s());
        mDatabase.child("Ride_Request").addValueEventListener(new FirebaseDataListener_ride());
        proSwipeBtn.setVisibility(View.GONE);

        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (User_mobile != null) {
                            new getFCM().execute(User_mobile);

                        }
                    }
                }, 2000);
            }
        });
    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ride_later:

                if (_PhoneNo != null) {
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Offline").setValue("YES");
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                    offline = true;
                    if (markers.size() != 0) {
                        for (int i = 0; i < markers.size(); i++) {
                            if (markers.get(i).getSnippet().contains(_PhoneNo)) {
                                markers.get(i).setVisible(false);
                            }
                        }
                    }
                }
                break;
            case R.id.ride_now:
                offline = false;
                if (My_lat != 0 && UNIQUE_RIDE == null && vehicle_type != null) {

                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(My_lat));
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(My_long));
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(My_lat));
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(My_long));

                    if (vehicle_type != null) {
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle").setValue(vehicle_type);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle_no").setValue(vehicle_no);
                    }
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(_PhoneNo);
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Photo").setValue(Driver_image);
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Offline").setValue("NO");
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Name").setValue(Owner_name);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Online_Date").setValue(formattedDate);

                    if (Owner_mobile != null) {
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(Owner_mobile);

                    }
                    if (marker != null) {
                        marker.setPosition(new LatLng(My_lat, My_long));
                    }
                    CameraPosition googlePlex = CameraPosition.builder()
                            .target(new LatLng(My_lat, My_long))
                            .zoom(16) // Sets the zoom
                            .bearing(360) // Rotate the camera
                            .build(); // Crea
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                    if (markers.size() != 0) {
                        for (int i = 0; i < markers.size(); i++) {
                            markers.get(i).setVisible(true);
                           /* if(markers.get(i).getSnippet().contains(_PhoneNo)) {

                            }*/
                        }
                    }

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
                break;

            case R.id.myLocationCustomButton:
                clicK = true;
                if (My_lat != 0) {

                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(My_lat, My_long)) // Sets the new camera position
                            .zoom(19) // Sets the zoom
                            .bearing(360) // Rotate the camera
                            // Set the camera tilt
                            .build(); // Crea
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(position));
                    clicK = false;
                } else {
                    new FetchCordinates().execute();
                }
                break;

            case R.id.confirm_ride:

                if (UNIQUE_RIDE != null) {
                    open_otp();

                }
                break;
            case R.id.stop_ride:
                Amb.setAnimation(animslideD);
                Amb.setVisibility(View.GONE);
                go = false;
                ask = false;
                wait = false;
                stop = true;
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

        final Dialog dialog = new Dialog(After_owner_login_copy.this);


        dialog.setContentView(R.layout.dialog_otp);
        final EditText Otp = dialog.findViewById(R.id.inputOtp_ride);
        Button Start = dialog.findViewById(R.id.start_ride);
        dialog.setCancelable(false);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(Otp.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please input OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (Otp.getText().toString().equals(String.valueOf(OTP))) {
                        new PostOtpdata().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
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

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mPathPolygonPoints.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));


                updateLocationUI();
            }
        };
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLastKnownLocation.setAccuracy(0.5f);

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
                .addOnConnectionFailedListener(new OnConnectionFailedListener() {
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
                Toast.makeText(appContext, "Something wentb wrong! Pleases try later." + connected,
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
        GooglemapApp.ErrorDialogFragment dialogFragment = new GooglemapApp.ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        if (map == null) {
            return;
        } else {
            googleMap = map;

            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(true);

            CameraPosition googlePlex;

            googlePlex = CameraPosition.builder()
                    .target(new LatLng(My_lat, My_long))
                    .zoom(17) // Sets the zoom
                    .bearing(360) // Rotate the camera
                    // Set the camera tilt
                    .build(); // Crea


            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(After_owner_login_copy.this);
            googleMap.setInfoWindowAdapter(adapter);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }


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
            mSensorService = new SensorService(After_owner_login_copy.this);
            mServiceIntent = new Intent(After_owner_login_copy.this, mSensorService.getClass());
            bottomNavigationView.setOnNavigationItemSelectedListener
                    (new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.action_item1:

                                    if (Vehicle_no == null) {
                                        mItems.clear();
                                        mItems2.clear();
                                        Lr.setAnimation(animslideD);
                                        Lr.setVisibility(View.GONE);
                                        bottomNavigationView.setAnimation(animslideU);
                                        bottomNavigationView.setVisibility(View.VISIBLE);
                                        new GetListCar().execute();
                                    } else {
                                        open_vehicle_dialog(Driver_image, Vehicle_image, Driver_name, Vehicle_no, Driver_selected);
                                    }

                                    bottomNavigationView.setAnimation(animslideD);
                                    bottomNavigationView.setVisibility(View.GONE);

                                    break;

                                case R.id.action_item2:
                                    if (Driver_selected.matches(_PhoneNo)) {
                                        Intent o = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                                        o.putExtra("Driver", "OWNER");
                                        o.putExtra("Driver_no", _PhoneNo);
                                        o.putExtra("my_lat", My_lat);
                                        o.putExtra("my_long", My_long);
                                        o.putExtra("reg", regId);
                                        startActivity(o);
                                        finish();
                                    } else {
                                        Intent o = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                                        o.putExtra("Driver", "DRIVER");
                                        o.putExtra("Driver_no", Driver_selected);
                                        o.putExtra("my_lat", My_lat);
                                        o.putExtra("my_long", My_long);
                                        o.putExtra("reg", regId);
                                        startActivity(o);
                                        finish();
                                    }
                                    bottomNavigationView.setAnimation(animslideD);
                                    bottomNavigationView.setVisibility(View.GONE);
                                    break;
                                case R.id.action_item3:
                                    if (Driver_selected != null && Owner_ID != 0) {
                                        if (!Owner_is_driver) {
                                            if (!(After_owner_login_copy.this.isFinishing())) {
                                                new AlertDialog.Builder(After_owner_login_copy.this)
                                                        .setIcon(R.mipmap.ic_launcher)
                                                        .setTitle("Are you sure!")
                                                        .setMessage("Remove " + Driver_name + " from employment?")
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                new DeleteDriver().execute();
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
                                        } else {
                                            if (!(After_owner_login_copy.this.isFinishing())) {
                                                new AlertDialog.Builder(After_owner_login_copy.this)
                                                        .setIcon(R.mipmap.ic_launcher)
                                                        .setTitle("Are you sure!")
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                new DeleteDriver().execute();
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
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Select a driver first", Toast.LENGTH_SHORT).show();
                                    }
                                    bottomNavigationView.setAnimation(animslideD);
                                    bottomNavigationView.setVisibility(View.GONE);
                                    break;
                            }

                            return true;
                        }
                    });

            Vehicles.addOnItemTouchListener(
                    new RecyclerTouchListener(After_owner_login_copy.this, Vehicles,
                            new RecyclerTouchListener.OnTouchActionListener() {
                                public boolean Me;

                                @Override
                                public void onClick(View view, final int position) {
                                    if (Driver_selected != null) {
                                        if (!mItems2.get(position).getVehicle_no(position).contains("Add Vehicle")) {
                                            if (mItems2.get(position).getMinimum_Balance_Status(position) == 0) {
                                                Vehicle_selected = mItems2.get(position).getVehicle_no(position);
                                                vehicle_type = mItems2.get(position).getVehicle_type(position);
                                                new UpdateVehicle().execute(Driver_selected, mItems2.get(position).getVehicle_no(position));
                                            } else {
                                                if (!(After_owner_login_copy.this.isFinishing())) {
                                                    new AlertDialog.Builder(After_owner_login_copy.this)
                                                            .setIcon(R.mipmap.ic_launcher)
                                                            .setTitle(mItems.get(position).getDriver_Name(position))
                                                            .setMessage("Balance low ")
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

                                            Intent o = new Intent(After_owner_login_copy.this, Add_vehicle_details.class);
                                            o.putExtra("Driver", Driver_selected);
                                            startActivity(o);

                                        }
                                    } else {
                                        Intent o = new Intent(After_owner_login_copy.this, Add_vehicle_details.class);
                                        o.putExtra("Driver", _PhoneNo);
                                        startActivity(o);

                                    }

                                }

                                @Override
                                public void onRightSwipe(View view, int position) {

                                }

                                @Override
                                public void onLeftSwipe(View view, int position) {

                                }
                            }));

            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config_URL.REGISTRATION_COMPLETE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config_URL.PUSH_NOTIFICATION));
            NotificationUtils.clearNotifications(getApplicationContext());


            Drivers.addOnItemTouchListener(
                    new RecyclerTouchListener(After_owner_login_copy.this, Drivers,
                            new RecyclerTouchListener.OnTouchActionListener() {
                                public boolean Me;

                                @Override
                                public void onClick(View view, final int position) {


                                    if (mItems.size() == 1) {
                                        Heading_.setText("No driver appointed");
                                    }
                                    if (mItems.size() != 0) {
                                        if (mItems.get(position).getDriver_Name(position).contains("Driver")) {
                                            noOwner.setVisibility(View.GONE);
                                            bottomNavigationView.setAnimation(animslideD);
                                            bottomNavigationView.setVisibility(View.GONE);
                                            Rl.setAnimation(animslideD);
                                            Rl.setVisibility(View.GONE);
                                            Lr.setAnimation(animslideD);
                                            Lr.setVisibility(View.GONE);
                                            if (!Owner_is_driver) {
                                                open_self();
                                            } else {
                                                open_list();
                                            }


                                        } else {

                                            if (TextUtils.isEmpty(mItems.get(position).getVerify_Driver(position))) {
                                                bottomNavigationView.setVisibility(View.GONE);
                                                if (!(After_owner_login_copy.this.isFinishing())) {
                                                    new AlertDialog.Builder(After_owner_login_copy.this)
                                                            .setIcon(R.mipmap.ic_launcher)
                                                            .setTitle(mItems.get(position).getDriver_Name(position))
                                                            .setMessage("Driver is not verified yet. Contact HelloCab")
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
                                            } else {
                                                if (mItems.get(position).getMinimum_Balance_Status(position) == 1) {
                                                    if (!(After_owner_login_copy.this.isFinishing())) {
                                                        new AlertDialog.Builder(After_owner_login_copy.this)
                                                                .setIcon(R.mipmap.ic_launcher)
                                                                .setTitle(mItems.get(position).getDriver_Name(position))
                                                                .setMessage("Balance low ")
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
                                                } else {

                                                    if (marker != null) {
                                                        marker.setVisible(false);
                                                    }
                                                    if (markerUser != null) {
                                                        marker.setVisible(false);
                                                    }

                                                    bottomNavigationView.setAnimation(animslideU);
                                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                                    Rl.setVisibility(View.GONE);
                                                    Driver_name = mItems.get(position).getDriver_Name(position);
                                                    Driver_selected = mItems.get(position).getDriver_Phone_No(position);
                                                    Driver_identity = mItems.get(position).getUnique_Id(position);
                                                    Driver_image = mItems.get(position).getDriver_Photo(position);
                                                    if (!TextUtils.isEmpty(mItems.get(position).getVehicle_Type(position))) {
                                                        Vehicle_no = mItems.get(position).getVehicle_No(position);
                                                        Vehicle_image = mItems.get(position).getVehicle_Image_1(position);
                                                        vehicle_type = mItems.get(position).getVehicle_Type(position);
                                                    }
                                                    if (Vehicle_no == null) {
                                                        if (Driver_selected.contains(_PhoneNo)) {
                                                            noOwner.setVisibility(View.VISIBLE);
                                                            noOwner.setText("No vehicle assigned to you");
                                                        } else {
                                                            noOwner.setVisibility(View.VISIBLE);
                                                            noOwner.setText("No vehicle assigned to " + Driver_name);
                                                        }
                                                    } else {
                                                        noOwner.setAnimation(animslideD);
                                                        noOwner.setVisibility(View.GONE);
                                                        if (Driver_selected.contains(_PhoneNo)) {
                                                            if (!offline) {
                                                                bottomNavigationView.setAnimation(animslideD);
                                                                bottomNavigationView.setVisibility(View.GONE);
                                                                Lr.setAnimation(animslideU);
                                                                Lr.setVisibility(View.VISIBLE);
                                                            } else {
                                                                Lr.setAnimation(animslideU);
                                                                Lr.setVisibility(View.VISIBLE);
                                                                bottomNavigationView.setAnimation(animslideU);
                                                                bottomNavigationView.setVisibility(View.VISIBLE);
                                                            }
                                                            if (marker != null) {
                                                                marker.setVisible(true);
                                                            }


                                                        } else {
                                                            if (!Driver_selected.contains(_PhoneNo)) {
                                                                bottomNavigationView.setAnimation(animslideU);
                                                                bottomNavigationView.setVisibility(View.VISIBLE);
                                                                Lr.setAnimation(animslideD);
                                                                Lr.setVisibility(View.GONE);
                                                                for (int i = 0; i < markers.size(); i++) {
                                                                    if (markers.get(i) != null) {
                                                                        markers.get(i).setVisible(false);
                                                                    }
                                                                }
                                                            }else{
                                                                bottomNavigationView.setAnimation(animslideD);
                                                                bottomNavigationView.setVisibility(View.GONE);
                                                                Lr.setAnimation(animslideU);
                                                                Lr.setVisibility(View.VISIBLE);
                                                            }
                                                            mDatabase.child("Driver_Online").addValueEventListener(new FirebaseDataListener_s());
                                                        }
                                                    }

                                                }

                                            }

                                        }
                                    }
                                }

                                @Override
                                public void onRightSwipe(View view, int position) {

                                }

                                @Override
                                public void onLeftSwipe(View view, int position) {

                                }
                            }));


        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void open_vehicle_dialog(String driver_image, String vehicle_image, String driver_name,
                                     final String vehicle_no, final String driver_selected) {

        Button Change_vehicle, Okbutton;
        ImageView Owner_image1;
        ImageView Owner_image2;
        final Dialog dialog = new Dialog(After_owner_login_copy.this);
        dialog.setContentView(R.layout.owner_driver_change_vehicle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Change_vehicle = dialog.findViewById(R.id.change_vehicle);
        Owner_image1 = dialog.findViewById(R.id.profile_driver);
        Owner_image2 = dialog.findViewById(R.id.imagecar);
        Okbutton = dialog.findViewById(R.id.buttonok);

        Picasso.Builder builder = new Picasso.Builder(After_owner_login_copy.this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build().load(driver_image).into(Owner_image1);
        Picasso.Builder builder2 = new Picasso.Builder(After_owner_login_copy.this);
        builder2.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder2.build().load(vehicle_image).into(Owner_image2);

        Change_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (!Owner_is_driver) {
                    if (!(After_owner_login_copy.this.isFinishing())) {
                        new AlertDialog.Builder(After_owner_login_copy.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("Remove vehicle!")
                                .setMessage("Vehicle no " + Vehicle_no + "from  " + Driver_name)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new DeleteVehicle().execute(vehicle_no, driver_selected);
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

                } else {
                    if (!(After_owner_login_copy.this.isFinishing())) {
                        new AlertDialog.Builder(After_owner_login_copy.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("Remove vehicle!")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new DeleteVehicle().execute(vehicle_no, driver_selected);
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
                }

            }
        });
        Okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();

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
                            ActivityCompat.requestPermissions(After_owner_login_copy.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(After_owner_login_copy.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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
                        if (mGoogleApiClient.isConnected()) {
                            startLocationUpdates();
                        } else {
                            startLocationUpdat();
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(After_owner_login_copy.this, REQUEST_CHECK_SETTINGS);
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

            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mGoogleApiClient.isConnected()) {
                        startLocationUpdates();
                    } else {
                        startLocationUpdat();
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

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
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

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mCurrentLocation = location;
            if (!animate) {
                My_lat = mCurrentLocation.getLatitude();
                My_long = mCurrentLocation.getLongitude();

                first = true;
                CameraPosition googlePlex;
                googlePlex = CameraPosition.builder()
                        .target(new LatLng(My_lat, My_long))
                        .zoom(19) // Sets the zoom
                        .bearing(360) // Rotate the camera
                        // Set the camera tilt
                        .build(); // Crea
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));


                if (Owner_is_driver) {
                    if (!cancelled) {
                        if (UNIQUE_RIDE != null) {
                            String[] pars = UNIQUE_RIDE.split("\\.");
                            con = TextUtils.join("", pars);
                            if (stop) {
                                ask = false;
                                wait = false;
                                go = false;
                                stop = false;
                                Accepted = false;
                                Intent i = new Intent(After_owner_login_copy.this, Success.class);
                                i.putExtra("my_lat", My_lat);
                                i.putExtra("my_long", My_long);
                                i.putExtra("COST", Cost);
                                i.putExtra("KM", Distance_travel);
                                i.putExtra("UNIQUEID", UNIQUE_RIDE);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                                finish();
                            }


                            mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                            if (marker != null) {
                                if (mPathPolygonPoints.size() > 1) {
                                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                    mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                    // mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                    // mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                    //mDatabase.child("Accepted_Ride").child(con).child("Driver_Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                    //mDatabase.child("Accepted_Ride").child(con).child("Driver_Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                    if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                        animate = true;
                                        animateCarMove(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                        ROUTE[0] = mPathPolygonPoints.get(1);
                                        if (go) {
                                            markerPoints = new ArrayList<LatLng>();
                                            markerPoints.clear();
                                            markerPoints.add(mPathPolygonPoints.get(0));
                                            markerPoints.add(mPathPolygonPoints.get(1));
                                            if (markerPoints.size() == 2) {
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
                            } else {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(mPathPolygonPoints.get(0))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            }

                        } else {
                            if (!Ride) {
                                if (vehicle_type != null) {
                                    noOwner.setVisibility(View.GONE);
                                    if (!offline) {
                                        mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                                        if (marker != null) {
                                            if (mPathPolygonPoints.size() > 1) {
                                                mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                                mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                                // mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                                //mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                                if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                                    animate = true;
                                                    animateCarMoveA(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                                    ROUTE[0] = mPathPolygonPoints.get(1);
                                                    mPathPolygonPoints.clear();
                                                    mPathPolygonPoints.add(ROUTE[0]);
                                                }
                                            }
                                        } else {
                                            marker = googleMap.addMarker(new MarkerOptions()
                                                    .position(mPathPolygonPoints.get(0))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                        }
                                    }
                                }

                            } else {
                                if (!again) {
                                    again = true;
                                    Intent i = new Intent(After_owner_login_copy.this, GetRide.class);
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
                } else {
                    Lr.setAnimation(animslideD);
                    Lr.setVisibility(View.GONE);
                }

            }
        }
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            if (!animate) {
                My_lat = mCurrentLocation.getLatitude();
                My_long = mCurrentLocation.getLongitude();
                if (mPathPolygonPoints.size() == 0) {
                    mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                }
                if (!first && googleMap != null) {
                    first = true;
                    CameraPosition googlePlex;
                    googlePlex = CameraPosition.builder()
                            .target(new LatLng(My_lat, My_long))
                            .zoom(19) // Sets the zoom
                            .bearing(360) // Rotate the camera
                            // Set the camera tilt
                            .build(); // Crea
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                }


                if (Owner_is_driver) {
                    if (!cancelled) {
                        if (UNIQUE_RIDE != null) {
                            String[] pars = UNIQUE_RIDE.split("\\.");
                            con = TextUtils.join("", pars);
                            if (stop) {
                                ask = false;
                                wait = false;
                                go = false;
                                stop = false;
                                Accepted = false;
                                Intent i = new Intent(After_owner_login_copy.this, Success.class);
                                i.putExtra("my_lat", My_lat);
                                i.putExtra("my_long", My_long);
                                i.putExtra("COST", Cost);
                                i.putExtra("KM", Distance_travel);
                                i.putExtra("UNIQUEID", UNIQUE_RIDE);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                                finish();
                            }


                            mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                            if (marker != null) {
                                if (mPathPolygonPoints.size() > 1) {
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
                                        if (go) {
                                            markerPoints = new ArrayList<LatLng>();
                                            markerPoints.clear();
                                            markerPoints.add(mPathPolygonPoints.get(0));
                                            markerPoints.add(mPathPolygonPoints.get(1));
                                            if (markerPoints.size() == 2) {
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
                            } else {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(mPathPolygonPoints.get(0))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                            }

                        } else {
                            if (!Ride) {
                                if (vehicle_type != null) {
                                    noOwner.setVisibility(View.GONE);
                                    if (!offline) {
                                        mPathPolygonPoints.add(new LatLng(My_lat, My_long));
                                        if (marker != null) {
                                            if (mPathPolygonPoints.size() > 1) {
                                                mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(mPathPolygonPoints.get(0).latitude));
                                                mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(mPathPolygonPoints.get(0).longitude));
                                                // mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(mPathPolygonPoints.get(1).latitude));
                                                //mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(mPathPolygonPoints.get(1).longitude));
                                                if (com.google.maps.android.SphericalUtil.computeDistanceBetween(mPathPolygonPoints.get(0), new LatLng(My_lat, My_long)) > 1) {
                                                    animate = true;
                                                    animateCarMoveA(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1));
                                                    ROUTE[0] = mPathPolygonPoints.get(1);
                                                    mPathPolygonPoints.clear();
                                                    mPathPolygonPoints.add(ROUTE[0]);
                                                }
                                            }
                                        } else {
                                            marker = googleMap.addMarker(new MarkerOptions()
                                                    .position(mPathPolygonPoints.get(0))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed)));
                                        }
                                    }
                                }

                            } else {
                                if (!again) {
                                    again = true;
                                    Intent i = new Intent(After_owner_login_copy.this, GetRide.class);
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
                } else {
                    Lr.setAnimation(animslideD);
                    Lr.setVisibility(View.GONE);
                }

            }
        }
    }

    private void animateCarMoveAbc(final Marker anmarker, final LatLng beginLatLng, final LatLng endLatLng, final String vehicle) {
        final Bitmap[] mMarkerIcon = new Bitmap[1];
        if (vehicle != null) {
            animate = true;
            final Handler handler = new Handler();
            final long startTime = SystemClock.uptimeMillis();
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {


                @Override
                public void run() {
                    if (!clicK) {
                        if (vehicle.contains("SEDAN")) {
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                        } else if (vehicle.contains("PICKUP")) {
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_truck);
                        } else if (vehicle.contains("SUV")) {
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_suv);
                        } else if (vehicle.contains("MINI")) {
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                        } else if (vehicle.contains("MICRO")) {
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                        } else if (vehicle.contains("AMBULANCE")) {
                            mMarkerIcon[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                        }

                        long elapsed = SystemClock.uptimeMillis() - startTime;
                        float t = interpolator.getInterpolation((float) elapsed / 3000);
                        double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                        double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                        if (Math.abs(lngDelta) > 180) {
                            lngDelta -= Math.signum(lngDelta) * 360;
                        }
                        double lng = lngDelta * t + beginLatLng.longitude;
                        float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(anmarker.getPosition(), new LatLng(lat, lng)));
                        Matrix matrix = new Matrix();
                        //matrix.setRotate(anmarker.getRotation());
                        matrix.postRotate(angleDeg);
                        if (googleMap != null) {
                            anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon[0], 0, 0, mMarkerIcon[0].getWidth(), mMarkerIcon[0].getHeight(), matrix, true)));
                            anmarker.setPosition(new LatLng(lat, lng));
                            anmarker.setAnchor(.5f, .5f);
                            anmarker.setVisible(true);
                            if (go) {
                                CameraPosition googlePlex = CameraPosition.builder()
                                        .target(new LatLng(lat, lng))
                                        .zoom(18)
                                        .bearing(anmarker.getRotation())
                                        .build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                            }
                        }

                        if (t < 1.0) {
                            handler.postDelayed(this, 16);
                        } else {
                            //matrix.postRotate(180);
                            //anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon[0], 0, 0, mMarkerIcon[0].getWidth(), mMarkerIcon[0].getHeight(), matrix, true)));
                            animate = false;
                            //anmarker.setPosition(endLatLng);

                        }
                    } else {
                        handler.removeCallbacks(this);
                    }
                }
            });
        }
    }

    public void SetZoomlevel() {
        if (googleMap != null) {
            if (markers.size() != 0) {
                LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder1.include(marker.getPosition());
                }
                LatLngBounds bounds = builder1.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
                googleMap.moveCamera(cu);
            }


        } else {
            mapFragment.getMapAsync(After_owner_login_copy.this);

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

        updateLocationUI();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        // stopLocationUpdates();
        //handler.removeCallbacks(r);
    }

    @Override
    protected void onDestroy() {
        stopLocationUpdates();
        //handler.removeCallbacks(r);
        super.onDestroy();

    }

    private void stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mGoogleApiClient.disconnect();

                    }
                });
    }

    private void normal() {
        Vehicles.setVisibility(View.GONE);
        Drivers.setVisibility(View.VISIBLE);
        mItems.clear();
        markers.clear();
        mItems2.clear();
        if (Owner_is_driver) {
            Lr.setAnimation(animslideU);
            Lr.setVisibility(View.VISIBLE);

        }

        if (UNIQUE_RIDE != null) {
            Accepted = true;
            ask = false;
            wait = true;
            go = false;
            stop = false;
            Ride = false;
            String[] pars = UNIQUE_RIDE.split("\\.");
            con = TextUtils.join("", pars);
            bottomNavigationView.setVisibility(View.GONE);
        }
        bottomNavigationView.setAnimation(animslideD);
        bottomNavigationView.setVisibility(View.GONE);
        if (checkPermissions()) {

            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();

            } else {
                startLocationUpdat();

            }
        } else if (!checkPermissions()) {
            requestPermissions();
        }
        new GetCustomer().execute();

    }

    private void open_self() {

        final Dialog dialog = new Dialog(After_owner_login_copy.this);


        dialog.setContentView(R.layout.self_driver);
        Button self = dialog.findViewById(R.id.buttonself);
        Button other = dialog.findViewById(R.id.buttondriver);
        Button listselect = dialog.findViewById(R.id.buttonlist);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        listselect.setVisibility(View.VISIBLE);
        self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent o = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                o.putExtra("Driver", "OWNER");
                o.putExtra("Driver_no", _PhoneNo);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                o.putExtra("reg", regId);
                startActivity(o);
                finish();

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent o = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                o.putExtra("Driver", "DRIVER");
                o.putExtra("Driver_no", "");
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                o.putExtra("reg", regId);
                startActivity(o);
                finish();

            }
        });
        listselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Drivers.setVisibility(View.GONE);
                mItems.clear();
                open_coupon();

            }
        });
        dialog.show();

    }

    private void open_driver(final String driver_selected_name, final int driver_selected_rating, final String driver_selected, final String image) {

        final Dialog dialog = new Dialog(After_owner_login_copy.this);
        dialog.setContentView(R.layout.get_driver);
        // Set the custom layout as alert dialog view
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialog.findViewById(R.id.home_sav1);
        Button btn_negative = (Button) dialog.findViewById(R.id.home_can1);
        CircleImageView driver = dialog.findViewById(R.id.driver_bill);
        RatingBar driver_rate = dialog.findViewById(R.id.ratingBarbill);
        final TextView Id_Driver = dialog.findViewById(R.id.t13);

        Id_Driver.setText(driver_selected_name);
        Picasso.Builder builder = new Picasso.Builder(After_owner_login_copy.this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build().load(image).into(driver);
        driver_rate.setRating(driver_selected_rating);
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new UpdateDriver().execute(driver_selected);
                dialog.cancel();


            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                recreate();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void open_coupon() {

        final Dialog dialog = new Dialog(After_owner_login_copy.this);
        dialog.setContentView(R.layout.apply_coupon);
        // Set the custom layout as alert dialog view
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialog.findViewById(R.id.home_sav);
        Button btn_negative = (Button) dialog.findViewById(R.id.home_can);
        final EditText Id_Driver = dialog.findViewById(R.id.app_coupon);
        // Create the alert dialog

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(Id_Driver.getText().toString())) {
                    new GetDrivers().execute(Id_Driver.getText().toString());
                    dialog.cancel();
                } else {
                    Id_Driver.setError("Empty");
                }

            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                recreate();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void open_list() {

        final Dialog dialog = new Dialog(After_owner_login_copy.this);


        dialog.setContentView(R.layout.self_driver);
        Button self = dialog.findViewById(R.id.buttonself);
        Button other = dialog.findViewById(R.id.buttondriver);
        Button listselect = dialog.findViewById(R.id.buttonlist);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        self.setVisibility(View.GONE);
        listselect.setVisibility(View.VISIBLE);
        listselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Drivers.setVisibility(View.GONE);
                mItems.clear();
                open_coupon();

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent o = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                o.putExtra("Driver", "DRIVER");
                o.putExtra("Driver_no", "");
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                o.putExtra("reg", regId);
                startActivity(o);
                finish();

            }
        });

        dialog.show();

    }

    public void SetZoomGot(final Marker markeruser, final Marker markerCar) {
        if (googleMap != null) {
            if (markeruser != null && markerCar != null) {
                ArrayList<Marker> markerAll = new ArrayList<Marker>();
                markerAll.add(markeruser);
                markerAll.add(markerCar);
                if (markerAll.size() != 0) {
                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
                    for (Marker marker : markerAll) {
                        builder1.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder1.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 120);
                    googleMap.animateCamera(cu);
                    LatLng origin = markeruser.getPosition();
                    LatLng dest = markerCar.getPosition();
                    String url = getDirectionsUrl(origin, dest);
                    if (!Start_ride) {
                        new DownloadTask().execute(url);
                    }
                }

            }
        } else {
            mapFragment.getMapAsync(After_owner_login_copy.this);

        }
    }

    public Marker createMarker(double latitude, double longitude, String imei, String name, String vehicle_type) {
        Marker Marker_;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.title(name);
        markerOptions.snippet(imei);
        Marker_ = googleMap.addMarker(markerOptions);
        if (vehicle_type.contains("SEDAN")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        } else if (vehicle_type.contains("PICKUP")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck));

        } else if (vehicle_type.contains("SUV")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_suv));

        } else if (vehicle_type.contains("MINI")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        } else if (vehicle_type.contains("MICRO")) {
            Marker_.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sed));

        }
        Marker_.setVisible(false);
        Marker_.showInfoWindow();
        return Marker_;


    }

    private void animateCarMove(final Marker anmarker, final LatLng beginLatLng, final LatLng endLatLng) {

        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();
        float angleDeg1 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng, endLatLng));
        float direction = (angleDeg1 > 0) ? 180 : 0;
        Location location = new Location("Test");
        location.setLatitude(beginLatLng.latitude);
        location.setLongitude(beginLatLng.longitude);
        Location location1 = new Location("Test");
        location1.setLatitude(endLatLng.latitude);
        location1.setLongitude(endLatLng.longitude);
        //bearing=location.bearingTo(location1);
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
                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * t + beginLatLng.longitude;
                //float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(new LatLng(lat, lng), endLatLng));
                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                Matrix matrix = new Matrix();
                matrix.setRotate(anmarker.getRotation());
                float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng, new LatLng(lat, lng)));
                float angleDeg1 = (float) (com.google.maps.android.SphericalUtil.computeHeading(beginLatLng, endLatLng));
                float direction = (angleDeg1 > 0) ? angleDeg + 180 : angleDeg;
                matrix.postRotate(direction);
                if (anmarker != null && googleMap != null) {
                    anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
                    anmarker.setPosition(new LatLng(lat, lng));
                    anmarker.setAnchor(0.5f, .7f);

                }
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    matrix.postRotate(180);
                    // anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
                    animate = false;
                }

            }
        });
    }

    private void animateCarMoveA(final Marker anmarker, final LatLng beginLatLng, final LatLng endLatLng) {

        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {


            @Override
            public void run() {
                // calculate phase of animation
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / 3000);
                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * t + beginLatLng.longitude;
                float angleDeg = (float) (com.google.maps.android.SphericalUtil.computeHeading(new LatLng(lat, lng), endLatLng));
                mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sed);
                Matrix matrix = new Matrix();
                matrix.postRotate(angleDeg);
                if (anmarker != null && googleMap != null) {
                    anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
                    anmarker.setPosition(new LatLng(lat, lng));
                    anmarker.setAnchor(0.5f, .5f);
                    if (go) {
                        CameraPosition googlePlex = CameraPosition.builder()
                                .target(new LatLng(lat, lng))
                                .zoom(19)
                                .bearing(anmarker.getRotation())
                                .build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                    }
                }
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    matrix.postRotate(180);
                    anmarker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));
                    animate = false;
                }

            }
        });
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
                        Intent j = new Intent(After_owner_login_copy.this, GooglemapApp.class);
                        j.putExtra("my_lat", My_lat);
                        j.putExtra("my_long", My_long);
                        startActivity(j);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_gallery:
                        navItemIndex = 1;
                        stopLocationUpdates();
                        Intent o = new Intent(After_owner_login_copy.this, Ride_later_dates.class);
                        o.putExtra("mylat", My_lat);
                        o.putExtra("mylong", My_long);
                        startActivity(o);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_documents:
                        drawer.closeDrawers();
                        Intent o1 = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                        o1.putExtra("Driver", "SELF");
                        o1.putExtra("Driver_no", _PhoneNo);
                        o1.putExtra("my_lat", My_lat);
                        o1.putExtra("my_long", My_long);
                        o1.putExtra("reg", regId);
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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class PostOtpdata extends AsyncTask<Void, Integer, String> {


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
                Confirm_ride.setVisibility(View.GONE);
                Stop_ride.setVisibility(View.VISIBLE);
                Amb.setVisibility(View.GONE);
                Start_ride = false;
                got = false;
                ask = false;
                wait = false;
                go = true;
                String[] pars = UNIQUE_RIDE.split("\\.");
                String con = TextUtils.join("", pars);
                if (mTaxes.size() == 0) {
                    new GetSettings().execute();
                } else {
                    Tax_applied = TextUtils.join("+", mTaxes);
                }
                mDatabase.child("Accepted_Ride").child(con).child("Tax_names").setValue(Tax_applied);
                mDatabase.child("Accepted_Ride").child(con).child("Tax_percentage").setValue(dfto.format(Tota_taxes));
                mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("YES");
                mDatabase.child("Accepted_Ride").child(con).child("START").setValue("YES");
                for (int i = 0; i < markers.size(); i++) {
                    markers.get(i).setVisible(false);
                }

            } else {
                Toast.makeText(getApplicationContext(), "Error! Please try again.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class FetchCordinates extends AsyncTask<String, Integer, String> {

        public VeggsterLocationListener mVeggsterLocationListener;
        private ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {

            mVeggsterLocationListener = new VeggsterLocationListener();
            if (ActivityCompat.checkSelfPermission(After_owner_login_copy.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(After_owner_login_copy.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {


                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mVeggsterLocationListener);

            } catch (SecurityException e) {

                e.printStackTrace();

            }
            progDailog = new ProgressDialog(After_owner_login_copy.this);
            progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                    dialog.cancel();
                }
            });
            progDailog.setMessage("Getting coordinates...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();


        }

        @Override
        protected void onCancelled() {
            out.println("Cancelled by user!");
            if (progDailog != null && progDailog.isShowing()) {
                progDailog.dismiss();
            }

            FetchCordinates.this.cancel(true);

        }

        @Override
        protected void onPostExecute(String result) {

            if (progDailog != null && progDailog.isShowing()) {
                progDailog.dismiss();
            }


            if (My_lat != 0) {

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(My_lat, My_long)) // Sets the new camera position
                        .zoom(19) // Sets the zoom
                        .bearing(360) // Rotate the camera
                        // Set the camera tilt
                        .build(); // Crea
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position));
                clicK = false;
            } else {

                Toast.makeText(getApplicationContext(), "Slow internet!Please try again", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (My_lat == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                try {

                    My_lat = location.getLatitude();
                    My_long = location.getLongitude();

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Unable to get Location"
                            , Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    private class PostBookdata extends AsyncTask<Void, Integer, String> {


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
                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    File sourceFile = new File(filePath);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("mobile", _PhoneNo)
                            .addFormDataPart("Unique_id", UNIQUE_RIDE)
                            .addFormDataPart("distance", String.valueOf(Distance_travel))
                            .addFormDataPart("cost", String.valueOf(Taotal_cost))
                            .addFormDataPart("coupon", Coupon_code)
                            .addFormDataPart("coupon_value", String.valueOf(Coupon_value))
                            .addFormDataPart("Minimum_fare", String.valueOf(Minimum_fare))
                            .addFormDataPart("Hourly_fare", String.valueOf(Hourly_fare))
                            .addFormDataPart("IP", mobileIp)
                            .addFormDataPart("image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
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
                    String[] pars = UNIQUE_RIDE.split("\\.");
                    con = TextUtils.join("", pars);
                    mDatabase.child("Accepted_Ride").child(con).child("START").setValue("NO");
                    ask = false;
                    wait = false;
                    go = false;
                    stop = false;
                    Accepted = false;
                    Intent i = new Intent(After_owner_login_copy.this, Success.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    i.putExtra("COST", Cost);
                    i.putExtra("KM", Distance_travel);
                    i.putExtra("UNIQUEID", UNIQUE_RIDE);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    finish();

                }
            } else {
                Snackbar.make(After_owner_login_copy.this.getWindow().getDecorView().getRootView(), "something went wrong..Please try again", Snackbar.LENGTH_LONG).show();

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
                        .url(Config_URL.URL_FCM_OWNER)
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
            if (Owner_is_driver) {
                new Update_FCM_driver().execute();
            }

        }

    }

    class Update_FCM_driver extends AsyncTask<Void, Integer, String> {


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

    private class FirebaseDataListener_s implements ValueEventListener {

        private int k = 0;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getChildrenCount() != 0) {
                Map<String, Object> objectMap = null;

                objectMap = (HashMap<String, Object>)
                        dataSnapshot.getValue();
                assert objectMap != null;

                for (Object obj : objectMap.values()) {
                    if (obj instanceof Map) {
                        Map<String, Object> mapObj = (Map<String, Object>) obj;
                        if (mapObj.get("First_Latitude") != null && mapObj.get("First_Longitude") != null
                                && mapObj.get("Second_Latitude") != null && mapObj.get("Second_Longitude") != null
                                && mapObj.get("Driver_Phone_no") != null && mapObj.get("Driver_Vehicle") != null
                                && mapObj.get("Owner_Phone_no") != null && mapObj.get("Offline") != null && mapObj.get("OnRide") != null) {
                            if (mapObj.get("Owner_Phone_no").toString().contains(_PhoneNo)) {
                                if (markers.size() != 0) {
                                    for (int i = 0; i < markers.size(); i++) {
                                        if (!markers.get(i).getSnippet().contains(_PhoneNo)) {
                                            if (mapObj.get("Driver_Phone_no").toString().contains(markers.get(i).getSnippet())) {
                                                vehicle_type = mapObj.get("Driver_Vehicle").toString();
                                                if (Driver_selected != null && !Driver_selected.contains(_PhoneNo)) {
                                                    if (mapObj.get("Driver_Phone_no").toString().contains(Driver_selected)) {
                                                        mPathPolygonPoint.add(new LatLng(Double.parseDouble((String) mapObj.get("First_Latitude")), Double.parseDouble((String) mapObj.get("First_Longitude"))));
                                                        mPathPolygonPoint.add(new LatLng(Double.parseDouble((String) mapObj.get("Second_Latitude")), Double.parseDouble((String) mapObj.get("Second_Longitude"))));

                                                        markers.get(i).setVisible(true);
                                                        markers.get(i).showInfoWindow();
                                                        CameraPosition googlePlex;
                                                        googlePlex = CameraPosition.builder()
                                                                .target(mPathPolygonPoint.get(1))
                                                                .zoom(18) // Sets the zoom
                                                                .bearing(360) // Rotate the camera
                                                                .build(); // Crea
                                                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                                                        if (markers.get(i).getPosition() != null) {
                                                            Location temp = new Location("this");
                                                            temp.setLatitude(mPathPolygonPoint.get(1).latitude);
                                                            temp.setLongitude(mPathPolygonPoint.get(1).longitude);
                                                            Location temp2 = new Location("thiss");
                                                            temp2.setLatitude(markers.get(i).getPosition().latitude);
                                                            temp2.setLongitude(markers.get(i).getPosition().longitude);
                                                            if (temp.distanceTo(temp2) > 1) {
                                                                animate = true;
                                                                animateCarMoveAbc(markers.get(i), markers.get(i).getPosition(), mPathPolygonPoint.get(1), mapObj.get("Driver_Vehicle").toString());
                                                            }
                                                        }
                                                        markers.get(i).setPosition(mPathPolygonPoint.get(1));
                                                        mPathPolygonPoint.clear();

                                                        if (mapObj.get("Offline").toString().contains("YES")) {
                                                            noOwner.setVisibility(View.GONE);
                                                            noOffline.setVisibility(View.VISIBLE);
                                                            noOffline.setText(mapObj.get("Name").toString() + " is " + "Offline");

                                                        } else {
                                                            noOffline.setAnimation(animslideD);
                                                            noOffline.setVisibility(View.GONE);
                                                            if (mapObj.get("OnRide").toString().contains("YES")) {
                                                                noOffline.setVisibility(View.VISIBLE);
                                                                noOffline.setText(mapObj.get("Name").toString() + " is " + "On Ride");
                                                                if (Driver_selected.contains(Owner_mobile)) {
                                                                    got = true;
                                                                    wait = false;
                                                                    go = false;
                                                                    stop = false;
                                                                }

                                                            } else {
                                                                noOffline.setAnimation(animslideD);
                                                                noOffline.setVisibility(View.GONE);
                                                            }
                                                        }

                                                    }
                                                }
                                                if (mapObj.get("Offline").toString().contains("YES")) {
                                                    noOwner.setVisibility(View.GONE);
                                                    noOffline.setVisibility(View.VISIBLE);
                                                    noOffline.setText(mapObj.get("Name").toString() + " is " + "Offline");
                                                }
                                                if (mapObj.get("OnRide").toString().contains("YES")) {
                                                    noOffline.setVisibility(View.VISIBLE);
                                                    noOffline.setText(mapObj.get("Name").toString() + " is " + "On Ride");

                                                }
                                            }

                                        }
                                    }

                                    //mapObj.clear();
                                }
                                if (mapObj.get("Driver_Phone_no").toString().contains(_PhoneNo)) {
                                    vehicle_type = (String) mapObj.get("Driver_Vehicle");
                                }
                            }
                        }

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

            if (UNIQUE_RIDE == null) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    if (dataSnapshot.child("Owner_Phone_no").getValue() != null) {
                        Owner_mobile = (String) dataSnapshot.child("Owner_Phone_no").getValue();
                        if (dataSnapshot.child("Driver_Vehicle").getValue() != null) {
                            vehicle_type = (String) dataSnapshot.child("Driver_Vehicle").getValue();
                            if (dataSnapshot.child("Offline").getValue() != null) {
                                String off = (String) dataSnapshot.child("Offline").getValue();
                                if (off.contains("NO")) {
                                    if (marker != null) {
                                        offline = false;

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
                        }
                    } else {
                        if (marker != null) {
                            marker.setVisible(false);
                        }

                        noOwner.setText("No vehicle assigned to you");
                        noOwner.setVisibility(View.VISIBLE);

                    }

                } else {
                    vehicle_type = null;
                    Owner_mobile = null;
                }


            } else {
                String[] pars = UNIQUE_RIDE.split("\\.");
                con = TextUtils.join("", pars);
                mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_got());

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class FirebaseDataListener_ride implements ValueEventListener {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (UNIQUE_RIDE == null) {
                if (dataSnapshot.getChildrenCount() != 0) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child != null) {
                            String key = child.getKey();
                            Object value = child.getValue();
                            if (((Map) value).get("Vehicle") != null) {
                                String vehicle = ((Map) value).get("Vehicle").toString();
                                if (vehicle_type != null && vehicle != null)
                                    if (vehicle.contains(vehicle_type)) {
                                        mDatabase.child("Cancel").child(_PhoneNo).child(key).addValueEventListener(new FirebaseDataListener_cancel());
                                    } else {
                                        Ride = false;
                                        if (again) {
                                            again = false;
                                            Intent i = new Intent(getApplicationContext(), After_owner_login_copy.class);
                                            startActivity(i);

                                        }
                                    }
                            }
                        }

                    }
                } else {
                    Ride = false;
                    if (again) {
                        again = false;
                        Intent i = new Intent(getApplicationContext(), After_owner_login_copy.class);
                        startActivity(i);

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class FirebaseDataListener_cancel implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (UNIQUE_RIDE == null) {
                if (dataSnapshot.getChildrenCount() != 0) {
                } else {
                    ask = true;
                    Ride = true;
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class GetListCar extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Vehicles.setVisibility(View.VISIBLE);
            Drivers.setVisibility(View.GONE);
            mItems.clear();
            mItems2.clear();

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
                    JSONArray Owner = jsonObj.getJSONArray("Owner_Details");
                    JSONArray Drivers = jsonObj.getJSONArray("Driver_Details");
                    JSONArray Vehicle = jsonObj.getJSONArray("Vehicle_detail");
                    // looping through All Contacts
                    if (_PhoneNo != null) {

                        int relation = 0;
                        if (Owner_ID != 0) {

                            for (int i = 0; i < Vehicle.length(); i++) {
                                JSONObject c = Vehicle.getJSONObject(i);
                                if (!c.isNull("Owner_ID") && c.getInt("Owner_ID") == Owner_ID && c.isNull("Driver_ID")) {

                                    Vehicle item = new Vehicle();
                                    item.setVehicle_no(c.getString("Vehicle_No"));
                                    item.setVehicle_image_1(c.getString("Vehicle_Photo_1"));
                                    item.setVehicle_type(c.getString("Vehicle_Type"));
                                    item.setMinimum_Balance_Status(c.getInt("Minimum_Balance_Status"));
                                    mItems2.add(item);

                                }
                            }
                            Vehicle item = new Vehicle();
                            item.setVehicle_no("Add Vehicle");
                            item.setVehicle_image_1("http://139.59.38.160/OHO/APP/Profile/addvehicle.png");
                            mItems2.add(item);
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
                                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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
            progressBar.setVisibility(View.GONE);


            if (Owner_ID != 0) {
                if (mItems2.size() != 0) {
                    Vehicle_adapter sAdapter = new Vehicle_adapter(After_owner_login_copy.this, mItems2);
                    sAdapter.notifyDataSetChanged();
                    Vehicles.setAdapter(sAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(After_owner_login_copy.this);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    Vehicles.setLayoutManager(mLayoutManager);

                    if (mItems2.size() == 1) {
                        Heading_.setText("No vehicle available");
                        noOwner.setText("No vehicle available");
                    } else {
                        Heading_.setText("Vehicle available for " + Driver_name);
                        noOwner.setText("Vehicle available for " + Driver_name);
                    }


                } else {

                    Heading_.setText("No driver available");
                    Vehicles.setVisibility(View.GONE);
                }


            }
        }

    }

    private class GetSettings extends AsyncTask<Void, Void, Void> {


        private int Online = 0;
        private String New_Version;
        private int New_Version_Importance = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Tota_taxes = 0;
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
                    mTaxes.clear();
                    for (int i = 0; i < Taxes.length(); i++) {
                        JSONObject c = Taxes.getJSONObject(i);
                        mTaxes.add(c.getString("Tax_Name"));
                        Tota_taxes = Tota_taxes + (c.getDouble("Tax_Percentage"));
                    }
                    for (int i = 0; i < Settings.length(); i++) {
                        JSONObject c = Settings.getJSONObject(i);
                        Online = c.getInt("Service_Online");
                        Minimum_Distance_in_Meter_for_Tracking = c.getInt("Minimum_Distance_in_Meter_for_Tracking");

                    }
                    for (int i = 0; i < Version.length(); i++) {
                        JSONObject c = Version.getJSONObject(i);
                        New_Version = c.getString("Version");
                        New_Version_Importance = c.getInt("Importance");

                    }

                    JSONArray Master_Vehicle_Type = jsonObj.getJSONArray("Vehicle_Rate");
                    for (int i = 0; i < Master_Vehicle_Type.length(); i++) {
                        JSONObject c = Master_Vehicle_Type.getJSONObject(i);
                        if (vehicle_type != null) {
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
                            if (!(After_owner_login_copy.this.isFinishing())) {
                                new AlertDialog.Builder(After_owner_login_copy.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("Update HelloCab")
                                        .setMessage("A new version of hellocab is available!")
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
                            if (!(After_owner_login_copy.this.isFinishing())) {
                                new AlertDialog.Builder(After_owner_login_copy.this)
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

            } else {
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

    private class GetCustomer extends AsyncTask<Void, Void, Void> {


        private int relation1 = 0;
        private String Owner_Addressproof, Owner_Cancel_Cheque_Photo;
        private String User_from, User_to, uMobile;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Owner_is_driver = false;
            mItems.clear();
            mItems2.clear();


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
                    JSONArray Owner = jsonObj.getJSONArray("Owner_Details");
                    JSONArray Drivers = jsonObj.getJSONArray("Driver_Details");
                    JSONArray Vehicle = jsonObj.getJSONArray("Vehicle_detail");
                    JSONArray Settings = jsonObj.getJSONArray("Settings");
                    JSONArray Version = jsonObj.getJSONArray("Version_driver");
                    JSONArray ride = jsonObj.getJSONArray("Book_Ride_Now");
                    JSONArray User = jsonObj.getJSONArray("User");
                    JSONArray Master_Vehicle_Type = jsonObj.getJSONArray("Vehicle_Rate");
                    for (int i = 0; i < Settings.length(); i++) {
                        JSONObject c = Settings.getJSONObject(i);
                        Online = c.getInt("Service_Online");

                    }
                    for (int i = 0; i < Version.length(); i++) {
                        JSONObject c = Version.getJSONObject(i);
                        New_Version = c.getString("Version");
                        New_Version_Importance = c.getInt("Importance");

                    }
                    if (_PhoneNo != null) {
                        for (int i = 0; i < Owner.length(); i++) {
                            JSONObject c = Owner.getJSONObject(i);
                            String relation = c.getString("Phone_No");


                            if (relation.matches(_PhoneNo)) {
                                Owner_name = c.getString("Name");
                                Owner_mobile = c.getString("Phone_No");
                                Owner_ID = c.getInt("ID");
                                Owner_image = c.getString("Photo");
                                Owner_Pan_Card = c.getString("Pancard_Photo");
                                Owner_Addressproof = c.getString("Addressproof_Photo");
                                Owner_Cancel_Cheque_Photo = c.getString("Cancel_Cheque_Photo");
                                Owner_Verified_By = c.getString("Verified_By");
                            }
                        }

                        if (Owner_ID != 0) {
                            for (int i = 0; i < Drivers.length(); i++) {
                                JSONObject c = Drivers.getJSONObject(i);
                                if (!c.isNull("Owner_ID") && !c.isNull("Driving_License_Photo")) {
                                    if (c.getInt("Owner_ID") == Owner_ID) {
                                        Album item = new Album();
                                        item.setDriver_ID(c.getInt("ID"));
                                        item.setDriver_Name(c.getString("Name"));
                                        item.setDriver_Phone_No(c.getString("Phone_No"));
                                        item.setOwner_ID(c.getInt("Owner_ID"));
                                        item.setVerify_Driver(c.getString("Verified_By"));
                                        if (c.getString("Phone_No").contains(_PhoneNo)) {
                                            Owner_is_driver = true;
                                            Driver_ID = c.getInt("ID");
                                        }

                                        item.setDriver_Photo(c.getString("Photo"));

                                        for (int j = 0; j < Vehicle.length(); j++) {
                                            JSONObject d = Vehicle.getJSONObject(j);
                                            if (!d.isNull("Owner_ID") && !d.isNull("Driver_ID")
                                                    && d.getInt("Owner_ID") == Owner_ID
                                                    && d.getInt("Driver_ID") == c.getInt("ID")) {
                                                item.setVehicle_No(d.getString("Vehicle_No"));
                                                item.setVehicle_Image_1(d.getString("Vehicle_Photo_1"));
                                                item.setVehicle_Type(d.getString("Vehicle_Type"));
                                                item.setVehicle_Model(d.getString("Vehicle_Model"));
                                                item.setVehicle_Company(d.getString("Vehicle_Company"));
                                                item.setMinimum_Balance_Status(d.getInt("Minimum_Balance_Status"));
                                                if (Owner_is_driver && d.getInt("Driver_ID") == Driver_ID) {
                                                    vehicle_type = d.getString("Vehicle_Type");
                                                    vehicle_no = d.getString("Vehicle_No");
                                                    if (d.getInt("Minimum_Balance_Status") == 1) {
                                                        Minimum_Balance = true;
                                                    }
                                                    for (int k = 0; k < Master_Vehicle_Type.length(); k++) {
                                                        JSONObject g = Master_Vehicle_Type.getJSONObject(k);
                                                        if (vehicle_type != null) {
                                                            if (!g.isNull("Vehicle_Type") && g.getString("Vehicle_Type").contains(vehicle_type)) {
                                                                Minimum_fare = g.getInt("Minimum_Rate");
                                                                Hourly_fare = g.getInt("Hourly_Rate");
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                            }

                                        }
                                        mItems.add(item);
                                    }

                                }
                            }


                        }
                        Album item = new Album();
                        item.setDriver_Name("Add Driver");
                        item.setDriver_Photo("http://139.59.38.160/OHO/APP/Profile/ic_driver.png");
                        item.setDriver_Phone_No("123456789");
                        mItems.add(item);
                    }

                    for (int i = 0; i < ride.length(); i++) {
                        JSONObject c = ride.getJSONObject(i);
                        if (!c.isNull("Driver_ID")) {

                            if (c.getInt("Driver_ID") == (Owner_ID)) {
                                OTP = c.getInt("OTP");

                                UNIQUE_RIDE = c.getString("Unique_Ride_Code");
                                User_from = c.getString("From_Address");
                                User_to = c.getString("To_Address");
                                for (int j = 0; j < User.length(); j++) {
                                    JSONObject d = User.getJSONObject(i);
                                    if (d.getInt("ID") == (c.getInt("User_ID"))) {
                                        uMobile = d.getString("Phone_No");
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
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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
            progressBar.setVisibility(View.GONE);

            if (Owner_Pan_Card != null && !Owner_Pan_Card.contains("null") && Owner_Addressproof != null && !Owner_Addressproof.contains("null")
                    && Owner_Cancel_Cheque_Photo != null && !Owner_Cancel_Cheque_Photo.contains("null")) {
                if (Owner_Verified_By != null && !Owner_Verified_By.contains("null")
                        && !TextUtils.isEmpty(Owner_Verified_By)) {
                    txtName.setText(Owner_name);
                    Picasso.Builder builder = new Picasso.Builder(After_owner_login_copy.this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(Owner_image).into(profile_image);
                    Image_Adapter sAdapter = new Image_Adapter(After_owner_login_copy.this, mItems);
                    sAdapter.notifyDataSetChanged();
                    sAdapter.setID(Owner_ID);
                    sAdapter.setOwnerisDriver(Owner_is_driver);
                    Drivers.setAdapter(sAdapter);

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(After_owner_login_copy.this);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                    Drivers.setLayoutManager(mLayoutManager);

                    if (UNIQUE_RIDE != null) {
                        for (int i = 0; i < markers.size(); i++) {
                            markers.get(i).setVisible(false);
                        }
                        String[] pars = UNIQUE_RIDE.split("\\.");
                        con = TextUtils.join("", pars);
                        ask = false;
                        wait = false;
                        got = true;
                        go = false;
                        stop = false;
                        Ride = false;
                        mDatabase.child("Accepted_Ride").child(con).addValueEventListener(new FirebaseDataListener_got());
                    } else {
                        ask = false;
                        wait = false;
                        go = false;
                        stop = false;
                        Ride = false;
                        Drivers.setVisibility(View.VISIBLE);
                        if (mItems.size() > 1) {
                            for (int i = 0; i < mItems.size(); i++) {
                                if (!mItems.get(i).getDriver_Name(i).contains("Driver") && mItems.get(i).getVehicle_No(i) != null) {
                                    if (googleMap != null) {
                                        markers.add(createMarker(mItems.get(i).getLat(i), mItems.get(i).getLong(i), mItems.get(i).getDriver_Phone_No(i), mItems.get(i).getDriver_Name(i)
                                                , mItems.get(i).getVehicle_Type(i)));

                                    }
                                }
                            }

                        }


                        if (Owner_is_driver && !offline && first) {
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Latitude").setValue(dft.format(My_lat));
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("First_Longitude").setValue(dft.format(My_long));
                            // mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Latitude").setValue(dft.format(My_lat));
                            //mDatabase.child("Driver_Online").child(_PhoneNo).child("Second_Longitude").setValue(dft.format(My_long));
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(_PhoneNo);
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Photo").setValue(Driver_image);
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(_PhoneNo);
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(_PhoneNo);
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Name").setValue(Owner_name);
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate = df.format(c.getTime());
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Online_Date").setValue(formattedDate);
                            if (vehicle_type != null) {
                                mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle").setValue(vehicle_type);
                                mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle_no").setValue(vehicle_no);
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
                            } else {
                                Lr.setAnimation(animslideD);
                                Lr.setVisibility(View.GONE);
                                bottomNavigationView.setAnimation(animslideU);
                                bottomNavigationView.setVisibility(View.VISIBLE);
                                noOwner.setVisibility(View.VISIBLE);
                                noOwner.setText("No vehicle assigned to you");
                            }
                            Lr.setVisibility(View.VISIBLE);

                            if (!isMyServiceRunning(mSensorService.getClass())) {
                                startService(mServiceIntent);
                            }

                        } else {
                            Lr.setVisibility(View.GONE);
                        }


                    }
                } else {
                    new AlertDialog.Builder(After_owner_login_copy.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Thank you for uploading all required documents.")
                            .setMessage("Your verification is pending! Please contact our customer support for verification. Thank you.")
                            .setPositiveButton("Contact", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String phone = "917002608241";
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                    startActivity(intent);
                                    dialog.cancel();

                                }
                            })

                            .show();
                }

            } else {
                new AlertDialog.Builder(After_owner_login_copy.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setTitle("Thank You!Almost there")
                        .setMessage("Please provide more infromation to become as a HelloCab partner.")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent o = new Intent(After_owner_login_copy.this, Owner_driver_add.class);
                                o.putExtra("Driver", "SELF");
                                o.putExtra("Driver_no", _PhoneNo);
                                o.putExtra("my_lat", My_lat);
                                o.putExtra("my_long", My_long);
                                o.putExtra("reg", regId);
                                startActivity(o);
                                finish();
                                dialog.cancel();
                            }
                        })

                        .show();
            }


        }
    }

    private class GetDrivers extends AsyncTask<String, Void, Void> {


        private int Driver_Rating = 0;
        private String Driver_selected_name;
        private String OTP;
        private String Driver_selected_image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mItems.clear();
            mItems2.clear();


        }

        @Override
        protected Void doInBackground(String... arg0) {
            OTP = arg0[0];
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config_URL.GET_SETTINGS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray Drivers = jsonObj.getJSONArray("Driver_Details");
                    // looping through All Contacts
                    if (_PhoneNo != null) {

                        int relation = 0;
                        for (int i = 0; i < Drivers.length(); i++) {
                            JSONObject c = Drivers.getJSONObject(i);
                            //relation = c.getInt("Owner_ID");
                            if (!c.isNull("Driver_OTP") && c.getInt("Driver_OTP") == Integer.parseInt(OTP)) {

                                //Driver_selected=c.getString("Phone_NO");
                                Driver_selected_name = (c.getString("Name"));
                                Driver_selected_image = (c.getString("Photo"));
                                if (!c.isNull("Rating")) {
                                    Driver_Rating = (c.getInt("Rating"));
                                } else {
                                    Driver_Rating = 0;
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
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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
            progressBar.setVisibility(View.GONE);
            if (Driver_selected_name != null) {
                open_driver(Driver_selected_name, Driver_Rating, OTP, Driver_selected_image);
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Wrong identification no!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                open_coupon();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();

            }
        }
    }

    private class UpdateDriver extends AsyncTask<String, Integer, String> {


        private boolean success = false;
        private String driver_got;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            String Id_Driver = args[0];
            return uploadFile(Id_Driver);
        }

        private String uploadFile(String id_Driver) {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile", _PhoneNo)
                        .addFormDataPart("driver", "")
                        .addFormDataPart("driver_otp", id_Driver)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_DRIVER_UPDATE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars = res.split("error");
                if (pars[1].contains("false")) {
                    success = true;
                    String[] pars_ = pars[1].split("false,");
                    JSONObject jObj = new JSONObject("{".concat(pars_[1]));
                    JSONObject user = jObj.getJSONObject("user");
                    driver_got = user.getString("Driver_no");
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
            progressBar.setVisibility(View.GONE);
            if (success) {
                mDatabase.child("Driver_Online").child(driver_got).child("Owner_Phone_no").setValue(_PhoneNo);

                recreate();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                open_coupon();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();

            }
        }

    }

    private class UpdateVehicle extends AsyncTask<String, Integer, String> {


        private boolean success = false;
        private String vehicle_no_got;
        private String dNo, vNo;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            dNo = args[0];
            vNo = args[1];
            return uploadFile(dNo, vNo);
        }

        private String uploadFile(String dNo, String vNO) {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile", _PhoneNo)
                        .addFormDataPart("driver", dNo)
                        .addFormDataPart("vehicle", vNO)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_CAR_UPDATE)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);
                String[] pars = res.split("error");
                if (pars[1].contains("false")) {
                    success = true;
                    String[] pars_ = pars[1].split("false,");
                    JSONObject jObj = new JSONObject("{".concat(pars_[1]));
                    JSONObject user = jObj.getJSONObject("user");
                    vehicle_no = user.getString("Vehicle_No");
                    vehicle_type = user.getString("Vehicle_Type");

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
            progressBar.setVisibility(View.GONE);
            if (success) {
                if (!Owner_is_driver) {
                    new AlertDialog.Builder(After_owner_login_copy.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Are you sure!")
                            .setMessage("Vehicle " + vNo + " is assigned to " + Driver_name)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDatabase.child("Driver_Online").child(dNo).child("Driver_Vehicle").setValue(vehicle_type);
                                    mDatabase.child("Driver_Online").child(dNo).child("Driver_Vehicle_no").setValue(vNo);
                                    mDatabase.child("Driver_Online").child(dNo).child("Offline").setValue("YES");
                                    dialog.cancel();
                                    recreate();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteVehicle().execute(vNo, dNo);
                                    dialog.cancel();
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(After_owner_login_copy.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Are you sure!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDatabase.child("Driver_Online").child(dNo).child("Driver_Vehicle").setValue(vehicle_type);
                                    mDatabase.child("Driver_Online").child(dNo).child("Driver_Vehicle_no").setValue(vNo);
                                    mDatabase.child("Driver_Online").child(dNo).child("Offline").setValue("YES");
                                    dialog.cancel();
                                    recreate();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteVehicle().execute(vNo, dNo);
                                    dialog.cancel();
                                }
                            }).show();
                }
            }

        }

    }

    private class DeleteVehicle extends AsyncTask<String, Integer, String> {


        private boolean success = false;
        private String vNo, dNo;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            vNo = args[0];
            dNo = args[1];
            return uploadFile(vNo, dNo);
        }

        private String uploadFile(String vno, String dno) {
            // TODO Auto-generated method stub

            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile", _PhoneNo)
                        .addFormDataPart("driver", dno)
                        .addFormDataPart("vehicle", vno)
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

            if (success) {
                mDatabase.child("Driver_Online").child(dNo).removeValue();
                if (dNo.contains(_PhoneNo)) {
                    Owner_is_driver = false;
                    vehicle_type = null;
                    Rl.setVisibility(View.GONE);
                    Lr.setVisibility(View.GONE);
                    bottomNavigationView.setAnimation(animslideU);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    noOwner.setVisibility(View.VISIBLE);
                    noOwner.setText("No vehicle assigned");
                } else {
                    noOwner.setVisibility(View.VISIBLE);
                    noOwner.setText("No vehicle assigned to " + Driver_selected);
                }
                recreate();
            }

        }

    }

    private class FirebaseDataListener_got implements ValueEventListener {

        private String Driver_accept, User_accept;
        private double Driver_lat = 0, Driver_long = 0, User_From_lat = 0, User_From_long = 0;
        private Marker markerd;
        private LatLng To_latLong;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (UNIQUE_RIDE != null) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Latitude").setValue(dft.format(My_lat));
                    mDatabase.child("Accepted_Ride").child(con).child("Driver_First_Longitude").setValue(dft.format(My_long));

                    Driver_accept = (String) dataSnapshot.child("START").getValue();
                    User_accept = (String) dataSnapshot.child("UserAccept").getValue();
                    User_mobile = (String) dataSnapshot.child("UserMobile").getValue();


                    if (data_f.getDistance_travel() != null && Distance_travel == 0) {
                        Distance_travel = Double.parseDouble(data_f.getDistance_travel());
                    }
                    if (Driver_accept != null) {
                        if (Driver_accept.contains("YES")) {
                            if (!drawn) {
                                wait = false;
                                Start_ride = false;
                                go = true;
                                ask = false;
                                stop = false;
                                got = false;
                                drawn = true;
                                Rl.setAnimation(animslideU);
                                Rl.setVisibility(View.VISIBLE);
                                Stop_ride.setAnimation(animslideU);
                                Stop_ride.setVisibility(View.VISIBLE);
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
                            }
                        } else if (Driver_accept.contains("NO")) {
                            stop = true;
                        }
                    } else {
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
                                Heading_.setVisibility(View.GONE);
                                Rl.setVisibility(View.VISIBLE);
                                Drivers.setVisibility(View.GONE);
                                Heading_.setVisibility(View.GONE);
                                Lr.setAnimation(animslideD);
                                Lr.setVisibility(View.GONE);
                                Rl.setAnimation(animslideU);
                                Rl.setVisibility(View.VISIBLE);
                                Confirm_ride.setText("Please wait for confirmation");
                                Amb.setVisibility(View.VISIBLE);
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

                            if (!go) {
                                if (marker != null) {
                                    marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                    marker.setVisible(true);

                                }

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
                            // Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
                            proSwipeBtn.setVisibility(View.VISIBLE);
                            if (!go) {

                                if (markerUser != null) {
                                    markerUser.setVisible(true);
                                    markerUser.setPosition(new LatLng(User_From_lat, User_From_long));
                                }

                                if (marker != null) {
                                    marker.setPosition(new LatLng(Driver_lat, Driver_long));
                                    marker.setVisible(true);

                                }

                            }

                        } else {
                            cancelled = true;
                        }

                    }
                    if (go) {
                        data_f = dataSnapshot.getValue(Book.class);
                        assert data_f != null;
                        if (data_f.getDriver_First_Longitude() != null
                                && data_f.getDriver_First_Latitude() != null
                                && data_f.getBook_To_Latitude() != null &&
                                data_f.getBook_To_Longitude() != null &&
                                data_f.getTax_names() != null) {
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
                            if (data_f.getTax_percentage() != null) {
                                Tax_percentage = Double.parseDouble(data_f.getTax_percentage());
                                if (data_f.getCoupon_code() != null && data_f.getCoupon_value() != null) {
                                    Coupon_code = data_f.getCoupon_code();
                                    Coupon_value = Double.parseDouble(data_f.getCoupon_value());
                                }
                            }

                            Cost = Cost - Coupon_value;
                            Taotal_cost = round(Cost, 2) + round(((Cost * Tax_percentage * 0.01)), 2);

                            mDatabase.child("Accepted_Ride").child(con).child("Bill").setValue(dfto.format(Taotal_cost));
                            if (Distance_travel == 0 && mPathPolygonPoints.size() > 1) {
                                markerPoints = new ArrayList<LatLng>();
                                markerPoints.clear();
                                markerPoints.add(mPathPolygonPoints.get(0));
                                markerPoints.add(mPathPolygonPoints.get(1));
                                if (markerPoints.size() == 2) {
                                    LatLng origin = markerPoints.get(0);
                                    LatLng dest = markerPoints.get(1);
                                    String url = getDirectionsUrl(origin, dest);
                                    new DownloadTask().execute(url);

                                }
                            }

                        } else {
                            cancelled = true;
                        }
                    }
                } else {
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("OnRide").setValue("NO");
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                    UNIQUE_RIDE = null;
                    Intent i = new Intent(After_owner_login_copy.this, Splash_screen.class);
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

    class DeleteDriver extends AsyncTask<Void, Integer, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
                        .addFormDataPart("driver", Driver_selected)
                        .build();
                Request request = new Request.Builder()
                        .url(Config_URL.URL_DRIVER_DELETE)
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
            progressBar.setVisibility(View.GONE);
            mDatabase.child("Driver_Online").child(Driver_selected).removeValue();
            //mDatabase.child("Driver_Online").child(Driver_selected).child("Driver_Vehicle").removeValue();
            //mDatabase.child("Driver_Online").child(Driver_selected).child("Driver_Vehicle_no").removeValue();
            if (Driver_selected.contains(_PhoneNo)) {
                Rl.setVisibility(View.GONE);
                Lr.setVisibility(View.GONE);
                bottomNavigationView.setAnimation(animslideU);
                bottomNavigationView.setVisibility(View.VISIBLE);
                Owner_is_driver = false;
            }
            recreate();
        }

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
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

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private float distance = 0;
        private double Total_cost = 0;

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
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


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        String pars = (String) point.get("distance");
                        String[] pars1 = pars.split(" ");
                        distance = Float.parseFloat(pars1[0]) / 1000;
                        if (Start_ride && !msg && User_mobile != null) {
                            if (Float.parseFloat(pars1[0]) < 10) {
                                new getFCM().execute(User_mobile);
                            }
                        }
                        if (go) {
                            if (con != null) {
                                Distance_travel = Distance_travel + distance;
                                Total_cost = (Minimum_fare + (Distance_travel) * Hourly_fare);
                                mDatabase.child("Accepted_Ride").child(con).child("Distance_travel").setValue(dfto.format(Distance_travel));
                                mDatabase.child("Accepted_Ride").child(con).child("Cost").setValue(dfto.format(Total_cost));
                            }
                        }
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        if (con != null) {

                            mDatabase.child("Accepted_Ride").child(con).child("Time_travel").setValue(duration);

                        }
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.startCap(new SquareCap());
                lineOptions.endCap(new SquareCap());
                lineOptions.jointType(ROUND);
                lineOptions.color(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));


            }
            if (lineOptions != null) {
                polylineFinal = googleMap.addPolyline(lineOptions);

            }


        }

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
                        if (!d.isNull("Phone_No")) {

                            if (d.getString("Phone_No").contains(arg0[0])) {

                                fcm_token = d.getString("Firebase_Token");
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
            if (fcm_token != null) {

                new CreateFCM().execute(fcm_token);

            }


        }
    }

    class CreateFCM extends AsyncTask<String, Integer, String> {
        private boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            String fcm = args[0];
            return uploadFile(fcm);
        }

        private String uploadFile(String fcm) {
            // TODO Auto-generated method stub
            String res = null;


            try {

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Request for ride")
                        .addFormDataPart("message", "Your Ride has arraived")
                        .addFormDataPart("push_type", "individual")
                        .addFormDataPart("regId", fcm)
                        .addFormDataPart("include_image", "FALSE")
                        .addFormDataPart("image", "")
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
            if (success) {
                proSwipeBtn.showResultIcon(true);
                Toast.makeText(getApplicationContext(), "User notified", Toast.LENGTH_SHORT).show();

            } else {
                proSwipeBtn.showResultIcon(false);
            }

        }

    }

}
