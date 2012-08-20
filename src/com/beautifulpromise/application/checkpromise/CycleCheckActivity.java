package com.beautifulpromise.application.checkpromise;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beautifulpromise.R;
import com.beautifulpromise.application.HomeActivity;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.ImageUtils;
import com.beautifulpromise.database.CheckDAO;
import com.beautifulpromise.database.CheckDBHelper;
import com.beautifulpromise.parser.Controller;
import com.facebook.halo.application.types.Album;
import com.facebook.halo.application.types.Tags;
import com.facebook.halo.application.types.User;
import com.facebook.halo.application.types.connection.Albums;
import com.facebook.halo.application.types.connection.Friends;
import com.facebook.halo.application.types.connection.Photos;
import com.facebook.halo.application.types.infra.FacebookType;
import com.facebook.halo.framework.core.Connection;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
/**
 * 장소 체크 엑티비티
 * @author ou
 *
 */
public class CycleCheckActivity extends MapActivity {

	CheckDBHelper gps_DBHelper;
	SQLiteDatabase db;
	
	LocationListener mLocationListener;
	MapView mapview;
	MapController mc;
	List<Overlay> overlay;
	
	AddPromiseDTO promiseobject;

	Connection<Friends> friends;
	
	TextView PromiseName_TextView;
	TextView Period_TextView;
	TextView MapHour_TextView;
	EditText Feed_EditBox;
	Button Post_Btn;
	Intent intent = new Intent();
	
	LinearLayout MapView_LinearLayout;

	Double Latitude;
	Double Longitude;

