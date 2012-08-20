package com.beautifulpromise.common;

import android.content.Context;

/**
 * 전역적으로 쓰이는 변수들
 * @author JM
 *
 */
public class Var {
	
	/**Side navigation*/
	public static boolean menuShowFlag = false;
	public static float LEFT_MENUBAR_CLOSE_RANGE = 350;
	public static float LEFT_MENUBAR_OPEN_RANGE = 30;
	public static int LEFT_MENUBAR_WIDTH = 360;
	public static int LEFT_MENUBAR_HEIGHT = 90;
	
	/**Facebook App ID*/
	public static final String APP_ID = "222120417868240";
	
	/**Facebook App permission*/
	public static final String[] PERMISSIONS =  new String[] {
		"user_about_me", "publish_stream", "user_photos", "manage_notifications"
	};
	
	/**웹 페이지 주소*/
	public static final String URL = "http://61.43.139.112/";
	
	/**beautifulpromise context - refresh notification call을 위해*/
	public static Context CONTEXT;
}
