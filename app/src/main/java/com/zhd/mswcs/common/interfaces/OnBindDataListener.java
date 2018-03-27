package com.zhd.mswcs.common.interfaces;

/**
 * Created by zhanghanjun on 2017/9/4.
 */

public interface OnBindDataListener<T> {
    void bindData(T data, int requestCode);
}
