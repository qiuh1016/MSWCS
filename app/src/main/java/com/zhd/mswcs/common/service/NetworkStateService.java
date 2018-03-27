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
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mswcs.moduls.message.bean.MessageServerBean;
import com.zhd.mswcs.moduls.message.presenter.MessagePresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;

import java.util.Timer;
import java.util.TimerTask;

public class NetworkStateService extends Service {
    private static final String TAG  = "netWorkChangeService==";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private MessagePresenter messagePresenter = new MessagePresenter(MyApplication.getInstance().getApplicationContext());
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
                    getSmsData();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getSmsData();
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
            getSmsData();
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    private synchronized void getSmsData(){
        LogUtils.e("开始请求消息列表数据...");
        if(StringUtils.isEmpty(PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"telephone"))){
            return;
        }
        if(!StringUtils.isEmpty(PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"smsVersion"))){
            smsVersion = PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"smsVersion");
        }
        JSONObject params = new JSONObject();
        try {
            params.put("smsVersion",smsVersion);
            params.put("phone", PreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(),"telephone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messagePresenter.getSMS(params);
        messagePresenter.setBtnClickListener(new OnBindDataListener<Object>() {
            @Override
            public void bindData(Object data, int requestCode) {
                switch (requestCode){
                    case 1:
                        MessageServerBean messageServerBean = ((MessageServerBean)data);
                        if(messageServerBean==null){
                            return;
                        }else{
                            try {
                                LogUtils.d("messageItemServerBean=",new Gson().toJson(messageServerBean));
                                if(messageServerBean.getResult()!=null&&messageServerBean.getResult().getSms()!=null&&messageServerBean.getResult().getSms().size()>0){
                                    PreferencesUtils.putString(getApplicationContext(),"smsVersion",messageServerBean.getResult().getSmsVersion());
                                    for(MessageServerBean.ResultBean.SmsBean smsBean :messageServerBean.getResult().getSms()){
                                        MessageLocalBean messageLocalBean = new MessageLocalBean();
                                        messageLocalBean.setSmsType(smsBean.getSmsType());
                                        messageLocalBean.setSender(smsBean.getSender());
                                        messageLocalBean.setReceiver(smsBean.getReceiver());
                                        messageLocalBean.setMessage(smsBean.getMessage());
                                        messageLocalBean.setLon(smsBean.getLon());
                                        messageLocalBean.setLat(smsBean.getLat());
                                        messageLocalBean.setCo(smsBean.getCo());
                                        String gpsTime = smsBean.getGpsTime().replace("T"," ");
                                        String dateTime = smsBean.getDateTime().replace("T"," ");
                                        messageLocalBean.setGpsTime(gpsTime);
                                        messageLocalBean.setState(smsBean.getState());
                                        messageLocalBean.setDateTime(dateTime);
                                        messageLocalBean.setToken(smsBean.getToken());
                                        MessageLocalBean bean = MyApplication.getInstance().getDb().selector(MessageLocalBean.class).where("token", "=", smsBean.getToken()).findFirst();
                                        if(bean!=null){
                                            messageLocalBean.setId(bean.getId());
                                            MyApplication.getInstance().getDb().update(messageLocalBean);
                                        }else{
                                            MyApplication.getInstance().getDb().saveBindingId(messageLocalBean);
                                        }
                                    }

                                    Intent intent = new Intent(Constant.REFRESH_CHAT_DATA_BRO);
                                    sendBroadcast(intent);
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
