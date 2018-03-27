package com.zhd.mswcs.moduls.message.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.message.bean.MessageItemServerBean;
import com.zhd.mswcs.moduls.message.bean.MessageServerBean;
import com.zhd.mswcs.moduls.message.model.ChatModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * Created by think-1 on 2017/11/20.
 */

public class MessagePresenter extends MvpBasePresenter<MvpView> {
    private ChatModel chatModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }

    public MessagePresenter(Context context) {
        super(context);
        this.chatModel = new ChatModel(context);
    }


    /**
     * 获取聊天和当前好友的所有聊天记录
     * @param requestParam
     */
    public void getSMS(JSONObject requestParam){
        chatModel.getSMS(requestParam, new HttpUtils.OnLceHttpResultListener<MessageServerBean>() {
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
            public void onResult(MessageServerBean result) {
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
     * 获取所有聊天对象列表
     * @param requestParam
     */
    public void getSMSItem(JSONObject requestParam){
        chatModel.getSMSItem(requestParam, new HttpUtils.OnLceHttpResultListener<MessageItemServerBean>() {
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
            public void onResult(MessageItemServerBean result) {
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
