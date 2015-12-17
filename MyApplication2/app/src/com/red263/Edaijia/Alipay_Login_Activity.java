package com.red263.Edaijia;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
 
public class Alipay_Login_Activity extends Activity {

	Button nextBtn;
	String balance;
	String alipaynameval;
	EditText alipayname;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_login);
        nextBtn=(Button)findViewById(R.id.loginbutton);
        alipayname=(EditText)findViewById(R.id.editpayname);
        alipaynameval=alipayname.getText().toString();
        
        Intent intent = getIntent();
        balance = intent.getStringExtra("MAP_BALANCE");
        alipayname.addTextChangedListener(new TextWatcher() {
        	          
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub
                // 得到文字，将其显示到TextView中
            	alipaynameval=alipayname.getText().toString();
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
        
        nextBtn.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("MAP_BALANCE",balance);
				bundle.putString("MAP_ALIPAYNAME",alipaynameval);
		        
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), PayResoult_Activity.class);
				startActivity(intent);
				finish();
			}
		});
    }
 
}
