package com.isotopes.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import com.isotopes.R;
import com.isotopes.adapter.ListShowAdapter;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.ShowPic;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.ToastUtil;

public class SquareActivity extends BaseActivity {
	
	private ListView lv_list;
	private ImageView iv_change;//修改显示模式
	private ImageView iv_back;//返回
	private ImageView iv_send;//发布
	
	private Dialog dialog;
	private ListShowAdapter adapter;
	private List<ShowPic> showPicList = new ArrayList<ShowPic>();
	
	private int show_model = 0;//显示模式，0为显示最新的，1为显示点赞数最多的
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SquareActivity.this.finish();
			}
		});
		
		iv_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump(SquareActivity.this, ReleaseActivity.class, false);
			}
		});
		
		iv_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(show_model == 0){
					//变为1
					show_model = 1;
					ToastUtil.showToast(mContext, "当前为按点赞数排列");
				}else if(show_model == 1){
					//变为0
					show_model = 0;
					ToastUtil.showToast(mContext, "当前为按最新发布排列");
				}
				initData();
			}
		});
		
		adapter.setOnClickListener(new ListShowAdapter.OnClickListener() {
			
			@Override
			public void onClick(View v, ShowPic showPic) {
				// TODO Auto-generated method stub
				if(showPic.getCreate_user().getObjectId().equals(Contast.user.getObjectId())){
					//是自己发布的，点击头像无效
				}else{
					//跳转显示这个人的信息和他发布的信息
					Bundle bundle = new Bundle();
					bundle.putString("objectId", showPic.getCreate_user().getObjectId());
					jump(SquareActivity.this, ShowCustomerDetailActivity.class, "key", bundle, false);
				}
			}
		});
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		if(show_model == 0){
			//获取图片列表
			//查询所有公开的数据
			BmobQuery<ShowPic> query = new BmobQuery<ShowPic>();
			query.order("-createdAt");
			query.addWhereEqualTo("power", "所有人");
		
			boolean isCache = query.hasCachedResult(ShowPic.class);
//			if(isCache){
//				query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	
//			}else{
//				query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
//			}
			query.findObjectsObservable(ShowPic.class)
			.subscribe(new Subscriber<List<ShowPic>>() {
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					dialog.show();
					super.onStart();
				}
				
				@Override
				public void onCompleted() {
					dialog.dismiss();
				}
		
				@Override
				public void onError(Throwable e) {
					e.printStackTrace();
				}
		
				@Override
				public void onNext(List<ShowPic> showPic) {
					if(showPic != null){
						//获取到数据，进行刷新
						showPicList = showPic;
						adapter.refreshData(showPicList);
					}
				}
			});
		}else if(show_model == 1){
			BmobQuery<ShowPic> query = new BmobQuery<ShowPic>();
			query.addWhereEqualTo("power", "所有人");
		
			boolean isCache = query.hasCachedResult(ShowPic.class);
//			if(isCache){
//				query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	
//			}else{
//				query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
//			}
			query.findObjectsObservable(ShowPic.class)
			.subscribe(new Subscriber<List<ShowPic>>() {
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					dialog.show();
					super.onStart();
				}
				
				@Override
				public void onCompleted() {
					dialog.dismiss();
				}
		
				@Override
				public void onError(Throwable e) {
					e.printStackTrace();
				}
		
				@Override
				public void onNext(List<ShowPic> showPic) {
					if(showPic != null){
						//将数据重新进行排序
						Collections.sort(showPic);
						//获取到数据，进行刷新
						showPicList = showPic;
						adapter.refreshData(showPicList);
						lv_list.setSelection(0);
					}
				}
			});
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_square);
		lv_list = (ListView) findViewById(R.id.lv_list);
		iv_change = (ImageView) findViewById(R.id.iv_change);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_send = (ImageView) findViewById(R.id.iv_send);
		
		dialog = DialogUtils.createLoadingDialog(mContext, "加载中，请稍候~");
		
		adapter = new ListShowAdapter(mContext, showPicList);
		lv_list.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initData();
		super.onResume();
	}
}
