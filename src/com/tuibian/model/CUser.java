package com.tuibian.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tuibian.common.CGlobal;

public class CUser extends CBaseObject {
	public String Name;
	public String Pwd;
	public int Type;
	public String TName;
	public String Sex;
	public String Birthday;
	public String Height;
	public String Weight;
	public String QQ;
	public String Email;
	public String Phone;
	public String mqttid;// IM即时通讯id
	public String token; // 登录令牌

	private CUserInfoMgr userInfoMgr;

	public CUserInfoMgr GetUserInfoMgr() {
		if (userInfoMgr == null) {
			userInfoMgr = new CUserInfoMgr();
			userInfoMgr.Ctx = CGlobal.GetCtx();
			userInfoMgr.m_Parent = this;
			String sWhere = String.format(" B_User_id='%s'", Id.toString());
			userInfoMgr.Load(sWhere, false);
		}
		return userInfoMgr;
	}

	public CUser() {
	}

	public CUser(JSONObject jsonobj) {
		try {
			Id = UUID.fromString(jsonobj.getString("id"));
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				Created = format.parse(jsonobj.getString("Created"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Creator = UUID.fromString(jsonobj.getString("Creator"));
			try {
				Updated = format.parse(jsonobj.getString("Updated"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Updator = UUID.fromString(jsonobj.getString("Updator"));

			Name = jsonobj.getString("Name");
			Pwd = jsonobj.getString("Pwd");
			Type = jsonobj.getInt("Type");
			TName = jsonobj.getString("TName");
			Sex = jsonobj.getString("Sex");
			Birthday = jsonobj.optString("Birthday");
			Height = jsonobj.optString("Height");
			Weight = jsonobj.optString("Weight");
			QQ = jsonobj.getString("QQ");
			Email = jsonobj.getString("Email");
			Phone = jsonobj.getString("Phone");
			mqttid = jsonobj.getString("mqttid");
			userInfoMgr=null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Boolean AddNewPO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "insert into B_User(Id,Name,Pwd,Type,TName,Sex,Birthday,Height,Weight,QQ,Email,Phone,mqttid,Created,Creator,Updated,Updator) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Ctx.getWritableDatabase().execSQL(
					sSql,
					new Object[] { Id, Name, Pwd, Type, TName, Sex, Birthday, Height, Weight, QQ, Email,
							Phone, mqttid, Created.getTime(), Creator,
							Updated.getTime(), Updator });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean UpdatePO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "update B_User set Name=?,Pwd=?,Type=?,TName=?,Sex=?,Birthday=?, Height=?, Weight=?, QQ=?,Email=?,Phone=?,mqttid=?,Updated=?,Updator=? where Id=?";
			Ctx.getWritableDatabase().execSQL(
					sSql,
					new Object[] { Name, Pwd, Type, TName, Sex, Birthday, Height, Weight, QQ, Email,
							Phone, mqttid, Updated.getTime(), Updator, Id });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean DeletePO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "delete from B_User where Id=?";
			Ctx.getWritableDatabase().execSQL(sSql, new Object[] { Id });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
