package com.beautifulpromise.application.feedviewer;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautifulpromise.R;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.ImageUtils;
import com.beautifulpromise.common.utils.WebViewManager;
import com.facebook.halo.application.types.User;
import com.facebook.halo.application.types.connection.Friends;
import com.facebook.halo.framework.core.Connection;

/**
 * Feed list를 보여줄때 list view를 설정해주는 Adapter 
 * @author JM
 *
 */
public class FeedListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<FeedItemDTO> arrayListFeedItem;
	int layout;
	String url;
	String mode;
	User user = new User();
	boolean isFriendFeed = false;
	boolean isMine;
	boolean isCheck;
	Connection<Friends> friends;
	Intent intent = new Intent();
	
	
	/**
	 * Constructor
	 * @param context
	 * @param layout
	 * @param arrayListFeedItem
	 * @param mode
	 * @param isCheck
	 */
	public FeedListAdapter(Context context, int layout, ArrayList<FeedItemDTO> arrayListFeedItem, String mode, Boolean isCheck) {
		this.context = context;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
		this.arrayListFeedItem = arrayListFeedItem;
		this.mode = mode;
		this.isCheck = isCheck;
		
		//자신의 피드리스트 보기 일경우
		if(mode.equals("me")) {
			isMine = true;
		} else {
			isMine = false;
			
			//친구리스트 받아오기
			user = Repository.getInstance().getUser();
			friends = user.friends();
		}
			
	}
	
	/**
	 * list view 의 size return 
	 */
	@Override
	public int getCount() {
		return arrayListFeedItem.size();
	}

	/**
	 * list view 의 position 에 해당하는 item return
	 */
	@Override
	public FeedItemDTO getItem(int position) {
		return arrayListFeedItem.get(position);
	}

	/**
	 * list view 의 position 에 해당하는 item id return
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 각 항목의 뷰 생성
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//최초 호출시 커스텀뷰 생성
		if(convertView == null) {
			convertView = inflater.inflate(layout, parent, false);
		}
		
		//자신의 피드리스트 일경우
		if(isMine) {
			isFriendFeed = true;
		} else {
			//친구 리스트 가져와서 해당 feed가 친구의 글인지 검사 
		    if(isFriendFeed(arrayListFeedItem.get(position).getFromId()))
	        	isFriendFeed = true;
	        else 
	        	isFriendFeed = false;
		}
        
		//name setting
		TextView name = (TextView)convertView.findViewById(R.id.nameText);
		name.setText(arrayListFeedItem.get(position).getFromName());
		
		//date setting
		TextView date = (TextView)convertView.findViewById(R.id.dateText);
		date.setText(arrayListFeedItem.get(position).getDate());
		
		//profile Image setting with caching
		WebView profileImage = (WebView)convertView.findViewById(R.id.profileImage);
		profileImage.setWebViewClient(new WebViewManager());
		profileImage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		profileImage.loadUrl(arrayListFeedItem.get(position).getProfileImagePath());
		
		//photo image setting
		WebView photoImage = (WebView)convertView.findViewById(R.id.photoImage);
		photoImage.setWebViewClient(new WebViewManager());
		photoImage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		if(arrayListFeedItem.get(position).getPhotoImagePath() == null)
			photoImage.setVisibility(View.GONE);
		else {
//			Log.e("position : " + position, arrayListFeedItem.get(position).getPhotoImagePath());
			url = ImageUtils.webViewImageReSize(arrayListFeedItem.get(position).getPhotoImagePath());
			photoImage.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
//			Log.e("EE", "H:" + photoImage.getHeight() + "// w : " + photoImage.getWidth());
//			 url = arrayListFeedItem.get(position).getPhotoImagePath();
//			 photoImage.loadUrl(url);
		}
		
		//feed setting
		TextView feed = (TextView)convertView.findViewById(R.id.feedText);
		feed.setText(arrayListFeedItem.get(position).getFeed());
		
		//친구의 피드일때 reply & like setting
		TextView reply = (TextView)convertView.findViewById(R.id.replyText);
		final TextView like = (TextView)convertView.findViewById(R.id.likeText);
		TextView noFriend = (TextView)convertView.findViewById(R.id.noFriendText);
		if(isFriendFeed) { 
			//reply setting
			reply.setText("" +arrayListFeedItem.get(position).getCommentCount());
			
			//like setting
			like.setText("" +arrayListFeedItem.get(position).getLikeCount());
			
			//reply & like click listener
			RelativeLayout viewerBottom = (RelativeLayout)convertView.findViewById(R.id.feedViewerBottom);
			viewerBottom.setOnClickListener(new RelativeLayout.OnClickListener() {
				public void onClick(View v) {
					intent.setAction("feedviewer.FeedWithReply");
					intent.putExtra("feedId", arrayListFeedItem.get(position).getId());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			});
		} else {
			ImageView replyImage = (ImageView)convertView.findViewById(R.id.replyImage);
			ImageView likeImage = (ImageView)convertView.findViewById(R.id.likeImage);
			replyImage.setVisibility(View.INVISIBLE);
			likeImage.setVisibility(View.INVISIBLE);
			reply.setVisibility(View.INVISIBLE);
			like.setVisibility(View.INVISIBLE);
			noFriend.setVisibility(View.VISIBLE);
		}
		
		//promise log click listener
		TextView promiseLog = (TextView) convertView.findViewById(R.id.promiseLogText);
		ImageView promiseLogImage = (ImageView) convertView.findViewById(R.id.promiseLogImage);

		if(isCheck) { //check List 에서 Feed list 볼때
			promiseLog.setVisibility(View.GONE);
			promiseLogImage.setVisibility(View.GONE);
		} else {
			promiseLog.setOnClickListener(new TextView.OnClickListener() {
				public void onClick(View v) {
					intent.setAction("feedviewer.PromiseFeedList");
					intent.putExtra("mode", mode);
					intent.putExtra("isCheck", true);
					intent.putExtra("feedId", arrayListFeedItem.get(position).getId());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			});
		}
		
		return convertView;
	}
	
	
	/**
	 * 해당 피드가 친구의 피드인지 아닌지 검사
	 * 친구의 피드면 true, 아니면 false
	 * @param id
	 * @return 친구의 피드면 true, 아니면 false
	 */
	private boolean isFriendFeed(String id) {
		//자신의 피드면 true return
		if(id.equals(user.getId()))
			return true;
		
		for(int i = 0; i < friends.getData().size(); i++) {
			if(friends.getData().get(i).getId().equals(id)) 
				return true;
		}
		return false;
	}
	
}
