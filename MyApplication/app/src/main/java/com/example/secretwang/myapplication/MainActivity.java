package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.print.PrintAttributes;
import android.text.style.BackgroundColorSpan;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] shoushu = new String[]{"1手", "2手", "3手", "4手",
           "5手","6手","7手","8手","9手","10手","11手","12手","13手","14手","15手","16手","17手","18手","19手","20手" };
    private  static  final  String[] xiangmu = new String[] {"美原油","恒生指数"};
    private static String TaskGuid = "ab8495db-3a4a-4f70-bb81-8518f60ec8bf";
    private Button buyMoreButton;
    private Button buyLessButton;
    private Button allSellButton;
    private WheelView wv;
    private WheelView wv2;
    private  int number;
    private  int category;
    private GoogleApiClient client;
    private ImageButton settingBtn;
    private  TextView shouTxt;
    private  TextView nametextView;
    private  ImageButton userBtn;
    private  Button holdButton;
    private TextView PriceTxt;

    public List orderNumbersList = new ArrayList();//订单编号数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        createButton();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        new Thread(latestPriceRunnable).start();
    }
    private void createButton() {
        //手数和品种
        shouTxt=(TextView) findViewById(R.id.shoushutextView);
        nametextView=(TextView) findViewById(R.id.nametextView);
        //设置按钮
        settingBtn=(ImageButton) findViewById(R.id.setBtn);
        settingBtn.setOnClickListener(settingBtnClick);
        //个人中心按钮
        userBtn=(ImageButton)findViewById(R.id.usrBtn);
        userBtn.setOnClickListener(userBtnClick);
        //持仓按钮
        holdButton = (Button)findViewById(R.id.button_hold);
        holdButton.setOnClickListener(holdButtonClick);
        // 看多按钮
        buyMoreButton = (Button)findViewById(R.id.button_buyMore);
        buyMoreButton.setOnClickListener(buyMoreClick);
//        看空按钮
        buyLessButton = (Button)findViewById(R.id.button_buyLess);
        buyLessButton.setOnClickListener(buyLessButtonClick);
//        全部卖出按钮
        allSellButton = (Button)findViewById(R.id.button_allSell);
        allSellButton.setOnClickListener(allSellClick);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //最新行情数据
        PriceTxt = (TextView)findViewById(R.id.textView_priceText);
    }
    //
    Handler latestPriceHandler = new Handler() {
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
           // Log.v("++++++++++++", string);
            String[] strArray = null;
            strArray = string.split(",");
            String CLF6Price=strArray[2].toString();
            String HKZ5Price=strArray[5].toString();
            Log.v("------------",CLF6Price);
            Log.v("------------", HKZ5Price);
            PriceTxt.setText(CLF6Price);
        }
    };
    Runnable latestPriceRunnable = new Runnable() {
        @Override
        public void run() {
           while (true) {
               String method = "TransformData";
               JSONObject parma = new JSONObject();
               try {
                   parma.put("TaskGuid", "ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                   parma.put("DataType", "MT4Data");
                   parma.put("DriverID", "1234567890");
                   parma.put("Type", "CLF6,HKZ5");
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               try {
                   String str_json = parma.toString();
                   request request = new request();
                   SoapObject string = request.getResult(method, str_json);
                   String jsonRequest = string.getProperty(0).toString();
                   Thread.sleep(1000);// 线程暂停1秒，单位毫秒
                   Message message = new Message();
                   Bundle bundle = new Bundle();
                   bundle.putString("value", jsonRequest);
                   message.setData(bundle);
                   latestPriceHandler.sendMessage(message);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        }
    };
    //    持仓按钮点击事件
    View.OnClickListener holdButtonClick =(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,holdActivity.class);
            startActivity(intent);
        }
    });

    //        跳转个人中心界面
    View.OnClickListener userBtnClick=(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,UserActivity.class);
            startActivity(intent);
        }
    });
        //    跳转设置界面
     View.OnClickListener settingBtnClick =(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View outerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.wheel_view, null);
                wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(Arrays.asList(shoushu));
                //     保存上次选择的手数
                wv.setSeletion(number);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        shouTxt.setText(item);
                        number=selectedIndex-2;
                    }
                });
                wv2 = (WheelView) outerView.findViewById(R.id.wheel_view_wv2);
                wv2.setOffset(2);
                wv2.setItems(Arrays.asList(xiangmu));
                wv2.setSeletion(category);
                wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item1) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item1);
                        nametextView.setText(item1);
                        category=selectedIndex-2;
                    }
                });
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("设置您委托的产品类型和手数")
                        .setView(outerView)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });

//    全部卖出按钮点击事件
    View.OnClickListener allSellClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(">>>>>", "全部卖出");
            new Thread(orderNumbersRunnable).start();
        }
    };
