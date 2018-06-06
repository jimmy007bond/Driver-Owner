package com.geekworkx.oho.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geekworkx.oho.Adapters.After_owner_login_copy;
import com.geekworkx.oho.Adapters.GooglemapApp;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Crop.ImagePicker;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.MyViewPager;
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
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class driver_documents_add extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Owner_driver_add.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 1001;
    private static final int REQUEST_EXTERNAL_STORAGE = 1002;
    private static final int IMAGE_1 = 101;
    private static final int IMAGE_2 = 102;
    private static final int IMAGE_3 = 103;
    private static final int IMAGE_4 = 104;
    private static final int IMAGE_5 = 105;
    private Toolbar toolbar;
    private EditText d_PhoneNo,dFirstname,dLastname,dBirthdate,dEmail,dIdentity,dStreetfirst,dSteetsecond,dCity,dState,dCountry,dPin;
    private ImageView dImage;
    private MyViewPager viewPager;
    private Button Submit1;
    private ImageView bPancard,bAddressproof,bAadharcard,bDrivingl,bCancelcheck;
    private ConnectionDetector cd;
    private boolean isInternetPresent=false;
    private ViewPagerAdapter adapter;
    private PrefManager pref;
    private ProgressBar progressBar;
    private String Name,_PhoneNo,WHO;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String filePath;
    private String Cam;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String Drivers;
    private String Owner_name,Owner_image,Owner_mobile,Owner_last,Owner_email,Owner_address_1,Owner_address_2,Owner_city,Owner_state,Owner_country,Owner_zip,IDENTIFICATION,UNIQUE_ID;
    private String Owner_birthdate;
    private String Owner_pancard,Owner_addressproof,Owner_aadhar="",Owner_cancelcheck;
    private DatabaseReference mDatabase;
    private String dDriver_mobile;
    private boolean Again=false;
    private String fileImage;
    private Button Panb,Addressb,Aadharb,Checkb,Dlb,Aadhar_cancel;
    private double My_lat=0,My_long=0;
    private File destination;
    private AutoCompleteTextView AddressSpinner;
    private String address_type="";
    private List<String> Vtype=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_documents_add);

        toolbar = findViewById(R.id.toolbarddocuments);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        viewPager=findViewById(R.id.viewPagerVerticaldocuments);

        Submit1=findViewById(R.id.submit);



        AddressSpinner=findViewById(R.id.spinner_address);
        Panb=findViewById(R.id.buttonp);
        Addressb=findViewById(R.id.buttonadd);
        Aadharb=findViewById(R.id.buttonaa);
        Checkb=findViewById(R.id.buttoncc);
        Dlb=findViewById(R.id.buttondl);
        Aadhar_cancel=findViewById(R.id.buttonaa_cancel);



        Panb.setOnClickListener(this);
        Addressb.setOnClickListener(this);
        Aadharb.setOnClickListener(this);
        Checkb.setOnClickListener(this);
        Dlb.setOnClickListener(this);
        Aadhar_cancel.setOnClickListener(this);

        AddressSpinner.setOnClickListener(this);



        bPancard=findViewById(R.id.panimage);
        bAddressproof=findViewById(R.id.addressimage);
        bAadharcard=findViewById(R.id.aadharimage);
        bDrivingl=findViewById(R.id.dlimage);
        bCancelcheck=findViewById(R.id.ccimage);


        bPancard.setOnClickListener(this);
        bAddressproof.setOnClickListener(this);
        bAadharcard.setOnClickListener(this);
        bDrivingl.setOnClickListener(this);
        bCancelcheck.setOnClickListener(this);


        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        progressBar=(ProgressBar)findViewById(R.id.progress_docs);
        pref = new PrefManager(this);
        HashMap<String, String> user = pref.getUserDetails();
        Name = user.get(PrefManager.KEY_NAME);
        WHO = user.get(PrefManager.KEY_WHO);
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);

        Intent i=getIntent();
        Drivers=i.getStringExtra("Driver");
        dDriver_mobile=i.getStringExtra("Driver_mobile");
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WHO.contains("OWNER")) {

                    Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                } else {
                    Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                    o.putExtra("my_lat", My_lat);
                    o.putExtra("my_long", My_long);
                    startActivity(o);
                    finish();
                }
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Vtype.clear();
            new GetListCar().execute();
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

        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet,Connect to internet", Snackbar.LENGTH_LONG).show();

        }
    }

    private class GetListCar extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Vtype.add("Select");

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
                    JSONArray address_proof_documents = jsonObj.getJSONArray("address_proof_documents");
                   if (_PhoneNo != null) {
                        for (int i = 0; i < address_proof_documents.length(); i++) {
                            JSONObject c = address_proof_documents.getJSONObject(i);
                            Vtype.add(c.getString("Document_Name"));

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


            if (Vtype.size() != 0) {
                ArrayAdapter<String> mAdapter=new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_list_item_1, Vtype);

                final String[] selection = new String[1];
                AddressSpinner.setAdapter(mAdapter);
                AddressSpinner.setCursorVisible(false);
                AddressSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AddressSpinner.showDropDown();
                        // _institute.setText(mIns.get(position));
                        selection[0] = (String) parent.getItemAtPosition(position);

                    }
                });

            }
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.spinner_address:
                AddressSpinner.showDropDown();
                break;


            case R.id.panimage:
                Cam="Pancard";
                Again=true;
                if(isCameraAllowed()) {
                    pickImage();
                }
                break;

            case R.id.addressimage:
                Cam="Addressproof";
                Again=true;
                if(isCameraAllowed()) {
                    pickImage();
                }
                break;
            case R.id.aadharimage:
                Cam="Aadharcard";
                Again=true;
                if(isCameraAllowed()) {
                    pickImage();
                }
                break;
            case R.id.dlimage:
                Cam="DL";
                if(isCameraAllowed()) {
                    pickImage();
                }
                break;
            case R.id.ccimage:
                Cam="Cancelcheck";
                Again=true;
                if(isCameraAllowed()) {
                    pickImage();
                }
                break;


            case R.id.buttonp:
                Cam="Pancard";
                if(Drivers.contains("SELF")){
                    new PostOwnerDocs().execute();
                }else  if (Drivers.contains("DRIVER")) {
                    new PostPic().execute();
                }else{
                    new PostOwnerAsDriverDocs().execute();
                }
                break;
            case R.id.buttonadd:
                if(!TextUtils.isEmpty(AddressSpinner.getText().toString())) {
                    address_type = AddressSpinner.getText().toString();
                    Cam="Addressproof";
                    if(Drivers.contains("SELF")){
                        new PostOwnerDocs().execute();
                    }else  if (Drivers.contains("DRIVER")) {
                        new PostPic().execute();
                    }else{
                        new PostOwnerAsDriverDocs().execute();
                    }
                }else{
                    AddressSpinner.setError("Empty");
                }

                break;
            case R.id.buttonaa:
                Cam="Aadharcard";
                if(Drivers.contains("SELF")){
                    new PostOwnerDocs().execute();
                }else  if (Drivers.contains("DRIVER")) {
                    new PostPic().execute();
                }else{
                    new PostOwnerAsDriverDocs().execute();
                }
                break;
            case R.id.buttoncc:
                Cam="Cancelcheck";
                if(Drivers.contains("SELF")){
                    new PostOwnerDocs().execute();
                }else  if (Drivers.contains("DRIVER")) {
                    new PostPic().execute();
                }else{
                    new PostOwnerAsDriverDocs().execute();
                }
                break;
            case R.id.buttondl:
                Cam="DL";
                if(Drivers.contains("SELF")){
                    new PostOwnerDocs().execute();
                }else  if (Drivers.contains("DRIVER")) {
                    new PostPic().execute();
                }else{
                    new PostOwnerAsDriverDocs().execute();
                }
                break;
            case R.id.buttonaa_cancel:
                if(!dDriver_mobile.contains(_PhoneNo)) {
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(dDriver_mobile);
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(_PhoneNo);
                }
                if(WHO.contains("OWNER")) {

                    new android.support.v7.app.AlertDialog.Builder(driver_documents_add.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Driver has been informed")
                            .setMessage("Please wait for his acceptance")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                                    o.putExtra("my_lat",My_lat);
                                    o.putExtra("my_long",My_long);
                                    startActivity(o);
                                    finish();
                                    dialog.cancel();
                                }
                            })
                            .show();
                }else {

                        Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                        o.putExtra("my_lat", My_lat);
                        o.putExtra("my_long", My_long);
                        startActivity(o);
                        finish();

                }
                break;

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

            Bitmap bitmap = ImagePicker.getImageCropped(this, resultCode, data,
                    ImagePicker.ResizeType.FIXED_SIZE, 300);
            if(bitmap!=null){
                if(verifyStoragePermissions()) {
                    if (Cam.contains("Pancard")) {
                        bPancard.setImageBitmap(bitmap);
                        if (((BitmapDrawable) bPancard.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) bPancard.getDrawable()).getBitmap();
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
                    } else if (Cam.contains("Addressproof")) {
                        bAddressproof.setImageBitmap(bitmap);
                        if (((BitmapDrawable) bAddressproof.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) bAddressproof.getDrawable()).getBitmap();
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
                                fileImage = destination.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (Cam.contains("Aadharcard")) {
                        bAadharcard.setImageBitmap(bitmap);
                        if (((BitmapDrawable) bAadharcard.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) bAadharcard.getDrawable()).getBitmap();
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
                                fileImage = destination.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    } else if (Cam.contains("Cancelcheck")) {
                        bCancelcheck.setImageBitmap(bitmap);
                        if (((BitmapDrawable) bCancelcheck.getDrawable()).getBitmap() != null) {
                            Bitmap bitmap1 = ((BitmapDrawable) bCancelcheck.getDrawable()).getBitmap();
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
                                fileImage = destination.getPath();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (Cam.contains("DL")) {
                        bDrivingl.setImageBitmap(bitmap);

                            if (((BitmapDrawable) bDrivingl.getDrawable()).getBitmap() != null) {
                                Bitmap bitmap1 = ((BitmapDrawable) bDrivingl.getDrawable()).getBitmap();
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
                                    fileImage = destination.getPath();

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
    private class PostPic  extends AsyncTask<Void, Integer, String> {


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

                File sourceFile1 = new File(fileImage);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Cam",Cam)
                        .addFormDataPart("WHO",Drivers)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("driver_phone",dDriver_mobile)
                        .addFormDataPart("proof",address_type)
                        .addFormDataPart("driver_doc", sourceFile1.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_OWNER_DRIVER_DOCUMENTS)
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
                if(Cam.contains("Pancard")) {
                    viewPager.setCurrentItem(1);
                }
                if(Cam.contains("Addressproof") ) {
                    viewPager.setCurrentItem(2);
                }
                if(Cam.contains("Aadharcard")) {
                    if(!dDriver_mobile.contains(_PhoneNo)) {
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(dDriver_mobile);
                        mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(_PhoneNo);
                    }else{

                    }
                    if(WHO.contains("OWNER")) {
                        new android.support.v7.app.AlertDialog.Builder(driver_documents_add.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle(dFirstname.getText().toString()+" has been informed")
                                .setMessage("Please wait for his acceptance")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                                        o.putExtra("my_lat",My_lat);
                                        o.putExtra("my_long",My_long);
                                        startActivity(o);
                                        finish();
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    }else {
                        Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                        o.putExtra("my_lat",My_lat);
                        o.putExtra("my_long",My_long);
                        startActivity(o);
                        finish();
                    }
                }

                if(Cam.contains("Cancelcheck") ) {
                    viewPager.setCurrentItem(3);


                }
                if(Cam.contains("DL") ) {
                    viewPager.setCurrentItem(4);
                }

            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error", Snackbar.LENGTH_LONG).show();

            }

        }


    }

    private class PostOwnerDocs  extends AsyncTask<Void, Integer, String> {


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

                File sourceFile1 = new File(fileImage);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Cam",Cam)
                        .addFormDataPart("WHO",Drivers)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("proof",address_type)
                        .addFormDataPart("driver_doc", sourceFile1.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_OWNER_DOCUMENTS)
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
                if(Cam.contains("Pancard")) {
                    viewPager.setCurrentItem(1);
                }
                if(Cam.contains("Addressproof") ) {
                    viewPager.setCurrentItem(2);
                }
                if(Cam.contains("Aadharcard")) {
                    if(!Drivers.contains("SELF")) {
                        if (!dDriver_mobile.contains(_PhoneNo)) {
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(dDriver_mobile);
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(_PhoneNo);
                        } else {

                        }
                        if (WHO.contains("OWNER")) {
                            new android.support.v7.app.AlertDialog.Builder(driver_documents_add.this)
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle(dFirstname.getText().toString() + " has been informed")
                                    .setMessage("Please wait for his acceptance")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                                            o.putExtra("my_lat",My_lat);
                                            o.putExtra("my_long",My_long);
                                            startActivity(o);
                                            finish();
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        } else {
                            Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                            o.putExtra("my_lat",My_lat);
                            o.putExtra("my_long",My_long);
                            startActivity(o);
                            finish();
                        }
                    } else {
                        if (WHO.contains("OWNER")) {

                            Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                            o.putExtra("my_lat", My_lat);
                            o.putExtra("my_long", My_long);
                            startActivity(o);
                            finish();
                        } else {
                            Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                            o.putExtra("my_lat", My_lat);
                            o.putExtra("my_long", My_long);
                            startActivity(o);
                            finish();
                        }
                    }
                }

                if(Cam.contains("Cancelcheck") ) {
                    if(Drivers.contains("SELF")){
                        viewPager.setCurrentItem(4);
                    }else {
                        viewPager.setCurrentItem(3);
                    }


                }
                if(Cam.contains("DL") ) {
                    viewPager.setCurrentItem(4);
                }

            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error", Snackbar.LENGTH_LONG).show();

            }

        }


    }

    private class PostOwnerAsDriverDocs  extends AsyncTask<Void, Integer, String> {


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

                File sourceFile1 = new File(fileImage);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Cam",Cam)
                        .addFormDataPart("WHO",Drivers)
                        .addFormDataPart("mobile",_PhoneNo)
                        .addFormDataPart("driver_doc", sourceFile1.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
                        .build();

                Request request = new Request.Builder()
                        .url(Config_URL.URL_OWNER_AS_DRIVER_DOCUMENTS)
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
                if(Cam.contains("Pancard")) {
                    viewPager.setCurrentItem(1);
                }
                if(Cam.contains("Addressproof") ) {
                    viewPager.setCurrentItem(2);
                }
                if(Cam.contains("Aadharcard")) {
                    if(!Drivers.contains("SELF")) {
                        if (!dDriver_mobile.contains(_PhoneNo)) {
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(dDriver_mobile);
                            mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(_PhoneNo);
                        } else {

                        }
                        if (WHO.contains("OWNER")) {
                            new android.support.v7.app.AlertDialog.Builder(driver_documents_add.this)
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle(dFirstname.getText().toString() + " has been informed")
                                    .setMessage("Please wait for his acceptance")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                                            o.putExtra("my_lat",My_lat);
                                            o.putExtra("my_long",My_long);
                                            startActivity(o);
                                            finish();
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        } else {
                            Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                            o.putExtra("my_lat",My_lat);
                            o.putExtra("my_long",My_long);
                            startActivity(o);
                            finish();
                        }
                    } else {
                        if (WHO.contains("OWNER")) {

                            Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
                            o.putExtra("my_lat", My_lat);
                            o.putExtra("my_long", My_long);
                            startActivity(o);
                            finish();
                        } else {
                            Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
                            o.putExtra("my_lat", My_lat);
                            o.putExtra("my_long", My_long);
                            startActivity(o);
                            finish();
                        }
                    }
                }

                if(Cam.contains("Cancelcheck") ) {
                    if(Drivers.contains("SELF")){
                        viewPager.setCurrentItem(4);
                    }else {
                        viewPager.setCurrentItem(3);
                    }


                }
                if(Cam.contains("DL") ) {
                    viewPager.setCurrentItem(4);
                }

            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Error", Snackbar.LENGTH_LONG).show();

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
        int result = ContextCompat.checkSelfPermission(driver_documents_add.this, CAMERA);

        //If permission is granted returning true
        if (result != PackageManager.PERMISSION_GRANTED){
            return  false;
        }else {
            return true;
        }

    }



    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
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
                    resId = R.id.second_update;
                    break;
                case 1:
                    resId = R.id.third_update;
                    break;
                case 2:
                    resId = R.id.fourth_update;
                    break;
                case 3:
                    resId = R.id.fifth_update;
                    break;
                case 4:
                    resId = R.id.sixth_update;
                    break;

            }
            return findViewById(resId);
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

            Intent o = new Intent(driver_documents_add.this, After_owner_login_copy.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
        } else {
            Intent o = new Intent(driver_documents_add.this, GooglemapApp.class);
            o.putExtra("my_lat", My_lat);
            o.putExtra("my_long", My_long);
            startActivity(o);
            finish();
        }

    }
}
