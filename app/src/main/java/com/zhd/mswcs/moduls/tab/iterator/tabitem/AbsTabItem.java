package com.zhd.mswcs.moduls.tab.iterator.tabitem;

import android.view.View;

import com.zhd.mswcs.moduls.tab.iterator.ITabItem;

/**
 * 作者: Dream on 16/8/16 16:19
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public abstract class AbsTabItem<T extends AbsTabItem.TabItemParams> implements ITabItem{

    private T params;

    public AbsTabItem(T params){
        this.params = params;
    }

    public T getParams() {
        return params;
    }

    @Override
    public void setChecked(boolean isChecked) {

    }

    @Override
    public View builder() {
        return params.inflate(getParams().layoutId);
    }

}
