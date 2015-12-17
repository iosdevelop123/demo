package com.red263.commmodule;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Application;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

public class AsyCheckColl extends AsyncTask<String, Void, String> {

	Button btn;
	Application App;

	public AsyCheckColl(Application app, Button btn) {
		this.btn = btn;
		this.App = app;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		String State;
		String isvip = FunUtils.getJsonforGet(App, params[4].toString());
	
	//	System.out.println("地址"+params[4].toString());
	//	System.out.println("vip"+isvip);
		
		if ("true".equals(isvip)) {
			List listparams = new ArrayList();
			listparams.add(new BasicNameValuePair("userid", params[1]
					.toString()));
			listparams.add(new BasicNameValuePair("driverid", params[2]
					.toString()));
			listparams.add(new BasicNameValuePair("usertype", params[3]
					.toString()));
			// 这里换成你的验证地址
			State = FunUtils.getJsonforPost(listparams, params[0].toString());
			
		//	System.out.println("是否收藏"+State);
			
			if ("true".equals(State)) {
				State = "collfalse";
			} else {
				State = "true";
			}
		} else {
			State = "vipfalse";
		}

		return State;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
	//	System.out.println("最后结果"+result);
		if ("true".equals(result)) {
			btn.setVisibility(View.VISIBLE);
		}

		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
