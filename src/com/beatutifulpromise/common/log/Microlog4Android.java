package com.beatutifulpromise.common.log;


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
	public static void init()
	{
		PatternFormatter formatter = new PatternFormatter();
		formatter.setPattern("   %d{ISO8601}    [%P]  %m  %T  ");
        logger.setLevel(Level.INFO);

        // write to LogCat
        LogCatAppender logCatAppender =   new LogCatAppender();
        logCatAppender.setFormatter(formatter);
        logger.addAppender(logCatAppender);
              
        // wirte to text file of SD-card. (need   WRITE_EXTERNAL_STORAGE permission)
        FileAppender fileAppender = new   FileAppender();
        fileAppender.setAppend( true );
        fileAppender.setFileName( "BeautifulPromise.log");
        fileAppender.setFormatter(formatter);
        logger.addAppender(fileAppender);
	}
	
	

}
