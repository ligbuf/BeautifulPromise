package com.beautifulpromise.common.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.beautifulpromise.common.log.Microlog4Android;

import android.util.Log;

/**
 * @description 날짜 정보에 관련된 클래스 (해당 형식으로 변환, 현재 날짜를 가져옴)
 * @author immk
 *
 */
public class DateUtils {

	/**
	 * 현재 날짜 리턴
	 * @return String
	 */
	public static String getDate() {

		DecimalFormat decimalFormat = new DecimalFormat("00");
		DecimalFormat NumFormat = new DecimalFormat("0000");

		Calendar currentDate = Calendar.getInstance();
		int year = currentDate.get(Calendar.YEAR);
		int month = currentDate.get(Calendar.MONTH);
		int date = currentDate.get(Calendar.DATE);

		String resultDate = NumFormat.format(year) + "년 "
				+ (month + 1) + "월 "
				+ (date) + "일";
		return resultDate;
	}
	
	/**
	 * 다음 날짜 리턴
	 * @return String
	 */
	public static String getNextDate() {

		DecimalFormat decimalFormat = new DecimalFormat("00");
		DecimalFormat NumFormat = new DecimalFormat("0000");

		Calendar currentDate = Calendar.getInstance();
		int year = currentDate.get(Calendar.YEAR);
		int month = currentDate.get(Calendar.MONTH);
		int date = currentDate.get(Calendar.DATE);

		String resultDate = NumFormat.format(year) + "년 "
				+ (month + 1) + "월 "
				+ (date + 1) + "일";
		return resultDate;
	}

	/**
	 * 날짜 형식 변경
	 * @param dateStr
	 * @param format
	 * @return String
	 */
	public static String convertDateFormat(String dateStr, String format) {
		// ex) Sun, 19 Oct 2008 15:00 GMT ->
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		date.setTime(Date.parse(dateStr));
		return dateFormat.format(date);
	}
	
	/**
	 * 해당 년도 리턴
	 * @return int
	 */
	public static int getYear(){
		Calendar currentDate = Calendar.getInstance();
		return currentDate.get(Calendar.YEAR);
	}

	/**
	 * 해당 월 리턴
	 * @return
	 */
	public static int getMonth(){
		Calendar currentDate = Calendar.getInstance();
		return currentDate.get(Calendar.MONTH);
	}
	
	/**
	 * 해당 일 리턴
	 * @return int
	 */
	public static int getDay(){
		Calendar currentDate = Calendar.getInstance();
		return currentDate.get(Calendar.DATE);
	}

	/**
	 * 현재 날짜 및 시간 리턴
	 * @return String
	 */
	public static String getCreateDate() {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		Date currentTime = new Date();
		return formatter.format (currentTime);	
	}
	
	/**
	 * 데이터 형식 변경
	 * @param dateStr 날짜형식
	 * @return 'yyyyMMdd' 형식의 날짜
	 */ 
	public static String convertStringDate(String dateStr){

		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy년 MM월 dd일");	
		Date date = null;
		try {
			date = (Date) formatter.parse(dateStr);
//			Log.i("immk", ""+date.toString());
			Microlog4Android.logger.info("immk"+" - "+""+date.toString());
			formatter = new SimpleDateFormat ("yyyyMMdd");
			return formatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}  
	}
}
