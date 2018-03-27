package com.zhd.mswcs.moduls.user.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.user.bean.LineBean;
import com.zhd.mswcs.moduls.user.model.LineModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * Created by think-1 on 2017/11/20.
 */

public class LinePresenter extends MvpBasePresenter<MvpView> {
    private LineModel lineModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }
    public LinePresenter(Context context) {
        super(context);
        this.lineModel = new LineModel(context);
    }



    /**
     * 提交路线
     * @param requestParam
     */
    public void submitShipLocus(JSONObject requestParam){
        lineModel.submitShipLocus(requestParam, new HttpUtils.OnLceHttpResultListener<LineBean>() {
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
            public void onResult(LineBean result) {
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
