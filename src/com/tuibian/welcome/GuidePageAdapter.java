package com.tuibian.welcome;

import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class GuidePageAdapter extends PagerAdapter {
	private List<View> list;
	private int pageCount;

	public GuidePageAdapter(List<View> list) {
		this.list = list;
		pageCount = list.size();
	}

	@Override
	public int getCount() {
		if (GuidePageUtil.isCycle) {
			return list == null ? 0 : Integer.MAX_VALUE;
		} else {
			return list == null ? 0 : list.size();
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (GuidePageUtil.isCycle) {
			position = position % pageCount;
		}
		container.removeView(list.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (GuidePageUtil.isCycle) {
			position = position % pageCount;
		}
		container.addView(list.get(position));
		return list.get(position);
	}
}
