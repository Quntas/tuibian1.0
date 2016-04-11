package com.tuibian.common;

import android.content.Context;

import com.tuibian.business.model.CDataManager;
import com.tuibian.model.CContext;
import com.tuibian.model.CUser;

//全局类
public class CGlobal {
	public static Context mContext;
	public static CContext m_Contex = null;
	public static CDataManager m_DataManager = null;
	//public static String SERVICE_ADDR = "http://221.8.77.120/Wixin";
	public static String SERVICE_ADDR = "http://118.126.142.42/UserWeb";
	public static CUser m_User = null; //当前登录用户
	public static String m_Nonce = "tuibian2016&^$*W!#%11@";
	public static Boolean isHeadImageDownloaded = false;
	public static String m_Mobile = "";
	//end

	// 获取全局上下文
	public static CContext GetCtx() {

		if (m_Contex == null)
			m_Contex = new CContext(mContext);

		return m_Contex;
	}

	public static void setCContext() {
		m_Contex = new CContext(mContext);

	}

	public static CDataManager GetDataManager() {

		if (m_DataManager == null)
			m_DataManager = new CDataManager();

		return m_DataManager;
	}
	public static void ResetDataManager() {
		m_DataManager = new CDataManager();
	}
}
