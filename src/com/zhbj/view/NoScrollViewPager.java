package com.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author PC-LMOH 自定义ViewPager：
 * 1.无法滚动的
 * 2.不拦截子控件事件的
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}

	//事件拦截，直接返回false不拦截子控件事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	// 重写onTouchEvent返回，直接返回true消费。。。不做任何动作
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}

}
