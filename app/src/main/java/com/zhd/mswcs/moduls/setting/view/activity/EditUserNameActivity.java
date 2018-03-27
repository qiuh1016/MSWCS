package com.zhd.mswcs.moduls.setting.view.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.CommonUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.setting.presenter.EditUserNamePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_edit_user_name)
public class EditUserNameActivity extends BaseMvpActivity<MvpView,EditUserNamePresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_name)
    private EditText et_name;


    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("更改姓名")
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
                        if(StringUtils.isEmpty(et_name.getText().toString().trim())){
                            ToastUtil.showToast(EditUserNameActivity.this,"请填写姓名");
                            return;
                        }

                        PreferencesUtils.putString(getApplicationContext(),"userName",et_name.getText().toString().trim());
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .build()
                .builder();
    }


    @Override
    public EditUserNamePresenter createPresenter() {
        return new EditUserNamePresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
        if(StringUtils.equals(getIntent().getStringExtra("userName"),"未设置")){
            et_name.setText("");
        }else{
            et_name.setText(getIntent().getStringExtra("userName"));
        }

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    CommonUtils.moveCursor2End(et_name);
                }
            }
        });

    }



}
