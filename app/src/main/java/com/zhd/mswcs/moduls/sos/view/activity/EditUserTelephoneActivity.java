package com.zhd.mswcs.moduls.sos.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.CommonUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.SpecialNavigation;
import com.zhd.mswcs.moduls.sos.presenter.EditUserTelephonePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zhanghanjun on 2017/9/13.
 */
@ContentView(R.layout.activity_edit_user_telephone)
public class EditUserTelephoneActivity extends BaseMvpActivity<MvpView,EditUserTelephonePresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.et_telephone)
    private EditText et_telephone;


    @Override
    public void initNavigation() {
        new SpecialNavigation
                .Builder(this,ll_rootView)
                .setTitle("设置手机号码")
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
                        if(StringUtils.isEmpty(et_telephone.getText().toString().trim())){
                            ToastUtil.showToast(EditUserTelephoneActivity.this,"请设置你的手机号码");
                            return;
                        }
                        PreferencesUtils.putString(getApplicationContext(),"telephone",et_telephone.getText().toString().trim());
                        Intent intent = new Intent(Constant.BIND_TELEPHONE_BRO);
                        sendBroadcast(intent);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .build()
                .builder();
    }


    @Override
    public EditUserTelephonePresenter createPresenter() {
        return new EditUserTelephonePresenter(this);
    }





    @Override
    public void initData() {
        super.initData();
        if(StringUtils.equals(getIntent().getStringExtra("telephone"),"未设置")){
            et_telephone.setText("");
        }else{
            et_telephone.setText(getIntent().getStringExtra("telephone"));
        }

        et_telephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    CommonUtils.moveCursor2End(et_telephone);
                }
            }
        });

    }



}
