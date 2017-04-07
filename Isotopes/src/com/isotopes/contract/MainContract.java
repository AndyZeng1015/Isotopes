package com.isotopes.contract;

import java.util.List;

import com.isotopes.beans.Banner;
import com.isotopes.beans.Notice;

public interface MainContract {
	public interface View{
		public void setNotice(String value);
		public void setBanner(List<Banner> bannerList);
    }

    public interface Presenter{
    	public void getNotice();
		public void getBanner();
    }
}
