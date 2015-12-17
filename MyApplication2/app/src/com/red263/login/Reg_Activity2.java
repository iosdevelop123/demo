package com.red263.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.red263.Edaijia.R;
import com.red263.commmodule.CitysWidget;
import com.red263.commmodule.FunUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Reg_Activity2 extends Activity {

	private CitysWidget citywidget;
	private Button regBtn;
	private RelativeLayout driveragelayout;
	private RelativeLayout safetellayout;
	private EditText editdriverage;
	private EditText safetel;

	//上页传递来的数据
	private String usertypeval;
	private String editusernameval;
	private String editpasswordval;
	private String editrealnameval;
	private String editmailval;
	private String edittelval;
	//本页获取到的数据
	private String province;
	private String city;
	private String area;
	private String driverage;
	private String safetelval;
	
	
	/** 用来操作SharePreferences的标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	/** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";

	/** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";

	/** 如果登陆失败,这个可以给用户确切的消息显示,true是网络连接失败,false是用户名和密码错误 */
	private boolean isNetError;
	/** 注册loading提示框 */
	private ProgressDialog proDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_activity2);
		
		
		findView();
		regBtn.setOnClickListener(submitListener);
		
	}
	
	private void findView(){
		citywidget = (CitysWidget) findViewById(R.id.cityswidget);
		regBtn = (Button) findViewById(R.id.regbutton);
		driveragelayout = (RelativeLayout) findViewById(R.id.driveragelayout);
		editdriverage =(EditText)findViewById(R.id.editdriverage);
		safetellayout=(RelativeLayout)findViewById(R.id.safetellayout);
		safetel=(EditText)findViewById(R.id.editsafetel);
		getinfo();
	}
	
	private void getinfo(){
		Intent intent=getIntent(); 
		usertypeval=intent.getStringExtra("MAP_USERTYPE"); 
		if("0".equals(usertypeval)){
			//如果是普通用户则不显示驾龄内容
			driveragelayout.setVisibility(View.GONE);
		}else{
			//如果是驾驶员就不显示安全联系号码
			safetellayout.setVisibility(View.GONE);
		}
		editusernameval=intent.getStringExtra("MAP_USERNAME");
		editrealnameval=intent.getStringExtra("MAP_USERREALNAME");
		editpasswordval=intent.getStringExtra("MAP_USERPASSWORD"); 
        editmailval=intent.getStringExtra("MAP_USERMAIL"); 
        edittelval=intent.getStringExtra("MAP_USERTEL");
        province=citywidget.getProvinceVal();

	}
	
	//获取本页的数据，必须放在按钮事件中除法
	private void getinfonowpage(){
        city=citywidget.getCityVal();
        area=citywidget.getAreaVal();   
        //判断注册类型是否为驾驶员，如果是就获取驾龄文本框的值
        if("1".equals(usertypeval)){
        driverage=editdriverage.getText().toString();
        }
        if("0".equals(usertypeval)){
        safetelval=safetel.getText().toString();
        }
	}
	
	/** 登录后台通知更新UI线程,主要用于登录失败,通知UI线程更新界面 */
	Handler registerHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			else if (isNetError) {
				Toast.makeText(getApplicationContext(), "注册失败:\n1.请检查您网络连接.\n2.请联系我们.!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	

	/** 监听注册确定按钮 */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
				proDialog = ProgressDialog.show(Reg_Activity2.this, "注册中..",
						"连接中..请稍后....", true, true);
				// 开一个线程进行注册,主要是用于失败,成功可以直接通过startAcitivity(Intent)转向
				Thread registerThread = new Thread(new RegisterHandler());
				registerThread.start();
		}
	};

	class RegisterHandler implements Runnable {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			getinfonowpage();
			//提交后获取值
			@SuppressWarnings("rawtypes")
			List params = new ArrayList();
			params.add(new BasicNameValuePair("usertype", usertypeval));
			params.add(new BasicNameValuePair("username", editusernameval));
			params.add(new BasicNameValuePair("realname", editrealnameval));
			params.add(new BasicNameValuePair("password", editpasswordval));
			params.add(new BasicNameValuePair("mail", editmailval));
			params.add(new BasicNameValuePair("tel", edittelval));
			params.add(new BasicNameValuePair("province", province));
			params.add(new BasicNameValuePair("city", city));
			params.add(new BasicNameValuePair("area", area));
			//获取用户类型
			if("1".equals(usertypeval)){
				params.add(new BasicNameValuePair("driverage", driverage));		
			}
			
			//获取用户安全联系电话号码
			if("0".equals(usertypeval)){
				params.add(new BasicNameValuePair("safetel", safetelval));		
			}
			
			
			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl
					.GetUrl(getApplication(),"reg.php");
			
			String registerState = validateLocalRegister(params, validateURL);

			// 注册成功
			if (!"false".equals(registerState)) {
				if("1".equals(usertypeval)){
					//但类型为驾驶员时就跳转到通知界面
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							Reg_Activity_result.class);
					// 转向注册页面
					startActivity(intent);
				}else{
					// 当类型为普通用几乎时就直接登录
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), Login_Activity.class);
					Bundle bundle = new Bundle();
					intent.putExtras(bundle);
					// 转向登陆后的页面
					startActivity(intent);
				}
				proDialog.dismiss();
			} else {
				// 通过调用handler来通知UI主线程更新UI,
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				message.setData(bundle);
				registerHandler.sendMessage(message);
			}
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private String validateLocalRegister(List params, String validateUrl) {
		// 用于标记登陆状态
				String strResult =FunUtils.getJsonforPost(params, validateUrl);
				if (!strResult.equals("false")) {
					saveSharePreferences(true, true);
			    } 
				return strResult;
	}
	
	/**
	 * 如果成功注册,则将登陆用户名和密码记录在SharePreferences
	 * 
	 * @param saveUserName
	 *            是否将用户名保存到SharePreferences
	 * @param savePassword
	 *            是否将密码保存到SharePreferences
	 * */
	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserName) {
 			share.edit().putString(SHARE_LOGIN_USERNAME,editusernameval).commit();
		}
		if (savePassword) {
			share.edit().putString(SHARE_LOGIN_PASSWORD,editpasswordval).commit();
		}
		share = null;
	}
	
}
