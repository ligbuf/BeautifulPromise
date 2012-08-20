package com.beautifulpromise.application.addpromise;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.beautifulpromise.R;
import com.beautifulpromise.common.utils.ImageUtils;

/**
 * @description Assets에 있는 캠페인관련 정보를 파싱해서 객체로 변환해주는 클래스
 * @author immk
 *
 */
public class LoadDonation {
	
	Context context;
	
	/**
	 * LoadDonation 생성자
	 * @param context
	 */
	LoadDonation (Context context) {
		this.context = context;
	}

	/**
	 * xml 데이터를 파싱하여 객체로 변환해주는 함수
	 * @return
	 */
	protected ArrayList<DonationDTO> loadDonation() {
		ArrayList<DonationDTO> list = new ArrayList<DonationDTO>();
		XmlPullParser parser = context.getResources().getXml(R.xml.donation);
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				if ("donation".equals(parser.getName())) {
					list.add(add(parser));
					parser.next();
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	/**
	 * xml 데이터를 파싱하여 DonationDTO 객체로 매핑한 후 리턴 
	 * @param parser XmlPullParser
	 * @return DonationDTO
	 */
	private DonationDTO add(XmlPullParser parser){
		
		DonationDTO donation = new DonationDTO();
		donation.setId(Integer.parseInt(parser.getAttributeValue(0)));
		donation.setTitle(parser.getAttributeValue(1));
		donation.setDetails(parser.getAttributeValue(2));
		donation.setDrawable(loadResource(parser.getAttributeValue(3)));
		donation.setAfterDrawable(loadResource(parser.getAttributeValue(4)));
		
		return donation;
	}
	
	/**
	 * 이미지 경로를 Drawable 이미지로 변환
	 * @param path 이미지 경로
	 * @return Drawable
	 */
	public Drawable loadResource(String path) {
		Drawable drawable = null;

		try {
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(path);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return ImageUtils.bitmapToDrawable(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drawable;
	}
}
