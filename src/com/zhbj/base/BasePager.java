package com.zhbj.base;

import com.example.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhbj.activity.MainActivity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author PC-LMOH 填充ViewPager，获取view的基类
 */
public class BasePager {
	public Activity mActivity;
	public View mRootView; // 类回传的view
	public TextView tvTitle;
	public ImageButton btnMenu;
	public FrameLayout flContent;//
	public ImageButton btnPhoto;//组图切换按钮
	public BasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	public void initData() {

	}

	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
		btnMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		return view;
	}

	/**
	 * slidingMenu收起或展开
	 */
	public void toggle() {
		MainActivity mMainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mMainUI.getSlidingMenu();
		slidingMenu.toggle();
	}
}
