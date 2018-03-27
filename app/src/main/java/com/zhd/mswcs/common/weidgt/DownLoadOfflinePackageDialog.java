package com.zhd.mswcs.common.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.moduls.location.bean.CityBean;

import java.util.ArrayList;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义离线数据包下载框
 */

public class DownLoadOfflinePackageDialog implements View.OnClickListener,MKOfflineMapListener {
    private Context mContext;
    private Dialog commonDialog;
    private ProgressBar progress_bar;
    private TextView tv_data_size;
    private TextView tv_radio;
    private TextView tv_update_package;
    private TextView tv_title;
    private MKOfflineMap mOffline = null;
    private ArrayList<CityBean> allCities = new ArrayList<>();
    private int downloadType = 1;//1、下载地图离线数据包2、更新地图离线数据包
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = new ArrayList<>();
    private static final String TAG = DownLoadOfflinePackageDialog.class.getSimpleName();

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    if(update.ratio!=100){
                        if(downloadType==1){
                            tv_radio.setText("已下载:"+String.format("%d%%",update.ratio));
                        }else{
                            tv_radio.setText("已更新:"+String.format("%d%%",update.ratio));
                        }
                        progress_bar.setMax(100);
                        progress_bar.setProgress(update.ratio);
                    }else{
                        if(downloadType==1){
                            ToastUtil.showToast(mContext,"离线数据包下载完成");
                        }else{
                            ToastUtil.showToast(mContext,"离线数据包更新完成");
                        }
                        dismissDialog();
                    }

                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);

                break;
            default:
                break;
        }

    }


    public DownLoadOfflinePackageDialog(Context context){
        this.mContext = context;
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        initDialog();
        initView();
    }

    private void initDialog() {
        if (commonDialog == null) {
            commonDialog = new Dialog(mContext, R.style.MyDialogStyleBottom);
            commonDialog.setCancelable(false);
            commonDialog.setCanceledOnTouchOutside(false);
            commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            commonDialog.setContentView(R.layout.dialog_download_offline_package);
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
        progress_bar = (ProgressBar) commonDialog.findViewById(R.id.progress_bar);
        tv_radio = (TextView) commonDialog.findViewById(R.id.tv_radio);
        tv_data_size = (TextView) commonDialog.findViewById(R.id.tv_data_size);
        tv_update_package = (TextView) commonDialog.findViewById(R.id.tv_update_package);
        tv_title = (TextView) commonDialog.findViewById(R.id.tv_title);
        tv_update_package.setOnClickListener(this);
        // 获取所有支持离线地图的城市
        ArrayList<MKOLSearchRecord> records = mOffline.getOfflineCityList();
        if (records != null) {
            for (MKOLSearchRecord record : records) {
                //V4.5.0起，保证数据不溢出，使用long型保存数据包大小结果
                allCities.add(new CityBean(record.cityID,record.cityName,formatDataSize(record.dataSize)));
            }
        }
        tv_data_size.setText(allCities.get(0).dataSize);
        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList != null&&localMapList.size()>0) {
             for(MKOLUpdateElement element:localMapList){
                 if(element.cityID==1){
                     if(element.update){
                         showDialog();
                         downloadType = 2;
                         tv_title.setText("更新离线地图数据包");
                         tv_update_package.setVisibility(View.VISIBLE);
                     }else{
                         Log.e(TAG,"已是最新离线数据包");
                         tv_update_package.setVisibility(View.GONE);
                     }
                 }
             }
        }else{
            showDialog();
            downloadType = 1;
            tv_title.setText("下载离线地图数据包");
            mOffline.start(allCities.get(0).cityId);
        }
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
          case R.id.tv_update_package:
              mOffline.remove(allCities.get(0).cityId);
              mOffline.start(allCities.get(0).cityId);
              break;

          default:
              break;
      }
    }



    /**
     * V4.5.0起，保证数据不溢出，使用long型保存数据包大小结果
     */
    public String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

}
