package com.beautifulpromise.database;

import java.util.ArrayList;
import java.util.HashMap;

import com.beautifulpromise.common.dto.AddPromiseDTO;
import com.beautifulpromise.common.utils.DateUtils;
import com.beautifulpromise.common.utils.TypeUtils;

/**
 * 목표에 관련된 데이터를 저장, 검색, 업데이트 시킬 수 있는 클래스
 * @author immk
 *
 */
public class GoalsDAO implements IGoalsDAO {

	private DatabaseHelper databaseHelper;

	public GoalsDAO(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * 목표 생성에 관한 정보 저장
	 */
	@Override
	public boolean insert(AddPromiseDTO addPromiseDTO) {
		
		boolean isSuccess = false;
		addPromiseDTO.setCreateDate(DateUtils.getCreateDate());
		String query = "INSERT INTO Goals(post_id, category, title, start_date, end_date, content, result, donation, create_date) " 
				+ "VALUES('" + addPromiseDTO.getPostId() + "',"+ addPromiseDTO.getCategoryId() + ",'" + addPromiseDTO.getTitle() + "','" 
				+ DateUtils.convertStringDate(addPromiseDTO.getStartDate()) + "','" + DateUtils.convertStringDate(addPromiseDTO.getEndDate()) + "','" + addPromiseDTO.getContent()
				+ "',0,"+ addPromiseDTO.getDonation().getId() + ",'" + addPromiseDTO.getCreateDate() + "')";
		if (SQLClient.executeUpdate(databaseHelper, query)) {
			int goalId = getGoalId(addPromiseDTO.getCreateDate());
			if (goalId >= 0) {
				
				switch (addPromiseDTO.getCategoryId()) {
				case 0: // Helpers, Alarms, Locations
					insertAlarms(goalId, addPromiseDTO.getTime(), addPromiseDTO.getMin(), addPromiseDTO.getDayPeriod());
					insertLocations(goalId, addPromiseDTO.getLatitue(), addPromiseDTO.getLongitude());
					break;
				case 1:	// Helpers, Alarms
					insertAlarms(goalId, addPromiseDTO.getTime(), addPromiseDTO.getMin(), addPromiseDTO.getDayPeriod());
					break;
				case 2:	// Helpers
					break;
				}
				isSuccess = true;
			}
		}
		return isSuccess;
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public boolean insertHelpers (int goalId, ArrayList<String> friendIds) {
//		
//		for(String friendId : friendIds){
//			String query = "INSERT INTO Goals(goal_id, friend_id) " + "VALUES(" + goalId+ ",'" + friendId + "')";
//			SQLClient.executeUpdate(databaseHelper, query);
//		}
//		return false;
//	}
	
	/**
	 * 알람 정보 저장
	 */
	public boolean insertAlarms (int goalId, int time, int min, boolean[] days) {
		String query = "INSERT INTO Alarms(goal_id, monday, tuesday, wednesday, thursday, friday, saturday, sunday, time, min) "
						+ "VALUES(" + goalId + "," + TypeUtils.BooleanToInteger(days[0]) + "," + TypeUtils.BooleanToInteger(days[1]) + "," +  TypeUtils.BooleanToInteger(days[2]) + "," +
						+ TypeUtils.BooleanToInteger(days[3]) + "," + TypeUtils.BooleanToInteger(days[4]) + "," +  TypeUtils.BooleanToInteger(days[5]) + "," 
						+  TypeUtils.BooleanToInteger(days[6]) + "," + time + "," + min + ")";
		boolean isSuccess = SQLClient.executeUpdate(databaseHelper, query);
		return isSuccess;
	}

	/**
	 * 위치 정보 저장
	 */
	public boolean insertLocations(int goalId, Double latitude, Double longitude) {
		String query = "INSERT INTO Locations(goal_id, latitude, longitude) "
						+ "VALUES(" + goalId + "," + latitude + "," + longitude + ")";
		boolean isSuccess = SQLClient.executeUpdate(databaseHelper, query);
		return isSuccess;
	}
	
	/**
	 * 목표 아이디 가져오기
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getGoalId(String createDate) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Integer goalId = -1;
		String query = "SELECT id FROM Goals WHERE create_date = '" + createDate + "'";
		ArrayList<HashMap> rows = SQLClient.executeQuery(databaseHelper, query, new Class[] { Integer.class });
		for (HashMap row : rows) {
			list.add((Integer) row.get("id"));
		}
		if (list.size() == 1) {
			goalId = list.get(0);
		}
		return goalId;
	}

	/**
	 * 목표에 관련된 데이터 가져오기
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AddPromiseDTO get(int id) {
		String query = "SELECT id, post_id, category, title, start_date, end_date, content, result, donation, create_date FROM Goals WHERE id = " + id;
		return parser(query);
	}
	
	/**
	 * 목표에 관련된 데이터 가져오기
	 */
	@Override
	public AddPromiseDTO get(String postId) {
		String query = "SELECT id, post_id, category, title, start_date, end_date, content, result, donation, create_date FROM Goals WHERE post_id = '" + postId + "'";
		return parser(query);
	}
	
	/**
	 * 목표에 관련된 데이터 가져온 후 객체로 변환
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AddPromiseDTO parser (String query){
		AddPromiseDTO promiseDTO = new AddPromiseDTO();
		ArrayList<HashMap> rows = SQLClient.executeQuery(databaseHelper, query, new Class[] {Integer.class, String.class, Integer.class, String.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class });
		for (HashMap row : rows) {
			promiseDTO.setId((Integer) row.get("id"));
			promiseDTO.setPostId((String) row.get("post_id"));
			promiseDTO.setCategoryId((Integer) row.get("category"));
			promiseDTO.setTitle((String) row.get("title"));
			promiseDTO.setStartDate((String) row.get("start_date"));
			promiseDTO.setEndDate((String) row.get("end_date"));
			promiseDTO.setContent((String) row.get("content"));
			promiseDTO.setResult((Integer) row.get("result"));
			promiseDTO.setDonationId((Integer) row.get("donation"));
			promiseDTO.setCreateDate((String) row.get("create_date"));
		}
		
		//TODO
		switch (promiseDTO.getCategoryId()) {
		case 0: // Locations, Alarms
			query = "SELECT latitude, longitude FROM Locations WHERE goal_id = " + promiseDTO.getId();
			rows = SQLClient.executeQuery(databaseHelper, query, new Class[] {Double.class, Double.class});
			for (HashMap row : rows) {
				promiseDTO.setLatitue((Double) row.get("latitude"));
				promiseDTO.setLongitude((Double) row.get("longitude"));
			}
		case 1: // Alarms 
			query = "SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday, time, min FROM Alarms WHERE goal_id = " + promiseDTO.getId();
			rows = SQLClient.executeQuery(databaseHelper, query, new Class[] {Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class});
			for (HashMap row : rows) {
				boolean[] selectDay = { TypeUtils.IntegerToBoolean((Integer) row.get("monday")), TypeUtils.IntegerToBoolean((Integer) row.get("tuesday")), TypeUtils.IntegerToBoolean((Integer) row.get("wednesday")),
										TypeUtils.IntegerToBoolean((Integer) row.get("thursday")), TypeUtils.IntegerToBoolean((Integer) row.get("friday")), TypeUtils.IntegerToBoolean((Integer) row.get("saturday")),
										TypeUtils.IntegerToBoolean((Integer) row.get("sunday"))};
				promiseDTO.setDayPeriod(selectDay);
				promiseDTO.setTime((Integer) row.get("time"));
				promiseDTO.setMin((Integer) row.get("min"));
			}
			break;
		case 2: // X
			break;
		}
		return promiseDTO;
		
	}
	
	/**
	 * Database에 저장된 목표리스트 가져오기
	 */
	@Override
	public ArrayList<AddPromiseDTO> getList() {
		String query= "SELECT id FROM Goals WHERE result=0 ORDER BY id";
		return getList(query);
	}
	/**
	 * Database에서 요일에 맞는 목표리스트 가져오기
	 */
	public ArrayList<AddPromiseDTO> getGoalList(int day) {
		ArrayList<AddPromiseDTO> list = new ArrayList<AddPromiseDTO>();
		//TODO 미평가 목록
		String query= "SELECT id FROM Goals WHERE result = 0 AND end_date<"+ DateUtils.convertStringDate(DateUtils.getDate()) + " ORDER BY id";
		list = getList(query);
		
		//요일에 맞는 평가 목록
		query = "SELECT gt.id as id"
				+ " FROM "
				+ " (SELECT id FROM Goals WHERE end_date >= "+DateUtils.convertStringDate(DateUtils.getDate()) + ") as gt"
				+ " INNER JOIN"
				+ " (SELECT goal_id FROM Alarms WHERE ";
		
		switch (day) {    
		case 1:
			query += "sunday=1";
			break;
		case 2:
			query += "monday=1";
			break;
		case 3:
			query += "tuesday=1";
			break;
		case 4:
			query += "wednesday=1";
			break;
		case 5:
			query += "thursday=1";
			break;
		case 6:
			query += "friday=1";
			break;
		case 7:
			query += "saturday=1";
			break;
		}
		query += ") as at"
				+ " ON gt.id = at.goal_id";
		
		list.addAll(getList(query));
		
//		query = "SELECT gt.id as id"
//				+ " FROM "
//				+ " (SELECT id FROM Goals WHERE end_date >= "+DateUtils.convertStringDate(DateUtils.getDate()) + ") as gt"
//				+ " INNER JOIN"
//				+ " (SELECT goal_id FROM Alarms WHERE monday=1) as at"
//				+ " ON gt.id = at.goal_id";
		
		// (SELECT gt.id FROM Goals WHERE ent_date >= NOW())
			// as gt
	// inner join
		// (SELECT at.goal_id FROM Alarms WHERE alram.monday=1)
			// as at
		// ON gt.id = at.goal_id
		
		//기타 목록
		query = "SELECT id FROM Goals WHERE result = 0 AND category=2 AND end_date>="+ DateUtils.convertStringDate(DateUtils.getDate()) + " ORDER BY id";
		list.addAll(getList(query));
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<AddPromiseDTO> getList(String query) {
		ArrayList<AddPromiseDTO> list = new ArrayList<AddPromiseDTO>();
		ArrayList<HashMap> rows = SQLClient.executeQuery(databaseHelper, query, new Class[] { Integer.class });
		for (HashMap row : rows) {
			AddPromiseDTO promiseDTO = get((Integer) row.get("id"));
			list.add(promiseDTO);
		}
		return list;
	}
	
	/**
	 * Database에 결과 정보 Update
	 */
	public boolean update(int id, int result){
		String query = "UPDATE Goals SET result = " + result + " WHERE id = " + id;
		boolean isSuccess = false;
		if (SQLClient.executeUpdate(databaseHelper, query)) {
			isSuccess = true;
		}
		return isSuccess;
	}
}