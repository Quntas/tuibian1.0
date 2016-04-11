package com.tuibian.aboutandset;

import android.os.Bundle;

import com.tuibian.main.BaseActivity;
import com.tuibian.R;

/**
 * 
 * 关于页面
 * 
 * @author Administrator
 * 
 */
public class About extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addView(R.layout.h_set_about);
		mTitleBar.setTitleText("关于");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
