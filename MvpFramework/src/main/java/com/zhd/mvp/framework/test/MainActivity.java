package com.zhd.mvp.framework.test;

import android.os.Bundle;

import com.zhd.architect.mvpframework.R;
import com.zhd.mvp.framework.support.view.MvpActivity;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getPresenter().register();
	}

	@Override
	public MainPresenter createPresenter() {
		return new MainPresenter(this);
	}

	@Override
	public void onLoginResult(String result) {
		getMvpView().onLoginResult("");
	}

	@Override
	public void onRegisterResult(String result) {
		
	}

}
