package com.zhd.mswcs.moduls.base.view.navigation.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhd.mswcs.moduls.base.view.navigation.INavigation;

/**
 * 添加默认的配置
  * 作者: zhj
   * QQ:1178075841
   * E-mail:1178075841@qq.com
 */
public abstract class AbsNavigation<P extends AbsNavigation.Builder.NavigationParams> implements INavigation {
    //定义抽象类: 因为这个导航条有很多默认的配置
    //我们通过抽象类定义默认的配置
    //汽车轮子(规范: 圆形,橡胶,尺寸等等.....)
    //汽车轮子:默认配置(模具)

    private P params;

    private View view;

    public AbsNavigation(P params){
        this.params = params;
    }

    public String getString(int id){
        return this.params.context.getResources().getString(id);
    }

    public int getColor(int id){
        return this.params.context.getResources().getColor(id);
    }

    public P getParams() {
        return params;
    }

    public View findViewById(int id){
        return view.findViewById(id);
    }

    @Override
    public void builder() {
        if (params == null){
            return;
        }
        //绑定布局
        view = LayoutInflater.from(params.context).inflate(bindLayoutId(),params.parent,false);
        //指定父容器
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null){
            //如果存在,那么我就干掉,重写添加,这样的方式我们就可以缓存视图
            parent.removeView(view);
        }
        params.parent.addView(view,0);
    }

    //构建导航条类---这类知识默认的配置---具体功能的实现,一定是具体的实现类决定(你这是UI框架)
    public abstract static class Builder{
        //构建导航条方法
        public abstract AbsNavigation build();

        public Builder(Context context, ViewGroup parent){
        }

        //默认配置参数
        public static class NavigationParams{
            public Context context;
            public ViewGroup parent;
            public NavigationParams(Context context, ViewGroup parent){
                this.context = context;
                this.parent = parent;
            }
        }
    }
}
