package com.beautifulpromise.common.log;


import android.util.Log;

import com.google.code.microlog4android.*;
import com.google.code.microlog4android.appender.*;
import com.google.code.microlog4android.format.*;

/**
 * Microlog4Android 
 * 2012. 8. 23.오후 2:56:52
 * TODO Microlog4Android 랩퍼 클래스
 * @author JeongSeungsu
 * @description 초기화만 해준다.
 */
public class Microlog4Android {
	/**
	 * 전역적으로 쓸수 있는 Logger 이것으로 이제 로그를 남긴다.
	 */
	static public Logger logger = LoggerFactory.getLogger();
		
	/**
	 * 2012. 8. 31.오후 5:18:31
	 * TODO MicroLog4Android 초기화
	 * @author JeongSeungsu
	 * @param PackageName 패키지 이름
	 * @param appendername 어펜더 이름들!
	 */
	public static void init(String PackageName, String appendernames)
	{
		try {
			PatternFormatter formatter = new PatternFormatter();
			formatter.setPattern("   %d{ISO8601}    [%P]  %m  %T  ");
			logger.setLevel(Level.DEBUG);

			String[] StrArray;

			StrArray = appendernames.split("\\|");

			for (String s : StrArray) {

				Appender appender = InitAppender(PackageName,s);

				appender.setFormatter(formatter);
				logger.addAppender(appender);
			}
			
			logger.info("LogInit");
		} catch (Exception e) { // 인스턴스(new)실패시에 대한 예외사항
			Log.e("LOG_ERROR", "FAIL Log4Andorid : " + appendernames);
		}

	}
	
	/**
	 * 2012. 8. 31.오후 5:17:57
	 * TODO		Appender생성
	 * @author JeongSeungsu
	 * @param PackageName 패키지 이름
	 * @param appendername 어펜더 이름
	 * @return 생성된 상위 인터페이스 어펜더
	 */
	private static Appender InitAppender(String PackageName, String appendername) {

		Log4Appender appender = null;

		try {
			
			appendername = PackageName+ "." + appendername;
			Class c = Class.forName(appendername);
			appender = (Log4Appender) c.newInstance();
			appender.CreateAppender();

		} catch (ClassNotFoundException e1) { // 클래스를 찾지못했을 경우에 대한 예외사항
			Log.e("LOG_ERROR", "Class is Not Found");
			return null;
		} catch (InstantiationException e2) { // 인스턴스(new)실패시에 대한 예외사항
			Log.e("LOG_ERROR", "new Instance Fail");
			return null;
		} catch (IllegalAccessException e3) { // 파일접근에 대한 예외사항
			Log.e("LOG_ERROR", "Class File Access Error");
			return null;
		}

		return appender.GetAppender();
	}

	

	
}
