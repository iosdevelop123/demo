package com.red263.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;
import com.red263.commmodule.FunUtils;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordChange_Activity extends Activity {

	TextView editusername;
	EditText editoldpassword;

	EditText editpassword;
	EditText editrpassword;
	Button subtbutton;

	String usertype;
	String userid;
	String username;
	updatepasswordt t;
	private ProgressDialog proDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_change_activity);
		findViews();

		subtbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if (checkpassword()) {
				if (editpassword.getText().toString()
						.equals(editrpassword.getText().toString())) {
					t = new updatepasswordt();
					if (!t.isAlive()) {
						t.start();
						proDialog = ProgressDialog.show(
								PasswordChange_Activity.this, "载入中..",
								"信息正在处理请稍后....", true, false);
					}
					t.setStartState();

				} else {
					Toast.makeText(PasswordChange_Activity.this,
							"密码修改失败，2个新密码不一致!", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	Handler updatepasswordh = new Handler() {
		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");
			if ("true".equals(state)) {
			//	MainActivity.instance.finish();

				// 需要传输数据到登陆后的界面,
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putInt("MAP_TABINDEX", 2);
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), MainActivity.class);
				// 转向登陆后的页面
				startActivity(intent);
				t.setStopState();
				if (proDialog != null) {
					proDialog.dismiss();
				}

				Toast.makeText(PasswordChange_Activity.this, "恭喜你，密码修改成功!",
						Toast.LENGTH_SHORT).show();
				finish();

			} else if ("oldpasswordfalse".equals(state)) {



				if (proDialog != null) {
					proDialog.dismiss();
				}
				
				t.setStopState();
				Toast.makeText(PasswordChange_Activity.this, "原密码错误",
						Toast.LENGTH_SHORT).show();

			} else if ("false".equals(state)) {
				if (proDialog != null) {
					proDialog.dismiss();
				}

				t.setStopState();
				Toast.makeText(PasswordChange_Activity.this, "修改密码失败",
						Toast.LENGTH_SHORT).show();
			}

			// System.out.println("修改状态2" + state);
		}
	};

	class updatepasswordt extends Thread {
		private boolean isRun = false;

		public updatepasswordt() {
			isRun = false;
		}

		public void setStopState() {
			isRun = false;
		}

		public void setStartState() {
			isRun = true;
		}

		public boolean getrunstate() {
			return isRun;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				if (isRun == true) {
					List params = new ArrayList();
					params.add(new BasicNameValuePair("userid", userid));
					params.add(new BasicNameValuePair("usertype", usertype));
					params.add(new BasicNameValuePair("oldpassword",
							editoldpassword.getText().toString()));
					params.add(new BasicNameValuePair("password", editpassword
							.getText().toString()));
					// 这里换成你的验证地址
					String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
							getApplication(), "changepassword.php");
					String State = FunUtils.getJsonforPost(params, validateURL);

					// System.out.println("修改状态"+State);

					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("state", State);
					message.setData(bundle);
					updatepasswordh.sendMessage(message);
				}
			}
		}
	};

	private void findViews() {
		init();
		editusername = (TextView) findViewById(R.id.editusername);
		editoldpassword = (EditText) findViewById(R.id.editoldpassword);
		editpassword = (EditText) findViewById(R.id.editpassword);
		editrpassword = (EditText) findViewById(R.id.editrpassword);
		//
		subtbutton = (Button) findViewById(R.id.subtbutton);
		this.editusername.setText(username);
	}

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}

}
