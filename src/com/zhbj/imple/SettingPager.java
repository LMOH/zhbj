package com.zhbj.imple;

import com.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("设置初始化啦...");

		// Ҫ��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("设置");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// �޸�ҳ�����
		tvTitle.setText("设置");

		// ���ز˵���ť
		btnMenu.setVisibility(View.GONE);
	}
}
