package com.zhbj.activity;

import com.example.zhbj.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhbj.fragment.ContentFragment;
import com.zhbj.fragment.LeftMenuFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

/**
 * slidingmenu
 */
public class MainActivity extends SlidingFragmentActivity {
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//主页面，侧边栏布局被fragment替换
		setContentView(R.layout.activity_main);

		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
		//预留宽度也别写死200，应该写成屏幕宽度*200/320
		WindowManager wm = getWindowManager();
		int screenWidth = wm.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(screenWidth*200/320);// 屏幕预留200像素宽度

		// 初始化Fragment
		initFragment();
	}

	/**
	 * 初始化Fragment
	 */
	public void initFragment() {
		FragmentManager fm = getSupportFragmentManager(); // 获取管理器
		FragmentTransaction transaction = fm.beginTransaction(); // 开始事务
		//用Fragment替换容器布局(布局ID，Fragment对象，标志)
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),TAG_LEFT_MENU); 
		transaction.replace(R.id.fl_main, new ContentFragment(),TAG_CONTENT); 
		transaction.commit();
	}


	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager(); // 获取管理器
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
		return leftMenuFragment;
	}
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager(); // 获取管理器
		ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
		return contentFragment;
	}
}
