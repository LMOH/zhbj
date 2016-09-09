package com.zhbj.imple.newsdetail;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.zhbj.activity.MainActivity;
import com.zhbj.base.BaseMenuDetailPager;
import com.zhbj.domain.NewsMenu.NewsTabData;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

/**新闻中心详情页-新闻
 * @author PC-LMOH
 *ViewPagerIndictor使用步骤：
 *1.引入库
 *2.解决support-v4冲突（让两个版本一致）
 *3.模仿例子程序布局
 *4.模仿代码（指示器indicator与viewpager在绑定适配器后绑定，在adapter重写getPageTitle返回标题）
 *5.在清单文件增加样式
 *6.背景修改颜色
 *7.修改样式-根据实际所需：背景，文字
 */
public class NewsCenterDetailPager extends BaseMenuDetailPager {
	
	@ViewInject(R.id.vp_news_detail)
	private ViewPager mDetailViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mTabPageIndicator;
	/**
	 * 页签页面对象集合
	 */
	private ArrayList<TabDetailPager> mTabDetailPagerList;	//页签页面集合
	/**
	 * 页签数据对象集合
	 */
	private ArrayList<NewsTabData> mNewsTabDataList;		//页签数据对象集合
	
	public NewsCenterDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mNewsTabDataList = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_newcenter_detail, null);
		ViewUtils.inject(this, view);
		
		return view;
	}
	
	@Override
	public void initData() {
		//初始化页签
		mTabDetailPagerList = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mNewsTabDataList.size(); i++) {
			TabDetailPager tabDetailPager = new TabDetailPager(mActivity,mNewsTabDataList.get(i));
			mTabDetailPagerList.add(tabDetailPager);
		}
		mDetailViewPager.setAdapter(new DetailAdapter());
		mTabPageIndicator.setViewPager(mDetailViewPager);
		//给indicator设置viewpager监听，设置侧边栏的状态
		mTabPageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//判断当前viewpager页数
				if (position == 0) {
					//开启侧边栏
					setLeftMenuEnable(true);
				}else{
					//关闭侧边栏
					setLeftMenuEnable(false);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
	}
	
	/****************************************************** */
	class DetailAdapter extends PagerAdapter{
		
		//返回标题给indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsTabDataList.get(position).title;
		}

		@Override
		public int getCount() {
			return mTabDetailPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mTabDetailPagerList.get(position);

			View view = pager.mRootView;
			container.addView(view);

			pager.initData();

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	/**
	 * 设置侧边栏开启或关闭
	 * 
	 * @param enable :false屏蔽，true开启
	 */
	public void setLeftMenuEnable(boolean enable) {
		MainActivity mMainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mMainUI.getSlidingMenu();
		if (enable) {
			// 开启侧边栏
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
		} else {
			// 关闭侧边栏
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 触摸无效
		}
	}
	
	/**用注解方式设置按键监听
	 * @param view
	 */
	@OnClick(R.id.btn_next_page)
	public void nextPage(View view){
		int currentItem = mDetailViewPager.getCurrentItem();
		currentItem++;
		mDetailViewPager.setCurrentItem(currentItem);
	}
}
