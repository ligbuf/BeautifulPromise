package com.beautifulpromise.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.beautifulpromise.common.repository.Repository;
import com.beautifulpromise.common.utils.MessageUtils;

public class Controller {
	
	HttpClients client;
	
	public Controller(){
		client = new HttpClients();
	}
	
	/**
	 * 신규 체크 생성
	 * @param postId facebook Post Id
	 * @param checkId facebook Post Id
	 * @return
	 */
	public boolean PublishCheck(String postId, String checkId) {
		boolean isSuccess = false;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("td_article_id", postId));
		params.add(new BasicNameValuePair("tdc_article_id", checkId));
		String data = client.getUrlToJson(MessageUtils.SET_NEW_CHECK, params);
		if(data != null)
			isSuccess = client.getResult(data);
		return isSuccess;
	}
	
	/**
	 * 포인트추가
	 * @param point 포인트 값
	 * @return boolean
	 */
	public boolean AddPoint (int point) {
		boolean isSuccess = false;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("fb_key", Repository.getInstance().getUserId()));
		params.add(new BasicNameValuePair("point", ""+point));
		String data = client.getUrlToJson(MessageUtils.SET_NEW_POINT, params);
		if(data != null)
			isSuccess = client.getResult(data);
		return isSuccess;
	}
	
	/**
	 * 캠페인에 포인트 추가 (기부)
	 * @param projectId 참여하는 캠페인 id
	 * @param point 기부하는 금액
	 * @return boolean
	 */
	public boolean DonationPointToProject (int projectId, int point) {
		boolean isSuccess = false;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pl_key", ""+projectId));
		params.add(new BasicNameValuePair("point", ""+point));
		String data = client.getUrlToJson(MessageUtils.SET_ADD_PROJECT_POINT, params);
		if(data != null)
			isSuccess = client.getResult(data);
		return isSuccess;
	}
	
	/**
	 * Todo 체크 리스트
	 * @param postId facebook Post Id
	 * @return ArrayList<String>
	 */

	@SuppressWarnings({"unchecked", "rawtypes"})
	public ArrayList<String> GetCheckList (String postId) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("td_article_id", postId));
		String data = client.getUrlToJson(MessageUtils.GET_TODO_CHECK_LIST, params);
		if(data != null){
			ArrayList<HashMap> rows = client.getResultList(data);
			for(HashMap row : rows){
				list.add((String) row.get("tdc_article_id"));
			}
		}
		return list;
	}
	
	/**
	 * Todo 의 헬퍼 리스트
	 * @param postId facebook Post Id
	 * @return boolean
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public ArrayList<String> GetHelperList (String postId) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("td_article_id", ""+postId));
		String data = client.getUrlToJson(MessageUtils.GET_TODO_HELPER_LIST, params);
		if(data != null){
			ArrayList<HashMap> rows = client.getResultList(data);
			for(HashMap row : rows){
				list.add((String) row.get("fb_member_fb_key"));
			}
		}
		return list;
	}
	
	/**
	 * 해당 캠페인의 진행사항 정보 가져오기
	 * @param projectId 참가하는 캠페인 id
	 * @return boolean
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public ArrayList<String> GetProjectStatus (int projectId) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pl_key", ""+projectId));
		String data = client.getUrlToJson(MessageUtils.GET_PROJECT_STATUS, params);
		if(data != null){
			ArrayList<HashMap> rows = client.getResultList(data);
			for(HashMap row : rows){
				list.add((String) row.get("pl_goal"));
				list.add((String) row.get("pl_now_status"));
			}
		}
		return list;
	}
	

	/**
	 * Todo List 가져오기
	 * @param mode(=me, helper, all)
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public ArrayList<String> GetTodoList (String mode) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mode", mode));
		if(!mode.equals("all"))
			params.add(new BasicNameValuePair("fb_key", Repository.getInstance().getUserId()));
		String data = client.getUrlToJson(MessageUtils.GET_TODO_LIST, params);
		if(data != null){
			ArrayList<HashMap> rows = client.getResultList(data);
			for(HashMap row : rows){
				list.add((String) row.get("td_article_id"));
			}
		}
		return list;
	}
}
