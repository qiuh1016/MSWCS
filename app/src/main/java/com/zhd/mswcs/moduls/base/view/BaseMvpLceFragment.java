package com.zhd.mswcs.moduls.base.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhd.mvp.framework.base.presenter.MvpPresenter;
import com.zhd.mvp.framework.support.lce.MvpLceView;
import com.zhd.mvp.framework.support.lce.impl.MvpLceFragment;

/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public abstract class BaseMvpLceFragment<M, V extends MvpLceView<M>, P extends MvpPresenter<V>> extends MvpLceFragment<M,V,P> {
    //我们自己的Fragment需要缓存视图
    private View viewContent;//缓存视图
    private boolean isInit;
    private boolean isPullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewContent == null){
            viewContent = inflater.inflate(getContentView(),container,false);
            initContentView(viewContent);
        }

        //判断Fragment对应的Activity是否存在这个视图
        ViewGroup parent = (ViewGroup) viewContent.getParent();
        if (parent != null){
            //如果存在,那么我就干掉,重写添加,这样的方式我们就可以缓存视图
            parent.removeView(viewContent);
        }
        return viewContent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isInit){
            this.isInit = true;
            initData();
        }
    }

    public abstract int getContentView();

    public void initData(){

    }

    public boolean isPullToRefresh() {
        return isPullToRefresh;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        super.loadData(pullToRefresh);
        this.isPullToRefresh = pullToRefresh;
    }

    public abstract void initContentView(View contentView);
    public abstract void initNavigation(View contentView);

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
