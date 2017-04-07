package com.isotopes.activities;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.geev2.TuSdkGeeV2;
import org.lasque.tusdk.geev2.impl.components.TuRichEditComponent;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import com.baidu.mapapi.model.LatLng;
import com.isotopes.R;
import com.isotopes.adapter.SelectPicAdapter;
import com.isotopes.appwidget.MyGridView;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.AddressInfo;
import com.isotopes.beans.PictureInfo;
import com.isotopes.beans.ShowPic;
import com.isotopes.utils.Contast;
import com.isotopes.utils.DialogUtils;
import com.isotopes.utils.ToastUtil;

public class ReleaseActivity extends BaseActivity implements TuSdkComponentDelegate {
	
	private static final int REQUEST_GET_ADDRESS = 0;
	
    private List<PictureInfo> mlist = new ArrayList<PictureInfo>();
    private SelectPicAdapter adapter;
    
    private MyGridView gv_pic_list;
    private ImageView iv_back;
    private EditText et_title;
    private TextView tv_send;
    private RelativeLayout rl_addr;
    private Spinner sp_power;
    private TextView tv_addr;
    private LatLng selectLatLng;//选择的坐标
    
    private Dialog dialog;
    
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
		gv_pic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击的是图片
            	if (mlist.get(position).isSelect()) {
                    return;
                }
            	//点击的是添加图片
                if (mlist.size() - 1 == position) {
                	//showChoosePicDialog();
                	selectPic();
                } 
            }
        });
		
		//返回
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReleaseActivity.this.finish();
			}
		});
		
		//谁可以看见
		rl_addr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jump(ReleaseActivity.this, SelectAddrMapActivity.class, REQUEST_GET_ADDRESS);
			}
		});
		
		
		//发布
		tv_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//检测数据 是否填写完整
				
				if(mlist == null || mlist.size() <= 0){
					ToastUtil.showToast(mContext, "请选择图片！");
					return;
				}
				
				dialog.show();
				
				String[] filePaths = new String[mlist.size()-1];//-1是去掉添加图片的按钮
				for(int i=0; i<mlist.size()-1; i++){
					filePaths[i] = mlist.get(i).getPicPath();
				}
				
				//批量上传
				BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
					
					@Override
					public void onSuccess(List<BmobFile> imgList, List<String> arg1) {
						// TODO Auto-generated method stub
						
						if(imgList.size() == mlist.size()-1){//-1是去掉添加图片的按钮
							ShowPic showPic = new ShowPic();
							showPic.setImgList(imgList);//图片列表
							showPic.setTitle(et_title.getText().toString().trim()+"");//标题
							showPic.setAddress(tv_addr.getText().toString().trim()+"");//坐标名称
							if(selectLatLng != null){
								showPic.setPoint(new BmobGeoPoint(selectLatLng.longitude, selectLatLng.latitude));//坐标点
							}
							showPic.setCreate_user(Contast.user);//谁发布的
							showPic.setPower(sp_power.getSelectedItem().toString());//谁可以看
							
							showPic.save(new SaveListener<String>() {

							    @Override
							    public void done(String objectId, BmobException e) {
							        if(e==null){
							        	ToastUtil.showToast(ReleaseActivity.this, "发布成功！");
							        	dialog.dismiss();
							        	ReleaseActivity.this.finish();
							        }else{
							            ToastUtil.showToast(ReleaseActivity.this, "发布失败:"+e.getMessage());
							            dialog.dismiss();
							        }
							    }
							});
						}else{
							Log.e("ZYN", "size:"+imgList.size());
						}
					}
					
					@Override
					public void onProgress(int arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ToastUtil.showToast(ReleaseActivity.this, "发布失败:"+arg1);
						dialog.dismiss();
					}
				});
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_release);
		gv_pic_list = (MyGridView) findViewById(R.id.gv_pic_list);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_send = (TextView) findViewById(R.id.tv_send);
		rl_addr = (RelativeLayout) findViewById(R.id.rl_addr);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		et_title = (EditText) findViewById(R.id.et_title);
		sp_power = (Spinner) findViewById(R.id.sp_power);
	}
	
	private void initData(){
		
		dialog = DialogUtils.createLoadingDialog(mContext, "发布中~");
		
		WindowManager wm1 = this.getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();
		mlist.add(new PictureInfo(false, "assets://add_pic.png"));
        adapter = new SelectPicAdapter(mlist, width);
        gv_pic_list.setAdapter(adapter);
        
        // 异步方式初始化滤镜管理器
        // 需要等待滤镜管理器初始化完成，才能使用所有功能
        //TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
        TuSdk.checkFilterManager(mFilterManagerDelegate);
	}
	
	private FilterManagerDelegate mFilterManagerDelegate = new FilterManagerDelegate()
	{
	    @Override
	    public void onFilterManagerInited(FilterManager manager)
	    {
	        //TuSdk.messageHub().showSuccess(ReleaseActivity.this, R.string.lsq_inited);
	    }
	};
	
	private void selectPic(){
		TuRichEditComponent comp =	TuSdkGeeV2.richEditCommponent(this, this);

		// 组件选项配置
		// 设置是否启用图片编辑 默认 true
		// comp.componentOption().setEnableEditMultiple(true);
		
		// 相机组件配置
		// 设置拍照后是否预览图片 默认 true
		// comp.componentOption().cameraOption().setEnablePreview(true);

		// 多选相册组件配置
		// 设置相册最大选择数量
		comp.componentOption().albumMultipleComponentOption().albumListOption().setMaxSelection(9);
		
		// 多功能编辑组件配置项
		// 设置最大编辑数量
		comp.componentOption().editMultipleComponentOption().setMaxEditImageCount(9);
		
		// 设置没有改变的图片是否保存(默认 false)
		// comp.componentOption().editMultipleComponentOption().setEnableAlwaysSaveEditResult(false);
		
		// 设置编辑时是否支持追加图片 默认 true
		// comp.componentOption().editMultipleComponentOption().setEnableAppendImage(true);
		
		// 设置照片排序方式
		// comp.componentOption().albumMultipleComponentOption().albumListOption().setPhotosSortDescriptor(PhotoSortDescriptor.Date_Added);
		
		// 设置最大支持的图片尺寸 默认：8000 * 8000
//		 comp.componentOption().albumMultipleComponentOption().albumListOption().setMaxSelectionImageSize(new TuSdkSize(8000, 8000));

		// 操作完成后是否自动关闭页面
		comp.setAutoDismissWhenCompleted(true)
		// 显示组件
			.showComponent();
	}
	
    /**
	 * 获取编辑结果
	 */
	@Override
	public void onComponentFinished(TuSdkResult result, Error error,TuFragment lastFragment) 
	{
		 for (ImageSqlInfo info : result.images)
		 {
		 	   PictureInfo pictureInfo = new PictureInfo(false, info.path);
		 	   mlist.add(0, pictureInfo);
		 }
		 adapter.refreshData(mlist);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQUEST_GET_ADDRESS:
			//得到选择的地址
			if(data != null){
				AddressInfo addressInfo = (AddressInfo) data.getParcelableExtra("addressInfo");
				tv_addr.setText(addressInfo.getName());
				selectLatLng = addressInfo.getLatLng();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
