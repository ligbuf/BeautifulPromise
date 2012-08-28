package com.beautifulpromise.application.addpromise;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.beautifulpromise.R;
import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.utils.ImageUtils;

/**
 * @author immk
 * @description 기부 캠페인 선택 Dialog
 */
public class DonationDialog extends Dialog{
	
	/**
	 * Custom Dialog 생성자
	 * @param context Context
	 * @param theme 테마 설정
	 */
    public DonationDialog(Context context, int theme) {
        super(context, theme);
    }
	
    /**
     * Custom Dialog 생성자
     * @param context Context
     */
	public DonationDialog(Context context) {
		super(context);
	}
	
	/**
	 * Custom Dialog View 설정
	 * @author immk
	 */
	public static class Builder {
    	 
        private Context context;
    	private DonationDialog dialog;
    	View layout;
    	ListView donationListView;
    	Button okayBtn;
    	AddPromiseDTO promiseDTO;

    	/**
    	 * Custom Dialog Builder 생성자
    	 * @param context
    	 */
        public Builder(Context context, AddPromiseDTO promiseDTO) {
            this.context = context;
            this.promiseDTO = promiseDTO;
        }
        
        /**
         * 캠페인 Dialog 생성
         * @return DonationDialog
         */
        public DonationDialog create() {
        	        	
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	dialog = new DonationDialog(context, R.style.Theme_Dialog);
            layout = inflater.inflate(R.layout.addpromise_donation_dialog, null);
            
            donationListView = (ListView) layout.findViewById(R.id.donation_list_view);
            
            LoadDonation donation = new LoadDonation(context);
            ArrayList<DonationDTO> donationList = donation.loadDonation();
            
            DonationAdapter adapter = new DonationAdapter(context, donationList);
            donationListView.setAdapter(adapter);
            donationListView.setOnItemClickListener(mItemClickListener);
            
            okayBtn = (Button) layout.findViewById(R.id.okay_button);
            
            okayBtn.setOnClickListener(buttonClick);
            dialog.setContentView(layout);			
            return dialog;
        }
        
        AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				if( promiseDTO.getDonation()!=null){
					int prevPosition = promiseDTO.getDonation().getId()-1;
					adapterView.getChildAt(prevPosition).setBackgroundResource(R.drawable.popup_bg_white_plate);
				}
				DonationDTO donation = (DonationDTO) adapterView.getAdapter().getItem(position);
				adapterView.getChildAt(position).setBackgroundResource(R.drawable.popup_bg_green_plate);
				promiseDTO.setDonation(donation);
			}
		};

		View.OnClickListener buttonClick = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.okay_button:
					
					if(promiseDTO.getDonation() != null){
						UploadFeedDialog.Builder uploadBuilder = new UploadFeedDialog.Builder(context, promiseDTO);
						Dialog uploadDialog = uploadBuilder.create();
						uploadDialog.show();
						dialog.dismiss();
					}else{
						Toast.makeText(context, "기부 프로젝트를 선택해주세요.", Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		};
    }
}
