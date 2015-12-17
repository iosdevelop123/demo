package com.red263.Edaijia;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.red263.commmodule.AsynOrderInfoA;
import com.red263.commmodule.AsynOrderInfoB;
import com.red263.commmodule.AsynOrderInfoC;
import com.red263.commmodule.AsynOrderInfoD;
import com.red263.commmodule.FunUtils;
import com.red263.commmodule.Topbar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Order_Activity extends Activity {

	private Topbar topbar;
	String driverid;
	String userid;
	String usertype;
	String username;
	String orderid;

	// 预约用户的userid
	String fuserid;

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
	public TextView assesscontentview;
	TextView safetel;
	TextView SMScontent;

	Button paybtn;

	ordert3 ord3;
	ordert2 ord2;
	ordert4 ord4;
	ordert5 ord5;

	private ProgressDialog proDialog;
	public static Order_Activity orderinstance = null;   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_activity);
		if(orderinstance!=null){
			Order_Activity.orderinstance.finish();
		}
		
        orderinstance=this;
		findViews();
		getinfo();
		
		ord3 = new ordert3();
		ord2 = new ordert2();
		ord4 = new ordert4();
		ord5 = new ordert5();

		assesssub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!ord2.isAlive()) {
					ord2.start();

					proDialog = ProgressDialog.show(Order_Activity.this,
							"载入中..", "信息正在处理请稍后....", true, false);

				}
			}
		});

		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!ord3.isAlive()) {
					ord3.start();

					proDialog = ProgressDialog.show(Order_Activity.this,
							"载入中..", "信息正在处理请稍后....", true, false);

				}
			}
		});

		comptBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ord4.isAlive()) {
					ord4.start();
					
					proDialog = ProgressDialog.show(Order_Activity.this,
							"载入中..", "信息正在处理请稍后....", true, false);
				}
			}
		});

		driverlocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putString("MAP_DRIVERID", driverid);
				bundle.putString("MAP_ORDERID", orderid);
				bundle.putString("MAP_FUSERID", fuserid);
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(),
						MapLocal_Order_Activity.class);
				startActivity(intent);
				finish();
				}
		});

		paybtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!ord5.isAlive()) {
					ord5.start();
					proDialog = ProgressDialog.show(Order_Activity.this,
							"载入中..", "信息正在处理请稍后....", true, false);
				}
			}
		});
	}

	/**
	 * 往指定的手机号上发送短信
	 * 
	 * @param destPhone
	 * @param message
	 */
	public static void sendSms(String destPhone, String message) {
		SmsManager smsManager = SmsManager.getDefault();
		if (message.length() > 70) {
			ArrayList<String> msgs = smsManager.divideMessage(message);
			for (String msg : msgs) {
				smsManager.sendTextMessage(destPhone, null, msg, null, null);
			}

		} else {
			smsManager.sendTextMessage(destPhone, null, message, null, null);
		}
	}

	Handler orderh5 = new Handler() {

		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");
			// System.out.println(state);
			if ("true".equals(state)) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putString("MAP_DRIVERID", driverid);
				bundle.putString("MAP_ORDERID", orderid);
				bundle.putString("MAP_FUSERID", fuserid);

				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), Order_Activity.class);
				startActivity(intent);

				if (proDialog != null) {
					proDialog.dismiss();
				}
				finish();

			} else {
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(Order_Activity.this, "支付失败!", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	class ordert5 extends Thread {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();
			params.add(new BasicNameValuePair("id", orderid));
			params.add(new BasicNameValuePair("userid", fuserid));

			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "pay_confirm.php");
			String State = FunUtils.getJsonforPost(params, validateURL);

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			orderh5.sendMessage(message);

			return;
		}
	};

	Handler orderh4 = new Handler() {

		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");
			// Log.v(">>>>>>>>>>>", state);
			if ("true".equals(state)) {

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putString("MAP_DRIVERID", driverid);
				bundle.putString("MAP_ORDERID", orderid);
				bundle.putString("MAP_FUSERID", fuserid);

				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), Order_Activity.class);
				startActivity(intent);
				if (proDialog != null) {
					proDialog.dismiss();
				}
				finish();

			}else {
				if (proDialog != null) {
					proDialog.dismiss();
				}
			}
		}
	};

	class ordert4 extends Thread {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();

			params.add(new BasicNameValuePair("id", orderid));
			params.add(new BasicNameValuePair("state", "2"));
			params.add(new BasicNameValuePair("price", price.getText()
					.toString()));

			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "oversubcribe2.php");
			String State = FunUtils.getJsonforPost(params, validateURL);

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			orderh4.sendMessage(message);
			return;
		}
	};

	Handler orderh3 = new Handler() {

		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");
			if ("true".equals(state)) {

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putString("MAP_DRIVERID", driverid);
				bundle.putString("MAP_ORDERID", orderid);
				bundle.putString("MAP_FUSERID", fuserid);

				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), Order_Activity.class);
				startActivity(intent);
				//
				// 直接使用本机发送通知

				if (safetel.getText().toString().length() > 5) {
					sendSms(safetel.getText().toString(), SMScontent.getText()
							.toString());
				}
				if (proDialog != null) {
					proDialog.dismiss();
				}

				finish();
			}else {
				if (proDialog != null) {
					proDialog.dismiss();
				}
			}

		}
	};

	class ordert3 extends Thread {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();

			params.add(new BasicNameValuePair("id", orderid));
			params.add(new BasicNameValuePair("state", "1"));
			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "oversubcribe.php");
			String State = FunUtils.getJsonforPost(params, validateURL);

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			orderh3.sendMessage(message);
			return;
		}
	};

	Handler orderh2 = new Handler() {

		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");

			if ("true".equals(state)) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putString("MAP_DRIVERID", driverid);
				bundle.putString("MAP_ORDERID", orderid);
				bundle.putString("MAP_FUSERID", fuserid);
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), Order_Activity.class);
				startActivity(intent);
				
				if (proDialog != null) {
					proDialog.dismiss();
				}
				finish();

			}else {
				if (proDialog != null) {
					proDialog.dismiss();
				}
			}

		}
	};

	class ordert2 extends Thread {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();
			params.add(new BasicNameValuePair("score", scoreratingbar
					.getRating() + ""));
			params.add(new BasicNameValuePair("content", assesscontent
					.getText().toString()));
			params.add(new BasicNameValuePair("driverid", orderid));
			params.add(new BasicNameValuePair("userid", userid));
			params.add(new BasicNameValuePair("orderid", orderid));
			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "assess.php");
			String State = FunUtils.getJsonforPost(params, validateURL);

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			orderh2.sendMessage(message);
			return;
		}
	};

	private void findViews() {
		init();
		topbar = (Topbar) findViewById(R.id.ordertopbar);
		// 设置返回tabindex
		topbar.setBackActivity(1);
		realname = (TextView) findViewById(R.id.realname);
		tel = (TextView) findViewById(R.id.tel);
		//
		servertypes = (TextView) findViewById(R.id.servertypes);
		editdate = (TextView) findViewById(R.id.editdate);
		inadr = (TextView) findViewById(R.id.inadr);
		toadr = (TextView) findViewById(R.id.toadr);
		//
		serverstate = (TextView) findViewById(R.id.serverstate);

		// 评价
		pscore = (TextView) findViewById(R.id.pscore);
		scoreratingbar = (RatingBar) findViewById(R.id.scoreratingBar);
		assesscontentview = (TextView) findViewById(R.id.assesscontentview);
		assesscontent = (EditText) findViewById(R.id.assesscontent);
		assesssub = (Button) findViewById(R.id.assesssub);

		comptBtn = (Button) findViewById(R.id.completesub);
		startBtn = (Button) findViewById(R.id.severstart);
		driverlocal = (Button) findViewById(R.id.driverlocal);
		//
		// 驾驶员结算价格
		price = (EditText) findViewById(R.id.price);

		paybtn = (Button) findViewById(R.id.payb);

		safetel = (TextView) findViewById(R.id.mobilenumber);
		SMScontent = (TextView) findViewById(R.id.Smscontent);

		Bindinfo();

	}

	private void Bindinfo() {

		AsynOrderInfoD asoD = new AsynOrderInfoD(getApplication(),
				assesscontentview, scoreratingbar);
		asoD.execute("assessinfo.php?id=" + orderid);

	}

	private void init() {
		Intent intent = getIntent();
		driverid = intent.getStringExtra("MAP_DRIVERID");
		orderid = intent.getStringExtra("MAP_ORDERID");
		fuserid = intent.getStringExtra("MAP_FUSERID");

		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}

	private void getinfo() {
		if ("0".equals(usertype)) {
			AsynOrderInfoA asoA = new AsynOrderInfoA(getApplication(), topbar,
					tel);
			asoA.execute("driverinfo.php?id=" + driverid);
		} else {
			AsynOrderInfoB asoB = new AsynOrderInfoB(getApplication(), topbar,
					tel, SMScontent, safetel);
			asoB.execute("userinfo.php?id=" + fuserid + "&usertype=0");
		}


		proDialog = ProgressDialog.show(this, "载入中..", "信息正在处理请稍后....", true,
				false);
		AsynOrderInfoC asoC = new AsynOrderInfoC(getApplication(), realname,
				tel, editdate, servertypes, inadr, toadr, serverstate, pscore,
				scoreratingbar, assesscontent, assesssub, comptBtn, startBtn,
				driverlocal, assesscontentview, SMScontent, usertype, price,
				paybtn, proDialog);
		asoC.execute("subscribeinfo.php?id=" + orderid);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		finish();
		super.onDestroy();
	}

}
