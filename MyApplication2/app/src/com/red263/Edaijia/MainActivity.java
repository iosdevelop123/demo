package com.red263.Edaijia;

import com.red263.Edaijia.R;
import com.red263.login.Login_Activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	private TabHost m_tabHost;
	private RadioGroup m_radioGroup;
	private String m_userid;
	// private String m_username;
	private String m_usertype;
	private Integer tabindex = 0;
//	public static MainActivity instance = null;   
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);
//		if(instance!=null){
//			MainActivity.instance.finish();
//		}
		initb();
		init();
//		instance=this;
		if (!PushisServiceRunning()) {
			if ("1".equals(m_usertype)) {
				// 启动service
				Intent service = new Intent(getApplicationContext(),
						service.PushNewService.class);
				// service.putExtra("MAP_TYPE",type);
				service.putExtra("MAP_USERID", m_userid);
				service.putExtra("MAP_USERTYPE", m_usertype);
				startService(service);
			}
		}

		// 启动service

		if (!BDServiceRunning()) {
			Intent service2 = new Intent(getApplicationContext(),
					service.BDLocalService.class);
			service2.putExtra("MAP_USERID", m_userid);
			service2.putExtra("MAP_USERTYPE", m_usertype);
			startService(service2);
		}

		// startlocal();

	}

	private boolean PushisServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.red263.Edaijia.service.PushNewService"
					.equals(service.service.getClassName())) {
				return true;
				// System.out.println("存在该service");
			}
		}
		return false;
	}

	private boolean BDServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.red263.Edaijia.service.BDLocalService"
					.equals(service.service.getClassName())) {
				return true;
				// System.out.println("存在该service");
			}
		}
		return false;
	}

	private void init() {
		m_tabHost = getTabHost();
		int count = Constant.mTabClassArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = m_tabHost.newTabSpec(Constant.mTextviewArray[i])
					.setIndicator(Constant.mTextviewArray[i])
					.setContent(getTabItemIntent(i));
			m_tabHost.addTab(tabSpec);
		}

		m_radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
		m_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_tab_index:
					m_tabHost.setCurrentTabByTag(Constant.mTextviewArray[0]);
					break;
				case R.id.main_tab_orderinfo:
					if (m_userid != null && !"".equals(m_userid)) {
						m_tabHost
								.setCurrentTabByTag(Constant.mTextviewArray[1]);
					} else {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								Login_Activity.class);
						startActivity(intent);
						finish();
					}
					break;
				case R.id.main_tab_baseinfo:
					if (m_userid != null && !"".equals(m_userid)) {
						m_tabHost
								.setCurrentTabByTag(Constant.mTextviewArray[2]);
					} else {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								Login_Activity.class);
						startActivity(intent);
						finish();
					}
					break;
				case R.id.main_tab_collect:
					if (m_userid != null && !"".equals(m_userid)) {
						m_tabHost
								.setCurrentTabByTag(Constant.mTextviewArray[3]);

					} else {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								Login_Activity.class);
						startActivity(intent);
						finish();
					}
					break;
				}
			}
		});

		((RadioButton) m_radioGroup.getChildAt(tabindex)).toggle();
	}

	private Intent getTabItemIntent(int index) {
		Intent intent = new Intent(this, Constant.mTabClassArray[index]);
		return intent;
	}

	private void initb() {
		Intent intent = getIntent();
		// m_usertype = intent.getStringExtra("MAP_USERTYPE");
		// m_userid = intent.getStringExtra("MAP_USERID");
		// m_username = intent.getStringExtra("MAP_USERNAME");
		// 根据传入index判断打开的tab
		tabindex = intent.getIntExtra("MAP_TABINDEX", 0);

		// 初始化相关参数
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);

		m_userid = sharedPreferences.getString("m_userid", "");
		m_usertype = sharedPreferences.getString("m_usertype", "");
		// m_username = sharedPreferences.getString("m_username", "");

	}

	private void givemessageandexit() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.icon, "醉无忧",
				System.currentTimeMillis());
		n.flags = Notification.FLAG_AUTO_CANCEL;

		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		// i.putExtra("MAP_USERID", iuserId);

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(getApplicationContext(), "醉无忧", "点击打开醉无忧！",
				contentIntent);
		nm.notify(100, n);
		finish();
	}

	/**
	 * 完全退出程序，需要增加权限！ <uses-permission
	 * android:name="android.permission.RESTART_PACKAGES"/> <uses-permission
	 * android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
	 */
	protected void exitProgram() {
		 ActivityManager activityManager = (ActivityManager)
		 getSystemService(ACTIVITY_SERVICE);
		 int sdk = Integer.valueOf(Build.VERSION.SDK).intValue();
		 if (sdk < 8) {
		 activityManager.restartPackage(getPackageName());
		 } else {
		 activityManager.killBackgroundProcesses(getPackageName());
		 }

		System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menuback:
			givemessageandexit();
			break;
		case R.id.changuser:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), Login_Activity.class);
			startActivity(intent);
			System.exit(0);
			break;
		case R.id.closeapp:
			exitProgram();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			
			exitProgram();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}