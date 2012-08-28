package com.beautifulpromise.common.log;


import com.beautifulpromise.R;
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
	 * 2012. 8. 23.오후 2:59:07
	 * TODO Microlog4Android 초기화
	 * @author JeongSeungsu
	 */
	public static void init(String appendername)
	{
		try
		{
		PatternFormatter formatter = new PatternFormatter();
		formatter.setPattern("   %d{ISO8601}    [%P]  %m  %T  ");
        logger.setLevel(Level.DEBUG);
        
        String[] StrArray;

		StrArray = appendername.split("\\|");

		for (String s : StrArray) {
			
			Appender appender =  InitAppender(s);
			
			appender.setFormatter(formatter);
			logger.addAppender(appender);
		}
		}
		catch(Exception e){ //인스턴스(new)실패시에 대한 예외사항   
			  e.printStackTrace();
		}  
		
	}
	
	private static Appender InitAppender(String appendername) {
		
			Log4Appender appender = null;
			if (appendername.equals("LogCatAppender")) {
				appender = new Log4LogCat();
			}
			if (appendername.equals("FileAppender")) {
				appender = new Log4File();
			}
			appender.CreateAppender();
			return appender.GetAppender();
	}
		

	
}
