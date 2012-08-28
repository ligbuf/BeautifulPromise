package com.beautifulpromise.common.log;

import com.google.code.microlog4android.appender.Appender;
import com.google.code.microlog4android.appender.LogCatAppender;

public class Log4LogCat extends Log4Appender {

	LogCatAppender appender;
	
	public Log4LogCat() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void CreateAppender() {
		// TODO Auto-generated method stub
		appender = new LogCatAppender();
	}

	@Override
	public Appender GetAppender() {
		// TODO Auto-generated method stub
		return appender;
	}

}
