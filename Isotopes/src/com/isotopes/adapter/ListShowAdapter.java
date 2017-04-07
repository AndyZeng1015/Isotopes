package com.isotopes.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import com.isotopes.R;
import com.isotopes.activities.PreviewPhotoActivity;
import com.isotopes.activities.ShowCustomerDetailActivity;
import com.isotopes.appwidget.CircleTransform;
import com.isotopes.base.BaseActivity;
import com.isotopes.base.BaseApplication;
import com.isotopes.beans.ShowPic;
import com.isotopes.beans.User;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DensityUtil;
import com.isotopes.utils.ImageUtils;
import com.isotopes.utils.ToastUtil;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListShowAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<ShowPic> showPicList;
	
	private OnClickListener listener;
	
	public void setOnClickListener(OnClickListener listener){
		this.listener = listener;
	}
	
	public ListShowAdapter(Context context, List<ShowPic> showPicList){
		mContext = context;
		this.showPicList = showPicList;
	}
	
	public void refreshData(List<ShowPic> showPicList){
		this.showPicList = showPicList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(showPicList != null){
			//如果没有数据，返回1
			if(showPicList.size() == 0){
				return 1;
			}else{
				return showPicList.size();
			}
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(showPicList != null){
			return showPicList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(showPicList != null && showPicList.size() != 0){
			ShowViewHolder holder;
			if(convertView == null || convertView instanceof TextView){
				convertView = View.inflate(mContext, R.layout.item_personal_list, null);
				holder = new ShowViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ShowViewHolder) convertView.getTag();
			
			holder.setData(showPicList.get(position));
			return convertView;
		}else{
			TextView tv_nodata = new TextView(mContext);
			tv_nodata.setText("暂时没有数据！");
			tv_nodata.setTextSize(28f);
			tv_nodata.setGravity(Gravity.CENTER);
			tv_nodata.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 200)));
			return tv_nodata;
		}
	}

	public class ShowViewHolder {
		
		private View mView;
		private ImageView iv_head;
		private TextView tv_name;
		private ImageView iv_like;
		private ImageView iv_default;
		private TextView tv_title;
		private TextView tv_like_person;
		private TextView tv_count;
		private LinearLayout ll_like_person;
		private RelativeLayout rl_addr;
		private TextView tv_addr;
		private LinearLayout ll_head;
		
		public ShowViewHolder(View view){
			mView = view;
			iv_head = (ImageView) mView.findViewById(R.id.iv_head);
			tv_name = (TextView) mView.findViewById(R.id.tv_name);
			iv_like = (ImageView) mView.findViewById(R.id.iv_like);
			iv_default = (ImageView) mView.findViewById(R.id.iv_default);
			tv_title = (TextView) mView.findViewById(R.id.tv_title);
			tv_like_person = (TextView) mView.findViewById(R.id.tv_like_person);
			tv_count = (TextView) mView.findViewById(R.id.tv_count);
			ll_like_person = (LinearLayout) mView.findViewById(R.id.ll_like_person);
			tv_addr = (TextView) mView.findViewById(R.id.tv_addr);
			rl_addr = (RelativeLayout) mView.findViewById(R.id.rl_addr);
			ll_head = (LinearLayout) mView.findViewById(R.id.ll_head);
		}
		
		public void setData(final ShowPic showPic){
			//发布者信息
			BmobQuery<User> query = new BmobQuery<User>();
			query.getObject(showPic.getCreate_user().getObjectId(), new QueryListener<User>() {

				@Override
				public void done(User user, BmobException arg1) {
					// TODO Auto-generated method stub
					Picasso.with(mContext).load(user.getHeadimg().getFileUrl()).placeholder(R.drawable.head_default).error(R.drawable.head_default).transform(new CircleTransform()).into(iv_head);
					tv_name.setText(user.getUsername());
				}
			});
			tv_title.setText(showPic.getTitle());
			//设置图片的张数显示
			if(showPic.getImgList() != null && showPic.getImgList().size() > 1){
				tv_count.setText(showPic.getImgList().size()+"");
				tv_count.setVisibility(View.VISIBLE);
			}else{
				tv_count.setVisibility(View.GONE);
			}
			//设置点赞的人
			setLike_user_name(showPic, ll_like_person, tv_like_person);
			
			//设置点赞图标显示
			if(showPic.getCreate_user().getObjectId().equals(Contast.user.getObjectId())){
				//是自己发布的，不显示点赞按钮
				iv_like.setVisibility(View.GONE);
			}else{
				iv_like.setVisibility(View.VISIBLE);
				
				//判断是否已经点赞
				if(showPic.getLike_user_name() != null && showPic.getLike_user_name().size() > 0 && showPic.getLike_user_name().contains(Contast.user.getUsername())){
					iv_like.setImageResource(R.drawable.dashboard_like_on_default);
				}else{
					iv_like.setImageResource(R.drawable.dashboard_like_off_default);
				}
				
				//设置点赞按钮事件
				iv_like.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(showPic.getLike_user_name() != null && showPic.getLike_user_name().contains(Contast.user.getUsername())){
							ToastUtil.showToast(mContext, "你已经点过赞了！");
						}else{
							//说明还没有点赞
							if(showPic.getLike_user_name() == null){
								showPic.setLike_user_name(new ArrayList<String>());
							}else{
								showPic.getLike_user_name().add(Contast.user.getUsername());//将自己的名字加入到喜欢的人
								showPic.update(showPic.getObjectId(), new UpdateListener() {
									
									@Override
									public void done(BmobException arg0) {
										// TODO Auto-generated method stub
										if(arg0 == null){
											//点赞成功
											iv_like.setImageResource(R.drawable.dashboard_like_on_default);
											setLike_user_name(showPic, ll_like_person, tv_like_person);
										}else{
											ToastUtil.showToast(mContext, arg0.getMessage());
										}
									}
								});
							}
						}
					}
				});
			}
			
			//图片的点击全屏显示
			iv_default.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//预览大图
					int count = showPic.getImgList().size();
					ArrayList<String> path = new ArrayList<String>();
					for(int i=0; i<showPic.getImgList().size(); i++){
						path.add(showPic.getImgList().get(i).getFileUrl());
					}
					Bundle bundle = new Bundle();
					bundle.putInt("count", count);
					bundle.putStringArrayList("path", path);
					((BaseActivity)mContext).jump((Activity) mContext, PreviewPhotoActivity.class, "key", bundle, false);
				}
			});
			
			//默认图片的显示
			if(showPic.getImgList() != null && showPic.getImgList().size() > 0){
				final String filePath = showPic.getImgList().get(0).getFileUrl();
				BaseApplication.getInstance().execRunnable(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							final Bitmap defaultImg = Picasso.with(mContext).load(filePath).config(Bitmap.Config.RGB_565).get();
							((Activity)mContext).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									iv_default.setLayoutParams(new RelativeLayout.LayoutParams(ImageUtils.getWidget_Width(mContext), ImageUtils.get_Image_heigth(mContext, defaultImg)));
									iv_default.setImageBitmap(defaultImg);
								}
							});
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}});
			}
			
			//设置地点
			if(showPic.getAddress() != null && !showPic.getAddress().equals("")){
				tv_addr.setText(showPic.getAddress());
				rl_addr.setVisibility(View.VISIBLE);
			}else{
				rl_addr.setVisibility(View.GONE);
			}
			
			//给头像设置点击事件
			ll_head.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listener != null){
						listener.onClick(v, showPic);
					}
					
				}
			});
			
		}
	}
	
	private void setLike_user_name(ShowPic showPic, LinearLayout ll_like_person, TextView tv_like_person){
		if(showPic.getLike_user_name() == null || showPic.getLike_user_name().size() <= 0){
			//没有点赞的人
			ll_like_person.setVisibility(View.GONE);
		}else{
			ll_like_person.setVisibility(View.VISIBLE);
			String like = "";
			for(int i=showPic.getLike_user_name().size()-1; i>=0; i--){
				like += showPic.getLike_user_name().get(i) + "，";
				if(i > 10){
					like += "等"+showPic.getLike_user_name().size()+"人";
					break;
				}
			}
			like = like.substring(0, like.length()-1);
			like += "觉得很赞。";
			tv_like_person.setText(like);
		}
	}
	
	public interface OnClickListener{
		public void onClick(View v, ShowPic showPic);
	}
}
