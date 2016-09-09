package com.zhbj.utils;

import com.example.zhbj.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 三级缓存：优先从内存缓存，再到本地缓存，最后到网络缓存
 * */
public class MyBitmapUtils {
	
	private NetCacheUtils mNetCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	
	public MyBitmapUtils() {
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
	}

	public void display(ImageView imageView, String url) {
		// 设置默认图片
		imageView.setImageResource(R.drawable.pic_item_list_default);
		//1.先从内存缓存获取
		Bitmap memoryCache = mMemoryCacheUtils.getMemoryCache(url);
		if (memoryCache != null) {
			imageView.setImageBitmap(memoryCache);
			return;				//内存缓存有，直接return，不走网络
		}
		//2.再从本地缓存获取
		Bitmap localCache = mLocalCacheUtils.getLocalCache(url);
		if (localCache != null) {
			imageView.setImageBitmap(localCache);
			return;				//本地缓存有，直接return，不走网络
		}
		//3.从网络获取（获取后缓存到本地）
		mNetCacheUtils.getBitmapFromNet(imageView, url);
	}
}
