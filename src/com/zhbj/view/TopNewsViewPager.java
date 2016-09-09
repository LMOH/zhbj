package com.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context) {
		super(context);
	}

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 1. 上下滑动需要拦截 2. 向右滑动并且当前是第一个页面,需要拦截 3. 向左滑动并且当前是最后一个页面,需要拦截
	 */
	// 重写方法，父控件根据要求拦截或不拦截该子控件事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		// 1.刚进页面不拦截，子控件自己能处理事件
		getParent().requestDisallowInterceptTouchEvent(true);
		// 2.根据滑动及滑动位置判断拦截不拦截
		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();

			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			// 计算
			int dx = endX - startX;
			int dy = endY - startY;
			if (Math.abs(dy) < Math.abs(dx)) {
				int currentItem = getCurrentItem();
				// 左右滑动
				if (dx > 0) {
					// 向右划
					if (currentItem == 0) {
						// 第一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 向左划
					int count = getAdapter().getCount();// item总数
					if (currentItem == count - 1) {
						// 最后一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				// 上下滑动,需要拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
