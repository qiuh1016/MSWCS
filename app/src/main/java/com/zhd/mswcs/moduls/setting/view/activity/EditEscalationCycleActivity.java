package com.zhd.mswcs.moduls.setting.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.setting.bean.EscalationCycleBean;
import com.zhd.mswcs.moduls.setting.adapter.EscalationCycleListAdapter;
import com.zhd.mswcs.moduls.setting.presenter.EditEscalationCyclePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_edit_escalation_cycle)
public class EditEscalationCycleActivity extends BaseMvpActivity<MvpView,EditEscalationCyclePresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.escalation_cycle_recyclerView)
    private RecyclerView escalation_cycle_recyclerView;
    private List<EscalationCycleBean> escalationCycleBeanList = new ArrayList<>();
    private EscalationCycleListAdapter escalationCycleListAdapter;


    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("更改上报周期")
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
                        List<EscalationCycleBean> list = escalationCycleListAdapter.getDataList();
                        String time = "";
                        for(EscalationCycleBean bean:list){
                            if(bean.isSelected()){
                                time = bean.getTime();
                                break;
                            }
                        }

                        if(!StringUtils.isEmpty(time)){
                            Intent intent = new Intent();
                            intent.putExtra("cycleName",time);
                            setResult(RESULT_OK,intent);
                            finish();
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
        escalationCycleListAdapter = new EscalationCycleListAdapter(this,escalationCycleBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        escalation_cycle_recyclerView.setLayoutManager(linearLayoutManager);
        escalation_cycle_recyclerView.addItemDecoration(new SimpleDividerDecoration(this));
        escalation_cycle_recyclerView.setAdapter(escalationCycleListAdapter);
        initEscalationCycleData();
    }

    private void initEscalationCycleData() {
        escalationCycleListAdapter.clearData();
        escalationCycleBeanList.add(new EscalationCycleBean("001","5分钟",true));
        escalationCycleBeanList.add(new EscalationCycleBean("002","10分钟",false));
        escalationCycleBeanList.add(new EscalationCycleBean("003","30分钟",false));
        escalationCycleListAdapter.notifyDataSetChanged();
    }

}