//    解析订单编号
    Handler orderHandler = new Handler(){
    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String string = bundle.getString("orderNumber");
        if (string.equals("null")){
            Toast.makeText(MainActivity.this, "没有订单", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(allSellRunnable).start();
        }
    }
};
    Runnable orderNumbersRunnable = new Runnable() {
        @Override
        public void run() {
            String TransformData = "TransformData";
            JSONObject zaicang = new JSONObject();
            try {
                zaicang.put("TaskGuid",TaskGuid);
                zaicang.put("DataType","ClientOpenTrades");
                zaicang.put("LoginAccount","1317");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(TransformData, zaicang.toString());
            orderNumbersList = getOrderNumberS(soapObject);
            Message message = new Message();
            Bundle bundle = new Bundle();
            if (orderNumbersList.size() == 0){
                bundle.putString("orderNumber","null");
            }else {
                bundle.putString("orderNumber", orderNumbersList.toString());
            }
            message.setData(bundle);
            orderHandler.sendMessage(message);
        }
    };
    //    得到订单编号数组
    private List getOrderNumberS(SoapObject soapObject){
        List list = new ArrayList();
        String s = soapObject.getProperty(0).toString();
        if (s.equals("[]")){}else {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String OrderNumber = jsonObject.getString("OrderNumber");
                    list.add(OrderNumber);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    Handler sellHandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("sellKey");
            if (string.equals("True")){
                buyMoreButton.setText("看多");
                buyLessButton.setText("看空");
                Toast.makeText(MainActivity.this, "卖出成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "卖出失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
//    全部卖出数据请求
    Runnable allSellRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
            JSONObject allSellParma = new JSONObject();
            String s = "";
                for (int i = 0; i < orderNumbersList.size(); i++) {
                    s += "," + orderNumbersList.get(i);
                }
                try {
                    allSellParma.put("TaskGuid", TaskGuid);
                    allSellParma.put("DataType", "CloseOrderS");
                    allSellParma.put("OrderNumberS", s.substring(1));
                    allSellParma.put("LoginAccount", "1317");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request request = new request();
                SoapObject soapObject = request.getResult(SetData, allSellParma.toString());
                Message message =new Message();
                Bundle bundle = new Bundle();
                if (soapObject.getProperty(0).toString().equals("True")){
                    bundle.putString("sellKey","True");
                }else {
                    bundle.putString("sellKey","Fals");
                }
                message.setData(bundle);
                sellHandler.sendMessage(message);
            }
    };


//    看多按钮点击事件
    View.OnClickListener buyMoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (buyMoreButton.getText().toString().equals("看多")) {
                buyMoreButtonClick();
            }else if (buyMoreButton.getText().toString().equals("追单")){
                buyMoreButtonBuyOnce();
            }else if (buyMoreButton.getText().toString().equals("反向开仓")){
                buyMoreButtonReverse();
            }
        }
    };
//    看多买入
    private void buyMoreButtonClick() {
        Log.i(">>>>>>>>>>>>>>>","看多买入");

        buyMoreButton.setText("追单");
        buyLessButton.setText("反向开仓");
    }
    Runnable kanduoRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","OpenBuy-New");
                parma.put("LoginAccount","1317");
                parma.put("Symbol","CLF6");
                parma.put("Volume","1");
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());

        }
    };
    private List getBuyMoreList(SoapObject soapObject){
        List list = new ArrayList();
        return list;
    }


//    看多追单
    private void buyMoreButtonBuyOnce() {Log.i(">>>>>", "看多追单买入");}

//    看多反向开仓
    private void buyMoreButtonReverse(){
        Log.i(">>>>>>","看多反向开仓");
        buyMoreButton.setText("追单");
        buyLessButton.setText("反向开仓");
    }

//  看空按钮点击事件
    View.OnClickListener buyLessButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (buyLessButton.getText().toString().equals("看空")) {
                buyLessButtonClick();
            }else if (buyLessButton.getText().toString().equals("追单")){
                buyLessButtonBuyOnce();
            }else if (buyLessButton.getText().toString().equals("反向开仓")){
                buyLessButtonReverse();
            }
        }
    };
    private void buyLessButtonClick() {
        Log.i(">>>>>","看空买入");
        buyLessButton.setText("追单");
        buyMoreButton.setText("反向开仓");
    }
    private void buyLessButtonBuyOnce() {
        Log.i(">>>>>","追单买入");
    }
    private void  buyLessButtonReverse() {
        Log.i(">>>>>>","看空反向开仓");
        buyLessButton.setText("追单");
        buyMoreButton.setText("反向开仓");
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.example.secretwang.myapplication/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.example.secretwang.myapplication/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}
