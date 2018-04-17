package com.zhd.mvp.framework.support.delegate.fragment;

import com.zhd.mvp.framework.base.presenter.MvpPresenter;
import com.zhd.mvp.framework.base.view.MvpView;
import com.zhd.mvp.framework.support.delegate.MvpDelegateCallback;

/**
 * 扩展目标接口 针对不同的模块，目标接口有差异
 * 
 * @author Dream
 *
 */
public interface FragmentMvpDelegateCallback<V extends MvpView, P extends MvpPresenter<V>>
		extends MvpDelegateCallback<V, P> {
	
}
