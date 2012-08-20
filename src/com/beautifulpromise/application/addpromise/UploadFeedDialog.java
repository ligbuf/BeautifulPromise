package com.beautifulpromise.application.addpromise;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautifulpromise.R;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.DateUtils;
import com.beautifulpromise.common.utils.ImageUtils;
import com.beautifulpromise.database.DatabaseHelper;
import com.beautifulpromise.database.GoalsDAO;
import com.facebook.halo.application.types.Album;
import com.facebook.halo.application.types.Tags;
import com.facebook.halo.application.types.User;
import com.facebook.halo.application.types.connection.Albums;
import com.facebook.halo.application.types.connection.Friends;
import com.facebook.halo.application.types.connection.Photos;
import com.facebook.halo.application.types.infra.FacebookType;
import com.facebook.halo.framework.core.Connection;

/**
 * @description 서약서 Upload를 위한 Dialog (내부 Database 저장, 서버 저장, Facebook에 서약서 Upload )
 * @author immk
 */
public class UploadFeedDialog extends Dialog{
	
	/**
	 * Custom Dialog 생성자
	 * @param context Context
	 * @param theme 테마 설정
	 */
    public UploadFeedDialog(Context context, int theme) {
        super(context, theme);
    }
	
    /**
     * Custom Dialog 생성자
     * @param context Context
     */
	public UploadFeedDialog(Context context) {
		super(context);
	}
	
	/**
	 * Custom Dialog View 설정
	 * @author immk
	 */
	public static class Builder {
    	 
        private Context context;
    	private UploadFeedDialog dialog;
    	
    	LinearLayout progressLayout;
    	LinearLayout promiseLayout;
    	ImageView donationImage;
    	TextView titleText;
    	TextView donationTitle;
    	TextView dateText;
    	ImageView signImage;
//    	TextView subContentText;
    	LinearLayout helperLayout;
    	
    	Button okayBtn;
    	Button cancelBtn;
    	
    	View layout;
    	AddPromiseDTO promiseDTO;
    	String path;
    	
    	int count = 0;
        
    	/**
    	 * Custom Dialog Builder 생성자
    	 * @param context
    	 */
        public Builder(Context context, AddPromiseDTO promiseDTO) {
        	this.context = context;
        	this.promiseDTO = promiseDTO;
		}

        /**
         * UploadFeedDialog 생성
         * @return Dialog
         */
		public UploadFeedDialog create() {
        	        	
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	dialog = new UploadFeedDialog(context, R.style.Theme_Dialog);
            layout = inflater.inflate(R.layout.addpromise_upload_feed_dialog, null);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            progressLayout = (LinearLayout) layout.findViewById(R.id.progressLayout);
            promiseLayout = (LinearLayout) layout.findViewById(R.id.promise_layout);
            
            donationImage = (ImageView) layout.findViewById(R.id.donation_image_view);
            titleText = (TextView) layout.findViewById(R.id.content_text);
            donationTitle = (TextView) layout.findViewById(R.id.donation_title_text);
            dateText = (TextView) layout.findViewById(R.id.date_text);
            signImage = (ImageView) layout.findViewById(R.id.sign_image);
//            subContentText = (TextView) layout.findViewById(R.id.sub_content_textview);
            helperLayout = (LinearLayout) layout.findViewById(R.id.helper_layout);
            
            okayBtn = (Button) layout.findViewById(R.id.okay_button);
            cancelBtn = (Button) layout.findViewById(R.id.cancel_button);
            
            donationImage.setImageDrawable(promiseDTO.getDonation().getDrawable());
            titleText.setText(setContent());
            donationTitle.setText(setDontationText());
            dateText.setText(DateUtils.getDate());
            signImage.setImageBitmap(promiseDTO.getSignBitmap());
            
//            subContentText.setText(promiseDTO.getContent());
            Bitmap bitmap = ((AddPromiseActivity)context).getHelperImage();
            helperLayout.setBackgroundDrawable(ImageUtils.bitmapToDrawable(bitmap));
            
            okayBtn.setOnClickListener(buttonClickListener);
            cancelBtn.setOnClickListener(buttonClickListener);
            progressLayout.setVisibility(View.GONE);
            dialog.setContentView(layout);			
            return dialog;
        }
		
		View.OnClickListener buttonClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.okay_button:
					// Feed Upload
					promiseLayout.buildDrawingCache();
					Bitmap captureBitmap = promiseLayout.getDrawingCache();
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/capture.jpeg");
						captureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						captureBitmap = Bitmap.createScaledBitmap(captureBitmap, promiseLayout.getWidth(), promiseLayout.getHeight(), true);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					path = ImageUtils.saveBitmap(context, captureBitmap);
					FacebookAsynTask task = new FacebookAsynTask();
					task.execute();
					break;
					
