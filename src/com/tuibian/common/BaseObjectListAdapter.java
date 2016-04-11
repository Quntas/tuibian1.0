package com.tuibian.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tuibian.main.BaseApplication;
import com.tuibian.model.CBaseObject;

public class BaseObjectListAdapter extends BaseAdapter {

	protected BaseApplication mApplication;
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<? extends CBaseObject> mDatas = new ArrayList<CBaseObject>();

	public BaseObjectListAdapter(Context context,
			List<? extends CBaseObject> datas) {

		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null) {
			mDatas = datas;
		}
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public List<? extends CBaseObject> getDatas() {
		return mDatas;
	}

}
