package com.isotopes.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.lasque.tusdk.core.task.ImageViewTaskWare.LoadType;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.isotopes.R;
import com.isotopes.adapter.SelectAddrAdapter;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.AddressInfo;
import com.isotopes.utils.ToastUtil;

public class SelectAddrMapActivity extends BaseActivity {
	
	private ImageView iv_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private ListView lv_addr_list;
	private ProgressBar pb_bar;
	private TextView tv_nodata;
	private ImageView iv_location_now;
	
	// 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    
    //搜索模块
  	private GeoCoder mPoiSearch;
    
    private boolean isFirstLoc = true; // 是否首次定位
    private LatLng nowMyLocation;//我当前的位置
    private LatLng nowSelectLocation;//当前选择的位置
    
    private List<AddressInfo> addressInfos = new ArrayList<AddressInfo>();
	private SelectAddrAdapter adapter;
    

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
				SelectAddrMapActivity.this.finish();
			}
		});
		
		//到当前位置
		iv_location_now.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(nowMyLocation != null){
					centerToLocation(nowMyLocation);
				}
			}
		});
		
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			
			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
				// 地图状态改变结束
			    //target地图操作的中心点。
			     LatLng target = mBaiduMap.getMapStatus().target;
			     searchDataList(target);
			}
			
			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lv_addr_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//将选择的地址返回
				AddressInfo addressInfo = addressInfos.get(arg2);
				Intent data = new Intent();
				data.putExtra("addressInfo", addressInfo);
				setResult(200, data);
				SelectAddrMapActivity.this.finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		mCurrentMode = LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        
        //设置缩放比例
  		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18f);
  		mBaiduMap.setMapStatus(msu);
  		
  		mPoiSearch = GeoCoder.newInstance();  
  		
  		adapter = new SelectAddrAdapter(SelectAddrMapActivity.this, addressInfos);
  		lv_addr_list.setAdapter(adapter);
	}

	
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_select_map);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		lv_addr_list = (ListView) findViewById(R.id.lv_addr_list);
		pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
		iv_location_now = (ImageView) findViewById(R.id.iv_location_now);
	}
	
	/**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            //让地图可以滑动，不会受实时定位的影响
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                nowMyLocation = ll;//记录当前的位置
                nowSelectLocation = ll;
                centerToLocation(ll);
                searchDataList(ll);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
    }
    
    /**
	 * 将指定经纬度定位到屏幕中心点
	 */
	private void centerToLocation(LatLng location){
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(location);
		mBaiduMap.animateMapStatus(msu);
	}

	private void searchDataList(LatLng latLng){
		//开始搜索
		mPoiSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		pb_bar.setVisibility(View.VISIBLE);
		
		//搜索监听
		mPoiSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// TODO Auto-generated method stub
				addressInfos.clear();
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		              ToastUtil.showToast(SelectAddrMapActivity.this, "抱歉，未能找到结果");
		              tv_nodata.setVisibility(View.VISIBLE);
		              pb_bar.setVisibility(View.GONE);
		              return;
		        }
			     //判断该地址附近是否有POI（Point of Interest,即兴趣点）  
				if (null != result.getPoiList()) { 
					for(PoiInfo info : result.getPoiList()){
						AddressInfo addressInfo = new AddressInfo();
						addressInfo.setName(info.name);
						addressInfo.setAddress(info.address);
						addressInfo.setLatLng(info.location);
						addressInfos.add(addressInfo);
					}
				}
				
				if(addressInfos.size() > 0){
					pb_bar.setVisibility(View.GONE);
					tv_nodata.setVisibility(View.GONE);
				}else{
					tv_nodata.setVisibility(View.VISIBLE);
					pb_bar.setVisibility(View.GONE);
				}
				
				adapter.refreshData(addressInfos);
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub
 			}
		});
	}

}


