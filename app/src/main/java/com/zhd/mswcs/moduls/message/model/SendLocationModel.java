package com.zhd.mswcs.moduls.message.model;


import android.content.Context;

import com.zhd.mswcs.moduls.base.model.BaseModel;
import com.zhd.mswcs.moduls.message.bean.SendGpsBean;
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
public class SendLocationModel extends BaseModel {

    public SendLocationModel(Context context) {
        super(context);
    }

    /**
     * 发送gps位置
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void sendGps(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .sendGps(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendGpsBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(SendGpsBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }


}
