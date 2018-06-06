package com.geekworkx.oho.Drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekworkx.oho.Model.Later;
import com.geekworkx.oho.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



/**
 * Created by parag on 22/09/16.
 */
public class Later_Adapter extends RecyclerView.Adapter<Later_Adapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<Later> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String _phoneNo;
    private double My_lat=0,My_long=0;

    public Later_Adapter(Context aContext, ArrayList<Later> mItems) {
        this.mItems = mItems;
        layoutInflater = LayoutInflater.from(aContext);
        this.mContext=aContext;

    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setMobile(String mobilet) {
        _phoneNo=mobilet;
    }

    public void setMyLat(double my_lat, double my_long) {
        My_lat=my_lat;
        My_long=my_long;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView laterDate,laterTime;
        private EditText inputFrom, inputTo;
        private TextInputLayout inputFromA, inputFromT;
        private Button laterEdit;
        private LinearLayout Bottom1,Bottom2;
        private ImageView DriverImage;
        private TextView Ride_cost;

        public ViewHolder(View itemView) {
            super(itemView);

            laterDate=itemView.findViewById(R.id.ride_date_later);
            laterTime=itemView.findViewById(R.id.ride_time_later);
            inputFrom=itemView.findViewById(R.id.input_from_address);
            inputTo=itemView.findViewById(R.id.input_to_address);
            inputFromA=itemView.findViewById(R.id.input_from);
            inputFromT=itemView.findViewById(R.id.input_to);
            laterEdit=itemView.findViewById(R.id.ride_later_edit);
            Bottom1=itemView.findViewById(R.id.bottom1);
            Bottom2=itemView.findViewById(R.id.bottom2);
            DriverImage=itemView.findViewById(R.id.ride_driver);
            Ride_cost=itemView.findViewById(R.id.ride_cost);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reportsrv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Later album_pos = mItems.get(position);
        DecimalFormat dft=new DecimalFormat("0.00");

        if(album_pos.getSnapshot(position)!=null && !TextUtils.isEmpty(album_pos.getSnapshot(position))){
            viewHolder.Bottom1.setVisibility(View.GONE);
            viewHolder.Bottom2.setVisibility(View.VISIBLE);
            viewHolder.inputFromA.setVisibility(View.VISIBLE);
            viewHolder.inputFromT.setVisibility(View.VISIBLE);
            Picasso.Builder builder1 = new Picasso.Builder(mContext);
            builder1.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });
            builder1.build().load(album_pos.getDriverImage(position)).into(viewHolder.DriverImage);

        }else{
            viewHolder.Bottom1.setVisibility(View.VISIBLE);
            viewHolder.Bottom2.setVisibility(View.GONE);
            viewHolder.inputFromA.setVisibility(View.VISIBLE);
            viewHolder.inputFromT.setVisibility(View.VISIBLE);
            if(album_pos.getVehicle_choosen(position)!=null && !TextUtils.isEmpty(album_pos.getVehicle_choosen(position))){
                viewHolder.laterEdit.setText("Info");
                viewHolder.Ride_cost.setVisibility(View.VISIBLE);
                viewHolder.Bottom2.setVisibility(View.VISIBLE);
                viewHolder.DriverImage.setVisibility(View.VISIBLE);
                Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_outstation);
                viewHolder.DriverImage.setImageBitmap(bm);
            }
        }
        String date_ = parseDateToddMMyyyy(album_pos.getDate(position));
        if (!TextUtils.isEmpty(album_pos.getFrom(position))) {
            viewHolder.inputFrom.setText(album_pos.getFrom(position));

        }
        if (!TextUtils.isEmpty(album_pos.getTo(position))) {
            viewHolder.inputTo.setText(album_pos.getTo(position));

        }
        if (!TextUtils.isEmpty(album_pos.getDate(position))) {
            viewHolder.laterDate.setText("On "+date_);

        }
        if (!TextUtils.isEmpty(album_pos.getTo(position))) {
            viewHolder.laterTime.setText("At "+album_pos.getTime(position));

        }

        if (!TextUtils.isEmpty(album_pos.getCost(position))) {
            viewHolder.Ride_cost.setText("\u20B9 " +dft.format(Double.parseDouble(album_pos.getCost(position))));

        }



    }

 
    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}





