package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.print.PrintAttributes;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
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
import java.io.InputStream;
import java.lang.reflect.Array;
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
    private static final String[] shoushu = new String[]{"1", "2", "3", "4",
           "5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20" };
    //private  static  final  String[] xiangmu = new String[] {"CLG6","HKZ5"};
    private  List<String> hblist = new ArrayList<String>();
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
    private String fanxianggoumaishoushu;
    private String kanduoOrkankong;
    private static String OpenSell_New = "OpenSell-New";
    private static String OpenBuy_New = "OpenBuy-New";

    public List orderNumbersList = new ArrayList();//订单编号数组
    private List SymbolNumberSList = new ArrayList();//选中货币的订单编号数组
    private String loginStr;
    private String BUYMORE = "看多";
    private String BUYLESS = "看空";
    private String BUYONCE = "追单";
    private String FANXIANG = "反向开仓";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        createButton();
        new Thread(zaicangRunnable).start();//进入主界面根据在仓订单刷新按钮名字
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        loginStr = sharedPreferences.getString("login", "");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        new Thread(latestPriceRunnable).start();//获取最新行情数据
        new Thread(HBListRunnable).start();//获取货币列表
        //new Thread(ServerTimeRunnable).start(); //获取服务器时间
    }

//获取货币列表
    Handler HBListhandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("HBListkey");
            try {
                JSONArray jsonArray = new JSONArray(string);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String Bh = jsonObject.getString("Bh");
                    String Name = jsonObject.getString("Name");
                    hblist.add(Bh);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Runnable HBListRunnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid","b4026263-704e-4e12-a64d-f79cb42962cc");
                parma.put("DataType","HBList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject string = request.getResult(method, parma.toString());
            String jsonRequest = string.getProperty(0).toString();
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("HBListkey",jsonRequest);
            message.setData(bundle);
            HBListhandler.sendMessage(message);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("zaicangkey");
            try {
                JSONArray jsonArray = new JSONArray(string);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String sym = jsonObject.getString("Symbol");
                    if (sym.equals("CLG6")){
                        String type =jsonObject.getString("TypeName");
                        if (type.equals("多")){
                            buyMoreButton.setText(BUYONCE);
                            buyLessButton.setText(FANXIANG);
                        }else {
                            buyLessButton.setText(BUYONCE);
                            buyMoreButton.setText(FANXIANG);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    Runnable zaicangRunnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("DriverID","1234567890");
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","ClientOpenTrades");
                parma.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(method, parma.toString());
            List list = getZaicangDingDan(soapObject);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("zaicangkey",list.toString());
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    private List getZaicangDingDan(SoapObject soapObject){
        List list = new ArrayList();
        String s = soapObject.getProperty(0).toString();
        if (s.equals("[]")){}else{
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i=0;i<jsonArray.length();i++){
                    Map<String,Object> map = new HashMap<>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    map.put("Symbol",jsonObject.getString("Symbol"));
                    map.put("TypeName",jsonObject.getString("TypeName"));
                    list.add(map);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return list;
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
//    设置按钮允许点击
    private void buttonCanClick(){
        allSellButton.setClickable(true);
        buyLessButton.setClickable(true);
        buyMoreButton.setClickable(true);
    }
//    设置按钮不允许被点击
    private void buttonCanNotClick() {
        allSellButton.setClickable(false);
        buyLessButton.setClickable(false);
        buyMoreButton.setClickable(false);
    }
    //
    Handler latestPriceHandler = new Handler() {
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
//            Log.v("++++++++++++", string);
            if (string.equals("连接超时")){}else {
                String[] strArray = null;
                strArray = string.split(",");
                String CLF6Price = strArray[2].toString();
                String HKZ5Price = strArray[5].toString();
                String HBName = strArray[1].toString();
                if (nametextView.getText().toString().equals(HBName)) {
                    PriceTxt.setText(CLF6Price);
                } else {
                    PriceTxt.setText(HKZ5Price);
                }
            }
        }
    };
    Runnable latestPriceRunnable = new Runnable() {
        @Override
        public void run() {
           while (true) {
               String method = "TransformData";
               JSONObject parma = new JSONObject();
               try {
                   parma.put("TaskGuid", TaskGuid);
                   parma.put("DataType", "MT4Data");
                   parma.put("DriverID", "1234567890");
                   parma.put("Type", "CLG6,HKZ5");
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
            String name = nametextView.getText().toString();
            System.out.println(name);
            intent.putExtra("name",name);
            startActivityForResult(intent, 0);
        }
    });
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        String s = data.getStringExtra("change");
        if(s.equals("true")){
            buyLessButton.setText(BUYLESS);
            buyMoreButton.setText(BUYMORE);
        }
    }



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
                wv2.setItems(hblist);
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
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(genjuzaicangdingdangaibianmairuanniumingziRunnable).start();//选择商品的时候根据在仓订单里面要选择的商品的购买的情况改变按钮的名字
                            }
                        })
                        .show();
            }
        });

    Runnable genjuzaicangdingdangaibianmairuanniumingziRunnable = new Runnable() {
        @Override
        public void run() {
            String TransformData = "TransformData";
            JSONObject zaicang = new JSONObject();
            try {
                zaicang.put("DriverID","1234567890");
                zaicang.put("TaskGuid",TaskGuid);
                zaicang.put("DataType","ClientOpenTrades");
                zaicang.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(TransformData, zaicang.toString());
            String duokong = genjuzaicangChangeButton(soapObject);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("duokong",duokong);
            message.setData(bundle);
            duokongHandler.sendMessage(message);
        }
    };
    private String genjuzaicangChangeButton(SoapObject soapObject){
        String s = soapObject.getProperty(0).toString();
        String duoOrkong = "";
        if (s.equals("[]")){
            buyLessButton.setText(BUYLESS);
            buyMoreButton.setText(BUYMORE);
        }else {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("Symbol");
                    if (name.equals(nametextView.getText().toString())){
                        duoOrkong = jsonObject.getString("TypeName");
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return duoOrkong;
    }
    Handler duokongHandler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String s = bundle.getString("duokong");
            if (s.equals("")){
                buyLessButton.setText(BUYLESS);
                buyMoreButton.setText(BUYMORE);
            }else if (s.equals("空")){
                buyMoreButton.setText(FANXIANG);
                buyLessButton.setText(BUYONCE);
            }else if (s.equals("多")){
                buyMoreButton.setText(BUYONCE);
                buyLessButton.setText(FANXIANG);
            }
        }
    };


//    全部卖出按钮点击事件
    View.OnClickListener allSellClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(">>>>>", "全部卖出");
            buttonCanNotClick();
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
                buyMoreButton.setText(BUYMORE);
                buyLessButton.setText(BUYLESS);
                buttonCanClick();
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
                zaicang.put("DriverID","1234567890");
                zaicang.put("TaskGuid",TaskGuid);
                zaicang.put("DataType","ClientOpenTrades");
                zaicang.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(TransformData, zaicang.toString());
            orderNumbersList = getOrderNumberS(soapObject);
            Message message = new Message();
            Bundle bundle = new Bundle();
            if (orderNumbersList.size() == 0){
                bundle.putString("orderNumber", "null");
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
                buyMoreButton.setText(BUYMORE);
                buyLessButton.setText(BUYLESS);
                Toast.makeText(MainActivity.this, "卖出成功", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(MainActivity.this, "卖出失败", Toast.LENGTH_SHORT).show();
            }
            buttonCanClick();
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
                    allSellParma.put("LoginAccount", loginStr);
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
            if (buyMoreButton.getText().toString().equals(BUYMORE)) {
                buyMoreButtonClick();
            }else if (buyMoreButton.getText().toString().equals(BUYONCE)){
                buyMoreButtonClick();
            }else if (buyMoreButton.getText().toString().equals(FANXIANG)){
                buyMoreButtonReverse();
            }
        }
    };




