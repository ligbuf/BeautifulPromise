package com.beautifulpromise.application;

import android.graphics.drawable.Drawable;

/**
 * 포인트샵에 사용되는 아이템들의 DTO객체
 * @author ou
 *
 */
public class PointItemDTO {

	private int id;
	private String title;
	private String price;
	private Drawable drawable;
	
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
	public String getprice() {
		return price;
	}
	public void setprice(String price) {
		this.price = price;
	}
}
