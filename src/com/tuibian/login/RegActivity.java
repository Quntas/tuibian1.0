package com.tuibian.login;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuibian.aboutandset.SetChangeUserinfo;
import com.tuibian.common.CGlobal;
import com.tuibian.common.Configure;
import com.tuibian.dialog.CustomDialogCircle;
import com.tuibian.main.BaseActivity;
import com.tuibian.main.MainActivity;
import com.tuibian.model.CContext;
import com.tuibian.model.CUser;
import com.tuibian.model.CUserMgr;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;
import com.tuibian.R;

public class RegActivity extends BaseActivity {

	private EditText mEditTextPhoneNumber, mEditTextVerifyCode,mEditTextPassword; 
	private TextView regButton;
	private TextView mGetVerifycode;
	
	Timer mTimer = null;
	TimerTask mTimerTask  = null;
	private int count = 60;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		
		Handler handler = new Handler();
		
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		initViews();
		initEvents();

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		regButton=(TextView) findViewById(R.id.btn_register);
		mGetVerifycode=(TextView) findViewById(R.id.getVerifyCode);
		mEditTextPhoneNumber = (EditText)findViewById(R.id.phoneNumber);
		mEditTextVerifyCode = (EditText)findViewById(R.id.verifyCode);
		mEditTextPassword = (EditText)findViewById(R.id.password);
		
	}
	@Override  
    protected void onResume() {  
        super.onResume();
        stopTimer();
    }  
	
	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

		mGetVerifycode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String phoneNumber = mEditTextPhoneNumber.getText().toString().trim();
				if (phoneNumber.equals("")) {
					Toast.makeText(RegActivity.this, "请输入手机号码！",
							Toast.LENGTH_LONG).show();
					mEditTextPhoneNumber.requestFocus();
					return;
				}
				if (phoneNumber.length()!=11) {
					Toast.makeText(RegActivity.this, "请输入有效的手机号码！",
							Toast.LENGTH_LONG).show();
					mEditTextPhoneNumber.requestFocus();
					return;
				}
				
				new getVerifyCodeAsyncTask().execute(phoneNumber);
			}
		});
		
		regButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String phoneNumber = mEditTextPhoneNumber.getText().toString().trim();
				String verifyCode = mEditTextVerifyCode.getText().toString().trim();
				if (phoneNumber.equals("")) {
					Toast.makeText(RegActivity.this, "请输入手机号码！",
							Toast.LENGTH_LONG).show();
					mEditTextPhoneNumber.requestFocus();
					return;
				}
				if (phoneNumber.length()!=11) {
					Toast.makeText(RegActivity.this, "请输入有效的手机号码！",
							Toast.LENGTH_LONG).show();
					mEditTextPhoneNumber.requestFocus();
					return;
				}
				if (verifyCode.equals("")) {
					Toast.makeText(RegActivity.this, "请输入短信验证码！",
							Toast.LENGTH_LONG).show();
					mEditTextVerifyCode.requestFocus();
					return;
				}
				String password = mEditTextPassword.getText().toString().trim();
				if (password.equals("")) {
					Toast.makeText(RegActivity.this, "请输入密码！",
							Toast.LENGTH_LONG).show();
					mEditTextPassword.requestFocus();
					return;
				}
				
				stopTimer();
				
				new UserRegAsyncTask().execute(phoneNumber, verifyCode, password);
			}
		});
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
	
	private void startTimer(){  
	        
	count = 60;
	 
	if (mTimer == null) {  
		mTimer = new Timer();  
	}  
	    
	if (mTimerTask == null) {  
		mTimerTask = new TimerTask() {  
	    @Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
			@Override
			public void run() {
				if (count <= 0) {
					stopTimer();
				} else {
					mGetVerifycode.setEnabled(false);
					mGetVerifycode.setText("剩余" + count + "秒");
								}
								count--;
			}
			});
	    }
		};
	};
	  
    if(mTimer != null && mTimerTask != null )  
        mTimer.schedule(mTimerTask, 0, 1000);  
	  
	} 
	private void stopTimer(){  
        
        if (mTimer != null) {  
            mTimer.cancel();  
            mTimer = null;  
        }  
  
        if (mTimerTask != null) {  
            mTimerTask.cancel();  
            mTimerTask = null;  
        }     
        
        mGetVerifycode.setEnabled(true);
		mGetVerifycode.setText("获取验证码");
		
        count = 0;  
  
    }  
	
	private class getVerifyCodeAsyncTask extends AsyncTask<String, String, JSONObject> {

		CustomDialogCircle dialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomDialogCircle(RegActivity.this,
					R.layout.h_dialog_layout_circle, R.style.DialogTheme);
			dialog.setTitle("正在获取短信验证码...");
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String phoneNumber = params[0];

			JSONObject jsonobj = null;
			
			//{"ret":"success","code":"666666"}
			//{"ret":"failed","desc":"电话号码无效！"}
			String sUrl = CGlobal.SERVICE_ADDR
					+ "/GetVerifyCodeServlet";;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phoneNo", phoneNumber);
			
			try {
				//String sContent = CommUtils.doPost(sUrl, map);
				//jsonobj = JSONUtil.toObj(sContent);
				jsonobj = new JSONObject();
				jsonobj.put("ret", "success");
				jsonobj.put("code", getSmsCodeTemp());

			} catch (Exception e) {
				e.printStackTrace();
			}

			return jsonobj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dialog.dismiss();
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(RegActivity.this, "网络访问异常！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			String ret = result.optString("ret");
			String code = result.optString("code");

			if (ret.equals("success")) {
				
				if (code!=null && !code.equals("null")) {
					mEditTextVerifyCode.setText(code);
					startTimer();
				}
				if (code!=null && code.equals("null")) {
					Toast.makeText(RegActivity.this, "请使用之前的验证码！", Toast.LENGTH_LONG).show();
				}
			} 
			else {
				Toast.makeText(RegActivity.this, "获取短信验证码失败，请点击按钮重新获取!",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	private class UserRegAsyncTask extends AsyncTask<String, String, JSONObject> {

		CustomDialogCircle dialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomDialogCircle(RegActivity.this,
					R.layout.h_dialog_layout_circle, R.style.DialogTheme);
			dialog.setTitle("正在注册，请稍候...");
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			String phoneNumber = params[0];
			String verifycode = params[1];
			String password = params[2];

			JSONObject jsonobj = null;
			
			String sUrl = CGlobal.SERVICE_ADDR
					+ "/UserRegServlet";;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phoneNo", phoneNumber);
			map.put("verifyCode", verifycode);
			map.put("password", password);
			try {
				//String sContent = CommUtils.doPostWithSession(sUrl, map);
				///jsonobj = JSONUtil.toObj(sContent);
				jsonobj = new JSONObject();
				jsonobj.put("ret", "success");
				jsonobj.put("phoneNo", phoneNumber);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return jsonobj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dialog.dismiss();
			// 从result中获取到值，并且根据响应跳转界面 成功就跳转 失败不跳转
			if (result == null) {
				Toast.makeText(RegActivity.this, "网络访问异常！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			//注册成功则返回//{"ret":"success"}
			//已注册则返回//{"ret":"failed",'desc':'电话号码已注册！'}
			String ret = result.optString("ret");
			String desc = result.optString("desc");

			if (ret.equals("success")) {
				
				//转向用户信息设置界面
				Intent intent = new Intent(RegActivity.this,
						SetChangeUserinfo.class);
				
				startActivity(intent);
				
				RegActivity.this.finish();
			}
			else
			{
				Toast.makeText(RegActivity.this, desc, Toast.LENGTH_LONG).show();
			}
				
		}
	}
	public String getSmsCodeTemp()
	{
		String code;
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String datestr = df.format(now);
		
		code = datestr.substring(datestr.length()-3, datestr.length()) + datestr.substring(datestr.length()-5, datestr.length()-3);
		code = code + new Random().nextInt(10);
		
		return code;
	}

}
