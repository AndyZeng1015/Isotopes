package com.isotopes.activities;

import java.net.URLEncoder;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.isotopes.R;
import com.isotopes.base.BaseActivity;

public class WebViewActivity extends BaseActivity {
	
	private WebView wv_view;
	private String city;
	
	private ProgressBar pb; 
	
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
		wv_view.setWebChromeClient(new WebChromeClient(){
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				pb.setProgress(newProgress);
				if(newProgress==100){
					pb.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		
		//不允许打开浏览器
		wv_view.setWebViewClient(new WebViewClient() {
	        public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
	}

	private void initData() {
		// TODO Auto-generated method stub
		//设置WebView属性，能够执行Javascript脚本  
		wv_view.getSettings().setJavaScriptEnabled(true); 
		wv_view.getSettings().setBuiltInZoomControls(true);
		String url = "https://m.mafengwo.cn/mdd/"+city;
		wv_view.loadUrl(url);
	}

	private void initView() {
		// TODO Auto-generated method stub
		city = getIntent().getBundleExtra("keys").getString("city");
		setContentView(R.layout.activity_webview);
		wv_view = (WebView) findViewById(R.id.wv_view);
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (wv_view.canGoBack()) {  
        	wv_view.goBack(); //goBack()表示返回WebView的上一页面  
        }else{
        	super.onBackPressed();
        }
	}
}
