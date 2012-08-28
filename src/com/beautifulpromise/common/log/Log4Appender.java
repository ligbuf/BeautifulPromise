package com.beautifulpromise.common.log;

import com.google.code.microlog4android.appender.Appender;

/**
 * Log4Appender 
 * 2012. 8. 28.오후 7:59:45
 * TODO FactoryMethod에서 이 클래스들을 가져와서 초기화 하는역활
 * @author JeongSeungsu
 * @description 
 */
public abstract class Log4Appender 
{
	/**
	 * 2012. 8. 28.오후 8:00:22
	 * TODO 모든 어펜더들이 해야할 기본적인 초기화 과정 
	 * @author JeongSeungsu
	 * @param appender 무슨 어펜더를 만들지 결정...
	 */
	public abstract void CreateAppender();
	
	public abstract Appender GetAppender();
	
}
