package com.tuibian.welcome;

import java.util.List;

import com.tuibian.common.Configure;
import com.tuibian.main.MainActivity;
import com.tuibian.R;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GuidePageActivity extends Activity implements OnPageChangeListener {
	private ViewPager vp_guide;
	private List<View> pageList;
	private LinearLayout layout_dotView;
	private ImageView[] imgDots;
	private int dotCount;// 原点个数
	private ImageView jumpguide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_page);
		// 下次不再启动引导页
		getSharedPreferences(Configure.mSharePName, 0).edit()
				.putBoolean(Configure.IsFirstRun, false).commit();
		//
		initView();
		initDots();
		setPage();
		jumpguide.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initView() {
		layout_dotView = (LinearLayout) findViewById(R.id.layout_dotView);
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		jumpguide = (ImageView) findViewById(R.id.jumpguide);
		vp_guide.setOnPageChangeListener(this);
		pageList = GuidePageUtil.getPageList(this);
		dotCount = pageList.size();
	}

	private void initDots() {
		imgDots = new ImageView[dotCount];
		for (int i = 0; i < dotCount; i++) {
			ImageView dotView = new ImageView(this);
			if (i == 0) {
				dotView.setBackgroundResource(R.drawable.dot_long);
			} else {
				dotView.setBackgroundResource(R.drawable.dot_short);
			}
			imgDots[i] = dotView;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// 设置原点的间距
			params.setMargins(7, 0, 7, 0);
			dotView.setLayoutParams(params);
			layout_dotView.addView(dotView);
		}
	}

	private void setPage() {
		vp_guide.setAdapter(new GuidePageAdapter(pageList));
		if (GuidePageUtil.isCycle) {
			vp_guide.setCurrentItem(100);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		if (GuidePageUtil.isCycle) {
			arg0 = arg0 % dotCount;
		}
		for (int i = 0; i < dotCount; i++) {
			if (i == arg0) {
				imgDots[i].setBackgroundResource(R.drawable.dot_long);
			} else {
				imgDots[i].setBackgroundResource(R.drawable.dot_short);
			}
		}

		if (arg0 == dotCount - 1) {
			jumpguide.setVisibility(View.VISIBLE);
		} else {
			jumpguide.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
}
