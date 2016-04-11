package com.tuibian.aboutandset;

import java.io.File;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.tuibian.common.CGlobal;
import com.tuibian.common.Configure;
import com.tuibian.dialog.CustomDialog;
import com.tuibian.login.LoginActivity;
import com.tuibian.main.BaseActivity;
import com.tuibian.main.MainActivity;
import com.tuibian.util.AsyncImageLoader;
import com.tuibian.util.CommUtils;
import com.tuibian.util.ToastUtil;
import com.tuibian.R;

/**
 * 
 * 设置页面
 * 
 * @author Administrator
 * 
 */
public class Set extends BaseActivity implements OnClickListener {
	private RelativeLayout change_pass;
	private RelativeLayout change_userinfo;
	private RelativeLayout quit;
	private RelativeLayout upload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addView(R.layout.h_set);
		mTitleBar.setTitleText("设置");
		change_pass = (RelativeLayout) findViewById(R.id.set_change_pass);
		change_userinfo = (RelativeLayout) findViewById(R.id.set_change_userinfo);
		quit = (RelativeLayout) findViewById(R.id.set_change_exit);
		upload = (RelativeLayout) findViewById(R.id.set_change_upload);
		change_pass.setOnClickListener(this);
		change_userinfo.setOnClickListener(this);
		quit.setOnClickListener(this);
		upload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 改变用户密码
		case R.id.set_change_pass:
			Intent userpass = new Intent(this, SetChangeUserPass.class);
			startActivity(userpass);
			break;
		// 改变用户信息
		case R.id.set_change_userinfo:
			Intent userinfo = new Intent(this, SetChangeUserinfo.class);
			startActivity(userinfo);
			break;
		// 退出登录
		case R.id.set_change_exit:
			CustomDialog alertDialog = new CustomDialog.Builder(Set.this)
					.setTitle("通知")
					.setMessage("是否退出登录")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									CGlobal.m_Contex = null;
									AsyncImageLoader.getIntance().clearCatche();
									RemoveLognInfo();
									Set.this.setResult(RESULT_OK);
									Set.this.finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.cancel();
								}
							}).create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();

			break;
		// 更新版本
		case R.id.set_change_upload:
			/*
			MainActivity m_MainActivity = (MainActivity) tack
					.getActivityByClass(MainActivity.class);
			m_MainActivity.m_updateContext = this;
			m_MainActivity.updateVersion();
			*/
			ToastUtil.show(Set.this, "已经是最新的版本！");
			break;
		}
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

	/** 清除帐号信息 */
	private void RemoveLognInfo() {
		getSharedPreferences(Configure.mSharePName, 0).edit()
				.remove(Configure.mUserPwd).commit();
		CGlobal.m_User = null;
	}
}
