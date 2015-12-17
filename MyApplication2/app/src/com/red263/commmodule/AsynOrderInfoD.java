package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.RatingBar;
import android.widget.TextView;

public class AsynOrderInfoD extends AsyncTask<String, Void, String>{

	Application App;

	TextView assesscontentview;
	RatingBar scoreratingbar;
	public AsynOrderInfoD(Application app,TextView assesscontentview,RatingBar scoreratingbar){
		this.App=app;
		this.assesscontentview=assesscontentview;
		this.scoreratingbar=scoreratingbar;
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
		if (!"false".equals(result)) {

			AssessJsonUtils AssessJsonUtils = new com.red263.commmodule.AssessJsonUtils();
			List<AssessListOrder> uorder = AssessJsonUtils
					.parseUserFromJson(result);
			Iterator iterator = uorder.iterator();
			iterator.hasNext();
			AssessListOrder orderL = (AssessListOrder) iterator.next();
			this.assesscontentview.setText("评价内容:" + orderL.getContents());
			this.scoreratingbar.setRating(Integer.parseInt(orderL.getScore()));
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
