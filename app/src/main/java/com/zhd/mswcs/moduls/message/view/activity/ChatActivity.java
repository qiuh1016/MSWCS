package com.zhd.mswcs.moduls.message.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.FileUtils;
import com.zhd.mswcs.common.utils.LoadingDialog;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.common.weidgt.LocationTypeDialog;
import com.zhd.mswcs.common.weidgt.audio.AudioRecorderButton;
import com.zhd.mswcs.common.weidgt.audio.MediaPlayerManager;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.message.bean.MessageLocalBean;
import com.zhd.mswcs.moduls.message.bean.SendChatBean;
import com.zhd.mswcs.moduls.message.bean.SendGetGpsBean;
import com.zhd.mswcs.moduls.message.bean.SendGpsBean;
import com.zhd.mswcs.moduls.message.presenter.ChatPresenter;
import com.zhd.mswcs.moduls.message.presenter.SendLocationPresenter;
import com.zhd.mswcs.moduls.setting.adapter.ChatListAdapter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by think-1 on 2017/11/21.
 */
@ContentView(R.layout.activity_chat)
public class ChatActivity  extends BaseMvpActivity<MvpView,ChatPresenter> implements MvpView{
    //,InputFilter//
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.chat_recycler)
    private RecyclerView chat_recycler;
    @ViewInject(R.id.id_recorder_button)
    private AudioRecorderButton id_recorder_button;
    @ViewInject(R.id.tv_send_line)
    private TextView tv_send_line;
    @ViewInject(R.id.ll_send_text)
    private LinearLayout ll_send_text;
    @ViewInject(R.id.ll_recorder)
    private LinearLayout ll_recorder;
    @ViewInject(R.id.iv_record)
    private ImageView iv_record;
    private boolean isShowRecord = false;
    private List<MessageLocalBean> chatRecordList = new ArrayList<>();
    private ChatListAdapter chatListAdapter;
    @ViewInject(R.id.et_send_content)
    private EditText et_send_content;
    private Handler mHandler = new Handler();
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();

    private String[] permissions={Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //动态获取权限监听
    private  PermissionListener mListener;

    @Override
    public ChatPresenter createPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle(getIntent().getStringExtra("chatName"))
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setMiddleIcon(R.mipmap.img_refresh)
                .setRightIcon(R.mipmap.img_normal_location_white)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setMiddleIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initChatListData();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocationTypeDialog locationTypeDialog = new LocationTypeDialog(ChatActivity.this);
                        locationTypeDialog.showDialog();
                        locationTypeDialog.setRequestLocationListener(new LocationTypeDialog.RequestLocationListener() {
                            @Override
                            public void requestLocationListener() {
                                sendGetGpsMsg();
                            }
                        });

                        locationTypeDialog.setSendLocationListener(new LocationTypeDialog.SendLocationListener() {
                            @Override
                            public void sendLocationListener() {
                                SendLocationPresenter sendLocationPresenter = new SendLocationPresenter(ChatActivity.this);
                                LoadingDialog.getInstance().showDialog(ChatActivity.this,"正在发送位置，请稍后...");
                                JSONObject params = new JSONObject();
                                try {
                                    params.put("sender", PreferencesUtils.getString(ChatActivity.this,"telephone"));
                                    params.put("receiver",getIntent().getStringExtra("chatTelephone"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sendLocationPresenter.sendGps(params);
                                sendLocationPresenter.setBtnClickListener(new OnBindDataListener<Object>() {
                                    @Override
                                    public void bindData(Object data, int requestCode) {
                                        switch (requestCode){
                                            case 1:
                                                SendGpsBean sendGpsBean = ((SendGpsBean)data);
                                                if(sendGpsBean==null){
                                                    ToastUtils.showLongToast(ChatActivity.this, R.string.code_system_error);
                                                }else{
                                                    LogUtils.d("sendGpsBean=",new Gson().toJson(sendGpsBean));
                                                    if(sendGpsBean.isResult()){
                                                        mHandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                LoadingDialog.getInstance().closeDialog();
                                                                initChatListData();
                                                                sendBroadcast(new Intent(Constant.SEARCH_LOCATION_BRO));
                                                            }
                                                        },3000);

                                                    }else{
                                                        ToastUtil.showToast(ChatActivity.this,"位置发送失败");
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                });


                            }
                        });
                    }
                })
                .build()
                .builder();
    }


    @Override
    public void initData() {
        super.initData();
        registerReciver();
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                ToastUtil.showToast(ChatActivity.this,"你拒绝了录音权限请求，无法发送语音功能");
            }
        });


        id_recorder_button.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {

            public void onFinish(float seconds, String filePath) {
                byte[] bytes = FileUtils.file2byte(filePath);
                String recordStr = Base64.encodeToString(bytes,0,bytes.length,Base64.DEFAULT);
                byte[] decodeBytes = Base64.decode(recordStr,Base64.DEFAULT);
                FileUtils.getFile(decodeBytes, Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"mswcs/test","audio_test.mp3");
                MessageLocalBean messageLocalBean = new MessageLocalBean();
                messageLocalBean.setSender(PreferencesUtils.getString(ChatActivity.this,"telephone"));
                messageLocalBean.setReceiver(getIntent().getStringExtra("chatTelephone"));
                messageLocalBean.setSmsType(30);
                messageLocalBean.setTime(seconds);
                messageLocalBean.setFilePath(filePath);
                chatRecordList.add(messageLocalBean);
                //更新数据
                chatListAdapter.notifyDataSetChanged();
                chat_recycler.scrollToPosition(chatRecordList.size()-1);
            }
        });

        //et_send_content.setFilters(new InputFilter[]{this});
        chatListAdapter = new ChatListAdapter(this,chatRecordList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chat_recycler.setLayoutManager(linearLayoutManager);
        chat_recycler.setAdapter(chatListAdapter);
        initChatListData();
    }


    private void initChatListData() {
        chatListAdapter.clearData();
        try {
            String sql = "select *  from  messageRecord where ( sender = "+
                    getIntent().getStringExtra("chatTelephone")+" and receiver = "
                    +PreferencesUtils.getString(this,"telephone")+")"
                    + " or ( sender = "+
                    PreferencesUtils.getString(this,"telephone")+" and receiver = "
                    + getIntent().getStringExtra("chatTelephone")+") order by dateTime asc ";
            List<DbModel> modellist = MyApplication.getInstance().getDb().findDbModelAll(new SqlInfo(sql));
            if(modellist!=null&&modellist.size()>0){
                for(DbModel dbModel:modellist){
                    MessageLocalBean messageLocalBean = new MessageLocalBean();
                    messageLocalBean.setId(dbModel.getInt("id"));
                    messageLocalBean.setSmsType(dbModel.getInt("smsType"));
                    messageLocalBean.setSender(dbModel.getString("sender"));
                    messageLocalBean.setReceiver(dbModel.getString("receiver"));
                    if(dbModel.getInt("smsType")==20){
                        messageLocalBean.setMessage("请求对方发送位置");
                    }else{
                        messageLocalBean.setMessage(dbModel.getString("message"));
                    }
                    messageLocalBean.setLon(dbModel.getString("lon"));
                    messageLocalBean.setLat(dbModel.getString("lat"));
                    messageLocalBean.setCo(dbModel.getString("co"));
                    messageLocalBean.setGpsTime(dbModel.getString("gpsTime"));
                    messageLocalBean.setState(dbModel.getString("state"));
                    messageLocalBean.setDateTime(dbModel.getString("dateTime"));
                    messageLocalBean.setToken(dbModel.getString("token"));
                    chatRecordList.add(messageLocalBean);
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        chatListAdapter.notifyDataSetChanged();
        chat_recycler.scrollToPosition(chatRecordList.size()-1);
    }


    @Event(value = R.id.btn_send )
    private void sendMessage(View btn){
        if(StringUtils.isEmpty(et_send_content.getText().toString().trim())){
            ToastUtil.showToast(ChatActivity.this,"请输入消息内容");
            return;
        }
        sendMsg();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
//            case SEND_LOCATION_REQUEST_CODE:
//                initChatListData();
//                break;
            default:
                break;
        }
    }



    private void sendMsg(){
        LoadingDialog.getInstance().showDialog(this,"正在发送，请稍后");
        JSONObject params = new JSONObject();
        try {
            params.put("message",et_send_content.getText().toString().trim());
            params.put("sender",PreferencesUtils.getString(ChatActivity.this,"telephone"));
            params.put("receiver",getIntent().getStringExtra("chatTelephone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getPresenter().sendChat(params);
        getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
            @Override
            public void bindData(Object data, int requestCode) {
                switch (requestCode){
                    case 1:
                        SendChatBean sendChatBean = ((SendChatBean)data);
                        if(sendChatBean==null){
                            LoadingDialog.getInstance().closeDialog();
                            ToastUtils.showLongToast(ChatActivity.this, R.string.code_system_error);
                        }else{
                            LogUtils.d("sendChatBean=",new Gson().toJson(sendChatBean));
                            if(sendChatBean.isResult()){
                                   mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        initChatListData();
                                        sendBroadcast(new Intent(Constant.NEW_MESSAGE_BRO));
                                        LoadingDialog.getInstance().closeDialog();
                                        et_send_content.setText("");
                                    }
                                },3000);

                            }else{
                                LoadingDialog.getInstance().closeDialog();
                                ToastUtils.showLongToast(ChatActivity.this,"消息发送失败");
                                et_send_content.setText("");
                            }

                        }
                        break;
                }
            }
        });
    }



    private void sendGetGpsMsg(){
        LoadingDialog.getInstance().showDialog(this,"正在请求位置，请稍后");
        JSONObject params = new JSONObject();
        try {
            params.put("sender",PreferencesUtils.getString(ChatActivity.this,"telephone"));
            params.put("receiver",getIntent().getStringExtra("chatTelephone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getPresenter().sendGetGps(params);
        getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
            @Override
            public void bindData(Object data, int requestCode) {
                switch (requestCode){
                    case 1:
                        SendGetGpsBean sendGetGpsBean = ((SendGetGpsBean)data);
                        if(sendGetGpsBean==null){
                            LoadingDialog.getInstance().closeDialog();
                            ToastUtils.showLongToast(ChatActivity.this, R.string.code_system_error);
                        }else{
                            LogUtils.d("sendGetGpsBean=",new Gson().toJson(sendGetGpsBean));
                            if(sendGetGpsBean.isResult()){
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        initChatListData();
                                        LoadingDialog.getInstance().closeDialog();
                                    }
                                },3000);

                            }else{
                                LoadingDialog.getInstance().closeDialog();
                                ToastUtils.showLongToast(ChatActivity.this,"GPS位置请求失败");
                                et_send_content.setText("");
                            }

                        }
                        break;
                }
            }
        });
    }


