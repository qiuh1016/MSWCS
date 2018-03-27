package com.zhd.mswcs.common.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.zhd.mswcs.R;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义位置类型弹出框
 */

public class LocationTypeDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private TextView tv_send_location;
    private TextView tv_request_location;
    private TextView tv_cancel;
    private static final String TAG = LocationTypeDialog.class.getSimpleName();
    private SendLocationListener sendLocationListener;
    private RequestLocationListener requestLocationListener;


    public void setSendLocationListener(SendLocationListener sendLocationListener) {
        this.sendLocationListener = sendLocationListener;
    }

    public void setRequestLocationListener(RequestLocationListener requestLocationListener) {
        this.requestLocationListener = requestLocationListener;
    }

    public interface SendLocationListener{
        void sendLocationListener();
    }

    public interface RequestLocationListener{
        void requestLocationListener();
    }


    public LocationTypeDialog(Context context){
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
            commonDialog.setContentView(R.layout.dialog_location_type);
            Window window = commonDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            WindowManager manager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels*2/3;
            window.setAttributes(lp);
        }
    }


    private void initView() {
        tv_send_location = (TextView) commonDialog.findViewById(R.id.tv_send_location);
        tv_request_location = (TextView) commonDialog.findViewById(R.id.tv_request_location);
        tv_cancel = (TextView) commonDialog.findViewById(R.id.tv_cancel);
        tv_send_location.setOnClickListener(this);
        tv_request_location.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
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
          case R.id.tv_send_location:
              dismissDialog();
              if(sendLocationListener!=null){
                  sendLocationListener.sendLocationListener();
              }
              break;

          case R.id.tv_request_location:
              dismissDialog();
              if(requestLocationListener!=null){
                  requestLocationListener.requestLocationListener();
              }
              break;

          case R.id.tv_cancel:
              dismissDialog();
              break;

          default:
              break;
      }
    }

}
