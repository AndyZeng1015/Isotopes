package com.isotopes.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.isotopes.R;
import com.isotopes.base.BaseActivity;
import com.isotopes.utils.ToastUtil;

public class HotSpotsActivity extends BaseActivity implements OnClickListener{
	
	private EditText et_data;
	private Button btn_search;
	private ImageView iv_beijing;
	private ImageView iv_shanghai;
	private ImageView iv_chengdu;
	private ImageView iv_guangzhou;
	private ImageView iv_shenzhen;
	private ImageView iv_tianjing;
	
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
		btn_search.setOnClickListener(this);
		iv_beijing.setOnClickListener(this);
		iv_shanghai.setOnClickListener(this);
		iv_chengdu.setOnClickListener(this);
		iv_guangzhou.setOnClickListener(this);
		iv_shenzhen.setOnClickListener(this);
		iv_tianjing.setOnClickListener(this);
		
		iv_back.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_hot_spots);
		et_data = (EditText) findViewById(R.id.et_data);
		btn_search = (Button) findViewById(R.id.btn_search);
		iv_beijing = (ImageView) findViewById(R.id.iv_beijing);
		iv_shanghai = (ImageView) findViewById(R.id.iv_shanghai);
		iv_chengdu = (ImageView) findViewById(R.id.iv_chengdu);
		iv_guangzhou = (ImageView) findViewById(R.id.iv_guangzhou);
		iv_shenzhen = (ImageView) findViewById(R.id.iv_shenzheng);
		iv_tianjing = (ImageView) findViewById(R.id.iv_tianjing);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search:
			String search_data = et_data.getText().toString().trim();
			if(search_data.equals("")){
				ToastUtil.showToast(mContext, "请输入搜索内容！");
			}else{
				Bundle bundle1 = new Bundle();
				bundle1.putString("select_data", search_data);
				jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle1, false);
			}
			break;
		case R.id.iv_beijing:
			//跳转到天安门
			Bundle bundle2 = new Bundle();
			bundle2.putString("select_data", "天安门");
			jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle2, false);
			break;
		case R.id.iv_shanghai:
			//东方明珠
			Bundle bundle3 = new Bundle();
			bundle3.putString("select_data", "东方明珠");
			jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle3, false);
			break;
		case R.id.iv_chengdu:
			Bundle bundle4 = new Bundle();
			bundle4.putString("select_data", "锦里");
			jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle4, false);
			break;
		case R.id.iv_guangzhou:
			Bundle bundle5 = new Bundle();
			bundle5.putString("select_data", "广州塔");
			jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle5, false);
			break;
		case R.id.iv_shenzheng:
			Bundle bundle6 = new Bundle();
			bundle6.putString("select_data", "世界之窗");
			jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle6, false);
			break;
		case R.id.iv_tianjing:
			Bundle bundle7 = new Bundle();
			bundle7.putString("select_data", "天津之眼");
			jump(HotSpotsActivity.this, HotSpotsListActivity.class, "key", bundle7, false);
			break;
		case R.id.iv_back:
			HotSpotsActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
