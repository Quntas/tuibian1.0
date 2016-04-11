package com.tuibian.aboutandset;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tuibian.common.CGlobal;
import com.tuibian.common.Commons;
import com.tuibian.dialog.MyDatePickerDialog;
import com.tuibian.main.BaseActivity;
import com.tuibian.model.CUser;
import com.tuibian.util.ToastUtil;
import com.tuibian.R;

public class SetChangeUserinfo extends BaseActivity {
	private EditText Phone, QQ, Email, Name, Height, Weight;
	private TextView Birthday;
	private RadioGroup SexCharge;
	private Button changeinfo_submit;
	private String tName, tPhone, tQQ, tEmail,tBirthday="1980-01-01",tHeight, tWeight;
	private String sex;
	private RadioButton rb;
	
	private ImageView clean1, clean2, clean3,clean4,clean_height,clean_weight;
	//控制提交按钮点击次数
	private Boolean summit_status =true;
	
	private DownAsyncTask downAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.h_set_changeuserinfo);
		mTitleBar.setTitleText("设置用户信息");

		LoadData();
		if(CGlobal.m_User!=null)
		{
			downAsyncTask = new DownAsyncTask();
			downAsyncTask.execute();
		}
	}

	public void LoadData() {
		
		if(CGlobal.m_User!=null)
		{
			Name.setText(CGlobal.m_User.TName);
			
			if(CGlobal.m_User.Birthday!=null) 
				Birthday.setText(CGlobal.m_User.Birthday);
			
			if(CGlobal.m_User.Height!=null) 
				Height.setText(CGlobal.m_User.Height);
			
			if(CGlobal.m_User.Weight!=null) 
				Weight.setText(CGlobal.m_User.Weight);
			
			Phone.setText(CGlobal.m_User.Phone);
			QQ.setText(CGlobal.m_User.QQ);
			Email.setText(CGlobal.m_User.Email);
			if (CGlobal.m_User.Sex.equalsIgnoreCase("男")) {
				SexCharge.check(SexCharge.getChildAt(0).getId());
			} 
			else {
				SexCharge.check(SexCharge.getChildAt(1).getId());
			}
		}
	}

	public void doClick() {
		tName = Name.getText().toString().trim();
		tPhone = Phone.getText().toString().trim();
		tQQ = QQ.getText().toString().trim();
		tEmail = Email.getText().toString().trim();
		tBirthday = Birthday.getText().toString().trim();
		tHeight = Height.getText().toString().trim();
		tWeight = Weight.getText().toString().trim();
		//判断输入条件
		if (TextUtils.isEmpty(tPhone)) {
			Toast.makeText(this, "请输入手机号码!", Toast.LENGTH_SHORT).show();
			return;
		}else if(Commons.validateMobileNumber(tPhone)==false){
			ToastUtil.show(this, "手机号格式不正确!");
			return;
		}
		
		if (TextUtils.isEmpty(tName)) {
			Toast.makeText(this, "昵称太长，请重新输入!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (tName.length()>16) {
			Toast.makeText(this, "请输入昵称!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(tHeight)) {
			Toast.makeText(this, "请输入身高（cm）!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(tWeight)) {
			Toast.makeText(this, "请输入体重（kg）!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(tName)) {
			Toast.makeText(this, "请输入昵称!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		/*
		if (TextUtils.isEmpty(tQQ)) {
			Toast.makeText(this, "请输入QQ号码!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(tEmail)) {
			Toast.makeText(this, "请输入邮箱!", Toast.LENGTH_SHORT).show();
			return;
		}
		if(Commons.validateEmail(tEmail)==false){
			ToastUtil.show(this, "邮箱格式不正确!");
			return;
		}
		*/
		
		summit_status=false;
		CGlobal.m_User.TName = tName;
		CGlobal.m_User.Phone = tPhone;
		CGlobal.m_User.Email = tEmail;
		CGlobal.m_User.QQ = tQQ;
		CGlobal.m_User.Birthday = tBirthday;
		CGlobal.m_User.Height = tHeight;
		CGlobal.m_User.Weight = tWeight;
		Birthday.setText(CGlobal.m_User.Birthday);
		
		int radioButtonId = SexCharge.getCheckedRadioButtonId();
		rb = (RadioButton) SetChangeUserinfo.this.findViewById(radioButtonId);
		sex = rb.getText().toString();
		CGlobal.m_User.Sex = sex;

		String sUserInfo = String
				.format("{\"Phone\":\"%s\",\"Name\":\"%s\",\"Pwd\":\"%s\", \"TName\":\"%s\",\"Sex\":\"%s\",\"Birthday\":\"%s\",\"Height\":\"%s\",\"Weight\":\"%s\",\"QQ\":\"%s\",\"Email\":\"%s\"}",
						CGlobal.m_User.Phone, CGlobal.m_User.Name,
						CGlobal.m_User.Pwd, CGlobal.m_User.TName,
						CGlobal.m_User.Sex, CGlobal.m_User.Birthday,
						CGlobal.m_User.Height, CGlobal.m_User.Weight,
						CGlobal.m_User.QQ, CGlobal.m_User.Email);

		UpdateAsyncTask myAsyncTask = new UpdateAsyncTask();
		myAsyncTask.execute(sUserInfo);

	}

	/** 异步线程用来获取数据 */
	private class DownAsyncTask extends AsyncTask<String, Void, CUser> {

		@Override
		protected CUser doInBackground(String... params) {
			return CGlobal.GetCtx().GetUserMgr()
					.SearchUserInfo(CGlobal.m_User.Id);
		}

		@Override
		protected void onPostExecute(CUser User) {

			if (User == null)
				return;
			// 有新数据，刷新界面
			CGlobal.m_User.TName = User.TName;
			CGlobal.m_User.Email = User.Email;
			CGlobal.m_User.Sex = User.Sex;
			CGlobal.m_User.Birthday = User.Birthday;
			CGlobal.m_User.Height = User.Height;
			CGlobal.m_User.Weight = User.Weight;
			CGlobal.m_User.Phone = User.Phone;
			CGlobal.m_User.QQ = User.QQ;
			CGlobal.m_User.Type = User.Type;

			LoadData();
			changeinfo_submit.setText("提交");
			changeinfo_submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(summit_status){
						doClick();
						
					}
				}
			});
		}
	}

	// 提交修改信息
	private class UpdateAsyncTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject jsonobj = null;
			
			if(CGlobal.m_User!=null)
			{
				jsonobj = CGlobal.GetCtx().GetUserMgr().UpdateUserInfo(params[0]);
			}
			else
			{
				jsonobj = new JSONObject();
				try {
					jsonobj.put("Status", "1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return jsonobj;
		}

		@Override
		protected void onPostExecute(JSONObject jsObj) {

			if (jsObj == null)
				return;
			// 有新数据，刷新界面
			long Status;
			try {
				Status = jsObj.getLong("Status");
				if (Status == 1) {
					Toast.makeText(SetChangeUserinfo.this, "修改成功！", 1500)
							.show();
					tack.removeActivity(SetChangeUserinfo.this);
					SetChangeUserinfo.this.finish();
				} else
					Toast.makeText(SetChangeUserinfo.this, "提交失败！", 1500)
							.show();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(SetChangeUserinfo.this, "提交失败！", 1500)
				.show();
			}

		}
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

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
		clean4 = (ImageView) findViewById(R.id.clean4);
		clean_height = (ImageView) findViewById(R.id.clean_height);
		clean_weight = (ImageView) findViewById(R.id.clean_weight);
		
		SexCharge = (RadioGroup) findViewById(R.id.change_sex);
		Name = (EditText) findViewById(R.id.change_nickName);
		Phone = (EditText) findViewById(R.id.change_phone);
		Phone.setInputType(InputType.TYPE_CLASS_PHONE);
		Birthday = (TextView) findViewById(R.id.change_birthday);
		Height = (EditText) findViewById(R.id.change_height);
		Weight = (EditText) findViewById(R.id.change_weight);
		QQ = (EditText) findViewById(R.id.change_qq);
		QQ.setInputType(InputType.TYPE_CLASS_NUMBER);
		Email = (EditText) findViewById(R.id.change_email);
		Email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		changeinfo_submit = (Button) findViewById(R.id.change_submit);
		changeinfo_submit.setText("读取数据中...");
		
		cleanTxt();
		
	}
	/** 清空数据 */
	private void cleanTxt() {
		clean1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Name.setText("");
			}
		});
		clean2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Phone.setText("");
			}
		});
		clean3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QQ.setText("");
			}
		});
		clean4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Email.setText("");
			}
		});
		clean_height.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Height.setText("");
			}
		});
		clean_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Weight.setText("");
			}
		});

		// 显示隐藏图片
		Name.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							clean1.setVisibility(View.GONE);
						} else {
							clean1.setVisibility(View.VISIBLE);
						}
					}
				});
		
		//点击日期按钮布局 设置日期
		Birthday.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        new MyDatePickerDialog(SetChangeUserinfo.this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
		            @Override
		            public void onDateSet(DatePicker view, int year, int month, int day) {
		                // TODO Auto-generated method stub
		                //更新EditText控件日期 小于10加0
		                StringBuffer sb = new StringBuffer(); 
                        sb.append(String.format("%d-%02d-%02d",year,month+1,day)); //2015-01-01
                        tBirthday = sb.toString();
                        Birthday.setText(tBirthday);
		            }
		        //}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) ).show();
		        }, Integer.parseInt(tBirthday.substring(0, 4)), Integer.parseInt(tBirthday.substring(5, 7))-1, Integer.parseInt(tBirthday.substring(8,10))).show();    
		    }
		});
		
		Height.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					clean_height.setVisibility(View.GONE);
				} else {
					clean_height.setVisibility(View.VISIBLE);
				}
			}
		});
		Weight.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					clean_weight.setVisibility(View.GONE);
				} else {
					clean_weight.setVisibility(View.VISIBLE);
				}
			}
		});
		
		Phone.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							clean2.setVisibility(View.GONE);
						} else {
							clean2.setVisibility(View.VISIBLE);
						}
					}
				});

		QQ.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							clean3.setVisibility(View.GONE);
						} else {
							clean3.setVisibility(View.VISIBLE);
						}
					}
				});
		
		Email.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					clean4.setVisibility(View.GONE);
				} else {
					clean4.setVisibility(View.VISIBLE);
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
				imm.hideSoftInputFromWindow(SetChangeUserinfo.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				break;

			}

			return super.onTouchEvent(event);
		}

}
