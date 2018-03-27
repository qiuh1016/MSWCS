package com.zhd.mswcs.moduls.location.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.location.adapter.SelectFriendListAdapter;
import com.zhd.mswcs.moduls.location.presenter.SelectFriendPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_select_friend)
public class SelectFriendActivity extends BaseMvpActivity<MvpView,SelectFriendPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.select_friend_recyclerView)
    private RecyclerView select_friend_recyclerView;
    private List<FriendBean> selectFriendBeanList = new ArrayList<>();
    private SelectFriendListAdapter selectFriendListAdapter;


    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("选择好友")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .build()
                .builder();
    }


    @Override
    public SelectFriendPresenter createPresenter() {
        return new SelectFriendPresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
        selectFriendListAdapter = new SelectFriendListAdapter(this,selectFriendBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        select_friend_recyclerView.setLayoutManager(linearLayoutManager);
        select_friend_recyclerView.addItemDecoration(new SimpleDividerDecoration(this));
        select_friend_recyclerView.setAdapter(selectFriendListAdapter);
        initAllUserData();
    }

    private void initAllUserData() {
        selectFriendListAdapter.clearData();
        try {
            List<FriendBean> friendList = MyApplication.getInstance().getDb().selector(FriendBean.class).findAll();
            if(friendList!=null&&friendList.size()>0){
                selectFriendBeanList.addAll(friendList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        selectFriendListAdapter.notifyDataSetChanged();
    }

}
