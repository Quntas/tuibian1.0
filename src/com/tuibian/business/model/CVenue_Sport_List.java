package com.tuibian.business.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CVenue_Sport_List {
	public String Sport_name;
	public String Sport_price;
	public CVenue_Sport_List() {
		Sport_name = "运动项目";
		Sport_price = "运动价格";
	}
	public CVenue_Sport_List(JSONObject jsonobj) {
		try {
			Sport_name = jsonobj.getString("vesp_price");
			Sport_price = jsonobj.optString("spty_title");
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
