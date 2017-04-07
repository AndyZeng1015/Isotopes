package com.isotopes.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isotopes.R;
import com.isotopes.adapter.HotSpotsAdapter;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.HotSpotsBean;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.ToastUtil;

public class HotSpotsListActivity extends BaseActivity {
	
	private ImageView iv_back;
	private ListView lv_list;
	
	private String search_data = "";
	
	private final static String URL = "http://route.showapi.com/268-1";
	private static int curr_index = 1;//当前的页数
	private static int total_index = 1;//总页数
	
	private Dialog dialog;
	
	private HotSpotsAdapter adapter;
	private HotSpotsBean hotSpotsBean;
	private List<HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> contentlistBeans = new ArrayList<HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean>();
	
	private Gson gson;
	private RequestParams params;
	
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
				HotSpotsListActivity.this.finish();
			}
		});
		
		lv_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
		            // 当不滚动时
		            case OnScrollListener.SCROLL_STATE_IDLE:
		                // 判断滚动到底部
		                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
		                    if((curr_index + 1) > total_index){
		                    	ToastUtil.showToast(mContext, "没有更多数据了！");
		                    }else{
		                    	curr_index = curr_index + 1;
		                    	initData();
		                    }
		              }
		              break;
					}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putParcelable("content", contentlistBeans.get(arg2));
				jump(HotSpotsListActivity.this, HotSpotsDetailActivity.class, "key", bundle, false);
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		dialog.show();
		//加载数据
		params = new RequestParams(URL);
		params.addQueryStringParameter("keyword", search_data);
		params.addQueryStringParameter("page", curr_index+"");
		params.addQueryStringParameter("showapi_appid", "34536");
		params.addQueryStringParameter("showapi_sign", "ea87ba1944ea43bdbce94ee1e82bd360");
		x.http().get(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				arg0.printStackTrace();
				if(arg0 != null){
					ToastUtil.showToast(mContext, arg0.getMessage());
				}
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				Log.e("ZYN", arg0);
				hotSpotsBean = gson.fromJson(arg0, HotSpotsBean.class);
				if(hotSpotsBean.getShowapi_res_code() != 0){
					ToastUtil.showToast(mContext, hotSpotsBean.getShowapi_res_error());
				}else{
					if(hotSpotsBean.getShowapi_res_body().getPagebean().getAllNum() <= 0){
						ToastUtil.showToast(mContext, "没有找到该景点！");
					}else{
						total_index = hotSpotsBean.getShowapi_res_body().getPagebean().getAllPages();//设置总页数
						contentlistBeans.addAll(hotSpotsBean.getShowapi_res_body().getPagebean().getContentlist());
						adapter.refreshData(contentlistBeans);
					}
				}
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_hot_spots_list);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_list = (ListView) findViewById(R.id.lv_list);
		search_data = getIntent().getBundleExtra("key").getString("select_data");
		gson = new Gson();
		
		dialog = DialogUtils.createLoadingDialog(mContext, "加载中，请稍候~");
		adapter = new HotSpotsAdapter(mContext, null);
		lv_list.setAdapter(adapter);
		
	}
}
