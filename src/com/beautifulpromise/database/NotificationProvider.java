package com.beautifulpromise.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * @description Content Provider를 활용한 노티피케이션 정보 가져오기
 * @author immk
 */
public class NotificationProvider extends ContentProvider {

	public static Uri CONTENT_URI = Uri.parse("content://com.beautifulpromise.database/notification");
	private SQLiteDatabase db;
	private DatabaseHelper helper;
	public static final String TABLE = "Notifications";
//	
//	static final int UriMatcher matcher;
//	static {}
//	
	/**
	 * DatabaseHelper 및 SQLiteDatabase 생성
	 */
	@Override
	public boolean onCreate() {
		helper = new DatabaseHelper(getContext());
		db = helper.getWritableDatabase();
		return db != null? true: false;
	}
	
	/**
	 * URL을 통해 데이터의 MIME 형식 요청
	 */
	@Override
	public String getType(Uri uri) { 
		return null;
	}
	
	/**
	 * URL을 통한 데이터 삭제
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		return 0;
	}

	/**
	 * URL을 통한 데이터 저장
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = db.insert(TABLE, null, values);
		if(id > 0){
			Uri notiUri = ContentUris.withAppendedId(CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(notiUri, null);
			return uri;
		}
		return null;
	}

	/**
	 * URL을 통한 데이터 검색
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String sql;
		
		if(selectionArgs != null)
			sql = "SELECT * FROM " + TABLE + " WHERE fb_id='"+selectionArgs[0]+"'";  // " WHERE _id="+uri.getPathSegments().get(1)
		else
			sql = "SELECT * FROM " + TABLE + " ORDER BY _id DESC"; 
		Cursor cursor = db.rawQuery(sql, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	/**
	 * URL을 통한 데이터 업데이트
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
