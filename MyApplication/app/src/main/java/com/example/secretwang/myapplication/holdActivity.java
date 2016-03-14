package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class holdActivity extends Activity {
    private Button historyButton;
    private ListView listView = null;
    private TextView priceTextView = null;
    private String loginStr;//登录名
    private ProgressDialog progressDialog;
    private List<Map<String, Object>> list = new ArrayList<>();
    private String no = "true";//判断是否全部平仓
    private List l_name = new ArrayList();//存储名字
    private String Ip;//手机ip地址
    private Timer timer;//定时器
    private Boolean isFirst;
    private String driverId;
    private request request = new request();//数据请求
    private Bundle bundle = new Bundle();//
    private String name1;
    private String name2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hold);
        SharedPreferences sharedPreferences =getSharedPreferences("userInfo",MODE_PRIVATE);
        loginStr = sharedPreferences.getString("login", "");
        //开启网络请求进度条
//        获取android唯一标识
        SharedPreferences driver =getSharedPreferences("driverID", MODE_PRIVATE);
        driverId = driver.getString("driver","");
        SharedPreferences IP = getSharedPreferences("IP",MODE_PRIVATE);
        Ip = IP.getString("IP", "");
        SharedPreferences name = getSharedPreferences("name", MODE_PRIVATE);
        name1 = name.getString("itemName","");
        name2 = name.getString("itemName2","");
        progressDialog = ProgressDialog.show(holdActivity.this, "", "正在加载,请稍候！");
        isFirst = true;
        new Thread(runnable).start();
        createUI();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void dingshiqi(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String method = "TransformData";
                JSONObject parma = new JSONObject();
                try {
                    parma.put("TaskGuid","ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                    parma.put("DriverID",driverId);
                    parma.put("DataType","ClientOpenTrades");
                    parma.put("LoginAccount",loginStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String str_json = parma.toString();
                SoapObject soapObject = request.getResult(method, str_json);
                List list = data(soapObject);
                Message message = new Message();
                bundle.putString("key",list.toString());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }, 1000, 5000);
    }


    private void createUI() {
        //        持仓
        listView = (ListView) findViewById(R.id.listView_holdList);
        priceTextView = (TextView) findViewById(R.id.textView12);
//        历史纪录Button
        historyButton = (Button) findViewById(R.id.button_history);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holdActivity.this, historyActivity.class);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("key");
            if (string.equals("[连接超时]")){
                if (isFirst) {
                    Toast.makeText(holdActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                    isFirst = false;
                }
            }else if (string.equals("[]")){
                list.removeAll(list);
                priceTextView.setText("0"); 
                if (isFirst){
                    Toast.makeText(holdActivity.this, "没有订单", Toast.LENGTH_SHORT).show();
                    isFirst = false;
                }
            }else {
                list.removeAll(list);
                l_name.removeAll(l_name);
                int price = 0;
                try {
                    JSONArray jsonArray = new JSONArray(string);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String s = null;
                        if (jsonObject.getString("Symbol").equals(name1)){
                            s = "美原油";
                        }else if (jsonObject.getString("Symbol").equals(name2)){
                            s = "恒生指数";
                        }
                        map.put("textView_name", s);
                        map.put("textView_buyNum", jsonObject.getString("Volume"));
                        map.put("textView_counterFee", jsonObject.getString("Commission"));
                        map.put("textView_buyMoreOrLess", jsonObject.getString("TypeName"));
                        map.put("textView_price", jsonObject.getString("Profit"));
                        map.put("textView_openPrice", jsonObject.getString("OpenPrice"));
                        map.put("textView_closePrice", jsonObject.getString("ClosePrice"));
                        map.put("textView_OrderNumber", jsonObject.getString("OrderNumber"));
                        price += jsonObject.getInt("Profit");
                        list.add(map);
                        l_name.add(jsonObject.getString("Symbol"));
                    }
                    if (price < 0) {
                        priceTextView.setTextColor(Color.parseColor("#0069d5"));
                    } else {
                        priceTextView.setTextColor(Color.parseColor("#fe0000"));
                    }
                    priceTextView.setText(String.valueOf(price));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listView.setAdapter(new HoldAdspter(getApplicationContext(), list));
            progressDialog.dismiss(); //关闭进度条
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           dingshiqi();
        }
    };
    private List data(SoapObject soapObject) {
        List list = new ArrayList();
        String string = soapObject.getProperty(0).toString();
        if (string.equals("连接超时")){
            list.add("连接超时");
        }else if (string.equals("[]")){list.add("");} else{
            try {
                JSONArray jsonArray = new JSONArray(string);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    map.put("Symbol", jsonObject.getString("Symbol"));
                    map.put("Volume", jsonObject.getInt("Volume"));
                    map.put("Commission", jsonObject.getInt("Commission"));
                    map.put("TypeName", "看" + jsonObject.getString("TypeName"));
                    map.put("Profit", jsonObject.getString("Profit"));
                    map.put("OpenPrice", jsonObject.getString("OpenPrice"));
                    map.put("ClosePrice", jsonObject.getString("ClosePrice"));
                    map.put("OrderNumber", jsonObject.getString("OrderNumber"));
                    list.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


//    自定义listViewItem的显示
    public class HoldAdspter extends BaseAdapter {
        private List<Map<String,Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        private String orderNum;//买到的货币的订单号
        private String buyNum;//买到的货币的手数
        private int posi;//得到在listView的哪一行
        protected HoldAdspter(Context context, List<Map<String, Object>> data){
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }
        public final class Hold {
            public TextView textView_name;
            public TextView textView_buyMoreOrLess;
            public TextView textView_buyNum;
            public TextView textView_counterFee;
            public TextView textView_price;
            public TextView textView_openPrice;
            public TextView textView_closePrice;
            public Button button_sell;
            public TextView textView_OrderNumber;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        /**
         * 获得唯一标识
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override public View getView(final int position,View contextView,ViewGroup parent){
            Hold hold = null;
            if (contextView == null){
                hold = new Hold();
                contextView = layoutInflater.inflate(R.layout.list,null);
                hold.textView_name = (TextView)contextView.findViewById(R.id.textView);
                hold.textView_buyMoreOrLess = (TextView)contextView.findViewById(R.id.textView14);
                hold.textView_buyNum = (TextView)contextView.findViewById(R.id.textView_buyNum);
                hold.textView_counterFee = (TextView)contextView.findViewById(R.id.textView_counterFee);
                hold.textView_price = (TextView)contextView.findViewById(R.id.textView_price);
                hold.textView_openPrice = (TextView) contextView.findViewById(R.id.textView_openPrice);
                hold.textView_closePrice = (TextView) contextView.findViewById(R.id.textView_closePrice);
                hold.textView_OrderNumber = (TextView) contextView.findViewById(R.id.OrderNumber);
                hold.button_sell = (Button)contextView.findViewById(R.id.button_sell);
                contextView.setTag(hold);
            }else {
                hold = (Hold)contextView.getTag();
            }
//        得到数据
            hold.textView_name.setText((String)data.get(position).get("textView_name"));
            hold.textView_buyMoreOrLess.setText((String)data.get(position).get("textView_buyMoreOrLess"));
            hold.textView_buyNum.setText((String)data.get(position).get("textView_buyNum"));
            hold.textView_counterFee.setText((String)data.get(position).get("textView_counterFee"));
            hold.textView_price.setText((String)data.get(position).get("textView_price"));
            hold.textView_openPrice.setText((String)data.get(position).get("textView_openPrice"));
            hold.textView_closePrice.setText((String)data.get(position).get("textView_closePrice"));
            hold.textView_OrderNumber.setText((String)data.get(position).get("textView_OrderNumber"));
//        判断是看多还是看空，显示背景颜色
            if (hold.textView_buyMoreOrLess.getText().toString().equals("看多")) {
//                hold.textView_buyMoreOrLess.setBackgroundColor(Color.parseColor("#7c0000"));
                hold.textView_buyMoreOrLess.setBackgroundResource(R.drawable.shape2);
            }else {
//                hold.textView_buyMoreOrLess.setBackgroundColor(Color.parseColor("#333399"));
                hold.textView_buyMoreOrLess.setBackgroundResource(R.drawable.shape1);
            }
//        根据盈利判断字体颜色。
            if (!hold.textView_price.getText().toString().startsWith("-")){
                hold.textView_price.setTextColor(Color.parseColor("#ff4320"));
            }else {
                hold.textView_price.setTextColor(Color.parseColor("#0069d5"));
            }
            final Hold finalHold = hold;
            hold.button_sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInfo(position, finalHold.textView_buyNum.getText().toString(), finalHold.textView_OrderNumber.getText().toString());
                }
            });
            hold.button_sell.setTag(position);
            return contextView;
        }
        public void showInfo(int position ,String buyNumStr,String string){
            orderNum = string;
            buyNum = buyNumStr;
            posi = position;
            new Thread(runnable).start();
            progressDialog = ProgressDialog.show(holdActivity.this, "","正在加载,请稍候！");
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String SetData = "SetData";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("DriverID",driverId);
                    jsonObject.put("TaskGuid","ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                    jsonObject.put("DataType","CloseOrder");
                    jsonObject.put("OrderNumber",orderNum);
                    jsonObject.put("Volume",buyNum);
//                jsonObject.put("LoginAccount","");
                    jsonObject.put("Ip",Ip);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request request = new request();
                SoapObject s = request.getResult(SetData, jsonObject.toString());
                String s1 = s.getProperty(0).toString();
                System.out.println(s1);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("key",s1);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        };
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                super.handleMessage(message);
                Bundle bundle = message.getData();
                String s = bundle.getString("key");
                if (s.equals("True")){
                    progressDialog.dismiss();
                    list.remove(posi);
                    l_name.remove(posi);
                    listView.setAdapter(new HoldAdspter(getApplicationContext(),list));
                }else {
                    Toast.makeText(holdActivity.this,"平仓失败",Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Do something.
            //        返回上一个activity传值
            timer.cancel();
            Intent intent = new Intent();
            String s = this.getIntent().getStringExtra("name");
            System.out.println(s);
            for (int i=0;i<l_name.size();i++){
                if (s.equals(l_name.get(i))){
                    no = "false";
                    break;
                }
            }
            intent.putExtra("change", no);
            this.setResult(0, intent);
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
