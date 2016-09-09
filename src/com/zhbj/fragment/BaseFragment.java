package com.zhbj.fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhbj.activity.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{
	
	public Activity mActivity;

	//Fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}
	
	//初始化Fragment布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = initView();
		return view;
	}
	
	//初始化数据，Fragment所依赖的activity的onCreate方法执行结束
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		 initData();
		super.onActivityCreated(savedInstanceState);
	}
	//抽象方法，必须由子类实现
	public abstract View initView();
	public abstract void initData();
	
}
