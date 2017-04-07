package com.isotopes.beans;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class FriendTable extends BmobObject {
	private String user_object_id;
	private List<User> friendList;
	
	public FriendTable(){
		friendList = new ArrayList<User>();
	}

	public String getUser_object_id() {
		return user_object_id;
	}

	public void setUser_object_id(String user_object_id) {
		this.user_object_id = user_object_id;
	}

	public List<User> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<User> friendList) {
		this.friendList = friendList;
	}
	
}
