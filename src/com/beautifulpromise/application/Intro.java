package com.beautifulpromise.application;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.beatutifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.R;
import com.beautifulpromise.application.intro.NotificationService;
import com.beautifulpromise.common.Var;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.MessageUtils;
import com.beautifulpromise.facebooklibrary.DialogError;
import com.beautifulpromise.facebooklibrary.Facebook;
import com.beautifulpromise.facebooklibrary.Facebook.DialogListener;
import com.beautifulpromise.facebooklibrary.FacebookError;
import com.beautifulpromise.facebooklibrary.SessionStore;
import com.beautifulpromise.parser.HttpClients;
import com.facebook.halo.application.types.User;
import com.facebook.halo.framework.common.AccessToken;

/**
 * Intro 클래스
 * SSO 처리 및 C2dm등록 이뤄짐
 * @author JM
 *
 */
public class Intro extends Activity {
	
	Facebook mFacebook;
	
	Button loginButton;
	
	Intent intent = new Intent();

	
	/**
	 * 액티비티 실행시 처음 실행
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mFacebook = new Facebook(Var.APP_ID);
		
		/**
		 * splash activity 
		 */
		setContentView(R.layout.intro);
		
		//이미 로긴 되있으면 바로 시작(자동 로그인 처리)
		if(SessionStore.restore(mFacebook, this)) {
			//TODO
			AccessToken.setAccessToken(mFacebook.getAccessToken());
			c2dmRegister();
			
			loadHandler.sendEmptyMessage(0);
			handler.sendEmptyMessageDelayed(0, 3000);
		} else {
			//facebook 으로 로그인하기
			loginButton = (Button) findViewById(R.id.loginButton);
			loginButton.setVisibility(View.VISIBLE);
			loginButton.setOnClickListener(buttonClickListener);
		}
		
		/**
		 * microlog4 android initialize
		 */
		Microlog4Android.init();
		//test log
		
		Microlog4Android.logger.info("Succesed");
		
	}
	
	/**
	 * c2dm register
	 * access token 받아온 후에 실행되야함
	 */
	private void c2dmRegister() {
	    Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
	    registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
	    registrationIntent.putExtra("sender", "mythopoeic301@gmail.com");
	    startService(registrationIntent);
	}
	
	/**
	 * delay 후 home activity 실행하는 handler
	 */
	Handler handler = new Handler() {
		/**
		 * 메시지 처리부분
		 */
		public void handleMessage(Message msg) {
			finish();
            if(setNewMember())
//            	Toast.makeText(Intro.this, "Success", Toast.LENGTH_SHORT).show();
            intent.setAction("HomeActivity");
            startActivity(intent);
		}
	};

	/**
	 * 서버에서 데이터 가져오는 handler
	 */
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			Repository.getInstance().setUser();
		}
	};

	/**
	 * SSO 로그인 처리
	 */
	View.OnClickListener buttonClickListener = new View.OnClickListener() {
		/**
		 * click 할때
		 */
		@Override
		public void onClick(View v) {
			/**
			 * facebook 인증 부분
			 */
			mFacebook.authorize(Intro.this, Var.PERMISSIONS , new DialogListener() {
				/**
				 * 인증 완료 되었을 때
				 */
                @Override
                public void onComplete(Bundle values) {
                	SessionStore.save(mFacebook, getBaseContext());
                	//TODO
                    AccessToken.setAccessToken(mFacebook.getAccessToken());
                    Repository.getInstance().setUser();
                    
                    c2dmRegister();
                    if(setNewMember())
//                    	Toast.makeText(Intro.this, "Success", Toast.LENGTH_SHORT).show();
                	intent.setAction("HomeActivity");
                    startActivity(intent);
                	finish();
                }
    
                /**
                 * facebook 인증 에러일 때
                 */
                @Override
                public void onFacebookError(FacebookError error) {}
    
                /**
                 * 에러났을경우
                 */
                @Override
                public void onError(DialogError e) {}
    
                /**
                 * 취소일 경우
                 */
                @Override
                public void onCancel() {}
            });
		}
	};
	
	//콜백함수를 사용하기위해
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        mFacebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	/**
	 * 새로운 멤버 서버에 추가
	 * @return
	 */
	private boolean setNewMember() {
		User user = Repository.getInstance().getUser();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fb_key", user.getId()));
		params.add(new BasicNameValuePair("fb_name", user.getName()));
		if(user.getGender().equals("female"))
			params.add(new BasicNameValuePair("fb_gender", "F"));
		else if(user.getGender().equals("male"))
			params.add(new BasicNameValuePair("fb_gender", "M"));
		else
			params.add(new BasicNameValuePair("fb_gender", "0"));
		
		if(user.getBirthday() != null)
			params.add(new BasicNameValuePair("fb_birth", user.getBirthday()));
		else
			params.add(new BasicNameValuePair("fb_birth", "0000-00-00"));
		
		HttpClients client = new HttpClients();
		String data = client.getUrlToJson(MessageUtils.SET_NEW_MEMBER, params);
		
		if(data != null){
			return client.getResult(data);
		} else 
			return false;
	}
	
	/**
	 * 액티비티 종료시 호출
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		stopService(new Intent(this, NotificationService.class));
	}
}
