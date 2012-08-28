package com.beautifulpromise.common.c2dm;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.beautifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.application.BeautifulPromiseActivity;
import com.beautifulpromise.common.Var;
import com.beautifulpromise.common.repository.Repository;

/**
 * C2dm registraion, C2dm message receive 부분
 * @author JM
 *
 */
public class C2DMReceiver extends BroadcastReceiver{

	/**
	 * intent 받을 때
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
        Microlog4Android.logger.error("############### - onReceive");
        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            handleMessage(context, intent);
         }
	}
	
    /**
     * C2DM서버에 자신의 단말 등록
     * @param context
     * @param intent
     */
    private void handleRegistration(Context context, Intent intent) {
        Microlog4Android.logger.error("############### - handleRegistration");
        
        /**c2dm 등록에 필요한 id*/
        String registration = intent.getStringExtra("registration_id");
        
        /**자신의 facebook Id*/
        String facebookId = Repository.getInstance().getUserId();
        
        if (intent.getStringExtra("error") != null) {
            // Registration failed, should try again later.
        } else if (intent.getStringExtra("unregistered") != null) {
            Microlog4Android.logger.error("@@@@@@@@unregistered - unregistered");
        } else if (registration != null) {
           // Send the registration ID to the 3rd party site that is sending the messages.
           // This should be done in a separate thread.
           // When done, remember that all registration is done. 
            Microlog4Android.logger.error("@@@@@@@@registration_id - "+registration);
            MessageDeliverer md = new MessageDeliverer(context, "registration", facebookId, registration);
            md.start();
        }
    }
	
    /**
     * c2dm 에서 받은 메시지 처리
     * @param context
     * @param intent
     */
    private void handleMessage(Context context, Intent intent) {
        Microlog4Android.logger.error("############### - handleMessage");
        /**받은 메시지*/
        String msg = null;
        
        /**메시지를 받아올 index*/
        String new_id = intent.getStringExtra("new_id");
        if (new_id != null) {
            Microlog4Android.logger.error("handleMessage - new_id: " + intent.getStringExtra("new_id"));
        }
        
        // 서버에 접속해 메시지를 받아온다.
        msg = getMessage(new_id);
        
        //Push 메시지 다른 activity로 전달
        ((BeautifulPromiseActivity)Var.CONTEXT).refreshNotification();
    }
    
    /**
     * 해당 index에 해당하는 메시지를 웹에서 받아옴
     * @param id index
     * @return 메시지
     */
    private String getMessage(String id) {
    	String message = null;
        try {
            URL getMessageURL = new URL(
                Var.URL + "c2dm_messenger.php?mode=read&id="
                    + id);
            HttpURLConnection conn = (HttpURLConnection) getMessageURL
                .openConnection();
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            byte data[] = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while (bis.read(data, 0, 1024) != -1) {
              sb.append(new String(data, "euc-kr"));
            }
            bis.close();
            is.close();
            conn.disconnect();
            String sdata = sb.toString().trim();
            Microlog4Android.logger.error("get Push MSG - sdata:" + sdata);
//            String[] arr_sdata = sdata.split("\n");
            message = sdata;
          } catch (MalformedURLException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
		return message;
    }
}
