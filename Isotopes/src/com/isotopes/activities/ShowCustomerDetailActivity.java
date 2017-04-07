package com.isotopes.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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

public class ShowCustomerDetailActivity extends BaseActivity {
	
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
	private ImageView iv_add_friend;
	
	private Dialog dialog;
	
	private ListShowAdapter adapter;
	private List<ShowPic> showPicList = new ArrayList<ShowPic>();
	
	private String customer_object_id = "";
	private User user;
	private List<String> customerFriendObjId;
	 
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
				ShowCustomerDetailActivity.this.finish();
			}
		});
		
		iv_add_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(user == null){
					ToastUtil.showToast(mContext, "添加失败！");
					return;
				}
				//获取好友列表
		        BmobQuery<FriendTable> query = new BmobQuery<FriendTable>();
		        query.addWhereEqualTo("user_object_id", Contast.user.getObjectId());//搜索user_object_id等于当前用户的
		        //执行查询方法
		        query.findObjects(new FindListener<FriendTable>() {
		            @Override
		            public void done(List<FriendTable> object, BmobException e) {
		                if(e==null){
		                	FriendTable friendList = object.get(0);//获取到好友
		                	
		                	if(friendList != null && friendList.getFriendList() != null){
		                        //读取登录成功后获取到的好友列表
		                		
		                		for(User customer : friendList.getFriendList()){
		                			if(customer.getObjectId().equals(customer_object_id)){
			        					//已经是好友了
			        					ToastUtil.showToast(mContext, "该用户已经是你的好友了！");
			        					return;
			        				}
		                		}
		                		//还不是好友，在这里进行添加
		                		object.get(0).getFriendList().add(user);
		                		object.get(0).update(object.get(0).getObjectId(), new UpdateListener() {
									
									@Override
									public void done(BmobException arg0) {
										// TODO Auto-generated method stub
										if(arg0==null){
											ToastUtil.showToast(mContext, "添加成功！");
								        }else{
								        	ToastUtil.showToast(mContext, "添加失败:"+arg0.getMessage());
								        }
									}
								});
		                    }
		                	
		                }else if(e.getErrorCode() == 9015){
		                	//没有表
		                	//在这里进行添加
							FriendTable friendTable = new FriendTable();
				        	friendTable.setUser_object_id(Contast.user.getObjectId());
				        	friendTable.getFriendList().add(user);
				        	
				        	friendTable.save(new SaveListener<String>() {
								
								@Override
								public void done(String arg0, BmobException arg1) {
									// TODO Auto-generated method stub
									if(arg1==null){
										ToastUtil.showToast(mContext, "添加成功！");
							        }else{
							        	ToastUtil.showToast(mContext, "添加失败:"+arg1.getMessage());
							        }
								}
							});
		                }else{
		                    ToastUtil.showToast(mContext, "添加好友失败失败："+e.getMessage());
		                }
		            }
		        });
				
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_customer_detail);
		
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
		iv_add_friend = (ImageView) findViewById(R.id.iv_add_friend);
		
		dialog = DialogUtils.createLoadingDialog(mContext, "加载中，请稍候~");
		
		lv_list.addHeaderView(headView);
		
		adapter = new ListShowAdapter(mContext, showPicList);
		lv_list.setAdapter(adapter);
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		customer_object_id = getIntent().getBundleExtra("key").getString("objectId");
		
		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(customer_object_id, new QueryListener<User>() {

			@Override
			public void done(User user, BmobException arg1) {
				// TODO Auto-generated method stub
				ShowCustomerDetailActivity.this.user = user;
				Picasso.with(mContext).load(user.getHeadimg().getFileUrl()).placeholder(R.drawable.head_default).error(R.drawable.head_default).transform(new CircleTransform()).into(iv_head);
				tv_username.setText(user.getUsername());
				tv_desc.setText(user.getDesc());
				if(user.getSex().equals("帅哥")){
					iv_sex.setImageResource(R.drawable.male);
				}else{
					iv_sex.setImageResource(R.drawable.female);
				}
				tv_addr.setText(user.getAddr());
				tv_school.setText(user.getSchool());
				tv_occ.setText(user.getOccupation());
				
				
				//通过objectId查询发布的内容
				BmobQuery<ShowPic> showPicQuery = new BmobQuery<ShowPic>();
				showPicQuery.addWhereEqualTo("create_user", customer_object_id);
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
					public void onNext(final List<ShowPic> showPic) {
						if(showPic != null){
							
							customerFriendObjId = new ArrayList<String>();
							
							//获取好友列表
					        BmobQuery<FriendTable> query = new BmobQuery<FriendTable>();
					        query.addWhereEqualTo("user_object_id",customer_object_id);//搜索user_object_id等于当前用户的
					        //执行查询方法
					        query.findObjects(new FindListener<FriendTable>() {
					            @Override
					            public void done(List<FriendTable> object, BmobException e) {
					                if(e==null){
					                	FriendTable friendList = object.get(0);//获取到好友
					                	
					                	if(friendList != null && friendList.getFriendList() != null && friendList.getFriendList().size() > 0){
					                    	for(User user : friendList.getFriendList()){
					                    		customerFriendObjId.add(user.getObjectId());
					                    	}
					                    	
					                    	List<ShowPic> showPics = new ArrayList<ShowPic>();
											for(ShowPic sp : showPic){
												//等于私密且不是自己发的
												if(sp.getPower().equals("私密")){
													
												}else if(sp.getPower().equals("好友") && customerFriendObjId.contains(Contast.user.getObjectId())){
													//如果类型是好友可见，而本人又在好友内
													showPics.add(sp);
												}else if(sp.getPower().equals("好友") && !customerFriendObjId.contains(Contast.user.getObjectId())){
													//如果类型是好友可见，而本人又不在好友内
												}else if(sp.getPower().equals("所有人")){
													showPics.add(sp);
												}
											}
											adapter.refreshData(showPics);
					                    }
					                }
					            }
					        });
							
						}
					}
				});
				
			}
		});
	}

}
