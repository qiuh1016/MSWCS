package com.zhd.mswcs.moduls.base.view;

import android.os.Bundle;
import com.zhd.mvp.framework.base.presenter.MvpPresenter;
import com.zhd.mvp.framework.support.lce.MvpLceView;
import com.zhd.mvp.framework.support.lce.impl.MvpLceActivity;
import org.xutils.x;

/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public abstract class BaseMvpLceActivity<M, V extends MvpLceView<M>, P extends MvpPresenter<V>> extends MvpLceActivity<M,V,P> {
    private boolean isInit;
    private boolean isPullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initNavigation();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isInit){
            this.isInit = true;
            initData();
        }
    }


    public void initData(){

    }

    public boolean isPullToRefresh() {
        return isPullToRefresh;
    }




    @Override
    public void loadData(boolean pullToRefresh) {
        this.isPullToRefresh = pullToRefresh;
    }

    public abstract void initNavigation();

    @Override
    public void bindData(M data) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
