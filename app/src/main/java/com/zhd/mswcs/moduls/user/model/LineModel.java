package com.zhd.mswcs.moduls.user.model;


import android.content.Context;

import com.zhd.mswcs.moduls.base.model.BaseModel;
import com.zhd.mswcs.moduls.user.bean.LineBean;
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
public class LineModel extends BaseModel {

    public LineModel(Context context) {
        super(context);
    }

    /**
     * 提交路线
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void submitShipLocus(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
         RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
         buildService(ServiceApi.class)
         .submitShipLocus(body)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new Observer<LineBean>() {
            @Override
            public void onCompleted() {
                onLceHttpResultListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                onLceHttpResultListener.onError(new Exception(e));
            }

            @Override
            public void onNext(LineBean model) {
                onLceHttpResultListener.onResult(model);
            }
        });
    }



}
