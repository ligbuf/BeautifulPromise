package com.beautifulpromise.application.addpromise;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.beautifulpromise.common.utils.ImageUtils;
import com.beautifulpromise.common.utils.MessageUtils;
import com.facebook.halo.application.types.connection.Friends;

/**
 * 친구 이미지의 List 객체를 ListView에서 표시할 수 있게 해주는 Adapter
 * @author immk
 *
 */
public class FriendImageAdapter extends BaseAdapter{

	Context context;
	ArrayList<Object> friendsList;
	
	/**
	 * Custom Adapter 생성자
	 * @param context Context
	 * @param friendsList 친구 목록 리스트
	 */
	public FriendImageAdapter(Context context, ArrayList<Object> friendsList) {
		this.context = context;
		this.friendsList = friendsList;
	}
	
	/**
	 * 리스트의 갯수
	 */
	@Override
	public int getCount() {
		if(friendsList != null)
			return friendsList.size();
		return 0;
	}

	/**
	 * 리스트 중 해당 position의 객체 정보 가져오기 
	 */
	@Override
	public Object getItem(int position) {

		if(friendsList != null && getCount() != 0)
			return friendsList.get(position);
		return null;
	}

	/**
	 * 리스트 중 해당 position의 id값 가져오기
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 리스트 중 해당 postion에 해당하는 데이터를 화면에 보여주기
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String friendId = "";
		if( getItem(position).getClass() == Friends.class){
			Friends friend = (Friends) getItem(position);
			friendId = friend.getId();
		} else{
			friendId = (String) getItem(position);
		}
		
		WebView imageView = new WebView(context);
		imageView.setFocusable(false);
		imageView.setFocusableInTouchMode(false);
//		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		imageView.loadDataWithBaseURL(null, ImageUtils.webViewImageReSize(MessageUtils.FACEBOOK_GRAPH_URL + friendId + "/picture" ), "text/html", "utf-8", null);
		return imageView;
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
}