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
 * 自定义添加好友方式弹出框
 */

public class AddFriendTypeDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private TextView tv_hand_add;
    private TextView tv_import_contacts;
    private TextView tv_cancel;
    private static final String TAG = AddFriendTypeDialog.class.getSimpleName();
    private HandAndListener handAndListener;
    private ImportContactsListener importContactsListener;

    public void setHandAndListener(HandAndListener handAndListener) {
        this.handAndListener = handAndListener;
    }

    public void setImportContactsListener(ImportContactsListener importContactsListener) {
        this.importContactsListener = importContactsListener;
    }

    public interface HandAndListener{
        void handAndListener();
    }

    public interface ImportContactsListener{
        void importContactsListener();
    }


    public AddFriendTypeDialog(Context context){
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
            commonDialog.setContentView(R.layout.dialog_add_friend_method);
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
        tv_hand_add = (TextView) commonDialog.findViewById(R.id.tv_hand_add);
        tv_import_contacts = (TextView) commonDialog.findViewById(R.id.tv_import_contacts);
        tv_cancel = (TextView) commonDialog.findViewById(R.id.tv_cancel);
        tv_hand_add.setOnClickListener(this);
        tv_import_contacts.setOnClickListener(this);
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
          case R.id.tv_hand_add:
              dismissDialog();
              if(handAndListener!=null){
                  handAndListener.handAndListener();
              }
              break;

          case R.id.tv_import_contacts:
              dismissDialog();
              if(importContactsListener!=null){
                  importContactsListener.importContactsListener();
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
