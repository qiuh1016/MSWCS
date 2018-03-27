package com.zhd.mswcs.moduls.base.presenter;

import android.content.Context;
import com.zhd.mvp.framework.base.presenter.impl.MvpBasePresenter;
import com.zhd.mvp.framework.base.view.MvpView;

/**
 * 作者: zhj
 * QQ:1178075841
 * E-mail:1178075841@qq.com
 */

public abstract class BasePresenter<V extends MvpView> extends MvpBasePresenter<V> {

    public BasePresenter(Context context) {
        super(context);
    }

}
