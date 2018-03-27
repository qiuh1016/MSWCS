package com.zhd.mswcs.moduls.launch;

import android.view.View;

/**
 * 策略上下文
 *
 * 作者: Dream on 16/9/22 21:43
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class LaunchContext {

    private ILaunch launch;
    private View target;

    public LaunchContext(ILaunch launch, View target){
        this.target = target;
        this.launch = launch;
    }

    public void launch(ILaunch.Callback callback){
        this.launch.delay(target,callback);
    }

}
