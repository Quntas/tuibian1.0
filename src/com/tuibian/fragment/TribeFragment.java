package com.tuibian.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuibian.R;

public class TribeFragment extends Fragment
{
	private View view;
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	/**
	 * 顶部三个LinearLayout
	 */
	private LinearLayout mTabLatest;
	private LinearLayout mTabHot;
	private LinearLayout mTabFollown;
	/**
	 * 顶部三个TextView
	 */
	private TextView mLatest;
	private TextView mHot;
	private TextView mFollow;

	/**
	 * Tab引导线
	 */
	private ImageView mTabLine;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_tribe, null);
		mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
		initView();

		initTabLine();
		
		/**
		 * 初始化Adapter
		 */
		mAdapter = new FragmentPagerAdapter(getChildFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);
		
		/**
		 * 设置监听
		 */
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				//重置所有TextView的字体颜色
				resetTextView();
				switch (position)
				{
				case 0:
					mLatest.setTextColor(getResources().getColor(R.color.tab_line));
					break;
				case 1:
					mHot.setTextColor(getResources().getColor(R.color.tab_line));
					break;
				case 2:
					mFollow.setTextColor(getResources().getColor(R.color.tab_line));
					break;
				}

				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				/**
				 * 利用position和currentIndex判断用户的操作是哪一页往哪一页滑动
				 * 然后改变根据positionOffset动态改变TabLine的leftMargin
				 */
				if (currentIndex == 0 && position == 0)// 0->1
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));
					mTabLine.setLayoutParams(lp);
					
				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));
					mTabLine.setLayoutParams(lp);

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));
					mTabLine.setLayoutParams(lp);
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 3) + currentIndex * (screenWidth / 3));
					mTabLine.setLayoutParams(lp);

				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});

		mViewPager.setCurrentItem(0);
		
		return view;
	}
	/**
	 * 根据屏幕的宽度，初始化引导线的宽度
	 */
	private void initTabLine()
	{
		mTabLine = (ImageView) view.findViewById(R.id.id_tab_line);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine.getLayoutParams();
		lp.width = screenWidth / 3;
		mTabLine.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	protected void resetTextView()
	{
		mLatest.setTextColor(getResources().getColor(R.color.black));
		mHot.setTextColor(getResources().getColor(R.color.black));
		mFollow.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 初始化控件，初始化Fragment
	 */
	private void initView()
	{

		mTabLatest = (LinearLayout) view.findViewById(R.id.id_tab_latest_ly);
		mTabHot = (LinearLayout) view.findViewById(R.id.id_tab_hot_ly);
		mTabFollown = (LinearLayout) view.findViewById(R.id.id_tab_follow_ly);

		mLatest = (TextView) view.findViewById(R.id.id_latest);
		mHot = (TextView) view.findViewById(R.id.id_hot);
		mFollow = (TextView) view.findViewById(R.id.id_follow);
		
		TribeLatestTab latestTab = new TribeLatestTab();
		TribeHotTab hotTab = new TribeHotTab();
		TribeFollowTab followTab = new TribeFollowTab();
		mFragments.add(latestTab);
		
		mFragments.add(hotTab);
		mFragments.add(followTab);
	}
}
