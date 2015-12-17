package com.red263.commmodule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class TakePhoto {

	//private String PhotoinPath;

	public static String take(Intent data,String PhotoinPath,String PhotoName) {
       
		String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return null;
        }
        
       // PhotoinPath=Environment.getExternalStorageDirectory ()+"/CitrusP/";

        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        FileOutputStream b = null;
        File file = new File(PhotoinPath);
        file.mkdirs();// 创建文件夹
        
        
        //创建一个当前时间组成文件名的文件
        String fileName = PhotoinPath+PhotoName+".jpg";
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return fileName;
	}
}
