package com.tuibian.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tuibian.common.Configure;
import com.tuibian.login.LoginActivity;
import com.tuibian.main.BaseActivity;
import com.tuibian.main.MainActivity;
import com.tuibian.R;

public class WelcomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Handler handler = new Handler();
		setContentView(R.layout.h_welcome);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
				// 启动引导页
				boolean isFirstRun = getSharedPreferences(Configure.mSharePName, 0)
				.getBoolean(Configure.IsFirstRun, true);
				if (isFirstRun) {
					Intent intent = new Intent(WelcomeActivity.this,
					GuidePageActivity.class);
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(WelcomeActivity.this,
							MainActivity.class);
					startActivityForResult(intent, 11);
					int version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
					if (version >= 5) {
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}
				}
				WelcomeActivity.this.finish();
			}
		}, 3000);

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
