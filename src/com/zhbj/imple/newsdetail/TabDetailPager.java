package com.zhbj.imple.newsdetail;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.zhbj.activity.NewsDetailActivity;
import com.zhbj.base.BaseMenuDetailPager;
import com.zhbj.domain.NewsDetailBean;
import com.zhbj.domain.NewsDetailBean.NewsData;
import com.zhbj.domain.NewsDetailBean.TopNews;
import com.zhbj.domain.NewsMenu.NewsTabData;
import com.zhbj.global.GlobalConstants;
import com.zhbj.utils.CacheUtils;
import com.zhbj.utils.PrefUtils;
import com.zhbj.view.PullToRefreshListView;
import com.zhbj.view.PullToRefreshListView.OnRefreshListener;
import com.zhbj.view.TopNewsViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TabDetailPager extends BaseMenuDetailPager {

	private ArrayList<TopNews> mTopNewsList;// 头部数据集合
	private NewsTabData mNewsTabData;// 单个页签数据
	private String mUrl;

	@ViewInject(R.id.tv_top_news_title)
	private TextView tv_top_title;
	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager mTopViewPager;
	@ViewInject(R.id.circle_indicator)
	private CirclePageIndicator circle_indicator;
	@ViewInject(R.id.lv_news)
	private PullToRefreshListView mNewsListView;
	private ArrayList<NewsData> mNewsList;
	private NewsAdapter mNewsAdapter;
	private String moreUrl;
	private String mMoreUrl;
	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mNewsTabData = newsTabData;
		mUrl = GlobalConstants.SERVER_URL + newsTabData.url;
	}

	@Override
	public View initView() {
		/*
		 * view = new TextView(mActivity); //view.setText("新闻中心详情页-新闻页签");
		 * view.setTextColor(Color.RED); view.setTextSize(22);
		 * view.setGravity(Gravity.CENTER);
		 */
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		View headerView = View.inflate(mActivity, R.layout.list_news_header, null);
		ViewUtils.inject(this, view);
		mNewsListView.addHeaderView(headerView);
		ViewUtils.inject(this, headerView);

		// 5. 前端界面设置回调
		mNewsListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 刷新中，加载数据，通知适配器
				getDataFromServer();
				mNewsAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLoadMore() {
				if (!TextUtils.isEmpty(mMoreUrl)) {
					getMoreDataFromServer();
				} else {
					// 没有下一页
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
					// 没有数据时也要收起控件
					mNewsListView.refreshCompelete(true);
				}
			}
		});
		// 设置listview条目点击，存储读取状态，跳转页面

		mNewsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 已读灰色，未读黑色（在getview方法中设置记录的已读未读效果）
				int headerViewsCount = mNewsListView.getHeaderViewsCount();
				position = position - headerViewsCount;

				String readIds = PrefUtils.getString(mActivity, "read_ids", "");
				int newsId = mNewsList.get(position).id;
				if (!readIds.contains(newsId + "")) { // 若存的id字符串不包含该条目id
					readIds = readIds + newsId + ",";
					PrefUtils.setString(mActivity, "read_ids", readIds);

					// 要将被点击的item的文字颜色改为灰色, 局部刷新, view对象就是当前被点击的对
					TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
					tv_title.setTextColor(Color.GRAY);
				}
				// 跳转到新闻详情activity
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("newsUrl", mNewsList.get(position).url);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * 加载更多数据
	 */
	protected void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result, true);
				// 加载完成，lv恢复原状
				mNewsListView.refreshCompelete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
			}
		});
	}

	@Override
	public void initData() {
		// view.setText("新闻中心详情页-新闻页签:"+mNewsTabData.title);
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (cache != null) {
			processData(cache, false);
		}
		getDataFromServer();

	}

	/**
	 * 各个页签的网络数据请求
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result, false);
				CacheUtils.setCache(mUrl, result, mActivity);
				// 加载完成，lv恢复原状
				mNewsListView.refreshCompelete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
			}
		});
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	protected void processData(String result, boolean isMore) {
		Gson gson = new Gson();
		NewsDetailBean newsDetailBean = gson.fromJson(result, NewsDetailBean.class);
		// 设置头条新闻适配器
		// 获取头条新闻数据
		mTopNewsList = newsDetailBean.data.topnews;
		moreUrl = newsDetailBean.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
		} else {
			mMoreUrl = null;
			mNewsListView.refreshCompelete(true);
		}
		if (!isMore) {
			if (mTopNewsList != null) {
				// 若头条新闻数据不为空，设置适配器
				TopNewsAdapter topNewsAdapter = new TopNewsAdapter();
				mTopViewPager.setAdapter(topNewsAdapter);
				circle_indicator.setViewPager(mTopViewPager);
				circle_indicator.setSnap(true);
				circle_indicator.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						tv_top_title.setText(mTopNewsList.get(position).title);
					}

					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});
				tv_top_title.setText(mTopNewsList.get(0).title);// 默认第一个头条标题
				circle_indicator.onPageSelected(0); // 回到第一个是强制回到首个数据
			}
			// 获取新闻列表数据
			mNewsList = newsDetailBean.data.news;
			// 设置适配器
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				mNewsListView.setAdapter(mNewsAdapter);
			}
		} else {
			// 加载更多数据
			ArrayList<NewsData> moreNews = newsDetailBean.data.news;
			mNewsList.addAll(moreNews);// 将数据追加在原来的集合中
			// 刷新listview
			mNewsAdapter.notifyDataSetChanged();
		}

		// 设置广告轮播
		if (mHandler == null) {
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					int currentItem = mTopViewPager.getCurrentItem();
					currentItem++;
					if (currentItem > mTopNewsList.size() - 1) {
						currentItem = 0;
					}
					mTopViewPager.setCurrentItem(currentItem);
					mHandler.sendEmptyMessageDelayed(0, 3000);// 形成内循环
					super.handleMessage(msg);
				}
			};
		}

		mHandler.sendEmptyMessageDelayed(0, 3000);

		mTopViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//按下，停止轮播，删除消息
					mHandler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_CANCEL:
					mHandler.sendEmptyMessageDelayed(0, 3000);
					break;
				case MotionEvent.ACTION_UP:
					mHandler.sendEmptyMessageDelayed(0, 3000);
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	class TopNewsAdapter extends PagerAdapter {
		public BitmapUtils mBitmapUtils;

		// 构造方法初始化BitmapUtils对象
		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			// 使用BitmapUtils设置默认加载图片
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {

			return mTopNewsList.size();

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// TopNews topNews = new TopNews();
			ImageView imageView = new ImageView(mActivity);
			// imageView.setImageResource(R.drawable.topnews_item_default);
			imageView.setScaleType(ScaleType.FIT_XY);// 设置图片缩放方式, 宽高填充父控件
			String imageUrl = mTopNewsList.get(position).topimage;
			// 设置并缓存图片
			mBitmapUtils.display(imageView, imageUrl);
			container.addView(imageView);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class NewsAdapter extends BaseAdapter {
		public BitmapUtils mBitmapUtils;

		// 构造方法初始化BitmapUtils对象
		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			// 使用BitmapUtils设置默认加载图片
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viewHolder;
			if (convertView == null) {
				view = View.inflate(mActivity, R.layout.list_item_news, null);
				viewHolder = new ViewHolder();
				viewHolder.icon = (ImageView) view.findViewById(R.id.iv_icon);
				viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
				viewHolder.data = (TextView) view.findViewById(R.id.tv_date);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title.setText(mNewsList.get(position).title);
			viewHolder.data.setText(mNewsList.get(position).pubdate);
			mBitmapUtils.display(viewHolder.icon, mNewsList.get(position).listimage);

			// 设置存储的已读未读信息
			String readIds = PrefUtils.getString(mActivity, "read_ids", "");
			if (readIds.contains(mNewsList.get(position).id + "")) {
				viewHolder.title.setTextColor(Color.GRAY);
			} else {
				viewHolder.title.setTextColor(Color.BLACK);
			}
			return view;
		}

		class ViewHolder {
			public ImageView icon;
			public TextView title;
			public TextView data;
		}
	}

}
