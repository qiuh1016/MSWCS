package com.zhd.mswcs.moduls.base.view;

import android.os.Bundle;

import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;
import com.zhd.mvp.framework.support.view.MvpActivity;
import org.xutils.x;

/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public abstract class BaseMvpActivity<V extends MvpView,P extends MvpBasePresenter<V>> extends MvpActivity<V,P> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initNavigation();
        initData();
    }


    public void initData(){}
    public abstract void initNavigation();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
