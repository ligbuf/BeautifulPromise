package com.beautifulpromise.application;

import org.slf4j.impl.MicrologLoggerAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.beatutifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.R;
import com.beautifulpromise.common.Var;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.database.NotificationProvider;
import com.beautifulpromise.facebooklibrary.Facebook;
import com.beautifulpromise.facebooklibrary.SessionStore;
import com.facebook.halo.application.types.Notifications;
import com.facebook.halo.application.types.User;
import com.facebook.halo.framework.core.Connection;
import com.google.code.microlog4android.format.PatternFormatter;


import com.google.code.microlog4android.*;

/**
 * Side navigation + Top menu bar를 담고 있는 액티비티
 * @author JM
 *
 */
public class BeautifulPromiseActivity extends Activity{

	Facebook mFacebook; 
	
	ImageButton leftMenuBtn;
	ImageButton homeBtn;
	ImageButton notificationBtn;
	ImageView newImage;
	ListView notificationListView;
	
	LinearLayout notificationLayout;
	LinearLayout leftMenuLayout;
	LinearLayout activityLayout;
	
	LinearLayout addPromiseBtn;
	LinearLayout myPromiseBtn;
	LinearLayout helperPromiseBtn;
	LinearLayout friendPromiseBtn;
	LinearLayout pointShopBtn;
	LinearLayout settingBtn;
	
	LinearLayout pointShopItemLayout1;
	LinearLayout pointShopItemLayout2;
	
	HorizontalScrollView hscroll;
	private int leftWidth = Var.LEFT_MENUBAR_WIDTH;
	
	NotificationAdapter adapter;
	ContentObserver observer;
	Cursor cursor;
	
	Intent intent = new Intent();
	Handler handler;
	Connection<Notifications> notificaitons;
		
