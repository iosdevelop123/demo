package com.red263.Edaijia;

import com.red263.Edaijia.R;
import com.red263.commmodule.AsynUserActivity;
import com.red263.login.NativePlaceEdit_Activity;
import com.red263.login.PasswordChange_Activity;
import com.red263.login.UserInfo_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity3 extends Activity {

	private TextView editusertype;
	private TextView editusername;
	private TextView editnativace;
	private TextView editrealname;
	private TextView editmail;
	private TextView edittel;
	private TextView safeteltext;
	private TextView editsafetel;
	private TextView editisvip;
	private Button EditBtn;

	private Button chanagepassBtn;
	private Button editnavplaceBtn;
	private Button UpimgBtn;
	private Button IsvipBtn;

	LinearLayout safetellayout;
	String driverid;
	String usertype;
	String userid;
	String username;
	private ProgressDialog proDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3_layout);
		init();
		proDialog = ProgressDialog.show(Activity3.this, "载入中..",
				"信息正在处理请稍后....", true, false);
		AsynUserActivity asa = new AsynUserActivity(getApplication(), usertype,
				editusertype, editusername, editnativace, editrealname,
				editmail, edittel, safeteltext, editsafetel, safetellayout,
				editisvip, proDialog);
		asa.execute("userinfo.php?id=" + userid + "&usertype=" + usertype);

		EditBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						UserInfo_Activity.class);
				startActivity(intent);
				// finish();
			}
		});

		chanagepassBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						PasswordChange_Activity.class);
				startActivity(intent);
				// finish();
			}
		});
		editnavplaceBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						NativePlaceEdit_Activity.class);
				startActivity(intent);
				// finish();
			}
		});

		UpimgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Upload_Activity.class);
				startActivity(intent);
			}
		});
		IsvipBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						Recharge_Activity.class);
				startActivity(intent);
			}
		});
	}

	private void findView() {

		editusertype = (TextView) findViewById(R.id.editusertype);
		editusername = (TextView) findViewById(R.id.editusername);
		editnativace = (TextView) findViewById(R.id.editnativace);
		editrealname = (TextView) findViewById(R.id.editrealname);
		editmail = (TextView) findViewById(R.id.editmail);
		edittel = (TextView) findViewById(R.id.edittel);
		//
		safetellayout = (LinearLayout) findViewById(R.id.safetellayout);
		safeteltext = (TextView) findViewById(R.id.safeteltext);
		editsafetel = (TextView) findViewById(R.id.editsafetel);

		editisvip = (TextView) findViewById(R.id.editvip);

		EditBtn = (Button) findViewById(R.id.editbutton);

		chanagepassBtn = (Button) findViewById(R.id.chanagepassBtn);
		editnavplaceBtn = (Button) findViewById(R.id.editnavplaceBtn);

		UpimgBtn = (Button) findViewById(R.id.UpimgBtn);

		IsvipBtn = (Button) findViewById(R.id.isvipBtn);
		if ("1".equals(usertype)) {
			IsvipBtn.setVisibility(View.GONE);
		}
	}

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
		findView();
	}

}