package com.beautifulpromise.application.checkpromise;

import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * 구글맵 PinOverlay클래스
 * @author ou
 *
 */
public class CycleGpsPinOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	/**
	 * PinOverlay 메소드
	 * @param defaultMarker
	 */
	public CycleGpsPinOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	/**
	 * 그리는 아이템 만드는 메소드
	 */
	@Override
	protected OverlayItem createItem(int arg0) {
		return mOverlays.get(arg0);
	}

	/**
	 * 그리는 아이템의 사이즈 리턴 메소드
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}

	/**
	 * 그리는 아이템 가져오는 메소드
	 * @param overlay
	 */
	public void addOverlayItem(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}
}