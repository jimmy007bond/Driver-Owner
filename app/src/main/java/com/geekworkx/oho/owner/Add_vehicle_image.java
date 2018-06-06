package com.geekworkx.oho.owner;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geekworkx.oho.Adapters.After_owner_login_copy;
import com.geekworkx.oho.Adapters.HeightWrappingViewPager;
import com.geekworkx.oho.ConnectionDetector;
import com.geekworkx.oho.Crop.ImagePicker;
import com.geekworkx.oho.Main_activity.PrefManager;
import com.geekworkx.oho.R;
import com.geekworkx.oho.URLS.Config_URL;
import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.Manifest.permission.CAMERA;

/**
 * Created by parag on 10/01/18.
 */

public class Add_vehicle_image extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Add_vehicle_image.class.getSimpleName();
    private ImageView BrcImage,BinsuranceImage;
    private int Cam=0;
    private ConnectionDetector cd;
    private boolean isInternetPresent=false;
    private ProgressBar progressBar;
    private String Name,_PhoneNo,WHO;
    private String filePath;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PrefManager pref;
    private String fileAdd;
    private String Driver_selected;
    private DatabaseReference mDatabase;
    private Button Upload2,Upload3;
    private HeightWrappingViewPager viewPager;
    private ViewPagerAdapter adapter;
    private EditText RcNo,InsuranceNo;
    private String Vehicle_no;
    private double My_lat=0,My_long=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle_image);

        cd = new ConnectionDetector(getApplicationContext());

        BrcImage=findViewById(R.id.bigrc);
        BinsuranceImage=findViewById(R.id.biginsurance);
        Upload2=findViewById(R.id.upload2);
        Upload3=findViewById(R.id.upload3);

        RcNo=findViewById(R.id.textRc);
        InsuranceNo=findViewById(R.id.textInsurance);

        BrcImage.setOnClickListener(this);
        Upload2.setOnClickListener(this);
        Upload3.setOnClickListener(this);
        BinsuranceImage.setOnClickListener(this);

        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        pref = new PrefManager(this);
        HashMap<String, String> user = pref.getUserDetails();
        WHO = user.get(PrefManager.KEY_WHO);
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
        Intent i=getIntent();
        Driver_selected=i.getStringExtra("Driver");
        Vehicle_no=i.getStringExtra("Vehicle_no");
        My_lat = i.getDoubleExtra("my_lat", 0);
        My_long = i.getDoubleExtra("my_long", 0);
        viewPager=findViewById(R.id.v12);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
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
            Snackbar.make(getWindow().getDecorView().getRootView(), "No Internet,Connect to internet", Snackbar.LENGTH_LONG).show();

        }
    }

        class ViewPagerAdapter extends PagerAdapter {

            @Override
            public int getCount() {
                return 2;
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
                        resId = R.id.v2;
                        break;
                    case 1:
                        resId = R.id.v3;
                        break;

                }
                return findViewById(resId);
            }
        }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bigrc:
                Cam=1;
                if(isCameraAllowed() && verifyStoragePermissions()) {
                    pickImage();
                }
                break;

            case R.id.biginsurance:
                Cam=2;
                if(isCameraAllowed() && verifyStoragePermissions()) {
                    pickImage();
                }
                break;


            case R.id.upload2:
                if(filePath!=null){
                    if(!TextUtils.isEmpty(RcNo.getText().toString())) {
                    fileAdd=RcNo.getText().toString();
                    new PostVehicleTOServer().execute();
                }else{
                    RcNo.setError("Empty");
                   }
                }else{
                    Toast.makeText(getApplicationContext(),"Please add image",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload3:
                if(filePath!=null){
                    if(!TextUtils.isEmpty(InsuranceNo.getText().toString())) {
                    fileAdd=InsuranceNo.getText().toString();
                    new PostVehicleTOServer().execute();
                    }else{
                        RcNo.setError("Empty");
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please add image",Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    public void pickImage() {
        ImagePicker.pickImage(this);
    }

    private class PostVehicleTOServer  extends AsyncTask<Void, Integer, String> {


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
                if(filePath!=null && !TextUtils.isEmpty(filePath)) {
                    File sourceFile = new File(filePath);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("driver", Driver_selected)
                            .addFormDataPart("mobile", _PhoneNo)
                            .addFormDataPart("Cam", String.valueOf(Cam))
                            .addFormDataPart("no", fileAdd)
                            .addFormDataPart("Vehicle_no", Vehicle_no)
                            .addFormDataPart("image", sourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))

                            .build();

                    Request request = new Request.Builder()
                            .url(Config_URL.URL_OWNER_VEHICLE_IMAGE_ADD)
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

            progressBar.setVisibility(View.GONE);

            if (success) {
                if(Cam==1){
                    viewPager.setCurrentItem(1);
                }else if(Cam==2){
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Driver_Phone_no").setValue(Driver_selected);
                    mDatabase.child("Driver_Online").child(_PhoneNo).child("Owner_Phone_no").setValue(_PhoneNo);

                    Intent o = new Intent(Add_vehicle_image.this, After_owner_login_copy.class);
                    o.putExtra("my_lat",My_lat);
                    o.putExtra("my_long",My_long);
                    startActivity(o);
                    finish();
                }
            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(), "Please add image of the document.", Snackbar.LENGTH_LONG).show();

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
        int result = ContextCompat.checkSelfPermission(Add_vehicle_image.this, CAMERA);

        //If permission is granted returning true
        if (result != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
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
                if (Cam == 1) {
                    BrcImage.setImageBitmap(bitmap);
                    if(verifyStoragePermissions()) {
                    if (((BitmapDrawable) BrcImage.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) BrcImage.getDrawable()).getBitmap();
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
                } else if (Cam == 2) {
                    BinsuranceImage.setImageBitmap(bitmap);
                    if(verifyStoragePermissions()) {
                    if (((BitmapDrawable) BinsuranceImage.getDrawable()).getBitmap() != null) {
                        Bitmap bitmap1 = ((BitmapDrawable) BinsuranceImage.getDrawable()).getBitmap();
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
    }


}
