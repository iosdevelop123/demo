package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AsynSubInfo extends AsyncTask<String, Void, String> {
	TextView realname;
	TextView tel;
	TextView nativeedit;
	TextView serverstate;
	TextView score;
	TextView inadrtext;
	TextView toadrtext;
	EditText inadr;
	EditText toadr;
	Topbar t;

	Spinner typespinner;
	Button sub;
	Application App;
	Spinner paytypespinner;
	String usertype;
	String userid;
	ProgressDialog proDialog;
	Context mContext;

	public AsynSubInfo(String usertype, Application app, TextView realname,
			TextView tel, TextView nativeedit, TextView serverstate,
			TextView score, TextView inadrtext, TextView toadrtext,
			EditText inadr, EditText toadr, Topbar t, Spinner typespinner,
			Button sub, Spinner paytypespinner, String userid,ProgressDialog proDialog,Context context) {
		this.usertype = usertype;
		this.App = app;
		this.realname = realname;
		this.tel = tel;
		this.nativeedit = nativeedit;
		this.serverstate = serverstate;
		this.score = score;
		this.inadrtext = inadrtext;
		this.toadrtext = toadrtext;
		this.inadr = inadr;
		this.toadr = toadr;
		this.t = t;
		this.typespinner = typespinner;
		this.sub = sub;
		this.paytypespinner = paytypespinner;
		this.userid = userid;
		this.proDialog=proDialog;
		this.mContext=context;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App, params[0].toString());
		return jsonData;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (!"flase".equals(result)) {
			JsonUtils UserOrderJsonUtils = new com.red263.commmodule.JsonUtils();
			List<DriverListOrder> uorder = UserOrderJsonUtils
					.parseUserFromJson(result);
			Iterator iterator = uorder.iterator();
			iterator.hasNext();
			DriverListOrder orderL = (DriverListOrder) iterator.next();

			t.setTextViewText("驾驶员：" + orderL.getRealname());
			this.nativeedit.setText("" + orderL.getProvince()
					+ orderL.getCity() + orderL.getArea());
			this.tel.setText(orderL.getTel());
			if (orderL.getAstate() != null && !"".equals(orderL.getAstate())) {
				String serverstateval = "";
				if (orderL.getAstate().equals("0")) {
					serverstateval = "预约中...";
	
				} else if (orderL.getAstate().equals("1")) {
					serverstateval = "服务中...";

				} else {
					if ("0".equals(usertype)) {
						this.inadrtext.setVisibility(View.VISIBLE);
						this.toadrtext.setVisibility(View.VISIBLE);
						this.typespinner.setVisibility(View.VISIBLE);
						this.paytypespinner.setVisibility(View.VISIBLE);
						this.inadr.setVisibility(View.VISIBLE);
						this.toadr.setVisibility(View.VISIBLE);
						this.sub.setVisibility(View.VISIBLE);
						//获取地址
						
//     					String nowlocal = FunUtils.getJsonforGet(App,
//							"userinfo.php?usertype=0&id=" + userid);
//     					
//						UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
//						List<UserOrder> uorder2 = UserJsonUtils
//								.parseUserFromJson(nowlocal);
//						Iterator iterator2 = uorder2.iterator();
//						iterator2.hasNext();
//						UserOrder orderL2 = (UserOrder) iterator2.next();
//						if(orderL2.getLocaladdr()!=null&&orderL2.getLocaladdr()!=""){
//						this.inadr.setText(orderL2.getLocaladdr());
//						}else{
//							this.inadr.setText("");
//						}

					}
					serverstateval = "空闲中...";
				}
				this.serverstate.setText(serverstateval);
			}

			if (orderL.getScore() != null && !"".equals(orderL.getScore())) {
				this.score.setText(orderL.getScore());
			}

			
		}

		if (proDialog != null) {
		proDialog.dismiss();
		}
		super.onPostExecute(result);

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
	
}
