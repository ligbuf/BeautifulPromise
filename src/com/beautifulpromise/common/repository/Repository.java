package com.beautifulpromise.common.repository;

import com.facebook.halo.application.types.User;

/**
 * @description SingleTon Pattern을 활용하여 로그인한 사용자 정보와 id값을 저장
 * @author immk
 *
 */
public class Repository {

	private static Repository instance = new Repository();
	private User user = new User();

	/**
	 * Repository 생성자
	 */
	public Repository() {
	}
	
	/**
	 * Repository의 객체를 리턴
	 * @return Repository
	 */
	public static Repository getInstance(){
		return instance;
	}

	/**
	 * 페이스북에 로그인한 사용자 정보 리턴
	 * @return User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 페이스북에 로그인한 사용자 정보 세팅
	 */
	public void setUser() {
		this.user = user.createInstance("me");
	}
	
	/**
	 *  페이스북에 로그인한 사용자 아이디값 리턴
	 * @return String
	 */
	public String getUserId() {
		return user.getId();
	}
}