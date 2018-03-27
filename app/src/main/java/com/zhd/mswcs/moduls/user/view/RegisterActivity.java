package com.zhd.mswcs.moduls.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_user_name)
    private EditText et_user_name;
    @ViewInject(R.id.et_user_account)
    private EditText et_user_account;
    @ViewInject(R.id.et_user_password)
    private EditText et_user_password;

    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("注册")
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


    private void gotoRegister(){
//        LoadingDialog.getInstance().showDialog(this,"正在登录，请稍后");
//        HashMap<String,Object> params = new HashMap<>();
//        params.put("userName",et_username.getText().toString().trim());
//        params.put("password",et_password.getText().toString().trim());
//        params.put("clientId", PreferencesUtils.getString(this,"clientId"));
//        getPresenter().register(params);
//        getPresenter().setBtnClickListener(new OnBindDataListener<Object>() {
//            @Override
//            public void bindData(Object data, int requestCode) {
//                LoadingDialog.getInstance().closeDialog();
//                switch (requestCode){
//                    case 1:
//                        UserBean userBean = ((UserBean)data);
//                        if(userBean==null){
//                            ToastUtils.showLongToast(LoginActivity.this, R.string.code_system_error);
//                        }else{
//                            LogUtils.d("userBean=",new Gson().toJson(userBean));
//                            if(userBean.getCode()== Constant.CODE_SUCCESS){
//                                MyApplication.getInstance().setUser(userBean);
//                                //用于记住密码功能
//                                PreferencesUtils.putString(getApplicationContext(),"userName",et_username.getText().toString().trim());
//                                PreferencesUtils.putString(getApplicationContext(),"password",et_password.getText().toString().trim());
//                                PreferencesUtils.putString(getApplicationContext(),"token",userBean.getData().getToken());
//                                PreferencesUtils.putString(getApplicationContext(),"realName",userBean.getData().getRealName());
//                                PreferencesUtils.putString(getApplicationContext(),"latitude",String.valueOf(userBean.getData().getLatitude()));
//                                PreferencesUtils.putString(getApplicationContext(),"longitude",String.valueOf(userBean.getData().getLongitude()));
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                finish();
//                            }else{
//                                ToastUtils.showLongToast(LoginActivity.this,userBean.getMsg());
//                            }
//
//                        }
//                        break;
//                }
//            }
//        });

        startActivity(new Intent(RegisterActivity.this, InputVerificationCodeActivity.class));
    }


    @Override
    public UserPresenter createPresenter() {
        return new UserPresenter(this);
    }


    @Event(value = R.id.btn_register)
    private void gotoRegister(View btn){
        if(checkValidate()){
            gotoRegister();
        }
    }


    private boolean checkValidate(){
        if(StringUtils.isEmpty(et_user_name.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请输入姓名");
            return false;
        } else if(StringUtils.isEmpty(et_user_account.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请输入账号");
            return false;
        }else if(StringUtils.isEmpty(et_user_password.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请输入密码");
            return false;
        }
        return true;
    }


    @Override
    public void initData() {
        super.initData();
    }


}
