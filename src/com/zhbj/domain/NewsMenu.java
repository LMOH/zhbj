package com.zhbj.domain;

import java.util.ArrayList;

/**
 * 整个侧边栏数据对象
 * @author PC-LMOH
 *
 */
public class NewsMenu {
	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data;
	
	/**侧边栏菜单对象
	 * @author PC-LMOH
	 *
	 */
	public class NewsMenuData{
		public ArrayList<NewsTabData> children;
		public int id;
		public String title;
		public int type;
		@Override
		public String toString() {
			return "NewsMenuData [children=" + children + ", id=" + id + ", title=" + title + ", type=" + type + "]";
		}
		
	}
	// 页签的对象
	/**新闻页签的对象
	 * @author PC-LMOH
	 *
	 */
	public class NewsTabData{
		public int id;
		public String title;
		public int type;
		public String url;
		@Override
		public String toString() {
			return "NewsTabData [id=" + id + ", title=" + title + ", type=" + type + ", url=" + url + "]";
		}
		
	}
	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}
	
}
