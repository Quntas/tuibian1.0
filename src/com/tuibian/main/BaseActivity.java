package com.tuibian.main;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tuibian.common.ActivityTack;
import com.tuibian.common.CGlobal;
import com.tuibian.model.CContext;
import com.tuibian.R;

public abstract class BaseActivity extends FragmentActivity {

	public final static String KEY = "KEY";
	public final static String UnNet = "";
	public final static String Hint = "";
	public int mScreenWidth;
	public int mScreenHeight;

	public TitleFragment mTitleBar;
	public LayoutInflater mInflate;

	public LinearLayout mContentView;

	// 启动tack单例
	public ActivityTack tack = ActivityTack.getInstanse();
	public InputMethodManager imm;
	protected List<HttpURLConnection> mHttpURLConnections = new ArrayList<HttpURLConnection>();
	public CContext mCContext;
	public Context mContext;
	private BaseApplication mBaseApplication;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tack.addActivity(this);
		mContext = getApplicationContext();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mInflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_abase_main);

		mContentView = (LinearLayout) findViewById(R.id.ll_content);
		mTitleBar = (TitleFragment) getSupportFragmentManager()
				.findFragmentById(R.id.my_base_TitleBar);

		mCContext = CGlobal.GetCtx();

		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		mBaseApplication = (BaseApplication) getApplication();

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

	}

	protected abstract void initViews();

	protected abstract void initEvents();

	protected abstract void init();

	/** 隐藏整个title */
	public void hideAll() {
		mTitleBar.hideAll();
	}

	/** 显示整个title */
	public void showAll() {
		mTitleBar.showAll();
	}

	/** 得到TitleFragment */
	public TitleFragment getTitileBar() {
		return mTitleBar;
	}

	/** 得到LayoutInflater */
	public LayoutInflater getInflater() {
		return mInflate;
	}

	/** 通过Id添加view,添加到linearLayout中 */
	protected void addView(int id) {

		mContentView.addView(mInflate.inflate(id, null),
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));

		initViews();
		initEvents();
		init();
	}

	/*
	 * protected void setNoDataText(String string) {
	 * mContentView.removeAllViews();
	 * mContentView.addView(ViewUtils.getNodata(this, string), 0, new
	 * LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
	 * LayoutParams.FILL_PARENT));
	 * 
	 * }
	 */

	/** 给HttpURLConnections添加HttpURLConnection */
	public void putHttpURLConnection(HttpURLConnection arg0) {
		mHttpURLConnections.add(arg0);
	}

	/** 得到mCContext */
	public CContext getCcontext() {
		return mCContext;

	}

	/** 断开每个HttpURLConnection */
	public void clearHttpURLConnection() {
		Iterator<HttpURLConnection> iterator = mHttpURLConnections.iterator();
		while (iterator.hasNext()) {
			HttpURLConnection HttpUrlConnection = iterator.next();
			if (HttpUrlConnection != null) {
				HttpUrlConnection.disconnect();
			}
		}
		mHttpURLConnections.clear();
	}

	/** 调用setIntent启动activity，key值自定义 */
	public void startActivityByParam(Class<?> cls, String Key, Object value) {
		if (value == null)
			return;

		startActivity(setIntent(cls, Key, value));
	}

	/** 调用setIntent启动activity */
	public void startActivityByParam(Class<?> cls, Object value) {
		if (value == null)
			return;

		startActivity(setIntent(cls, KEY, value));
	}

	/** 调用setIntent启动service */
	public void startServiceAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null)
			return;

		startService(setIntent(cls, key, value));
	}

	/** 调用setIntent发送广播 */
	public void senBroadcastAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null)
			return;

		sendBroadcast(setIntent(cls, key, value));
	}

	/** 根据key值和value的类型，放入intent中的数据 */
	private Intent setIntent(Class<?> cls, String key, Object value) {
		Intent intent = new Intent();
		intent.setClass(this, cls);

		Bundle mBundle = new Bundle();

		if (value instanceof Boolean)
			mBundle.putBoolean(key, (Boolean) value);
		else if (value instanceof String)
			mBundle.putString(key, (String) value);
		else if (value instanceof Integer)
			mBundle.putInt(key, (Integer) value);

		return intent.putExtras(mBundle);

	}

	/** 启动activity，并添加Bundle */
	public void StartActivityAttachBundle(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 启动activity */
	public void startActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);

		startActivity(intent);
	}

	/** 启动获得返回值的activity，当此antivity finish（）时，会把结果传回当前activity2 */
	public void startActivityForResult(Class<?> cls, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);

		startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动获得返回值的activity，并在activity中添加key和value， 当此antivity
	 * finish（）时，会把结果传回当前activity2
	 */
	public void startActivityForResultAttachParam(Class<?> cls, String key,
			Object value, int requestCode) {
		if (key == null || value == null)
			return;

		startActivityForResult(setIntent(cls, key, value), requestCode);
	}

	/** 启动service */
	public void startService(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startService(intent);
	}

	/** 发送广播 */
	public void sendBorast(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		sendBroadcast(intent);
	}

	/** 发送广播，并添加一个action */
	public void sendBorastByAction(String Action) {
		Intent intent = new Intent();
		intent.setAction(Action);
		sendBroadcast(intent);
	}

	/** 把一个数据封装成Message */
	public Message getMessage(String key, Object value) {

		Bundle mBundle = new Bundle();

		if (value instanceof Boolean)
			mBundle.putBoolean(key, (Boolean) value);
		else if (value instanceof String)
			mBundle.putString(key, (String) value);
		else if (value instanceof Integer)
			mBundle.putInt(key, (Integer) value);

		Message message = Message.obtain();
		message.setData(mBundle);
		return message;

	}

	/** 注册广播接收者 */
	public void mRegisterReceiver(BroadcastReceiver broadReceiver, String Action) {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Action);

		registerReceiver(broadReceiver, myIntentFilter);
	}

	/** 广播接收者取消注册 */
	public void mUnregisterReceiver(BroadcastReceiver broadReceiver) {
		unregisterReceiver(broadReceiver);

	}

	/** 得到EditText的文本值 */
	public String getText(EditText mEdittext) {

		return mEdittext.getText().toString().trim();

	}

	public BaseApplication getApl() {
		return mBaseApplication;

	}

	/** 得到intent中KEY的Boolean值 */
	protected boolean getBoolean() {

		return this.getIntent().getExtras().getBoolean(KEY);

	}

	/** 得到intent中KEY的String值 */
	protected String getString() {

		return this.getIntent().getExtras().getString(KEY);

	}

	/** 得到intent中KEY的int值 */
	protected int getInt() {

		return this.getIntent().getExtras().getInt(KEY);

	}

	/** 得到BaseActivity的包名 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 当一个窗口即将被销毁时，被发送 */
	public void onDestroy() {
		clearHttpURLConnection();
		tack.removeActivity(this);
		super.onDestroy();
	}

}
