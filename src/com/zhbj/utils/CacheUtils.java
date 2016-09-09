package com.zhbj.utils;

import android.app.Activity;
import android.content.Context;

/**网络缓存工具类
 * @author PC-LMOH
 *
 */
public class CacheUtils {

	public static void setCache(String url, String json, Context ctx) {
		PrefUtils.setString(ctx, url, json);
	}

	public static String getCache(String url, Context ctx) {
		return PrefUtils.getString(ctx, url, null);
	}
	
}
