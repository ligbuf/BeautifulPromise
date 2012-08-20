package com.beautifulpromise.application.checkpromise;

import java.io.File;

import com.beautifulpromise.R;
import com.beautifulpromise.common.utils.StorageUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

/**
 * @author ou
 * 카메라 다이얼로그 엑티비티
 */
public class CameraDialog extends Dialog{
	
	public static final int FINISH_TAKE_PHOTO = 3;
	public static final int FINISH_GET_IMAGE = 4;
	public static final String IMAGE_PATH = "image_path";
	/**
	 * context, theme 받아서 객체 생성
	 * @param context 다이얼 로그를 띄울 엑티비티 context
	 * @param theme 테마 종류
	 */
    public CameraDialog(Context context, int theme) {
        super(context, theme);
    }
	/**
	 * context만 받아서 객체 생성
	 * @param context 다이얼 로그를 띄울 엑티비티 context
	 */
	public CameraDialog(Context context) {
		super(context);
	}
	/**
	 * Dialog Builder 클래스
	 * @author ou
	 *
	 */
	public static class Builder {
    	 
        private Context context;
    	Button takeImageButton;
    	Button getImageButton;
    	Button cancelButton;
    	private CameraDialog dialog;
    	View layout;

    	/**
    	 * 해당 엑티비티의 context받아서 Builder객체 생성
    	 * @param context
    	 */
        public Builder(Context context) {
            this.context = context;
        }
        
        /**
         * Dialog Create 메소드
         * @return
         */
        public CameraDialog create() {
        	        	
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	dialog = new CameraDialog(context, R.style.Theme_Dialog);
            layout = inflater.inflate(R.layout.checkpromise_camera_dialog, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            takeImageButton = (Button) layout.findViewById(R.id.take_photo_button);
            getImageButton = (Button) layout.findViewById(R.id.get_photo_button);
            cancelButton = (Button) layout.findViewById(R.id.camera_cancel_button);

            takeImageButton.setOnClickListener(buttonClick);
            getImageButton.setOnClickListener(buttonClick);
            cancelButton.setOnClickListener(buttonClick);
            dialog.setContentView(layout);			
            return dialog;
        }

        View.OnClickListener buttonClick = new View.OnClickListener() {

        	Intent intent;
        	
        	/**
        	 * 카메라 찍기, 앨범에서 선택, 취소 버튼 이벤트
        	 */
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.take_photo_button:
					String imagePath = StorageUtils.getFilePath(context)+".png";
					Uri pictureUri = Uri.fromFile(new File(imagePath));
					intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					((Activity)context).startActivityForResult(intent, FINISH_TAKE_PHOTO);
					dialog.dismiss();
					break;
				case R.id.get_photo_button:
					intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					intent.putExtra("return-data", true);
					((Activity) context).startActivityForResult(intent, FINISH_GET_IMAGE);
					dialog.dismiss();
					break;
				case R.id.camera_cancel_button:
					dialog.dismiss();
					break;
				default:
					break;
				}
			}
		};
        
    }
}
