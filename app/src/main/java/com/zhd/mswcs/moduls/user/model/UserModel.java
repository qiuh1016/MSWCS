package com.zhd.mswcs.moduls.user.model;


import android.content.Context;

import com.zhd.mswcs.moduls.base.model.BaseModel;
import com.zhd.mswcs.moduls.user.bean.UserBean;
import com.zhd.mswcs.moduls.user.bean.VerificationCodeBean;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mswcs.network.ServiceApi;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: zhj on 16/9/24 21:09
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public class UserModel extends BaseModel {

    public UserModel(Context context) {
        super(context);
    }

    /**
     * 用户登录
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void login(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
         RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
         buildService(ServiceApi.class)
         .login(body)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new Observer<UserBean>() {
            @Override
            public void onCompleted() {
                onLceHttpResultListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                onLceHttpResultListener.onError(new Exception(e));
            }

            @Override
            public void onNext(UserBean model) {
                onLceHttpResultListener.onResult(model);
            }
        });
    }




    /**
     * 发送验证码
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void sendVertificateCode(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .sendVertificateCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VerificationCodeBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(VerificationCodeBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }




}
