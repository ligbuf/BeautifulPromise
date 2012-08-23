package com.beautifulpromise.application.feedviewer;

import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.beautifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.R;
import com.beautifulpromise.application.BeautifulPromiseActivity;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.parser.Controller;
import com.facebook.halo.application.types.Post;
import com.facebook.halo.application.types.User;

/**
 * 피드 리스트 뷰어
 * @author JM
 *
 */
public class PromiseFeedList extends BeautifulPromiseActivity{
	//feed item 들을 담고있는 array
	ArrayList<FeedItemDTO> arrayFeedItem;
	
	//서버에서 받아올 feed item list
	ArrayList<String> toDoList;
	
	//feed item 객체
	FeedItemDTO feedItem;
	
	//server와의 interface 객체
	Controller ctrl;
	
	//사용자 정보를 담고 있는 객체 
	User user;
	
	//피드 정보를 담고 있는 객체
	Post feed;
	
	//progress bar
	LinearLayout feedProgressLayout;

	LinearLayout feedListLayout;
	
	Intent intent;
	String mode;
	String feedId;
	
	//check List에서 볼 경우 true(약속일지 UI 없음)
	boolean isCheck;
	
	FeedListAdapter feedListAdapter;
	ListView feedList;
	
	/**
	 * 액티비티 실행시 처음 실행
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //Layout setting
		feedListLayout = (LinearLayout)View.inflate(this, R.layout.feedviewer_feed_list, null);
		setActivityLayout(feedListLayout);
		
		//variable setting -> setContentView 다음에 나와야함
		setVariable();
		
		FeedLoadAsyncTask task = new FeedLoadAsyncTask();
		task.execute();
		
	}
	
	/**
	 * 각종 변수 초기화
	 */
	private void setVariable() {
		//객체생성
		arrayFeedItem = new ArrayList<FeedItemDTO>();
		toDoList = new ArrayList<String>();
		user = Repository.getInstance().getUser();
		ctrl = new Controller();
		intent = getIntent();
		
		isCheck = intent.getBooleanExtra("isCheck", false);
		mode = intent.getStringExtra("mode");
		feedId = intent.getStringExtra("feedId");
		
		//progress bar
		feedProgressLayout = (LinearLayout) findViewById(R.id.feedProgressLayout);
	
		//list view 생성
		feedList = (ListView)feedListLayout.findViewById(R.id.feedList);
	}
	
	/**
	 * progress bar 처리
	 * @author JM
	 *
	 */
	private class FeedLoadAsyncTask extends AsyncTask<URL, Integer, Long> {

		/**
		 * progress bar 처리시 background 처리
		 */
		@Override
		protected Long doInBackground(URL... params) {
			if(isCheck) { //약속일지 피드 뿌려주는 부분일경우
				Microlog4Android.logger.error("FEED id "+ feedId);
				toDoList = ctrl.GetCheckList(feedId);
			} else { //me, helper, all 경우
				//서버에서 피드 리스트 가져옴
				toDoList = ctrl.GetTodoList(mode);		
				Microlog4Android.logger.error("toDoList mode :" + mode + "" + toDoList);
			}
			
			//가져온 데이터를 arrayList에 담음
			for(String s : toDoList) {
//				Log.e("s : ", "" + s);
				feed = new Post();
				feed = feed.createInstance(s);
//				Log.e("feed : ", "" + feed);
				if(feed != null ) {
					feedItem = new FeedItemDTO(feed);
					arrayFeedItem.add(feedItem);
				}
			}
			return null;
		}

		/**
		 * progress bar 처리시 background 처리 후 실행
		 */
		@Override
		protected void onPostExecute(Long result) {
			//progress bar 없에고, 받아온 데이터 띄워줌
			feedProgressLayout.setVisibility(View.GONE);
			feedList.setVisibility(View.VISIBLE);
			
			//adapter 생성 후 레이아웃&데이터 세팅
			feedListAdapter = new FeedListAdapter(getApplicationContext(), R.layout.feedviewer_feed_item, arrayFeedItem, mode, isCheck);
			
			//list view 와 adapter 연결
			feedList.setAdapter(feedListAdapter);
			
			super.onPostExecute(result);
		}

	}
	
}
