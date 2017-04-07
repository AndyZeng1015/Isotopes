package com.isotopes.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isotopes.R;
import com.isotopes.appwidget.SlideShowView;
import com.isotopes.appwidget.SlideShowView.OnViewPagerItemClickListener;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.HotSpotsBean;

public class HotSpotsDetailActivity extends BaseActivity {
	
	private HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean contentlistBean;
	
	private ImageView iv_back;
	private TextView tv_title;
	private SlideShowView ss_show;
	private ImageView iv_no_pic;
	private TextView tv_content_summary;
	
	private LinearLayout ll_attention;
	private TextView tv_attention;
	
	private LinearLayout ll_coupon;
	private TextView tv_coupon;
	
	private LinearLayout ll_opentime;
	private TextView tv_opentime;
	
	private RelativeLayout rl_price;
	private TextView tv_price;
	
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
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HotSpotsDetailActivity.this.finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		//设置标题
		tv_title.setText(contentlistBean.getName());
		//设置图片轮播 
		List<String> imgPathList = new ArrayList<String>();
		for(int i=0; i<contentlistBean.getPicList().size(); i++){
			imgPathList.add(contentlistBean.getPicList().get(i).getPicUrl());
		}
		if(imgPathList.size() <= 0){
			iv_no_pic.setVisibility(View.VISIBLE);
			ss_show.setVisibility(View.GONE);
		}else{
			iv_no_pic.setVisibility(View.GONE);
			ss_show.setVisibility(View.VISIBLE);
			ss_show.initAndStartBanner(imgPathList, new OnViewPagerItemClickListener() {
				
				@Override
				public boolean onClick(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		//设置攻略概况
		String content = "";
		if(contentlistBean.getSummary() != null){
			content += contentlistBean.getSummary();
		}
		if(contentlistBean.getContent() != null){
			content += contentlistBean.getContent();
		}
		if(content.equals("")){
			tv_content_summary.setText("暂无");
		}else{
			tv_content_summary.setText(Html.fromHtml(content));
		}
		
		//设置注意事项
		if(contentlistBean.getAttention() == null || contentlistBean.getAttention().equals("")){
			ll_attention.setVisibility(View.GONE);
		}else{
			ll_attention.setVisibility(View.VISIBLE);
			tv_attention.setText(Html.fromHtml(contentlistBean.getAttention()));
		}
		
		//设置优惠政策
		if(contentlistBean.getCoupon() == null || contentlistBean.getCoupon().equals("")){
			ll_coupon.setVisibility(View.GONE);
		}else{
			ll_coupon.setVisibility(View.VISIBLE);
			tv_coupon.setText(Html.fromHtml(contentlistBean.getCoupon()));
		}
		
		//设置开放时间
		if(contentlistBean.getOpentime() == null || contentlistBean.getOpentime().equals("")){
			ll_opentime.setVisibility(View.GONE);
		}else{
			ll_opentime.setVisibility(View.VISIBLE);
			tv_opentime.setText(Html.fromHtml(contentlistBean.getOpentime()));
		}
		
		//设置价格
		if(contentlistBean.getPrice() == null || contentlistBean.getPrice().equals("")){
			rl_price.setVisibility(View.GONE);
		}else{
			rl_price.setVisibility(View.VISIBLE);
			tv_price.setText(Html.fromHtml("门票："+contentlistBean.getPrice()));
		}
		
	}

	private void initView(){
		setContentView(R.layout.activity_hot_spots_detail);
		iv_back =(ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		ss_show = (SlideShowView) findViewById(R.id.ss_show);
		iv_no_pic = (ImageView) findViewById(R.id.iv_no_pic);
		tv_content_summary = (TextView) findViewById(R.id.tv_content_summary);
		
		ll_attention = (LinearLayout) findViewById(R.id.ll_attention);
		tv_attention = (TextView) findViewById(R.id.tv_attention);
		
		ll_coupon = (LinearLayout) findViewById(R.id.ll_coupon);
		tv_coupon = (TextView) findViewById(R.id.tv_coupon);
		
		ll_opentime = (LinearLayout) findViewById(R.id.ll_opentime);
		tv_opentime = (TextView) findViewById(R.id.tv_opentime);
		
		rl_price = (RelativeLayout) findViewById(R.id.rl_price);
		tv_price = (TextView) findViewById(R.id.tv_price);
		
		contentlistBean = getIntent().getBundleExtra("key").getParcelable("content");
	}
}
