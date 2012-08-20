package com.beautifulpromise.application.addpromise;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautifulpromise.R;

/**
 * 캠페인 List 객체를 ListView에서 표시할 수 있게 해주는 Adapter
 * @author immk
 */
public class DonationAdapter extends BaseAdapter {
	
	Context context;
	private ArrayList<DonationDTO> donationList;
	private LayoutInflater inflater;
	ViewHolder holder;
	
	/**
	 * Custom Adapter 생성자
	 * @param context Context
	 * @param donationList 캠페인에 관련된 리스트
	 */
	public DonationAdapter(Context context, ArrayList<DonationDTO> donationList) {
		this.context = context;
		this.setDonationList(donationList);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 리스트의 갯수
	 */
	@Override
	public int getCount() {
		return getDonationList().size();
	}

	/**
	 * 리스트 중 해당 position의 객체 정보 가져오기 
	 */
	@Override
	public Object getItem(int position) {
		return getDonationList().get(position);
	}
	
	/**
	 * 리스트 중 해당 position의 id값 가져오기
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * ViewHolder
	 * @author immk
	 */
	public class ViewHolder {
		public TextView donationTitle;
		public TextView donationDetails;
		public ImageView donationImage;
		public LinearLayout donationLayout;
	}

	/**
	 * 리스트 중 해당 postion에 해당하는 데이터를 화면에 보여주기
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DonationDTO donation = getDonationList().get(position);

		if(convertView == null) {
			convertView = inflater.inflate(R.layout.addpromise_donation_item, null);

			holder = new ViewHolder();
			holder.donationLayout = (LinearLayout) convertView.findViewById(R.id.donation_layout);
			holder.donationImage = (ImageView) convertView.findViewById(R.id.donation_image_view);
			holder.donationTitle = (TextView)convertView.findViewById(R.id.donation_title_text);
			holder.donationDetails = (TextView)convertView.findViewById(R.id.donation_details_text);
			convertView.setTag(holder);
		
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(donation != null) {			
			if(donation.getDrawable() != null)
				holder.donationImage.setImageDrawable(donation.getDrawable());
			holder.donationTitle.setText(donation.getTitle());
			holder.donationDetails.setText(donation.getDetails());
		}
		return convertView;
	}
	
	/**
	 * adapter의 데이터가 변했을 때 호출하는 함수
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public ArrayList<DonationDTO> getDonationList() {
		return donationList;
	}

	public void setDonationList(ArrayList<DonationDTO> donationList) {
		this.donationList = donationList;
	}

}
