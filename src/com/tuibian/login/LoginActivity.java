package com.tuibian.login;

import java.io.File;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.tuibian.common.CGlobal;
import com.tuibian.common.Configure;
import com.tuibian.dialog.CustomDialog;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.MainActivity;
import com.tuibian.model.CContext;
import com.tuibian.model.CUser;
import com.tuibian.model.CUserMgr;
import com.tuibian.util.CommUtils;
import com.tuibian.R;

public class LoginActivity extends Activity {
	private static final int Type = 0;
	private TextView loginButton;
	private EditText EditName;
	private EditText EditPwd;
	private ImageView name_clean_img;
	private ImageView pwd_clean_img;
	private TextView login_name_txt;
	private TextView login_pwd_txt;
	
	private TextView regButton;//add by jason 2015.8.27

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initview();
		initEvent();
		// 自动登录
		String sUserName = getSharedPreferences(Configure.mSharePName, 0)
				.getString(Configure.mUserName, "");
		
		String sMobile = getSharedPreferences(Configure.mSharePName, 0)
				.getString(Configure.mMobile, "");
		
		String sPwd = getSharedPreferences(Configure.mSharePName, 0).getString(
				Configure.mUserPwd, "");
		EditPwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		
		/*
		if (sUserName != null && sUserName.length() > 0) {
			EditName.setText(sUserName);
			EditPwd.setText(sPwd);
			if(!sPwd.equals("")&&(sPwd!=null)){
				new loginAsyncTask().execute(sUserName, sPwd);
			}
		}
		*/
		//临时处理
		if (sMobile != null && sMobile.length() > 0) {
			EditName.setText(sMobile);
		}
		editNameIsNull();
		editPwdIsNull();
	}

	private void initview() {
		//login_name_txt = (TextView) findViewById(R.id.login_name_txt);
		//login_pwd_txt = (TextView) findViewById(R.id.lonin_pwd_txt);
		// 得到输入框的值
		EditName = (EditText) findViewById(R.id.phoneNumber);
		EditPwd = (EditText) findViewById(R.id.password);
		EditPwd.setLongClickable(false);
		EditPwd.setTextIsSelectable(false);
		
		name_clean_img = (ImageView) findViewById(R.id.name_clean);
		pwd_clean_img = (ImageView) findViewById(R.id.pwd_clean);
		loginButton = (TextView) findViewById(R.id.btn_login);
		
		regButton = (TextView) findViewById(R.id.btn_register);
				
		cleanEditText();
	}

	private void initEvent() {
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isTablet(LoginActivity.this) == false) {
					String userName = EditName.getText().toString().trim();
					String userPwd = EditPwd.getText().toString();
					if (userName.equals("")) {
						Toast.makeText(LoginActivity.this, "请输入手机号码！",
								Toast.LENGTH_LONG).show();
						EditName.requestFocus();
						return;
					}

					if (userPwd.equals("")) {
						Toast.makeText(LoginActivity.this, "请输入密码！",
								Toast.LENGTH_LONG).show();
						EditPwd.requestFocus();
						return;
					} 
					// 后续判断Status的值为1或者0登陆
					new loginAsyncTask().execute(userName, userPwd);
				} else {
					CustomDialog alertDialog = new CustomDialog.Builder(
							LoginActivity.this)
							.setTitle("注意")
							.setMessage("该软件版本只能支持手机")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											LoginActivity.this.finish();
											dialog.cancel();
										}
									})
							.

							setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.cancel();
										}
									}).create();
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();
				}
			}

		});
		
		regButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
		        	Intent intent = new Intent(LoginActivity.this, RegActivity.class);
					startActivity(intent);
			}
		});
	}

	private void editPwdIsNull() {
		if (!(EditPwd.getText().toString()).isEmpty()) {
			//login_pwd_txt.setVisibility(View.GONE);
			pwd_clean_img.setVisibility(View.VISIBLE);
		}else{
			//login_pwd_txt.setVisibility(View.VISIBLE);
			pwd_clean_img.setVisibility(View.GONE);
		}
	}

	private void editNameIsNull() {
		//文本不为空
		if (!(EditName.getText().toString()).equals("")) {
			//login_name_txt.setVisibility(View.GONE);
			name_clean_img.setVisibility(View.VISIBLE);
		}else{
			//文本为空
			//login_name_txt.setVisibility(View.VISIBLE);
			name_clean_img.setVisibility(View.GONE);
		}
	}
	//清除输入字符串
	private void cleanEditText() {
		
		name_clean_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditName.setText("");
				//login_name_txt.setVisibility(View.GONE);
//				name_clean_img.setVisibility(View.GONE);
			}
		});
		
		pwd_clean_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					
				EditPwd.setText("");
				//login_pwd_txt.setVisibility(View.GONE);
