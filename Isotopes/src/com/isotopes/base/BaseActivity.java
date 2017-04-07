package com.isotopes.base;

import com.isotopes.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {
	
	public Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseApplication.getInstance().addActivity(this);
		this.mContext = this;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BaseApplication.getInstance().removeActivity(this);
	}
	
    public void jump(Activity activity, Class to_class, boolean isClose) {
        Intent intent = new Intent(activity, to_class);
        startActivity(intent);
        if(isClose == true){
            activity.finish();
        }
        overridePendingTransition(R.anim.enter, R.anim.out);
    }
    
    public void jump(Activity activity, Class to_class, int requestId) {
        Intent intent = new Intent(activity, to_class);
        startActivityForResult(intent, requestId);
        overridePendingTransition(R.anim.enter, R.anim.out);
    }

    public void jump(Activity activity, Class to_class, String key, Bundle bundle, boolean isClose) {
        Intent intent = new Intent(activity, to_class);
        intent.putExtra(key, bundle);
        startActivity(intent);
        if(isClose == true){
            activity.finish();
        }
        overridePendingTransition(R.anim.enter, R.anim.out);
    }
    
    public void jump(Activity activity, Class to_class, String key, Bundle bundle,  int requestId) {
        Intent intent = new Intent(activity, to_class);
        intent.putExtra(key, bundle);
        startActivityForResult(intent, requestId);
        overridePendingTransition(R.anim.enter, R.anim.out);
    }
    
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
