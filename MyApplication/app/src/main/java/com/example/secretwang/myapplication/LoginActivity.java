package com.example.secretwang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       Button btn = (Button) findViewById(R.id.login);
//        跳转到登录界面
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //


                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
              startActivity(intent);

            }
        });
    }
}
