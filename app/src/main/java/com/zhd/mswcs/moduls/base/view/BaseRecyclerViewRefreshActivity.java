package com.zhd.mswcs.moduls.base.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.decoration.DividerItemDecoration;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public abstract class BaseRecyclerViewRefreshActivity<V extends MvpView,P extends MvpBasePresenter<V>> extends BaseMvpActivity<V,P>{

    private XRefreshView refreshView;
    private RecyclerView recyclerView;
    private BaseRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager linearLayoutManager;

    public XRefreshView getRefreshView() {
        return refreshView;
    }

    public BaseRecyclerAdapter getAdapter() {
        return recyclerAdapter;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    private boolean isDownRefresh;

    public boolean isDownRefresh() {
        return isDownRefresh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRefreshView();
    }

    /**
     * 初始化下拉刷新组件
     */
    public void initRefreshView(){
        refreshView = (XRefreshView)findViewById(R.id.xrefreshview);
        //是否可以下拉刷新,true代表可以,false代表不支持
        refreshView.setPullRefreshEnable(true);
        //是否允许加载更多
        refreshView.setPullLoadEnable(true);
        //设置下拉刷新完成之后,刷新头部停留的时间
        refreshView.setPinnedTime(1000);
        //设置是否自动刷新(进入页面自动刷新)
        refreshView.setAutoRefresh(false);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        //设置列表为垂直方向显示
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(20));

        //绑定Adapter
        recyclerAdapter = bindAdapter();
        recyclerView.setAdapter(recyclerAdapter);

        //给我们的Adapter添加加载更多的布局
        recyclerAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));

        //添加下拉刷新监听
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){

            /**
             * 下拉刷新
             */
            @Override
            public void onRefresh() {
                super.onRefresh();
                refreshData(true);
            }

            /**
             * 上拉加载更多
             * @param isSlience
             */
            @Override
            public void onLoadMore(boolean isSlience) {
                super.onLoadMore(isSlience);
                refreshData(false);
            }
        });
    }

    public abstract BaseRecyclerAdapter bindAdapter();

    public void refreshData(boolean isDownRefresh){
        this.isDownRefresh = isDownRefresh;
    }


    //提供给子类决定是否需要下拉刷新功能
    public void setRefresh(boolean isRefresh, boolean isLoadMore) {
        //是否可以下拉刷新,true代表可以,false代表不支持
        refreshView.setPullRefreshEnable(isRefresh);
        //是否允许加载更多
        refreshView.setPullLoadEnable(isLoadMore);
    }


}
