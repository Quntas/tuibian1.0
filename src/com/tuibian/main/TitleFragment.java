package com.tuibian.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuibian.R;

public class TitleFragment extends Fragment {

	private TextView tv_title;
	public ImageView iv_help;
	private Button btn_submit;
	private RelativeLayout layout_head;
	public ImageView btnBack;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.title_fragment, null);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		iv_help = (ImageView) view.findViewById(R.id.iv_help);
		btn_submit = (Button) view.findViewById(R.id.btn_commit);
		layout_head = (RelativeLayout) view.findViewById(R.id.layout_head);
		btnBack = (ImageView) view.findViewById(R.id.iv_back);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		return view;
	}

	/** 得到TextView */
	public TextView getTitleView() {
		return tv_title;
	}

	/***
	 * 设置textview的文本
	 * 
	 * @param text
	 */
	public void setTitleText(CharSequence text) {
		tv_title.setText(text);
	}

	/**
	 * 设置textview的文本
	 * 
	 * @param resId
	 *            通过Id
	 */
	public void setTitleText(int resId) {
		tv_title.setText(resId);
	}

	/** 得到 relative的高度 */
	public int getTitleHeight() {
		return layout_head.getHeight();
	}

	/** 隐藏返回按钮 */
	public void hideBackBtn() {
		btnBack.setVisibility(View.GONE);
	}

	/** 隐藏整个title */
	public void hideAll() {
		layout_head.setVisibility(View.GONE);
	}

	/** 显示整个title */
	public void showAll() {
		layout_head.setVisibility(View.VISIBLE);
	}

	/** 为返回按钮添加监听器 */
	public void setBackClickLisntener(View.OnClickListener listener) {
		if (listener != null) {
			btnBack.setOnClickListener(listener);
		}
	}

	/** 设置图片背景，显示问题按钮并添加监听器。 */
	public void showQuestion(View.OnClickListener listener) {
		iv_help.setVisibility(View.VISIBLE);
		if (listener != null) {
			iv_help.setOnClickListener(listener);
		}
	}

	/** 重载方法， 设置图片背景，显示问题按钮并添加监听器。srcID为图片地址 */
	public void showQuestion(int srcId, View.OnClickListener listener) {
		iv_help.setImageResource(srcId);
		iv_help.setVisibility(View.VISIBLE);
		if (listener != null) {
			iv_help.setOnClickListener(listener);
		}
	}

	/** 显示提交按钮并添加监听器 */
	public void showSubmitBtn(View.OnClickListener listener) {
		btn_submit.setVisibility(View.VISIBLE);
		if (listener != null) {
			btn_submit.setOnClickListener(listener);
		}
	}

	/** 设置提交按钮的文本 */
	public void replaceSubmitText(CharSequence text) {
		if (btn_submit != null && !TextUtils.isEmpty(text)) {
			btn_submit.setText(text);
		}
	}

	/** 设置提交按钮是否可见，b=true为可见 */
	public void hideRight(boolean b) {

		btn_submit.setVisibility(!b ? View.VISIBLE : View.INVISIBLE);

	}

	/** 设置标题头的背景 */
	public void setBackground(int r) {

		view.setBackgroundResource(r);
	}

}
