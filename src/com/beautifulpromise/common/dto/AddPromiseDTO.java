package com.beautifulpromise.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.beautifulpromise.application.addpromise.DonationDTO;
import com.facebook.halo.application.types.connection.Friends;

import android.graphics.Bitmap;

/**
 * @description 목표 생성을 위한 DTO 객체 (facebook의 post id와 목표 생성의 제목, 카테고리 분류, 알람 주기 등의 데이터를 객체화)
 * @author immk
 */
public class AddPromiseDTO implements Serializable {

	private static final long serialVersionUID = -2166289912424139381L;
	
	private int id;
	private String userId;
	private String postId;
	private int categoryId;
	private String title;
	private String startDate; 
	private String endDate;
	private boolean[] dayPeriod;
	private int time;
	private int min;
	private Double latitue;
	private Double longitude;
	private String content;
	private ArrayList<Friends> helperList;
	private DonationDTO donation;
	private int donationId;
	private String createDate;
	private int result ;
	private String signPath;
	private Bitmap signBitmap;
	private int d_day;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public boolean[] getDayPeriod() {
		return dayPeriod;
	}
	public void setDayPeriod(boolean[] selectDay) {
		this.dayPeriod = selectDay;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<Friends> getHelperList() {
		return helperList;
	}
	public void setHelperList(ArrayList<Friends> helperList) {
		this.helperList = helperList;
	}
	public String getSignPath() {
		return signPath;
	}
	public void setSignPath(String signPath) {
		this.signPath = signPath;
	}
	public Bitmap getSignBitmap() {
		return signBitmap;
	}
	public void setSignBitmap(Bitmap signBitmap) {
		this.signBitmap = signBitmap;
	}
	public DonationDTO getDonation() {
		return donation;
	}
	public void setDonation(DonationDTO donation) {
		this.donation = donation;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public Double getLatitue() {
		return latitue;
	}
	public void setLatitue(Double latitue) {
		this.latitue = latitue;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getD_day() {
		return d_day;
	}
	public void setD_day(String d_day) {
		GregorianCalendar cal = new GregorianCalendar();       
		  long currentTime = cal.getTimeInMillis() / (1000*60*60*24);       
		  //getTimeInMillis()는 현재 시각을 밀리세컨드로 리턴하므로 1000으로 나눔.
		  //날짜로 구하려고 60*60*24 를 해줌
		  cal.set(Integer.parseInt(d_day.substring(0, 4)),Integer.parseInt(d_day.substring(4, 6)) - 1,Integer.parseInt(d_day.substring(6, 8)),0,0);      
		  //기준이 될 특정 날짜를 세팅(주의:일, 월 은 0부터 시작)      
		  long birthTime = cal.getTimeInMillis() / (1000*60*60*24); 
		  //수정한 시각을 밀리세컨드로 리턴받아서 1000으로 나눔      
		  int interval = (int)(birthTime -currentTime);     
		  //현재시각에서 생일시각을 빼서 현재까지 경과된 시간을 구함
		this.d_day = interval;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public int getDonationId() {
		return donationId;
	}
	public void setDonationId(int donationId) {
		this.donationId = donationId;
	}
}
