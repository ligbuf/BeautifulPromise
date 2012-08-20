package com.beautifulpromise.common.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Environment;

/**
 * @description 데이터 저장을 위한 관련된 클래스
 * @author immk
 *
 */
public class StorageUtils {

	public static final String DIR_PATH = "BeautifulPromise";
    
	/**
	 * 파일의 경로 리턴
	 * @param context
	 * @return
	 */
    public static String getFilePath(Context context){
    		
		final File path = new File(Environment.getExternalStorageDirectory(), DIR_PATH);
		if (!path.exists()) {
			path.mkdir();
		}

    	SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmss");
	    Date currentTime = new Date();
	    String dateString = formatter.format(currentTime); 
	    String filePath = path + "/" + dateString ;
	    return filePath;
    }
    
    /**
     * 이미지 저장 경로 리턴
     * @param context
     * @return
     */
    public static String getImaegPath(Context context){
    	
    	
		final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
		if (!path.exists()) {
			path.mkdir();
		}
	    String filePath = path + "/" + "no_image" ;
	    return filePath;
    }
}
