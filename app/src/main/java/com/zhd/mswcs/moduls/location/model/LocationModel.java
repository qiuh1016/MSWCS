package com.zhd.mswcs.moduls.location.model;


import android.content.Context;

import com.zhd.mswcs.moduls.base.model.BaseModel;
import com.zhd.mswcs.moduls.location.bean.GpsItemServerBean;
import com.zhd.mswcs.moduls.message.bean.SendGetGpsBean;
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
public class LocationModel extends BaseModel {

    public LocationModel(Context context) {
        super(context);
    }




    /**
     * 获取所有gps位置列表
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void getGPSItem(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .getGpsItem(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GpsItemServerBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(GpsItemServerBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }



    /**
     * 发送gps位置请求
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void sendGetGps(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .sendGetGps(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendGetGpsBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(SendGetGpsBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }


}
