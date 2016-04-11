package com.tuibian.common;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tuibian.main.BaseApplication;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CContext;
import com.tuibian.util.ActivityTackUtils;
import com.tuibian.util.ToastUtil;

public abstract class BaseFragmentActivity extends FragmentActivity {
	public final static String KEY = "KEY";
	public int mScreenWidth;
	public int mScreenHeight;
	public InputMethodManager imm;

	public String[] mStrings;

	public int mScreewidth;
	public int mScreeheight;
	protected List<HttpURLConnection> mHttpURLConnections = new ArrayList<HttpURLConnection>();
	public ActivityTackUtils tack = ActivityTackUtils.getInstanse();
	public CContext mContext;
	public BaseApplication mBA;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBA = (BaseApplication) this.getApplication();
		mContext = CGlobal.GetCtx();
		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		mScreewidth = wm.getDefaultDisplay().getWidth();
		mScreeheight = wm.getDefaultDisplay().getHeight();

		tack.addActivity(this);
	}

	public void onDestroy() {
		clearHttpURLConnection();
		tack.removeActivity(this);
		super.onDestroy();
	}

	/** 初始化视图 **/
	protected abstract void initTitleBar();

	/** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();

	public void putHttpURLConnection(HttpURLConnection arg0) {
		mHttpURLConnections.add(arg0);
	}

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

	/** key取值时用。 value。1.boolean参数。2.String。3.int类型。 */
	public void startActivityAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null) {
			return;
		}

		startActivity(getIntent(cls, key, value));
	}

	/** key取值时用。 value。1.boolean参数。2.String。3.int类型。 */
	public void mStartServiceAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null) {
			return;
		}

		startService(getIntent(cls, key, value));
	}

	/** key取值时用。 value。1.boolean参数。2.String。3.int类型。 */
	public void mSendBorastAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null) {
			return;
		}
		sendBroadcast(getIntent(cls, key, value));
	}

	private Intent getIntent(Class<?> cls, String key, Object value) {
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

	/** 含有Bundle通过Action跳转界面 **/
	public void mStartActivityAttachBundle(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 含有Bundle通过Action跳转界面 **/
	public void mStartActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);

		startActivity(intent);
	}

	public void startActivityForResult(Class<?> cls, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);

		startActivityForResult(intent, requestCode);
	}

	public void startActivityForResultAttachParam(Class<?> cls, String key,
			Object value, int requestCode) {
		if (key == null || value == null)
			return;

		startActivityForResult(getIntent(cls, key, value), requestCode);
	}

	/** 含有Bundle通过Action跳转界面 **/
	public void mStartService(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		startService(intent);
	}

	/** 含有Bundle通过Action跳转界面 **/
	public void mSendBorast(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		sendBroadcast(intent);
	}

	public void mToast(String arg0) {
		if (arg0 != null)
			ToastUtil.showShortToast(this, arg0);

	}

	public void mRegisterReceiver(BroadcastReceiver broadReceiver, String Action) {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Action);

		registerReceiver(broadReceiver, myIntentFilter);
	}

	public void mUnregisterReceiver(BroadcastReceiver broadReceiver) {
		unregisterReceiver(broadReceiver);

	}

	public String getText(EditText mEdittext) {

		return mEdittext.getText().toString().trim();

	}

	protected boolean getBoolean() {

		return this.getIntent().getExtras().getBoolean(KEY);

	}

	protected String getString() {

		return this.getIntent().getExtras().getString(KEY);

	}

	protected int getInt() {

		return this.getIntent().getExtras().getInt(KEY);

	}

	protected View fId(int id) {
		return this.findViewById(id);

	}

	/** 判断一个集合是否为空。 空返回false 否则返回true */
	public boolean isEmpty(List<CBaseObject>... lst) {
		List<CBaseObject> list = null;
		for (int i = 0; i < lst.length; i++) {

			list = lst[i];

			if (list == null || list.size() == 0) {
				return false;
			}

		}

		return true;

	}

	/** 把后一个集合追加到前面一个集合。 */
	public void AddAll(List<CBaseObject> lstObj, List<CBaseObject> lst) {

		if (lstObj == null)
			lstObj = new ArrayList<CBaseObject>();

		if (lst != null) {

			lstObj.clear();
			lstObj.addAll(lst);
		}
	}
}
