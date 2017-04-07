package com.isotopes.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获得屏幕相关的辅助类
 *
 * @author
 *
 */
public class ScreenUtil {
    public static Context context;

    private ScreenUtil() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /** 初始化获取屏幕高和宽 */
    public static void init(Context mContext) {
        context = mContext;

    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕密度
     *
     * @param context
     * @return
     */
    public static float getMetric(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        // 屏幕密度（0.75 / 1.0 / 1.5）
        return metric.density;

    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    
    /**
	 * 
	 * @param screen_width
	 *            屏幕宽度
	 * @param bitmap
	 *            图片
	 * @return
	 */
	public static int get_Image_heigth(int screen_width, Bitmap bitmap) {
		// 图片的宽度和高度
		int image_width = bitmap.getWidth();
		int image_height = bitmap.getHeight();
		int widget_height = screen_width * image_height / image_width;
		// height_dip = widget_height * (screen_dpi / 160);
		return widget_height;
	}


}