package com.zhd.mswcs.moduls.location.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.TimeUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseRefreshFragment;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.location.adapter.LocationListAdapter;
import com.zhd.mswcs.moduls.location.bean.GpsItemLocalBean;
import com.zhd.mswcs.moduls.location.presenter.AllUserLocationPresenter;
import com.zhd.mswcs.moduls.location.view.activity.TrajectoryActivity;
import com.zhd.mswcs.moduls.sos.view.activity.EditUserTelephoneActivity;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: zhanghanjun on 16/9/22 23:23
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */

public class LocationFragment extends BaseRefreshFragment<MvpView,AllUserLocationPresenter> implements MvpView{
    private RecyclerView location_recyclerView;
    private LinearLayout ll_rootView;
    private List<GpsItemLocalBean> gpsItemServerBeanList = new ArrayList<>();
    private LocationListAdapter allUserLocationAdapter;
    private TextView tv_title;
    private ImageView iv_left;
    private ImageView iv_middle;
    private ImageView iv_right;
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();
    private LinearLayout ll_no_data;
    private ArrayList<String> phoneList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.fragment_location;
    }

    @Override
    public void initNavigation() {}

    @Override
    public void initContentView(View contentView) {
        super.initContentView(contentView);
        setRefresh(true,false);
        ll_no_data = (LinearLayout) contentView.findViewById(R.id.ll_no_data);
        location_recyclerView = (RecyclerView)contentView.findViewById(R.id.location_recyclerView);
        ll_rootView = (LinearLayout) contentView.findViewById(R.id.ll_rootView);
        tv_title =  (TextView)contentView.findViewById(R.id.tv_title);
        iv_left =  (ImageView) contentView.findViewById(R.id.iv_left);
        iv_middle =  (ImageView) contentView.findViewById(R.id.iv_middle);
        iv_right =  (ImageView) contentView.findViewById(R.id.iv_right);
        ll_rootView = (LinearLayout) contentView.findViewById(R.id.ll_rootView);
        tv_title.setText("位置");
        iv_left.setBackgroundResource(MyApplication.getInstance().getImageRes());
        iv_middle.setBackgroundResource(R.mipmap.img_refresh);
        iv_right.setBackgroundResource(R.mipmap.img_location_white);
        iv_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
                    ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
                    Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
                    startActivity(intent);
                    return;
                }
                initAllUserData();
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrajectoryActivity.class);
                intent.putStringArrayListExtra("data",phoneList);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
    }


    @Override
    public AllUserLocationPresenter createPresenter() {
        return new AllUserLocationPresenter(getContext());
    }

    @Override
    public void initData() {
        allUserLocationAdapter = new LocationListAdapter(getActivity(), gpsItemServerBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        location_recyclerView.setLayoutManager(linearLayoutManager);
        location_recyclerView.addItemDecoration(new SimpleDividerDecoration(getActivity()));
        location_recyclerView.setAdapter(allUserLocationAdapter);
    }

    private void initAllUserData() {
        Log.e("locationFragment","==refresh Data===");
        allUserLocationAdapter.clearData();
        gpsItemServerBeanList.clear();
        if(phoneList!=null&&phoneList.size()>0){
            phoneList.clear();
        }
        List<GpsItemLocalBean> list = null;
        String currentUser = PreferencesUtils.getString(getActivity(),"telephone");
        try {
            list = MyApplication.getInstance().getDb().selector(GpsItemLocalBean.class).where("receiver","=", currentUser).where(WhereBuilder.b("isDelete","=","0")).findAll();
            if(list!=null&&list.size()>0){
                for(GpsItemLocalBean gpsItemLocalBean:list){
                    if(!phoneList.contains(gpsItemLocalBean.getSender())){
                        phoneList.add(gpsItemLocalBean.getSender());
                    }
                }
                for(String phone:phoneList) {
                    if (StringUtils.isEmpty(phone)) {
                        continue;
                    }
                    GpsItemLocalBean gpsItemLocalBean = new GpsItemLocalBean();
                    DbModel bean =  MyApplication.getInstance().getDb().findDbModelFirst(new SqlInfo("select * from GpsItem where ( sender = '"+phone+ "' and receiver = '"+currentUser+"') and isDelete = 0 order by dateTime desc"));
                    if(bean==null){
                        continue;
                    }
                    FriendBean friendBean = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone", "=", phone).findFirst();
                    if(friendBean!=null){
                        gpsItemLocalBean.setUserName(friendBean.getUserName());
                    }else{
                        if(!StringUtils.equals(phone,currentUser)){
                            gpsItemLocalBean.setUserName(phone);
                            FriendBean newFriend = new FriendBean();
                            newFriend.setUserName(phone);
                            newFriend.setNicknNme(phone);
                            newFriend.setCreateTime(TimeUtils.getCurTimeString());
                            newFriend.setUpdateTime(TimeUtils.getCurTimeString());
                            newFriend.setTelephone(phone);
                            try {
                                MyApplication.getInstance().getDb().saveBindingId(newFriend);
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e("tag","保存好友失败");
                            }
                        }
                    }
                    gpsItemLocalBean.setId(bean.getInt("ID"));
                    gpsItemLocalBean.setSmsType(bean.getInt("smsType"));
                    gpsItemLocalBean.setSender(bean.getString("sender"));
                    gpsItemLocalBean.setReceiver(bean.getString("receiver"));
                    gpsItemLocalBean.setMessage(bean.getString("message"));
                    gpsItemLocalBean.setLon(bean.getString("lon"));
                    gpsItemLocalBean.setLat(bean.getString("lat"));
                    gpsItemLocalBean.setCo(bean.getString("co"));
                    gpsItemLocalBean.setGpsTime(bean.getString("gpsTime"));
                    gpsItemLocalBean.setState(bean.getString("state"));
                    gpsItemLocalBean.setDateTime(bean.getString("dateTime"));
                    gpsItemLocalBean.setToken(bean.getString("token"));
                    gpsItemServerBeanList.add(gpsItemLocalBean);
                }

            }
            allUserLocationAdapter.notifyDataSetChanged();
            if(gpsItemServerBeanList!=null&&gpsItemServerBeanList.size()>0){
                getRefreshView().setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
            }else{
                getRefreshView().setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refreshData(boolean isDownRefresh) {
        registerReciver();
        if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
            ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
            Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
            startActivity(intent);
            return;
        }
        initAllUserData();
    }


    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.HAS_NETWORK_BRO);
        intentFilter.addAction(Constant.NO_NETWORK_BRO);
        intentFilter.addAction(Constant.SEARCH_LOCATION_BRO);
        getActivity().registerReceiver(dynamicReceiver,intentFilter);
    }


    //通过继承 BroadcastReceiver建立动态广播接收器
    class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(StringUtils.equals(intent.getAction(),Constant.HAS_NETWORK_BRO)){
                iv_left.setBackgroundResource(R.mipmap.img_has_wifi);
            }else if(StringUtils.equals(intent.getAction(),Constant.NO_NETWORK_BRO)){
                iv_left.setBackgroundResource(R.mipmap.img_no_wifi);
            }else if(StringUtils.equals(intent.getAction(),Constant.SEARCH_LOCATION_BRO)){
                initAllUserData();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dynamicReceiver!=null){
            getActivity().unregisterReceiver(dynamicReceiver);
        }
    }

}
