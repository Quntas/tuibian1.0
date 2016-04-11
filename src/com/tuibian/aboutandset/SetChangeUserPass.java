package com.tuibian.aboutandset;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuibian.common.CGlobal;
import com.tuibian.login.LoginActivity;
import com.tuibian.main.BaseActivity;
import com.tuibian.R;

public class SetChangeUserPass extends BaseActivity {
	private EditText Edchangepass_oldpass, Edchangepass_newpass,
			Edchangepass_newpass2;
	private TextView Edchangepass_user;
	private String v_changepass_oldpass, v_changepass_newpass,
			v_changepass_newpass2;
	private Button changepass_submit;

	private ImageView clean1, clean2, clean3;

	private String newName;
	private MyAsyncTask myAsyncTask;
	// 控制提交按钮点击次数
	private Boolean summit_status = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.h_set_changeuserpass);
		mTitleBar.setTitleText("修改密码");
	}

	public void doClick() {
		v_changepass_oldpass = Edchangepass_oldpass.getText().toString();
		v_changepass_newpass = Edchangepass_newpass.getText().toString();
		v_changepass_newpass2 = Edchangepass_newpass2.getText().toString();
		newName = v_changepass_newpass;
		if (v_changepass_oldpass.equals("") || v_changepass_newpass.equals("")
				|| v_changepass_newpass2.equals("")) {// 账号验证
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;

		}

		if (!v_changepass_newpass2.equals(v_changepass_newpass)) {
			Toast.makeText(SetChangeUserPass.this, "两次输入密码不一致！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (v_changepass_newpass2.length()<6) {
			Toast.makeText(SetChangeUserPass.this, "密码最低支持6位",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (v_changepass_newpass2.length()>12) {
			Toast.makeText(SetChangeUserPass.this, "密码最长支持12位",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!v_changepass_oldpass.equals(CGlobal.m_User.Pwd)) {
			Toast.makeText(SetChangeUserPass.this, "原密码输入错误！",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (CGlobal.m_User.Pwd.equals(v_changepass_newpass)) {
			Toast.makeText(SetChangeUserPass.this, "原密码和新密码一致，请重新输入！",
					Toast.LENGTH_SHORT).show();
			Edchangepass_newpass.setText("");
			Edchangepass_newpass2.setText("");
			return;
		}
		summit_status = false;
		myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute(0);

		// MineSe tChangeUserPass.this.finish();
	}

	/** 异步线程用来获取数据 */
	private class MyAsyncTask extends AsyncTask<Integer, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Integer... params) {

			JSONObject jsonobj = null;
			
			//jsonobj = CGlobal.GetCtx().GetUserMgr().ModPwd(newName);

			jsonobj = new JSONObject();
			try {
				jsonobj.put("Status", "1");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonobj;
		}

		@Override
		protected void onPostExecute(JSONObject jsonobj) {

			if (jsonobj == null)
				return;
			// 有新数据，刷新界面
			long Status;
			try {
				Status = jsonobj.getLong("Status");
				if (Status == 1) {
					Toast.makeText(SetChangeUserPass.this, "修改成功！",
							Toast.LENGTH_SHORT).show();
					tack.removeActivity(SetChangeUserPass.this);
					SetChangeUserPass.this.finish();
				} else
					Toast.makeText(SetChangeUserPass.this, "请重新输入！",
							Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	protected void initEvents() {
		changepass_submit = (Button) findViewById(R.id.h_changepass_submit);
		changepass_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (summit_status) {
					doClick();
				}
			}
		});

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initViews() {
		clean1 = (ImageView) findViewById(R.id.clean1);
		clean2 = (ImageView) findViewById(R.id.clean2);
		clean3 = (ImageView) findViewById(R.id.clean3);

		Edchangepass_user = (TextView) findViewById(R.id.h_change_user);
		Edchangepass_oldpass = (EditText) findViewById(R.id.h_change_pass);
		Edchangepass_oldpass.setTextIsSelectable(false);
		Edchangepass_newpass = (EditText) findViewById(R.id.h_change_passnew);
		Edchangepass_newpass.setTextIsSelectable(false);
		Edchangepass_newpass2 = (EditText) findViewById(R.id.h_change_passnew2);
		Edchangepass_newpass2.setTextIsSelectable(false);
		changepass_submit = (Button) findViewById(R.id.h_changepass_submit);

		//Edchangepass_user.setText(CGlobal.m_User.Name);
		Edchangepass_user.setText(CGlobal.m_Mobile);

		cleanTxt();
	}

	/** 清空数据 */
	private void cleanTxt() {
		clean1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Edchangepass_oldpass.setText("");
			}
		});
		clean2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Edchangepass_newpass.setText("");
			}
		});
		clean3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Edchangepass_newpass2.setText("");
			}
		});

		// 显示隐藏图片
		Edchangepass_oldpass
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							clean1.setVisibility(View.GONE);
						} else {
							clean1.setVisibility(View.VISIBLE);
						}
					}
				});

		Edchangepass_newpass
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							clean2.setVisibility(View.GONE);
						} else {
							clean2.setVisibility(View.VISIBLE);
						}
					}
				});

		Edchangepass_newpass2
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							clean3.setVisibility(View.GONE);
						} else {
							clean3.setVisibility(View.VISIBLE);
						}
					}
				});

	}

	// 点击
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		// 触摸屏幕时刻
		case MotionEvent.ACTION_DOWN:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(SetChangeUserPass.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			break;

		}

		return super.onTouchEvent(event);
	}

}
