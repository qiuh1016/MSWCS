package com.zhd.mswcs.moduls.location.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerAdapter;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.location.bean.GpsItemLocalBean;
import com.zhd.mswcs.moduls.location.view.activity.TrajectoryActivity;

import java.util.List;

/**
 * Created by think-1 on 2017/11/20.
 */

public class AllUserLocationAdapter extends BaseRecylerAdapter<GpsItemLocalBean>{
    private Context mContext;
    private List<GpsItemLocalBean> dataList;
    public AllUserLocationAdapter(Activity context, List<GpsItemLocalBean> data) {
        super(context, data);
        this.mContext = context;
        this.dataList = data;
    }

    @Override
    public BaseRecylerHolder createViewHolder(View view, int viewType) {
        return new AllUserLocationHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseRecylerHolder baseHolder, int position) {
        super.onBindViewHolder(baseHolder, position);
        AllUserLocationHolder allUserLocationHolder = (AllUserLocationHolder) baseHolder;
        final GpsItemLocalBean data = dataList.get(position);
        allUserLocationHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
        allUserLocationHolder.iv_location.setImageResource(R.mipmap.img_location);
        allUserLocationHolder.tv_latitude.setText("纬度："+String.valueOf(data.getLat()));
        allUserLocationHolder.tv_longtitude.setText("经度："+String.valueOf(data.getLon()));
        allUserLocationHolder.tv_location_time.setText(String.valueOf(data.getDateTime()));
        allUserLocationHolder.tv_user_name.setText(data.getUserName());
        allUserLocationHolder.iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrajectoryActivity.class);
                intent.putExtra("sender",data.getSender());
                intent.putExtra("receiver",data.getReceiver());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_all_user_location,null,false);
    }

    private class AllUserLocationHolder extends BaseRecylerHolder{
        private ImageView iv_user_head;
        private TextView tv_user_name;
        private TextView tv_location_time;
        private TextView tv_longtitude;
        private ImageView iv_location;
        private  TextView tv_latitude;
        public AllUserLocationHolder(View itemView) {
            super(itemView);
            iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
            tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            tv_location_time = (TextView)itemView.findViewById(R.id.tv_location_time);
            tv_longtitude = (TextView)itemView.findViewById(R.id.tv_longtitude);
            iv_location = (ImageView)itemView.findViewById(R.id.iv_location);
            tv_latitude = (TextView)itemView.findViewById(R.id.tv_latitude);
        }
    }



    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }

}
