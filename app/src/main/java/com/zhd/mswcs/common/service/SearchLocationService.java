package com.zhd.mswcs.common.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.NetworkUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.TimeUtils;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.location.bean.GpsItemLocalBean;
import com.zhd.mswcs.moduls.location.bean.GpsItemServerBean;
import com.zhd.mswcs.moduls.location.presenter.AllUserLocationPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.util.Timer;
import java.util.TimerTask;

public class SearchLocationService extends Service {
    private static final String TAG  = "SearchLocationService==";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private AllUserLocationPresenter allUserLocationPresenter = new AllUserLocationPresenter(MyApplication.getInstance().getApplicationContext());
    private String smsVersion = "";
    private Timer timer = new Timer();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.e(TAG, "网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    context.sendBroadcast(new Intent(Constant.HAS_NETWORK_BRO));
                    String name = info.getTypeName();
                    Log.e(TAG, "当前网络名称：" + name);
                    initAllUserData();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            initAllUserData();
                        }
                    },3000,3000);//3秒之后，每隔3秒做一次run()操作

                } else {
                    Log.e(TAG, "没有可用网络");
                    context.sendBroadcast(new Intent(Constant.NO_NETWORK_BRO));
                    //doSomething()
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (NetworkUtils.isConnected(getApplicationContext())) {
            initAllUserData();
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private void initAllUserData() {
        LogUtils.e("开始请求位置列表数据...");
        if(StringUtils.isEmpty(PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"telephone"))){
            return;
        }
        if(!StringUtils.isEmpty(PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"smsVersion"))){
            smsVersion = PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"smsVersion");
        }
            JSONObject params = new JSONObject();
            try {
                params.put("smsVersion",smsVersion);
                params.put("phone",PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"telephone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
              allUserLocationPresenter.getGPSItem(params);
              allUserLocationPresenter.setBtnClickListener(new OnBindDataListener<Object>() {
                @Override
                public void bindData(Object data, int requestCode) {
                    switch (requestCode){
                        case 1:
                            GpsItemServerBean gpsItemServerBean = ((GpsItemServerBean)data);
                            if(gpsItemServerBean==null){
                                return;
                            }else{
                                try {
                                    LogUtils.d("gpsItemServerBean=",new Gson().toJson(gpsItemServerBean));
                                    if(gpsItemServerBean.getResult()!=null&&gpsItemServerBean.getResult().getSms()!=null&&gpsItemServerBean.getResult().getSms().size()>0){
                                        PreferencesUtils.putString(MyApplication.getInstance().getApplicationContext(),"smsVersion",gpsItemServerBean.getResult().getSmsVersion());
                                        for(GpsItemServerBean.ResultBean.SmsBean smsBean :gpsItemServerBean.getResult().getSms()){
                                            FriendBean friendBean = MyApplication.getInstance().getDb().selector(FriendBean.class).where("telephone", "=", smsBean.getSender()).findFirst();
                                            GpsItemLocalBean gpsItemLocalBean = new GpsItemLocalBean();
                                            if(friendBean==null){
                                                FriendBean friend = new FriendBean();
                                                friend.setUserName(smsBean.getSender());
                                                friend.setNicknNme(smsBean.getSender());
                                                friend.setCreateTime(TimeUtils.getCurTimeString());
                                                friend.setUpdateTime(TimeUtils.getCurTimeString());
                                                friend.setTelephone(smsBean.getSender());
                                                MyApplication.getInstance().getDb().saveBindingId(friend);
                                                gpsItemLocalBean.setUserName(smsBean.getSender());
                                            }else{
                                                gpsItemLocalBean.setUserName(friendBean.getUserName());
                                            }
                                            gpsItemLocalBean.setSmsType(smsBean.getSmsType());
                                            gpsItemLocalBean.setSender(smsBean.getSender());
                                            gpsItemLocalBean.setReceiver(smsBean.getReceiver());
                                            gpsItemLocalBean.setMessage(smsBean.getMessage());
                                            gpsItemLocalBean.setLon(smsBean.getLon());
                                            gpsItemLocalBean.setLat(smsBean.getLat());
                                            gpsItemLocalBean.setCo(smsBean.getCo());
                                            String gpsTime = smsBean.getGpsTime().replace("T"," ");
                                            String dateTime = smsBean.getDateTime().replace("T"," ");
                                            gpsItemLocalBean.setGpsTime(gpsTime);
                                            gpsItemLocalBean.setState(smsBean.getState());
                                            gpsItemLocalBean.setDateTime(dateTime);
                                            gpsItemLocalBean.setToken(smsBean.getToken());
                                            GpsItemLocalBean bean = MyApplication.getInstance().getDb().selector(GpsItemLocalBean.class).where("token", "=", smsBean.getToken()).findFirst();
                                            if(bean!=null){
                                                gpsItemLocalBean.setIsDelete(bean.getIsDelete());
                                                gpsItemLocalBean.setId(bean.getId());
                                                MyApplication.getInstance().getDb().update(gpsItemLocalBean);
                                            }else{
                                                gpsItemLocalBean.setIsDelete("0");
                                                MyApplication.getInstance().getDb().saveBindingId(gpsItemLocalBean);
                                            }
                                        }

                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                    }
                }
            });

    }

}
