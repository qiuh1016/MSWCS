package com.zhd.mvp.framework.support.delegate.viewgroup;

import android.os.Parcelable;

import com.zhd.mvp.framework.base.presenter.MvpPresenter;
import com.zhd.mvp.framework.base.view.MvpView;

/**
 * 以下代理是针对布局的代理
 * 
 * 目标接口
 * 
 * @author Dream
 *
 * @param <V>
 * @param <P>
 */
public interface ViewGroupMvpDelegate<V extends MvpView, P extends MvpPresenter<V>> {

	/**
	 * 关联布局
	 * 
	 */
	public void onAttachedToWindow();

	/**
	 * 解除关联
	 */
	public void onDetachedFromWindow();

	/**
	 * 保存布局实例状态（这里是指布局相关数据）
	 * 
	 * @return
	 */
	public Parcelable onSaveInstanceState();

	/**
	 * 恢复布局实例状态
	 * 
	 * @param state
	 */
	public void onRestoreInstanceState(Parcelable state);
}
