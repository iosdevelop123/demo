package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class AsynOrderInfoC extends AsyncTask<String, Void, String> {

	Application App;

	TextView realname;
	TextView tel;
	TextView editdate;
	TextView servertypes;
	TextView inadr;
	TextView toadr;
	TextView serverstate;
	TextView pscore;

	EditText price;
	RatingBar scoreratingbar;
	EditText assesscontent;
	Button assesssub;
	Button comptBtn;
	Button startBtn;
	Button driverlocal;
	TextView assesscontentview;
	TextView SMScontent;

	String usertype;
	Button paybtn;
	ProgressDialog proDialog;

	public AsynOrderInfoC(Application app, TextView realname, TextView tel,
			TextView editdate, TextView servertypes, TextView inadr,
			TextView toadr, TextView serverstate, TextView pscore,
			RatingBar scoreratingbar, EditText assesscontent, Button assesssub,
			Button comptBtn, Button startBtn, Button driverlocal,
			TextView assesscontentview, 
			TextView SMScontent, String usertype, EditText price, Button paybtn,ProgressDialog proDialog) {
		this.App = app;

		this.realname = realname;
		this.tel = tel;
		this.editdate = editdate;
		this.servertypes = servertypes;
		this.inadr = inadr;
		this.toadr = toadr;
		this.serverstate = serverstate;
		this.pscore = pscore;

		this.scoreratingbar = scoreratingbar;
		this.assesscontent = assesscontent;
		this.assesssub = assesssub;
		this.comptBtn = comptBtn;
		this.startBtn = startBtn;
		this.driverlocal = driverlocal;
		this.assesscontentview = assesscontentview;
		

		this.SMScontent = SMScontent;
		this.usertype = usertype;

		this.price = price;

		this.paybtn = paybtn;
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
		String jsonData = FunUtils.getJsonforGet(App, params[0].toString());
		return jsonData;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (!"false".equals(result)) {
			OrderJsonUtils OrderJsonUtils = new com.red263.commmodule.OrderJsonUtils();
			List<OrderListOrder> uorder = OrderJsonUtils
					.parseUserFromJson(result);
			Iterator iterator = uorder.iterator();
			iterator.hasNext();
			OrderListOrder orderL3 = (OrderListOrder) iterator.next();
			this.editdate.setText(orderL3.getDate());
			this.servertypes.setText(orderL3.getTypes());
			this.inadr.setText(orderL3.getInadr());
			this.toadr.setText(orderL3.getToadr());
			String smstxt=this.SMScontent.getText().toString() + orderL3.getInadr() + "送往"
					+ orderL3.getToadr() + "，请您放心！";
	
			
			this.SMScontent.setText(smstxt);
			
				// 根据当前状态指定控件显示，状态为0时并且用户是驾驶员，显示开始服务按钮
			    this.serverstate.setText("服务即将开始");
			    //
				if (orderL3.getState().equals("0")) {
					if ("1".equals(usertype)) {
					this.startBtn.setVisibility(View.VISIBLE);
					}
				}
				// 根据当前状态指定控件显示，状态为1时并且用户是驾驶员，显示服务完成按钮
				if (orderL3.getState().equals("1")) {
				
				    this.serverstate.setText("服务进行中");
				    //
					if ("1".equals(usertype)) {
					this.price.setVisibility(View.VISIBLE);
					this.comptBtn.setVisibility(View.VISIBLE);
					}
				}
				// 根据当前状态指定控件显示，状态为1时并且用户是驾驶员，显示服务完成按钮
				if (orderL3.getState().equals("2")) {
				    
					this.serverstate.setText("服务完成");
				    //
					if ("1".equals(usertype)) {
					this.assesscontentview.setVisibility(View.VISIBLE);
					this.pscore.setVisibility(View.VISIBLE);
					this.scoreratingbar.setVisibility(View.VISIBLE);
					scoreratingbar.setIsIndicator(true);
					}

				}

			
			if ("0".equals(usertype)) {
				if (orderL3.getIspay().equals("0")) {
					if (orderL3.getState().equals("2")) {
						this.paybtn.setVisibility(View.VISIBLE);
					}
				}
				if (orderL3.getIspay().equals("1")) {
					if (orderL3.getIsassess().equals("0")) {
						// this.paybtn.setVisibility(View.VISIBLE);
						this.assesscontent.setVisibility(View.VISIBLE);
						this.scoreratingbar.setVisibility(View.VISIBLE);
						this.assesssub.setVisibility(View.VISIBLE);
						this.pscore.setVisibility(View.VISIBLE);
					} else {
						this.assesscontentview.setVisibility(View.VISIBLE);
						this.pscore.setVisibility(View.VISIBLE);
						this.scoreratingbar.setVisibility(View.VISIBLE);
						scoreratingbar.setIsIndicator(true);

					}
				}

			}

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
