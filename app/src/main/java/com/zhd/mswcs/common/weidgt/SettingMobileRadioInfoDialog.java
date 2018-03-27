package com.zhd.mswcs.common.weidgt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.CommonUtils;
import com.zhd.mswcs.common.utils.PreferencesUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.weidgt.picture.view.CircleImageView;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义机动电台信息设置弹出框
 */

public class SettingMobileRadioInfoDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private Button btn_confirm;
    private Button btn_cancel;
    private EditText et_mobile_radio_name;
    private RelativeLayout rl_mobile_radio_head;
    private CircleImageView iv_mobile_radio_head;
    private static final String TAG = SettingMobileRadioInfoDialog.class.getSimpleName();
    private ConfirmClickListener confirmClickListener;
    private SelectHeadClickListener selectHeadClickListener;

    public void setSelectHeadClickListener(SelectHeadClickListener selectHeadClickListener) {
        this.selectHeadClickListener = selectHeadClickListener;
    }

    public void setConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface ConfirmClickListener{
        void click(String name,String headStr);
    }

    public interface SelectHeadClickListener{
        void changeHead();
    }


    public SettingMobileRadioInfoDialog(Activity context){
        this.mContext = context;
        initDialog();
        initView();
    }

    private void initDialog() {
        if (commonDialog == null) {
            commonDialog = new Dialog(mContext, R.style.MyDialogStyleBottom);
            commonDialog.setCancelable(true);
            commonDialog.setCanceledOnTouchOutside(true);
            commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            commonDialog.setContentView(R.layout.layout_setting_mobile_radio_info);
            Window window = commonDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            WindowManager manager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels-100;
            window.setAttributes(lp);
        }
    }


    private void initView() {
        rl_mobile_radio_head = (RelativeLayout) commonDialog.findViewById(R.id.rl_mobile_radio_head);
        iv_mobile_radio_head = (CircleImageView) commonDialog.findViewById(R.id.iv_mobile_radio_head);
        et_mobile_radio_name = (EditText) commonDialog.findViewById(R.id.et_mobile_radio_name);
        btn_cancel = (Button) commonDialog.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) commonDialog.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        rl_mobile_radio_head.setOnClickListener(this);
        if(!StringUtils.isEmpty(PreferencesUtils.getString(mContext,"mobileRadioHead"))){
            String userHeadStr = PreferencesUtils.getString(mContext,"mobileRadioHead");
            byte[] bytes = Base64.decode(userHeadStr,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            iv_mobile_radio_head.setImageBitmap(bitmap);
        }

        if(!StringUtils.isEmpty(PreferencesUtils.getString(mContext,"mobileRadioName"))){
            et_mobile_radio_name.setText(PreferencesUtils.getString(mContext,"mobileRadioName"));
        }

        et_mobile_radio_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    CommonUtils.moveCursor2End(et_mobile_radio_name);
                }
            }
        });

    }


    /**
     * 显示dialog
     */
    public void showDialog(){
        if(commonDialog!=null){
            commonDialog.show();
        }
    }

    /**
     * 关闭dialog
     */
    public void dismissDialog(){
        if(commonDialog!=null&&commonDialog.isShowing()){
            commonDialog.dismiss();
        }
    }


    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.btn_confirm:
              if(StringUtils.isEmpty(et_mobile_radio_name.getText().toString().trim())){
                  ToastUtil.showToast(mContext,"请输入机动电台名称");
                  return;
              }

              if(StringUtils.isEmpty(PreferencesUtils.getString(mContext,"mobileRadioHead"))){
                  ToastUtil.showToast(mContext,"请设置机动电台头像");
                  return;
              }
              dismissDialog();
              if(confirmClickListener!=null){
                  confirmClickListener.click(et_mobile_radio_name.getText().toString().trim(),PreferencesUtils.getString(mContext,"mobileRadioHead"));
              }
              break;

          case R.id.rl_mobile_radio_head:
              if(selectHeadClickListener!=null){
                  selectHeadClickListener.changeHead();
              }
              break;

          case R.id.btn_cancel:
              dismissDialog();
              break;

          default:
              break;
      }
    }

    /**
     * 设置机动电台头像数据
     * @param bitmap
     */
    public void setImageData(Bitmap bitmap ){
        iv_mobile_radio_head.setImageBitmap(bitmap);
    }

}
