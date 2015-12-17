package com.red263.Edaijia;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.red263.commmodule.AsyCheckColl;
import com.red263.commmodule.AsynSubInfo;
import com.red263.commmodule.AsyncInaddr;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Sub_Activity extends Activity {

	Topbar t;
	TextView realname;
	TextView tel;
	TextView nativeedit;
	TextView serverstate;
	TextView score;
	TextView inadrtext;
	TextView toadrtext;
	EditText inadr;
	EditText toadr;

	Spinner typespinner;
	Button sub;
	Button collect;
	Button mapLocalBtn;

	String driverid;
	String usertype;
	String userid;
	String username;
	int tabindex;

	Spinner paytypespinner;

	subt2 su2;
	// subt3 su3;
	subt4 su4;

	private ProgressDialog proDialog;
	public static Sub_Activity subinstance = null;   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_activity);
		if(subinstance!=null){
			Sub_Activity.subinstance.finish();
		}
		subinstance=this;
		init();
		findViews();
		su2 = new subt2();
		su4 = new subt4();
		sub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!su4.isAlive()) {
					su4.start();
					proDialog = ProgressDialog.show(Sub_Activity.this, "载入中..",
							"信息正在处理请稍后....", true, false);
				}
			}
		});

		collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!su2.isAlive()) {
					su2.start();
					proDialog = ProgressDialog.show(Sub_Activity.this, "载入中..",
							"信息正在处理请稍后....", true, false);
					
				}
				
			}
		});

		mapLocalBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						MapLocal_Activity.class);
				Bundle bundle = new Bundle();
				bundle.putString("MAP_DRIVERID", driverid);

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	Handler subh4 = new Handler() {
		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");

			if ("true".equals(state)) {
				//  MainActivity.instance.finish();
				// 需要传输数据到登陆后的界面,
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				// 转向登陆后的页面
				startActivity(intent);
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(Sub_Activity.this, "预约成功，等待驾驶员联系您!",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(Sub_Activity.this, "预约失败，请联系管理员处理!",
						Toast.LENGTH_SHORT).show();
			}
	
		}
	};

	class subt4 extends Thread {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();
			params.add(new BasicNameValuePair("userid", userid));
			params.add(new BasicNameValuePair("driverid", driverid));
			params.add(new BasicNameValuePair("inadr", inadr.getText()
					.toString()));
			params.add(new BasicNameValuePair("toadr", toadr.getText()
					.toString()));
			params.add(new BasicNameValuePair("types", typespinner
					.getSelectedItem().toString()));

			params.add(new BasicNameValuePair("howpay", String
					.valueOf(paytypespinner.getSelectedItemId())));

			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "subscribe.php");
			String State = FunUtils.getJsonforPost(params, validateURL);

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			subh4.sendMessage(message);
			return;
		}
	};


	Handler subh2 = new Handler() {
		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");

			if ("true".equals(state)) {
				sub.setEnabled(false);
			//	MainActivity.instance.finish();
				// 需要传输数据到登陆后的界面,
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putInt("MAP_TABINDEX", 3);
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), MainActivity.class);
				// 转向登陆后的页面
				startActivity(intent);
				if (proDialog != null) {
					proDialog.dismiss();
				}
				
				Toast.makeText(Sub_Activity.this, "收藏成功!", Toast.LENGTH_SHORT)
				.show();
				finish();
			} else {
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(Sub_Activity.this, "收藏失败!", Toast.LENGTH_SHORT)
						.show();
			}

		}
	};

	class subt2 extends Thread {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();
			params.add(new BasicNameValuePair("userid", userid));
			params.add(new BasicNameValuePair("driverid", driverid));
			// 传入用户类型
			params.add(new BasicNameValuePair("usertype", usertype));

			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "collect.php");
			String State = FunUtils.getJsonforPost(params, validateURL);

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			subh2.sendMessage(message);
			return;
		}
	};

	private void init() {
		Intent intent = getIntent();
		driverid = intent.getStringExtra("MAP_DRIVERID");
		tabindex = intent.getIntExtra("MAP_INDEXTAB", 0);
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}

	private void findViews() {
		t = (Topbar) findViewById(R.id.subtopbar);
		t.setBackActivity(tabindex);
		realname = (TextView) findViewById(R.id.realname);
		tel = (TextView) findViewById(R.id.tel);
		nativeedit = (TextView) findViewById(R.id.nativeedit);
		serverstate = (TextView) findViewById(R.id.serverstate);
		//
		typespinner = (Spinner) findViewById(R.id.typespinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, // 上下文对象
				R.array.server_type_array, // 在strings.xml中定义的那个数组
				android.R.layout.simple_spinner_item // 布局文件(Android自带的，定义下拉菜单是什么样子的)
				);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item // 定义每一个选项是什么样子
		);
		// 为Spinner绑定数据
		typespinner.setAdapter(adapter);
		typespinner.setPrompt("服务类型");

		score = (TextView) findViewById(R.id.score);
		sub = (Button) findViewById(R.id.sub);
		collect = (Button) findViewById(R.id.collect);

		inadrtext = (TextView) findViewById(R.id.inadrtext);
		toadrtext = (TextView) findViewById(R.id.toadrtext);

		inadr = (EditText) findViewById(R.id.inadr);
		toadr = (EditText) findViewById(R.id.toadr);
		//
		mapLocalBtn = (Button) findViewById(R.id.localmap);

		paytypespinner = (Spinner) findViewById(R.id.paytypespinner);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, // 上下文对象
				R.array.pay_type_array, // 在strings.xml中定义的那个数组
				android.R.layout.simple_spinner_item // 布局文件(Android自带的，定义下拉菜单是什么样子的)
				);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item // 定义每一个选项是什么样子
		);
		// 为Spinner绑定数据
		paytypespinner.setAdapter(adapter2);
		paytypespinner.setPrompt("支付方式");

		// 只有是vip用户才有资格收藏
		if ("0".equals(usertype)) {
			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "collectcheck.php");
			AsyCheckColl asc = new AsyCheckColl(getApplication(), collect);
			asc.execute(validateURL, userid, driverid, usertype,
					"checkvip.php?userid=" + userid);
			//
			AsyncInaddr asaddr=new AsyncInaddr(getApplication(),inadr);
			asaddr.execute("userinfo.php?usertype=0&id=" + userid);
			
			
		}
		
		proDialog = ProgressDialog.show(this, "载入中..",
				"信息正在处理请稍后....", true, false);
		
		AsynSubInfo asi = new AsynSubInfo(usertype, getApplication(), realname,
				tel, nativeedit, serverstate, score, inadrtext, toadrtext,
				inadr, toadr, t, typespinner, sub, paytypespinner,userid,proDialog,getApplicationContext());
		asi.execute("driverinfo.php?id=" + driverid);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		finish();
		super.onDestroy();
	}

}
