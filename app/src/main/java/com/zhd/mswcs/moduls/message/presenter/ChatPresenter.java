package com.zhd.mswcs.moduls.message.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.message.bean.SendChatBean;
import com.zhd.mswcs.moduls.message.bean.SendGetGpsBean;
import com.zhd.mswcs.moduls.message.model.ChatModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * Created by think-1 on 2017/11/20.
 */

public class ChatPresenter extends MvpBasePresenter<MvpView> {
    private ChatModel chatModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }
    public ChatPresenter(Context context) {
        super(context);
        this.chatModel = new ChatModel(context);

    }

    /**
     * 发送聊天记录
     * @param requestParam
     */
    public void sendChat(JSONObject requestParam){
        chatModel.sendChat(requestParam, new HttpUtils.OnLceHttpResultListener<SendChatBean>() {
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
            public void onResult(SendChatBean result) {
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
     * 发送gps位置请求
     * @param requestParam
     */
    public void sendGetGps(JSONObject requestParam){
        chatModel.sendGetGps(requestParam, new HttpUtils.OnLceHttpResultListener<SendGetGpsBean>() {
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
            public void onResult(SendGetGpsBean result) {
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
