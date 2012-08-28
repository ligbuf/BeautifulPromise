package com.beautifulpromise.database;

import java.util.ArrayList;

import junit.framework.TestFailure;

import com.beautifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.common.dto.AddPromiseDTO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * feed, gps 데이터 베이스 DAO클래스
 * @author ou
 *
 */
public class CheckDAO {
	private CheckDBHelper checkDBHelper;
	SQLiteDatabase db;
	
	public CheckDAO(CheckDBHelper checkDBHelper) {
		this.checkDBHelper = checkDBHelper;
		db = this.checkDBHelper.getWritableDatabase();
	}

	/**
	 * 데이터 베이스 close
	 */
	public void close(){
		db.close();
	}

	/**
	 * gps 테이블 초기화
	 * @return 성공시 true
	 */
	public boolean gpsinit(){
		db.delete("gps", null, null);
		return true;
		
	}

	/**
	 * gps테이블에 데이터 삽입 
	 * @param id 목표 ID
	 * @param Latitude 위도
	 * @param Longitude 경도
	 * @return
	 */
	public boolean gpsinsert(String id, Double Latitude,Double Longitude){
		ContentValues row;
		row = new ContentValues();
		row.put("promiseid", id);
		row.put("latitude", Latitude);
		row.put("longitude", Longitude);
		db.insert("gps", null, row);
		return true;
		
	}
	
	/**
	 * 목표 ID에 해당하는 위도 경도 정보 리턴
	 * @param id 해당 목표 ID
	 * @return 위도 경도 Location배열
	 */
	public Double[] getGPS(String id){
		Double [] Location = new Double[2];
		Cursor cursor = db.rawQuery("SELECT latitude, longitude FROM gps WHERE promiseid=" + id, null);
		cursor.moveToNext();
		Location[0] = cursor.getDouble(0);
		Location[1] = cursor.getDouble(1);
		cursor.close();
		return Location;
	}

	/**
	 * 목표 ID에 해당하는 오늘 목표를 수행 했는지 체크 정보 갱신
	 * @param id 해당 목표 ID
	 * @param check 성공시 true, 실패시 false
	 * @return
	 */
	public boolean feedcheckupdate(String id, int check){
		try {
			ContentValues row;
			row = new ContentValues();
			row.put("promiseid", id);
			row.put("feedcheck", check);
			db.update("feed", row, "promiseid=" + id, null);
		} catch (Exception e) {
			//Log.e("ou", e.toString());
			Microlog4Android.logger.error("ou"+" - "+e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * feed 테이블에 해당 목표 ID와 체크상태 삽입
	 * @param id 목표 ID
	 * @param check 해당 목표의 오늘 목표 체크 여부
	 * @return 성공시 true, 실패시 false
	 */
	public boolean feedcheckinsert(String id, int check){
		try {

			ContentValues row;
			row = new ContentValues();
			row.put("promiseid", id);
			row.put("feedcheck", check);
			db.insert("feed", null ,row);
		} catch (Exception e) {
			//Log.e("ou", e.toString());
			Microlog4Android.logger.error("ou"+" - "+e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * 해당 목표 ID를 Select하여 check값을 가져옴
	 * 해당 목표가 테이블에 없는 경우는 0리턴
	 * @param id
	 * @return 해당 목표의 오늘 목표체크여부
	 */
	public int feedcheckdo(String id){
		Cursor cursor = db.rawQuery("SELECT feedcheck FROM feed WHERE promiseid=" + id , null);
		int check;
		if(cursor.getCount() != 0)
		{
			cursor.moveToNext();
			check = cursor.getInt(0);
		}
		else
		{
			feedcheckinsert(id, 0);
			check = 0;
		}
		cursor.close();
 		return check;
	}
	
	/**
	 * feed테이블 초기화
	 * check를 전부 0으로 바꿈
	 * @param promisedto
	 * @return
	 */
	public boolean feedcheckinit(ArrayList<AddPromiseDTO> promisedto){
		ContentValues row;
		row = new ContentValues();
		db.delete("feed", null, null);
		try {
			for (AddPromiseDTO temppromise : promisedto) {
				row.clear();
				row.put("promiseid", temppromise.getPostId());
				row.put("feedcheck", 0);
				db.insert("feed", null, row);
			}
		} catch (Exception e) {
			//Log.e("ou", e.toString());
			Microlog4Android.logger.error("ou"+" - "+e.toString());
			return false;
		}
		return true;		
	}
}
