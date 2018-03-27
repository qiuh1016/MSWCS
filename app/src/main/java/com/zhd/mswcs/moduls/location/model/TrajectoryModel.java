package com.zhd.mswcs.moduls.location.model;


import android.content.Context;

import com.zhd.mswcs.moduls.base.model.BaseModel;
import com.zhd.mswcs.moduls.location.bean.MyGpsLocusBean;
import com.zhd.mswcs.moduls.location.bean.TrajectoryBean;
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
public class TrajectoryModel extends BaseModel {

    public TrajectoryModel(Context context) {
        super(context);
    }


    /**
     * 获取当前船的gps位置
     * @param onLceHttpResultListener
     */
    public void getLocalGps(final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        buildService(ServiceApi.class)
                .getLocalGps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TrajectoryBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(TrajectoryBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }




    /**
     * 获取指定时间段自己的轨迹
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void getMyGpsLocus(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .getMyGpsLocus(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyGpsLocusBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(MyGpsLocusBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }


}
