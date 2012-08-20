package com.beautifulpromise.application.feedviewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beautifulpromise.R;
import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.WebViewManager;
import com.facebook.halo.application.types.Comment;
import com.facebook.halo.application.types.Post.Comments;
import com.facebook.halo.application.types.User;

/**
 * 피드+댓글 뷰어에 쓰이는 댓글 list view adapter
 * @author JM
 *
 */
public class ReplyListAdapter extends BaseAdapter{
	
	ArrayList<Comment> arrayComment;
	Context context;
	LayoutInflater inflater;
	int layout;
	User user;

	/**
	 * constructor
	 * @param context
	 * @param layout
	 * @param arrayComment
	 */
	public ReplyListAdapter(Context context, int layout, ArrayList<Comment> arrayComment) {
		this.context = context;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
		this.arrayComment = arrayComment;
		user = Repository.getInstance().getUser();
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
	
		/************************************
		 * reply layout 
		 ************************************/
		
		//name setting
		final TextView name = (TextView)convertView.findViewById(R.id.replyNameText);
		name.setText(arrayComment.get(position).getFrom().getName());

		//feed setting
		TextView feed = (TextView)convertView.findViewById(R.id.replyFeedText);
		feed.setText(arrayComment.get(position).getMessage());
		
		//reply profile image setting
		WebView profileImage = (WebView)convertView.findViewById(R.id.replyProfileImage);
		profileImage.setWebViewClient(new WebViewManager());
		profileImage.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		profileImage.loadUrl(arrayComment.get(position).getFrom().getPicture());
		
		//date setting
		final TextView date = (TextView)convertView.findViewById(R.id.replyDateText);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd aa hh:mm");
		date.setText("" + df.format(arrayComment.get(position).getCreatedTime()));
		
		//좋아요
		final TextView like = (TextView)convertView.findViewById(R.id.replyLikeText);
		if(arrayComment.get(position).getUserLikes()) { //사용자가 좋아한 댓글인지 아닌지 체크
			like.setText("좋아요 취소");
		}
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(like.getText().equals("좋아요")) { //좋아요하기
					like.setText("좋아요 취소");
					user.publishLikes(arrayComment.get(position).getId());
				}
				else { //좋아요 취소하기
					like.setText("좋아요");
					user.publishUndoLikes(arrayComment.get(position).getId());
				}
				
			}
		});
		
		return convertView;
	}
	

	/**
	 * list view 의 size return
	 */
	@Override
	public int getCount() {
		return arrayComment.size();

	}

	/**
	 * list view 의 position에 해당하는 item return
	 */
	@Override
	public Comment getItem(int position) {
		return arrayComment.get(position);
	}

	/**
	 * list view 의 position에 해당하는 item id return
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	

}
