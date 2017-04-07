package com.isotopes.presenter;

import java.util.List;

import rx.Subscriber;
import android.content.Context;
import android.util.Log;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;

import com.isotopes.beans.Banner;
import com.isotopes.beans.Notice;
import com.isotopes.contract.MainContract;
import com.isotopes.utils.ToastUtil;

public class MainPresenter implements MainContract.Presenter{
	private MainContract.View view;
	private Context mContext;
	
	public MainPresenter(Context context, MainContract.View view){
		this.mContext = context;
		this.view = view;
	}

	@Override
	public void getNotice() {
		// TODO Auto-generated method stub
		BmobQuery<Notice> query = new BmobQuery<Notice>();
		query.setLimit(1);
		query.order("createdAt");

		boolean isCache = query.hasCachedResult(Notice.class);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}
		query.findObjectsObservable(Notice.class)
		.subscribe(new Subscriber<List<Notice>>() {
			@Override
			public void onCompleted() {}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(List<Notice> notice) {
				if(notice != null && notice.size() > 0){
					view.setNotice(notice.get(0).getContent());
				}
			}
		});
	}

	@Override
	public void getBanner() {
		// TODO Auto-generated method stub
		BmobQuery<Banner> query = new BmobQuery<Banner>();
		boolean isCache = query.hasCachedResult(Banner.class);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}
		query.findObjectsObservable(Banner.class)
		.subscribe(new Subscriber<List<Banner>>() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				ToastUtil.showToast(mContext, arg0.getMessage());
			}

			@Override
			public void onNext(List<Banner> bannerList) {
				// TODO Auto-generated method stub
				if(bannerList != null && bannerList.size() > 0){
					//设置Banner
					view.setBanner(bannerList);
				}
			}
		});
	}
	
}
