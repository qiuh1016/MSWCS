package com.zhd.mswcs.moduls.base.view;

import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;

import com.andview.refreshview.XRefreshView;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.weidgt.xrefreshview.CustomGifHeader;
import com.zhd.mswcs.common.weidgt.xrefreshview.CustomerFooter;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;


/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public abstract class BaseRefreshFragment<V extends MvpView,P extends MvpBasePresenter<V>> extends BaseMvpFragment<V,P>{
    public static long lastRefreshTime;
    private XRefreshView refreshView;
    public XRefreshView getRefreshView() {
        return refreshView;
    }


    private boolean isDownRefresh;

    public boolean isDownRefresh() {
        return isDownRefresh;
    }

    @Override
    public void initContentView(View contentView) {
        initRefreshView(contentView);
    }

    /**
     * 初始化下拉刷新组件
     */
    public void initRefreshView(View contentView){
        refreshView = (XRefreshView)contentView.findViewById(R.id.xrefreshview);
        //是否可以下拉刷新,true代表可以,false代表不支持
        refreshView.setPullRefreshEnable(true);
        //是否允许加载更多
        refreshView.setPullLoadEnable(true);
        // 设置上次刷新的时间
        refreshView.restoreLastRefreshTime(lastRefreshTime);
        //当下拉刷新被禁用时，调用这个方法并传入false可以不让头部被下拉
        refreshView.setMoveHeadWhenDisablePullRefresh(true);
        //设置下拉刷新完成之后,刷新头部停留的时间
        refreshView.setPinnedTime(1000);
        //设置是否自动刷新(进入页面自动刷新)
        refreshView.setAutoRefresh(true);
        //自定义头部下拉刷新视图
        //refreshView.setCustomHeaderView(new SmileyHeaderView(getActivity()));
        //refreshView.setCustomHeaderView(new CustomHeader(getActivity(),1000));
        refreshView.setCustomHeaderView(new CustomGifHeader(getActivity()));
        refreshView.setMoveForHorizontal(true);
        //设置当非RecyclerView上拉加载完成以后的回弹时间
        refreshView.setScrollBackDuration(300);

        //自定义尾部加载更多视图
        refreshView.setCustomFooterView(new CustomerFooter(getActivity()));
        //添加下拉刷新监听
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            /**
             * 下拉刷新
             */
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.stopRefresh();
                        lastRefreshTime = refreshView.getLastRefreshTime();
                        refreshData(true);
                    }
                }, 1000);
            }

            /**
             * 上拉加载更多
             * @param isSilence
             */
            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshView.stopLoadMore();
                        refreshData(false);
                    }
                }, 1000);
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
                if (direction > 0) {
                    LogUtils.e("下拉");
                } else {
                    LogUtils.e("上拉");
                }
            }
        });


        refreshView.setOnAbsListViewScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LogUtils.i("onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                LogUtils.i("onScroll");
            }
        });
    }

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
