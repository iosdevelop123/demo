
package com.example.secretwang.myapplication;

import android.app.Activity;

import android.app.ProgressDialog;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

public class LoginActivity extends Activity {
    private TextView userName;
    private TextView passWord;
    private Button btn;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (TextView) findViewById(R.id.userName);
        passWord = (TextView) findViewById(R.id.passWord);
        btn = (Button) findViewById(R.id.login);
        //button点击事件
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //开启线程
                    new Thread(runnable).start();
                    btn.setClickable(false);
                    btn.setText("登陆中...");
                    btn.setBackgroundResource(R.drawable.shape);
                    //开启网络请求进度条
                    progressDialog = ProgressDialog.show(LoginActivity.this, "","正在加载,请稍候！");
                }
            });

        }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
            //Log.v("66666666666",string);
            progressDialog.dismiss(); //关闭进度条
            if (string.equals("True")){
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                SharedPreferences setting = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = setting.edit();
                editor.putString("login",userName.getText().toString());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else if (string.equals("输入字符串的格式不正确。")){
                Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
            }else if (string.equals("{\"ErrMessage\":\"用户名或密码错误\"}")){
                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            }else if (string.equals("连接超时")){
                Toast.makeText(LoginActivity.this,"请求超时",Toast.LENGTH_SHORT).show();
            }
            btn.setText("登录");
            btn.setClickable(true);
            btn.setBackgroundResource(R.drawable.shape);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            JSONObject param = new JSONObject();
            try{
                param.put("TaskGuid","ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                param.put("DataType","Query");
                param.put("LoginAccount",userName.getText().toString());
                param.put("Password",passWord.getText().toString());
            }catch (JSONException e){
                e.printStackTrace();
            }
            try{
                String str_json=param.toString();
                request request = new request();
                SoapObject string = request.getResult(method, str_json);
                String jsonRequest = string.getProperty(0).toString();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("value", jsonRequest);
                message.setData(bundle);
                handler.sendMessage(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
