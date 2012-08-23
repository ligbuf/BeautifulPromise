package com.beautifulpromise.application.addpromise;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;	//이게 뭔지 몰라도 수정했음 .contorl 붙어있음

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beatutifulpromise.common.log.Microlog4Android;
import com.beautifulpromise.R;
import com.beautifulpromise.application.addpromise.UploadFeedDialog.Builder.DatabaseAsynTask;
import com.beautifulpromise.application.addpromise.UploadFeedDialog.Builder.ServerAsynTask;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.DateUtils;
import com.beautifulpromise.common.utils.ImageUtils;
import com.beautifulpromise.database.DatabaseHelper;
import com.beautifulpromise.database.GoalsDAO;
import com.beautifulpromise.parser.Controller;
import com.facebook.halo.application.types.Album;
import com.facebook.halo.application.types.Tags;
import com.facebook.halo.application.types.User;
import com.facebook.halo.application.types.connection.Albums;
import com.facebook.halo.application.types.connection.Friends;
import com.facebook.halo.application.types.connection.Photos;
import com.facebook.halo.application.types.infra.CategorizedFacebookType;
import com.facebook.halo.application.types.infra.FacebookType;
import com.facebook.halo.framework.core.Connection;

/**
 * @description 기부증 Upload를 위한 Dialog (내부 Database 갱신, Facebook에 기부증 Upload )
 * @author immk
 */
public class UploadDonationLetterDialog extends Dialog{
	
	/**
	 * Custom Dialog 생성자
	 * @param context Context
	 * @param theme 테마 설정
	 */
    public UploadDonationLetterDialog(Context context, int theme) {
        super(context, theme);
    }
	
    /**
     * Custom Dialog 생성자
     * @param context Context
     */
	public UploadDonationLetterDialog(Context context) {
		super(context);
	}
	
	/**
	 * Custom Dialog View 설정
	 * @author immk
	 */
	public static class Builder {
    	 
        private Context context;
    	private UploadDonationLetterDialog dialog;
    	
    	LinearLayout progressLayout;
    	LinearLayout promiseLayout;
    	ImageView donationImage;
    	ImageView heartImage;
    	TextView resultText;
    	TextView donationTitle;
    	TextView dateText;
    	GridView helperGrid;
    	
    	Button okayBtn;
    	
    	View layout;
    	String path;
    	
    	int count = 0;
    	boolean bResult;
    	ArrayList<DonationDTO> donationList;
		ArrayList<String> helperList;
		AddPromiseDTO addPromiseDTO;
        
    	/**
    	 * Custom Dialog Builder 생성자
    	 * @param context
    	 */
        public Builder(Context context, boolean result, AddPromiseDTO addPromiseDTO) {
        	this.context = context;
        	this.bResult = result;
        	this.addPromiseDTO = addPromiseDTO;
		}

        /**
         * UploadDonationLetterDialog 생성
         * @return Dialog
         */
		public UploadDonationLetterDialog create() {
        	        	
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	dialog = new UploadDonationLetterDialog(context, R.style.Theme_Dialog);
            layout = inflater.inflate(R.layout.addpromise_upload_donation_dialog, null);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            progressLayout = (LinearLayout) layout.findViewById(R.id.progressLayout);
            promiseLayout = (LinearLayout) layout.findViewById(R.id.promise_layout);
            heartImage = (ImageView) layout.findViewById(R.id.heart_image);
            
            donationImage = (ImageView) layout.findViewById(R.id.donation_image_view);  // 사진
            resultText = (TextView) layout.findViewById(R.id.content_text);  			// 목표를 실패 성공 하셨습니다.
            donationTitle = (TextView) layout.findViewById(R.id.donation_title_text);	// ~프로젝트에 도움을 주신분들 ....
            dateText = (TextView) layout.findViewById(R.id.date_text);					// 날짜
            helperGrid = (GridView) layout.findViewById(R.id.friend_image_gridview);
            
            okayBtn = (Button) layout.findViewById(R.id.okay_button);
                        
            ViewAsynTask task = new ViewAsynTask();
            task.execute();
            
            LoadDonation donation = new LoadDonation(context);
            donationList = donation.loadDonation();

            dateText.setText(DateUtils.getDate());
 
            okayBtn.setOnClickListener(buttonClickListener);
            progressLayout.setVisibility(View.GONE);
            dialog.setContentView(layout);			
            return dialog;
        }
		
		View.OnClickListener buttonClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.okay_button:
					
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
					
