package com.tuibian.model;

import java.util.ArrayList;
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
import com.tuibian.util.CommUtils;
import com.tuibian.util.JSONUtil;

public class CUserMgr extends CBaseObjectMgr {

	@Override
	public Boolean GetListPO(String sWhere, List<String> cmdParas,
			String sOrderby) {
		// TODO Auto-generated method stub
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from B_User ");

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

		int idxId = 0, idxName = 0, idxPwd = 0, idxType = 0, idxTName = 0, idxSex = 0, idxQQ = 0, idxEmail = 0, idxPhone = 0, idxmqttid = 0, idxCreated = 0, idxCreator = 0, idxUpdated = 0, idxUpdator = 0;
		if (cursor.getCount() >= 0) {
			idxId = cursor.getColumnIndex("Id");
			idxName = cursor.getColumnIndex("Name");
			idxPwd = cursor.getColumnIndex("Pwd");
			idxType = cursor.getColumnIndex("Type");
			idxTName = cursor.getColumnIndex("TName");
			idxSex = cursor.getColumnIndex("Sex");
			idxQQ = cursor.getColumnIndex("QQ");
			idxEmail = cursor.getColumnIndex("Email");
			idxPhone = cursor.getColumnIndex("Phone");
			idxmqttid = cursor.getColumnIndex("mqttid");
			idxCreated = cursor.getColumnIndex("Created");
			idxCreator = cursor.getColumnIndex("Creator");
			idxUpdated = cursor.getColumnIndex("Updated");
			idxUpdator = cursor.getColumnIndex("Updator");
		}
		while (cursor.moveToNext()) {
			CUser obj = new CUser();
			obj.Ctx = Ctx;
			obj.Id = UUID.fromString(cursor.getString(idxId));
			obj.Name = cursor.getString(idxName);
			obj.Pwd = cursor.getString(idxPwd);
			obj.Type = cursor.getInt(idxType);
			obj.TName = cursor.getString(idxTName);
			obj.Sex = cursor.getString(idxSex);
			obj.QQ = cursor.getString(idxQQ);
			obj.Email = cursor.getString(idxEmail);
			obj.Phone = cursor.getString(idxPhone);
			obj.mqttid = cursor.getString(idxmqttid);

			obj.Created = new Date(cursor.getLong(idxCreated));
			obj.Creator = UUID.fromString(cursor.getString(idxCreator));
			obj.Updated = new Date(cursor.getLong(idxUpdated));
			obj.Updator = UUID.fromString(cursor.getString(idxUpdator));

			obj.m_ObjectMgr = this;
			m_lstObj.add(obj);
			m_sortObj.put(obj.Id, obj);

			if (m_dtimeLastUpdated.before(obj.Updated))
				m_dtimeLastUpdated = obj.Updated;
		}
		cursor.close();

		return true;
	}

	@Override
	public List<CBaseObject> Download(int iOrderBy/* 0顺序；1倒序 */) {
		return null;
	}

	// 查询单个用户信息
	public CUser SearchUserInfo(UUID FriendId) {
		// TODO Auto-generated method stub
		//String sUrl = CGlobal.SERVICE_ADDR + "/UserSearchServlet";
		String sUrl = CGlobal.SERVICE_ADDR
				+ "/Security/Service/SearchUserInfo.ashx";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		map.put("FriendId", FriendId.toString());

		CUser User = null;
		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);
			long Status = jsonobj.getLong("Status");
			if (Status == 0)
				return null;
			JSONObject entitys = jsonobj.getJSONObject("Ret");
			CUser obj = new CUser(entitys);
			obj.Ctx = Ctx;

			User = obj;
			Delete(obj.Id);
			AddNew(obj);
			Save(); // 保存到本地数据库

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return User;
	}

	// 注册
	public JSONObject Reg(String UserInfo) {
		return Reg(UserInfo, CGlobal.m_Nonce);
	}

	public static JSONObject Reg(String UserInfo, String Nonce) {
		// TODO Auto-generated method stub
		//String sUrl = CGlobal.SERVICE_ADDR + "/UserRegServlet";
		String sUrl = CGlobal.SERVICE_ADDR + "/Security/Service/Reg.ashx";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UserInfo", UserInfo);
		map.put("Nonce", Nonce);

		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);

			long Status = jsonobj.getLong("Status");
			if (Status == 1) {
				JSONObject jsonUserInfo = JSONUtil.toObj(UserInfo);
				CUser obj = new CUser(jsonUserInfo);

				JSONObject entitys = jsonobj.getJSONObject("Ret");
				obj.Id = UUID.fromString(entitys.getString("id"));

				CGlobal.m_User = obj;
			}

			return jsonobj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 登录
	public static JSONObject Login(String Name, String Pwd, int Type,
			String VCode) {

		String sUrl = "http://192.168.238.106:8080/sshe/api/register!validate.sy";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data.userMobile", Name);
		map.put("dat.pwd", Pwd);
		map.put("sms", "123");
		try {
			String sContent = CommUtils.doPost(sUrl, map);

			JSONObject jsonobj = JSONUtil.toObj(sContent);

			return jsonobj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 修改密码
	public JSONObject ModPwd(String NewPwd) {
		// TODO Auto-generated method stub
		//String sUrl = CGlobal.SERVICE_ADDR + "/UserChangePwdServlet";
		String sUrl = CGlobal.SERVICE_ADDR + "/Security/Service/ModPwd.ashx";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Uid", CGlobal.m_User.Id.toString());
		map.put("Pwd", CGlobal.m_User.Pwd);
		map.put("NewPwd", NewPwd);

		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);

			return jsonobj;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// 修改用户资料
	public JSONObject UpdateUserInfo(String UserInfo) {
		// TODO Auto-generated method stub
		//String sUrl = CGlobal.SERVICE_ADDR + "/SetUserInfoServlet";
		String sUrl = CGlobal.SERVICE_ADDR
				+ "/Security/Service/UpdateUserInfo.ashx";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		map.put("UserInfo", UserInfo);

		try {
			String sContent = CommUtils.doPost(sUrl, map);
			JSONObject jsonobj = JSONUtil.toObj(sContent);

			return jsonobj;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// 查询用户
	public List<CBaseObject> SearchUser(String Keyword) {
		String sUrl = CGlobal.SERVICE_ADDR
				+ "/Security/Service/SearchUser.ashx";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		map.put("Keyword", Keyword);

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
				CUser obj = new CUser(jsonobj2);
				obj.Ctx = Ctx;

				lstRet.add(obj);
				// AddNew(obj);

				if (m_dtimeLastUpdated.before(obj.Updated))
					m_dtimeLastUpdated = obj.Updated;
			}
			// Save(); //保存到本地数据库

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return lstRet;
	}

	// 获取多个用户
	public List<CBaseObject> GetUserList(List<UUID> lstId) {
		// TODO Auto-generated method stub
		String sUrl = CGlobal.SERVICE_ADDR
				+ "/Security/Service/GetUserList.ashx";

		String ids = "";
		for (int i = 0; i < lstId.size(); i++) {
			if (i != lstId.size() - 1)
				ids += lstId.get(i).toString() + ",";
			else
				ids += lstId.get(i).toString();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", CGlobal.m_User.token.toString());
		map.put("ids", ids);

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
				CUser obj = new CUser(jsonobj2);
				obj.Ctx = Ctx;

				lstRet.add(obj);
				AddNew(obj);

				if (m_dtimeLastUpdated.before(obj.Updated))
					m_dtimeLastUpdated = obj.Updated;
			}
			Save(); // 保存到本地数据库

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return lstRet;
	}

}
