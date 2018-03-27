package com.zhd.mswcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhd.mswcs.R;
import com.zhd.mswcs.config.SharedPreferencesConfig;
import com.zhd.mswcs.moduls.launch.ILaunch;
import com.zhd.mswcs.moduls.launch.LaunchContext;
import com.zhd.mswcs.moduls.launch.impl.LaunchRxJava;
import com.zhd.mswcs.moduls.user.view.LoginActivity;

/**
 * 作者:zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        startMain();
    }

    //需求: 启动一个过度动画(演示加载数据过程,下载过程)
    //动画时长:1-3秒钟左右
    //需求增强:我希望过度采用策略?
    //load数据、检查更新、下载更新启动页
    //策略模式
    //角色:策略接口、上下文、具体的策略
    private void startMain(){
        View target = findViewById(R.id.iv_launch);
        LaunchContext launchContext = new LaunchContext(new LaunchRxJava(),target);
        launchContext.launch(new ILaunch.Callback() {
            @Override
            public void call() {
                if (SharedPreferencesConfig.isShowGuide(LaunchActivity.this)){
                    startActivity(new Intent(LaunchActivity.this,GuideActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(LaunchActivity.this,HomeActivity.class));
                    finish();
                }
            }
        });

    }


}