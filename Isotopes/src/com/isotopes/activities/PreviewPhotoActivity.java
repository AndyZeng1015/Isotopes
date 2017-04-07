package com.isotopes.activities;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isotopes.R;
import com.isotopes.base.BaseActivity;
import com.isotopes.base.BaseApplication;
import com.isotopes.utils.ImageUtils;
import com.squareup.picasso.Picasso;

public class PreviewPhotoActivity extends BaseActivity {
	
	private int count = 0;
	private List<String> pathList;
	private int index = 0;
	
	private TextView tv_count;
	private ViewPager vp_photo;
	private ImageView iv_back;
	
	private ArrayList<ImageView> imageViews;
	
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
				PreviewPhotoActivity.this.finish();
			}
		});
		
		vp_photo.setOnPageChangeListener(pageChangeListener);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getBundleExtra("key");
		count = bundle.getInt("count");
		pathList = bundle.getStringArrayList("path");
		
		//设置张数
		tv_count.setText((index+1) +"/"+ count);
		//设置图片
		BaseApplication.getInstance().execRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					imageViews = new ArrayList<ImageView>();
					for(int i=0; i<count; i++){
						Bitmap defaultImg = Picasso.with(mContext).load(pathList.get(i)).config(Bitmap.Config.RGB_565).get();
						ImageView iv_pic = new ImageView(mContext);
						iv_pic.setLayoutParams(new RelativeLayout.LayoutParams(ImageUtils.getWidget_Width(mContext), ImageUtils.get_Image_heigth(mContext, defaultImg)));
						iv_pic.setImageBitmap(defaultImg);
						imageViews.add(iv_pic);
					}
					((BaseActivity)mContext).runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							vp_photo.setAdapter(new MyPageAdapter(imageViews));
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_preview_photo);
		tv_count = (TextView) findViewById(R.id.tv_count);
		vp_photo = (ViewPager) findViewById(R.id.vp_photo);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		// 页面选择响应函数
		public void onPageSelected(int arg0) {
			index = arg0;
			tv_count.setText((index + 1) + "/" + count);
		}

		// 滑动中
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		// 滑动状态改变
		public void onPageScrollStateChanged(int arg0) {

		}

	};

	private class MyPageAdapter extends PagerAdapter {

		private ArrayList<ImageView> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<ImageView> listViews) {// 构造函数
			// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}
