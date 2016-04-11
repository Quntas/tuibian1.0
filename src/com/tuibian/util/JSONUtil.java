package com.tuibian.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JSONUtil {
	public interface IJSONEntity {
		public JSONObject toJSON();

	}

	public static String toJSON(List<? extends IJSONEntity> obj) {
		JSONArray array = new JSONArray();
		for (IJSONEntity entity : obj)
			array.put(entity.toJSON());
		return array.toString();
	}

	public static JSONObject toObj(String json) {
		if (json == null)
			return null;

		try {
			return new JSONObject(json);
		} catch (Exception e) {
			Log.e("JSONUtil", e.getMessage());
			return null;
		}
	}

	public static String getUserId(JSONObject entitys) {
		if (entitys == null)
			return null;
		try {

			JSONObject m_entity = entitys.getJSONObject("Ret");

			return m_entity.getString("id");
		} catch (Exception e) {
			Log.e("JSONUtil", e.getMessage());
			return null;
		}
	}
}
