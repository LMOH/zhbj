package com.zhbj.view;

import java.text.SimpleDateFormat;

import java.util.Date;

import com.example.zhbj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener{

	// 刷新状态
	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;

	private int mCurrentState = STATE_PULL_TO_REFRESH;
	private View mHeaderView;
	private int mHeaderViewHeight;
	private int downY = -1;
	private int moveY;
	private TextView tv_title;
	private TextView tv_time;
	private ImageView iv_arrow;
	private ProgressBar pb_loading;
	private RotateAnimation rotateUpAnim;
	private RotateAnimation rotateDownAnim;

	public PullToRefreshListView(Context context) {
		super(context);
		init();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		initHeaderView();
		initAnim();
		initFooterView();
	}

	/**
	 * 加载脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
		this.addFooterView(mFooterView);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		this.setOnScrollListener(this);
	}

	/**
	 * 初始化动画，供箭头图片使用
	 */
	private void initAnim() {
		// 向上转
		rotateUpAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateUpAnim.setDuration(200);
		rotateUpAnim.setFillAfter(true);
		rotateDownAnim = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateDownAnim.setDuration(200);
		rotateDownAnim.setFillAfter(true);
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
		iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pb_loading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		this.addHeaderView(mHeaderView);
		// 默认头布局隐藏
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
	}

	// 重写ontouchevent，显示头布局
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 滑动时，若当前状态是正在刷新，跳出循环
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			if (downY == -1) {// 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,
				// 导致startY没有赋值,此处需要重新获取一下
				downY = (int) ev.getY();
			}
			moveY = (int) ev.getY();
			int dY = moveY - downY;
			int firstVisiblePosition = getFirstVisiblePosition();// 当前显示的第一个item的位置
			// 下拉，且当前显示第一个条目
			if (dY > 0 && firstVisiblePosition == 0) {
				int paddingTop = -mHeaderViewHeight + dY;
				mHeaderView.setPadding(0, paddingTop, 0, 0);
				if (paddingTop > 0 && mCurrentState == STATE_PULL_TO_REFRESH) {
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					stateRefresh();
				} else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					mCurrentState = STATE_PULL_TO_REFRESH;
					stateRefresh();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -1; // 手抬起，先恢复原始状态
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING; // 当状态为刷新中时，调用回调方法
				stateRefresh();
				if (mListener != null) {
					mListener.onRefresh();
				}
				// 完整展示头布局
				mHeaderView.setPadding(0, 0, 0, 0);
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据状态刷新显示
	 */
	private void stateRefresh() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tv_title.setText("下拉刷新");
			iv_arrow.setVisibility(View.VISIBLE);
			pb_loading.setVisibility(View.INVISIBLE);
			iv_arrow.setAnimation(rotateDownAnim);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tv_title.setText("释放刷新");
			iv_arrow.setVisibility(View.VISIBLE);
			pb_loading.setVisibility(View.INVISIBLE);
			iv_arrow.setAnimation(rotateUpAnim);
			break;
		case STATE_REFRESHING:
			tv_title.setText("刷新中...");
			// 先结束动画才能隐藏控件
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.INVISIBLE);
			pb_loading.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	/**
	 * 刷新完成，界面恢复原状
	 */
	public void refreshCompelete(boolean success) {
		if (!isLoadMore) {
			if (success) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
				mCurrentState = STATE_PULL_TO_REFRESH;
				tv_title.setText("下拉刷新");
				iv_arrow.setVisibility(View.VISIBLE);
				pb_loading.setVisibility(View.INVISIBLE);
				setCurrentTime();
			}
		}else{
			mFooterView.setPadding(0,-mFooterViewHeight , 0, 0);
			isLoadMore = false;
		}	
	}

	/**
	 * 设置当前事件
	 */
	private void setCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());

		tv_time.setText(time);
	}

	// 3.定义成员变量，接收监听对象
	private OnRefreshListener mListener;
	private View mFooterView;

	// 2.暴露接口，设置监听
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	// 1.写回调接口
	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();
	}

	private boolean isLoadMore;// 标记是否正在加载更多
	private int mFooterViewHeight;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) { //空闲状态
			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount() -1 && !isLoadMore) {
				isLoadMore = true;

				mFooterView.setPadding(0, 0, 0, 0);// 显示加载更多的布局

				setSelection(getCount() - 1);// 将listview显示在最后一个item上,
												// 从而加载更多会直接展示出来, 无需手动滑动
				
				//通知主界面加载下一页数据
				if(mListener!=null) {
					mListener.onLoadMore();
				}
			}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

}
