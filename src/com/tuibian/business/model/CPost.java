package com.tuibian.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tuibian.model.CBaseObject;
import com.tuibian.model.CBaseObjectMgr;


public class CPost { //extends CBaseObject {
	public String Id;
	public String Name;
	public String Sign;
	public String Time;
	public String ImgUrl;

	public CPost() {
		ImgUrl = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
	}

	public CPost(JSONObject jsonobj) {
		try {
			Id = jsonobj.getString("id");
			Name = jsonobj.getString("Name");
			Sign = jsonobj.getString("Sign");
			ImgUrl = jsonobj.optString("ImgUrl");
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
