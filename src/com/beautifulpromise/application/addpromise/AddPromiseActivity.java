package com.beautifulpromise.application.addpromise;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beautifulpromise.R;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.DateUtils;
import com.beautifulpromise.common.utils.ImageUtils;
import com.facebook.halo.application.types.User;
import com.facebook.halo.application.types.connection.Friends;
import com.facebook.halo.framework.core.Connection;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * @author immk
 * 목표 생성을 위한 Activity
 */
public class AddPromiseActivity extends MapActivity {

	protected static final int TIME_DIALOG_ID = 100;

	LinearLayout progressLayout;
	Button category1Btn;
	Button category2Btn;
	Button category3Btn;
	
	EditText goalTitleEdit;
	Button createBtn;
	
	LinearLayout dateLayout;
	TextView startDateText;
	TextView endDateText;
	
	LinearLayout dayRepeatLayout;
	TextView dayRepeatText;
	
	LinearLayout alarmLayout;
	TextView alarmTimeText;
	
	EditText contentEdit;
	
	LinearLayout helperLayout;
	GridView helperGrid;
	
	Button helperBtn;
	Button mapBtn;
	Button tutorialBtn;
	
	int startYear;
	int startMonth;
	int startDay;
	int endYear;
	int endMonth;
	int endDay;
	AddPromiseDTO promiseDTO;
	
	MapView mapView;
	MapController mapController;
	MyLocationOverlay myLocationOverlay;
	
	Connection<Friends> friends;
	
	/**
	 * 목표 생성 화면 (View의 id 설정, 초기 세팅) 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpromise_main_layout);
        
        setVariable();
		FriendsAsynTask task = new FriendsAsynTask();
		task.execute();
		
        mapView = new MapView(this, getResources().getString(R.string.map_api_key));
        mapView.setClickable(true);
		mapView.displayZoomControls(true);

		mapController = mapView.getController();
		mapController.setZoom(15);
		
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();

		mapView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == event.ACTION_UP){
					Log.i("immk", "ACTION_UP");
					int X = (int) event.getX();
					int Y = (int) event.getY();
					
					GeoPoint geoPoint = mapView.getProjection().fromPixels(X, Y);
					
					mapController.animateTo(geoPoint);
					mapController.setZoom(15); 
					    
				    List<Overlay> mapOverlays = mapView.getOverlays();
		            Drawable drawable = getResources().getDrawable(R.drawable.ico_pin);
		            MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, AddPromiseActivity.this);
		            
		            OverlayItem overlayitem = new OverlayItem(geoPoint, "", "");
		            itemizedoverlay.addOverlay(overlayitem);
		            mapOverlays.clear();
		            mapOverlays.add(itemizedoverlay);
		            
		            promiseDTO.setLatitue(Double.valueOf(geoPoint.getLatitudeE6()));
		            promiseDTO.setLongitude(Double.valueOf(geoPoint.getLongitudeE6()));
		            
		            Log.i("immk", ""+ Double.valueOf(geoPoint.getLatitudeE6()) + " " + Double.valueOf(geoPoint.getLongitudeE6()));
				}
				return false;
			}
		});
		
        dateLayout.setOnClickListener(buttonClickListener);
        dayRepeatLayout.setOnClickListener(buttonClickListener);
        alarmLayout.setOnClickListener(buttonClickListener);
        createBtn.setOnClickListener(buttonClickListener);
        
        helperBtn.setOnClickListener(buttonClickListener);
        mapBtn.setOnClickListener(buttonClickListener);
        tutorialBtn.setOnClickListener(buttonClickListener);
        
        category1Btn.setOnClickListener(buttonClickListener);
        category2Btn.setOnClickListener(buttonClickListener);
        category3Btn.setOnClickListener(buttonClickListener);
        
        helperLayout.setOnClickListener(buttonClickListener);
        
		startYear = DateUtils.getYear();
		startMonth = DateUtils.getMonth();
		startDay = DateUtils.getDay();

		endYear = startYear;
		endMonth = startMonth;
		endDay = startDay + 1;
		
		setDate(startYear, startMonth, startDay, endYear, endMonth, endDay);
		
		promiseDTO = new AddPromiseDTO();
		setView(0);
		promiseDTO.setCategoryId(0);
//		CreateObject(0);
		promiseDTO.setDayPeriod(new boolean[]{false, false, false, false, false, false, false});
		
//		DatabaseHelper databaseHelper = new DatabaseHelper(this);
//		GoalsDAO dao = new GoalsDAO(databaseHelper);
//		ArrayList<AddPromiseDTO> aa = dao.getList();
//		ArrayList<AddPromiseDTO> bb = dao.getGoalList(2);
//		Log.i("immk", bb.get(0).getTitle());
		
//		Controller ctr = new Controller();
//		ArrayList<String> aa = ctr.GetTodoList("me");
//		ArrayList<String> bb = ctr.GetTodoList("helper");
//		ArrayList<String> cc = ctr.GetTodoList("all");
//		int i = 0 ;
//		ArrayList<String> aa = ctr.GetHelperList("113852212089571");
//		ArrayList<String> bb = ctr.GetCheckList("15649845");
//		ArrayList<String> cc = ctr.GetProjectStatus(3);
    }
    
    /**
     * 	버튼 액션 설정 : 시작일/종료일 설정, 알람 주기 설정, 알람 시간 설정, 도우미 선택, 좌표 선택 등등
     */
    View.OnClickListener buttonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.date_layout:
				DateDialog.Builder pickerViewBuilder = new DateDialog.Builder(AddPromiseActivity.this);
				pickerViewBuilder.setDate(startYear, startMonth, startDay, endYear, endMonth, endDay);
				Dialog dateDialog = pickerViewBuilder.create();
				dateDialog.show();
				break;
				
