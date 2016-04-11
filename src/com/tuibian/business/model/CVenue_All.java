package com.tuibian.business.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CVenue_All {
	public String Id;
	public String Name;
	public String ImgUrl;
	public String IconUrl;
	public CVenue_All() {
		Id = "111";
		Name="aaa";
		ImgUrl = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
		IconUrl = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
		// TODO Auto-generated constructor stub
	}

	public CVenue_All(JSONObject jsonobj) {
		try {
			Id = jsonobj.getString("spty_id");
			Name = jsonobj.getString("spty_name");
			ImgUrl = jsonobj.optString("spty_img");
			IconUrl = jsonobj.optString("spty_icon");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
