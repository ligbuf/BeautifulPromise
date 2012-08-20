package com.beautifulpromise.common.alarm;

import com.beautifulpromise.R;
import com.beautifulpromise.application.checkpromise.CycleCheckActivity;
import com.beautifulpromise.application.checkpromise.WorkCheckActivity;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.database.CheckDAO;
import com.beautifulpromise.database.CheckDBHelper;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Alarm클래스에서 설정한 알람을 받는 BroadcastReceiver클래스
 * @author ou
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	LocationListener mLocationListener;
	LocationManager lm;
	CheckDBHelper gps_DBHelper;
	Context context;
	CheckDAO checkDAO;
	AddPromiseDTO promiseobject;

	/**
	 * 시간, 장소 목표인 것을 확인한 후
	 * 시간 목표의 경우는 바로 알림창 띄우고
	 * 장소 목표의 경우는 해당 시간에 gps 데이터베이스에 
	 * 위도 경도, 목표ID값을 넣은 후 알림창 띄우기
	 */
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		CheckDBHelper checkDBHelper = new CheckDBHelper(this.context);
		checkDAO = new CheckDAO(checkDBHelper);

		Intent i = null;
		
		// home에서 객체 받아오기
		Object tempobject = intent.getExtras().get("PromiseDTO");
		promiseobject = (AddPromiseDTO) tempobject;

		if (promiseobject.getCategoryId() == 0) {
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			i = new Intent(AlarmReceiver.this.context, CycleCheckActivity.class);
			
			mLocationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					if (location != null) {

						Double Latitude = location.getLatitude();
						Double Longitude = location.getLongitude();
						checkDAO.gpsinit();
						checkDAO.gpsinsert(promiseobject.getPostId(), Latitude, Longitude);
						checkDAO.close();
						lm.removeUpdates(mLocationListener);
					}
				}

				public void onProviderDisabled(String arg0) {
					Toast.makeText(AlarmReceiver.this.context,
							"provider disabled " + arg0, Toast.LENGTH_SHORT)
							.show();
				}

				public void onProviderEnabled(String arg0) {
					Toast.makeText(AlarmReceiver.this.context,
							"provider enabled " + arg0, Toast.LENGTH_SHORT)
							.show();
				}

				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
					// TODO Auto-generated method stub
					Toast.makeText(AlarmReceiver.this.context,
							"GPS 상태가 변경되었습니다.\n" + arg0 + ", " + arg1 + "", Toast.LENGTH_SHORT).show();
				}
			};
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000,
					5, mLocationListener);

		} else if ((promiseobject.getCategoryId() == 1))
		{
			i = new Intent(AlarmReceiver.this.context, WorkCheckActivity.class);
			checkDAO.close();
		}
		
		Bundle extras = new Bundle();
		extras.putSerializable("PromiseDTO", promiseobject);
		i.putExtras(extras);

		PendingIntent contentIntent = PendingIntent.getActivity(AlarmReceiver.this.context, 0, i, PendingIntent.FLAG_ONE_SHOT);
		
		CharSequence tickerText = "아름다운 약속을 실천하실 시간입니다.";
		CharSequence contentTitle = "아름다운 약속";
		CharSequence contentText = promiseobject.getTitle();
		
		Notification notification = new Notification(R.drawable.icon, tickerText, 0);
		notification.setLatestEventInfo(AlarmReceiver.this.context, contentTitle, contentText, contentIntent);
		
		NotificationManager notificationManager = (NotificationManager) AlarmReceiver.this.context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1,notification);
		
	}
}
