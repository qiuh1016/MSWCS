package com.zhd.mswcs.moduls.setting.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.setting.adapter.SelectSendFriendListAdapter;
import com.zhd.mswcs.moduls.setting.bean.SelectFriendListBean;
import com.zhd.mswcs.moduls.setting.presenter.EditEscalationCyclePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_edit_send_telephone)
public class SelectSendFriendActivity extends BaseMvpActivity<MvpView,EditEscalationCyclePresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.send_telephone_recyclerView)
    private RecyclerView send_telephone_recyclerView;
    private List<SelectFriendListBean> selectFriendBeanList = new ArrayList<>();
    private SelectSendFriendListAdapter selectFriendListAdapter;


    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("选择好友")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setRightText("保存")
                .setRightIcon(R.drawable.btn_green_bg)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if(PreferencesUtils.getInt(SelectSendFriendActivity.this,"sos_position")==-1){
                          ToastUtil.showToast(SelectSendFriendActivity.this,"请先选择好友");
                      }else{
                          int pos = PreferencesUtils.getInt(SelectSendFriendActivity.this,"sos_position");
                          String sos_telephone = selectFriendBeanList.get(pos).getTelephone();
                          if(!StringUtils.isEmpty(sos_telephone)){
                              PreferencesUtils.putString(getApplicationContext(),"sos_send_telephone",sos_telephone);
                              Intent intent = new Intent();
                              setResult(RESULT_OK,intent);
                              finish();
                          }

                      }

                    }
                })
                .build()
                .builder();
    }


    @Override
    public EditEscalationCyclePresenter createPresenter() {
        return new EditEscalationCyclePresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
        selectFriendListAdapter = new SelectSendFriendListAdapter(this,selectFriendBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        send_telephone_recyclerView.setLayoutManager(linearLayoutManager);
        send_telephone_recyclerView.addItemDecoration(new SimpleDividerDecoration(this));
        send_telephone_recyclerView.setAdapter(selectFriendListAdapter);
        initAllUserData();
    }

    private void initAllUserData() {
        selectFriendListAdapter.clearData();
        try {
            List<SelectFriendListBean> friendList = MyApplication.getInstance().getDb().selector(SelectFriendListBean.class).where("currentUser","=", PreferencesUtils.getString(this,"telephone")).findAll();
            if(friendList!=null&&friendList.size()>0){
                selectFriendBeanList.addAll(friendList);
                String currentUser = PreferencesUtils.getString(this,"telephone");
                for(int i=0;i<selectFriendBeanList.size();i++){
                    if(StringUtils.equals(selectFriendBeanList.get(i).getTelephone(),currentUser)){
                        selectFriendBeanList.remove(i);
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        selectFriendListAdapter.notifyDataSetChanged();
    }

}
