package com.zhbj.imple;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhbj.activity.MainActivity;
import com.zhbj.base.BaseMenuDetailPager;
import com.zhbj.base.BasePager;
import com.zhbj.domain.NewsMenu;
import com.zhbj.fragment.LeftMenuFragment;
import com.zhbj.global.GlobalConstants;
import com.zhbj.imple.newsdetail.InteractDetailPager;
import com.zhbj.imple.newsdetail.NewsCenterDetailPager;
import com.zhbj.imple.newsdetail.PhotoDetailPager;
import com.zhbj.imple.newsdetail.TopicDetailPager;
import com.zhbj.utils.CacheUtils;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewsPager extends BasePager {

	public NewsMenu mNewsMenu;
	public ArrayList<BaseMenuDetailPager> mMenuDetailPager;

	public NewsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("新闻初始化啦....");

		/*
		 * // 添加控件至布局 TextView view = new TextView(mActivity);
		 * view.setText("新闻中心"); view.setTextColor(Color.RED);
		 * view.setTextSize(22); view.setGravity(Gravity.CENTER);
		 * 
		 * flContent.addView(view);
		 */

		tvTitle.setText("新闻");

		// 判断本地是否缓存了json数据，有的先从本地获取数据解析，再请求网络更新解析
		String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("有缓存啦....");
			processData(cache);
		}
		// 再从服务器请求更新的json数据
		getDataFromServer();
	}

	/**
	 * 从服务器获取数据 需要权限:<uses-permission android:name="android.permission.INTERNET"
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		// 由于请求回来的数据是json字符串，所以String
		httpUtils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// System.out.println("加载服务器返回数据" + result);
				// 解析数据
				processData(result);
				// 缓存json数据
				CacheUtils.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
			}
		});
	}

	/**
	 * 解析JSON数据： 使用Gson解析时,对象书写技巧: 1. 逢{}创建对象, 逢[]创建集合(ArrayList) 2.
	 * 所有字段名称要和json返回字段高度一致
	 * 
	 * @param result
	 */
	public void processData(String json) {
		Gson gson = new Gson();
		mNewsMenu = gson.fromJson(json, NewsMenu.class);
		// System.out.println("解析后数据" + mNewsMenu);
		// 传递侧边栏数据对象到LeftMenuFragment
		// 1.获取MainActivity对象，2调用其方法获取fragment对象,3，调用fragment对象，传递数据
		MainActivity mMainUI = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mMainUI.getLeftMenuFragment();
		// 需要的数据集合
		leftMenuFragment.setMenuData(mNewsMenu.data);

		// 初始化新闻详情页至集合
		mMenuDetailPager = new ArrayList<BaseMenuDetailPager>();
		// 添加数据,初始化各个详情页
		// 传递数据给新闻详情页的页签使用
		mMenuDetailPager.add(new NewsCenterDetailPager(mActivity, mNewsMenu.data.get(0).children));
		mMenuDetailPager.add(new TopicDetailPager(mActivity));
		mMenuDetailPager.add(new PhotoDetailPager(mActivity, btnPhoto));
		mMenuDetailPager.add(new InteractDetailPager(mActivity));

		// 默认设置当前详情页为第一页
		setCurrentDetailPager(0);
	}

	/**
	 * 设置当前详情页
	 * 
	 * @param position侧边栏点击item的position
	 */
	public void setCurrentDetailPager(int position) {
		// 给所在布局添加内容
		BaseMenuDetailPager pager = mMenuDetailPager.get(position);
		View rootView = pager.mRootView;
		// 清除旧布局
		flContent.removeAllViews();
		// 添加新布局
		flContent.addView(rootView);
		// 初始化详情页数据
		pager.initData();
		// 更新标题
		tvTitle.setText(mNewsMenu.data.get(position).title);
		
		// 如果是组图页面, 需要显示切换按钮
				if (pager instanceof PhotoDetailPager) {
					btnPhoto.setVisibility(View.VISIBLE);
				} else {
					// 隐藏切换按钮
					btnPhoto.setVisibility(View.GONE);
				}
	}
}
