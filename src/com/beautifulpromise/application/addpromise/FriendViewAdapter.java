package com.beautifulpromise.application.addpromise;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.beautifulpromise.R;
import com.facebook.halo.application.types.connection.Friends;

/**
 * @description 친구 이미지의 List 객체를 ListView에서 표시할 수 있게 해주는 Adapter
 * @author immk
 *
 */
public class FriendViewAdapter extends BaseAdapter {
	
	Context context;
	private List<Friends> friendsList;
	private LayoutInflater inflater;
	ViewHolder holder;
	
	/**
	 * Custom Adapter 생성자
	 * @param context Context
	 * @param friendsList 친구 목록 리스트
	 */
	public FriendViewAdapter(Context context, List<Friends> friendsList) {
		this.context = context;
		this.friendsList=friendsList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 리스트의 갯수
	 */
	@Override
	public int getCount() {
		return getFriendsList().size();
	}

	/**
	 * 리스트 중 해당 position의 객체 정보 가져오기 
	 */
	@Override
	public Object getItem(int position) {
		return getFriendsList().get(position);
	}

	/**
	 * 리스트 중 해당 position의 id값 가져오기
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * ViewHolder
	 * @author immk
	 */
	public class ViewHolder {
		public CheckedTextView friendNameText;
	}

	/**
	 * 리스트 중 해당 postion에 해당하는 데이터를 화면에 보여주기
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friends friend = getFriendsList().get(position);

		if(convertView == null) {
			convertView = inflater.inflate(R.layout.addpromise_friendlist_item, null);

			holder = new ViewHolder();
			holder.friendNameText = (CheckedTextView) convertView.findViewById(R.id.friend_name_text);
			convertView.setTag(holder);
		
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.friendNameText.setText(friend.getName());
		
		return convertView;
	}
	
	/**
	 * adapter의 데이터가 변했을 때 호출하는 함수
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public List<Friends> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<Friends> friendsList) {
		this.friendsList = friendsList;
	}

}
