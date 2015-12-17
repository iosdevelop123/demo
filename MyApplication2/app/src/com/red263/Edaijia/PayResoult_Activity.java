package com.red263.Edaijia;

import com.red263.commmodule.FunUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PayResoult_Activity extends Activity {

	String balance;
	String alipayname;
	TextView alipaynametextview;
	TextView balanceview;
	Button subBtn;
	private String userid;
	private String usertype;
	private String username;
	payt pt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_resoult_activity);
		init();
		pt=new payt();
		Intent intent = getIntent();
		balance = intent.getStringExtra("MAP_BALANCE");
		alipayname = intent.getStringExtra("MAP_ALIPAYNAME");

		alipaynametextview = (TextView) findViewById(R.id.editalipayname);
		balanceview = (TextView) findViewById(R.id.editbalance);
		subBtn = (Button) findViewById(R.id.subtbutton);
		this.alipaynametextview.setText(alipayname);
		this.balanceview.setText(balance);

		subBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// AsynPay asp=new
				// AsynPay(getApplication(),getApplicationContext());
				// asp.execute("upbalance.php?userid=" + userid+ "&usertype=" +
				// usertype+"&balance="+balance);
				if(!pt.isAlive()){
				pt.start();
				}
			}
		});

	}

	Handler h = new Handler() {
		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");
			if ("true".equals(state)) {

				Toast.makeText(PayResoult_Activity.this,
						"支付成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent =new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putString("MAP_BALANCE", balance);
				bundle.putString("MAP_ALIPYNAME", alipayname);
				intent.putExtras(bundle);	
				intent.setClass(getApplicationContext(), Pay_ok_Activity.class);
				startActivity(intent);
				finish();
			}
		}
	};

	class payt extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String jsonData = FunUtils.getJsonforGet(getApplication(),
					"upbalance.php?userid=" + userid + "&usertype=" + usertype
							+ "&balance=" + balance);

			System.out.println(jsonData);
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", jsonData);
			message.setData(bundle);
			h.sendMessage(message);
		}
	};

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}

}
