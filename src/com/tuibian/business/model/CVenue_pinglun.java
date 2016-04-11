package com.tuibian.business.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CVenue_pinglun {
	public String uesrName;
	public String ImgUrl;
	public String Date ;
	public String content ;
	public CVenue_pinglun() {
		uesrName = "用户1";
		Date = "2016-2-29";
		content="好";
		ImgUrl = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
	}
	public CVenue_pinglun(JSONObject jsonobj) {
		try {
			uesrName = jsonobj.getString("uesrName");
			Date = jsonobj.getString("Date");
			content = jsonobj.optString("content");
			ImgUrl = jsonobj.optString("ImgUrl");
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
