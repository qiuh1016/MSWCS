package com.zhd.mswcs.moduls.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.service.NetworkStateService;
import com.zhd.mswcs.common.service.SearchLocationService;
import com.zhd.mswcs.common.utils.LoadingDialog;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.config.ActivityCollector;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.user.bean.UserBean;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_input_verification_code)
public class InputVerificationCodeActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.tv_telphone)
    private TextView tv_telphone;
    @ViewInject(R.id.et_verification_code)
    private EditText et_verification_code;
    @ViewInject(R.id.btn_complete)
    private Button btn_complete;
    private String telephone="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.add(this);
    }

    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("填写验证码")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .build()
                .builder();
    }


    private void gotoLogin(){
        LoadingDialog.getInstance().showDialog(this,"正在登录，请稍后");
        JSONObject params = new JSONObject();
        try {
            params.put("userPhone",telephone);
            params.put("checkCode",et_verification_code.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getPresenter().login(params);
        getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
            @Override
            public void bindData(Object data, int requestCode) {
                LoadingDialog.getInstance().closeDialog();
                switch (requestCode){
                    case 1:
                        UserBean userBean = ((UserBean)data);
                        if(userBean==null){
                            ToastUtil.showToast(InputVerificationCodeActivity.this, R.string.code_system_error);
                        }else{
                            LogUtils.d("userBean=",new Gson().toJson(userBean));
                            if(userBean.isResult()){
                                MyApplication.getInstance().setUser(userBean);
                                PreferencesUtils.putString(getApplicationContext(),"telephone",telephone);
                                startService(new Intent(InputVerificationCodeActivity.this, SearchLocationService.class));
                                startService(new Intent(InputVerificationCodeActivity.this, NetworkStateService.class));
                                startActivity(new Intent(InputVerificationCodeActivity.this, SelectUserTypeActivity.class));
                                ActivityCollector.finishAll();
                            }else{
                                ToastUtil.showToast(InputVerificationCodeActivity.this,"验证码不正确，请重新输入");
                            }
                        }
                        break;
                }
            }
        });


//        PreferencesUtils.putString(getApplicationContext(),"telephone",telephone);
//        startService(new Intent(InputVerificationCodeActivity.this, SearchLocationService.class));
//        startService(new Intent(InputVerificationCodeActivity.this, NetworkStateService.class));
//        startActivity(new Intent(InputVerificationCodeActivity.this, SelectUserTypeActivity.class));
//        ActivityCollector.finishAll();

    }


    @Override
    public UserPresenter createPresenter() {
        return new UserPresenter(this);
    }


    @Event(value = R.id.btn_complete)
    private void gotoLogin(View btn){
        if(checkValidate()){
            gotoLogin();
        }
    }


    private boolean checkValidate(){
        if(StringUtils.isEmpty(et_verification_code.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请填写验证码");
            return false;
        }
        return true;
    }


    @Override
    public void initData() {
        super.initData();
        telephone = getIntent().getStringExtra("userPhone");
        if(!StringUtils.isEmpty(telephone)){
            tv_telphone.setText(telephone.substring(0,3)+"  "+telephone.substring(3,7)+"  "+telephone.substring(7,11));
        }

    }


}
