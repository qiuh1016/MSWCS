package com.zhd.mswcs.common.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.common.utils.ToastUtil;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义机动电台密码验证弹出框
 */

public class SettingMobileRadioPasswordDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private Button btn_confirm;
    private Button btn_cancel;
    private EditText et_commander_pwd;
    private static final String TAG = SettingMobileRadioPasswordDialog.class.getSimpleName();
    private ConfirmClickListener confirmClickListener;

    public void setConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface ConfirmClickListener{
        void click(String pwd);
    }



    public SettingMobileRadioPasswordDialog(Context context){
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
            commonDialog.setContentView(R.layout.layout_setting_mobile_radio_pwd);
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
        et_commander_pwd = (EditText) commonDialog.findViewById(R.id.et_commander_pwd);
        btn_cancel = (Button) commonDialog.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) commonDialog.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
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
              if(StringUtils.isEmpty(et_commander_pwd.getText().toString().trim())){
                  ToastUtil.showToast(mContext,"请输入指挥长密码");
                  return;
              }
              dismissDialog();
              if(confirmClickListener!=null){
                  confirmClickListener.click(et_commander_pwd.getText().toString().trim());
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
