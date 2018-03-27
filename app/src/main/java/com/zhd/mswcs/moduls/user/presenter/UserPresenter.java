package com.zhd.mswcs.moduls.user.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.user.bean.UserBean;
import com.zhd.mswcs.moduls.user.bean.VerificationCodeBean;
import com.zhd.mswcs.moduls.user.model.UserModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * 作者:zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public class UserPresenter extends MvpBasePresenter<MvpView> {
    private UserModel userModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }
    public UserPresenter(Context context){
        super(context);
        this.userModel = new UserModel(context);
    }

    /**
     * 用户登录
     * @param requestParam
     */
    public void login(JSONObject requestParam){
        userModel.login(requestParam, new HttpUtils.OnLceHttpResultListener<UserBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(bindDataListener!=null){
                    bindDataListener.bindData(null,1);
                }
            }

            @Override
            public void onResult(UserBean result) {
                if(bindDataListener!=null){
                    if (result == null){
                        bindDataListener.bindData(null,1);
                    }else {
                        bindDataListener.bindData(result,1);
                    }
                }
            }
        });
    }



    /**
     * 发送验证码
     * @param requestParam
     */
    public void sendVertificateCode(JSONObject requestParam){
        userModel.sendVertificateCode(requestParam, new HttpUtils.OnLceHttpResultListener<VerificationCodeBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(bindDataListener!=null){
                    bindDataListener.bindData(null,1);
                }
            }

            @Override
            public void onResult(VerificationCodeBean result) {
                if(bindDataListener!=null){
                    if (result == null){
                        bindDataListener.bindData(null,1);
                    }else {
                        bindDataListener.bindData(result,1);
                    }
                }
            }
        });
    }







}
