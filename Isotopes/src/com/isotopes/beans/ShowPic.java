package com.isotopes.beans;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;

public class ShowPic extends BmobObject implements Comparable<ShowPic>{
	private String title;//标题
	private List<BmobFile> imgList;//图片
	private User create_user;//发布者
	private List<String> like_user_name;//点赞的人的名字
	private String power;//权限：公开，好友可见，私密
	private BmobGeoPoint point;//定位信息
	private String address;//定位文字信息
	
	public ShowPic(){
		imgList = new ArrayList<BmobFile>();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<BmobFile> getImgList() {
		return imgList;
	}
	public void setImgList(List<BmobFile> imgList) {
		this.imgList = imgList;
	}
	public User getCreate_user() {
		return create_user;
	}
	public void setCreate_user(User create_user) {
		this.create_user = create_user;
	}
	public List<String> getLike_user_name() {
		return like_user_name;
	}
	public void setLike_user_name(List<String> like_user_name) {
		this.like_user_name = like_user_name;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public BmobGeoPoint getPoint() {
		return point;
	}
	public void setPoint(BmobGeoPoint point) {
		this.point = point;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int compareTo(ShowPic another) {
		// TODO Auto-generated method stub
		 int m1=0;
		 if(this.getLike_user_name() != null){
			 m1 = this.getLike_user_name().size();
		 }
		 int m2=0;
		 if(another.getLike_user_name() != null){
			 m2=another.getLike_user_name().size();
		 }
         int result=0;
         if(m1>m2)
         {
             result=-1;
         }
         if(m1<m2)
         {
             result=1;
         }
         return result;
	}
}
