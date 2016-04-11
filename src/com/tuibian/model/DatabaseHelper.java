package com.tuibian.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// private static final int VERSION = 1; //0.1版
	// private static final int VERSION = 2; // 0.2版
	//private static final int VERSION = 3; // 0.3版
	private static final int VERSION = 4; // 0.4版

	private static DatabaseHelper instance;
	private static String m_dbName="";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public static DatabaseHelper getInstance(Context context, String name) {

		if (instance == null || !m_dbName.equalsIgnoreCase(name)) {
			instance = new DatabaseHelper(context, name);
			m_dbName=name;
		}
		return instance;
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	// 该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {

		// TODO Auto-generated method stub

		// 创建数据库所有表
		// 用户
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("create table IF NOT EXISTS B_User (");
		sqlBuilder.append("Id  varchar(50) PRIMARY KEY  NOT NULL UNIQUE,");
		sqlBuilder.append("Name  varchar(50) ,");
		sqlBuilder.append("Pwd  varchar(50) ,");
		sqlBuilder.append("Type  int ,");
		sqlBuilder.append("TName  varchar(50) ,");
		sqlBuilder.append("Sex  varchar(10) ,");
		sqlBuilder.append("Birthday  varchar(10) ,");
		sqlBuilder.append("QQ  varchar(50) ,");
		sqlBuilder.append("Email  varchar(250) ,");
		sqlBuilder.append("Phone  varchar(50) ,");
		sqlBuilder.append("mqttid  varchar(50) ,");
		sqlBuilder.append("Created  long ,");
		sqlBuilder.append("Creator  varchar(50) ,");
		sqlBuilder.append("Updated  long ,");
		sqlBuilder.append("Updator  varchar(50) )");
		db.execSQL(sqlBuilder.toString());
		// 用户扩展信息
		sqlBuilder = new StringBuilder();
		sqlBuilder.append("create table IF NOT EXISTS UserInfo (");
		sqlBuilder.append("Id  varchar(50)  PRIMARY KEY,");
		sqlBuilder.append("B_User_id  varchar(50) ,");
		sqlBuilder.append("CertNo  varchar(50) ,");
		sqlBuilder.append("StartDate  long ,");
		sqlBuilder.append("EndDate  long ,");
		sqlBuilder.append("Height  numeric(18,2) ,");
		sqlBuilder.append("Weight  numeric(18,2) ,");
		sqlBuilder.append("BloodType  varchar(2) ,");
		sqlBuilder.append("Nation  varchar(50) ,");
		sqlBuilder.append("Remarks  text ,");
		sqlBuilder.append("Created  long ,");
		sqlBuilder.append("Creator  varchar(50) ,");
		sqlBuilder.append("Updated  long ,");
		sqlBuilder.append("Updator  varchar(50) )");
		db.execSQL(sqlBuilder.toString());

		sqlBuilder = new StringBuilder();
		sqlBuilder.append("create table IF NOT EXISTS Msg(");
		sqlBuilder.append("Id  varchar(50) PRIMARY KEY,");
		sqlBuilder.append("Title  varchar(50) ,");
		sqlBuilder.append("Url  varchar(250) ,");
		sqlBuilder.append("Remarks  text ,");
		sqlBuilder.append("Created  long ,");
		sqlBuilder.append("Creator  varchar(50) ,");
		sqlBuilder.append("Updated  long ,");
		sqlBuilder.append("Updator  varchar(50) )");
		db.execSQL(sqlBuilder.toString());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	/**
	* 方法：检查某表列是否存在
	* @param db
	* @param tableName 表名
	* @param columnName 列名
	* @return
	*/
	private boolean checkColumnExist(SQLiteDatabase db, String tableName, String columnName) {
	    boolean result = false ;
	    Cursor cursor = null ;
	    try{
	        //查询一行
	        cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
	            , null );
	        result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
	    }catch (Exception e){
	         Log.e("DatabaseHelper","checkColumnExists1..." + e.getMessage()) ;
	    }finally{
	        if(null != cursor && !cursor.isClosed()){
	            cursor.close() ;
	        }
	    }
	    return result ;
	}
}
