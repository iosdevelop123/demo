package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AsynUserActivity extends AsyncTask<String, Void, String>{

	Application App;
	String Usertype;
	
	private TextView Editusertype;
	private TextView Editusername;
	private TextView Editnativace;
	private TextView Editrealname;
	private TextView Editmail;
	private TextView Edittel;
	private TextView Safeteltext;
	private TextView Editsafetel;
	private TextView Editisvip;
	ProgressDialog proDialog;
	LinearLayout Safetellayout;
	public AsynUserActivity(Application app,String usertype,TextView editusertype,TextView editusername,TextView editnativace,TextView editrealname,TextView editmail,TextView edittel,TextView safeteltext,TextView editsafetel,LinearLayout safetellayout,TextView editisvip,ProgressDialog proDialog){
		this.App=app;
		this.Usertype=usertype;
		this.Editmail=editmail;
		this.Editusertype=editusertype;
		this.Editusername=editusername;
		this.Editnativace=editnativace;
		this.Editrealname=editrealname;
		this.Edittel=edittel;
		this.Safeteltext=safeteltext;
		this.Editsafetel=editsafetel;
		this.Editisvip=editisvip;
		Safetellayout=safetellayout;
		this.proDialog=proDialog;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App,params[0].toString());
		return jsonData;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}


	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (!"false".equals(result)) {
			
			UserJsonUtils UserOrderJsonUtils = new com.red263.commmodule.UserJsonUtils();
			List<UserOrder> uorder = UserOrderJsonUtils
					.parseUserFromJson(result);
			Iterator iterator = uorder.iterator();
			iterator.hasNext();
			UserOrder orderL = (UserOrder) iterator.next();
			if ("0".equals(Usertype)) {
				this.Editusertype.setText("普通用户");
			} else {
				this.Editusertype.setText("驾驶员");
			}
			this.Editusername.setText(orderL.getName());
			this.Editnativace.setText(orderL.getProvince() + orderL.getCity()
					+ orderL.getArea());
			this.Editrealname.setText(orderL.getRealname());
			this.Editmail.setText(orderL.getMail());
			this.Edittel.setText(orderL.getTel());
//			
			if ("0".equals(Usertype)) {
				Editsafetel.setText(orderL.getSafetel());
				if(Integer.parseInt(orderL.getBalance())<100){
					Editisvip.setText("否");
				}else{
					Editisvip.setText("是"+"{余额："+orderL.getBalance()+"}");
				}
			} else {
				// 如果不是会员则不显示安全号码
				Safeteltext.setVisibility(View.GONE);
				Editsafetel.setVisibility(View.GONE);
				Safetellayout.setVisibility(View.GONE);
				
			}
		}
		if(proDialog!=null){
			proDialog.dismiss();
		}
		super.onPostExecute(result);
	}

}
