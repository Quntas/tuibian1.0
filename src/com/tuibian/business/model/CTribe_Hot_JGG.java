package com.tuibian.business.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CTribe_Hot_JGG {

	public String Id;
	public String ImgUrl1;// 图片

	public CTribe_Hot_JGG() {
		// 目前测试用
		ImgUrl1 = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";

	}

	public CTribe_Hot_JGG(JSONObject jsonobj) {
		// 这需要外面传
		try {
			Id = jsonobj.getString("id");
			ImgUrl1 = jsonobj.optString("ImgUrl1");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
