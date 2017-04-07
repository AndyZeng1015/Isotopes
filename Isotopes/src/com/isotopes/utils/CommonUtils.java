package com.isotopes.utils;

import android.os.Environment;
import java.io.File;

public class CommonUtils {

	//在SD卡上创建一个文件夹
	public static void createSDCardDir(){

		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

			File image_path = new File(Config.IMAGE_PATH);

			if (!image_path.exists()) {
				//若不存在，创建目录，可以在应用启动的时候创建
				image_path.mkdirs();
			}
		}
	}
}
