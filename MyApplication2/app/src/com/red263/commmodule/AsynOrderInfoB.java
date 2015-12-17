package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.TextView;

public class AsynOrderInfoB extends AsyncTask<String, Void, String>{

	Application App;
	Topbar topbar;
	TextView tel;
	TextView SMScontent;
	TextView safetel;
	
	public AsynOrderInfoB(Application app,Topbar topbar,TextView tel,TextView SMScontent,TextView safetel){
		this.App=app;
		this.topbar=topbar;
		this.tel=tel;
		this.SMScontent=SMScontent;
		this.safetel=safetel;
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
		
		UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
		List<UserOrder> uorder = UserJsonUtils.parseUserFromJson(result);
		Iterator iterator = uorder.iterator();
		iterator.hasNext();
		UserOrder orderL = (UserOrder) iterator.next();
		topbar.setTextViewText("预约用户：" + orderL.getRealname());
		this.tel.setText(orderL.getTel());
		this.safetel.setText(orderL.getSafetel());
		
		String smstxt=this.SMScontent.getText().toString()+"您的亲人"+orderL.getRealname()+"，联系电话为"+orderL.getTel()+"。正在由我送从";
		this.SMScontent.setText(smstxt);	
		
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
