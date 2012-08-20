package com.beautifulpromise.application.addpromise;

import android.graphics.drawable.Drawable;

/**
 * @author immk
 * @description 기부 캠페인 정보 객체
 */
public class DonationDTO {
	
	/**
	 * @param id 캠페인 아이디
	 * @param title 캠페인 타이틀
	 * @param details 캠페인 세부내용
	 * @param drawable 캠페인 관련 이미지1
	 * @param afterDrawable 캠페인 관련 이미지2
	 */
	private int id;  
	private String title;
	private String details;
	private Drawable drawable;
	private Drawable afterDrawable;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Drawable getAfterDrawable() {
		return afterDrawable;
	}
	public void setAfterDrawable(Drawable afterDrawable) {
		this.afterDrawable = afterDrawable;
	}
}
