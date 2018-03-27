package com.zhd.mswcs.moduls.location.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.weidgt.SelectTrajectoryDialog;
import com.zhd.mswcs.moduls.base.view.adapter.recyclerview.BaseRecylerHolder;
import com.zhd.mswcs.moduls.location.bean.GpsItemLocalBean;
import com.zhd.mswcs.moduls.location.view.activity.TrajectoryActivity;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by think-1 on 2017/11/27.
 */

public class LocationListAdapter extends RecyclerSwipeAdapter<LocationListAdapter.AllUserLocationHolder> {
    private Context mContext;
    private List<GpsItemLocalBean> dataList;

    public LocationListAdapter(Context context,List<GpsItemLocalBean> list){
        this.mContext = context;
        this.dataList = list;
    }

    @Override
    public AllUserLocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_user_location,null,false);
        return new AllUserLocationHolder(view);
    }

    @Override
    public void onBindViewHolder(final AllUserLocationHolder viewHolder, int position) {
        final GpsItemLocalBean data = dataList.get(position);
        viewHolder.iv_user_head.setImageResource(R.mipmap.img_user_head);
        viewHolder.iv_location.setImageResource(R.mipmap.img_location);
        viewHolder.tv_latitude.setText("纬度："+String.valueOf(data.getLat()));
        viewHolder.tv_longtitude.setText("经度："+String.valueOf(data.getLon()));
        viewHolder.tv_location_time.setText(String.valueOf(data.getDateTime()));
        viewHolder.tv_user_name.setText(data.getUserName());
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        data.setIsDelete("1");
                        MyApplication.getInstance().getDb().saveOrUpdate(data);
                        ToastUtil.showToast(mContext,"删除成功");
                        dataList.remove(data);
                        notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(mContext,"删除失败");
                }
            }
        });

        viewHolder.swipeLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });

        viewHolder.iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTrajectoryDialog selectTrajectoryDialog = new SelectTrajectoryDialog(mContext);
                selectTrajectoryDialog.showDialog();
                selectTrajectoryDialog.setSelectCallBackListener(new SelectTrajectoryDialog.SelectCallBackListener() {
                    @Override
                    public void callBack(String startTime, String endTime) {
                        Intent intent = new Intent(mContext, TrajectoryActivity.class);
                        intent.putExtra("type",2);
                        intent.putExtra("sender",data.getSender());
                        intent.putExtra("receiver",data.getReceiver());
                        intent.putExtra("startTime",startTime);
                        intent.putExtra("endTime",endTime);
                        mContext.startActivity(intent);
                    }
                });

            }
        });


        viewHolder.rl_rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTrajectoryDialog selectTrajectoryDialog = new SelectTrajectoryDialog(mContext);
                selectTrajectoryDialog.showDialog();
                selectTrajectoryDialog.setSelectCallBackListener(new SelectTrajectoryDialog.SelectCallBackListener() {
                    @Override
                    public void callBack(String startTime, String endTime) {
                        Intent intent = new Intent(mContext, TrajectoryActivity.class);
                        intent.putExtra("type",2);
                        intent.putExtra("sender",data.getSender());
                        intent.putExtra("receiver",data.getReceiver());
                        intent.putExtra("startTime",startTime);
                        intent.putExtra("endTime",endTime);
                        mContext.startActivity(intent);
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.layout_swipe;
    }


     class AllUserLocationHolder extends BaseRecylerHolder {
         private ImageView iv_user_head;
         private TextView tv_user_name;
         private TextView tv_location_time;
         private TextView tv_longtitude;
         private ImageView iv_location;
         private  TextView tv_latitude;
         private SwipeLayout swipeLayout;
         private TextView btn_delete;
         private RelativeLayout rl_rootView;
         public AllUserLocationHolder(View itemView) {
             super(itemView);
             iv_user_head = (ImageView)itemView.findViewById(R.id.iv_user_head);
             tv_user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
             tv_location_time = (TextView)itemView.findViewById(R.id.tv_location_time);
             tv_longtitude = (TextView)itemView.findViewById(R.id.tv_longtitude);
             iv_location = (ImageView)itemView.findViewById(R.id.iv_location);
             tv_latitude = (TextView)itemView.findViewById(R.id.tv_latitude);
             swipeLayout = (SwipeLayout)itemView.findViewById(R.id.layout_swipe);
             btn_delete = (TextView)itemView.findViewById(R.id.btn_delete);
             rl_rootView = (RelativeLayout) itemView.findViewById(R.id.rl_rootView);
        }
    }

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }


}
