package com.isotopes.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.isotopes.R;
import com.isotopes.base.BaseActivity;
import com.isotopes.base.BaseApplication;
import com.isotopes.utils.Contast;
import com.isotopes.utils.Md5Util;
import com.isotopes.utils.ToastUtil;

public class ChangePasswordActivity extends BaseActivity {
	
	private EditText et_old_password;
	private EditText et_new_password;
	private EditText et_save_password;
	private Button btn_save;
	private ImageView iv_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_new_password.getText().toString().trim().equals("") || et_save_password.getText().toString().trim().equals("") || et_old_password.getText().toString().trim().equals("")){
					//数据不完整
					ToastUtil.showToast(mContext, "请将数据填写完整！");
				}else{
					if(!et_new_password.getText().toString().trim().equals(et_save_password.getText().toString().trim())){
						//两次输入密码不一致
						ToastUtil.showToast(mContext, "两次输入密码不一致！");
					}else{
						BmobUser.updateCurrentUserPassword(Md5Util.md5(et_old_password.getText().toString().trim()), Md5Util.md5(et_new_password.getText().toString().trim()), new UpdateListener() {

						    @Override
						    public void done(BmobException e) {
						        if(e==null){
						            ToastUtil.showToast(mContext, "密码修改成功，请重新登录！");
						            Contast.user = null;//清空用户信息
							    	Contast.className = MainActivity.class;//设置登录成功后跳转的页面
									BaseApplication.getInstance().exit();
									jump(ChangePasswordActivity.this, LoginActivity.class, true);
						        }else{
						            ToastUtil.showToast(mContext, "密码修改失败:" + e.getMessage());
						        }
						    }

						});
					}
				}
			}
		});
		
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangePasswordActivity.this.finish();
			}
		});
	}

	private void initView(){
		setContentView(R.layout.activity_change_password);
		et_old_password = (EditText) findViewById(R.id.et_old_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_save_password = (EditText) findViewById(R.id.et_save_password);
		btn_save = (Button) findViewById(R.id.btn_save);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}
}