//    看多买入
    private void buyMoreButtonClick() {
        Log.i(">>>>>>>>>>>>>>>", "看多买入");
        new Thread(kanduoRunnable).start();
        buttonCanNotClick();
    }
//    根据返回，判断是否买入成功
    Handler buyMoreHandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("buyMore");
            if (string.startsWith("{\"Comment\"")) {
                buyMoreButton.setText(BUYONCE);
                buyLessButton.setText(FANXIANG);
                Toast.makeText(MainActivity.this,"买入成功",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    Toast.makeText(MainActivity.this,jsonObject.getString("ErrMessage"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            buttonCanClick();
        }
    };
//    看多数据请求
    Runnable kanduoRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","OpenBuy-New");
                parma.put("LoginAccount",loginStr);
                parma.put("Symbol",nametextView.getText().toString());
                parma.put("Volume",shouTxt.getText().toString());
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("buyMore", soapObject.getProperty(0).toString());
            message.setData(bundle);
            buyMoreHandler.sendMessage(message);
        }
    };



//    看多反向开仓
    private void buyMoreButtonReverse(){
        Log.i(">>>>>>","看多反向开仓");
        kanduoOrkankong = OpenBuy_New;
        new Thread(fanxiangOrderNumRunnable).start();
    }




    Handler fanxiangOrderNumHandler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String s = bundle.getString("kanduofanxiangOrderNum");
            try {
                JSONArray jsonArray = new JSONArray(s);
                int shoushu = 0;
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    shoushu += jsonObject.getInt("shoushu");
                    String order = jsonObject.getString("order");
                    SymbolNumberSList.add(order);
                }
                fanxianggoumaishoushu = String.valueOf(shoushu);
                new Thread(kanduoallSellRunnable).start();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    Runnable fanxiangOrderNumRunnable = new Runnable() {
        @Override
        public void run() {
            String TransformData = "TransformData";
            JSONObject zaicang = new JSONObject();
            try {
                zaicang.put("DriverID","1234567890");
                zaicang.put("TaskGuid",TaskGuid);
                zaicang.put("DataType","ClientOpenTrades");
                zaicang.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(TransformData, zaicang.toString());
            List list = getxiangtongdeOrderNums(soapObject);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("kanduofanxiangOrderNum", list.toString());
            message.setData(bundle);
            fanxiangOrderNumHandler.sendMessage(message);
        }
    };
    private List getxiangtongdeOrderNums(SoapObject soapObject){
        List list = new ArrayList();
        String s = soapObject.getProperty(0).toString();
        if (s.equals("[]")){}else {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Map<String,Object> map = new HashMap<>();
                    String name = jsonObject.getString("Symbol");
                    if (name.equals(nametextView.getText().toString())){
                        Log.i("名称",name);
                        map.put("order", jsonObject.getString("OrderNumber"));
                        map.put("shoushu",jsonObject.getString("Volume"));
                        list.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    Handler kanduosellHandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("sellKey");
            if (string.equals("True")){
                new Thread(fanxiangmairuRunnable).start();
            }else {
                Toast.makeText(MainActivity.this, "反向开仓失败", Toast.LENGTH_SHORT).show();
            }
            buttonCanClick();
        }
    };
    //    全部卖出数据请求
    Runnable kanduoallSellRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
            JSONObject allSellParma = new JSONObject();
            String s = "";
            for (int i = 0; i < SymbolNumberSList.size(); i++) {
                s += "," + SymbolNumberSList.get(i);
            }
            try {
                allSellParma.put("TaskGuid", TaskGuid);
                allSellParma.put("DataType", "CloseOrderS");
                allSellParma.put("OrderNumberS", s.substring(1));
                allSellParma.put("LoginAccount", loginStr);
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
            kanduosellHandler.sendMessage(message);
        }
    };
//  反向买入请求
    Runnable fanxiangmairuRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType",kanduoOrkankong);
                parma.put("LoginAccount",loginStr);
                parma.put("Symbol",nametextView.getText().toString());
                parma.put("Volume",fanxianggoumaishoushu);
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("kanduofanxiangmairu", soapObject.getProperty(0).toString());
            message.setData(bundle);
            fanxiangmairuHandler.sendMessage(message);
        }
    };
    Handler fanxiangmairuHandler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String s = bundle.getString("kanduofanxiangmairu");
            if (s.startsWith("{\"Comment\"")) {
                if (kanduoOrkankong.equals(OpenBuy_New)) {
                    buyMoreButton.setText(BUYONCE);
                    buyLessButton.setText(FANXIANG);
                }else {
                    buyLessButton.setText(BUYONCE);
                    buyMoreButton.setText(FANXIANG);
                }
                buttonCanClick();
                Toast.makeText(MainActivity.this,"反向开仓成功",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Toast.makeText(MainActivity.this,jsonObject.getString("ErrMessage"),Toast.LENGTH_SHORT).show();
                    buttonCanClick();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };




//  看空按钮点击事件
    View.OnClickListener buyLessButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (buyLessButton.getText().toString().equals(BUYLESS)) {
                buyLessButtonClick();
            }else if (buyLessButton.getText().toString().equals(BUYONCE)){
                buyLessButtonClick();
            }else if (buyLessButton.getText().toString().equals(FANXIANG)){
                buyLessButtonReverse();
            }
        }
    };
    private void buyLessButtonClick() {
        Log.i(">>>>>","看空买入");
        new Thread(kankongRunnable).start();
        buttonCanNotClick();
    }
    Handler kankonghandle = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("buyLess");
            if (string.startsWith("{\"Comment\"")) {
                buyLessButton.setText(BUYONCE);
                buyMoreButton.setText(FANXIANG);
                Toast.makeText(MainActivity.this,"买入成功",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    Toast.makeText(MainActivity.this,jsonObject.getString("ErrMessage"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            buttonCanClick();
        }
    };
    Runnable kankongRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","OpenSell-New");
                parma.put("LoginAccount",loginStr);
                parma.put("Symbol",nametextView.getText().toString());
                parma.put("Volume",shouTxt.getText().toString());
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("buyLess", soapObject.getProperty(0).toString());
            message.setData(bundle);
            kankonghandle.sendMessage(message);
        }
    };


    private void  buyLessButtonReverse() {
        Log.i(">>>>>>","看空反向开仓");
        kanduoOrkankong = OpenSell_New;
        new Thread(fanxiangOrderNumRunnable).start();
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
