package com.zhd.mswcs.moduls.launch.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import com.zhd.mswcs.moduls.launch.ILaunch;

/**
 * 具体的策略(专门用来执行过度动画)
 *
 * 作者: Dream on 16/9/22 21:41
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class LaunchAnimator implements ILaunch {

    @Override
    public void delay(View target,final Callback callback) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target,"alpha",0.0f,1.0f);
        //2秒钟
        alphaAnimator.setDuration(2000);
        alphaAnimator.start();
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //主线程
                callback.call();
            }
        });
    }

}
