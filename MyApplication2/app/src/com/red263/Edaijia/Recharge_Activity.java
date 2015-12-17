package com.red263.Edaijia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Recharge_Activity extends Activity {

	private EditText editbalance;
	Button nextbutton;
	private String balance;
	
	private String userid ;
	private String usertype;
	private String username;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_activity);
        init();
        this.editbalance=(EditText)findViewById(R.id.editbalance);
        this.nextbutton=(Button)findViewById(R.id.nextbutton);
        
        
        editbalance.addTextChangedListener(new TextWatcher() {
	          
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub
                // 得到文字，将其显示到TextView中
            	balance=editbalance.getText().toString();
            }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
             
        });
        balance=this.editbalance.getText().toString();
        
        nextbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("MAP_BALANCE",balance);		
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), Alipay_Login_Activity.class);
				startActivity(intent);
				finish();
			}
		});
        
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
