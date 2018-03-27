package com.zhd.mswcs.moduls.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.activity.HomeActivity;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_select_user_type)
public class SelectUserTypeActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.btn_user_mobile_subscriber)
    private Button btn_user_mobile_subscriber;
    @ViewInject(R.id.btn_user_shore_station)
    private Button btn_user_shore_station;


    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("选择用户类型")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .build()
                .builder();
    }


    @Override
    public UserPresenter createPresenter() {
        return new UserPresenter(this);
    }


    @Event(value = R.id.btn_user_mobile_subscriber)
    private void goToMobileSubscriber(View btn){
        startActivity(new Intent(this, SubmitLineActivity.class));
        finish();
    }


    @Event(value = R.id.btn_user_shore_station)
    private void goToShoreStation(View btn){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }



    @Override
    public void initData() {
        super.initData();
    }



}
