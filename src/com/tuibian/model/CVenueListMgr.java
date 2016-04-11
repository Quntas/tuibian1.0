package com.tuibian.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;

public class CVenueListMgr extends CBaseObjectMgr {

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
	public static JSONObject VenueList (String city,String type,double lonngitude,double latitude,String uuid) {

		String sUrl = "http://192.168.238.149:8080/AppServer/api/venue/venues";
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("device_uuid", uuid);
		map.put("spty_id", type);
		map.put("venu_city", city);
		map.put("device_longitude", lonngitude);
		map.put("device_latitude", latitude);
		
		try {
			String sContent = CommUtils.doPost(sUrl, map);

			JSONObject jsonobj = JSONUtil.toObj(sContent);
			
			Log.w("WYQ", jsonobj+"");

			return jsonobj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	public static JSONObject Venueinfo (String id) {

		String sUrl = "http://192.168.238.149:8080/AppServer/api/venue/detail";
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("venu_id", id);
		try {
			String sContent = CommUtils.doPost(sUrl, map);

			JSONObject jsonobj = JSONUtil.toObj(sContent);
			
			Log.w("WYQS", jsonobj+"");

			return jsonobj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	public static JSONObject VenueCommentsList (String id) {

		String sUrl = "http://192.168.238.149:8080/AppServer/api/venue/comments";
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("venu_id", id);
		try {
			String sContent = CommUtils.doPost(sUrl, map);

			JSONObject jsonobj = JSONUtil.toObj(sContent);
			
			Log.w("WYQS", jsonobj+"");

			return jsonobj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	

}
