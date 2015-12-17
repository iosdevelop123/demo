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
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Login_Activity extends Activity {

	private String userType = "";
	private String userName = "";
	private String password = "";

	private EditText view_userName;
	private EditText view_password;
	private Button view_reg;
	private Spinner view_userType;
	private Button view_loginSubmit;
	private CheckBox view_rememberMe;

	private boolean statecheck = false;

	/** 初始化注册View组件 */
	private void findViewsById() {
		view_loginSubmit = (Button) findViewById(R.id.loginbutton);
		view_userName = (EditText) findViewById(R.id.editaccount);
		view_password = (EditText) findViewById(R.id.editpassword);
		view_reg = (Button) findViewById(R.id.reglink);
		view_rememberMe = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);

		// 设置spinner属性
		view_userType = (Spinner) this.findViewById(R.id.usertype);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, // 上下文对象
				R.array.login_type_array, // 在strings.xml中定义的那个数组
				android.R.layout.simple_spinner_item // 布局文件(Android自带的，定义下拉菜单是什么样子的)
				);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item // 定义每一个选项是什么样子
		);
		// 为Spinner绑定数据
		view_userType.setAdapter(adapter);
		view_userType.setPrompt("用户类型");
	}

	/** 用来操作SharePreferences的标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	/** 如果登录成功后,用于保存用户类型到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_USERTYPE = "MAP_LOGIN_USERTYPE";

	/** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";

	/** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";

	/** 如果登陆失败,这个可以给用户确切的消息显示,true是网络连接失败,false是用户名和密码错误 */
	private boolean isNetError;

	/** 登录loading提示框 */
	private ProgressDialog proDialog;

	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(Login_Activity.this,
						"登陆失败:\n1.请检查您网络连接.\n2.请联系我们.!", Toast.LENGTH_SHORT)
						.show();
			}
			// 用户名和密码错误
			else {
				if (statecheck) {
					Toast.makeText(Login_Activity.this,
							"该帐号尚未通过审核，请带上您的证件到公司开通!", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(Login_Activity.this, "登陆失败,用户密码错误!",
							Toast.LENGTH_SHORT).show();
					// 清除以前的SharePreferences密码
					clearSharePassword();
				}
			}
		}
	};

	/** 清除密码 */
	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
		share = null;
	}

	/**
	 * 初始化界面
	 * 
	 * @param isRememberMe
	 *            如果当时点击了RememberMe,并且登陆成功过一次,则saveSharePreferences(true,ture)后,
	 *            则直接进入
	 * */
	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		String userType = share.getString(SHARE_LOGIN_USERTYPE, "");
		String userName = share.getString(SHARE_LOGIN_USERNAME, "");
		String password = share.getString(SHARE_LOGIN_PASSWORD, "");

		if (!"".equals(userType)) {
			view_userType.setSelection(Integer.parseInt(userType));
		}
		if (!"".equals(userName)) {
			view_userName.setText(userName);
		}
		if (!"".equals(password)) {
			view_password.setText(password);
			view_rememberMe.setChecked(true);
		}
		// 如果密码也保存了,则直接让登陆按钮获取焦点
		if (view_password.getText().toString().length() > 0) {
			view_loginSubmit.requestFocus();
		}
		share = null;
	}

	/**
	 * 如果登录成功过,则将登陆用户名和密码记录在SharePreferences
	 * 
	 * @param saveUserName
	 *            是否将用户名保存到SharePreferences
	 * @param savePassword
	 *            是否将密码保存到SharePreferences
	 * */
	private void saveSharePreferences(boolean saveUserType,
			boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserType) {
			share.edit()
					.putString(SHARE_LOGIN_USERTYPE,
							view_userType.getSelectedItemId() + "").commit();
		}
		if (saveUserName) {
			share.edit()
					.putString(SHARE_LOGIN_USERNAME,
							view_userName.getText().toString()).commit();
		}
		if (savePassword) {
			share.edit()
					.putString(SHARE_LOGIN_PASSWORD,
							view_password.getText().toString()).commit();
		}
		
		share = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		findViewsById();
		initView(false);
		setListener();
	}

	/** 记住我的选项是否勾选 */
	private boolean isRememberMe() {
		if (view_rememberMe.isChecked()) {
			return true;
		}
		return false;
	}

	class LoginFailureHandler implements Runnable {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			userType = view_userType.getSelectedItemId() + "";
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();

			@SuppressWarnings("rawtypes")
			List params = new ArrayList();
			params.add(new BasicNameValuePair("usertype", userType));
			params.add(new BasicNameValuePair("username", userName));
			params.add(new BasicNameValuePair("password", password));

			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl
					.GetUrl(getApplication(),"login.php");
			String loginStateorid = validateLocalLogin(params, validateURL);


			// 判断登录是否成功
			if ("false".equals(loginStateorid)) {
				// 通过调用handler来通知UI主线程更新UI,
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			} else if ("statefalse".equals(loginStateorid)) {
				//根据审核状态提供反馈的信息的条件，为true说明因为为审核
				statecheck = true;
				// 通过调用handler来通知UI主线程更新UI,
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			} else {
					// 需要传输数据到登陆后的界面,
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					
					SharedPreferences sharedPreferences = getSharedPreferences("userdate", Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();//获取编辑器
					editor.putString("m_usertype", userType);
					editor.putString("m_userid", loginStateorid);
					editor.putString("m_username",userName);
					editor.commit();//提交修改	
					// 转向登陆后的页面
					startActivity(intent);
				proDialog.dismiss();
				finish();
			}
			// */
		}
	}

	/** 登录Button Listener */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			proDialog = ProgressDialog.show(Login_Activity.this, "连接中..",
					"连接中..请稍后....", true, true);
			// 开一个线程进行登录验证,主要是用于失败,成功可以直接通过startAcitivity(Intent)转向
			Thread loginThread = new Thread(new LoginFailureHandler());
			loginThread.start();
		}
	};

	/**
	 * 如果连接服务器超过5秒,也算连接失败.
	 * 
	 * @param params
	 *            参数
	 * @param validateUrl
	 *            网址
	 * */

	@SuppressWarnings("rawtypes")
	private String validateLocalLogin(List params, String validateUrl) {
		// 用于标记登陆状态
		String strResult = FunUtils.getJsonforPost(params, validateUrl);
		// */
		if ("false".equals(strResult)) {
			if (!isNetError) {
				clearSharePassword();
			}
			// */
		} else {
			if (isRememberMe()) {
				saveSharePreferences(true, true, true);
			} else {
				saveSharePreferences(true, true, false);
			}
		}
		if (!view_rememberMe.isChecked()) {
			clearSharePassword();
		}
		// */
		return strResult;
	}

	/** 设置监听器 */
	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_reg.setOnClickListener(registerLstener);
		view_rememberMe.setOnCheckedChangeListener(rememberMeListener);
	}

	/** 记住我checkBoxListener */
	private OnCheckedChangeListener rememberMeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (view_rememberMe.isChecked()) {
				Toast.makeText(Login_Activity.this, "如果登录成功,以后账号和密码会自动输入!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/** 注册Listener */
	private OnClickListener registerLstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Login_Activity.this, Reg_Activity.class);
			// 转向注册页面
			startActivity(intent);
		}
	};

}
