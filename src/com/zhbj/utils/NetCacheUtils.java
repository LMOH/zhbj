package com.zhbj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**网络缓存
 * @author PC-LMOH
 *
 */
public class NetCacheUtils {

	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}

	public  void getBitmapFromNet(ImageView imageView, String url) {
		new MyBitmapTask().execute(imageView, url); // 启动AsyncTask
	}

	/*********************** AsyncTask异步缓存使用 **********************************/
	/**
	 * @author PC-LMOH 三个泛型参数说明：
	 *         参数1：代表在doInBackground的参数类型，此处doInBackground收到的可变参数params[0]
	 *         和params[1] 分别为imageView,url 参数2：代表onProgressUpdate的参数类型
	 *         参数3：代表doInBackground的返回类型和onPostExecute的参数类型，由前者返回参数给后者更新UI
	 */
	class MyBitmapTask extends AsyncTask<Object, Integer, Bitmap> {

		private String mUrl;
		private ImageView mImageView;

		// 预加载，运行在主线程
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// 正在加载，运行在子线程。可直接异步请求
		@Override
		protected Bitmap doInBackground(Object... params) {
			mImageView = (ImageView) params[0];
			mUrl = (String) params[1];
			mImageView.setTag(mUrl);// 打标记, 将当前imageview和url绑定在了一起
			Bitmap bitmap = downLoadBitmap(mUrl);
			// 进度更新，根据下载方法传回的实时进去更新
			// publishProgress(); //values可变参数
			return bitmap;			//返回结果供onPostExecute使用
		}

		// 更新进度，运行在主线程
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// 加载结束，运行在主线程。可直接更新UI
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				// 给imageView设置图片
				// 由于listview的重用机制导致imageview对象可能被多个item共用,
				// 从而可能将错误的图片设置给了imageView对象
				// 所以需要在此处校验, 判断是否是正确的图片
				String url = (String) mImageView.getTag();
				if (url.equals(this.mUrl)) {
					// 判断图片绑定的url是否就是当前bitmap的url,
					// 如果是,说明图片正确,设置给imageview
					mImageView.setImageBitmap(result);
					//写本地缓存,内存缓存
					mLocalCacheUtils.setLocalCache(mUrl, result);
					mMemoryCacheUtils.setMemoryCache(mUrl, result);
				}
			}
			super.onPostExecute(result);
		}

	}

	/**
	 * 从网络下载图片，无框架使用
	 * 
	 * @param mUrl
	 * @return Bitmap对象
	 */
	public Bitmap downLoadBitmap(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn = (HttpURLConnection) new URL(url).openConnection();

			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);// 连接超时
			conn.setReadTimeout(5000);// 读取超时

			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				InputStream inputStream = conn.getInputStream();

				// 根据输入流生成bitmap对象
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

				return bitmap;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
}
