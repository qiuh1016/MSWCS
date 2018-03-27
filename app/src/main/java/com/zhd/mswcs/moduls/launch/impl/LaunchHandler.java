package com.zhd.mswcs.moduls.launch.impl;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zhd.mswcs.moduls.launch.ILaunch;

/**
 * 具体的策略(专门用来执行过度动画)
 *
 * 作者: Dream on 16/9/22 21:41
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class LaunchHandler implements ILaunch {



    @Override
    public void delay(View target,final Callback callback) {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.call();
            }
        };

        new Thread(){
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
