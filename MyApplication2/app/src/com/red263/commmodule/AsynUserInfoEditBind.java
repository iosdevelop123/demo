package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;

public class AsynUserInfoEditBind extends AsyncTask<String, Void, String> {

	Application App;

	EditText editrealname;
	EditText editmail;
	EditText edittel;
	EditText editsafetel;
	ProgressDialog proDialog;
	public AsynUserInfoEditBind(Application app, EditText editrealname,
			EditText editmail, EditText edittel, EditText editsafetel,ProgressDialog proDialog) {
		this.App = app;

		this.editrealname = editrealname;
		this.editmail = editmail;
		this.edittel = edittel;
		this.editsafetel = editsafetel;
		this.proDialog=proDialog;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App, params[0].toString());
		return jsonData;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
		this.editmail.setText(orderL.getMail().toString());
		this.editrealname.setText(orderL.getRealname());
		this.edittel.setText(orderL.getTel());
		this.editsafetel.setText(orderL.getSafetel());

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
