package com.zhbj.imple.newsdetail;

import com.zhbj.base.BaseMenuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**新闻中心详情页-专题
 * @author PC-LMOH
 *
 */
public class TopicDetailPager extends BaseMenuDetailPager {

	public TopicDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		System.out.println("新闻中心详情页-专题初始化啦....");

		// 初始化布局
		TextView view = new TextView(mActivity);
		view.setText("新闻中心详情页-专题");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		return view;
	}

}
