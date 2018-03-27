package com.zhd.mswcs.common.weidgt;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.common.weidgt.datepicker.CustomDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhanghanjun on 2017/9/26.
 * 自定义查找好友轨迹弹出框
 */

public class SelectTrajectoryDialog implements View.OnClickListener{
    private Context mContext;
    private Dialog commonDialog;
    private String startTime,endTime;
    private CustomDatePicker startTimePicker,endTimePicker;
    private LinearLayout ll_starttime;
    private TextView tv_starttime;
    private LinearLayout ll_endtime;
    private TextView tv_endtime;
    private Button btn_cancel;
    private Button btn_confirm;
    private RadioGroup rg_select;
    private static final String TAG = SelectTrajectoryDialog.class.getSimpleName();
    private SelectCallBackListener selectCallBackListener;
    private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    public void setSelectCallBackListener(SelectCallBackListener selectCallBackListener) {
        this.selectCallBackListener = selectCallBackListener;
    }

    public interface SelectCallBackListener{
        void callBack(String startTime,String endTime);
    }

    public SelectTrajectoryDialog(Context context){
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
            commonDialog.setContentView(R.layout.dialog_select_date);
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
        rg_select = (RadioGroup) commonDialog.findViewById(R.id.rg_select);
        btn_cancel = (Button) commonDialog.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) commonDialog.findViewById(R.id.btn_confirm);
        ll_starttime  = (LinearLayout) commonDialog.findViewById(R.id.ll_starttime);
        ll_endtime  = (LinearLayout) commonDialog.findViewById(R.id.ll_endtime);
        tv_starttime  = (TextView) commonDialog.findViewById(R.id.tv_starttime);
        tv_endtime  = (TextView) commonDialog.findViewById(R.id.tv_endtime);
        initStartTimePicker();
        initEndTimePicker();
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        ll_starttime.setOnClickListener(this);
        ll_endtime.setOnClickListener(this);
        rg_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_current_day:
                        startTime = sdf.format(new Date());
                        startTime = startTime.split(" ")[0]+" "+"00:00";
                        endTime = sdf.format(new Date());
                        dismissDialog();
                        if(selectCallBackListener!=null){
                            selectCallBackListener.callBack(startTime,endTime);
                        }
                        break;
                    case R.id.btn_before_week:
                        startTime = sdf.format(new Date());
                        String dateStr =  startTime.split(" ")[0];
                        String timeStr = startTime.split(" ")[1];
                        String year = dateStr.split("-")[0];
                        String month = dateStr.split("-")[1];
                        int day = Integer.parseInt(dateStr.split("-")[2])-7;
                        if(day>=10){
                            startTime = year+"-"+month+"-" + day+" "+timeStr;
                        }else{
                            startTime = year+"-"+month + "-"+ "0"+day +" "+timeStr;
                        }
                        endTime = sdf.format(new Date());
                        dismissDialog();
                        if(selectCallBackListener!=null){
                            selectCallBackListener.callBack(startTime,endTime);
                        }
                        break;
                    case R.id.btn_before_month:
                        startTime = sdf.format(new Date());
                        String dateStr2 =  startTime.split(" ")[0];
                        String timeStr2 = startTime.split(" ")[1];
                        String year2 = dateStr2.split("-")[0];
                        int month2 = Integer.parseInt(dateStr2.split("-")[1])-1;
                        String day2 = dateStr2.split("-")[2];
                        if(month2>=10){
                            startTime = year2+"-"+month2+"-" + day2+" "+timeStr2;
                        }else{
                            startTime = year2+"-"+ "0"+month2 + "-"+day2 +" "+timeStr2;
                        }
                        endTime = sdf.format(new Date());
                        dismissDialog();
                        if(selectCallBackListener!=null){
                            selectCallBackListener.callBack(startTime,endTime);
                        }
                        break;
                    default:
                        break;
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
          case R.id.ll_starttime:
              // 日期格式为yyyy-MM-dd HH:mm
              startTimePicker.show(startTime);
              break;

          case R.id.ll_endtime:
              // 日期格式为yyyy-MM-dd HH:mm
              endTimePicker.show(endTime);
              break;

          case R.id.btn_cancel:
              dismissDialog();
              break;

          case R.id.btn_confirm:
              Date date = null;
              try {
                 date = sdf.parse(startTime);
              } catch (ParseException e) {
                  e.printStackTrace();
              }
              long sTime = date.getTime();
              long currentTime = System.currentTimeMillis();
              long s = (currentTime - sTime) / (1000*60*60*24);
              if(s>31){//是否大于一个月
                  ToastUtil.showToast(mContext,"轨迹查询时间不能大于一个月");
                  return;
              }
              dismissDialog();
              if(selectCallBackListener!=null){
                  selectCallBackListener.callBack(startTime,endTime);
              }
              break;

          default:
              break;
      }
    }



    private void initStartTimePicker() {
        startTime = sdf.format(new Date());
        //设置当前显示的时间
        tv_starttime.setText(startTime);
        startTimePicker = new CustomDatePicker(mContext, "开始时间", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_starttime.setText(time);
                startTime = time;
            }
        }, "2007-01-01 00:00", "2027-12-31 23:59");
        startTimePicker.showSpecificTime(true);
        startTimePicker.setIsLoop(true);
    }



    private void initEndTimePicker() {
        endTime = sdf.format(new Date());
        //设置当前显示的时间
        tv_endtime.setText(endTime);
        endTimePicker = new CustomDatePicker(mContext, "结束时间", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_endtime.setText(time);
                endTime = time;
            }
        }, "2007-01-01 00:00", "2027-12-31 23:59");
        endTimePicker.showSpecificTime(true);
        endTimePicker.setIsLoop(true);
    }






}
