package com.isotopes.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

import org.xutils.x;

import com.isotopes.R;
import com.isotopes.beans.PictureInfo;
import com.isotopes.utils.ImageUtils;

public class SelectPicAdapter extends BaseAdapter {
    private List<PictureInfo> minfo;
    private int Pwidth;//屏幕宽度

    public SelectPicAdapter(List<PictureInfo> info,int width){
        this.minfo=info;
        this.Pwidth=width;
    }
    
    //刷新数据
    public void refreshData(List<PictureInfo> minfo){
    	this.minfo = minfo;
    	this.notifyDataSetChanged();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getCount() {
        return minfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv=new ImageView(parent.getContext());
        x.image().bind(iv,minfo.get(position).getPicPath(),ImageUtils.getSmallOptions(false));
        iv.setBackgroundResource(R.drawable.boder_task_tv);
        iv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,(Pwidth-dip2px(parent.getContext(),29))/4));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }
}
