package com.isotopes.adapter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.android.bbalbs.common.a.c;
import com.isotopes.R;
import com.isotopes.beans.HotSpotsBean;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotSpotsAdapter extends BaseAdapter {

	private List<HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> hotSpotsList;
	private Context mContext;
	
	public void refreshData(List<HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> contentList){
		this.hotSpotsList = contentList;
		this.notifyDataSetChanged();
	}
	
	public HotSpotsAdapter(Context context, List<HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> hotSpotsBeans){
		this.mContext = context;
		this.hotSpotsList = hotSpotsBeans;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(hotSpotsList != null){
			return hotSpotsList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(hotSpotsList != null){
			return hotSpotsList.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HotSpotsHolder holder;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.item_hot_spots, null);
			holder = new HotSpotsHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (HotSpotsHolder) convertView.getTag();
		}
		holder.setData(hotSpotsList.get(position));
		return convertView;
	}
	
	class HotSpotsHolder{
		
		private View mView;
		private ImageView iv_img;
		private TextView tv_name;
		private TextView tv_address;
		
		public HotSpotsHolder(View view){
			mView = view;
			iv_img = (ImageView) view.findViewById(R.id.iv_img);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_address = (TextView) view.findViewById(R.id.tv_address);
		}
		
		public void setData(HotSpotsBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean contentlistBean){
			if(contentlistBean.getPicList() == null || contentlistBean.getPicList().size() <= 0){
				Picasso.with(mContext).load(R.drawable.no_pic).placeholder(R.drawable.pic_loading).error(R.drawable.error).into(iv_img);
			}else{
				Picasso.with(mContext).load(contentlistBean.getPicList().get(0).getPicUrlSmall()).placeholder(R.drawable.pic_loading).error(R.drawable.error).into(iv_img);
			}
			tv_name.setText(contentlistBean.getName());
			tv_address.setText(contentlistBean.getAddress());
		}
	}

}
