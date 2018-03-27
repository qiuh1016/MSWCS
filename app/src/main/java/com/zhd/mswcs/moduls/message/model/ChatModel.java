package com.zhd.mswcs.moduls.message.model;


import android.content.Context;

import com.zhd.mswcs.moduls.base.model.BaseModel;
import com.zhd.mswcs.moduls.message.bean.MessageItemServerBean;
import com.zhd.mswcs.moduls.message.bean.MessageServerBean;
import com.zhd.mswcs.moduls.message.bean.SendChatBean;
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
public class ChatModel extends BaseModel {

    public ChatModel(Context context) {
        super(context);
    }

    /**
     * 发送聊天记录
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void sendChat(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
         RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
         buildService(ServiceApi.class)
         .sendChat(body)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new Observer<SendChatBean>() {
            @Override
            public void onCompleted() {
                onLceHttpResultListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                onLceHttpResultListener.onError(new Exception(e));
            }

            @Override
            public void onNext(SendChatBean model) {
                onLceHttpResultListener.onResult(model);
            }
        });
    }




    /**
     * 获取聊天和当前好友的所有聊天记录
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void getSMS(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .getSMS(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageServerBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(MessageServerBean model) {
                        onLceHttpResultListener.onResult(model);
                    }
                });
    }




    /**
     * 获取所有聊天对象列表
     * @param requestParam
     * @param onLceHttpResultListener
     */
    public void getSMSItem(JSONObject requestParam , final HttpUtils.OnLceHttpResultListener onLceHttpResultListener) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestParam.toString());
        buildService(ServiceApi.class)
                .getSMSItem(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageItemServerBean>() {
                    @Override
                    public void onCompleted() {
                        onLceHttpResultListener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLceHttpResultListener.onError(new Exception(e));
                    }

                    @Override
                    public void onNext(MessageItemServerBean model) {
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