			case R.id.day_repeat_layout:
				RepeatDayDialog.Builder dayBuilder = new RepeatDayDialog.Builder(AddPromiseActivity.this, promiseDTO.getDayPeriod());
				Dialog dayDialog = dayBuilder.create();
				dayDialog.show();
				break;
				
			case R.id.alarm_time_layout:
				showDialog(TIME_DIALOG_ID);
				break;
				
			case R.id.helper_layout:
				
			case R.id.helper_button:
				FriendViewDialog.Builder friendViewBuilder = new FriendViewDialog.Builder(AddPromiseActivity.this, friends.getData(), promiseDTO.getHelperList());
				Dialog friendDialog = friendViewBuilder.create();
				friendDialog.show();
				break;
				
			case R.id.google_map_button:
				MapViewDialog dialog = new MapViewDialog(AddPromiseActivity.this, R.style.Theme_Dialog, mapView);
				dialog.show();
				break;
				
			case R.id.tutorial_button:
				break;
				
			case R.id.create_button:
				
				//TODO promiseDTO 객체에 정보 담기  
				promiseDTO.setTitle(goalTitleEdit.getText().toString());
				promiseDTO.setStartDate(startDateText.getText().toString());
				promiseDTO.setEndDate(endDateText.getText().toString());
				promiseDTO.setContent(contentEdit.getText().toString());
				
				SignViewDialog.Builder signBuilder = new SignViewDialog.Builder(AddPromiseActivity.this, promiseDTO);
				Dialog signDialog = signBuilder.create();
				signDialog.show();
				break;
				
			case R.id.category_1_button:
				setView(0);
				promiseDTO.setCategoryId(0);
//				CreateObject(0);
				break;
				
			case R.id.category_2_button:
				setView(1);
				promiseDTO.setCategoryId(1);
//				CreateObject(1);
				break;
				
			case R.id.category_3_button:
				setView(2);
				promiseDTO.setCategoryId(2);
//				CreateObject(2);
				break;
				