//				pwd_clean_img.setVisibility(View.GONE);
			}
		});
		//显示隐藏图片
		EditName.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {//失去焦点
					editNameIsNull();
				}else{
					//得到焦点
					//login_name_txt.setVisibility(View.GONE);
					name_clean_img.setVisibility(View.VISIBLE);
				}
			}
		});
		
		EditPwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {//失去焦点
					editPwdIsNull();
				}else{
					//login_pwd_txt.setVisibility(View.GONE);
					pwd_clean_img.setVisibility(View.VISIBLE);
				}
			}
		});
		
	}
	

	private class loginAsyncTask extends AsyncTask<String, String, JSONObject> {

		CustomDialogCircle dialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomDialogCircle(LoginActivity.this,
					R.layout.h_dialog_layout_circle, R.style.DialogTheme);
			dialog.setTitle("正在登录，请稍候...");
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String username = params[0];
			String password = params[1];

			//////临时演示使用
			CGlobal.m_Mobile = username;
			username = "13604334226";
			password = "101514";
			
			CGlobal.m_User = new CUser();
			CGlobal.m_User.Name = username;
			CGlobal.m_User.Pwd = password;
			
			// 调用登陆方法
			JSONObject object = CUserMgr.Login(username, password, Type, null);

			return object;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dialog.dismiss();
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(LoginActivity.this, "网络访问异常！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			//jsonobject里的内容需要自定义和高兹
			//代表成功和失败
			String Status = result.optString("success");
			//提示信息
			String Err = result.optString("msg");

			if (Status.equals("true")) {
				// 登录成功
				//逻辑自己写
				try {
					JSONObject entitys = result.getJSONObject("Ret");

					String uid = entitys.optString("id");
					CContext.DATABASE_NAME = uid + ".db";
					
					CGlobal.m_User.Id = UUID.fromString(uid);
					CGlobal.m_User.mqttid = entitys.optString("mqttid");
					CGlobal.m_User.token = entitys.optString("token");
					CGlobal.m_User.TName = entitys.optString("TName");
					CGlobal.m_User.Sex = entitys.optString("Sex");
					// String t=CGlobal.m_User.token;

					CGlobal.setCContext();// 设置Context。
					CGlobal.ResetDataManager();//重置

					// 保存自动登陆
					getSharedPreferences(Configure.mSharePName, 0)
							.edit()
							.putString(Configure.mMobile, CGlobal.m_Mobile)
							.commit();
					getSharedPreferences(Configure.mSharePName, 0)
							.edit()
							.putString(Configure.mUserName, CGlobal.m_User.Name)
							.commit();
					getSharedPreferences(Configure.mSharePName, 0).edit()
							.putString(Configure.mUserPwd, CGlobal.m_User.Pwd)
							.commit();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//Intent intent = new Intent(LoginActivity.this,
				//		MainActivity.class);
				//startActivity(intent);
				LoginActivity.this.setResult(RESULT_OK, null);
				LoginActivity.this.finish();
			} else {
				Toast.makeText(LoginActivity.this, "登录失败," + Err,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	/**
	 * 判断是否是平板
	 * 
	 * @param context
	 * @return 平板返回 True，手机返回 False
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	// 触摸回调方法
    public boolean onTouchEvent(MotionEvent event) { 
        switch (event.getAction()) {
        	//触摸屏幕时刻
        	case MotionEvent.ACTION_DOWN:
        		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        		imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        		break;
        }
        return super.onTouchEvent(event); 
    } 
    
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    /*
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();   
            } else {
                finish();
                System.exit(0);
            }
            return true;   
        }
    */
        return super.onKeyDown(keyCode, event);
    
    }
    
}
