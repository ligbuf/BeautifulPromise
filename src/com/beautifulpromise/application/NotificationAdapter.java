package com.beautifulpromise.application;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautifulpromise.R;
import com.beautifulpromise.common.utils.ImageUtils;

/**
 * Notification List 객체를 ListView에서 표시할 수 있게 해주는 Adapter
 * @author immk
 */
public class NotificationAdapter extends CursorAdapter{

	Context context;
	private LayoutInflater inflater;
	Cursor cursor;
	
	/**
	 * 생성자
	 * @param context
	 * @param cursor
	 */
	public NotificationAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.context = context;
		this.cursor = cursor;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 데이터베이스의 커서를 이용해 데이터와 화면을 연결
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		WebView userImage = (WebView) view.findViewById(R.id.user_image);		
		TextView title = (TextView) view.findViewById(R.id.noti_title_textview);
		userImage.setBackgroundColor(Color.TRANSPARENT);
		userImage.loadDataWithBaseURL(null, ImageUtils.webViewImageReSize("http://graph.facebook.com/" + cursor.getString(cursor.getColumnIndex("send_user_id")) +  "/picture"), "text/html", "utf-8",null);
		userImage.setFocusable(false);
		title.setText(cursor.getString(cursor.getColumnIndex("title")));
	}

	/**
	 * 커서를 통해 데이터를 보여주기위한 화면을 생성
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.notificaion_item, null);
	}
}
