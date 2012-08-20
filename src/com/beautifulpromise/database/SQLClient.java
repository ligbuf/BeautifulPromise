package com.beautifulpromise.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @description 데이터베이스에서 정보를 검색한 후 객체화 시켜주는 클래스
 * @author immk
 */
public class SQLClient {

	@SuppressWarnings("rawtypes")
	public static ArrayList executeQuery(DatabaseHelper databaseHelper, String query, Class[] types) {
		if (databaseHelper == null) return null;

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> row = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;

		try {
			db = databaseHelper.getReadableDatabase();
			cursor = db.rawQuery(query, null);
			cursor.moveToFirst();

			int columCount = cursor.getColumnCount();
			if (columCount == types.length && cursor.getCount() > 0) {
				do {
					row = new HashMap<String, Object>();
					for (int i = 0; i < columCount; i++) {
						if (types[i] == Byte.class) {
							row.put(cursor.getColumnName(i), cursor.getBlob(i));
						} else if (types[i] == Short.class) {
							row.put(cursor.getColumnName(i), cursor.getShort(i));
						} else if (types[i] == Long.class) {
							row.put(cursor.getColumnName(i), cursor.getLong(i));
						} else if (types[i] == Double.class) {
							row.put(cursor.getColumnName(i), cursor.getDouble(i));
						} else if (types[i] == Integer.class) {
							row.put(cursor.getColumnName(i), new Integer(cursor.getInt(i)));
						} else if (types[i] == String.class) {
							row.put(cursor.getColumnName(i), cursor.getString(i));
						} 
					}
					list.add(row);
				} while (cursor.moveToNext());
			}
			cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception se) {
				}
			}

			if (db != null) {
				try {
					db.close();
				} catch (Exception se) {
				}
			}
		}
		return list;
	}

	public static boolean executeUpdate(DatabaseHelper databaseHelper, String query) {
		return executeUpdate(databaseHelper, query, null);
	}
	
	public static boolean executeUpdate(DatabaseHelper databaseHelper, String query, Object[] objects) {

		if (databaseHelper == null) return false;
		
		boolean isSucess = false;
		SQLiteDatabase db = null;
		
		try {
			db = databaseHelper.getReadableDatabase();
			if( objects == null )
				db.execSQL(query);
			else
				db.execSQL(query, objects);
			
			isSucess = true;
		} catch (Exception e) {
			isSucess = false;
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (Exception se) {
				}
			}
		}

		return isSucess;
	}
}