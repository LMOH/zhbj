package com.zhbj.fragment;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhbj.activity.MainActivity;
import com.zhbj.domain.NewsMenu.NewsMenuData;
import com.zhbj.imple.NewsPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author PC-LMOH
 * 侧边栏Fragment
 */
public class LeftMenuFragment extends BaseFragment {
	
	@ViewInject(R.id.lv_menu)
	public ListView mMenuListView;
	public ArrayList<NewsMenuData> mNewsMenuData;
	/**
	 * 当前item位置
	 */
	public int mCurrentPos;
	private MenuListAdapter mAdapter;
	
	@Override
	public View initView() {
		View view = View.inflate(mActivity,R.layout.fragment_left_menu, null);
		ViewUtils.inject(this,view); 	//注入view和事件
		
		return view;
	}

	@Override
	public void initData() {
		
	}


	/**设置侧滑板菜单数据
	 * @param data
	 */
	public void setMenuData(ArrayList<NewsMenuData> data) {
		//位置归零
		mCurrentPos = 0;
		mNewsMenuData = data;
		mAdapter = new MenuListAdapter();
		mMenuListView.setAdapter(mAdapter);
		mMenuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCurrentPos = position;
				//刷新适配器
				mAdapter.notifyDataSetChanged();
				// 收起侧边栏
				toggle();
				//切换详情页
				setCurrentDetailPager(position);
			}
		});
	}
	/**切换详情页
	 * @param position
	 */
	protected void setCurrentDetailPager(int position) {
		MainActivity mMianUI = (MainActivity) mActivity;
		ContentFragment contentFragment = mMianUI.getContentFragment();
		NewsPager pager = contentFragment.getNewsPager();
		//在新闻首页设置详情页
		pager.setCurrentDetailPager(position);
	}

	/**
	 * slidingMenu收起或展开
	 */
	public void toggle() {
		MainActivity mMainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mMainUI.getSlidingMenu();
		slidingMenu.toggle();
	}
	class MenuListAdapter extends BaseAdapter{

		private TextView tv_menu;

		@Override
		public int getCount() {
			return mNewsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			tv_menu = (TextView) view.findViewById(R.id.tv_menu);
			tv_menu.setText(mNewsMenuData.get(position).title);
			if (mCurrentPos == position) {
				tv_menu.setEnabled(true);
			}else{
				tv_menu.setEnabled(false);
			}
			return view;
		}
		
	}
}
