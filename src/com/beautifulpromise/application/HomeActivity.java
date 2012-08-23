package com.beautifulpromise.application;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beatutifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.R;
import com.beautifulpromise.common.alarm.Alarm;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.database.CheckDAO;
import com.beautifulpromise.database.CheckDBHelper;
import com.beautifulpromise.database.DatabaseHelper;
import com.beautifulpromise.database.GoalsDAO;
/**
 * @author ou
 * 메인 시작 엑티비티 클래스
 *
 */
public class HomeActivity extends BeautifulPromiseActivity {
	

	ListView PromiseListView;
	MyListAdapter MyAdapter;
	
	int flag = 0;
	AnimationDrawable mAni;
	AlphaAnimation animation1;
	AlphaAnimation animation2;
	ImageView img;

	/**
	 * HomeActivity onCreate메소드
	 * 배너 애니메이션 처리
	 * 배너 클릭이벤트 처리
	 * 알람설정
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.homeactivity, null);
		setActivityLayout(layout);

		PromiseListView = (ListView) findViewById(R.id.home_list);
		// 알람
		ArrayList<AddPromiseDTO> PromiseDTO = GetNotresultPromise();
		Alarm alarm = new Alarm();
		alarm.SetAlarm(this, PromiseDTO);
		
		if(flag == 0) {
			img = (ImageView) findViewById(R.id.home_test);
			
			mAni = new AnimationDrawable();
			
//			animation1 = new AlphaAnimation(1.0f, 0.6f); 
//			animation1.setDuration(1950); 
//		    animation1.setStartOffset(50); 
//			
//		    animation2 = new AlphaAnimation(0.6f, 1.0f); 
//		    animation2.setStartOffset(50);
//		    animation2.setDuration(1950); 
			
			mAni.addFrame((BitmapDrawable) getResources().getDrawable(
					R.drawable.home_banner1), 2000);
			mAni.addFrame((BitmapDrawable) getResources().getDrawable(
					R.drawable.home_banner2), 2000);
			mAni.addFrame((BitmapDrawable) getResources().getDrawable(
					R.drawable.home_banner3), 2000);
			mAni.addFrame((BitmapDrawable) getResources().getDrawable(
					R.drawable.home_banner4), 2000);
			
			mAni.setOneShot(false);
			img.setImageDrawable(mAni);

			mAni.start();
//			img.startAnimation(animation1);
			
			flag++;
		}
//		
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("FullBanner");
				startActivity(intent);
				
			}
		});
		

//	    //animation1 AnimationListener 
//	    animation1.setAnimationListener(new AnimationListener(){ 
//	 
//	        @Override 
//	        public void onAnimationEnd(Animation arg0) { 
//	            // start animation2 when animation1 ends (continue) 
//	        	img.startAnimation(animation2); 
//	        } 
//	 
//	        @Override 
//	        public void onAnimationRepeat(Animation arg0) { 
//	            // TODO Auto-generated method stub 
//	 
//	        } 
//	 
//	        @Override 
//	        public void onAnimationStart(Animation arg0) { 
//	            // TODO Auto-generated method stub 
//	 
//	        } 
//	 
//	    }); 
//	    
//	    //animation2 AnimationListener 
//	    animation2.setAnimationListener(new AnimationListener(){ 
//	 
//	        @Override 
//	        public void onAnimationEnd(Animation arg0) { 
//	            // start animation1 when animation2 ends (repeat) 
//	        	img.startAnimation(animation1); 
//	        } 
//	 
//	        @Override 
//	        public void onAnimationRepeat(Animation arg0) { 
//	            // TODO Auto-generated method stub 
//	 
//	        } 
//	 
//	        @Override 
//	        public void onAnimationStart(Animation arg0) { 
//	            // TODO Auto-generated method stub 
//	 
//	        } 
//	 
//	    }); 
	    
	}


	/**
	 * 리스트뷰 어뎁터 클래스
	 * @author ou
	 *
	 */
	class MyListAdapter extends BaseAdapter implements OnClickListener {
		Context maincon;
		LayoutInflater Inflater;
		ArrayList<AddPromiseDTO> arSrc;
		int layout;

		/**
		 * ListView에 들어갈 어뎁터 객체 설정
		 * @param context
		 * @param alayout
		 * @param aarSrc 리스트에 들어갈 ArrayList<AddPromiseDTO>객체
		 */
		public MyListAdapter(Context context, int alayout,
				ArrayList<AddPromiseDTO> aarSrc) {
			maincon = context;
			Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arSrc = aarSrc;
			layout = alayout;
		}

		/**
		 * ArrayList<AddPromiseDTO>의 size가져옴
		 */
		public int getCount() {
			return arSrc.size();
		}
		/**
		 * ListView에서 선택된 아이템의 위치를 가져옴
		 */
		public AddPromiseDTO getItem(int position) {
			return arSrc.get(position);
		}

