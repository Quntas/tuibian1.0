package com.tuibian.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.tuibian.main.BaseApplication;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CContext;
import com.tuibian.util.ToastUtil;
import com.tuibian.util.VerificationUtils;

public class BaseFragment extends Fragment {
	public final static String KEY = "KEY";
	public final static String UnNet = "没有网络连接，请检查！";
	public final static String Hint = "提示";

	private CContext mCContext;
	private Activity mContext;
	private BaseApplication mBaseApplication;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCContext = CGlobal.GetCtx();
		mBaseApplication = (BaseApplication) mContext.getApplication();

	}

	public BaseApplication getApl() {
		return mBaseApplication;

	}

	public CContext getCcontext() {
		return mCContext;

	}

	public Context getContext() {
		return mContext;

	}

	/** key取值时用。 value。1.boolean参数。2.String。3.int类型。 */
	public void startServiceAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null)
			return;

		mContext.startService(getIntent(cls, key, value));
	}

	/** key取值时用。 value。1.boolean参数。2.String。3.int类型。 */
	public void senBroadcastAttachParam(Class<?> cls, String key, Object value) {
		if (key == null || value == null)
			return;

		mContext.sendBroadcast(getIntent(cls, key, value));
	}

	private Intent getIntent(Class<?> cls, String key, Object value) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);

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
	public void startService(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);
		mContext.startService(intent);
	}

	/** 含有Bundle通过Action跳转界面 **/
	public void sendBorast(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);
		mContext.sendBroadcast(intent);
	}

	/** 含有Bundle通过Action跳转界面 **/
	public void sendBorastByAction(String Action) {
		Intent intent = new Intent();
		intent.setAction(Action);
		mContext.sendBroadcast(intent);
	}

	/** key取值时用。 value。1.boolean参数。2.String。3.int类型。 */
	public void startActivityByParam(Class<?> cls, Object value) {
		if (value == null)
			return;

		startActivity(getIntent(cls, KEY, value));
	}

	public void mToast(String arg0) {
		if (arg0 != null)
			ToastUtil.showShortToast(mContext, arg0);

	}

	public String getText(EditText mEdittext) {

		return mEdittext.getText().toString().trim();

	}

	/** 判断一个集合是否为空。 空返回false 否则返回true */
	public boolean isEmpty(List<? extends CBaseObject>... lst) {
		List<CBaseObject> list = null;
		for (int i = 0; i < lst.length; i++) {

			list = (List<CBaseObject>) lst[i];

			if ((list != null || list.size() != 0)) {
				return false;
			}

		}

		return true;

	}

	public boolean isEmpty(String... texts) {
		return VerificationUtils.isEmpty(texts);
	}

	/** 把后一个集合追加到前面一个集合。 */
	public void cleanAll(List<CBaseObject> lstObj, List<CBaseObject> lst) {

		if (lstObj == null)
			lstObj = new ArrayList<CBaseObject>();

		if (lst != null) {

			lstObj.clear();
			lstObj.addAll(lst);
		}
	}

	public boolean isEmpty(List<? extends CBaseObject> lst) {
		if (lst == null || lst.isEmpty())
			return true;
		return false;

	}

	public boolean isEmpty(List<? extends CBaseObject> lst, int Postion) {

		if (lst == null || lst.isEmpty())
			return true;
		else {
			try {
				CBaseObject myLst = (CBaseObject) lst.get(Postion);
				if (myLst == null)
					return true;
			} catch (Exception e) {
				return true;

			}

		}
		return false;

	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}
}
