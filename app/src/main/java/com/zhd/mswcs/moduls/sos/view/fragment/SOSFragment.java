package com.zhd.mswcs.moduls.sos.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.LoadingDialog;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseMvpFragment;
import com.zhd.mswcs.moduls.message.bean.SendChatBean;
import com.zhd.mswcs.moduls.message.presenter.ChatPresenter;
import com.zhd.mswcs.moduls.setting.view.activity.EditSOSSettingActivity;
import com.zhd.mswcs.moduls.sos.presenter.SOSPresenter;
import com.zhd.mswcs.moduls.sos.view.activity.EditUserTelephoneActivity;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by think-1 on 2017/12/18.
 */

public class SOSFragment extends BaseMvpFragment<MvpView,SOSPresenter> implements MvpView {
    private TextView tv_title;
    private TextView tv_send_sos;
    private ImageView iv_left;
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();
    private Handler mHandler = new Handler();
    private ChatPresenter chatPresenter;

    @Override
    public int getContentView() {
        return R.layout.fragment_sos;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initContentView(View contentView) {
        tv_title =  (TextView)contentView.findViewById(R.id.tv_title);
        tv_send_sos =  (TextView)contentView.findViewById(R.id.tv_send_sos);
        iv_left =  (ImageView) contentView.findViewById(R.id.iv_left);
        tv_title.setText("SOS");
        iv_left.setBackgroundResource(MyApplication.getInstance().getImageRes());
    }

    @Override
    public SOSPresenter createPresenter() {
        return new SOSPresenter(getActivity());
    }


    @Override
    public void initData() {
        super.initData();
        chatPresenter = new ChatPresenter(getActivity());
        registerReciver();
        tv_send_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "sos_send_content"))||StringUtils.isEmpty(PreferencesUtils.getString(getActivity(),"sos_send_telephone"))){
                    ToastUtil.showToast(getActivity(),"请完善SOS设置");
                    Intent intent = new Intent(getActivity(), EditSOSSettingActivity.class);
                    startActivity(intent);
                    return;
                }
                if(StringUtils.isEmpty(PreferencesUtils.getString(getActivity(), "telephone"))){
                    ToastUtil.showToast(getActivity(),"请先绑定你的手机号");
                    Intent intent = new Intent(getActivity(), EditUserTelephoneActivity.class);
                    startActivity(intent);
                    return;
                }

                String message = PreferencesUtils.getString(getActivity(), "sos_send_content");
                String sender = PreferencesUtils.getString(getActivity(), "telephone");
                String receiver = PreferencesUtils.getString(getActivity(),"sos_send_telephone");
                sendMsg(message,sender,receiver);

            }
        });

    }

    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.HAS_NETWORK_BRO);
        intentFilter.addAction(Constant.NO_NETWORK_BRO);
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


    private void sendMsg(String msg,String sender,String receiver){
        LoadingDialog.getInstance().showDialog(getActivity(),"正在发送，请稍后");
        JSONObject params = new JSONObject();
        try {
            params.put("message",msg);
            params.put("sender",sender);
            params.put("receiver",receiver);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chatPresenter.sendChat(params);
        chatPresenter.setBtnClickListener(new OnBindDataListener<Object>() {
            @Override
            public void bindData(Object data, int requestCode) {
                switch (requestCode){
                    case 1:
                        SendChatBean sendChatBean = ((SendChatBean)data);
                        if(sendChatBean==null){
                            LoadingDialog.getInstance().closeDialog();
                            ToastUtils.showLongToast(getActivity(), R.string.code_system_error);
                        }else{
                            LogUtils.d("sendChatBean=",new Gson().toJson(sendChatBean));
                            if(sendChatBean.isResult()){
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoadingDialog.getInstance().closeDialog();
                                        ToastUtils.showLongToast(getActivity(),"SOS消息发送成功");
                                    }
                                },3000);

                            }else{
                                LoadingDialog.getInstance().closeDialog();
                                ToastUtils.showLongToast(getActivity(),"SOS消息发送失败");
                            }

                        }
                        break;
                }
            }
        });
    }



}
