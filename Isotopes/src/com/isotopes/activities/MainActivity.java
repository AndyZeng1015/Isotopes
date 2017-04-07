package com.isotopes.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.isotopes.R;
import com.isotopes.appwidget.SlideShowView;
import com.isotopes.appwidget.SlideShowView.OnViewPagerItemClickListener;
import com.isotopes.base.BaseActivity;
import com.isotopes.base.BaseApplication;
import com.isotopes.beans.Banner;
import com.isotopes.contract.MainContract;
import com.isotopes.presenter.MainPresenter;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.ToastUtil;

public class MainActivity extends BaseActivity implements MainContract.View{
	
	private long mExitTime = Integer.MIN_VALUE;
	private MainContract.Presenter mIPresenter;
	
	private SlideShowView ss_show;
	private LinearLayout ll_friend;
	private LinearLayout ll_square;
	private LinearLayout ll_search;
	private ImageView iv_person;
	private TextView tv_notice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mIPresenter = new MainPresenter(mContext, MainActivity.this);
		initView();
		initData();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		//点击用户
		iv_person.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//判断是否登录，如果未登录，跳到登录界面，否则跳转到个人中心界面
				if(Contast.user != null){
					//登录了
					jump(MainActivity.this, PersonalActivity.class, false);
				}else{
					//未登录
					DialogUtils.createAlertDialog(mContext, "提示", "请先登录再进行操作！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Contast.className = PersonalActivity.class;
							jump(MainActivity.this, LoginActivity.class, false);
						}
					}).show();
				}
			}
		});
		
		//点击好友
		ll_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Contast.user != null){
					//登录了
					jump(MainActivity.this, FriendActivity.class, false);
				}else{
					//未登录
					DialogUtils.createAlertDialog(mContext, "提示", "请先登录再进行操作！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Contast.className = FriendActivity.class;
							jump(MainActivity.this, LoginActivity.class, false);
						}
					}).show();
				}
			}
		});
		
		ll_square.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Contast.user != null){
					//登录了
					jump(MainActivity.this, SquareActivity.class, false);
				}else{
					//未登录
					DialogUtils.createAlertDialog(mContext, "提示", "请先登录再进行操作！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Contast.className = SquareActivity.class;
							jump(MainActivity.this, LoginActivity.class, false);
						}
					}).show();
				}
			}
		});
		
		ll_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Contast.user != null){
					//登录了
					jump(MainActivity.this, HotSpotsActivity.class, false);
				}else{
					//未登录
					DialogUtils.createAlertDialog(mContext, "提示", "请先登录再进行操作！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Contast.className = HotSpotsActivity.class;
							jump(MainActivity.this, LoginActivity.class, false);
						}
					}).show();
				}
			}
		});
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		mIPresenter.getBanner();//获取轮播图
		mIPresenter.getNotice();//获取通知
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		iv_person = (ImageView) findViewById(R.id.iv_person);
		ss_show = (SlideShowView) findViewById(R.id.ss_show);
		ll_friend = (LinearLayout) findViewById(R.id.ll_friend);
		ll_square = (LinearLayout) findViewById(R.id.ll_square);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_notice.setSelected(true);  
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(System.currentTimeMillis() - mExitTime < 2000){
			//先退出账号
			EMClient.getInstance().logout(true, new EMCallBack() {
	            
			    @Override
			    public void onSuccess() {
			        // TODO Auto-generated method stub
			    	Contast.user = null;//清空用户信息
			    	Contast.className = MainActivity.class;//设置登录成功后跳转的页面
					BaseApplication.getInstance().exit();
			    }
			    
			    @Override
			    public void onProgress(int progress, String status) {
			        // TODO Auto-generated method stub
			        
			    }
			    
			    @Override
			    public void onError(int code, String message) {
			        // TODO Auto-generated method stub
			    }
			});
			super.onBackPressed();
		}else {
			ToastUtil.showToast(mContext, "再点一次退出");
			mExitTime = System.currentTimeMillis();
		}
	}

	@Override
	public void setNotice(String value) {
		// TODO Auto-generated method stub
		tv_notice.setText(value);
	}

	private List<Banner> mBannerList;
	
	@Override
	public void setBanner(List<Banner> bannerList) {
		// TODO Auto-generated method stub
		mBannerList = bannerList;
		List<BmobFile> fileList = new ArrayList<BmobFile>();
		for(Banner banner : mBannerList){
			fileList.add(banner.getImgFile());
		}
		ss_show.initAndStartBanner(fileList, new OnViewPagerItemClickListener() {
			
			int flage = 0 ;
			float downX = 0;
			float downY = 0;
			
			@Override
			public boolean onClick(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
	                flage = 0 ;
	                downX = event.getX();
	                downY = event.getY();
	                break ;
	            case MotionEvent.ACTION_MOVE:
	            	float abs = (event.getX()-downX)*(event.getX()-downX) + (event.getY() - downY)*(event.getY() - downY);
	                if(abs > 12){
	                	flage = 1;
	                }
	                break ;
	            case  MotionEvent.ACTION_UP :
	                if (flage == 0) {
	                    int item = ss_show.getViewPager().getCurrentItem();
	                    Bundle bundle = new Bundle();
	                    bundle.putString("city", mBannerList.get(item).getCityId());
	                    jump(MainActivity.this, WebViewActivity.class, "keys", bundle, false);
	                }

				default:
					break;
				}
			return false;
			}
		});
	}
}
