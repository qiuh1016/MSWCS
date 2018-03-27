package com.zhd.mswcs.moduls.tab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TabHost;
import com.zhd.mswcs.R;
import com.zhd.mswcs.common.utils.ToastUtil;
import com.zhd.mswcs.moduls.tab.iterator.ITabItem;
import com.zhd.mswcs.moduls.tab.iterator.TabIterator;
import com.zhd.mswcs.views.DreamFragmentTabHost;

/**
 * 作者: Dream on 16/9/22 23:29
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public abstract class BaseTabActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{

    private DreamFragmentTabHost fragmentTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabHostRes();
        initFragmentTabHost();
    }

    //第四步:采用OO设计,定义Tab对象
    //第五步:初始化FragmentTabHost
    private void initFragmentTabHost() {
        fragmentTabHost = (DreamFragmentTabHost) findViewById(android.R.id.tabhost);
        //绑定TabHost到FragmentManager
        //为什么要绑定?---因为FragmentTabHost要管理这个Tab对应的Fragment(原来这个事情是我们自己搞)
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        //去掉Tab之间的分割线
        fragmentTabHost.getTabWidget().setDividerDrawable(null);

        TabIterator<ITabItem> iterator = getTabIterator();

        int i = 0;

        while (iterator.hasNext()){
            ITabItem tabItem = iterator.next();
            View indicator = tabItem.builder();
            //创建一个Tab
            //newTabSpec:构造参数---传入Tab名字
            //setIndicator:指定Tab的样式
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(tabItem.getTabString()).setIndicator(indicator);
            //绑定Fragment
            //tabSpec参数:指定Tab
            //clss参数:指定tab对应的Fragment
            //args参数:给我们Fragment绑定参数
            fragmentTabHost.addTab(tabSpec, tabItem.getFragmentClazz(), null);
            //给我们的FragmentTabHost设置背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.main_tab_bg));

            //给我们的Tab绑定监听
            //通过源码分析:FragmentTabHost对点击Tab事件做了拦截,目的为了切换我们的Fragment
            fragmentTabHost.setOnTabChangedListener(this);

            if (i == 0) {
                //默认选中第一个Tab
                tabItem.setChecked(true);
            }
            i++;
        }

    }

    @Override
    public void onTabChanged(String tabId) {
        if (TextUtils.isEmpty(tabId)) {
            //弹出PopupWindow
            ToastUtil.showToast(this, "我有一个梦想,梦想找到了心意中妹子");
            //PopupWindow弹窗
            //VIP企业级项目开发---项目
            return;
        }
        TabIterator<ITabItem> iterator = getTabIterator();
        while (iterator.hasNext()){
            ITabItem tabItem = iterator.next();
            if (tabId.equals(tabItem.getTabString())){
                tabItem.setChecked(true);
            }else{
                tabItem.setChecked(false);
            }
        }
    }

    public abstract TabIterator<ITabItem> getTabIterator();

    public abstract void initTabHostRes();

}
