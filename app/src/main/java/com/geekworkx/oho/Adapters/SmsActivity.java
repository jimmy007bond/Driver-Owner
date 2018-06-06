package com.geekworkx.oho.Adapters;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Crop.ImagePicker;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.R;
import com.geekworkx.oho.Splash_screen.Splash_screen;
import com.geekworkx.oho.URLS.Config_URL;

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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;


/**
 * Created by parag on 13/01/17.
 */
public class SmsActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int ALARM_REQUEST_CODE = 301;
    private HeightWrappingViewPager viewPager;
    private ViewPagerAdapter adapter;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private static final String TAG = SmsActivity.class.getSimpleName();
    private ImageView Profile_image;
    private EditText Name,_PhoneNo;
    private static String Name_giver,Identificationr="",_PhoneNo_giver="",_PhoneNo_again_="";
    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 200;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private Compress_image compress;
    private File pic;
    private Uri picUri;
    private String filePath;
    private Button First_button;
    private ProgressBar progressBar;
    private PrefManager pref;
    private Button Second_button,Third_button;
    private String otp;
    private EditText inputOtp;
    private PrefUtil prefUtil;
    private String WHO;
    private String UNIQUE_ID_USER,UNIQUE_ID_RIDER;
    private double From_lat,From_long,To_lat,To_long;
    private ScrollView Scrool;
    private double My_long,My_lat;
    private static final int REQUEST_INVITED=101;
    private String USER;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private String URL_OWNER_DRIVER;
    private String URL_VERIFY_OTP;
    private Button Already,Forgot_i;
    private EditText _PhoneNo_again,Identification;
    private boolean again=false;
    private String URL_OWNER_DRIVER_VALIDATION;
    private boolean OTP=false;
    private static final int IMAGE_1 = 101;
    private static final int IMAGE_2 = 102;
    private TextView LogingAs,Login_Already;
    private Bitmap bitmap;
    private int Profile=0;
    private String mobileIp="";
    private boolean forgot=false;
    private String URL_FORGOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        viewPager = (HeightWrappingViewPager) findViewById(R.id.viewPagerVertical);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        progressBar=(ProgressBar)findViewById(R.id.progress_rider);
        prefUtil=new PrefUtil(this);
        pref = new PrefManager(this);
        Profile_image=(ImageView)findViewById(R.id.profile_picture);
        Name= (EditText) findViewById(R.id.u_name);
        _PhoneNo= (EditText) findViewById(R.id.u_mobile);
        otp=inputOtp.getText().toString();
        compress=new Compress_image(this);
        First_button= (Button) findViewById(R.id.first_button);
        Second_button= (Button) findViewById(R.id.second_butona);
        Third_button=findViewById(R.id.third_button);
        Forgot_i=findViewById(R.id.forgot_identity);
        _PhoneNo_again=findViewById(R.id.mobile_already);
        Identification=findViewById(R.id.identification);
        Already=findViewById(R.id.already);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        WHO=i.getStringExtra("WHO");
        LogingAs=findViewById(R.id.loginAs);
        Login_Already=findViewById(R.id.login_already);

        if(WHO.contains("OWNER")){
            LogingAs.setText("Login as Owner");
            Login_Already.setText("Login as Owner");
           // Identification.setHint("Owner ID no");
            URL_OWNER_DRIVER=Config_URL.URL_REQUEST_SMS_OWNER;
            URL_VERIFY_OTP=Config_URL.URL_VERIFY_OTP_OWNER;
            URL_OWNER_DRIVER_VALIDATION=Config_URL.URL_VALID_OWNER;

        }else if(WHO.contains("DRIVER")){
            LogingAs.setText("Login as Driver");
            Login_Already.setText("Login as Driver");
            //Identification.setHint("Driver ID no");
            URL_OWNER_DRIVER=Config_URL.URL_REQUEST_SMS_DRIVER;
            URL_VERIFY_OTP=Config_URL.URL_VERIFY_OTP_DRIVER;
            URL_OWNER_DRIVER_VALIDATION=Config_URL.URL_VALID_DRIVER;
        }


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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (verifyStoragePermissions()) {
                First_button.setOnClickListener(this);
                Second_button.setOnClickListener(this);
                Profile_image.setOnClickListener(this);
                Already.setOnClickListener(this);
                Third_button.setOnClickListener(this);
                Forgot_i.setOnClickListener(this);

                if (pref.isLoggedIn()) {

                    if(WHO.contains("OWNER")) {
                        Intent o = new Intent(SmsActivity.this, After_owner_login_copy.class);
                        startActivity(o);
                        finish();
                    }else{
                        Intent o = new Intent(SmsActivity.this, GooglemapApp.class);
                        o.putExtra("my_lat",My_lat);
                        o.putExtra("my_long",My_long);
                        startActivity(o);
                        finish();
                    }

                } else {
                    if(!again) {
                        if(WHO.contains("OWNER")) {
                            viewPager.setCurrentItem(0);
                        }else{
                            if (OTP) {
                                viewPager.setCurrentItem(1);
                            } else {
                                viewPager.setCurrentItem(0);
                            }
                        }


                    }else{
                        viewPager.setCurrentItem(2);
                    }

                    }


                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }

                    });



            } else {
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().getRootView(), "Permission not granted", Snackbar.LENGTH_INDEFINITE)
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
        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet,Connect to internet", Snackbar.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.first_button:

                Name_giver=Name.getText().toString();
                _PhoneNo_giver=_PhoneNo.getText().toString();
                if(TextUtils.isEmpty(Name.getText().toString())){
                    Name.setError("Empty");
                }
                if(TextUtils.isEmpty(_PhoneNo_giver)){
                    _PhoneNo.setError("Empty");
                }else if(isValidPhoneNumber(_PhoneNo_giver)){

                        pref.setIsWaitingForSms(true);
                        validateForm();
                } else {
                    _PhoneNo.setError("Invalid _PhoneNo no!");

                }
                break;
            case R.id.second_butona:
                verifyOtp();
                break;
            case R.id.profile_picture:
                if(isCameraAllowed()) {
                    Profile=1;
                    pickImage();

                }
                break;
            case R.id.already:
                again=true;
                viewPager.setCurrentItem(2);
                break;

            case R.id.third_button:
                _PhoneNo_again_=_PhoneNo_again.getText().toString();
                Identificationr=Identification.getText().toString();
                if(TextUtils.isEmpty(Identification.getText().toString())){
                    Identification.setError("Empty");
                }
                if(TextUtils.isEmpty(_PhoneNo_again_)){
                    _PhoneNo_again.setError("Empty");
                }else if(isValidPhoneNumber(_PhoneNo_again_)){


                   new PostValidation().execute();


                } else {
                    _PhoneNo_again.setError("Invalid Mobile no!");

                }
                break;

            case R.id.forgot_identity:

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "917002608241"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                break;

            default:
                break;
        }
    }


    private boolean isCameraAllowed() {
        int result = ContextCompat.checkSelfPermission(SmsActivity.this, CAMERA);

        //If permission is granted returning true
        if (result != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
        }

    }


    public void pickImage() {
        ImagePicker.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_PICK) {
            ImagePicker.beginCrop(this, resultCode, data);
        } else if (requestCode == ImagePicker.REQUEST_CROP) {

             bitmap = ImagePicker.getImageCropped(this, resultCode, data,
                    ImagePicker.ResizeType.FIXED_SIZE, 300);
             if(Profile==1){
                 if(bitmap!=null){
                     Profile_image.setImageBitmap(bitmap);
                 }
             }

            //Log.d(SmsActivity.this, "bitmap picked: " + bitmap);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void validateForm() {

        if (isValidPhoneNumber(_PhoneNo_giver)) {

            pref.setMobileNumber(_PhoneNo_giver);

            new PostDataTOServer().execute();

        } else {
           _PhoneNo.setError("Error!");

        }
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.first_pager;
                    break;
                case 1:
                    resId = R.id.second_pager_;
                    break;
                case 2:
                    resId = R.id.third_pager;
                    break;


            }
            return findViewById(resId);
        }
    }


    private class PostDataTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;
        private String filePath_;
        private String UNIQUE_MOBILE;
        private String successful;

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
                        .addFormDataPart("giver_name", Name_giver)
                        .addFormDataPart("giver_mobile","91"+_PhoneNo_giver)
                        .addFormDataPart("IP",mobileIp)
                        .build();

                Request request = new Request.Builder()
                        .url(URL_OWNER_DRIVER)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                String[] pars=res.split("error");
                if(pars[1].contains("false")){
                    success = true;
                    successful="true";

                }else if(pars[1].contains("true")){
                    success=false;
                    successful="true";
                }else  if(pars[1].contains("null")){
                    successful=null;
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

            if(successful!=null) {

                if (success) {
                    OTP=true;
                    viewPager.setCurrentItem(1);
                } else {
                    viewPager.setCurrentItem(2);

                }
            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Please check mobile no.", Snackbar.LENGTH_LONG).show();

            }

            }


    }

    private class PostValidation  extends AsyncTask<Void, Integer, String> {


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
                        .addFormDataPart("identity", Identificationr)
                        .addFormDataPart("giver_mobile","91"+_PhoneNo_again_)
                        .build();

                Request request = new Request.Builder()
                        .url(URL_OWNER_DRIVER_VALIDATION)
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

            if (success) {
                pref.createLogin(Name_giver, "91"+_PhoneNo_again_,WHO);
                if(WHO.contains("OWNER")) {
                    Intent o = new Intent(SmsActivity.this, After_owner_login_copy.class);
                    startActivity(o);
                    finish();
                }else{
                    Intent o = new Intent(SmsActivity.this, GooglemapApp.class);
                    o.putExtra("my_lat",My_lat);
                    o.putExtra("my_long",My_long);
                    startActivity(o);
                    finish();
                }


            } else {

                Snackbar.make(getWindow().getDecorView().getRootView(),"Please check mobile no.", Snackbar.LENGTH_LONG).show();

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


    private void verifyOtp() {
        otp = inputOtp.getText().toString().trim();
        if(TextUtils.isEmpty(otp)){
            inputOtp.setError("Please enter OTP");
        }
        else {

                                new OTPverify().execute();

        }
    }


    private static boolean isValidPhoneNumber(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;

    }
    class OTPverify  extends AsyncTask<Void, Integer, String> {
        private boolean success;
        private String filePath_;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if(verifyStoragePermissions()) {
                if (((BitmapDrawable) Profile_image.getDrawable()).getBitmap() != null) {
                    Bitmap bitmap1 = ((BitmapDrawable) Profile_image.getDrawable()).getBitmap();
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
                        filePath_ = destination.getPath();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

        protected String doInBackground(Void... args) {
            return uploadFile();
        }

        private String uploadFile() {
            // TODO Auto-generated method stub
            String res = null;
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            String filename = filePath_.substring(filePath_.lastIndexOf("/") + 1);


            try {
                File sourceFile = new File(filePath_);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("otp",otp)
                        .addFormDataPart("giver_mobile","91"+_PhoneNo_giver)
                        .addFormDataPart("image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))

                        .build();

                Request request = new Request.Builder()
                        .url(URL_VERIFY_OTP)
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

        protected void onPostExecute(final String file_url) {

            if (success == true) {
                pref.createLogin(Name_giver, "91"+_PhoneNo_giver,WHO);
                if(WHO.contains("OWNER")) {
                    Intent o = new Intent(SmsActivity.this, After_owner_login_copy.class);
                    startActivity(o);
                    finish();
                }else{
                    Intent o = new Intent(SmsActivity.this, GooglemapApp.class);
                    o.putExtra("my_lat",My_lat);
                    o.putExtra("my_long",My_long);
                    startActivity(o);
                    finish();
                }

            }else{

                Snackbar.make(getWindow().getDecorView().getRootView(), "Not a valid otp", Snackbar.LENGTH_LONG).show();

            }
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

    }




    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if(viewPager.getCurrentItem()==1){
            viewPager.setCurrentItem(0);
        }else {
            Intent o = new Intent(SmsActivity.this, Splash_screen.class);
            startActivity(o);
            finish();
        }
    }
}