			default:
				break;
			}	
		}
	};

    
    /**
     * 	알람 시간 설정
     */
	TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			alarmTimeText.setText(""+ hourOfDay + "시 "  + minute + "분");
			promiseDTO.setTime(hourOfDay);
			promiseDTO.setMin(minute);
		}
	};
	
	/**
	 * 알람 시간 설정
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			final Calendar calendar = Calendar.getInstance();
		    int mHour = calendar.get(Calendar.HOUR_OF_DAY);
		    int  mMinute = calendar.get(Calendar.MINUTE);
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
		}
		return null;
	};
	

	/**
	 * 화면 세팅 
	 * @param position 카테고리 position
	 */
	private void setView (int position){
		
		switch (position) {
		case 0:
			category1Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_1_select));
			category2Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_2));
			category3Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_3));
			
			dayRepeatLayout.setVisibility(View.VISIBLE);
			alarmLayout.setVisibility(View.VISIBLE);
			mapBtn.setVisibility(View.VISIBLE);
			break;
		case 1:
			category1Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_1));
			category2Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_2_select));
			category3Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_3));
			
			dayRepeatLayout.setVisibility(View.VISIBLE);
			alarmLayout.setVisibility(View.VISIBLE);
			mapBtn.setVisibility(View.GONE);
			break;
		case 2:
			category1Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_1));
			category2Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_2));
			category3Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_3_select));			
			
			dayRepeatLayout.setVisibility(View.GONE);
			alarmLayout.setVisibility(View.GONE);
			mapBtn.setVisibility(View.GONE);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * 날짜 설정
	 * @param startYear 시작년도
	 * @param startMonth 시작 월
	 * @param startDay 시작 날짜
	 * @param endYear 끝나는 년도
	 * @param endMonth 끝나는 월
	 * @param endDay 끝나는 날짜
	 */
	public void setDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay){
		startDateText.setText(""+ startYear + "년 " + (startMonth+1) + "월 " + startDay + "일");
		endDateText.setText(""+ endYear + "년 " + (endMonth+1) + "월 " + endDay + "일");
		this.startYear = startYear;
		this.startMonth = startMonth;
		this.startDay = startDay;
		this.endYear = endYear;
		this.endMonth = endMonth;
		this.endDay = endDay;
	}
	
    /**
     *  변수명 설정
     */
	private void setVariable(){
		
		progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
//		goalSpinner = (Spinner) findViewById(R.id.goal_spinner);
		
		category1Btn = (Button) findViewById(R.id.category_1_button);
		category2Btn = (Button) findViewById(R.id.category_2_button);
		category3Btn = (Button) findViewById(R.id.category_3_button);
		
		createBtn = (Button) findViewById(R.id.create_button);
		createBtn = (Button) findViewById(R.id.create_button);
		
        createBtn = (Button) findViewById(R.id.create_button);
    	goalTitleEdit = (EditText) findViewById(R.id.goal_title_edit);

        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        startDateText = (TextView) findViewById(R.id.start_date_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);
        
        dayRepeatLayout = (LinearLayout) findViewById(R.id.day_repeat_layout);
        dayRepeatText = (TextView) findViewById(R.id.day_repeat_text);
        
        alarmLayout = (LinearLayout) findViewById(R.id.alarm_time_layout);
        alarmTimeText = (TextView) findViewById(R.id.alarm_time_text);
        
        contentEdit = (EditText) findViewById(R.id.content_edit);
        
        helperGrid = (GridView) findViewById(R.id.friend_image_gridview);
        helperGrid.setFocusable(false);
        
        helperLayout = (LinearLayout) findViewById(R.id.helper_layout);
        helperBtn = (Button) findViewById(R.id.helper_button);
        mapBtn = (Button) findViewById(R.id.google_map_button);
        tutorialBtn = (Button) findViewById(R.id.tutorial_button);
	}

	@Override
	protected boolean isRouteDisplayed() {		
		return false;
	}

	/**
	 * 친구 목록 가져오기
	 * @return 친구 목록 리스트
	 */
	private List<Friends> getFriendList(){
		User user = Repository.getInstance().getUser();
		friends = user.friends();
		return friends.getData();
	}
	
	/**
	 * 친구 목록 설정 및 화면에 세팅
	 * @param helperList 도우미 목록
	 */
	public void setHelperFriends(ArrayList<Friends> helperList){
		
		if(helperList != null && helperList.size()>0){
			promiseDTO.setHelperList(helperList);
		}else{
			promiseDTO.setHelperList(null);
		}
		
		ArrayList<Object> helper = new ArrayList<Object>();
		for(Friends friend : helperList){
			helper.add(friend);
		}
		FriendImageAdapter adapter = new FriendImageAdapter(this, helper);
		helperGrid.setAdapter(adapter);
	}

	/**
	 * 도우미 목록을 이미지로 변환
	 * @return Bitmap
	 */
	public Bitmap getHelperImage() {
		helperGrid.setDrawingCacheEnabled(false);
		return ImageUtils.capture(helperGrid);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
//		locationManager.removeUpdates(locationListener);
	}

	@Override
	protected void onResume() {
		super.onResume();	
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {

			public void run() {
				mapController.setCenter(myLocationOverlay.getMyLocation());
				mapView.getOverlays().add(myLocationOverlay);
			}
		});
	}
	
	public void visibleProgress(){
		progressLayout.setVisibility(View.VISIBLE);
	}

	public void goneProgress(){
		progressLayout.setVisibility(View.GONE);
	}
	
	public class FriendsAsynTask extends AsyncTask<URL, Integer, Long> {
		
		@Override
		protected Long doInBackground(URL... params) {
			getFriendList();
			return 0L;
		}

		@Override
		protected void onPreExecute() {
			progressLayout.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Long result) {
			goneProgress();
		}
	}
	
