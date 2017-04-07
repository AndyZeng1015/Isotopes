package com.isotopes.activities;

import rx.Observable;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.isotopes.R;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.User;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.Md5Util;
import com.isotopes.utils.ToastUtil;

public class RegisterActivity extends BaseActivity{
	
	private EditText et_username;
	private EditText et_phone;
	private EditText et_password;
	private EditText et_re_password;
	private Button btn_register;
	
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
			}
		});
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_username.getText().toString().trim().equals("") || et_phone.getText().toString().trim().equals("") || et_password.getText().toString().trim().equals("") || et_re_password.getText().toString().trim().equals("")){
					ToastUtil.showToast(mContext, "请将数据填写完整！");
					return;
				}
				if(!et_password.getText().toString().trim().equals(et_re_password.getText().toString().trim())){
					ToastUtil.showToast(mContext, "两次输入密码不一致！");
					return;
				}
				
				//注册
				final Dialog dialog = DialogUtils.createLoadingDialog(mContext, "正在注册，请稍候~");
				dialog.show();
				mUser = new User();
				mUser.setUsername(et_username.getText().toString().trim());
				mUser.setMobilePhoneNumber(et_phone.getText().toString().trim());
				mUser.setPassword(Md5Util.md5(et_password.getText().toString().trim()));
				mUser.signUp(new SaveListener<User>() {

					@Override
					public void done(final User arg0, BmobException e2) {
						// TODO Auto-generated method stub
						if(e2==null){
				            //注册Bmob成功 还要注册环信，使用Bmob的objectId来注册环信
							//同步方法
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {  
										//环信的用户名和密码同为ObjectId
									    EMClient.getInstance().createAccount(arg0.getObjectId(), arg0.getObjectId());//同步方法  
									    ToastUtil.showToastOnThread((Activity) mContext, "注册成功！");  
									    RegisterActivity.this.finish();
									} catch (HyphenateException e) {  
									    e.printStackTrace();  
									    //此处我们根据错误类型可以判断是什么原因引起的注册失败,我们只列出常见的原因1.网络连接失败2.用户名已注册  
									    switch (e.getErrorCode()) {  
									        case EMError.SERVER_BUSY:  
									        case EMError.SERVER_UNKNOWN_ERROR:  
									        case EMError.SERVER_NOT_REACHABLE:  
									        case EMError.SERVER_TIMEOUT:  
									        case EMError.NETWORK_ERROR://网络错误
									        	ToastUtil.showToastOnThread((Activity) mContext, "网络有问题,请稍后再试");  
									            break;  
									        case EMError.USER_ALREADY_EXIST://用户名已存在  
									        	ToastUtil.showToastOnThread((Activity) mContext, "用户名已存在,请尝试登录");  
									            break;  
									        default: 
									        	ToastUtil.showToastOnThread((Activity) mContext, "注册失败:"+e.getMessage());
									            break;  
									    }  
									}  
								}
							}).start();
				        }else{
				        	ToastUtil.showToast(mContext, e2.getMessage());
				        }
						dialog.dismiss();
					}
				});
				
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_register);
		et_username = (EditText) findViewById(R.id.username);
		et_phone = (EditText) findViewById(R.id.phone);
		et_password = (EditText) findViewById(R.id.password);
		et_re_password = (EditText) findViewById(R.id.confirm_password);
		btn_register = (Button) findViewById(R.id.btn_register);
	}

}
