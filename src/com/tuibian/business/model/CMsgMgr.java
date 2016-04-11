package com.tuibian.business.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tuibian.common.CGlobal;
import com.tuibian.model.CBaseObject;
import com.tuibian.model.CBaseObjectMgr;
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;


public class CMsgMgr extends CBaseObjectMgr {

	@Override
	public Boolean GetListPO(String sWhere, List<String> cmdParas,
			String sOrderby) {
		// TODO Auto-generated method stub
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from Msg ");

		if (sWhere != null && sWhere.length() > 0) {
			sqlBuilder.append(" where ");
			sqlBuilder.append(sWhere);
		}
		if (sOrderby != null && sOrderby.length() > 0) {
			sqlBuilder.append(" order by ");
			sqlBuilder.append(sOrderby);
		} else
			sqlBuilder.append(" order by Created");

		SQLiteDatabase db = Ctx.getReadableDatabase();
		Cursor cursor;
		if (cmdParas != null && cmdParas.size() > 0) {
			String[] arr = new String[cmdParas.size()];
			cursor = db.rawQuery(sqlBuilder.toString(), cmdParas.toArray(arr));
		} else {
			cursor = db.rawQuery(sqlBuilder.toString(), null);
		}

		while (cursor.moveToNext()) {
			CMsg obj = new CMsg();
			obj.Ctx = Ctx;
			obj.Id = UUID.fromString(cursor.getString(cursor
					.getColumnIndex("Id")));

			obj.Title = cursor.getString(cursor.getColumnIndex("Title"));
			obj.Url = cursor.getString(cursor.getColumnIndex("Url"));
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
	public void AddMsg(CMsg msg) {
		AddNew(msg,true);
	}

	@Override
	public List<CBaseObject> Download(int iOrderBy/* 0顺序；1倒序 */) {
		// TODO Auto-generated method stub
		String sUrl = CGlobal.SERVICE_ADDR
				+ "GetMsgListServlet";

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		map.put("OrderBy", iOrderBy);
		map.put("LastUpdated", format.format(m_dtimeLastUpdated));

		List<CBaseObject> lstRet = new ArrayList<CBaseObject>();
		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);
			long Status = jsonobj.getLong("Status");
			if (Status == 0)
				return null;
			JSONArray entitys = jsonobj.getJSONArray("Ret");
			for (int i = 0; i < entitys.length(); i++) {
				JSONObject jsonobj2 = entitys.getJSONObject(i);
				CMsg obj = new CMsg(jsonobj2);
				if (Find(obj.Id) != null)
					continue;
				obj.Ctx = Ctx;

				lstRet.add(obj);
				AddNew(obj);

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
