package com.beautifulpromise.common.log;

import com.google.code.microlog4android.appender.Appender;
import com.google.code.microlog4android.appender.FileAppender;

public class Log4File extends Log4Appender {

	FileAppender appender;
	
	public Log4File() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void CreateAppender() {
		// TODO Auto-generated method stub
		appender = new FileAppender();
		appender.setAppend(true);
		appender.setFileName("BeautifulPromise.log");
	}

	@Override
	public Appender GetAppender() {
		// TODO Auto-generated method stub
		return appender;
	}

}
