package com.zhbj.fragment;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhbj.activity.MainActivity;
import com.zhbj.base.BasePager;
import com.zhbj.imple.GovaPager;
import com.zhbj.imple.HomePager;
import com.zhbj.imple.NewsPager;
import com.zhbj.imple.SettingPager;
import com.zhbj.imple.SmartPager;
import com.zhbj.view.NoScrollViewPager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	private NoScrollViewPager vp_content;
	private ArrayList<BasePager> mPagers;
	private RadioGroup rg_group;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}

	@Override
	public void initData() {
		mPagers = new ArrayList<BasePager>();
		// 添加五个标签页
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsPager(mActivity));
		mPagers.add(new SmartPager(mActivity));
		mPagers.add(new GovaPager(mActivity));
		mPagers.add(new SettingPager(mActivity));
		// 设置适配器
		vp_content.setAdapter(new ContentAdapter());

		// 监听rg_group选中状态，切换viewpager显示画面,屏蔽平滑滚动动画
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					vp_content.setCurrentItem(0, false);
					break;
				case R.id.rb_news:
					vp_content.setCurrentItem(1, false);
					break;
				case R.id.rb_smart:
					vp_content.setCurrentItem(2, false);
					break;
				case R.id.rb_gova:
					vp_content.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					vp_content.setCurrentItem(4, false);
					break;
				default:
					break;
				}
			}
		});
		// 设置viewpager页面切换监听，优化加载数据
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 当页面选中，才加载数据
				BasePager pager = mPagers.get(position);
				pager.initData();
				// 判断首页，尾页，屏蔽侧边栏
				if (position == 0 || position == mPagers.size() - 1) {
					setLeftMenuEnable(false);
				} else {
					setLeftMenuEnable(true);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		// 监听viewpager页面切换，注意刚进来首页不处于切换结果
		mPagers.get(0).initData(); // 手动首页加载数据
		setLeftMenuEnable(false);
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

	/******************* ViewPager适配器 *****************************/
	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagers.get(position);
			View view = pager.mRootView;// 获取当前页面对象的布局

			// pager.initData();// 初始化数据, viewpager会默认加载下一个页面,
			// 为了节省流量和性能,不要在此处调用初始化数据的方法

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	/**获取新闻首页
	 * @return
	 */
	public NewsPager getNewsPager() {
		NewsPager newsPager = (NewsPager) mPagers.get(1);
		return newsPager;
	}
}
