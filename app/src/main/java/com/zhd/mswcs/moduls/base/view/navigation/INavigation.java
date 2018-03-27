package com.zhd.mswcs.moduls.base.view.navigation;

/**
 * 定义导航条的规范
 *
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */
public interface INavigation {

    //绑定布局ID
    public int bindLayoutId();

    //创建布局并且绑定布局到UI主界面
    public void builder();

}
