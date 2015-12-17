package com.red263.Edaijia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Pay_ok_Activity extends Activity {

	TextView info1;
	Button okBtn;
	
	String balance;
	String alipayname;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_ok_activity);
        info1=(TextView)findViewById(R.id.info1);
        okBtn=(Button)findViewById(R.id.okbutton);
        
    	Intent intent = getIntent();
		balance = intent.getStringExtra("MAP_BALANCE");
		alipayname = intent.getStringExtra("MAP_ALIPYNAME");
		
		this.info1.setText("你已经成功付款"+balance+"元");
		this.okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				Bundle bundle = new Bundle();
				// 传递id给主截面判断应该载入那个tab
				bundle.putInt("MAP_TABINDEX", 2);
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
    }

}
