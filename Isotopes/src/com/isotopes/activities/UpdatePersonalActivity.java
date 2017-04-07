package com.isotopes.activities;

import java.io.File;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.isotopes.R;
import com.isotopes.appwidget.CircleTransform;
import com.isotopes.base.BaseActivity;
import com.isotopes.beans.User;
import com.isotopes.utils.Config;
import com.isotopes.utils.Contast;
import com.isotopes.utils.ImageUtils;
import com.isotopes.utils.ToastUtil;
import com.squareup.picasso.Picasso;

public class UpdatePersonalActivity extends BaseActivity {
	
	private ImageView iv_head;
	private EditText et_username;
	private Spinner sp_sex;
	private EditText et_phone;
	private EditText et_addr;
	private EditText et_school;
	private EditText et_occupation;
	private EditText et_desc;
	private ImageView iv_back;
	
	private TextView tv_save;
	
	private File head_img_file;
	
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
		//修改头像
		iv_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChoosePicDialog();
			}
		});
		
		//保存
		tv_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(head_img_file != null){
					Contast.user.setHeadimg(new BmobFile(head_img_file));
				}
				Contast.user.setUsername(et_username.getText().toString().trim());
				Contast.user.setSex(sp_sex.getSelectedItem().toString());
				Contast.user.setMobilePhoneNumber(et_phone.getText().toString().trim());
				Contast.user.setAddr(et_addr.getText().toString().trim());
				Contast.user.setSchool(et_school.getText().toString().trim());
				Contast.user.setOccupation(et_occupation.getText().toString().trim());
				Contast.user.setDesc(et_desc.getText().toString().trim());
				
				if(Contast.user.getHeadimg() == null || Contast.user.getUsername().equals("") || Contast.user.getSex().equals("") || Contast.user.getMobilePhoneNumber().equals("") || Contast.user.getAddr().equals("") || Contast.user.getSchool().equals("") || Contast.user.getOccupation().equals("") || Contast.user.getDesc().equals("")){
					ToastUtil.showToast(mContext, "请将信息填写完整！");
					return;
				}
				//提交保存
				Contast.user.update(Contast.user.getObjectId(),new UpdateListener() {
				    @Override
				    public void done(BmobException e) {
				        if(e==null){
				        	ToastUtil.showToast(mContext, "保存成功 ");
				        	if(getIntent().getBundleExtra("key").getString("type") != null && getIntent().getBundleExtra("key").getString("type").equals("main")){
				        		jump(UpdatePersonalActivity.this, MainActivity.class, true);
				        	}else{
				        		UpdatePersonalActivity.this.finish();
				        	}
				        }else{
				        	ToastUtil.showToast(mContext, "保存失败:"+e.getMessage());
				        }
				    }
				});
				
			}
		});
		
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdatePersonalActivity.this.finish();
			}
		});
	}
	
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    
    private String mTake_pic_path;
    private String crop_pic_path;//裁剪保存后的图片路径
    
    /**
     * 选择头像设置是从拍照还是相册中选
     */
    private void showChoosePicDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.setTitle("选择头像");
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = View.inflate(mContext, R.layout.dialog_select_pic, null);
        dialog.setContentView(view);
        // 拍照
        view.findViewById(R.id.from_camera).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        formCamera();
                        dialog.dismiss();
                    }
                });
        // 从相册中选择
        view.findViewById(R.id.from_gallery).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        formGallerya();
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
	
	/**
     * 拍照
     */
    private void formCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTake_pic_path = ImageUtils.getRandomFileName(Config.IMAGE_PATH);
        File file = new File(mTake_pic_path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//将图片文件转化为一个uri传入
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /**
     * 照片中选择
     */
    private void formGallerya() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照返回图片
        if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                //图片路径
                String localSelectPath = mTake_pic_path;
                //裁剪图片
                crop_pic_path = ImageUtils.getRandomFileName(Config.IMAGE_PATH);
                ImageUtils.startPhotoZoom(this, Uri.fromFile(new File(localSelectPath)), 1, 1, 150, 150, PHOTO_REQUEST_CUT, crop_pic_path);
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                ContentResolver resolver = getContentResolver();
                Uri originalUri = data.getData();        //获得图片的uri
                //最后根据索引值获取图片路径
                String localSelectPath = ImageUtils.getPath(mContext, originalUri);
                //裁剪图片
                crop_pic_path = ImageUtils.getRandomFileName(Config.IMAGE_PATH);
                ImageUtils.startPhotoZoom(this, Uri.fromFile(new File(localSelectPath)), 1, 1, 150, 150, PHOTO_REQUEST_CUT, crop_pic_path);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
            	final BmobFile head_img = new BmobFile(new File(crop_pic_path));
            	head_img.upload(new UploadFileListener() {                
            		@Override                
            		public void done(BmobException e) {                    
            			if(e==null){   
            				Contast.user.setHeadimg(head_img);  
            				Picasso.with(mContext).load(new File(crop_pic_path)).transform(new CircleTransform()).into(iv_head);
            			}else{
            				ToastUtil.showToast(mContext, "图片上传失败:"+e.getMessage());
            			}
            		}
            	});
            	
            }
        }
    }

	private void initData() {
		// TODO Auto-generated method stub
		//设置头像
		if(Contast.user.getHeadimg() != null){
			Picasso.with(mContext).load(Contast.user.getHeadimg().getFileUrl()).placeholder(R.drawable.head_default).error(R.drawable.head_default).transform(new CircleTransform()).into(iv_head);
		}
		et_username.setText(Contast.user.getUsername()==null?"":Contast.user.getUsername());
		if(Contast.user.getSex() != null && Contast.user.getSex().equals("帅哥")){
			sp_sex.setSelection(0);
		}else{
			sp_sex.setSelection(1);
		}
		et_phone.setText(Contast.user.getMobilePhoneNumber()==null?"":Contast.user.getMobilePhoneNumber());
		et_addr.setText(Contast.user.getAddr()==null?"":Contast.user.getAddr());
		et_school.setText(Contast.user.getSchool()==null?"":Contast.user.getSchool());
		et_occupation.setText(Contast.user.getOccupation()==null?"":Contast.user.getOccupation());
		et_desc.setText(Contast.user.getDesc()==null?"":Contast.user.getDesc());
		
		((TextView)findViewById(R.id.tv_title)).setText(getIntent().getBundleExtra("key").getString("title"));
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_update_personal);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_username = (EditText) findViewById(R.id.et_username);
		sp_sex = (Spinner) findViewById(R.id.sp_sex);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_addr = (EditText) findViewById(R.id.et_addr);
		et_school = (EditText) findViewById(R.id.et_school);
		et_occupation = (EditText) findViewById(R.id.et_occupation);
		et_desc = (EditText) findViewById(R.id.et_desc);
		
		tv_save = (TextView) findViewById(R.id.tv_save);
	}
}