//	private void CreateObject(int key) {
//		
//		switch (key) {
//		case 0: 	// 장소
//			promiseDTO.setTitle("수영학원 빠지지 말기!!!");
//			promiseDTO.setContent("꾸준히 다녀보자!");
//			promiseDTO.setEndDate("2012년 6월 30일");
//			promiseDTO.setLatitue(3.749611E7);
//			promiseDTO.setLongitude(1.27051993E8);
//			promiseDTO.setDayPeriod(new boolean[]{true, true, true, true, true, true, true});
//			promiseDTO.setTime(10);
//			promiseDTO.setMin(30);
//			break;
//		case 1:  	// 시간
//			promiseDTO.setTitle("토익 800점 맞기~");
//			promiseDTO.setContent("1. 하루 30분 공부하기 \n2. 영어 단어 50개 암기");
//			promiseDTO.setEndDate("2012년 7월 7일");
//			promiseDTO.setDayPeriod(new boolean[]{true, true, true, true, true, true, true});
//			promiseDTO.setTime(10);
//			promiseDTO.setMin(0);
//			break;
//		case 2:		// 기타
//			promiseDTO.setTitle("금주 하기!");
//			promiseDTO.setContent("1. 아침 9시에 일어나기 \n2. 2시에 자기");
//			promiseDTO.setEndDate("2012년 7월 20일");
//			break;			
//		default:
//			break;
//		}
//	}
	
	/**
	 * 반복 주기 설정 및 화면에 보여주기
	 * @param dayArr 선택된 날짜 배열
	 */
	public void setRepeatDay(boolean[] dayArr){
		promiseDTO.setDayPeriod(dayArr);
		String day = "";
		if(dayArr[0]){
			day += "월 ";
		}
		if(dayArr[1]){
			day += "화 ";
		}
		if(dayArr[2]){
			day += "수 ";
		}
		if(dayArr[3]){
			day += "목 ";
		}
		if(dayArr[4]){
			day += "금 ";
		}
		if(dayArr[5]){
			day += "토 ";
		}
		if(dayArr[6]){
			day += "일 ";
		}
		
		if(day.equals("월 화 수 목 금 토 일 "))
			dayRepeatText.setText("매일");
		else if(day.equals("월 화 수 목 금 "))
			dayRepeatText.setText("주중");
		else if(day.equals("토 일 "))
			dayRepeatText.setText("주말");
		else 
			dayRepeatText.setText(day);
	}
}
