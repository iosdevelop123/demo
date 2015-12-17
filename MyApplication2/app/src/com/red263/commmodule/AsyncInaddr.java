package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.EditText;

public class AsyncInaddr extends AsyncTask<String, Void, String> {

	Application App;
	EditText Inadr;
    public AsyncInaddr (Application app,EditText inadr) {
	this.App=app;
	this.Inadr=inadr;
    }

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App, params[0].toString());
		return jsonData;
	}
	

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (!"flase".equals(result)) {
				
			UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
			List<UserOrder> uorder2 = UserJsonUtils
					.parseUserFromJson(result);
			@SuppressWarnings("rawtypes")
			Iterator iterator2 = uorder2.iterator();
			iterator2.hasNext();
			UserOrder orderL2 = (UserOrder) iterator2.next();
			if(orderL2.getLocaladdr()!=null&&orderL2.getLocaladdr()!=""){
			this.Inadr.setText(orderL2.getLocaladdr());
			}else{
				this.Inadr.setText("");
			}
		}
		super.onPostExecute(result);
	}
}
