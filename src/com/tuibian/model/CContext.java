package com.tuibian.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CContext {

	public static String DATABASE_NAME = "tuibian";
	public DatabaseHelper DbHelper;
	private final Context context;

	private CUserMgr userMgr;

	public CUserMgr GetUserMgr() {
		if (userMgr == null) {
			userMgr = new CUserMgr();
			userMgr.Ctx = this;
			userMgr.Load("", false);
		}
		return userMgr;
	}

	public CContext(Context ctx) {
		context = ctx;

		Log.i("Name", DATABASE_NAME);
		DbHelper = DatabaseHelper.getInstance(ctx, DATABASE_NAME);
	}

	public SQLiteDatabase getReadableDatabase() {

		if (DbHelper != null) {
			return DbHelper.getReadableDatabase();
		}
		return null;
	}

	public SQLiteDatabase getWritableDatabase() {
		return DbHelper.getWritableDatabase();
	}
	
	public DatabaseHelper getDbHelper() {
		return DbHelper;
	}
}
