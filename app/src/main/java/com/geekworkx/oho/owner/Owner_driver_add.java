package com.geekworkx.oho.owner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekworkx.oho.Adapters.After_owner_login_copy;
import com.geekworkx.oho.Adapters.GooglemapApp;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Crop.ImagePicker;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.HttpHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;

/**
 * Created by parag on 05/01/18.
 */

public class Owner_driver_add extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Owner_driver_add.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 1001;
    private static final int REQUEST_EXTERNAL_STORAGE = 1002;
    private static final int IMAGE_ = 1003;

    private Toolbar toolbar;
    private EditText dFirstname,dBirthdate,dEmail,dIdentity,dStreetfirst,dCity,dState,dCountry,dPin;
    private EditText d_PhoneNo;
    private ImageView dImage;
    private Button Submit1;
    private ConnectionDetector cd;
    private boolean isInternetPresent=false;
    private PrefManager pref;
    private ProgressBar progressBar;
    private String Name,_PhoneNo,WHO;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String filePath;
    private int mYear, mMonth, mDay;
    private String Cam;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String Drivers;
    private String Owner_name,Owner_image,Owner_mobile,Owner_last,Owner_email,Owner_address_1,Owner_address_2,Owner_city,Owner_state,Owner_country,Owner_zip,IDENTIFICATION,UNIQUE_ID;
    private String Driver_name,Driver_image,Driver_mobile,Driver_email,Driver_address,Driver_city,Driver_state,Driver_country,Driver_zip,Driver_identification_mark;
    private String Owner_birthdate;
    private String Driver_birthdate,Driver_pancard,Driver_addressproof,Driver_aadhar,Driver_cancelcheck;
    private String Owner_pancard,Owner_addressproof,Owner_aadhar="",Owner_cancelcheck;
    private DatabaseReference mDatabase;
    private String dDriver_mobile;
    private boolean Again=false;
    private String fileImage;
    private String Drivers_no;
    private double My_lat=0,My_long=0;
    private String Again_upload="NO";
    private TextView Text101,Text102;
    private int Online=0;
    private int Owner_ID=0;
    private String mobileIp;
    private String regId;
    private boolean reload=false;
    private DatePickerDialog datePickerDialog;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_driver);

        toolbar = findViewById(R.id.toolbardd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        d_PhoneNo=findViewById(R.id.mobileo);
        dFirstname=findViewById(R.id.fnameo);
        dBirthdate=findViewById(R.id.bdateo);
        dEmail=findViewById(R.id.emailo);
        dIdentity=findViewById(R.id.identityo);
        dStreetfirst=findViewById(R.id.faddresso);
        dCity=findViewById(R.id.cityo);
        dState=findViewById(R.id.stateo);
        dCountry=findViewById(R.id.countryo);
        dPin=findViewById(R.id.pino);
        dImage=findViewById(R.id.img_profilo);
        Text101=findViewById(R.id.textView101);
        Text102=findViewById(R.id.textView102);

        Submit1=findViewById(R.id.submit);




        Submit1.setOnClickListener(this);
        dImage.setOnClickListener(this);
        dBirthdate.setOnClickListener(this);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        progressBar=(ProgressBar)findViewById(R.id.progress_driver);
        pref = new PrefManager(this);
        HashMap<String, String> user = pref.getUserDetails();
        Name = user.get(PrefManager.KEY_NAME);
        WHO = user.get(PrefManager.KEY_WHO);
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        Intent i=getIntent();
        Drivers=i.getStringExtra("Driver");
        regId=i.getStringExtra("reg");
        Drivers_no=i.getStringExtra("Driver_no");
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WHO.contains("OWNER")) {

                    Intent o = new Intent(Owner_driver_add.this, After_owner_login_copy.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                } else {
                    Intent o = new Intent(Owner_driver_add.this, GooglemapApp.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                }
            }
        });
        if(Drivers.contains("SELF")){
            Text101.setText("Add");
            Text102.setText(" Information");
            dIdentity.setVisibility(View.GONE);
        }else if(Drivers.contains("OWNER")){
            Text101.setText("Add");
            Text102.setText(" Information");
            dIdentity.setVisibility(View.VISIBLE);
        }else if(Drivers.contains("DRIVER")){
            Text101.setText("Add");
            Text102.setText(" Driver");
            dIdentity.setVisibility(View.VISIBLE);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if((Drivers.contains("SELF")||Drivers.contains("OWNER")) && !Again) {
                new GetCustomer().execute();
            }else{
                if(Drivers_no!=null && !reload) {
                    reload=true;
                    new GetAll().execute();
                }
            }

            if(Again){
                if(bitmap!=null) {
                    dImage.setImageBitmap(bitmap);
                }
            }

        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet,Connect to internet", Snackbar.LENGTH_LONG).show();

        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if(!TextUtils.isEmpty(d_PhoneNo.getText().toString())&& !TextUtils.isEmpty(dFirstname.getText().toString())
                        &&!TextUtils.isEmpty(dBirthdate.getText().toString())&&
                        !TextUtils.isEmpty(dStreetfirst.getText().toString())&& !TextUtils.isEmpty(dPin.getText().toString())){
                    if(d_PhoneNo.getText().toString().contains(_PhoneNo)) {
                        dDriver_mobile = _PhoneNo;
                    }else{
                        if(d_PhoneNo.getText().toString().startsWith("91")) {
                            dDriver_mobile =  d_PhoneNo.getText().toString();
                        }else{
                            dDriver_mobile = "91"+d_PhoneNo.getText().toString();
                        }
                    }
                    if(Drivers.contains("SELF")){
                        new PostOwnerTOServer().execute();
                    }
                    else
                     {
                        if(!TextUtils.isEmpty(dIdentity.getText().toString())) {
                            if (Drivers.contains("DRIVER")) {
                                new PostDataTOServer().execute();
                            } else if (Drivers.contains("OWNER")) {
                                new PostOwnerAsDriverTOServer().execute();
                            }
                        }else{
                           Toast.makeText(getApplicationContext(),"Please input identification mark",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Please fill up the form ",Toast.LENGTH_SHORT).show();
                }
                 if(TextUtils.isEmpty(d_PhoneNo.getText().toString())){
                d_PhoneNo.setError("Can not empty");
            }
                if(TextUtils.isEmpty(dFirstname.getText().toString())){
                    dFirstname.setError("Can not empty");
                }
                if(TextUtils.isEmpty(dBirthdate.getText().toString())){
                    dBirthdate.setError("Can not empty");
                }
                if(TextUtils.isEmpty(dIdentity.getText().toString())){
                    dIdentity.setError("Can not empty");
                }
                if(TextUtils.isEmpty(dStreetfirst.getText().toString())){
                    dStreetfirst.setError("Can not empty");
                }
                if(TextUtils.isEmpty(dPin.getText().toString())){
                    dPin.setError("Can not empty");
                }

                if(dStreetfirst.getText().toString().matches(dCity.getText().toString())
                       ){
                    Toast.makeText(getApplicationContext(),"Address field can not be same",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.img_profilo:
                Cam="Profile";
                if(isCameraAllowed()) {
                    pickImage();
                }
                break;
            case R.id.bdateo:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dBirthdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear-18, mMonth, mDay);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                datePickerDialog.show();
                break;




        }
    }

    public void pickImage() {
        ImagePicker.pickImage(this);
    }
    private class PostOwnerTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        private File destination;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            if(filePath.contains("139.59.38.160")) {
                if (verifyStoragePermissions()) {
                    if (((BitmapDrawable) dImage.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) dImage.getDrawable()).getBitmap();
                        final File mediaStorageDir = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                        destination = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                        FileOutputStream fo;
                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            fileImage = destination.getPath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }else {
                fileImage = filePath;
            }


        }

        protected String doInBackground(Void... args) {
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            try {
                File sourceFile = new File(fileImage);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("owner_phone",_PhoneNo)
                        .addFormDataPart("owner_name",dFirstname.getText().toString())
                        .addFormDataPart("owner_email",dEmail.getText().toString())
                        .addFormDataPart("owner_birth_date",dBirthdate.getText().toString())
                        .addFormDataPart("owner_address",dStreetfirst.getText().toString())
                        .addFormDataPart("owner_city",dCity.getText().toString())
                        .addFormDataPart("owner_state", dState.getText().toString())
                        .addFormDataPart("owner_country",dCountry.getText().toString())
                        .addFormDataPart("owner_pin",dPin.getText().toString())
                        .addFormDataPart("owner_image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))

                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_ADD_OWNER_INFORMATION)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;


                }else {
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

            progressBar.setVisibility(View.GONE);

            if (success) {
                if (destination != null){
                    File file = new File(destination.getPath());
                    file.delete();
                    if (file.exists()) {
                        try {
                            file.getCanonicalFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (file.exists()) {
                            getApplicationContext().deleteFile(file.getName());
                        }
                    }
                }
                Intent o = new Intent(Owner_driver_add.this, driver_documents_add.class);
                o.putExtra("Driver",Drivers);
                o.putExtra("Driver_mobile",_PhoneNo );
                o.putExtra("my_lat",My_lat);
                o.putExtra("my_long",My_long);
                startActivity(o);
                finish();

            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error", Snackbar.LENGTH_LONG).show();

            }

        }


    }


    private class PostOwnerAsDriverTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        private File destination;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            if(filePath.contains("139.59.38.160")) {
                if (verifyStoragePermissions()) {
                    if (((BitmapDrawable) dImage.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) dImage.getDrawable()).getBitmap();
                        final File mediaStorageDir = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                        destination = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                        FileOutputStream fo;
                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            fileImage = destination.getPath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }else {
                fileImage = filePath;
            }


        }

        protected String doInBackground(Void... args) {
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            try {
                File sourceFile = new File(fileImage);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("owner_phone",_PhoneNo)
                        .addFormDataPart("owner_name",dFirstname.getText().toString())
                        .addFormDataPart("owner_email",dEmail.getText().toString())
                        .addFormDataPart("owner_birth_date",dBirthdate.getText().toString())
                        .addFormDataPart("owner_address",dStreetfirst.getText().toString())
                        .addFormDataPart("owner_city",dCity.getText().toString())
                        .addFormDataPart("owner_state", dState.getText().toString())
                        .addFormDataPart("owner_country",dCountry.getText().toString())
                        .addFormDataPart("owner_pin",dPin.getText().toString())
                        .addFormDataPart("Identification",dIdentity.getText().toString())
                        .addFormDataPart("Firebase_Token",regId)
                        .addFormDataPart("owner_image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                        .addFormDataPart("IP",mobileIp)

                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_OWNER_AS_DRIVER)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;


                }else {
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

            progressBar.setVisibility(View.GONE);

            if (success) {
                if (destination != null){
                    File file = new File(destination.getPath());
                    file.delete();
                    if (file.exists()) {
                        try {
                            file.getCanonicalFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (file.exists()) {
                            getApplicationContext().deleteFile(file.getName());
                        }
                    }
                }
                Intent o = new Intent(Owner_driver_add.this, driver_documents_add.class);
                o.putExtra("Driver",Drivers);
                o.putExtra("Driver_mobile",dDriver_mobile );
                o.putExtra("my_lat",My_lat);
                o.putExtra("my_long",My_long);
                startActivity(o);
                finish();

            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error", Snackbar.LENGTH_LONG).show();

            }

        }


    }

    private class PostDataTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        private File destination;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            if(filePath!=null && filePath.contains("139.59.38.160")) {
                if (verifyStoragePermissions()) {
                    if (((BitmapDrawable) dImage.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) dImage.getDrawable()).getBitmap();
                        final File mediaStorageDir = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                        destination = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                        FileOutputStream fo;
                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            fileImage = destination.getPath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }else {
                fileImage = filePath;
            }


        }

        protected String doInBackground(Void... args) {
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            try {
                File sourceFile = new File(fileImage);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("driver_phone",dDriver_mobile)
                        .addFormDataPart("driver_first_name", dFirstname.getText().toString())
                        .addFormDataPart("driver_email", dEmail.getText().toString())
                        .addFormDataPart("driver_cut_mark",dIdentity.getText().toString())
                        .addFormDataPart("driver_birth_date", dBirthdate.getText().toString())
                        .addFormDataPart("driver_address_1",dStreetfirst.getText().toString())
                        .addFormDataPart("driver_city",dCity.getText().toString())
                        .addFormDataPart("driver_state", dState.getText().toString())
                        .addFormDataPart("driver_country",dCountry.getText().toString())
                        .addFormDataPart("driver_zip",dPin.getText().toString())
                        .addFormDataPart("driver_lat", String.valueOf(My_lat))
                        .addFormDataPart("driver_long", String.valueOf(My_long))
                        .addFormDataPart("driver_image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                        .addFormDataPart("IP",mobileIp)
                        .addFormDataPart("who",WHO)
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_OWNER_DRIVER)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;


                }else {
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

            progressBar.setVisibility(View.GONE);

                if (success) {
                    if (destination != null){
                        File file = new File(destination.getPath());
                        file.delete();
                        if (file.exists()) {
                            try {
                                file.getCanonicalFile().delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file.exists()) {
                                getApplicationContext().deleteFile(file.getName());
                            }
                        }
                    }

                    Intent o = new Intent(Owner_driver_add.this, driver_documents_add.class);
                    o.putExtra("Driver",Drivers);
                    o.putExtra("Driver_mobile",dDriver_mobile );
                    o.putExtra("my_lat",My_lat);
                    o.putExtra("my_long",My_long);
                    startActivity(o);
                    finish();

            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Please add image", Snackbar.LENGTH_LONG).show();

            }

        }


    }


    public boolean verifyStoragePermissions() {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    private boolean isCameraAllowed() {
        int result = ContextCompat.checkSelfPermission(Owner_driver_add.this, CAMERA);

        //If permission is granted returning true
        if (result != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        }else {
            return true;
        }
        return false;
    }
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Owner_driver_add.this, CAMERA)){
            Toast.makeText(Owner_driver_add.this,"This permission is helpful",Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(Owner_driver_add.this,new String[]{CAMERA},MY_PERMISSIONS_REQUEST_USE_CAMERA);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {


                    Toast.makeText(Owner_driver_add.this, "Camera Permission is helpful", Toast.LENGTH_SHORT).show();
                    Intent i = Owner_driver_add.this.getIntent();
                    finish();
                    startActivity(i);

                }
                break;
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_PICK) {
            ImagePicker.beginCrop(this, resultCode, data);
        } else if (requestCode == ImagePicker.REQUEST_CROP) {

             bitmap = ImagePicker.getImageCropped(this, resultCode, data,
                    ImagePicker.ResizeType.FIXED_SIZE, 300);
                if(bitmap!=null) {
                    Again=true;
                    dImage.setImageBitmap(bitmap);
                    if(verifyStoragePermissions()) {
                        if (((BitmapDrawable) dImage.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) dImage.getDrawable()).getBitmap();
                            final File mediaStorageDir = new File(
                                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(new Date());
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            File destination = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            FileOutputStream fo;
                            try {
                                destination.createNewFile();
                                fo = new FileOutputStream(destination);
                                fo.write(bytes.toByteArray());
                                fo.close();
                                filePath = destination.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private class GetCustomer extends AsyncTask<Void, Void, Void> {


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
                    JSONArray Owner = jsonObj.getJSONArray("Owner_Details");
                    JSONArray Settings = jsonObj.getJSONArray("Settings");
                    JSONArray Version = jsonObj.getJSONArray("Version_driver");

                    for (int i = 0; i < Settings.length(); i++) {
                        JSONObject c = Settings.getJSONObject(i);
                        Online = c.getInt("Service_Online");

                    }

                        for (int i = 0; i < Owner.length(); i++) {
                            JSONObject c = Owner.getJSONObject(i);
                            String relation = c.getString("Phone_No");


                            if (relation.matches(_PhoneNo) && Online==1) {
                                Owner_name = c.getString("Name");
                                Owner_mobile = c.getString("Phone_No");
                                Owner_ID = c.getInt("ID");
                                Owner_image = c.getString("Photo");
                                Owner_email=c.getString("Email");
                                Owner_address_1=c.getString("Address");
                                Owner_city=c.getString("City");
                                Owner_state=c.getString("State");
                                Owner_country=c.getString("Country");
                                Owner_zip=c.getString("Pin");
                                Owner_birthdate=c.getString("Date_Of_Birth");
                                Owner_pancard=c.getString("Pancard_Photo");
                                Owner_addressproof=c.getString("Addressproof_Photo");
                                Owner_aadhar=c.getString("Aadhar_Card_Photo");
                                Owner_cancelcheck=c.getString("Cancel_Cheque_Photo");
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
                if(!TextUtils.isEmpty(d_PhoneNo.getText().toString())&& !TextUtils.isEmpty(dFirstname.getText().toString())
                        &&!TextUtils.isEmpty(dBirthdate.getText().toString())&& !TextUtils.isEmpty(dIdentity.getText().toString())&&
                        !TextUtils.isEmpty(dStreetfirst.getText().toString())&& !TextUtils.isEmpty(dPin.getText().toString()) && !TextUtils.isEmpty(filePath)){

                }else{
                    Toast.makeText(getApplicationContext(),"Please fill up the form and add image",Toast.LENGTH_SHORT).show();
                }
                d_PhoneNo.setText(Owner_mobile);
                if(Owner_name!=null && !Owner_name.contains("null")) {
                    dFirstname.setText(Owner_name);
                }
                if(Owner_email!=null && !Owner_email.contains("null")) {
                    dEmail.setText(Owner_email);
                }
                if(Owner_birthdate!=null && !Owner_birthdate.contains("null")) {
                    dBirthdate.setText(Owner_birthdate);
                }
                if(Owner_address_1!=null && !Owner_address_1.contains("null")) {
                    dStreetfirst.setText(Owner_address_1);
                }
                if(Owner_city!=null && !Owner_city.contains("null")) {
                    dCity.setText(Owner_city);
                }
                if(Owner_state!=null && !Owner_state.contains("null")) {
                    dState.setText(Owner_state);
                }
                if(Owner_country!=null && !Owner_country.contains("null")) {
                    dCountry.setText(Owner_country);
                }
                if(Owner_zip!=null && !Owner_zip.contains("null")) {
                    dPin.setText(Owner_zip);
                }
                if(Owner_image!=null && !Owner_image.contains("null")) {
                    Picasso.Builder builder = new Picasso.Builder(Owner_driver_add.this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(Owner_image).into(dImage);
                    filePath = Owner_image;
                }

                reload=true;
            }
        }

    }

    private class GetAll extends AsyncTask<Void, Void, Void> {


        private String Owner_cut;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            if(WHO.contains("DRIVER")){
                Drivers_no=_PhoneNo;
            }

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

                    // looping through All Contacts
                    if (WHO!= null) {
                        for (int i = 0; i < Owner.length(); i++) {
                            JSONObject c = Owner.getJSONObject(i);
                            String relation = c.getString("Phone_No");


                            if (relation.matches(_PhoneNo)) {
                                Owner_name = c.getString("Name");
                                Owner_mobile = c.getString("Phone_No");
                                Owner_ID = c.getInt("ID");
                                Owner_image = c.getString("Photo");
                                }
                        }

                        for (int i = 0; i < Drivers.length(); i++) {
                            JSONObject c = Drivers.getJSONObject(i);
                            String relation = c.getString("Phone_No");
                            if (!c.isNull("Driving_License_Photo") && c.getString("Phone_No").matches(Drivers_no)) {

                                if (relation.contains(Drivers_no)) {
                                    Driver_identification_mark=c.getString("Identification_Mark");
                                    Driver_name=c.getString("Name");
                                    Driver_email=c.getString("Email");
                                    Driver_address=c.getString("Address");
                                    Driver_city=c.getString("City");
                                    Driver_state=c.getString("State");
                                    Driver_zip=c.getString("Pin");
                                    Driver_birthdate=c.getString("Date_Of_Birth");
                                    Driver_pancard=c.getString("Pancard_Photo");
                                    Driver_addressproof=c.getString("Addressproof_Photo");
                                    Driver_aadhar=c.getString("Aadhar_Card_Photo");
                                    Driver_cancelcheck=c.getString("Cancel_Cheque_Photo");
                                    Driver_country=c.getString("Country");
                                    Driver_mobile=c.getString("Phone_No");
                                    Driver_image=c.getString("Photo");
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


            if (Driver_mobile != null) {
                if(!TextUtils.isEmpty(d_PhoneNo.getText().toString())&& !TextUtils.isEmpty(dFirstname.getText().toString())
                        &&!TextUtils.isEmpty(dBirthdate.getText().toString())&& !TextUtils.isEmpty(dIdentity.getText().toString())&&
                        !TextUtils.isEmpty(dStreetfirst.getText().toString())&& !TextUtils.isEmpty(dPin.getText().toString()) && !TextUtils.isEmpty(filePath)){

                }else{
                    Toast.makeText(getApplicationContext(),"Please fill up the form and add image",Toast.LENGTH_SHORT).show();
                }
                d_PhoneNo.setText(Driver_mobile);
                dFirstname.setText(Driver_name);
                dEmail.setText(Driver_email);
                dBirthdate.setText(Driver_birthdate);
                dStreetfirst.setText(Driver_address);
                dCity.setText(Driver_city);
                dState.setText(Driver_state);
                dCountry.setText(Driver_country);
                dPin.setText(Driver_zip);
                dIdentity.setText(Driver_identification_mark);

                    Picasso.Builder builder = new Picasso.Builder(Owner_driver_add.this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    builder.build().load(Driver_image).into(dImage);
                    filePath = Driver_image;

                dDriver_mobile=d_PhoneNo.getText().toString();

            }
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_white, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh1:

                recreate();



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        if (WHO.contains("OWNER")) {

            Intent o = new Intent(Owner_driver_add.this, After_owner_login_copy.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
        } else {
            Intent o = new Intent(Owner_driver_add.this, GooglemapApp.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
        }

    }
}
