package com.zhbj.imple;

import com.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class GovaPager extends BasePager {

	public GovaPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("政务初始化啦...");

		// 要给帧布局填充布局对象
		TextView view = new TextView(mActivity);
		view.setText("政务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// 修改页面标题
		tvTitle.setText("人口管理");

	}
}
