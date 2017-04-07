package com.isotopes.beans;

import java.util.Date;
import cn.bmob.v3.BmobObject;

public class Notice extends BmobObject{
	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}	
