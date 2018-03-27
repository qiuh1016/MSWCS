package com.zhd.mswcs.moduls.message.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.message.bean.SendGpsBean;
import com.zhd.mswcs.moduls.message.model.SendLocationModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * Created by think-1 on 2017/11/20.
 */

public class SendLocationPresenter extends MvpBasePresenter<MvpView> {
    private SendLocationModel sendLocationModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }
    public SendLocationPresenter(Context context) {
        super(context);
        this.sendLocationModel = new SendLocationModel(context);
    }


    /**
     * 发送gps位置
     * @param requestParam
     */
    public void sendGps(JSONObject requestParam){
        sendLocationModel.sendGps(requestParam, new HttpUtils.OnLceHttpResultListener<SendGpsBean>() {
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
            public void onResult(SendGpsBean result) {
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
