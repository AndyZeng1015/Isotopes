package com.isotopes.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment.AddFriendListener;
import com.hyphenate.easeui.ui.EaseContactListFragment.OnItemLongClickListener;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment.EaseContactListItemClickListener;
import com.hyphenate.easeui.ui.EaseConversationListFragment.EaseConversationListItemClickListener;
import com.isotopes.R;
import com.isotopes.base.BaseApplication;
import com.isotopes.beans.FriendTable;
import com.isotopes.beans.User;
import com.isotopes.utils.Contast;
import com.isotopes.utils.ToastUtil;

public class FriendActivity extends EaseBaseActivity {
	
	 private TextView unreadLabel;
	    private Button[] mTabs;
	    private EaseConversationListFragment conversationListFragment;
	    private EaseContactListFragment contactListFragment;
	    private Fragment[] fragments;
	    private int index;
	    private int currentTabIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseApplication.getInstance().addActivity(this);
		initView();
		initData();
		initListener();
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		//添加好友
		contactListFragment.setAddFriendListener(new AddFriendListener() {
			
			@Override
			public void addFriendClick() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
				builder.setCancelable(false);
				builder.setTitle("添加好友");
				final EditText et_username = new EditText(FriendActivity.this);
				builder.setView(et_username);
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 //TODO Auto-generated method stub
						//参数为要添加的好友的username和添加理由
						if(et_username.getText().toString().trim().equals("")){
							ToastUtil.showToast(FriendActivity.this, "请输入好友昵称！");
						}else{
							//根据输入的用户名查找用户，再根据objectId添加好友
							BmobQuery<User> query = new BmobQuery<User>();
							query.addWhereEqualTo("username", et_username.getText().toString().trim());
							query.findObjects(new FindListener<User>() {
							    @Override
							    public void done(final List<User> object,BmobException e) {
							        if(e==null){
							            //查询成功
							        	BmobQuery<FriendTable> friendTable = new BmobQuery<FriendTable>();
							        	friendTable.addWhereEqualTo("user_object_id", Contast.user.getObjectId());
							        	friendTable.findObjects(new FindListener<FriendTable>() {

											@Override
											public void done(List<FriendTable> arg0,BmobException arg1) {
												// TODO Auto-generated method stub
												if(arg1 == null){
													//记录存在
													arg0.get(0).getFriendList().add(object.get(0));
													arg0.get(0).update(arg0.get(0).getObjectId(), new UpdateListener() {
														
														@Override
														public void done(BmobException arg0) {
															// TODO Auto-generated method stub
															if(arg0==null){
																ToastUtil.showToast(FriendActivity.this, "添加成功！");
																//重新加载列表
													        	getFriendList();
													        }else{
													        	ToastUtil.showToast(FriendActivity.this, "添加失败:"+arg0.getMessage());
													        }
														}
													});
													
												}else{
													//记录不存在
													FriendTable friendTable = new FriendTable();
										        	friendTable.setUser_object_id(Contast.user.getObjectId());
										        	friendTable.getFriendList().add(object.get(0));
										        	
										        	friendTable.save(new SaveListener<String>() {
														
														@Override
														public void done(String arg0, BmobException arg1) {
															// TODO Auto-generated method stub
															if(arg1==null){
																ToastUtil.showToast(FriendActivity.this, "添加成功！");
																//重新加载列表
													        	getFriendList();
													        }else{
													        	ToastUtil.showToast(FriendActivity.this, "添加失败:"+arg1.getMessage());
													        }
														}
													});
												}
											}
										});
							        }else{
							        	ToastUtil.showToast(FriendActivity.this, "该用户不存在！");
							        }
							    }
							});
						}
					}
				});
				builder.show();
			}
		});
		
		contactListFragment.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public void itemLongClick(final EaseUser user) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
				builder.setTitle("提示");
				builder.setMessage("你确定要删除好友"+user.getNickname()+"吗？");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
			        	BmobQuery<FriendTable> friendTable = new BmobQuery<FriendTable>();
			        	friendTable.addWhereEqualTo("user_object_id", Contast.user.getObjectId());
			        	friendTable.findObjects(new FindListener<FriendTable>() {

							@Override
							public void done(List<FriendTable> arg0,BmobException arg1) {
								// TODO Auto-generated method stub
								if(arg1 == null){
									//记录存在
									for(User auser : arg0.get(0).getFriendList()){
										if(auser.getObjectId().equals(user.getUsername())){
											arg0.get(0).getFriendList().remove(auser);
											break;
										}
									}
									arg0.get(0).update(arg0.get(0).getObjectId(), new UpdateListener() {
										
										@Override
										public void done(BmobException arg0) {
											// TODO Auto-generated method stub
											if(arg0==null){
												ToastUtil.showToast(FriendActivity.this, "删除成功！");
												//重新加载列表
									        	getFriendList();
									        }else{
									        	ToastUtil.showToast(FriendActivity.this, "删除失败:"+arg0.getMessage());
									        }
										}
									});
								}else{
									ToastUtil.showToast(FriendActivity.this, "删除失败:"+arg1.getMessage());
								}
							}
											
				});
			}
		});
				builder.show();
				
			}
		
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		BaseApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}

	private void initData() {
		// TODO Auto-generated method stub
		conversationListFragment = new EaseConversationListFragment();
        contactListFragment = new EaseContactListFragment();
        
        getFriendList();
        
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            
            @Override
            public void onListItemClicked(EMConversation conversation, String nickName) {
            	Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
            	intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
            	intent.putExtra("nickName", nickName);
            	conversation.getExtField();
                startActivity(intent);
            }
        });
        contactListFragment.setContactListItemClickListener(new EaseContactListItemClickListener() {
            
            @Override
            public void onListItemClicked(EaseUser user) {
            	Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
            	intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
            	intent.putExtra("nickName", user.getNickname());
                startActivity(intent);
            }
        });
        fragments = new Fragment[] { conversationListFragment, contactListFragment};
        // add and show first fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(conversationListFragment)
                .commit();
	}

	//获取好友列表
	private void getFriendList() {
		// TODO Auto-generated method stub
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
                    	Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
                        //读取登录成功后获取到的好友列表
                		for(int i = 0; i < friendList.getFriendList().size(); i++){
                            EaseUser user = new EaseUser(friendList.getFriendList().get(i).getObjectId());
                            user.setNickname(friendList.getFriendList().get(i).getUsername());
                            user.setAvatar(friendList.getFriendList().get(i).getHeadimg().getFileUrl());
                            contacts.put(friendList.getFriendList().get(i).getObjectId(), user);
                        }
                		contactListFragment.setContactsMap(contacts);
                		contactListFragment.refresh(contacts);
                		
                		conversationListFragment.setContactsMap(contacts);
                		conversationListFragment.refresh();
                    }
                	
                }else if(e.getErrorCode() == 9015){
                	Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
                	contactListFragment.setContactsMap(contacts);
                	conversationListFragment.setContactsMap(contacts);
                }else{
                    ToastUtil.showToast(FriendActivity.this, "加载好友列表失败！");
                    FriendActivity.this.finish();
                }
            }
        });
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_friend);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        mTabs = new Button[3];
        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
        mTabs[1] = (Button) findViewById(R.id.btn_address_list);
        mTabs[0].setSelected(true);
	}
	
	/**
     * onTabClicked
     * 
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
        case R.id.btn_conversation:
            index = 0;
            break;
        case R.id.btn_address_list:
            index = 1;
            break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab as selected.
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }
	
}
