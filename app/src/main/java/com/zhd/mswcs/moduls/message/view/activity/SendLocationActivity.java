package com.zhd.mswcs.moduls.message.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.LoadingDialog;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.message.bean.SendGpsBean;
import com.zhd.mswcs.moduls.message.presenter.SendLocationPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_send_location)
public class SendLocationActivity extends BaseMvpActivity<MvpView,SendLocationPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    private Handler mHandler = new Handler();


    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("位置")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setRightIcon(R.drawable.btn_green_bg)
                .setRightText("发送")
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadingDialog.getInstance().showDialog(SendLocationActivity.this,"正在发送位置，请稍后...");
                        JSONObject params = new JSONObject();
                        try {
                            params.put("sender", PreferencesUtils.getString(SendLocationActivity.this,"telephone"));
                            params.put("receiver",getIntent().getStringExtra("chatTelephone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getPresenter().sendGps(params);
                        getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
                            @Override
                            public void bindData(Object data, int requestCode) {
                                switch (requestCode){
                                    case 1:
                                        SendGpsBean sendGpsBean = ((SendGpsBean)data);
                                        if(sendGpsBean==null){
                                            ToastUtils.showLongToast(SendLocationActivity.this, R.string.code_system_error);
                                        }else{
                                            LogUtils.d("sendGpsBean=",new Gson().toJson(sendGpsBean));
                                            if(sendGpsBean.isResult()){
                                                mHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        LoadingDialog.getInstance().closeDialog();
                                                        Intent intent = new Intent();
                                                        setResult(RESULT_OK,intent);
                                                        finish();
                                                    }
                                                },3000);

                                            }else{
                                                ToastUtil.showToast(SendLocationActivity.this,"位置发送失败");
                                                Intent intent = new Intent();
                                                setResult(RESULT_OK,intent);
                                                finish();
                                            }
                                        }
                                        break;
                                }
                            }
                        });
                    }
                })
                .build()
                .builder();
    }


    @Override
    public SendLocationPresenter createPresenter() {
        return new SendLocationPresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
    }



}
