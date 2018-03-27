package com.zhd.mswcs.moduls.tab.iterator.tabiterator;

import com.zhd.mswcs.moduls.tab.iterator.ITabItem;
import com.zhd.mswcs.moduls.tab.iterator.TabIterable;
import com.zhd.mswcs.moduls.tab.iterator.TabIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Dream on 16/8/16 17:27
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class ListTabIterable implements TabIterable<ITabItem> {

    private List<ITabItem> tabItemList;

    public ListTabIterable(){
        this.tabItemList = new ArrayList<ITabItem>();
    }

    @Override
    public TabIterator<ITabItem> iterator() {
        return new ListTabIterator(tabItemList);
    }

    public void addTab(ITabItem tabItem){
        if (this.tabItemList.contains(tabItem)){
            this.tabItemList.remove(tabItem);
        }
        this.tabItemList.add(tabItem);
    }

    public void remove(ITabItem tabItem){
        this.tabItemList.remove(tabItem);
    }

    public class ListTabIterator implements TabIterator<ITabItem> {

        private List<ITabItem> tabItemList;
        private int index = 0;

        public ListTabIterator(List<ITabItem> tabItemList){
            this.tabItemList = tabItemList;
        }

        @Override
        public boolean hasNext() {
            if (tabItemList == null){
                return false;
            }
            return index < tabItemList.size();
        }

        @Override
        public ITabItem next() {
            return this.tabItemList.get(index++);
        }

        @Override
        public void remove() {
            if (index < tabItemList.size()){
                this.tabItemList.remove(index);
            }
        }
    }

}
