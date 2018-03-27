package com.zhd.mswcs.moduls.location.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.location.bean.GpsItemServerBean;
import com.zhd.mswcs.moduls.location.model.LocationModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * Created by think-1 on 2017/11/20.
 */

public class AllUserLocationPresenter extends MvpBasePresenter<MvpView> {
    private LocationModel locationModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }
    public AllUserLocationPresenter(Context context) {
        super(context);
        this.locationModel = new LocationModel(context);
    }



    /**
     * 获取所有gps位置列表
     * @param requestParam
     */
    public void getGPSItem(JSONObject requestParam){
        locationModel.getGPSItem(requestParam, new HttpUtils.OnLceHttpResultListener<GpsItemServerBean>() {
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
            public void onResult(GpsItemServerBean result) {
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
