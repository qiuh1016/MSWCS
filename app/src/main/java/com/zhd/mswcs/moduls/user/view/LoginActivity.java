package com.zhd.mswcs.moduls.user.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.common.utils.CommonUtils;
import com.zhd.mswcs.common.utils.LoadingDialog;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.common.weidgt.DownLoadOfflinePackageDialog;
import com.zhd.mswcs.common.weidgt.SettingIpAddressDialog;
import com.zhd.mswcs.config.ActivityCollector;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.user.bean.VerificationCodeBean;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_username)
    private EditText et_username;
    @ViewInject(R.id.et_password)
    private EditText et_password;
    private String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    //动态获取权限监听
    private static PermissionListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownLoadOfflinePackageDialog(this);
        ActivityCollector.add(this);
    }

    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("登录")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .build()
                .builder();
    }


    private void gotoSendVeritification(){
        LoadingDialog.getInstance().showDialog(this,"正在发送验证码，请稍后...");
        JSONObject params = new JSONObject();
        try {
            params.put("userphone",et_username.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getPresenter().sendVertificateCode(params);
        getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
            @Override
            public void bindData(Object data, int requestCode) {
                LoadingDialog.getInstance().closeDialog();
                switch (requestCode){
                    case 1:
                        VerificationCodeBean verificationCodeBean = ((VerificationCodeBean)data);
                        if(verificationCodeBean==null){
                            ToastUtils.showLongToast(LoginActivity.this, R.string.code_system_error);
                        }else{
                            LogUtils.d("verificationCodeBean=",new Gson().toJson(verificationCodeBean));
                            if(verificationCodeBean.isResult()){
                                Intent intent = new Intent(LoginActivity.this, InputVerificationCodeActivity.class);
                                intent.putExtra("userPhone",et_username.getText().toString().trim());
                                startActivity(intent);
                            }else{
                                ToastUtil.showToast(LoginActivity.this,"验证码获取失败");
                            }
                        }
                        break;
                }
            }
        });

//        Intent intent = new Intent(LoginActivity.this, InputVerificationCodeActivity.class);
//        intent.putExtra("userPhone",et_username.getText().toString().trim());
//        startActivity(intent);

    }


    @Override
    public UserPresenter createPresenter() {
        return new UserPresenter(this);
    }


    @Event(value = R.id.btn_next)
    private void goToNext(View btn){
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                if(checkValidate()){
                    gotoSendVeritification();
                }
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                ToastUtil.showToast(LoginActivity.this,"你拒绝了权限请求，某些功能可能无法使用");
            }
        });
    }


    @Event(value = R.id.tv_setting_ip)
    private void goToSettingIp(View btn){
        SettingIpAddressDialog settingIpAddressDialog = new SettingIpAddressDialog(this,PreferencesUtils.getString(this,"ipAddress"));
        settingIpAddressDialog.showDialog();
        settingIpAddressDialog.setConfirmClickListener(new SettingIpAddressDialog.ConfirmClickListener() {
            @Override
            public void click(String ipAddress) {
                PreferencesUtils.putString(getApplicationContext(),"ipAddress",ipAddress);
            }
        });
    }




//    @Event(value = R.id.tv_register)
//    private void goToRegister(View btn){
//        startActivity(new Intent(this,RegisterActivity.class));
//    }


    private boolean checkValidate(){
        if(StringUtils.isEmpty(et_username.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请输入你的手机号码");
            return false;
        } else if(!CommonUtils.isMobileNO(et_username.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"手机号码格式不正确,请核对");
            return false;
        }

//        else if(StringUtils.isEmpty(et_password.getText().toString().trim())){
//            ToastUtils.showShortToast(getApplicationContext(),"请输入密码");
//            return false;
//        }
        return true;
    }


    @Override
    public void initData() {
        super.initData();
    }



    //andrpoid 6.0 需要写运行时权限
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(LoginActivity.this, permissionList.toArray(new String[permissionList.size()]), 1);
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
                    List<String> deniedPermissions = new ArrayList<String>();
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



}
