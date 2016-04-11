package com.tuibian.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tuibian.model.CBaseObject;
import com.tuibian.model.CBaseObjectMgr;


public class CVenue { //extends CBaseObject {
	public String Id;
	public String Name;
	public String Address;
	public String Latitude ;
	public String Longitude ;
	public String Distance;
	public String Price;
	public String ImgUrl;

	public CVenue() {
		Latitude = "";
		Longitude = "";
		ImgUrl = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
	}

	public CVenue(JSONObject jsonobj) {
		try {
			Id = jsonobj.getString("id");
			Name = jsonobj.getString("Name");
			Address = jsonobj.getString("Address");
			Latitude = jsonobj.optString("Latitude");
			Longitude = jsonobj.optString("Longitude");
			Distance = jsonobj.optString("Distance");
			ImgUrl = jsonobj.optString("ImgUrl");
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
