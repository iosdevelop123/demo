package com.red263.login;

import com.red263.Edaijia.Activity1;
import com.red263.Edaijia.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Reg_Activity_result extends Activity {

	private Button stateBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity_result);
        stateBtn=(Button)findViewById(R.id.statebutton);
        stateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						Activity1.class);
				// 转向注册页面
				startActivity(intent);
			}
		});
    }


}
