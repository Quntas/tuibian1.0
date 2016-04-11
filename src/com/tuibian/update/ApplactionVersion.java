package com.tuibian.update;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class ApplactionVersion {

	public static VisionInfo getVersionInfo() throws Exception {

		String Url = "http://221.8.77.120/Wixin//keditor/plugins/link/ver.json";

		URL url = new URL(Url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {

			InputStream inputstream = conn.getInputStream();
			return parseJSON(inputstream);

		}
		return null;

	}

	public static VisionInfo parseJSON(InputStream instream) throws Exception {

		VisionInfo mVisionInfo = new VisionInfo();
		byte[] json = StreamTool.read(instream);
		String string = new String(json, "utf-8");

		JSONObject dataJson = new JSONObject(string);

		mVisionInfo.AndroidVer = dataJson.getString("AndroidVer");
		mVisionInfo.AndroidUrl = dataJson.getString("AndroidUrl");
		mVisionInfo.AndroidUpdate = dataJson.getString("AndroidUpdate");// 1 强制
																		// //0

		return mVisionInfo;

	}

}
