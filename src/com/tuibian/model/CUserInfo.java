package com.tuibian.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class CUserInfo extends CBaseObject {
	public UUID B_User_id;
	public String CertNo;
	public String CardNo;
	public Date StartDate;
	public Date EndDate;
	public String BloodType;
	public String Nation;
	public String Remarks;

	public CUserInfo() {
	}

	public CUserInfo(JSONObject jsonobj) {
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

			B_User_id = UUID.fromString(jsonobj.getString("B_User_id"));
			CertNo = jsonobj.getString("CertNo");
			CardNo = jsonobj.getString("CardNo");
			BloodType = jsonobj.getString("BloodType");
			Nation = jsonobj.getString("Nation");
			Remarks = jsonobj.getString("Remarks");
			try {
				StartDate = format.parse(jsonobj.getString("StartDate"));
				EndDate = format.parse(jsonobj.getString("EndDate"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Boolean AddNewPO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "insert into UserInfo(Id,B_User_id,CertNo,CardNo,StartDate,EndDate,BloodType,Nation,Remarks,Created,Creator,Updated,Updator) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Ctx.getWritableDatabase().execSQL(
					sSql,
					new Object[] { Id, B_User_id, CertNo, CardNo,
							StartDate.getTime(), EndDate.getTime(),
							BloodType, Nation, 
							Remarks, Created.getTime(),
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
			String sSql = "update UserInfo set CertNo=?,CardNo=?,StartDate=?,EndDate=?,BloodType=?,Nation=?,Remarks=?,Updated=?,Updator=? where Id=?";
			Ctx.getWritableDatabase().execSQL(
					sSql,
					new Object[] { CertNo, CardNo, StartDate.getTime(), EndDate.getTime(),
							BloodType, Nation, Remarks, 
							Updated.getTime(), Updator, Id });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean DeletePO() {
		// TODO Auto-generated method stub
		try {
			String sSql = "delete from UserInfo where Id=?";
			Ctx.getWritableDatabase().execSQL(sSql, new Object[] { Id });

		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
