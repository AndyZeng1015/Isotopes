package com.isotopes.appwidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.xutils.x;
import org.xutils.image.ImageOptions;

import cn.bmob.v3.datatype.BmobFile;

import com.isotopes.R;
import com.isotopes.utils.DensityUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class SlideShowView extends FrameLayout {

	private final static int IMAGE_COUNT = 5;
	private final static int TIME_INTERVAL = 5;
	private final static boolean isAutoPlay = true; 
	
	private List<ImageView> imageViewsList;
	private List<View> dotViewsList;
	
	private ViewPager viewPager;
	private LinearLayout ll_dot;
	private int currentItem  = 0;
	private ScheduledExecutorService scheduledExecutorService;
	private Context mContext;
	
	//Handler
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}
		
	};
	
	public ViewPager getViewPager(){
		return viewPager;
	}
	
	public SlideShowView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}
	
	/**
	 * 初始化数据并启动轮播
	 */
	public void initAndStartBanner(List<?> imgFiles, OnViewPagerItemClickListener listener ){
		initData();
		initUI(mContext, imgFiles);
		if(isAutoPlay){
			startPlay();
		}
		initListener(listener);
	}
	
	private void initListener(final OnViewPagerItemClickListener listener) {
		// TODO Auto-generated method stub
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return listener.onClick(v, event);
			}
		});
	}

	private void startPlay(){
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
	}

	private void stopPlay(){
		scheduledExecutorService.shutdown();
	}

	private void initData(){
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
		
	}
	
	
	private void initUI(Context context, List<?> fileList){
		LayoutInflater.from(context).inflate(R.layout.view_pager, this, true);
		if(fileList.get(0) instanceof BmobFile){
			for(Object file : fileList){
				ImageView view =  new ImageView(context);
				Picasso.with(mContext).load(((BmobFile) file).getFileUrl()).placeholder(R.drawable.pic_loading).error(R.drawable.pic_load_error).into(view);
				view.setScaleType(ScaleType.FIT_XY);
				imageViewsList.add(view);
			}
			
			viewPager = (ViewPager) findViewById(R.id.viewPager);
			viewPager.setFocusable(true);
			
			//放置dot的容器
			ll_dot = (LinearLayout) findViewById(R.id.ll_dot);
			
			int count = fileList.size();
			for(int i=0; i<count; i++){
				View view = new View(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 8), DensityUtil.dip2px(mContext, 8));
				view.setBackgroundResource(R.drawable.dot_black);
				if(i != 0){
					params.leftMargin = DensityUtil.dip2px(mContext, 5);
				}
				view.setLayoutParams(params);
				
				ll_dot.addView(view);
				
				dotViewsList.add(view);
				
			}
			
			viewPager.setAdapter(new MyPagerAdapter());
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
		}else{
			for(Object file : fileList){
				ImageView view =  new ImageView(context);
				Picasso.with(mContext).load((String)file).placeholder(R.drawable.pic_loading).error(R.drawable.pic_load_error).into(view);
				view.setScaleType(ScaleType.FIT_XY);
				imageViewsList.add(view);
			}
			
			viewPager = (ViewPager) findViewById(R.id.viewPager);
			viewPager.setFocusable(true);
			
			//放置dot的容器
			ll_dot = (LinearLayout) findViewById(R.id.ll_dot);
			ll_dot.setVisibility(View.GONE);
			
			viewPager.setAdapter(new MyPagerAdapter());
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
		}
		
	}
	
	private class MyPagerAdapter  extends PagerAdapter{

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//((ViewPag.er)container).removeView((View)object);
			((ViewPager)container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private class MyPageChangeListener implements OnPageChangeListener{

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
            case 1:
                isAutoPlay = false;
                break;
            case 2:
            	isAutoPlay = true;
                break;
            case 0:
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                    viewPager.setCurrentItem(0);
                }
                else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                }
                break;
        }
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int pos) {
			// TODO Auto-generated method stub
			
			currentItem = pos;
			for(int i=0;i < dotViewsList.size();i++){
				if(i == pos){
					((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_black);
				}else {
					((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_white);
				}
			}
		}
		
	}
	
	private class SlideShowTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem = (currentItem+1)%imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}
		
	}

	private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                drawable.setCallback(null);
            }
        }
    }
	
	public interface OnViewPagerItemClickListener{
		boolean onClick(View v, MotionEvent event);
	}
	
}
