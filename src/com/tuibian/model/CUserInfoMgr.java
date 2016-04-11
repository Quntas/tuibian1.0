package com.tuibian.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tuibian.common.CGlobal;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;

public class CUserInfoMgr extends CBaseObjectMgr {

	@Override
	public Boolean GetListPO(String sWhere, List<String> cmdParas,
			String sOrderby) {
		// TODO Auto-generated method stub
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from UserInfo ");

		if (sWhere != null && sWhere.length() > 0) {
			sqlBuilder.append(" where ");
			sqlBuilder.append(sWhere);
		}
		if (sOrderby != null && sOrderby.length() > 0) {
			sqlBuilder.append(" order by ");
			sqlBuilder.append(sOrderby);
		} else
			sqlBuilder.append(" order by Created");

		Log.e("Ctx .getReadableDatabase()", "" + Ctx.getReadableDatabase());
		SQLiteDatabase db = Ctx.getReadableDatabase();
		Cursor cursor;
		if (cmdParas != null && cmdParas.size() > 0) {
			String[] arr = new String[cmdParas.size()];
			cursor = db.rawQuery(sqlBuilder.toString(), cmdParas.toArray(arr));
		} else {
			cursor = db.rawQuery(sqlBuilder.toString(), null);
		}

		while (cursor.moveToNext()) {
			CUserInfo obj = new CUserInfo();
			obj.Ctx = Ctx;
			obj.Id = UUID.fromString(cursor.getString(cursor
					.getColumnIndex("Id")));
			obj.B_User_id = UUID.fromString(cursor.getString(cursor
					.getColumnIndex("B_User_id")));
			obj.CertNo = cursor.getString(cursor.getColumnIndex("CertNo"));
			obj.CardNo = cursor.getString(cursor.getColumnIndex("CardNo"));
			obj.StartDate = new Date(cursor.getLong(cursor
					.getColumnIndex("StartDate")));
			obj.EndDate = new Date(cursor.getLong(cursor
					.getColumnIndex("EndDate")));
			
			obj.BloodType = cursor
					.getString(cursor.getColumnIndex("BloodType"));
			obj.Nation = cursor.getString(cursor.getColumnIndex("Nation"));
			obj.Remarks = cursor.getString(cursor.getColumnIndex("Remarks"));

			obj.Created = new Date(cursor.getLong(cursor
					.getColumnIndex("Created")));
			obj.Creator = UUID.fromString(cursor.getString(cursor
					.getColumnIndex("Creator")));
			obj.Updated = new Date(cursor.getLong(cursor
					.getColumnIndex("Updated")));
			obj.Updator = UUID.fromString(cursor.getString(cursor
					.getColumnIndex("Updator")));

			obj.m_ObjectMgr = this;
			m_lstObj.add(obj);
			m_sortObj.put(obj.Id, obj);

			if (m_dtimeLastUpdated.before(obj.Updated))
				m_dtimeLastUpdated = obj.Updated;
		}
		cursor.close();

		return true;
	}

	public List<CBaseObject> Download(int iOrderBy/* 0顺序；1倒序 */) {

		//String sUrl = CGlobal.SERVICE_ADDR + "/GetUserInfoServlet";
		String sUrl = "http://118.126.142.43"
				+ "/Health/Service/GetUserInfoEx.ashx";

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		map.put("UserId", m_Parent.Id.toString());
		map.put("LastUpdated", format.format(m_dtimeLastUpdated));

		List<CBaseObject> lstRet = new ArrayList<CBaseObject>();
		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);
			long Status = jsonobj.getLong("Status");
			if (Status == 0)
				return null;
			this.RemoveAll();
			Save();
			JSONArray entitys = jsonobj.getJSONArray("Ret");
			for (int i = 0; i < entitys.length(); i++) {
				JSONObject jsonobj2 = entitys.getJSONObject(i);
				CUserInfo obj = new CUserInfo(jsonobj2);
				obj.Ctx = Ctx;

				lstRet.add(obj);

				this.AddNew(obj);

				if (m_dtimeLastUpdated.before(obj.Updated))
					m_dtimeLastUpdated = obj.Updated;
			}
			Save();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return lstRet;
	}

}
