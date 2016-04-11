package com.tuibian.util;

import com.tuibian.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast;

	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
			mToast = null;// toast隐藏后，将其置为null
		}
	};

	public static void showShortToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.where_custom_toast, null);// 自定义布局
		TextView text = (TextView) view.findViewById(R.id.toast_message);// 显示的提示文字
		text.setText(message);
		mHandler.removeCallbacks(r);
		if (mToast == null) {// 只有mToast==null时才重新创建，否则只需更改提示文字
			mToast = new Toast(context);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.BOTTOM, 0, 150);
			mToast.setView(view);
		}
		mHandler.postDelayed(r, 1000);// 延迟1秒隐藏toast
		mToast.show();
	}

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
}
