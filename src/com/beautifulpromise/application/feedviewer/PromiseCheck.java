package com.beautifulpromise.application.feedviewer;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.beautifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.R;
import com.beautifulpromise.application.addpromise.AddPromiseActivity;
import com.beautifulpromise.application.addpromise.RepeatDayDialog;
import com.beautifulpromise.application.addpromise.UploadDonationLetterDialog;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.database.DatabaseHelper;
import com.beautifulpromise.database.GoalsDAO;
import com.beautifulpromise.parser.Controller;
import com.facebook.halo.application.types.Post;

/**
 * D-day가 되서 성공/실패를 결정하는 클래스
 * @author JM
 *
 */
public class PromiseCheck extends Activity {
	//feed item 들을 담고있는 array
	ArrayList<FeedItemDTO> arrayFeedItem;
	
	//서버에서 받아올 feed item list
	ArrayList<String> checkList;
	
	//server와의 interface 객체
	Controller ctrl;
	
	//progress bar
	LinearLayout feedProgressLayout;
	
	LinearLayout checkListLayout;
	ListView feedList;
	Button yesBtn;
	Button noBtn;
	Intent intent;
	
	String mode;
	String feedId;
	boolean isCheck;
	
	Post feed;
	
	//feed item 객체
	FeedItemDTO feedItem;
	
	GoalsDAO goalsDAO;
	
	AddPromiseDTO promiseobject;
	
	/**
	 * 액티비티 실행시 처음 실행
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedviewer_check_promise);
		
		setVariable();
		
		FeedLoadAsyncTask task = new FeedLoadAsyncTask();
		task.execute();
		
		
		/**
		 * set yes btn click listener
		 */
		yesBtn.setOnClickListener(new OnClickListener() {
			
			/**
			 * click 시
			 */
			@Override
			public void onClick(View v) {
				//페북에 성공 글 올리기
				UploadDonationLetterDialog.Builder builder = new UploadDonationLetterDialog.Builder(PromiseCheck.this, true, promiseobject);
				Dialog dialog = builder.create();
				dialog.show();
			}
		});
		
		/**
		 * set no btn click listener
		 */
		noBtn.setOnClickListener(new OnClickListener() {
			
			/**
			 * click 시
			 */
			@Override
			public void onClick(View v) {
				//페북에 실패 글 올리기
				UploadDonationLetterDialog.Builder builder = new UploadDonationLetterDialog.Builder(PromiseCheck.this, false, promiseobject);
				Dialog dialog = builder.create();
				dialog.show();
			}
		});
	}
	
	/**
	 * 변수 초기화
	 */
	private void setVariable() {
		//feed item 들을 담고있는 array
		arrayFeedItem = new ArrayList<FeedItemDTO>();
		checkList = new ArrayList<String>();
		ctrl = new Controller();
		
		mode = "me";
		isCheck = true;
		
		//list view 생성
		feedList = (ListView)findViewById(R.id.promiseCheckList);
		
		yesBtn = (Button)findViewById(R.id.yesButton);
		noBtn = (Button)findViewById(R.id.noButton);
		
		//progress bar
		feedProgressLayout = (LinearLayout) findViewById(R.id.checkProgressLayout);
		
		intent = getIntent();
		feedId = intent.getStringExtra("feedId");
		
		//db
		DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
		goalsDAO = new GoalsDAO(databaseHelper);
		
		//intent 
		Object tempobject = getIntent().getExtras().get("PromiseDTO");
		promiseobject = (AddPromiseDTO) tempobject;
	}
	
	/**
	 * progress bar 처리 
	 * @author JM
	 *
	 */
	private class FeedLoadAsyncTask extends AsyncTask<URL, Integer, Long> {

		/**
		 * progress bar 에서 background 처리
		 */
		@Override
		protected Long doInBackground(URL... params) {
			checkList = ctrl.GetCheckList(promiseobject.getPostId());
			
			/**
			 * 가져온 데이터를 arrayList에 담음
			 */
			for(String s : checkList) {
				Microlog4Android.logger.error("s : "+"" + s);
				feed = new Post();
				feed = feed.createInstance(s);
				if(feed != null ) {
					feedItem = new FeedItemDTO(feed);
					arrayFeedItem.add(feedItem);
				}
			}
			return null;
		}

		/**
		 * progress bar 에서 background 처리 후 실행
		 */
		@Override
		protected void onPostExecute(Long result) {
			//progress bar 없에고, 받아온 데이터 띄워줌
			feedProgressLayout.setVisibility(View.GONE);
			feedList.setVisibility(View.VISIBLE);
			
			//adapter 생성 후 레이아웃&데이터 세팅
			FeedListAdapter feedListAdapter = new FeedListAdapter(getApplicationContext(), R.layout.feedviewer_feed_item, arrayFeedItem, mode, isCheck);
			
			//list view 와 adapter 연결
			feedList.setAdapter(feedListAdapter);
			
			super.onPostExecute(result);
		}
	}

}
