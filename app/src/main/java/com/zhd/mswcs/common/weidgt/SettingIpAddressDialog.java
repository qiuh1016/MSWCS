package com.zhd.mswcs.common.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.CommonUtils;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义设置IP地址弹出框
 */

public class SettingIpAddressDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private Button btn_confirm;
    private Button btn_cancel;
    private EditText et_ip_address;
    private TextView tv_tip;
    private static final String TAG = SettingIpAddressDialog.class.getSimpleName();
    private ConfirmClickListener confirmClickListener;

    public void setConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface ConfirmClickListener{
        void click(String ipAddress);
    }



    public SettingIpAddressDialog(Context context,String currentIpAddress){
        this.mContext = context;
        initDialog();
        initView(currentIpAddress);
    }

    private void initDialog() {
        if (commonDialog == null) {
            commonDialog = new Dialog(mContext, R.style.MyDialogStyleBottom);
            commonDialog.setCancelable(true);
            commonDialog.setCanceledOnTouchOutside(true);
            commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            commonDialog.setContentView(R.layout.layout_setting_ip_address);
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


    private void initView(String currentIpAddress) {
        et_ip_address = (EditText) commonDialog.findViewById(R.id.et_ip_address);
        btn_cancel = (Button) commonDialog.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) commonDialog.findViewById(R.id.btn_confirm);
        tv_tip = (TextView) commonDialog.findViewById(R.id.tv_tip);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_tip.setText(Html.fromHtml("温馨提示：输入内容格式为:<br/>61.164.208.174:8100或192.168.1.101"));
        if(!StringUtils.isEmpty(currentIpAddress)){
            et_ip_address.setText(currentIpAddress);
        }else{
            et_ip_address.setText("");
        }

        et_ip_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    CommonUtils.moveCursor2End(et_ip_address);
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
              if(StringUtils.isEmpty(et_ip_address.getText().toString().trim())){
                  ToastUtil.showToast(mContext,"请输入IP地址");
                  return;
              }
              dismissDialog();
              if(confirmClickListener!=null){
                  confirmClickListener.click(et_ip_address.getText().toString().trim());
              }
              break;

          case R.id.btn_cancel:
              dismissDialog();
              break;

          default:
              break;
      }
    }

}
