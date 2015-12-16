package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class UserActivity extends Activity {
    private Button button_back = null;
    private Button button_exit = null;
    private  static final String[] strs = new String[] {
            "可用余额","昵称","修改密码"
    };//定义一个String数组用来显示ListView的内容
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);
//       button_back=(Button)findViewById(R.id.btn_back);
//        button_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        button_exit=(Button)findViewById(R.id.btn_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("您确定退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                     Intent intent = new Intent(UserActivity.this,LoginActivity.class);
                                      startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        lv=(ListView) findViewById(R.id.userlistView);
        //Adapter来绑定数据
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strs));
    }
}
