package com.red263.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;
import com.red263.commmodule.AsyNativePlace;
import com.red263.commmodule.CitysWidget;
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
import android.widget.Toast;

public class NativePlaceEdit_Activity extends Activity {

	private CitysWidget cityswidget;
	private Button subbutton;
	
	String usertype;
	String userid;
	String username;

	navtharead2 na2;
	

	private ProgressDialog proDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_place_edit_activity);
       
        na2=new navtharead2();
        findViews();       
        subbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!na2.isAlive()){
				na2.start();
				proDialog = ProgressDialog.show(NativePlaceEdit_Activity.this, "载入中..",
						"信息正在处理请稍后....", true, false);
				}
			}
		});


    }

    
    Handler navtharhad2=new Handler(){
    	public void handleMessage(Message msg) {
    		String state = msg.getData().getString("state");
    		
    		if("true".equals(state)){
    		//	MainActivity.instance.finish();
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
				
				Toast.makeText(NativePlaceEdit_Activity.this,
						"恭喜你，所在地修改成功!", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(NativePlaceEdit_Activity.this,
						"对不起，修改失败!", Toast.LENGTH_SHORT).show();
			}
    		
    	}
    };
    
    
    class navtharead2 extends Thread {
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void run() {
			// TODO Auto-generated method stub
			List params = new ArrayList();
			params.add(new BasicNameValuePair("userid", userid));
			params.add(new BasicNameValuePair("usertype", usertype));
			
			params.add(new BasicNameValuePair("province", cityswidget.getProvinceVal()));
			params.add(new BasicNameValuePair("city", cityswidget.getCityVal()));
			params.add(new BasicNameValuePair("area", cityswidget.getAreaVal()));
			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl
					.GetUrl(getApplication(),"changenavtiplace.php");
			String State = FunUtils.getJsonforPost(params, validateURL);
			
			
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("state", State);
			message.setData(bundle);
			navtharhad2.sendMessage(message);
			
		}
	};
    
    
	
	
	

    private void findViews(){
    	 init();
    	 cityswidget=(CitysWidget)findViewById(R.id.cityswidget);
    	 subbutton=(Button)findViewById(R.id.subbutton);
 		proDialog = ProgressDialog.show(NativePlaceEdit_Activity.this, "载入中..",
				"信息正在处理请稍后....", true, false);
    	 
    	 AsyNativePlace asnp=new AsyNativePlace(getApplication(), cityswidget,proDialog);
    	 asnp.execute("userinfo.php?id="+ userid+"&usertype="+usertype);    	 
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
