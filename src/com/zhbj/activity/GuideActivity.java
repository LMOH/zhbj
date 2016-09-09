package com.zhbj.activity;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.zhbj.utils.DensityUtils;
import com.zhbj.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class GuideActivity extends Activity {
	private ViewPager vp_pager;
	private int mImageIds[] = new int[] { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };
	private ArrayList<ImageView> mImageList;
	private LinearLayout ll_container;
	private ImageView iv_redPoint;
	private int mPointDis;
	private Button btn_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		initUI();
		initData();
		// 控制器Controller
		initAdapter();
	}

	private void initData() {
		mImageList = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			// 设置图片背给ImageView
			imageView.setBackgroundResource(mImageIds[i]);
			mImageList.add(imageView);

			// 初始化小圆点，循环添加至容器ll_container
			ImageView pointView = new ImageView(this);
			pointView.setImageResource(R.drawable.shape_point_gray); // 设置形状
			// 初始化布局参数，宽高包裹内容。父控件是谁就是其布局参数，根据布局参数设置边距
			LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				// 从第二个点设置左边距(间距别写死成px),写成10dp(尺寸适配)
				int dipTopx = DensityUtils.dipTopx(getApplicationContext(), 10);
				params.leftMargin = dipTopx;
			}
			// 把布局参数设置给IV
			pointView.setLayoutParams(params);
			// 添加小白点至容器
			ll_container.addView(pointView);
		}
	}

	private void initUI() {
		vp_pager = (ViewPager) findViewById(R.id.vp_pager);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		iv_redPoint = (ImageView) findViewById(R.id.iv_red_point);
		btn_start = (Button) findViewById(R.id.btn_start);
	}

	public void initAdapter() {
		vp_pager.setAdapter(new GuideAdapter());
		// .求小红点一个页面偏移距离
		/**** 监听layout方法结束事件，位置确定好了再测量（视图树），确定偏移距离mPointDis ****/
		iv_redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// layout方法执行结束时的回调
				iv_redPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);// 移除监听，避免重复回调
				mPointDis = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();// 得到红点一个页面偏移距离
				System.out.println(mPointDis);
			}
		});
		/************************** 监听viewpager页面滑动 ****************************/
		vp_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 某个页面被选中
				/*************最后一个页面显示按钮***************/
				if (position == mImageList.size() - 1) {
					btn_start.setVisibility(View.VISIBLE);
				} else {
					btn_start.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				/*
				 * 参数说明： 当页面滑动过程中的回调(position当前位置（在viewpager的位置）,
				 * positionOffset移动偏移量百分比（到100%就是滑到下一个页面的0%）,
				 * positionOffsetPixels)
				 */
				// measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
				// 根据百分比更新小红点的位置,修改小红点的布局参数
				// 当前左边距()才能准确跟随百分比(偏移百分比*红点一个页面偏移距离)+当前所属位置*红点一个页面偏移距离
				int leftMargin = (int)( positionOffset* mPointDis) + position * mPointDis;
				System.out.println("leftMargin---" + leftMargin);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_redPoint.getLayoutParams();
				params.leftMargin = leftMargin; // 修改左边距
				iv_redPoint.setLayoutParams(params); // 刷新布局参数
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 页面状态发送变化
			}
		});
		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//存储已经不是第一次进入状态到sp
				PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);
				//跳转到主页面
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
				finish();
			}
		});
	}

	class GuideAdapter extends PagerAdapter {

		// item的个数
		@Override
		public int getCount() {
			return mImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 初始化item布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageList.get(position);
			container.addView(view);
			return view;
		}

		// 销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
