package com.zhbj.activity;

import com.example.zhbj.R;
import com.example.zhbj.R.id;
import com.example.zhbj.R.layout;
import com.zhbj.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends Activity {

	private RelativeLayout rl_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		rl_root = (RelativeLayout) findViewById(R.id.rl_root);

		initAnimation();

	}

	private void initAnimation() {
		// 动画
		// 1.旋转动画
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true); // 动画停留在结束位置
		// 2.缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true); // 动画停留在结束位置
		// 3.渐变动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true); // 动画停留在结束位置
		// 4.动画集合,添加动画
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);
		// 5.执行动画
		rl_root.startAnimation(animationSet);

		// 6.监听动画，跳转至下一页面
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			// 在动画结束时跳转页面
			// 如果是第一次进入, 跳新手引导
			// 否则跳主页面
			@Override
			public void onAnimationEnd(Animation animation) {
				boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
				Intent intent;
				if (isFirstEnter) {
					intent = new Intent(SplashActivity.this, GuideActivity.class);
				} else {
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(getApplicationContext());
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(getApplicationContext());
		super.onPause();
	}
}
