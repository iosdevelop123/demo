
package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.telephony.TelephonyManager;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

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
    private CheckBox checkBox;
    static String YES = "yes";
    static String NO = "no";
    static String login, password;
    private String isMemory = "";//isMemory变量用来判断SharedPreferences有没有数据，包括上面的YES和NO
    private String FILE = "userInfo";//用于保存SharedPreferences的文件
    private SharedPreferences sp = null;//声明一个SharedPreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (TextView) findViewById(R.id.userName);
        passWord = (TextView) findViewById(R.id.passWord);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        btn = (Button) findViewById(R.id.login);
        sp = getSharedPreferences(FILE, MODE_PRIVATE);
        isMemory = sp.getString("isMemory", NO);
        //进入界面时，这个if用来判断SharedPreferences里面name和password有没有数据，有的话则直接打在EditText上面
        if (isMemory.equals(YES)){
            login = sp.getString("login","");
            password = sp.getString("password","");
            userName.setText(login);
            passWord.setText(password);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(login,userName.getText().toString());
        editor.putString(password,passWord.getText().toString());
        editor.commit();
        //button点击事件
        //触击登录按钮，执行remenber方法文本框里的信息重新写入SharedPreferences里面覆盖之前的，
        // 去除掉勾选框，触击登录按钮执行remenber方法就将之前保存到SharedPreferences的数据清除了
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = userName.getText().toString();
                password = passWord.getText().toString();
                remember();
                //开启线程
                new Thread(runnable).start();
                btn.setClickable(false);
                btn.setText("登陆中...");
                btn.setBackgroundResource(R.drawable.shape);
                //开启网络请求进度条
                progressDialog = ProgressDialog.show(LoginActivity.this, "", "正在加载,请稍候！");
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String s = telephonyManager.getDeviceId();
//        String s = "111";
        SharedPreferences driverId = getSharedPreferences("driverID",MODE_PRIVATE);
        SharedPreferences.Editor drivereditor = driverId.edit();
        drivereditor.putString("driver",s);
        drivereditor.commit();
    }
    // remenber方法用于判断是否记住密码，checkBox1选中时，提取出EditText里面的内容，
    // 放到SharedPreferences里面的login和password中
    public void remember(){
        if (checkBox.isChecked()){
//            if (sp == null){
//                sp=getSharedPreferences(FILE,MODE_PRIVATE);
//            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("login",userName.getText().toString());
            editor.putString("password",passWord.getText().toString());
            editor.putString("isMemory",YES);
            editor.commit();
        }else if (!checkBox.isChecked()){
            sp=getSharedPreferences(FILE,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("isMemory",NO);
            editor.commit();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
            //Log.v("66666666666",string);
            progressDialog.dismiss(); //关闭进度条
            if (string.equals("True")) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                SharedPreferences setting = getSharedPreferences("userInfo", MODE_PRIVATE);
//                SharedPreferences.Editor editor = setting.edit();
//                editor.putString("login", userName.getText().toString());
//                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (string.equals("输入字符串的格式不正确。")) {
                Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (string.equals("{\"ErrMessage\":\"用户名或密码错误\"}")) {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            } else if (string.equals("连接超时")) {
                Toast.makeText(LoginActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
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
            try {
                param.put("TaskGuid", "ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                param.put("DataType", "Query");
                param.put("LoginAccount", userName.getText().toString());
                param.put("Password", passWord.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String str_json = param.toString();
                request request = new request();
                SoapObject string = request.getResult(method, str_json);
                String jsonRequest = string.getProperty(0).toString();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("value", jsonRequest);
                message.setData(bundle);
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //左下角返回按钮点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("您确定退出e操盘吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //退出程序
                            // finish();
                            // 返回桌面操作
                             Intent home = new Intent(Intent.ACTION_MAIN);
                             home.addCategory(Intent.CATEGORY_HOME);
                             startActivity(home);
//                             onBackPressed();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .setCancelable(false)
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