//    @Override
//    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//        for (int i = start; i < end; i++) {
//            if (!isChinese(source.charAt(i))) {
//                return "";
//            }
//        }
//        return null;
//    }


    @Event(value = R.id.iv_record)
    private void showRecordBtn(View btn){
       if(!isShowRecord){
           iv_record.setBackgroundResource(R.mipmap.img_keyboard);
           ll_recorder.setVisibility(View.VISIBLE);
           id_recorder_button.setVisibility(View.VISIBLE);
           ll_send_text.setVisibility(View.GONE);
           tv_send_line.setVisibility(View.GONE);
           isShowRecord = true;
       }else{
           iv_record.setBackgroundResource(R.mipmap.img_record);
           ll_recorder.setVisibility(View.GONE);
           id_recorder_button.setVisibility(View.GONE);
           ll_send_text.setVisibility(View.VISIBLE);
           tv_send_line.setVisibility(View.VISIBLE);
           isShowRecord = false;
       }

    }



    //    //andrpoid 6.0 需要写运行时权限
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ChatActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(ChatActivity.this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }



    public interface PermissionListener {
        /**
         * 成功获取权限
         */
        void onGranted();

        /**
         * 为获取权限
         * @param deniedPermission
         */
        void onDenied(List<String> deniedPermission);
    }



    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
        if(dynamicReceiver!=null){
           unregisterReceiver(dynamicReceiver);
        }
    }



    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.REFRESH_CHAT_DATA_BRO);
        registerReceiver(dynamicReceiver,intentFilter);
    }


    //通过继承 BroadcastReceiver建立动态广播接收器
    class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
                if(StringUtils.equals(intent.getAction(),Constant.REFRESH_CHAT_DATA_BRO)){
                    LogUtils.e("receiverSuccess=","收到刷新聊天数据广播请求");
                    initChatListData();
                }
            }
        }


}
