package com.zhbj.activity;

import com.example.zhbj.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsPushActivity extends Activity {
	private ProgressBar pb_progress;
	private WebView wv_news;
	private TextView tv_push_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_push);
		initView();
		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		wv_news.loadUrl(url);

		WebSettings settings = wv_news.getSettings();
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);

		wv_news.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// System.out.println("寮�鍔犺浇缃戦〉浜�);
				pb_progress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pb_progress.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		// mWebView.goBack();//上一页
		// mWebView.goForward();//前一页
		//进度监听
		wv_news.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("progress"+newProgress);
					pb_progress.setProgress(newProgress);
					if (newProgress == 100) {
						pb_progress.setProgress(100);
						pb_progress.setVisibility(View.GONE);
					}
				super.onProgressChanged(view, newProgress);
				
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				
			}
		});
	}

	private void initView() {
		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
		wv_news = (WebView) findViewById(R.id.wv_news);
		tv_push_title = (TextView) findViewById(R.id.tv_push_title);
		tv_push_title.setText("最新闻推送");
	}
}