				case R.id.cancel_button:
					dialog.cancel();
					break;
				default:
					break;
				}
			}
		};

		/**
		 * '아름다운 서약서'의 타이틀 설정
		 * @return String
		 */
		private String setContent(){
			
			String str;
			
			str = promiseDTO.getStartDate() + " ~ " + promiseDTO.getEndDate() 
					+ "까지 " + "\"" + promiseDTO.getTitle() + "\" 목표를 성실히 수행하겠습니다. ";
			return str; 
		}
		
		/**
		 * '아름다운 서약서'의 캠페인 설정
		 * @return String
		 */
		private String setDontationText(){
			String str;
			
			str = "이 목표는 " + promiseDTO.getDonation().getTitle() + "과 함께 합니다.";
			return str; 
		}
		
		/**
		 * '아름다운 서약서'를 페이스북에  업로드 / 도움을 주는 사람 태깅 하기 위한 AsyncTask
		 * @author immk
		 */
		public class FacebookAsynTask extends AsyncTask<URL, Integer, Long> {
			
			boolean result;
			String facebookId;
			
			@Override
			protected Long doInBackground(URL... params) {
				
				User user = Repository.getInstance().getUser();
				String albumId = null ;
				Connection<Album> albums = user.albums();
				for(List<Album> albumList : albums)
					for(Album album : albumList){
						if(album.getName().equals("아름다운 약속")) {
							albumId = album.getId();
							break;
						}
					}
				
				if(albumId == null){
					Albums album = new Albums();
					album.setName("아름다운 약속");
					album.setMessage("아름다운 서약서");
					albumId = user.publishAlbums(album).getId();
				}
						
				Photos photos = new Photos();
				photos.setMessage(promiseDTO.getContent() + "\n\n" + "[Tiny Village] 앱 다운 받기" + "\n" + "http://bit.ly/yBZAXu");
				photos.setSource(path);
				photos.setFileName("Beautiful Promise");
				FacebookType type = user.publishPhotos(albumId, photos);
				facebookId = type.getId();
				promiseDTO.setPostId(facebookId);
				
				ArrayList<Tags> tags = new ArrayList<Tags>();
				int count=0;
				if(promiseDTO.getHelperList() != null && promiseDTO.getHelperList().size() > 0){
					for(Friends friend : promiseDTO.getHelperList()){
						count++;
						Tags tag = new Tags();
						tag.setTagUid(friend.getId());
						tag.setX(""+(20*count));
						tag.setY(""+(60));
						tags.add(tag);
					}
					result = user.publishTagsAtPhoto(facebookId, tags);
				}else 
					result = true;
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
				if(this.result){
					DatabaseAsynTask dbTask = new DatabaseAsynTask();
					dbTask.execute();
					ServerAsynTask task = new ServerAsynTask(facebookId);
					task.execute();
				}
			}
		}
		
		/**
		 * '아름다운 서약서' 정보를 내부 DB에 저장
		 * @author immk
		 */
		public class DatabaseAsynTask extends AsyncTask<URL, Integer, Long> {

			@Override
			protected Long doInBackground(URL... params) {
				DatabaseHelper databaseHelper = new DatabaseHelper(context);
				GoalsDAO dao = new GoalsDAO(databaseHelper);
				boolean isCheck = dao.insert(promiseDTO);
//				Log.i("immk", "" + isCheck);
				count++;
				return 0L;
			}

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected void onProgressUpdate(Integer... progress) {
			}

			@Override
			protected void onPostExecute(Long result) {
				if(count == 2){
					progressLayout.setVisibility(View.GONE);
					dialog.dismiss();
					((Activity)context).finish();
				}
			}
		}
		
		/**
		 * '아름다운 서약서' 정보를 외부 DB에 저장
		 * @author immk
		 */		public class ServerAsynTask extends AsyncTask<URL, Integer, Long> {
			
			String facebookId;
			public ServerAsynTask(String facebookId){
				this.facebookId = facebookId;
			}
			
			@Override
			protected Long doInBackground(URL... params) {
				AddPromiseController ctr = new AddPromiseController();
				boolean isCheck = ctr.InsertPromise(promiseDTO);
				count++;
				return 0L;
			}

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected void onProgressUpdate(Integer... progress) {
			}

			@Override
			protected void onPostExecute(Long result) {
				if(count == 2){
					progressLayout.setVisibility(View.GONE);
					dialog.dismiss();
					((Activity)context).finish();
				}
			}
		}
    }
}
