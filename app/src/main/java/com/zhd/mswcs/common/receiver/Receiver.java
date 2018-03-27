package com.zhd.mswcs.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhd.mswcs.MyApplication;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.StringUtils;
import com.zhd.mswcs.constant.Constant;

/**
 * Created by think-1 on 2017/11/29.
 */

//这是我们创建的广播接收器，他先得继承BroadcastReceiver父类，然后复写其onReceive(这里不带r)方法。
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        //onReceive方法的内容就是接收了广播之后的处理方法。
        Log.d("msg","onReceiver");
        if(StringUtils.equals(intent.getAction(), Constant.HAS_NETWORK_BRO)){
            MyApplication.getInstance().setImageRes(R.mipmap.img_has_wifi);
        }else if(StringUtils.equals(intent.getAction(),Constant.NO_NETWORK_BRO)){
            MyApplication.getInstance().setImageRes(R.mipmap.img_no_wifi);
        }
    }
}