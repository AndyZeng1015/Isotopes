package com.isotopes.activities;

import com.isotopes.R;
import com.isotopes.R.layout;
import com.isotopes.base.BaseActivity;
import com.isotopes.base.BaseApplication;
import com.isotopes.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

	private void initData() {
		// TODO Auto-generated method stub
		BaseApplication.getInstance().execRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SystemClock.sleep(2000);
				CommonUtils.createSDCardDir();
				jump(SplashActivity.this, MainActivity.class, true);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_splash);
	}
	
}
