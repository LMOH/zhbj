package com.zhbj.domain;

import java.util.ArrayList;

public class NewsDetailBean {
	public NewsTab data;
	public class NewsTab{
            public String more;
            public  ArrayList<NewsData> news;
            public  ArrayList<TopNews> topnews;
	}
	public class NewsData{
		public int id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}
	// 头条新闻
		public class TopNews {
			public int id;
			public String topimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}
}
