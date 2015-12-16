
package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;



public class LoginActivity extends Activity {
    private TextView userName;
    private TextView passWord;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (TextView) findViewById(R.id.userName);
        passWord = (TextView) findViewById(R.id.passWord);
        btn = (Button) findViewById(R.id.login);
//        跳转到登录界面
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                new Thread(runnable).start();
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
            Log.i("", string);
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            String str_json = "{" +
                    "\"TaskGuid\":\"ab8495db-3a4a-4f70-bb81-8518f60ec8bf\"," +
                    "\"DataType\":\"Query\"," +
                    "\"LoginAccount\":\""+userName.getText().toString()+"\"," +
                    "\"Password\":\""+passWord.getText().toString()+"\"," +
                    "}";
            request request = new request();
            SoapObject string = request.getResult(method, str_json);
            String jsonRequest = string.getProperty(0).toString();
            Log.e(">>>>>>>>>>",jsonRequest);
            if (jsonRequest.equals("True")){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
//            else if (jsonRequest.contains("ErrMessage")){
//                new AlertDialog.Builder(LoginActivity.this)
//                        .setTitle("用户名密码错误")
//                        .setPositiveButton("确定",null)
//                        .show();
//            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("value",string.toString());
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
}
