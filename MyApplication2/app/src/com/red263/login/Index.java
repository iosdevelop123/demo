package com.red263.login;

import com.red263.Edaijia.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
//启动界面首屏
public class Index extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_activity);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(intent);
                finish();
            }
                        
        }, 2500);
    }


}
