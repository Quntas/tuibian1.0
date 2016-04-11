package com.tuibian.welcome;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tuibian.R;

public class GuidePageUtil {

	public static final boolean isCycle = false;

	public static List<View> getPageList(Context context) {
		List<View> pageList = new ArrayList<View>();
		pageList.add(getPageView(context, R.drawable.guide_01));
		pageList.add(getPageView(context, R.drawable.guide_02));
		pageList.add(getPageView(context, R.drawable.guide_03));
		return pageList;
	}

	private static View getPageView(Context context, int imgResId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View pageView = inflater
				.inflate(R.layout.activity_guid_page_item, null);
		ImageView imgPage = (ImageView) pageView.findViewById(R.id.imgPage);
		imgPage.setBackgroundResource(imgResId);
		return pageView;
	}
}
