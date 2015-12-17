package com.red263.commmodule;

import com.red263.Edaijia.R;

import android.app.Application;
import android.content.Context;


public class LinkUrl {

	//Application获取连接
	public static String GetUrl(Application app,String s) {
	
		return app.getString(R.string.linkrul)+"service/"+s;

	}
	//Context获取连接
	public static String GetUrlByContex(Context context,String s) {
		return context.getString(R.string.linkrul)+"service/"+s;
	}
}


