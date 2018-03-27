package com.zhd.mswcs.moduls.location.presenter;

import android.content.Context;

import com.zhd.mswcs.common.interfaces.OnBindDataListener;
import com.zhd.mswcs.moduls.location.bean.MyGpsLocusBean;
import com.zhd.mswcs.moduls.location.bean.TrajectoryBean;
import com.zhd.mswcs.moduls.location.model.TrajectoryModel;
import com.zhd.mswcs.network.HttpUtils;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.json.JSONObject;

/**
 * Created by think-1 on 2017/11/20.
 */

public class TrajectoryPresenter extends MvpBasePresenter<MvpView> {
    private TrajectoryModel trajectoryModel;
    private OnBindDataListener<Object> bindDataListener;
    public void setBtnClickListener(OnBindDataListener<Object> bindDataListener) {
        this.bindDataListener = bindDataListener;
    }
    public TrajectoryPresenter(Context context) {
        super(context);
        this.trajectoryModel = new TrajectoryModel(context);
    }


    /**
     * 获取当前船的gps位置
     */
    public void getLocalGps(){
        trajectoryModel.getLocalGps( new HttpUtils.OnLceHttpResultListener<TrajectoryBean>() {
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
            public void onResult(TrajectoryBean result) {
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
     * 获取指定时间内自己的轨迹
     * @param requestParam
     */
    public void getMyGpsLocus(JSONObject requestParam){
        trajectoryModel.getMyGpsLocus(requestParam, new HttpUtils.OnLceHttpResultListener<MyGpsLocusBean>() {
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
            public void onResult(MyGpsLocusBean result) {
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
