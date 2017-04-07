package com.isotopes.base;

import android.app.Activity;
import android.app.Application;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lasque.tusdk.core.TuSdk;
import org.xutils.BuildConfig;
import org.xutils.x;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.easeui.controller.EaseUI;

import cn.bmob.v3.Bmob;

/**
 * 
 * @author zyn
 *
 */
public class BaseApplication extends Application{
    private List<Activity> activityList = new LinkedList<Activity>();
    private String tag = "App";
    public static String APPID ="d66d4d7fb5e69f084a39eb6fb31a7ac0";
    
    private static BaseApplication instance;
    public static BaseApplication getInstance(){
        return instance;
    }

    private ExecutorService es;

    public void execRunnable(Runnable r){
        es.execute(r);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        es = Executors.newFixedThreadPool(3);
        Bmob.initialize(this,APPID);
        EaseUI.getInstance().init(this, null);
        
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        
        //开启调试
        TuSdk.enableDebugLog(true);
        //涂图初始化
        TuSdk.init(this, "61e34d3b10b037c2-01-ijnoq1");
        
        SDKInitializer.initialize(getApplicationContext());  
    }


	public void exit(){
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

  //将Activity从容器中移除
    public void removeActivity(Activity activity){ activityList.remove(activity); }
}
