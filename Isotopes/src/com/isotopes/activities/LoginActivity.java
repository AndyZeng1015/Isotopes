package com.isotopes.activities;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.isotopes.R;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.User;
import com.isotopes.utils.Config;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.Md5Util;
import com.isotopes.utils.SharedPreferencesUtils;
import com.isotopes.utils.ToastUtil;

public class LoginActivity extends BaseActivity {
	
	private EditText et_username;
	private EditText et_password;
	private Button btn_login;
	private Button btn_register;
	
	private final static int LOGIN_SUCCESS = 1;
	
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
		//登录
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_username.getText().toString().trim().equals("")){
					ToastUtil.showToast(mContext, "用户名不能为空！");
					return;
				}
				
				if(et_password.getText().toString().trim().equals("")){
					ToastUtil.showToast(mContext, "密码不能为空！");
					return;
				}
				
				SharedPreferencesUtils.saveString(mContext, Config.USERNAME, et_username.getText().toString().trim());
				
				final Dialog dialog = DialogUtils.createLoadingDialog(mContext, "登录中，请稍候~");
				dialog.show();
				
				//登录Bmob
				User user = new User();
            	user.setUsername(et_username.getText().toString().trim());
            	user.setPassword(Md5Util.md5(et_password.getText().toString().trim()));
            	user.login(new SaveListener<BmobUser>() {

					@Override
					public void done(BmobUser arg0, BmobException arg1) {
						// TODO Auto-generated method stub
						if(arg1 == null){
							//登录成功后登录环信
							EMClient.getInstance().login(arg0.getObjectId(), arg0.getObjectId(), new EMCallBack() {
			                    
			                    @Override
			                    public void onSuccess() {
			                    	//环信登录成功
			                    	//保存用户信息
									Contast.user = BmobUser.getCurrentUser(User.class);
									
									if(Contast.user.getHeadimg() == null){
					                	ToastUtil.showToastOnThread((Activity) mContext, "请先完善个人资料");
					                	Bundle bundle = new Bundle();
					    				bundle.putString("title", "完善个人资料");
					    				bundle.putString("type", "main");//跳转类型，完成后跳转到主页面
					    				jump(LoginActivity.this, UpdatePersonalActivity.class, "key", bundle, false);
					                }else{
					                	jump(LoginActivity.this, Contast.className, true);
					                }
									dialog.dismiss();
			                    }
			                    
			                    @Override
			                    public void onProgress(int progress, String status) {
			                        
			                    }
			                    
			                    @Override
			                    public void onError(int code, String error) {
			                    	ToastUtil.showToastOnThread((Activity) mContext, "登录失败:"+error);
			                    	dialog.dismiss();
			                    }
			                });
						}else{
							ToastUtil.showToastOnThread((Activity) mContext, "登录失败:"+arg1.getMessage());
	                    	dialog.dismiss();
						}
					}

            	});
			}
		});
		//注册
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump(LoginActivity.this, RegisterActivity.class, false);
			}
		});
		
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		et_username.setText(SharedPreferencesUtils.getString(mContext, Config.USERNAME, ""));//如果保存有登录名，读取出来
	
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
	}
}
