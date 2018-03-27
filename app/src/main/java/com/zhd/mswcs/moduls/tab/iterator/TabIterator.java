package com.zhd.mswcs.moduls.tab.iterator;

/**
 * 作者: Dream on 16/8/16 16:12
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public interface TabIterator<T> {

    boolean hasNext();

    T next();

    void remove();

}
