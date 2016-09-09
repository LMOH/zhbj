package com.zhbj.imple.newsdetail;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhbj.base.BaseMenuDetailPager;
import com.zhbj.domain.PhotosBean;
import com.zhbj.domain.PhotosBean.PhotoNews;
import com.zhbj.global.GlobalConstants;
import com.zhbj.utils.CacheUtils;
import com.zhbj.utils.MyBitmapUtils;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 新闻中心详情页-组图
 * 
 * @author PC-LMOH
 *
 */
public class PhotoDetailPager extends BaseMenuDetailPager implements View.OnClickListener{

	private ImageButton mbtnPhoto;
	private ListView lv_photo;
	private GridView gv_photo;
	private ArrayList<PhotoNews> mNewsList;


	public PhotoDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		this.mbtnPhoto = btnPhoto;
		mbtnPhoto.setOnClickListener(this);
	}

	@Override
	public View initView() {
		System.out.println("新闻中心详情页-组图初始化啦....");

		/*
		 * // 初始化布局 TextView view = new TextView(mActivity);
		 * view.setText("新闻中心详情页-组图"); view.setTextColor(Color.RED);
		 * view.setTextSize(22); view.setGravity(Gravity.CENTER);
		 */
		View view = View.inflate(mActivity, R.layout.pager_photo_detail, null);
		lv_photo = (ListView) view.findViewById(R.id.lv_photo);
		gv_photo = (GridView) view.findViewById(R.id.gv_photo);
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(GlobalConstants.PHOTOS_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result);

				CacheUtils.setCache(GlobalConstants.PHOTOS_URL, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

		mNewsList = photosBean.data.news;

		lv_photo.setAdapter(new PhotoAdapter());
		gv_photo.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致,
												// 所以可以共用一个adapter
	}

	class PhotoAdapter extends BaseAdapter {

		//private BitmapUtils mBitmapUtils;
		private MyBitmapUtils mBitmapUtils;
		
		public PhotoAdapter() {
			/*mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);*/
			mBitmapUtils = new MyBitmapUtils();
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public PhotoNews getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PhotoNews item = getItem(position);

			holder.tvTitle.setText(item.title);
			mBitmapUtils.display(holder.ivPic, item.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivPic;
		public TextView tvTitle;
	}

	private boolean isListView = true;// 标记当前是否是listview展示

	@Override
	public void onClick(View v) {
		if (isListView) {
			// 切成gridview
			lv_photo.setVisibility(View.GONE);
			gv_photo.setVisibility(View.VISIBLE);
			mbtnPhoto.setImageResource(R.drawable.icon_pic_list_type);

			isListView = false;
		} else {
			// 切成listview
			lv_photo.setVisibility(View.VISIBLE);
			gv_photo.setVisibility(View.GONE);
			mbtnPhoto.setImageResource(R.drawable.icon_pic_grid_type);

			isListView = true;
		}
	}
}
