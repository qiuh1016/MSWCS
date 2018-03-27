package com.zhd.mswcs.moduls.friend.view.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.TimeUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.friend.bean.FriendBean;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_add_friend)
public class AddFriendActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_telephone)
    private EditText et_telephone;

    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("添加好友")
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
                            saveFriendInfo();
                        }
                    }
                })
                .build()
                .builder();
    }


    private void saveFriendInfo(){
        FriendBean friend = new FriendBean();
        friend.setCurrentUser(PreferencesUtils.getString(this,"telephone"));
        friend.setUserName(et_name.getText().toString().trim());
        friend.setNicknNme(et_name.getText().toString().trim());
        friend.setCreateTime(TimeUtils.getCurTimeString());
        friend.setUpdateTime(TimeUtils.getCurTimeString());
        friend.setTelephone(et_telephone.getText().toString().trim());
        try {
            MyApplication.getInstance().getDb().saveBindingId(friend);
            setResult(RESULT_OK);
            finish();
        } catch (DbException e) {
            e.printStackTrace();
            ToastUtil.showToast(AddFriendActivity.this,"保存好友失败");
        }
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
        }else if(StringUtils.equals(et_telephone.getText().toString().trim(),PreferencesUtils.getString(getApplicationContext(),"telephone"))){
            ToastUtils.showShortToast(getApplicationContext(),"不能添加自己为好友");
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


}
