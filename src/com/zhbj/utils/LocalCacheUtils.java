package com.zhbj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**本地缓存
 * @author PC-LMOH
 *
 */
public class LocalCacheUtils {

	private static final String LOCAL_CACHE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/zhbj74_cache";	//本地文件夹路径
	
	/**写本地缓存
	 * @param url
	 * @param bitmap
	 */
	public void setLocalCache(String url, Bitmap bitmap){
		File dir = new File(LOCAL_CACHE_PATH);
		if (!dir.exists() || !dir.isDirectory()) {		//若文件夹不存在或不是文件
			dir.mkdirs();// 创建文件夹
		}

		try {
			String fileName = MD5Encoder.encode(url);

			File cacheFile = new File(dir, fileName);

			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
					cacheFile));// 参1:图片格式;参2:压缩比例0-100; 参3:输出流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		/** 读本地缓存
		 * @param url
		 * @return
		 */
		public Bitmap getLocalCache(String url) {
			try {
				File cacheFile = new File(LOCAL_CACHE_PATH, MD5Encoder.encode(url));

				if (cacheFile.exists()) {
					Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
							cacheFile));
					return bitmap;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
}
