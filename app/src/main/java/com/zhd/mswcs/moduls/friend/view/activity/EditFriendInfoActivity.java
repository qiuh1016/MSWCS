package com.zhd.mswcs.moduls.friend.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.TimeUtils;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.message.view.activity.ChatActivity;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_edit_friend_info)
public class EditFriendInfoActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_telephone)
    private EditText et_telephone;
    @ViewInject(R.id.tv_send_msg)
    private TextView tv_send_msg;


    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("好友信息")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setRightIcon(R.drawable.btn_green_bg)
                .setRightText("保存")
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkValidate()){
                            editFriendInfo();
                        }
                    }
                })
                .build()
                .builder();
    }


    private void editFriendInfo(){
        Intent intent = new Intent();
        intent.putExtra("position",getIntent().getIntExtra("position",-1));
        intent.putExtra("friendName",et_name.getText().toString().trim());
        intent.putExtra("telephone",et_telephone.getText().toString().trim());
        intent.putExtra("updateTime", TimeUtils.getCurTimeString());
        setResult(RESULT_OK,intent);
        finish();

    }


    @Override
    public UserPresenter createPresenter() {
        return new UserPresenter(this);
    }



    private boolean checkValidate(){
        if(StringUtils.isEmpty(et_name.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请输入姓名");
            return false;
        }else if(StringUtils.isEmpty(et_telephone.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请输入手机号码");
            return false;
        }
        return true;
    }


    @Override
    public void initData() {
        super.initData();
        et_name.setText(getIntent().getStringExtra("friendName"));
        et_telephone.setText(getIntent().getStringExtra("telephone"));
    }


    @Event(value = R.id.tv_send_msg)
    private void sendMessage(View btn){
        Intent intent = new Intent(EditFriendInfoActivity.this, ChatActivity.class);
        intent.putExtra("chatName",getIntent().getStringExtra("friendName"));
        intent.putExtra("chatTelephone",getIntent().getStringExtra("telephone"));
        startActivity(intent);
    }



}
