package com.isotopes.beans;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Banner extends BmobObject{
	private String cityId;
	private BmobFile imgFile;
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public BmobFile getImgFile() {
		return imgFile;
	}
	public void setImgFile(BmobFile imgFile) {
		this.imgFile = imgFile;
	}
	
}
