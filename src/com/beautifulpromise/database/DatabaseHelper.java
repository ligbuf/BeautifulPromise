package com.beautifulpromise.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @description 목표, 알람, GPS 정보, 노티피케이션에 관련된 Database를 생성
 * @author immk
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, DatabaseMetadata.DATABASE_NAME, null, DatabaseMetadata.DATABASE_VERSTION);
	}

	/**
	 * Database생성
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE Goals (");
		buffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
		buffer.append("post_id TEXT,");
		buffer.append("category INT,");
		buffer.append("title TEXT,");
		buffer.append("start_date TEXT,");
		buffer.append("end_date TEXT,");
		buffer.append("content TEXT, ");
		buffer.append("result INT, "); 
		buffer.append("donation INT, "); 
		buffer.append("create_date TEXT"); 
		buffer.append(")");
		db.execSQL(buffer.toString());

//		buffer.setLength(0);
//		buffer.append("CREATE TABLE Helpers (");
//		buffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
//		buffer.append("goal_id INT,");
//		buffer.append("friend_id INT");
//		buffer.append(")");
//		db.execSQL(buffer.toString());

		buffer.setLength(0);
		buffer.append("CREATE TABLE Locations (");
		buffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
		buffer.append("goal_id INT,");
		buffer.append("latitude DOUBLE,");
		buffer.append("longitude DOUBLE");
		buffer.append(")");
		db.execSQL(buffer.toString());
		
		buffer.setLength(0);
		buffer.append("CREATE TABLE Alarms (");
		buffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
		buffer.append("goal_id INT,");
		buffer.append("monday INT,");
		buffer.append("tuesday INT,");
		buffer.append("wednesday INT,");
		buffer.append("thursday INT,");
		buffer.append("friday INT,");
		buffer.append("saturday INT,");
		buffer.append("sunday INT,");
		buffer.append("time INT,");
		buffer.append("min INT");
		buffer.append(")");
		db.execSQL(buffer.toString());
		
		buffer.setLength(0);
		buffer.append("CREATE TABLE Notifications (");
		buffer.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		buffer.append("title TEXT,");
		buffer.append("send_user_id TEXT,");
		buffer.append("fb_id TEXT");
		buffer.append(")");
		db.execSQL(buffer.toString());

	}

	/**
	 * Database를 업그레이드할 경우 호출
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Goals");
//		db.execSQL("DROP TABLE IF EXISTS Helpers");
		db.execSQL("DROP TABLE IF EXISTS Locations");
		db.execSQL("DROP TABLE IF EXISTS Alarms");
		db.execSQL("DROP TABLE IF EXISTS Notifications");
		onCreate(db);
	}
}