	/**
	 * 생성할때 알림바제거
	 * promiseobject에 해당 목표에 대한 정보를 넣어 각 뷰에 목표에 대한 정보 넣음
	 * gps 데이터 베이스에 있는 정보 받아와서 구글맵으로 뿌려줌
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkpromise_cyclecheck_activity);
		
		//알림바 제거
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(1);

		PromiseName_TextView = (TextView) findViewById(R.id.checkpromise_cyclecheck_promisename_text);
		Period_TextView = (TextView) findViewById(R.id.checkpromise_cyclecheck_period_text);
		MapHour_TextView= (TextView) findViewById(R.id.checkpromise_cyclecheck_maphour_text);
		Feed_EditBox = (EditText) findViewById(R.id.checkpromise_cyclecheck_content_edit);
		Post_Btn = (Button) findViewById(R.id.checkpromise_cyclecheck_post_btn);
		MapView_LinearLayout = (LinearLayout) findViewById(R.id.checkpromise_cyclecheck_mapview_layout);
		
		Post_Btn.setOnClickListener(buttonClickListener);
		
		//home에서 객체 받아오기
		Object tempobject = getIntent().getExtras().get("PromiseDTO");
		promiseobject = (AddPromiseDTO) tempobject;
		
		//약속 제목 텍스트 설정
		PromiseName_TextView.setText(promiseobject.getTitle());
		
		//목표기간 텍스트 설정
		String StartTime = promiseobject.getStartDate();
		StartTime = StartTime.substring(0, 4) + "." + StartTime.substring(4, 6)+ "." + StartTime.substring(6, 8);
		
		String EndTime = promiseobject.getEndDate();
		EndTime = EndTime.substring(0, 4) + "." + EndTime.substring(4, 6)+ "." + EndTime.substring(6, 8);
		
		Period_TextView.setText(StartTime + " ~ " + EndTime);
		
		//mapview 텍스트 설정
		int hour;
		//오전
		if(promiseobject.getTime() < 12)
		{
			hour = promiseobject.getTime();
			if(promiseobject.getTime()==0)
				hour=12;
			MapHour_TextView.setText("오전 " + hour + "시 " +promiseobject.getMin()+"분에");
		}
		//오후
		else
		{
			hour = promiseobject.getTime()-12;
			if(promiseobject.getTime()==0)
				hour=12;
			MapHour_TextView.setText("오후 " + hour + "시 " +promiseobject.getMin()+"분에");
		}
		
		mapview = (MapView) findViewById(R.id.checkpromise_cyclecheck_mapview);
		mapview.setBuiltInZoomControls(true);
		mapview.setSatellite(false);
		mc = mapview.getController();
		
		try{
			
			CheckDBHelper checkDBHelper = new CheckDBHelper(this);
			CheckDAO checkDAO = new CheckDAO(checkDBHelper);
			
			Double [] Location = new Double[2];
			Location = checkDAO.getGPS(promiseobject.getPostId());
			Latitude=Location[0];
			Longitude=Location[1];
			checkDAO.close();
			
			// 위도 경로 입력
			GeoPoint gp = new GeoPoint((int) (Latitude * 1000000),
					(int) (Longitude * 1000000));
			mc.animateTo(gp);
			mc.setZoom(18);

			// PIN만들기
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.comment_write);
			bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false);
			Drawable drawable = new BitmapDrawable(bitmap);
			CycleGpsPinOverlay mdio = new CycleGpsPinOverlay(drawable);
			OverlayItem overlayitem = new OverlayItem(gp, "", "");
			mdio.addOverlayItem(overlayitem);
			overlay = mapview.getOverlays();
			overlay.add(mdio);
		}catch (Exception e) {
			Toast.makeText(this, "GPS좌표값이 저장되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
		}
		

	}

	/**
	 * 각 버튼의 클릭 이벤트메소드
	 * post : 아름다운 약속 앨범에 GPS시간과 구글맵이 사진으로 올려짐
	 * 
	 */
	View.OnClickListener buttonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.checkpromise_cyclecheck_post_btn:
				boolean result;
				MapView_LinearLayout.buildDrawingCache();
				Bitmap captureBitmap = MapView_LinearLayout.getDrawingCache();
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(Environment
							.getExternalStorageDirectory().toString()
							+ "/capture.jpeg");
					captureBitmap
							.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					captureBitmap = Bitmap.createScaledBitmap(captureBitmap,
							MapView_LinearLayout.getWidth(), MapView_LinearLayout.getHeight(),
							true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				String path = ImageUtils.saveBitmap(CycleCheckActivity.this, captureBitmap);

				User user = Repository.getInstance().getUser();

				String albumId = null;
				Connection<Album> albums = user.albums();
				for (List<Album> albumList : albums)
					for (Album album : albumList) {

						System.out.println("Album ID : " + album.getId());
						if (album.getName().equals("아름다운 약속")) {
							albumId = album.getId();
							break;
						}
					}

				if (albumId == null) {
					Albums album = new Albums();
					album.setName("아름다운 약속");
					album.setMessage("당신이 약속을 지킴으로써 소중한 한 생명이 살아날 수 있습니다.");
					albumId = user.publishAlbums(album).getId();
				}

				Photos photos = new Photos();
				photos.setMessage(PromiseName_TextView.getText().toString() + "\n\n"
						+ "목표 기간 : " + Period_TextView.getText().toString() + "\n\n"
						+ Feed_EditBox.getText().toString() + "\n\n\n"
						+ "구글 Play 유료게임 1위!!! 팔라독 다운 받기\n"
						+ "http://bit.ly/LaEn8k\n");
				photos.setSource(path);
				photos.setFileName("Beautiful Promise");
				FacebookType type = user.publishPhotos(albumId, photos);

				Controller ctr = new Controller();
				ArrayList<String> HelperList = ctr.GetHelperList(promiseobject.getPostId());
				if(HelperList.size() != 0)
				{
					ArrayList<Tags> tags = new ArrayList<Tags>();
					for (String helper : HelperList) {
						Tags tag = new Tags();
						tag.setTagUid(helper);
						tags.add(tag);
					}
					result = user.publishTagsAtPhoto(type.getId(), tags);
				}
				else
				{
					result = true;
				}
				
				if (result) {
					Toast.makeText(CycleCheckActivity.this, "성공", Toast.LENGTH_SHORT).show();
					ctr.PublishCheck(promiseobject.getPostId(), type.getId());

					CheckDBHelper checkDBHelper = new CheckDBHelper(CycleCheckActivity.this);
					CheckDAO checkDAO = new CheckDAO(checkDBHelper);
					checkDAO.feedcheckupdate(promiseobject.getPostId(), 1);
					checkDAO.close();
					
					intent.setAction("HomeActivity");
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(CycleCheckActivity.this, "Upload에 실패하였습니다.",
							Toast.LENGTH_SHORT).show();
				}
				break;
				
			default:
				break;
			}
		}
	};

	/**
	 * 구글 맵에 현재 위치를 보여줄 것인가
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * 엑티비티 제거시
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
