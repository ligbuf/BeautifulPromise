package com.beautifulpromise.application;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
 * main 화면에 Full Banner 
 * @author JM
 *
 */
public class FullBanner extends Activity {
	/**
	 * 액티비티 실행시 호출
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullbanner);
		}
}
