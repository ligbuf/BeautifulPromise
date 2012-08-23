package com.beautifulpromise.common.alarm;

import java.util.ArrayList;
import java.util.Calendar;

import com.beatutifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.database.DatabaseHelper;
import com.beautifulpromise.database.GoalsDAO;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/**
 * 전체 목표에 대해 알람을 호출하는 클래스
 * @author ou
 *
 */
public class Alarm {

	Context context;
	
	/**
	 * 해당 요일에 수행해야 되는 목표들을 불러와 장소, 시간 목표에 맞춰 알람매니저를 세팅
	 * @param context 알람 클래스를 생성한 엑티비티의 context
	 * @param promisedto 해당요일에 수행해야되는 목표의 ArrayList
	 */
	public void SetAlarm(Context context, ArrayList<AddPromiseDTO> promisedto) {
		this.context = context;
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Intent intent;
		PendingIntent sender;

		for (AddPromiseDTO Promise : promisedto) {
			//장소 알람(GPS)
			if (Promise.getCategoryId() == 0) {
				intent = new Intent(context, AlarmReceiver.class);
				
				Bundle extras = new Bundle();
				extras.putSerializable("PromiseDTO", Promise);
				intent.putExtras(extras);
				sender = PendingIntent.getBroadcast(context, 0, intent, 0);

				// 알람 시간
				Calendar calendar = Calendar.getInstance();
//				calendar.set(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDay(), Promise.getTime(), Promise.getMin());
//				calendar.set(2012,5,10,17,18);
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.SECOND, 10);

				// 알람 등록
				am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
				break;
			}
			//시간 알람
			else if ((Promise.getCategoryId() == 1))
			{
				intent = new Intent(context, AlarmReceiver.class);
				
				Bundle extras = new Bundle();
				extras.putSerializable("PromiseDTO", Promise);
				intent.putExtras(extras);
				sender = PendingIntent.getBroadcast(context, 0, intent, 0);

				// 알람 시간
				Calendar calendar = Calendar.getInstance();
//				calendar.set(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDay(), Promise.getTime(), Promise.getMin());
//				calendar.set(2012,5,10,17,18);
				Microlog4Android.logger.error("ou - "+Integer.toString(calendar.getTime().getYear())+ Integer.toString(calendar.getTime().getMonth())+ Integer.toString(calendar.getTime().getDay())+Integer.toString(Promise.getTime()) +Integer.toString(Promise.getMin()));
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.SECOND, 15);

				// 알람 등록
				am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
				break;
			}
			//기타 목표는 알람 없음
			else
			{
			}
		}
	}
}
