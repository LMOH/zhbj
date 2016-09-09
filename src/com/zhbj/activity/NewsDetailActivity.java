package com.zhbj.activity;

import com.example.zhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class NewsDetailActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.ll_control)
	private LinearLayout ll_Control;
	@ViewInject(R.id.btn_back)
	private ImageButton btn_back;
	@ViewInject(R.id.btn_menu)
	private ImageButton btn_menu;
	@ViewInject(R.id.btn_share)
	private ImageButton btn_share;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btn_textsize;
	@ViewInject(R.id.wv_news)
	private WebView mWebView;
	@ViewInject(R.id.pb_news_loading)
	private ProgressBar pbLoading;

	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activty_news_detail);
		ViewUtils.inject(this);
		initView();
		initNewsWeb();
	}

	private void initNewsWeb() {
		mUrl = getIntent().getStringExtra("newsUrl");
		mWebView.loadUrl(mUrl);

		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient() {
			// 寮�鍔犺浇缃戦〉
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// System.out.println("寮�鍔犺浇缃戦〉浜�);
				pbLoading.setVisibility(View.VISIBLE);
			}

			// 缃戦〉鍔犺浇缁撴潫
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// System.out.println("缃戦〉鍔犺浇缁撴潫");
				pbLoading.setVisibility(View.INVISIBLE);
			}

			// 鎵�湁閾炬帴璺宠浆浼氳蛋姝ゆ柟娉�
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// System.out.println("璺宠浆閾炬帴:" + url);
				view.loadUrl(url);// 
				return true;
			}
		});

		// mWebView.goBack();//上一页
		// mWebView.goForward();//前一页
		//进度监听
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				
			}
		});
	}

	private void initView() {
		// 设置隐藏显示
		ll_Control.setVisibility(View.VISIBLE);
		btn_back.setVisibility(View.VISIBLE);
		btn_menu.setVisibility(View.INVISIBLE);
		//设置监听
		btn_back.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_textsize.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_textsize:
			
			setTextSize();
			break;
		case R.id.btn_share:
			showShare();
			break;
			
		default:
			break;
		}
	}

	private int tempWhich; // 临时保存变量
	private int currentWhich = 2; //确定后保存变量

	/**
	 * 设置字体大小对话框
	 */
	private void setTextSize() {
		AlertDialog.Builder builder = new Builder(this);
		//鑾峰彇webview鐨剆etting
		final WebSettings settings = mWebView.getSettings();
		String[] textSize = new String[] { "超大号字体","大号字体", "正常字体", "小号字体", "超大号字体" };
		
		builder.setTitle("字体设置");
		builder.setSingleChoiceItems(textSize, currentWhich, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tempWhich = which;
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (tempWhich) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					//settings.setTextZoom(100);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}
				currentWhich = tempWhich;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 
		 //oks.setTheme(OnekeyShareTheme.SKYBLUE);//设置主题
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("分享");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		 oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");
		 
		// 启动分享GUI
		 oks.show(this);
		 }
}
