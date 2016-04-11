package com.tuibian.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

	/**
	 * 判断当前网络连接状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean IsNetConnected(Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnectedOrConnecting();
		}
		return false;
	}

}