					FacebookAsynTask  task = new FacebookAsynTask();
					task.execute();
					break;
				default:
					break;
				}
			}
		};

		/**
		 * 프로젝트 타이틀과 결과에 따른 메시지 설정
		 * @param projectTitle
		 * @param result
		 * @return String
		 */
		private String setContent(String projectTitle, boolean result){
			
			String str;	
			str = "'" +projectTitle + "' 목표를 ";
			
			if(result)
				str += "성공하였습니다.";
			else
				str += "실패하였습니다.";
			return str; 
		}
		
		/**
		 * 카테고리, 결과, 포인트에 따른 기부 메시지 설정
		 * @param category
		 * @param result
		 * @param point
		 * @return
		 */
		private String setDontationText(int category, boolean result, int point){
			String str = "";
			
			if(category == 6){
				heartImage.setVisibility(View.VISIBLE);
				if(result)
					str = "" + point + " 개를 획득하였습니다.";
				else
					str = "" + point + " 개가 도움을 주신 분들께 전달되었습니다.";
				
			} else {
				heartImage.setVisibility(View.GONE);
				if(result)
					str += "도움을 주신분들과 회원님의 이름으로 ";
				else 
					str += "도움을 주신분들의 이름으로 ";
				
				switch (category) {
				case 1:
					str += donationList.get(0).getTitle();
					break;

				case 2:
					str += donationList.get(1).getTitle();
					break;
					
				case 3:
					str += donationList.get(2).getTitle();
					break;

				case 4:
					str += donationList.get(3).getTitle();
					break;
					
				case 5:
					str += donationList.get(4).getTitle();
					break;

				default:
					break;
				}
				
				str += "에 기부금이 전달되었습니다.";
			} 
				
			return str; 
		}
		
		/**
		 * 피드에 업로드 될 '아름다운 기부증' 생성을 위한 AsyncTask
		 * @author immk
		 */
		public class ViewAsynTask extends AsyncTask<URL, Integer, Long> {
			
			@Override
			protected Long doInBackground(URL... params) {
				
//				DatabaseHelper databaseHelper = new DatabaseHelper(context);
//				GoalsDAO dao = new GoalsDAO(databaseHelper);
//				addPromiseDTO = dao.get(feedId);				
				Controller ctr = new Controller();
				helperList = ctr.GetHelperList(addPromiseDTO.getPostId());
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
				
				if(addPromiseDTO != null) {
					resultText.setText(setContent(addPromiseDTO.getTitle(), bResult));           
					donationImage.setImageDrawable(donationList.get(addPromiseDTO.getCategoryId()).getAfterDrawable());
		            donationTitle.setText(setDontationText(addPromiseDTO.getDonationId(), bResult, 200));
				}
				
				ArrayList<Object> helper = new ArrayList<Object>();
				
				if(bResult)
					helper.add(Repository.getInstance().getUserId());
				
				for(String friend : helperList){
					helper.add(friend);
				}
				FriendImageAdapter adapter = new FriendImageAdapter(context, helper);
				helperGrid.setAdapter(adapter);
			}
		}
		
		/**
		 * 페이스북에 피드를 업로드 하기 위한 AsyncTask
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
				photos.setMessage("\n\n" + "[아름다운 약속] 앱 다운 받기" + "\n" + "http://www.enjoybazar.com");
				photos.setSource(path);
				photos.setFileName("Beautiful Promise");
				FacebookType type = user.publishPhotos(albumId, photos);
				facebookId = type.getId();
				
				ArrayList<Tags> tags = new ArrayList<Tags>();
				int count=0;
				if(helperList != null && helperList.size() > 0){
					for(String friend : helperList){
						count++;
						Tags tag = new Tags();
						tag.setTagUid(friend);
						tag.setX(""+(20*count));
						tag.setY(""+(80));
						tags.add(tag);
					}
					result = user.publishTagsAtPhoto(facebookId, tags);
				}else 
					result = true;
				
				Controller ctr = new Controller();
				boolean aa ; 
				if(addPromiseDTO.getDonationId() == 6)
					aa = ctr.AddPoint(150);
				else
					aa = ctr.DonationPointToProject(addPromiseDTO.getDonationId(), 150);
				
				Microlog4Android.logger.info("immk - aa : "+aa);
				
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
					DatabaseHelper databaseHelper = new DatabaseHelper(context);
					GoalsDAO dao = new GoalsDAO(databaseHelper);
					if(bResult){
						dao.update(addPromiseDTO.getId(), 1);
					} else
						dao.update(addPromiseDTO.getId(), 2);
					progressLayout.setVisibility(View.GONE);
					dialog.dismiss();
					((Activity) context).finish();
					
				}
			}
		}
    }
}
