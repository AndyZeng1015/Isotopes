package com.isotopes.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.hyphenate.easeui.domain.EaseUser;
import com.isotopes.R;
import com.isotopes.adapter.ListShowAdapter;
import com.isotopes.appwidget.CircleTransform;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.FriendTable;
import com.isotopes.beans.ShowPic;
import com.isotopes.beans.User;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.ToastUtil;
import com.squareup.picasso.Picasso;

public class PersonalActivity extends BaseActivity {
	
	private ListView lv_list;
	private ImageView iv_back;
	
	private View headView;
	private ImageView iv_head;
	private TextView tv_username;
	private TextView tv_desc;
	private ImageView iv_sex;
	private TextView tv_addr;
	private TextView tv_school;
	private TextView tv_occ;
	private TextView tv_set;
	
	private final static int CHANGE_PERSONAL_DATA = 1;
	
	private Dialog dialog;
	
	private ListShowAdapter adapter;
	private List<ShowPic> showPicList = new ArrayList<ShowPic>();
	
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
				PersonalActivity.this.finish();
			}
		});
		
		headView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("title", "修改个人资料");
				jump(PersonalActivity.this, UpdatePersonalActivity.class, "key", bundle, CHANGE_PERSONAL_DATA);
			}
		});
		
		//设置
		tv_set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump(PersonalActivity.this, SettingActivity.class, false);
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
					jump(PersonalActivity.this, ShowCustomerDetailActivity.class, "key", bundle, false);
				}
			}
		});
		
	}
	
	private List<String> friendNameList;
	
	private void initData() {
		// TODO Auto-generated method stub
		Picasso.with(mContext).load(Contast.user.getHeadimg().getFileUrl()).placeholder(R.drawable.head_default).error(R.drawable.head_default).transform(new CircleTransform()).into(iv_head);
		tv_username.setText(Contast.user.getUsername());
		tv_desc.setText(Contast.user.getDesc());
		if(Contast.user.getSex().equals("帅哥")){
			iv_sex.setImageResource(R.drawable.male);
		}else{
			iv_sex.setImageResource(R.drawable.female);
		}
		tv_addr.setText(Contast.user.getAddr());
		tv_school.setText(Contast.user.getSchool());
		tv_occ.setText(Contast.user.getOccupation());
		
		//查询到好友列表
		friendNameList = new ArrayList<String>();//名字
		//获取好友列表
        BmobQuery<FriendTable> query = new BmobQuery<FriendTable>();
        query.addWhereEqualTo("user_object_id", Contast.user.getObjectId());//搜索user_object_id等于当前用户的
        //执行查询方法
        query.findObjects(new FindListener<FriendTable>() {
            @Override
            public void done(List<FriendTable> object, BmobException e) {
                if(e==null){
                	FriendTable friendList = object.get(0);//获取到好友
                	
                	if(friendList != null && friendList.getFriendList() != null && friendList.getFriendList().size() > 0){
                    	for(User user : friendList.getFriendList()){
                    		friendNameList.add(user.getUsername());
                    	}
                    }
                	friendNameList.add(Contast.user.getUsername());
                	getShowData(friendNameList);
                }else if(e.getErrorCode() == 9015){
                	friendNameList.add(Contast.user.getUsername());
                	getShowData(friendNameList);
                }else{
                    ToastUtil.showToast(mContext, "获取好友发布失败！");
                }
            }
        });

	}
	
	private void getShowData(List<String> friendList){
		//通过名字获取ObjectId
		BmobQuery<User> userQuery = new BmobQuery<User>();
		userQuery.addWhereContainedIn("username", friendList);
		//执行查询方法
		userQuery.findObjects(new FindListener<User>() {
			@Override
			public void done(List<User> arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				List<String> objectIds = new ArrayList<String>();
				for(User user : arg0){
					objectIds.add(user.getObjectId());
				}
				
				//通过objectId查询发布的内容
				BmobQuery<ShowPic> showPicQuery = new BmobQuery<ShowPic>();
				showPicQuery.addWhereContainedIn("create_user", objectIds);
				showPicQuery.order("-createdAt");
				showPicQuery.findObjectsObservable(ShowPic.class)
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
							List<ShowPic> showPics = new ArrayList<ShowPic>();
							for(ShowPic sp : showPic){
								//等于私密且不是自己发的
								if(sp.getPower().equals("私密") && !sp.getCreate_user().getUsername().equals(Contast.user.getUsername())){
									
								}else{
									showPics.add(sp);
								}
							}
							adapter.refreshData(showPics);
						}
					}
				});
				
			}
		});
	}

	private void initView(){
		setContentView(R.layout.activity_personal);
		lv_list = (ListView) findViewById(R.id.lv_list);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		headView = View.inflate(mContext, R.layout.item_personal_head, null);
		iv_head = (ImageView) headView.findViewById(R.id.iv_head);
		tv_username = (TextView) headView.findViewById(R.id.tv_username);
		tv_desc = (TextView) headView.findViewById(R.id.tv_desc);
		iv_sex = (ImageView) headView.findViewById(R.id.iv_sex);
		tv_addr = (TextView) headView.findViewById(R.id.tv_addr);
		tv_school = (TextView) headView.findViewById(R.id.tv_school);
		tv_occ = (TextView) headView.findViewById(R.id.tv_occ);
		tv_set = (TextView) findViewById(R.id.tv_set);
		
		dialog = DialogUtils.createLoadingDialog(mContext, "加载中，请稍候~");
		
		lv_list.addHeaderView(headView);
		
		adapter = new ListShowAdapter(mContext, showPicList);
		lv_list.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == CHANGE_PERSONAL_DATA){
			initData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
