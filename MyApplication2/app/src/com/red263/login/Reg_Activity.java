package com.red263.login;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;

import com.red263.Edaijia.R;
import com.red263.commmodule.FunUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Reg_Activity extends Activity {

	private Spinner usertype;
	private EditText editusername;	
	private EditText editpassword;
	private EditText editrpassword;
	private EditText editrealname;
	private EditText editmail;
	private EditText edittel;
	private Button nextbtn;

	public String usertypeval;
	private String editusernameval;
	private String editpasswordval;
	private String editrpasswordval;
	private String editrealnameval;
	private String editmailval;
	private String edittelval;

	public StringBuilder suggest;

 	public boolean checkname = false;
	regactivet regt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_activity);
		regt= new regactivet();
		if (!regt.isAlive()) {
			
			regt.start();
		//	regt.setStartState();
		}
		findview();
		nextbtn.setOnClickListener(nextonclickLister);

		editmail.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!"".equals(editmail.getText().toString())) {
					String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
					String email = editmail.getText().toString();
					Pattern p = Pattern.compile(strPattern);
					Matcher m = p.matcher(email);
					if (!m.matches()) {
						Toast.makeText(getApplicationContext(), "邮箱格式错误，请重输！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void findview() {
		usertype = (Spinner) findViewById(R.id.usertype);
		editusername = (EditText) findViewById(R.id.editusername);
		editpassword = (EditText) findViewById(R.id.editpassword);
		editrpassword = (EditText) findViewById(R.id.editrpassword);
		editrealname = (EditText) findViewById(R.id.editrealname);
		editmail = (EditText) findViewById(R.id.editmail);
		edittel = (EditText) findViewById(R.id.edittel);
		nextbtn = (Button) findViewById(R.id.nextbutton);

		// 设置spinner属性
		usertype = (Spinner) this.findViewById(R.id.usertype);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, // 上下文对象
				R.array.login_type_array, // 在strings.xml中定义的那个数组
				android.R.layout.simple_spinner_item // 布局文件(Android自带的，定义下拉菜单是什么样子的)
				);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item // 定义每一个选项是什么样子
		);
		// 为Spinner绑定数据
		usertype.setAdapter(adapter);
		usertype.setPrompt("用户类型");
		// */
	}

	private void getVal() {
		usertypeval = usertype.getSelectedItemId() + "";
		editusernameval = editusername.getText().toString();
		editpasswordval = editpassword.getText().toString();
		editrpasswordval = editrpassword.getText().toString();
		editrealnameval = editrealname.getText().toString();
		editmailval = editmail.getText().toString();
		edittelval = edittel.getText().toString();
		
		validateForm(editusernameval, editmailval, edittelval, editpasswordval,
				editrpasswordval);
	}

	// 设置下一步的按钮事件
	private OnClickListener nextonclickLister = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getVal();
			if (suggest.length() == 0) {
				Intent intent = new Intent();
				intent.putExtra("MAP_USERTYPE", usertypeval);
				intent.putExtra("MAP_USERNAME", editusernameval);
				intent.putExtra("MAP_USERPASSWORD", editpasswordval);
				intent.putExtra("MAP_USERREALNAME", editrealnameval);
				intent.putExtra("MAP_USERMAIL", editmailval);
				intent.putExtra("MAP_USERTEL", edittelval);
				// */
				intent.setClass(Reg_Activity.this, Reg_Activity2.class);
				startActivity(intent);
			}
		}
	};

	/** 验证注册表单 */
	private void validateForm(String userName, String email, String tel,
			String password, String password2) {
		suggest = new StringBuilder();
	 	regt.setStartState();
		// 检测用户名是否重复
		if (!checkname) {
			suggest.append(getText(R.string.suggust_namecheck) + "\n");
		}
		if (userName.length() < 1) {
			suggest.append(getText(R.string.suggust_name) + "\n");
		}
		if (password.length() < 1 || password2.length() < 1) {
			suggest.append(getText(R.string.suggust_pass) + "\n");
		}
		if (!password.equals(password2)) {
			suggest.append(getText(R.string.suggust_passcheck));
		}
		if (email.length() < 1) {
			suggest.append(getText(R.string.suggust_email) + "\n");
		}
		if (tel.length() < 1) {
			suggest.append(getText(R.string.suggust_tel) + "\n");
		}

		if (suggest.length() > 0) {
			Toast.makeText(this, suggest.subSequence(0, suggest.length() - 1),
					Toast.LENGTH_SHORT).show();
		}
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// private boolean checkname(){
	// List params = new ArrayList();
	// params.add(new BasicNameValuePair("usertype", usertypeval));
	// params.add(new BasicNameValuePair("username", editusernameval));
	//
	// // 这里换成你的验证地址
	// String validateURL = com.red263.commmodule.LinkUrl
	// .GetUrl(getApplication(),"checkname.php");
	//
	// String checkState = FunUtils.getJsonforPost(params, validateURL);
	// if("false".equals(checkState)){
	// return false;
	// }else{
	// return true;
	// }
	// }

	Handler regactiveh = new Handler() {
		public void handleMessage(Message msg) {
			String State = msg.getData().getString("state");
			if("true".equals(State)){
				 checkname=true;
				 regt.setStopState();

			}else{
				 checkname=false;
				 regt.setStopState();
			}
		}
	};


	class regactivet extends Thread {
		private boolean isRun = false;

		public regactivet() {
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
				if (isRun == true&&editusernameval!=null&&editusernameval!="") {
				//	System.out.println("输出"+isRun);
					// 这里换成你的验证地址
					List params = new ArrayList();
					params.add(new BasicNameValuePair("usertype", usertypeval));
					params.add(new BasicNameValuePair("username",
							editusernameval));
					String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
							getApplication(), "checkname.php");
					String checkState = FunUtils.getJsonforPost(params,
							validateURL);

					 Message message = new Message();
					 Bundle bundle = new Bundle();
					 bundle.putString("state", checkState);
					 message.setData(bundle);
					 regactiveh.sendMessage(message);
				}
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//关闭该页面获取数据的线程
		android.os.Process.killProcess(android.os.Process.myPid()) ;
		super.onDestroy();
	}
}
