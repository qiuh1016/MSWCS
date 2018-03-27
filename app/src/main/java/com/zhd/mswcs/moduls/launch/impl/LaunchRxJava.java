package com.zhd.mswcs.moduls.launch.impl;

import android.view.View;

import com.zhd.mswcs.moduls.launch.ILaunch;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 具体的策略(延时启动主页)
 * 模拟网络请求(读取数据库、操作文件、访问网络等等......IO操作)
 *
 * 作者: Dream on 16/9/22 21:41
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class LaunchRxJava implements ILaunch {

    @Override
    public void delay(View target,final Callback callback) {
        Observable.just("").timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        callback.call();
                    }
                });
    }

}
