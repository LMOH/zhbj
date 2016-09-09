package com.zhbj.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import org.apache.http.conn.routing.RouteInfo.TunnelType;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * LruCache
 * @author PC-LMOH > 内存溢出 不管android设备总内存是多大, 都只给每个app分配一定内存大小, 16M,
 *         一旦超出16M就内存溢出了 从 Android 2.3 (API Level9)开始，
 *         垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。
 *         官方建议使用LruCache内存缓存技术对那些大量占用应用程序宝贵内存的图片提供了快速访问的方法。 
 *         其中最核心的类是LruCache(此类在android-support-v4的包中提供) 
 *         > 引用 - 默认强引用,垃圾回收器不会回收 - 软引用, 垃圾回收器会考虑回收
 *         SoftReference - 弱引用, 垃圾回收器更会考虑回收 WeakReference 
 *         - 虚引用, 垃圾回收器最优先回收PhantomReference
 */
public class MemoryCacheUtils {

	private LruCache<String, Bitmap> mMemoryCache;
	
	public MemoryCacheUtils(){
		long maxMemory = Runtime.getRuntime().maxMemory();
		//初始化LruCache
		mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory/8)){
			// 返回每个对象的大小
						@Override
						protected int sizeOf(String key, Bitmap value) {
							// int byteCount = value.getByteCount();
							int byteCount = value.getRowBytes() * value.getHeight();// 计算图片大小:每行字节数*高度
							return byteCount;
						}
		};
	}

	/**
	 * 写内存缓存
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void setMemoryCache(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
	}

	/**
	 * 读内存缓存
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getMemoryCache(String url) {
		
		return mMemoryCache.get(url);
	}
}


/*public class MemoryCacheUtils {

	// 使用软引用定义变量，使其能容易被垃圾回收机制回收
	// 用SoftReference对Bitmap进行包装，使其成为软引用对象
	HashMap<String, SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();

	*//**
	 * 写内存缓存
	 * 
	 * @param url
	 * @param bitmap
	 *//*
	public void setMemoryCache(String url, Bitmap bitmap) {
		SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
		map.put(url, soft);
	}

	*//**
	 * 读内存缓存
	 * 
	 * @param url
	 * @return
	 *//*
	public Bitmap getMemoryCache(String url) {
		SoftReference<Bitmap> soft = map.get(url);
		if (soft != null) {
			Bitmap bitmap = soft.get();
			return bitmap;
		}
		return null;
	}
}
*/