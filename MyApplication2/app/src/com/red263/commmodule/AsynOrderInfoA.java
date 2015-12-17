package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.TextView;

public class AsynOrderInfoA extends AsyncTask<String, Void, String>{

	Application App;
	Topbar topbar;
	TextView tel;
	public AsynOrderInfoA(Application app,Topbar topbar,TextView tel){
		this.App=app;
		this.topbar=topbar;
		this.tel=tel;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App,params[0].toString());

		
		return jsonData;
	}



	@SuppressWarnings("rawtypes")
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		
		JsonUtils UserOrderJsonUtils = new com.red263.commmodule.JsonUtils();
		List<DriverListOrder> uorder = UserOrderJsonUtils
				.parseUserFromJson(result);
		Iterator iterator = uorder.iterator();
		iterator.hasNext();
		DriverListOrder orderL = (DriverListOrder) iterator.next();
		
		topbar.setTextViewText("驾驶员：" + orderL.getRealname());
		this.tel.setText(orderL.getTel());
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
