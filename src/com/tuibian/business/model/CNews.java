package com.tuibian.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tuibian.model.CBaseObject;
import com.tuibian.model.CBaseObjectMgr;


public class CNews { //extends CBaseObject {
	public String Id;
	public String Title;
	public String Desc;
	public String ImgUrl;
	public String Sort;
	public String Time;
	public String Fans;
	public String Url;

	public CNews() {
		Id = "";
		Title = "";
		Desc = "";
		Sort = "";
		Fans = "";
		Url =  "http://m.sohu.com";
		Time  = "2016-03-07";
		ImgUrl = "http://i1.itc.cn/20141216/556_745ee2fe_4fe0_d0a7_5b02_577b3e698130_1.jpg";
	}

	public CNews(JSONObject jsonobj) {
		try {
			Id = jsonobj.getString("id");
			
			Title = jsonobj.getString("Title");
			Desc = jsonobj.getString("Desc");
			Sort = jsonobj.getString("Sort");
			Fans = jsonobj.getString("Fans");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
