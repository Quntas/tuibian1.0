package com.tuibian.business.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CTribe_Latest {

	public String Id;
	public String ImgUrl1;//小头像
	public String Name;
	public String ImgUrl2;//大头像
	public String ImgUrl3;//点赞
	public String ImgUrl4;//评论

	public CTribe_Latest() {
		//目前测试用
		ImgUrl1 = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
		ImgUrl2 = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
		ImgUrl3 = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
		ImgUrl4 = "http://aimage.quyundong.com/uploads/20150909/2015091441794566972558205027.jpg";
		
	}

	public CTribe_Latest(JSONObject jsonobj) {
		//这需要外面传
		try {
			Id = jsonobj.getString("id");
			Name = jsonobj.getString("Name");
			ImgUrl1 = jsonobj.optString("ImgUrl1");
			ImgUrl2 = jsonobj.getString("ImgUrl2");
			ImgUrl3 = jsonobj.getString("ImgUrl3");
			ImgUrl4 = jsonobj.getString("ImgUrl4");
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
