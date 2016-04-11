package com.tuibian.business.model;

import com.tuibian.common.CGlobal;

public class CDataManager {
	private CMsgMgr MsgMgr;
	public CMsgMgr GetMsgMgr() {
		if (MsgMgr == null) {
			MsgMgr = new CMsgMgr();
			MsgMgr.Ctx = CGlobal.GetCtx();
			MsgMgr.Load("", false);
		}
		return MsgMgr;
	}
}
