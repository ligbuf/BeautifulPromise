package com.beautifulpromise.application.checkpromise;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

/**
 * 시간 목표 체크 한것을 피드하는 엑티비티
 * @author ou
 *
 */
public class WorkCheckFeedActivity extends Activity{
	private static final int CROP_FROM_CAMERA = 1;
	TextView PromiseName_TextView;
	TextView Period_TextView;
	TextView Content_TextView;
	EditText Feed_EditBox;
	ImageView Upload_ImageView;
	
	Button PostBtn;
	Button CameraBtn;
	
	Intent intent;
	AddPromiseDTO promiseobject;
	
	/**
	 * 가져온 목표에 맞게 뷰들 텍스트 설정
	 * 가져온 목표를 수행한 시간도 텍스트 뷰에 세팅
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkpromise_workcheck_feed_activity);
		
		PromiseName_TextView = (TextView) findViewById(R.id.checkpromise_workcheckfeed_promisename_text);
		Period_TextView = (TextView) findViewById(R.id.checkpromise_workcheckfeed_period_text);
		PostBtn = (Button) findViewById(R.id.checkpromise_workcheckfeed_post_btn);
		CameraBtn = (Button) findViewById(R.id.checkpromise_workcheckfeed_camera_btn);
		Content_TextView = (TextView)findViewById(R.id.checkpromise_workcheckfeed_content_text);
		Upload_ImageView = (ImageView)findViewById(R.id.checkpromise_workcheckfeed_uploadimage_img);
		Feed_EditBox = (EditText)findViewById(R.id.checkpromise_workcheckfeed_content_edit);
		
		Intent intent= getIntent();
		String time= intent.getStringExtra("Time");
		Content_TextView.setText(time);
		
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
		
		PostBtn.setOnClickListener(buttonClickListener);
		CameraBtn.setOnClickListener(buttonClickListener);
		
	}
	
	/**
	 * post, camera 버튼 이벤트 처리
	 * 
	 */
	View.OnClickListener buttonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.checkpromise_workcheckfeed_post_btn:
				
				boolean result;
				Upload_ImageView.buildDrawingCache();
				Bitmap captureBitmap = Upload_ImageView.getDrawingCache();
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(Environment
							.getExternalStorageDirectory().toString()
							+ "/capture.jpeg");
					captureBitmap
							.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					captureBitmap = Bitmap.createScaledBitmap(captureBitmap,
							Upload_ImageView.getWidth(), Upload_ImageView.getHeight(),
							true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				String path = ImageUtils.saveBitmap(WorkCheckFeedActivity.this, captureBitmap);

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
						+ Content_TextView.getText().toString() + "\n\n"
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
					boolean aa = ctr.PublishCheck(promiseobject.getPostId(), type.getId());
					
					CheckDBHelper checkDBHelper = new CheckDBHelper(WorkCheckFeedActivity.this);
					CheckDAO checkDAO = new CheckDAO(checkDBHelper);
					checkDAO.feedcheckupdate(promiseobject.getPostId(), 1);
					checkDAO.close();
					
					intent.setAction("HomeActivity");
					startActivity(intent);
					
					Toast.makeText(WorkCheckFeedActivity.this, "성공",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(WorkCheckFeedActivity.this, "Upload에 실패하였습니다.",
							Toast.LENGTH_SHORT).show();
				}
				break;
			
			case R.id.checkpromise_workcheckfeed_camera_btn:
				CameraDialog.Builder cameraBuilder = new CameraDialog.Builder(WorkCheckFeedActivity.this);
				Dialog cameraDialog = cameraBuilder.create();
				cameraDialog.show();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 다이얼로그에서 받아온 값을 이용해 사진 찍기, 앨범 선택, 취소 버튼 처리
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Uri imageUri;
		Bitmap bitmap;
		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP_FROM_CAMERA:
			{
				// 크롭이 된 이후의 이미지를 넘겨 받습니다. 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
				// 임시 파일을 삭제합니다.
				final Bundle extras = data.getExtras();
	
				if(extras != null)
				{
					Bitmap photo = extras.getParcelable("data");
					Upload_ImageView.setImageBitmap(photo);
				}
	
	
				break;
			}
			
			case CameraDialog.FINISH_TAKE_PHOTO:
				bitmap = (Bitmap) data.getExtras().get("data"); 
				String path = ImageUtils.saveBitmap(WorkCheckFeedActivity.this, bitmap);
				Upload_ImageView.setImageBitmap(bitmap);
				break;
				
			case CameraDialog.FINISH_GET_IMAGE:
				imageUri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(imageUri, proj, null, null, null);
				cursor.moveToFirst();
				String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
				bitmap = ImageUtils.getResizedBitmap(imagePath);
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
	
				intent.putExtra("outputX", Upload_ImageView.getWidth()/2);
				intent.putExtra("outputY", Upload_ImageView.getHeight()/2);
				intent.putExtra("aspectX", Upload_ImageView.getWidth());
				intent.putExtra("aspectY", Upload_ImageView.getHeight());
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CROP_FROM_CAMERA);
//				Upload_ImageView.setImageBitmap(bitmap);

				break;
			}
		}
	}
}
