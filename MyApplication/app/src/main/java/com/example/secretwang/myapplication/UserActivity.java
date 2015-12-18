package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import java.util.ArrayList;
import java.util.HashMap;

public class UserActivity extends Activity {
    private Button btn_exit ;
    private ListView lv;
    private String Balance;
    String[] from={"item0","item1","item2"};  //这里是ListView显示内容每一列的列名
    //这里是ListView显示每一列对应的list_item中控件的id
    int[] to={R.id.user_item0,R.id.user_item1, R.id.user_item2};
    Object[] userItem0={R.drawable.keyongyue,R.drawable.gerenxinxi,R.drawable.shoushi};
    String[] userItem1={"可用余额","昵称","修改密码"}; //这里第一列所要显示的项目
    String[] userItem2={"0.00","nick",""};  //第二列
    ArrayList<HashMap<String,Object>> list=null;
    HashMap<String,Object> map=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //加载视图
        loadUI();
        //开启线程
        new Thread(runnable).start();
  }
    public void loadUI(){
        //创建ArrayList对象；
        list=new ArrayList<HashMap<String,Object>>();
        //将数据存放进ArrayList对象中，数据安排的结构是，ListView的一行数据对应一个HashMap对象，
        //HashMap对象，以列名作为键，以该列的值作为Value，将各列信息添加进map中，然后再把每一列对应
        //的map对象添加到ArrayList中
        for(int i=0; i<3; i++){
            map=new HashMap<String,Object>();
            map.put("item0", userItem0[i]);
            map.put("item1", userItem1[i]);
            map.put("item2", userItem2[i]);
            list.add(map);
        }
        //创建一个SimpleAdapter对象
        SimpleAdapter adapter=new SimpleAdapter(UserActivity.this,list,R.layout.list_user,from,to);
        //调用ListActivity的setListAdapter方法，为ListView设置适配器
        lv=(ListView) findViewById(R.id.userlistView);
        lv.setAdapter(adapter);
        //退出登录按钮
        btn_exit=(Button)findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("您确定退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
             // Log.e(">>>>>>>>>>", string);
            try {
                //将解析的字符串转换成json对象
                JSONObject jsonObject=new JSONObject(string);
                String money=jsonObject.getString("Balance");
                Balance = money + "$";
               // Log.e("=============",Balance);
                String[] userItem3={Balance,"nick"," "};
                list=new ArrayList<HashMap<String,Object>>();
                for(int i=0; i<3; i++) {
                    map = new HashMap<String, Object>();
                    map.put("item0", userItem0[i]);
                    map.put("item1", userItem1[i]);
                    map.put("item2", userItem3[i]);
                    list.add(map);
                }
                //创建一个SimpleAdapter对象
                SimpleAdapter adapter=new SimpleAdapter(UserActivity.this,list,R.layout.list_user,from,to);
                lv.setAdapter(adapter);
            }catch (JSONException e){
                e.printStackTrace();
            }
            }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            JSONObject param = new JSONObject();
            try{
                param.put("TaskGuid","ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                param.put("DataType","ClientRecord");
                param.put("LoginAccount","1317");
            }catch (JSONException e){
                e.printStackTrace();
            }
            String str_json=param.toString();
            request request = new request();
            SoapObject string = request.getResult(method, str_json);
            String jsonRequest = string.getProperty(0).toString();
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("value",jsonRequest);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
}