	/**
	 * 엑티비티 실행시 처음 실행
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		Var.CONTEXT = this;
		
		leftMenuBtn = (ImageButton) findViewById(R.id.left_menu_button);
		homeBtn = (ImageButton) findViewById(R.id.home_button);
		notificationBtn = (ImageButton) findViewById(R.id.notification_button);
		newImage = (ImageView) findViewById(R.id.new_feeds_image);
		
		addPromiseBtn = (LinearLayout) findViewById(R.id.addPromiseLayout);
		myPromiseBtn = (LinearLayout) findViewById(R.id.myPromiseLayout);
		helperPromiseBtn = (LinearLayout) findViewById(R.id.helperPromiseLayout);
		friendPromiseBtn = (LinearLayout) findViewById(R.id.friendPromiseLayout);
		pointShopBtn = (LinearLayout) findViewById(R.id.pointShopLayout);
		settingBtn = (LinearLayout) findViewById(R.id.accountLayout);
		
		notificationLayout  = (LinearLayout) findViewById(R.id.notification_layout);
		notificationListView = (ListView) findViewById(R.id.notification_listview);
		leftMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
		activityLayout = (LinearLayout) findViewById(R.id.activity_layout);
		pointShopItemLayout1 = (LinearLayout) findViewById(R.id.pointshopItemLayout1);
		pointShopItemLayout2 = (LinearLayout) findViewById(R.id.pointshopItemLayout2);
		
		leftMenuBtn.setOnClickListener(clickLisetner);
		homeBtn.setOnClickListener(clickLisetner);
		notificationBtn.setOnClickListener(clickLisetner);
		
		addPromiseBtn.setOnClickListener(clickLisetner);
		myPromiseBtn.setOnClickListener(clickLisetner);
		helperPromiseBtn.setOnClickListener(clickLisetner);
		friendPromiseBtn.setOnClickListener(clickLisetner);
		pointShopBtn.setOnClickListener(clickLisetner);
		settingBtn.setOnClickListener(clickLisetner);
		
		pointShopItemLayout1.setOnClickListener(clickLisetner);
		pointShopItemLayout2.setOnClickListener(clickLisetner);
		
		hscroll = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
		mySmoothScrollTo(leftWidth, 0);
		hscroll.setOnTouchListener(hscrollTouchListener);
		
		notificationLayout.setVisibility(View.GONE);
	
		cursor = managedQuery(NotificationProvider.CONTENT_URI, null, null, null, null);

		cursor.moveToFirst();
		adapter = new NotificationAdapter(this, cursor);
		notificationListView.setAdapter(adapter);
		
	    visibleNewImage();

	} 
	
	/**
	 * Click listener
	 */
	View.OnClickListener clickLisetner = new View.OnClickListener() {
		/**
		 * click 할때
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.left_menu_button:
					if(!Var.menuShowFlag){
						mySmoothScrollTo(0, 0);
					}else{
						mySmoothScrollTo(leftWidth, 0);
					}
				break;

			case R.id.home_button:
				intent.setAction("HomeActivity");
				startActivity(intent);
				finish();
				break;
				
			case R.id.notification_button:
				
				if(notificationLayout.isShown()){
					notificationLayout.setVisibility(View.GONE);
					notificationLayout.setVisibility(View.GONE);
				} else {
					goneNewImage();
					notificationLayout.setVisibility(View.VISIBLE);
					notificationLayout.setVisibility(View.VISIBLE);
				}
				break;	
				
			case R.id.addPromiseLayout:
				mySmoothScrollTo(leftWidth, 0);
				intent.setAction("addpromise.AddPromiseActivity");
				startActivity(intent);
				break;

			case R.id.myPromiseLayout:
				mySmoothScrollTo(leftWidth, 0);
				intent.setAction("feedviewer.PromiseFeedList");
				intent.putExtra("mode", "me");
				startActivity(intent);
				break;
				
			case R.id.helperPromiseLayout:
				mySmoothScrollTo(leftWidth, 0);
				intent.setAction("feedviewer.PromiseFeedList");
				intent.putExtra("mode", "helper");
				startActivity(intent);
				break;
				
			case R.id.friendPromiseLayout:
				mySmoothScrollTo(leftWidth, 0);
				intent.setAction("feedviewer.PromiseFeedList");
				intent.putExtra("mode", "all");
				startActivity(intent);
				break;
				
			case R.id.pointShopLayout:
				mySmoothScrollTo(leftWidth, 0);
				intent.setAction("PointShopActivity");
				startActivity(intent);
				break;
				
			case R.id.accountLayout:
				new AlertDialog.Builder(BeautifulPromiseActivity.this)
				.setTitle("로그아웃")
				.setMessage("로그아웃 하시겠습니까?")
				.setIcon(R.drawable.icon)
				.setPositiveButton("확인", logoutClickListener)
				.setNegativeButton("취소", null)
				.show();
				break;
				
			case R.id.pointshopItemLayout1:
				break;
			
			case R.id.pointshopItemLayout2:
				break;
				
			default:
				break;
			}
		}
	};
	
	/**
	 * Logout dialog 확인 click listener
	 */
	DialogInterface.OnClickListener logoutClickListener = new DialogInterface.OnClickListener() {
		
		/**
		 * click 할때
		 */
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mFacebook = new Facebook(Var.APP_ID);
			SessionStore.clear(BeautifulPromiseActivity.this);
			
			intent = new Intent(BeautifulPromiseActivity.this, Intro.class);
			startActivity(intent);
		}
	};

	
	/**
	 * horizontal scroll touch listener
	 */
	View.OnTouchListener hscrollTouchListener = new OnTouchListener() {
		/**
		 * click 할때
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				int x = hscroll.getScrollX();
				hscrollCheck(x, 0);
			}
			return false;
		}
	};
	
	/**
	 * notification 변경된 부분 refresh
	 */
	public void refreshNotification() {
		Log.e("Refresh", "notification");
		User user = Repository.getInstance().getUser();
		notificaitons = user.notifications();
		if (notificaitons.getData().size() > 0) {
			ContentResolver cr = getContentResolver();
			for (Notifications notification : notificaitons.getData()) {
				// Uri notiUri =
				// ContentUris.withAppendedId(NotificationProvider.CONTENT_URI,
				// id);
				// Cursor cursor = cr.query(NotificationProvider.CONTENT_URI,
				// new String[]{"_id"}, "fb_id=?", new
				// String[]{notification.getId()}, null);
				Cursor cursor = cr.query(NotificationProvider.CONTENT_URI,
						new String[] { "_id" }, "fb_id=?",
						new String[] { notification.getId() }, null);
				if (cursor.getCount() == 0) {
					ContentValues row = new ContentValues();
					row.put("title", notification.getTitle());
					row.put("send_user_id", notification.getFrom().getId());
					row.put("fb_id", notification.getId());
					cr.insert(NotificationProvider.CONTENT_URI, row);
					visibleNewImage();
				}
			}
		}
	}

	/**
	 * horizontal scroll check(open or close)
	 * @param x
	 * @param y
	 */
	public void hscrollCheck(int x, int y) {
		if (x <= leftWidth) {	
			if (x <= leftWidth /2) { 			// 왼쪽 메뉴 보이게
				mySmoothScrollTo(0, y);
			} else {							// 왼쪽 메뉴 사라지게
				mySmoothScrollTo(leftWidth, y);
			}
		}
	}
	
	/**
	 * smooth 하게 side navigation 움직이기
	 * @param x
	 * @param y
	 */
	protected void mySmoothScrollTo(final int x, final int y) {
		/**
		 * scroll 옮김
		 */
		hscroll.post(new Runnable() {
			/**
			 * thread 실행
			 */
			@Override
			public void run() {
				if(x == 0)
					Var.menuShowFlag = true;
				else
					Var.menuShowFlag = false;
				
				hscroll.smoothScrollTo(x, y);
			}
		});
	}
	
	/**
	 * setContentView with BeautifulpromiseActivity
	 * @param layout
	 */
	protected void setActivityLayout(LinearLayout layout){
		activityLayout.addView(layout);
	}
	
	/**
	 * setContentView with BeautifulpromiseActivity
	 * @param layout
	 */
	protected void setActivityLayout(ImageView layout){
		activityLayout.addView(layout);
	}
	
	/**
	 * visible notification new Image
	 */
	public void visibleNewImage(){
		if(newImage.getVisibility() == View.INVISIBLE)
			newImage.setVisibility(View.VISIBLE);
	}
	
	/**
	 * gone notification new Image 
	 */
	public void goneNewImage(){
		newImage.setVisibility(View.INVISIBLE);
	}
}
