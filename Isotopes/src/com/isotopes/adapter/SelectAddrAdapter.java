package com.isotopes.adapter;

import java.util.List;

import com.isotopes.R;
import com.isotopes.beans.AddressInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectAddrAdapter extends BaseAdapter {

	private List<AddressInfo> addressInfos;
	private Context mContext;
	
	public SelectAddrAdapter(Context context, List<AddressInfo> addressInfos){
		this.addressInfos = addressInfos;
		this.mContext = context;
	}
	
	public void refreshData(List<AddressInfo> addressInfos){
		this.addressInfos  = addressInfos;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (addressInfos != null) {
			return addressInfos.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(addressInfos != null){
			return addressInfos.get(arg0);
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
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.item_addr_select, null);
		}
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
		
		tv_name.setText(addressInfos.get(position).getName());
		tv_address.setText(addressInfos.get(position).getAddress());
		
		return convertView;
	}

}
