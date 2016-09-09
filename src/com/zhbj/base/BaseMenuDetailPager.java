package com.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;

/**新闻页详情页基类
 * @author PC-LMOH
 *
 */
public abstract class BaseMenuDetailPager {
	public Activity mActivity;
	public View mRootView;// 菜单详情页根布局
	public ImageButton btnPhoto;//组图切换按钮
	public BaseMenuDetailPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
		
	}
	// 初始化布局,必须子类实现
	public abstract View initView();
	
	public void initData(){
		
	}
}
