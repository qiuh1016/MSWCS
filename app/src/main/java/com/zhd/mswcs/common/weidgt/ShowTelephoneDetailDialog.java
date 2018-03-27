package com.zhd.mswcs.common.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.zhd.mswcs.R;
import com.zhd.mswcs.moduls.base.view.decoration.SimpleDividerDecoration;
import com.zhd.mswcs.moduls.setting.adapter.SOSTelephoneDetailListAdapter;
import com.zhd.mswcs.moduls.setting.bean.SendTelephoneListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义发送号码详情弹出框
 */

public class ShowTelephoneDetailDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private Button btn_confirm;
    private RecyclerView recycler_telephone_detail;
    private List<SendTelephoneListBean> dataList = new ArrayList<>();
    private SOSTelephoneDetailListAdapter adapter;
    private static final String TAG = ShowTelephoneDetailDialog.class.getSimpleName();
    private ConfirmClickListener confirmClickListener;

    public void setConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface ConfirmClickListener{
        void click(String ipAddress);
    }



    public ShowTelephoneDetailDialog(Context context, List<SendTelephoneListBean> telephone){
        this.mContext = context;
        initDialog();
        initView(telephone);
    }

    private void initDialog() {
        if (commonDialog == null) {
            commonDialog = new Dialog(mContext, R.style.MyDialogStyleBottom);
            commonDialog.setCancelable(true);
            commonDialog.setCanceledOnTouchOutside(true);
            commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            commonDialog.setContentView(R.layout.layout_show_tel_detail);
            Window window = commonDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            WindowManager manager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels-100;
            lp.height = dm.heightPixels/2;
            window.setAttributes(lp);
        }
    }


    private void initView( List<SendTelephoneListBean> telephone) {
        btn_confirm = (Button) commonDialog.findViewById(R.id.btn_confirm);
        recycler_telephone_detail = (RecyclerView) commonDialog.findViewById(R.id.recycler_telephone_detail);
        btn_confirm.setOnClickListener(this);
        dataList.addAll(telephone);
        adapter = new SOSTelephoneDetailListAdapter(mContext, dataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycler_telephone_detail.setLayoutManager(linearLayoutManager);
        recycler_telephone_detail.addItemDecoration(new SimpleDividerDecoration(mContext));
        recycler_telephone_detail.setAdapter(adapter);
        recycler_telephone_detail.setOnScrollListener(onScrollListener);
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
              dismissDialog();
              break;

          default:
              break;
      }
    }



    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

}
