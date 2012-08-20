package com.beautifulpromise.application.addpromise;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @description 맵에 보여질 아이콘 설정 및 터치 이벤트 설정
 * @author immk
 *
 */
public class MyItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	/**
	 * MyItemizedOverlay 생성자
	 * @param defaultMarker 마커 Drawable 이미지
	 * @param context Context
	 */
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	/**
	 * Map에 마커 이미즈를 Overlay 하기 위한 함수
	 * @param overlay OverlayItem
	 */
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	/**
	 * 마커 리스트 중 해당 position에 해당하는 OverlayItem 리턴
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	/**
	 * 마커 리스트의 사이즈
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}

	/**
	 * 마커 터치 리스너
	 */
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
//		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//		dialog.setTitle(item.getTitle());
//		dialog.setMessage(item.getSnippet());
//		dialog.show();
		return true;
	}
}
