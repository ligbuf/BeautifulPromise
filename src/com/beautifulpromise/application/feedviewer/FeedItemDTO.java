package com.beautifulpromise.application.feedviewer;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.facebook.halo.application.types.Post;
import com.facebook.halo.application.types.Post.Comments;
import com.facebook.halo.application.types.Post.Likes;
import com.facebook.halo.application.types.User;

/**
 * post 객체를 feed 객체로 변환하는 Data transfer object
 * @author JM
 *
 */
public class FeedItemDTO implements Serializable {
	
	private static final long serialVersionUID = 6025863160122617608L;
	
	private String id;
	private String profileImagePath;
	private String fromName;
	private String fromId;
	private String date;
	private String photoImagePath;
	private String feed;
	private Comments comment;
	private Likes like;
	private Long commentCount;
	private Long likeCount;
	
	/**
	 * constructure
	 */
	public FeedItemDTO() {}
	
	/**
	 * post 객체를 feed 객체로 변환하는 생성자
 	 * @param post
	 */
	public FeedItemDTO(Post post) {
		id = post.getId();
		
		fromName = post.getFrom().getName();
		fromId= post.getFrom().getId();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd aa hh:mm");
		date = df.format(post.getCreatedTime());
		
		User user = new User();
		user = user.createInstance(post.getFrom().getId());
		
		profileImagePath = user.picture();
		
		photoImagePath = post.getPicture();
		
		if(post.getMessage() == null)
			feed = post.getName();
		else
			feed = post.getMessage();
		
		comment = post.getComments();
		
		like = post.getLikes();
		
		if(like == null)
			likeCount = 0L;
		else 
			likeCount = (long) like.getData().size();
		
		if(comment == null)
			commentCount = 0L;
		else {
			commentCount = (long) comment.getData().size();
		}
	}
	
	/**
	 * Getter & Setter
	 */
	public final String getProfileImagePath() {
		return profileImagePath;
	}
	public final void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}
	public final String getDate() {
		return date;
	}
	public final void setDate(String date) {
		this.date = date;
	}
	public final String getPhotoImagePath() {
		int length = photoImagePath.length();
		String tmp = photoImagePath.substring(0, length-5) + "n" + photoImagePath.substring(length-4, length);
		return tmp;
	}
	public final void setPhotoImagePath(String photoImagePath) {
		this.photoImagePath = photoImagePath;
	}
	public final String getFeed() {
		return feed;
	}
	public final void setFeed(String feed) {
		this.feed = feed;
	}

	public final Comments getComment() {
		return comment;
	}

	public final void setComment(Comments comment) {
		this.comment = comment;
	}

	public final Long getCommentCount() {
		return commentCount;
	}

	public final void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public final Long getLikeCount() {
		return likeCount;
	}

	public final void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final Likes getLike() {
		return like;
	}

	public final void setLike(Likes like) {
		this.like = like;
	}

	public final String getFromName() {
		return fromName;
	}

	public final void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public final String getFromId() {
		return fromId;
	}

	public final void setFromId(String fromId) {
		this.fromId = fromId;
	}


}