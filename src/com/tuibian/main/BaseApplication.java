package com.tuibian.main;

import android.app.Application;
import android.os.Handler;

import com.tuibian.common.CGlobal;

public class BaseApplication extends Application {

	// /** 用于service和Activity之间的通信 。 */
	// private Handler m_handler = null;

	/** 用于向服务器发送消息的Handler */
	private Handler mSendMsgHandler;

	public Handler getSendMsgHandler() {
		return mSendMsgHandler;
	}

	public void setSendMsgHandler(Handler handler) {
		mSendMsgHandler = handler;
	}

	// public Handler getHandler() {
	// return m_handler;
	// }
	//
	// public void setHandler(Handler handler) {
	// m_handler = handler;
	// }

	/** 加载上下文对象 */
	public void onCreate() {

		super.onCreate();

		CGlobal.mContext = this.getApplicationContext();

	}

}
