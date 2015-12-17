package com.red263.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;
import com.red263.commmodule.AsynUserInfoEditBind;
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

public class UserInfo_Activity extends Activity {

	TextView editusertype;
	TextView editusername;
	EditText editrealname;
	EditText editmail;
	EditText edittel;
	EditText editsafetel;
	Button subtbutton;
	//
	String usertype;
	String userid;
	String username;
	userinfoedit u1;

	private ProgressDialog proDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);
        findViews();
        u1=new userinfoedit();
        this.subtbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!u1.isAlive()){
				u1.start();

				proDialog = ProgressDialog.show(UserInfo_Activity.this, "载入中..",
						"信息正在处理请稍后....", true, false);
				}

			}
		});
    }

    private void findViews(){
    	init();
    	editusertype=(TextView)findViewById(R.id.editusertype);
    	editusername=(TextView)findViewById(R.id.editusername);
    	editrealname=(EditText)findViewById(R.id.editrealname);
    	editmail=(EditText)findViewById(R.id.editmail);
    	edittel=(EditText)findViewById(R.id.edittel);
    	editsafetel=(EditText)findViewById(R.id.editsafetel);
    	subtbutton=(Button)findViewById(R.id.subtbutton);
    	if("1".equals(usertype)){
    		editusertype.setText("驾驶员");
    	}else{
    		editusertype.setText("普通用户");
    	}
    	this.editusername.setText(username);
    	//bind();
    	proDialog = ProgressDialog.show(UserInfo_Activity.this, "载入中..",
				"信息正在处理请稍后....", true, false);
    	AsynUserInfoEditBind asu=new AsynUserInfoEditBind(getApplication(), editrealname, editmail, edittel, editsafetel,proDialog);
    	asu.execute("userinfo.php?id="+ userid+"&usertype="+usertype);
    }
    

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}
	
	
	Handler userinfoeditHanlder=new Handler(){
		public void handleMessage(Message msg) {
			String state = msg.getData().getString("state");
			if ("true".equals(state)){
			  //  MainActivity.instance.finish();
				// 需要传输数据到登陆后的界面,
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putInt("MAP_TABINDEX", 2);
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(),
						MainActivity.class);
				// 转向登陆后的页面
				startActivity(intent);
				if(proDialog!=null){
					proDialog.dismiss();
				}
				
				Toast.makeText(UserInfo_Activity.this,
						"恭喜你，用户信息修改成功!", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				if(proDialog!=null){
					proDialog.dismiss();
				}
				Toast.makeText(UserInfo_Activity.this,
						"对不起，修改失败!", Toast.LENGTH_SHORT).show();
			}
		
		}
	};
	
	class  userinfoedit extends Thread{
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();
			params.add(new BasicNameValuePair("userid", userid));
			params.add(new BasicNameValuePair("usertype", usertype));
			
			params.add(new BasicNameValuePair("realname",editrealname.getText().toString()));
			params.add(new BasicNameValuePair("mail",editmail.getText().toString()));
			params.add(new BasicNameValuePair("tel", edittel.getText().toString()));
			params.add(new BasicNameValuePair("safetel", editsafetel.getText().toString()));
			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl
					.GetUrl(getApplication(),"changeuserinfo.php");
			String State = FunUtils.getJsonforPost(params, validateURL);
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			userinfoeditHanlder.sendMessage(message);
		}
	};

}
