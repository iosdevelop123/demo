package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class AsyNativePlace extends AsyncTask<String, Void, String>{

	Application App;
	CitysWidget Ctw;
	ProgressDialog proDialog;
	public AsyNativePlace(Application app,CitysWidget ctw,ProgressDialog proDialog){
		this.App=app;
		this.Ctw=ctw;
		this.proDialog=proDialog;
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
		if(!"false".equals(result)){
		UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
		List<UserOrder> uorder = UserJsonUtils.parseUserFromJson(result);
		Iterator iterator = uorder.iterator();
		iterator.hasNext();
		UserOrder orderL = (UserOrder) iterator.next();
		Ctw.setAreaVal(orderL.getArea().toString());
		}
		if(proDialog!=null){
		proDialog.dismiss();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
