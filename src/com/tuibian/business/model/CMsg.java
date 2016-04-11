package com.tuibian.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.tuibian.model.CBaseObject;
import com.tuibian.model.CBaseObjectMgr;


public class CMsg extends CBaseObject {
	public String Url;
	public String Title;
	public String Remarks;

	public CMsg() {
	}

	public CMsg(JSONObject jsonobj) {
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

			Url = jsonobj.getString("Url");
			Title = jsonobj.getString("Title");
			Remarks = jsonobj.getString("Remarks");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Boolean AddNewPO() {
		// TODO Auto-generated method stub
		try {
			
			String sSql = "insert into Msg(Id, Title, Url,Remarks,Created,Creator,Updated,Updator) values (?,?,?,?,?,?,?,?,?)";
			Ctx.getWritableDatabase().execSQL(
					sSql,
					new Object[] { Id, Title, Url, Remarks, Created.getTime(),
							Creator, Updated.getTime(), Updator });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean UpdatePO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "update Msg set Title=?,Url=?,Remarks=?,Updated=?,Updator=? where Id=?";
			Ctx.getWritableDatabase().execSQL(
					sSql,
					new Object[] { Url, Title, Remarks, Updated.getTime(),
							Updator, Id });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean DeletePO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "delete from Msg where Id=?";
			Ctx.getWritableDatabase().execSQL(sSql, new Object[] { Id });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
