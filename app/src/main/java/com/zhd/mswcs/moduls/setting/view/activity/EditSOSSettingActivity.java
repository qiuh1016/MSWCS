package com.zhd.mswcs.moduls.setting.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.LogUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.weidgt.ShowTelephoneDetailDialog;
import com.zhd.mswcs.constant.Constant;
import com.zhd.mswcs.moduls.base.view.BaseMvpActivity;
import com.zhd.mswcs.moduls.base.view.navigation.impl.DefaultNavigation;
import com.zhd.mswcs.moduls.setting.bean.SendTelephoneListBean;
import com.zhd.mswcs.moduls.setting.presenter.EditSOSSettingPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by think-1 on 2017/11/21.
 */
@ContentView(R.layout.activity_edit_sos_setting)
public class EditSOSSettingActivity extends BaseMvpActivity<MvpView,EditSOSSettingPresenter> implements MvpView{
    @ViewInject(R.id.ll_rootView)
    private LinearLayout ll_rootView;
    @ViewInject(R.id.tv_send_content)
    private TextView tv_send_content;
    @ViewInject(R.id.tv_send_telephone)
    private TextView tv_send_telephone;
    @ViewInject(R.id.iv_show_detail)
    private ImageView iv_show_detail;
    private static final int EDIT_SEND_CONTENT_REQUEST_CODE = 1;
    private static final int EDIT_SEND_TELEPHONE_REQUEST_CODE = 2;


    @Override
    public EditSOSSettingPresenter createPresenter() {
        return new EditSOSSettingPresenter(this);
    }

    @Override
    public void initNavigation() {
        new DefaultNavigation
                .Builder(this,ll_rootView)
                .setTitle("更改SOS设置")
                .setTitleColor(getResources().getColor(R.color.common_white),true)
                .setHeadBgColor(getResources().getColor(R.color.common_black))
                .setLeftIcon(R.mipmap.img_back)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(Constant.SOS_SETTING_BRO);
                        sendBroadcast(intent);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .build()
                .builder();
    }

    @Override
    public void initData() {
        super.initData();
        if(StringUtils.isEmpty(PreferencesUtils.getString(this,"sos_send_content"))){
            tv_send_content.setText("未设置");
        }else{
            tv_send_content.setText(PreferencesUtils.getString(this,"sos_send_content"));
        }


        if(StringUtils.isEmpty(PreferencesUtils.getString(this,"sos_send_telephone"))){
            tv_send_telephone.setText("未设置");
            iv_show_detail.setVisibility(View.GONE);
        }else{
            String str = PreferencesUtils.getString(this,"sos_send_telephone");
            tv_send_telephone.setText(str);
            iv_show_detail.setVisibility(View.GONE);
        }

//        List<SendTelephoneListBean> list = null;
//        try {
//            String currentUser = PreferencesUtils.getString(EditSOSSettingActivity.this,"telephone");
//            list = MyApplication.getInstance().getDb().selector(SendTelephoneListBean.class).where("currentUser","=",currentUser).and("isDelete","=","0").findAll();
//            if(list!=null&&list.size()>0){
//                if(list.size()==1){
//                    tv_send_telephone.setText(list.get(0).getContactTelephone());
//                }else{
//                    tv_send_telephone.setText(list.get(0).getContactTelephone()+"...");
//                }
//                iv_show_detail.setVisibility(View.VISIBLE);
//            }else{
//                iv_show_detail.setVisibility(View.GONE);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//            LogUtils.e("querySOSTelephone","query error====");
//        }


    }


    @Event(value = R.id.rl_send_content)
    private void SettingSendContent(View btn){
        Intent intent = new Intent( EditSOSSettingActivity.this,EditSendContentActivity.class);
        if(StringUtils.equals(tv_send_content.getText().toString().trim(),"未设置")){
            intent.putExtra("sendContent","");
        }else{
            intent.putExtra("sendContent",tv_send_content.getText().toString().trim());
        }
        startActivityForResult(intent,EDIT_SEND_CONTENT_REQUEST_CODE);

    }


    @Event(value = R.id.rl_send_telephone)
    private void SettingSendTelephone(View btn){
//        Intent intent = new Intent(EditSOSSettingActivity.this, SelectContactsListActivity.class);
//        startActivityForResult(intent,EDIT_SEND_TELEPHONE_REQUEST_CODE);
        Intent intent = new Intent( EditSOSSettingActivity.this,SelectSendFriendActivity.class);
        startActivityForResult(intent,EDIT_SEND_TELEPHONE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case  EDIT_SEND_CONTENT_REQUEST_CODE:
                    tv_send_content.setText(PreferencesUtils.getString(this,"sos_send_content"));
                    break;
                case EDIT_SEND_TELEPHONE_REQUEST_CODE:
                    String str = PreferencesUtils.getString(this,"sos_send_telephone");
                    if(StringUtils.isEmpty(str)){
                        iv_show_detail.setVisibility(View.GONE);
                    }else{
                        tv_send_telephone.setText(str);
                        iv_show_detail.setVisibility(View.GONE);
                    }

//                    List<SendTelephoneListBean> list = null;
//                    try {
//                        String currentUser = PreferencesUtils.getString(EditSOSSettingActivity.this,"telephone");
//                        list = MyApplication.getInstance().getDb().selector(SendTelephoneListBean.class).where("currentUser","=",currentUser).and("isDelete","=","0").findAll();
//                        if(list!=null&&list.size()>0){
//                            if(list.size()==1){
//                                tv_send_telephone.setText(list.get(0).getContactTelephone());
//                            }else{
//                                tv_send_telephone.setText(list.get(0).getContactTelephone()+"...");
//                            }
//                            iv_show_detail.setVisibility(View.VISIBLE);
//                        }else{
//                            iv_show_detail.setVisibility(View.GONE);
//                        }
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                        LogUtils.e("querySOSTelephone","query error====");
//                    }
                    break;
                default:
                    break;
            }

    }


    @Event(value = R.id.iv_show_detail)
    private void showDetailInfo(View btn){
//        String str = PreferencesUtils.getString(this,"sos_send_telephone");
//        String name = PreferencesUtils.getString(this,"sos_send_name");
//        StringBuilder stringBuilder = new StringBuilder();
//        String allTelephone = "";
//        String[] array = str.split(",");
//        String[] nameArray = name.split(",");
//        if(array.length>1){
//            for(int i=0;i<array.length;i++){
//                if(i==array.length-1){
//                    stringBuilder.append(nameArray[i]+":"+array[i]);
//                }else{
//                    stringBuilder.append(nameArray[i]+":"+ array[i]+"<br/>");
//                }
//            }
//            allTelephone = stringBuilder.toString();
//        }else{
//            allTelephone = str;
//        }

        List<SendTelephoneListBean> list = null;
        try {
            String currentUser = PreferencesUtils.getString(EditSOSSettingActivity.this,"telephone");
            list = MyApplication.getInstance().getDb().selector(SendTelephoneListBean.class).where("currentUser","=",currentUser).and("isDelete","=","0").findAll();
            ShowTelephoneDetailDialog showTelephoneDetailDialog = new ShowTelephoneDetailDialog(EditSOSSettingActivity.this,list);
            showTelephoneDetailDialog.showDialog();
        } catch (DbException e) {
            e.printStackTrace();
            LogUtils.e("querySOSTelephone","query error====");
        }
    }
}
