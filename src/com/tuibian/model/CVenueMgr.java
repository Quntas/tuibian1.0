package com.tuibian.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;

public class CVenueMgr extends CBaseObjectMgr {
	
	@Override
	public Boolean GetListPO(String sWhere, List<String> cmdParas,
			String sOrderby) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CBaseObject> Download(int iOrderBy) {
		// TODO Auto-generated method stub
		return null;
	}
	public static JSONObject VenueTypes () {

		String sUrl = "http://192.168.238.149:8080/AppServer/api/venue/types";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);
			return jsonobj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	
}
