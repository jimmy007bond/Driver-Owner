package com.geekworkx.oho.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekworkx.oho.Model.Vehicle;
import com.geekworkx.oho.R;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by parag on 22/09/16.
 */
public class Vehicle_adapter extends RecyclerView.Adapter<Vehicle_adapter.ViewHolder>  {

    // The items to display in your RecyclerView
    private ArrayList<Vehicle> mItems;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private String parent_category;
    private String parent_service;
    private String TAG;
    private String filePath = null;
    private String _PhoneNo;
    private int Owner_ID=0;
    private boolean Owner_is_driver=false;


    public Vehicle_adapter(Context aContext, ArrayList<Vehicle> mItems) {
        this.mItems = mItems;
        layoutInflater = LayoutInflater.from(aContext);
        this.mContext=aContext;

    }


    public void setParent(String parent){

        parent_category=parent;
    }

    public void setService(String service){

        parent_service=service;
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
        _PhoneNo=mobilet;
    }

    public void setID(int owner_id) {
        Owner_ID=owner_id;
    }

    public void setOwnerisDriver(boolean owner_is_driver) {
        Owner_is_driver=owner_is_driver;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageThumbnail;
        private TextView Detail;
        private RelativeLayout Rl;
        private DotProgressBar pmini;
        public ViewHolder(View itemView) {
            super(itemView);

            imageThumbnail = (ImageView)itemView.findViewById(R.id.item_pic);
            Detail = (TextView)itemView.findViewById(R.id.desc);
            pmini=itemView.findViewById(R.id.dot_progress_mini);

            Rl= (RelativeLayout) itemView.findViewById(R.id.rl_t);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.selection, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Vehicle Vehicle_pos = mItems.get(position);



        if (!TextUtils.isEmpty(Vehicle_pos.getVehicle_no(position))) {
            viewHolder.Detail.setText(Vehicle_pos.getVehicle_no(position));
            viewHolder.Detail.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            viewHolder.Detail.setVisibility(View.GONE);
        }

        if (Vehicle_pos.getVehicle_image_1(position) != null) {
            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });
            builder.build().load(Vehicle_pos.getVehicle_image_1(position)).into(viewHolder.imageThumbnail);



        }

        viewHolder.imageThumbnail.clearColorFilter();
        viewHolder.imageThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.imageThumbnail.setAlpha(0.8f);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


}





