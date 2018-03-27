package com.zhd.mswcs.moduls.launch;

import android.view.View;

/**
 * 策略接口
 *
 * 作者: Dream on 16/9/22 21:38
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public interface ILaunch {

    //策略方法(具体的算法结构)
    void delay(View target,Callback callback);

    interface Callback{
        void call();
    }

}
