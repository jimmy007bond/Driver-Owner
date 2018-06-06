package com.geekworkx.oho.owner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.geekworkx.oho.Adapters.After_owner_login_copy;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Crop.ImagePicker;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.geekworkx.oho.helper.HttpHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.ArrayList;
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
 * Created by parag on 06/01/18.
 */

public class Add_vehicle_details extends AppCompatActivity implements View.OnClickListener  {
    private static final String TAG = Add_vehicle_details.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 1001;
    private static final int REQUEST_EXTERNAL_STORAGE = 1002;
    private Toolbar toolbar;
    private EditText Vehicle_no;
    private Button Submit;
    private int image_no=0;
    private ConnectionDetector cd;
    private boolean isInternetPresent=false;
    private String Name,_PhoneNo,WHO;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PrefManager pref;
    private String fileRc,fileFirst,fileIns,fileSecond;
    private String Vehicle_type,Vehicle_model,Vehicle_n,Vehicle_company;
    private String Driver_selected;
    private String Driver_identity;
    private DatabaseReference mDatabase;
    private Spinner vType,vModel,vCompany;
    private ImageView Image_1,Image_2,Image_3,Image_4;
    private String mobileIp;
    private int Owner_ID=0;
    private ProgressBar progressBar;
    private List<String>Vtype=new ArrayList<String>();
    private List<String>Vmodel=new ArrayList<String>();
    private List<String>Vcompany=new ArrayList<String>();
    private String filePath1,filePath2,filePath3,filePath4;
    private boolean getImage=false;
    private double My_lat=0,My_long=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_add);

        toolbar = findViewById(R.id.toolbarvv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        cd = new ConnectionDetector(getApplicationContext());
        Submit=findViewById(R.id.submitv);
        Vehicle_no=findViewById(R.id.vehicle_add);
        vModel=findViewById(R.id.vmodel);
        vType=findViewById(R.id.vtype);
        vCompany=findViewById(R.id.vcompany);
        vModel=findViewById(R.id.vmodel);

        Image_1=findViewById(R.id.Vehicle_image_1);
        Image_2=findViewById(R.id.Vehicle_image_2);
        Image_3=findViewById(R.id.Vehicle_image_3);
        Image_4=findViewById(R.id.Vehicle_image_4);


        progressBar=(ProgressBar)findViewById(R.id.progressbarVehicle);
        pref = new PrefManager(this);
        HashMap<String, String> user = pref.getUserDetails();
        Name = user.get(PrefManager.KEY_NAME);
        WHO = user.get(PrefManager.KEY_WHO);
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        Intent i=getIntent();
        Driver_selected=i.getStringExtra("Driver");
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(Add_vehicle_details.this, After_owner_login_copy.class);
                o.putExtra("my_lat",My_lat);
                o.putExtra("my_long",My_long);
                startActivity(o);
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Submit.setOnClickListener(this);
        Image_1.setOnClickListener(this);
        Image_2.setOnClickListener(this);
        Image_3.setOnClickListener(this);
        Image_4.setOnClickListener(this);
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
            Vtype.clear();
            Vcompany.clear();
            Vmodel.clear();

           new GetListCar().execute();
            vType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!parent.getItemAtPosition(position).toString().contains("Select")) {
                        Vehicle_type = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getApplicationContext(), "Select detail of vehicle ", Toast.LENGTH_SHORT).show();

                }
            });
            vModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!parent.getItemAtPosition(position).toString().contains("Select")) {
                        Vehicle_model = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getApplicationContext(), "Select detail of vehicle ", Toast.LENGTH_SHORT).show();

                }
            });
            vCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!parent.getItemAtPosition(position).toString().contains("Select")) {
                        Vehicle_company = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getApplicationContext(), "Select detail of vehicle ", Toast.LENGTH_SHORT).show();

                }
            });




        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet,Connect to internet", Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View view) {

            switch (view.getId()) {
                case R.id.submitv:
                    if (filePath1 != null && filePath2 != null && filePath3 != null && filePath4 != null){
                        if (!TextUtils.isEmpty(Vehicle_no.getText().toString()) && Vehicle_company != null &&
                                Vehicle_model != null && Vehicle_type != null) {
                            new PostDataTOServer().execute();
                        } else {

                            Toast.makeText(getApplicationContext(), "Please fill up the form ", Toast.LENGTH_SHORT).show();
                        }
            }else{
            Toast.makeText(getApplicationContext(), "Please add all images ", Toast.LENGTH_SHORT).show();
        }

            break;

                case R.id.Vehicle_image_1:
                    image_no=1;
                    if(isCameraAllowed()) {
                        pickImage();
                    }

                    break;
                case R.id.Vehicle_image_2:
                    image_no=2;
                    if(isCameraAllowed()) {
                        pickImage();
                    }

                    break;
                case R.id.Vehicle_image_3:
                    image_no=3;
                    if(isCameraAllowed()) {
                        pickImage();
                    }

                    break;
                case R.id.Vehicle_image_4:
                    image_no=4;
                    if(isCameraAllowed()) {
                        pickImage();
                    }

                    break;
            }
            

    }

    public void pickImage() {
        getImage=true;
        ImagePicker.pickImage(this);
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
        int result = ContextCompat.checkSelfPermission(Add_vehicle_details.this, CAMERA);

        //If permission is granted returning true
        if (result != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        }else {
            return true;
        }
        return false;
    }
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_vehicle_details.this, CAMERA)){
            Toast.makeText(Add_vehicle_details.this,"This permission is helpful",Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(Add_vehicle_details.this,new String[]{CAMERA},MY_PERMISSIONS_REQUEST_USE_CAMERA);
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


                    Toast.makeText(Add_vehicle_details.this, "Camera Permission is helpful", Toast.LENGTH_SHORT).show();
                    Intent i = Add_vehicle_details.this.getIntent();
                    finish();
                    startActivity(i);

                }
                break;
            }


        }
    }




    private class GetListCar extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Vtype.add("Select");
            Vmodel.add("Select");
            Vcompany.add("Select");

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
                    JSONArray Vehicle_Type = jsonObj.getJSONArray("Vehicle_Type");
                    JSONArray Vehicle_Company = jsonObj.getJSONArray("Vehicle_Company");
                    JSONArray Vehicle_Model = jsonObj.getJSONArray("Vehicle_Model");
                    // looping through All Contacts
                    if (_PhoneNo != null) {
                        for (int i = 0; i < Owner.length(); i++) {
                            JSONObject c = Owner.getJSONObject(i);
                            String relation = c.getString("Phone_No");


                            if (relation.matches(_PhoneNo)) {
                                Owner_ID = c.getInt("ID");
                                }
                        }
                        if (Owner_ID != 0) {

                            for (int i = 0; i < Vehicle_Type.length(); i++) {
                                JSONObject c = Vehicle_Type.getJSONObject(i);
                                if (!c.isNull("ID")) {
                                    for (int j = 0; j < Vehicle_Model.length(); j++) {
                                        JSONObject d = Vehicle_Model.getJSONObject(j);
                                        if (!d.isNull("Vehicle_Type") && c.getInt("ID")==d.getInt("Vehicle_Type")) {
                                            Vtype.add(c.getString("Vehicle_Type"));
                                            Vmodel.add(d.getString("Vehicle_Model"));
                                        }
                                    }
                                }
                            }

                            for (int j = 0; j < Vehicle_Company.length(); j++) {
                                JSONObject d = Vehicle_Company.getJSONObject(j);
                                if (!d.isNull("ID")) {
                                    Vcompany.add(d.getString("Vehicle_Company"));
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
                       Toast.makeText(getApplicationContext(),"No internet connection!Connect to internet.",Toast.LENGTH_SHORT).show();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_vehicle_details.this, android.R.layout.simple_spinner_item, Vtype);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vType.setAdapter(adapter);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Add_vehicle_details.this, android.R.layout.simple_spinner_item, Vmodel);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vModel.setAdapter(adapter1);

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Add_vehicle_details.this, android.R.layout.simple_spinner_item, Vcompany);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vCompany.setAdapter(adapter2);
            }else{

                }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_PICK) {
            ImagePicker.beginCrop(this, resultCode, data);
        } else if (requestCode == ImagePicker.REQUEST_CROP) {

            Bitmap bitmap = ImagePicker.getImageCropped(this, resultCode, data,
                    ImagePicker.ResizeType.FIXED_SIZE, 300);
            if (bitmap != null) {
                if(verifyStoragePermissions()) {
                if (image_no == 1) {
                    Image_1.setImageBitmap(bitmap);


                        if (((BitmapDrawable) Image_1.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) Image_1.getDrawable()).getBitmap();
                            final File mediaStorageDir = new File(
                                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(new Date());
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            File destination1 = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            FileOutputStream fo;
                            try {
                                destination1.createNewFile();
                                fo = new FileOutputStream(destination1);
                                fo.write(bytes.toByteArray());
                                fo.close();
                                filePath1 = destination1.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    } else if (image_no == 2) {
                    Image_2.setImageBitmap(bitmap);
                    if (((BitmapDrawable) Image_2.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) Image_2.getDrawable()).getBitmap();
                        final File mediaStorageDir = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                        File destination2 = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                        FileOutputStream fo;
                        try {
                            destination2.createNewFile();
                            fo = new FileOutputStream(destination2);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            filePath2 = destination2.getPath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (bitmap != null) {
                    if (image_no == 3) {
                        Image_3.setImageBitmap(bitmap);
                        if (((BitmapDrawable) Image_3.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) Image_3.getDrawable()).getBitmap();
                            final File mediaStorageDir = new File(
                                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(new Date());
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            File destination3 = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            FileOutputStream fo;
                            try {
                                destination3.createNewFile();
                                fo = new FileOutputStream(destination3);
                                fo.write(bytes.toByteArray());
                                fo.close();
                                filePath3 = destination3.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    } else if (image_no == 4) {
                        Image_4.setImageBitmap(bitmap);
                        if (((BitmapDrawable) Image_3.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) Image_3.getDrawable()).getBitmap();
                            final File mediaStorageDir = new File(
                                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                    Locale.getDefault()).format(new Date());
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            File destination4 = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            FileOutputStream fo;
                            try {
                                destination4.createNewFile();
                                fo = new FileOutputStream(destination4);
                                fo.write(bytes.toByteArray());
                                fo.close();
                                filePath4 = destination4.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    private class PostDataTOServer  extends AsyncTask<Void, Integer, String> {


        private boolean success;

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
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            try {
                File sourceFile1 = new File(filePath1);
                File sourceFile2 = new File(filePath2);
                File sourceFile3 = new File(filePath3);
                File sourceFile4 = new File(filePath4);
                if (filePath1 != null && filePath2 != null && filePath3 != null && filePath4 != null) {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("driver", Driver_selected)
                            .addFormDataPart("mobile", _PhoneNo)
                            .addFormDataPart("vehicle_no", Vehicle_no.getText().toString())
                            .addFormDataPart("vehicle_company", Vehicle_company)
                            .addFormDataPart("vehicle_type", Vehicle_type)
                            .addFormDataPart("vehicle_model", Vehicle_model)
                            .addFormDataPart("IP", mobileIp)
                            .addFormDataPart("image_1", sourceFile1.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                            .addFormDataPart("image_2", sourceFile2.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile2))
                            .addFormDataPart("image_3", sourceFile3.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile3))
                            .addFormDataPart("image_4", sourceFile4.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile4))
                            .build();

                    Request request = new Request.Builder()
                            .url(Config_URL.URL_OWNER_VEHICLE)
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
                }else{
                    Toast.makeText(getApplicationContext(), "Please add images ", Toast.LENGTH_SHORT).show();

                }
                } catch(UnknownHostException | UnsupportedEncodingException e){
                    Log.e("TAG", "Error: " + e.getLocalizedMessage());
                } catch(Exception e){
                    Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
                }


                return res;


        }

        protected void onPostExecute(String file_url) {

            progressBar.setVisibility(View.GONE);

            if (success) {
                mDatabase.child("Driver_Online").child(Driver_selected).child("Vehicle_type").setValue(Vehicle_type);
                mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Vehicle_no").setValue(Vehicle_no);
                Intent o = new Intent(Add_vehicle_details.this, Add_vehicle_image.class);
                o.putExtra("Driver",Driver_selected);
                o.putExtra("Vehicle_no",Vehicle_no.getText().toString());
                o.putExtra("my_lat",My_lat);
                o.putExtra("my_long",My_long);
                startActivity(o);
                finish();
            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error", Snackbar.LENGTH_LONG).show();

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

            Intent o = new Intent(Add_vehicle_details.this, After_owner_login_copy.class);
        o.putExtra("my_lat",My_lat);
        o.putExtra("my_long",My_long);
            startActivity(o);
            finish();

    }
}
