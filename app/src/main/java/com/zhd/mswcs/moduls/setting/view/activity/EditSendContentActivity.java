package com.zhd.mswcs.moduls.setting.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.CommonUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtils;
import com.zhd.mswcs.common.weidgt.edittext.ClearEditText;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.user.presenter.UserPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_edit_send_content)
public class EditSendContentActivity extends BaseMvpActivity<MvpView,UserPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_send_content)
    private ClearEditText et_send_content;

    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("更改发送内容")
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
                            Intent intent = new Intent();
                            PreferencesUtils.putString(getApplicationContext(),"sos_send_content",et_send_content.getText().toString().trim());
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                })
                .build()
                .builder();
    }





    @Override
    public UserPresenter createPresenter() {
        return new UserPresenter(this);
    }



    private boolean checkValidate(){
        if(StringUtils.isEmpty(et_send_content.getText().toString().trim())){
            ToastUtils.showShortToast(getApplicationContext(),"请填写SOS发送内容");
            return false;
        }
        return true;
    }


    @Override
    public void initData() {
        super.initData();
        et_send_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_send_content.setText(getIntent().getStringExtra("sendContent"));
                    CommonUtils.moveCursor2End(et_send_content);
                }
            }
        });
    }


}