		/**
		 * 위치를 가져옴
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * D-Day, D-day가 지나서 평가를 해야되는 약속들
		 * 오늘 피드를 올린 약속
		 * 오늘 피드를 올리지 않은 약속으로 나눠서 리스트뷰에 보여줌
		 * 
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
			}

			TextView promisenametxt = (TextView) convertView
					.findViewById(R.id.promisename);
			TextView d_daytxt = (TextView) convertView.findViewById(R.id.d_day);
			ImageView checkimg = (ImageView) convertView.findViewById(R.id.home_check);

			promisenametxt.setText(arSrc.get(position).getTitle());
			int check = 0;
			
			CheckDBHelper checkDBHelper = new CheckDBHelper(HomeActivity.this);
			CheckDAO checkDAO = new CheckDAO(checkDBHelper);
			
			check = checkDAO.feedcheckdo(arSrc.get(position).getPostId());
			checkDAO.close();
			
			// D-Day, D-day가 지나서 평가를 해야되는 약속들
			if (arSrc.get(position).getResult() == 0 && arSrc.get(position).getD_day() < 1) {
				d_daytxt.setText("D-Day");
				d_daytxt.setTextColor(Color.RED);
				checkimg.setImageResource(R.drawable.ico_finished);
			}
			// 오늘 피드를 올린 약속
			else if(check == 1)
			{
				d_daytxt.setText("D-"+ String.valueOf(arSrc.get(position).getD_day()));
				checkimg.setImageResource(R.drawable.ico_assessment);
			}
			//오늘 피드를 올리지 않은 약속
			else
			{
				d_daytxt.setText("D-" + String.valueOf(arSrc.get(position).getD_day()));
				checkimg.setImageResource(R.drawable.ico_clear);
			}

			convertView.setTag(position);
			convertView.setOnClickListener(this);

			return convertView;
		}

		/**
		 * 리스트뷰 클릭 이벤트
		 * 각 목표의 CategoryID에 맞춰서 각 목표체크 엑티비티로 전환
		 * 완료체크해야되는 목표의 경우는 따로 확인하여 PromiseCheck엑티비티로 전환
		 */
		public void onClick(View v) {
			int position = (Integer) v.getTag();

			AddPromiseDTO promiseObject = getItem(position);
			Intent intent = new Intent();

			Bundle extras = new Bundle();
			extras.putSerializable("PromiseDTO", promiseObject);

			//완료 목표
			if(promiseObject.getResult() == 0 && promiseObject.getD_day() < 1)
			{
				intent.setAction("feedviewer.PromiseCheck");
			}
			// 주기(GPS)
			else if(promiseObject.getCategoryId() == 0) {
				intent.setAction("checkpromise.CycleCheckActivity");
			}
			// 운동/공부 (타이머)
			else if (promiseObject.getCategoryId() == 1) {
				intent.setAction("checkpromise.WorkCheckActivity");
			}
			else {
				intent.setAction("checkpromise.EtcCheckActivity");
			}
			intent.putExtras(extras);
			startActivity(intent);
			}
	}

	/**
	 * 메인 화면이 다시 시작될때마다 리스트뷰를 GetNotresultPromise메소드를 호출,
	 * D-Day계산 하여 리스트 뷰에 다시 출력 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<AddPromiseDTO> PromiseDTO = GetNotresultPromise();
//		Calendar oCalendar = Calendar.getInstance();
//		DatabaseHelper databaseHelper = new DatabaseHelper(this);
//		GoalsDAO goalsDAO = new GoalsDAO(databaseHelper);
//		
//		promisedto = goalsDAO.getGoalList(oCalendar.get(Calendar.DAY_OF_WEEK));
//		
//		int index = promisedto.size();
//		for (int j = 0; j < index; j++) {
//			for (int i = 0; i < promisedto.size(); i++) {
//				if(promisedto.get(i).getResult() != 0)
//				{
//					promisedto.remove(i);
//					break;
//				}
//			}
//		}
		
		// D-day계산해서 객체에 값넣음
		for (int i = 0; i < PromiseDTO.size(); i++) {
			PromiseDTO.get(i).setD_day(PromiseDTO.get(i).getEndDate());
			}

		
		MyAdapter = new MyListAdapter(this, R.layout.homeactivity_list,
				PromiseDTO);
 
		// PromiseListView = (ListView) findViewById(R.id.list);
		PromiseListView.setAdapter(MyAdapter);
		PromiseListView.setItemsCanFocus(false);
		PromiseListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	/**
	 * 서버 측에 오늘 요일에 해야되는 목표를 ArrayList<AddPromiseDTO>로 받음
	 * 이 ArrayList를 오늘 날짜와 완료 날짜를 이용하여 D-Day계산하여 ArrayList<AddPromiseDTO>에 넣음
	 * 그리고 이미 완료 되어 있는 목표는 ArrayList<AddPromiseDTO>에서 뺌
	 * @return promisedto
	 */
	public ArrayList<AddPromiseDTO> GetNotresultPromise() {
		ArrayList<AddPromiseDTO> promisedto;
		Calendar oCalendar = Calendar.getInstance();
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		GoalsDAO goalsDAO = new GoalsDAO(databaseHelper);
		
		promisedto = goalsDAO.getGoalList(oCalendar.get(Calendar.DAY_OF_WEEK));
		
		int index = promisedto.size();
		for (int j = 0; j < index; j++) {
			for (int i = 0; i < promisedto.size(); i++) {
				if(promisedto.get(i).getResult() != 0)
				{
					promisedto.remove(i);
					break;
				}
			}
		}
		return promisedto;
		
	}
}

