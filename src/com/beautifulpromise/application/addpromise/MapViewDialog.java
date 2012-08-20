package com.beautifulpromise.application.addpromise;

import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.beautifulpromise.R;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/**
 * @description 구글 맵을 연동한 Dialog
 * @author immk
 */
public class MapViewDialog extends Dialog {

	private Context context;
//	private MapViewDialog dialog;
	LinearLayout mapLayout;
//	MapView mapView;
	Button okayBtn;
	Button cancelBtn;
	MapController mapController;

	/**
	 * MapViewDialog 생성
	 * @param context Context
	 * @param theme 테마 생성
	 * @param mapView MapView
	 */
	public MapViewDialog(Context context, int theme, MapView mapView) {
		super(context, theme);
		
		setContentView(R.layout.addpromise_google_map_dialog);
		
		mapLayout = (LinearLayout) findViewById(R.id.map_layout);
		okayBtn = (Button) findViewById(R.id.okay_button);
		cancelBtn = (Button) findViewById(R.id.cancel_button);

		okayBtn.setOnClickListener(buttonClickListener);
		cancelBtn.setOnClickListener(buttonClickListener);
		
		mapLayout.addView(mapView);
	}

	/**
	 * MapViewDialog 생성
	 * @param context Context
	 * @param mapView MapView
	 */
	public MapViewDialog(Context context, MapView mapView) {
		super(context);
		
		setContentView(R.layout.addpromise_google_map_dialog);
	
		mapLayout = (LinearLayout) findViewById(R.id.map_layout);
		okayBtn = (Button) findViewById(R.id.okay_button);
		cancelBtn = (Button) findViewById(R.id.cancel_button);

		okayBtn.setOnClickListener(buttonClickListener);
		cancelBtn.setOnClickListener(buttonClickListener);
		
		mapLayout.addView(mapView);

//		mapView.displayZoomControls(true);
//
//		mapController = mapView.getController();
//		LocationManager locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
//		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
//		String best = locationMgr.getBestProvider(criteria, true);
//		locationMgr.requestLocationUpdates(best, 1000, 0, this);
	

	}
	View.OnClickListener buttonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.okay_button:
				mapLayout.removeAllViews();
				dismiss();
				
				break;

			case R.id.cancel_button:
				mapLayout.removeAllViews();
				dismiss();
				break;
			default:
				break;
			}
		}
	};
}

// 참고 http://micropilot.tistory.com/1867
