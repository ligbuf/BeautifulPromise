package com.beautifulpromise.common.c2dm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.util.Log;

import com.beautifulpromise.common.Var;
import com.facebook.halo.framework.common.AccessToken;

/**
 * 중앙서버(서드파티서버)로 메시지를 전송한다
 * @author JM
 *
 */
class MessageDeliverer extends Thread {
	/**넘어오는 컨텍스트*/
    Context context;
    
    /**사용자 facebook id*/
    String facebookId;
    
    /**전송할 메시지*/
    String message;
    
    /**c2dm 등록id*/
    String registrationId;
    
    /**웹에 포스트할때 모드*/
    String mode;

    /**
     * 생성자
     * @param context
     * @param message
     * @param facebookId
     * @param registrationId
     */
    public MessageDeliverer(Context context, String message, String facebookId,
        String registrationId) {
      this.context = context;
      this.message = message;
      this.facebookId = facebookId;
      this.registrationId = registrationId;
    }
    
    /**
     * thread start
     */
    public void run() {
      if (facebookId == null || facebookId.equals("")) {
        Log.e("MessageDeliverer", "Macaddress is invalid");
        return;
      }
      if (message == null || message.equals("")) {
        Log.e("MessageDeliverer", "Message is empty");
        return;
      }
      try {
    	//등록모드
    	mode = "register";
    	
        // 서드파티 애플리케이션 서버에 id등록 요청을 하기 위한 URL
        URL idRegistrationUrl = new URL(
            Var.URL + "c2dm_messenger.php?" 
                + "registration_id=" + registrationId + "&facebook_id="
                + facebookId + "&access_token=" + AccessToken.getAccessToken() + "&mode=" + mode);
        HttpURLConnection conn = (HttpURLConnection) idRegistrationUrl
            .openConnection();
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);
        conn.setUseCaches(false);
        Log.e("MessageDeliverer", "Message send URL: " + idRegistrationUrl.toString());
        // URL에 접속
        InputStream is = conn.getInputStream();
        is.close();
        conn.disconnect();
        //TODO
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }