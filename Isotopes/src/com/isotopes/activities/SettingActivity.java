package com.isotopes.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.isotopes.R;
import com.isotopes.base.BaseActivity;
import com.isotopes.base.BaseApplication;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DialogUtils;

public class SettingActivity extends BaseActivity {
	
	private RelativeLayout rl_set_password;
	private RelativeLayout rl_logout;
	
	private Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		//修改密码
		rl_set_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump(SettingActivity.this, ChangePasswordActivity.class, false);
			}
		});
		
		
		//注销登录
		rl_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("提示");
				builder.setMessage("确认注销登录吗？");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog2, int which) {
						// TODO Auto-generated method stub
						//注销登录
						dialog.show();
						EMClient.getInstance().logout(true, new EMCallBack() {
				            
						    @Override
						    public void onSuccess() {
						        // TODO Auto-generated method stub
						    	Contast.user = null;//清空用户信息
						    	Contast.className = MainActivity.class;//设置登录成功后跳转的页面
								BaseApplication.getInstance().exit();
								jump(SettingActivity.this, LoginActivity.class, true);
								dialog.dismiss();
						    }
						    
						    @Override
						    public void onProgress(int progress, String status) {
						        // TODO Auto-generated method stub
						        
						    }
						    
						    @Override
						    public void onError(int code, String message) {
						        // TODO Auto-generated method stub
						    	dialog.dismiss();
						    }
						});
					}
				});
				builder.show();
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting);
		rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
		rl_set_password = (RelativeLayout) findViewById(R.id.rl_set_password);
		rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
		
		dialog = DialogUtils.createLoadingDialog(mContext, "加载中，请稍候~");
	}
}
