package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] shoushu = new String[]{"1", "2", "3", "4",
           "5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20" };
    private  List<String> hblist = new ArrayList<String>();//货币英文名
    private  List<String> nameList = new ArrayList<String>();//货币中文名
    private static String TaskGuid = "ab8495db-3a4a-4f70-bb81-8518f60ec8bf";
    private Button buyMoreButton;//看多按钮
    private Button buyLessButton;//看空按钮
    private Button allSellButton;//全部卖出按钮
    private TextView yingliText;//主界面显示盈利的text
    private TextView duoOrkongText;//主界面显示是看多买入还是看空买入
    private TextView mairuduoshaoshouTex;//主界面显示买入多少手
    private WheelView wv;
    private WheelView wv2;
    private  int number;
    private  int category;
    private ImageButton settingBtn;
    private  TextView shouTxt;//设置委托手数
    private  TextView nametextView;//名称
    private  ImageButton userBtn;
    private  Button holdButton;
    private TextView PriceTxt;//最新行情
    private String fanxianggoumaishoushu;//反向开仓时，统计持仓的手数
    private String kanduoOrkankong;//在反向开仓的时候，给个值，判断是看多反向还是看空反向
    private static String OpenSell_New = "OpenSell-New";//宏定义
    private static String OpenBuy_New = "OpenBuy-New";
    private Timer timer;//定时器
    private String itemName;//储存切换货币时要切换的货币的名字
    private ProgressDialog progressDialog;//刷新提示框
    private String CLF6Price;
    private String HKZ5Price;
    public List orderNumbersList = new ArrayList();//订单编号数组
    private List SymbolNumberSList = new ArrayList();//选中货币的订单编号数组
    private String loginStr;
    private String BUYMORE = "看多";//宏定义
    private String BUYLESS = "看空";
    private String BUYONCE = "追单";
    private String FANXIANG = "反向开仓";
    private Button netBtn;
    private int LowestMoney;//保证金
    private String NAME1;//宏定义
    private String NAME2 = "HKF5";

    private int NowHour;//当前时间
    private int NowMinute;
    private int profit = 0;//总共盈利
    private String driverId;//手机唯一标识
    private request request = new request();//数据请求
    private Bundle bundle = new Bundle();
    private JSONObject parma = new JSONObject();//请求数据要传入的参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        createButton();
        SharedPreferences driver = getSharedPreferences("driverID",MODE_PRIVATE);
        driverId = driver.getString("driver", "");
        timer = new Timer();//定时器
        new Thread(zaicangRunnable).start();//进入主界面根据在仓订单刷新按钮名字
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        loginStr = sharedPreferences.getString("login", "");
        new Thread(HBListRunnable).start();//获取货币列表
        //网络判断动画
        netAnimation();
        new Thread(latestPriceRunnable).start();//获取最新行情数据
        chicangyingliTimeDingshi();//定时刷新盈利
        new Thread(yuerunnable).start();//请求余额线程
    }
//    获取当前时间
    private void getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String s = simpleDateFormat.format(new Date());
        NowHour = Integer.parseInt(s.substring(0, 2)) + 8;
        if (NowHour>=24){
            NowHour = NowHour - 24;
        }
        NowMinute = Integer.parseInt(s.substring(3, 5));//截取字符串
    }

    //网络判断
    private void netAnimation(){
        NetWorkUtils net = new NetWorkUtils();
        int type=net.getAPNType(MainActivity.this);
        if (type==0){
            netBtn.setText("您当前使用的是2/3/4G网络");
            netBtn.setTextColor(Color.WHITE);
            //netBtn.setBackgroundColor(Color.BLUE);
            //初始化
            Animation alphaAnimation  = new AlphaAnimation(1.0f,0.0f);
            //设置动画时间
            alphaAnimation .setDuration(10000);
            netBtn.startAnimation(alphaAnimation);
           // netBtn.setBackgroundColor(Color.TRANSPARENT);
            netBtn.setVisibility(View.INVISIBLE);
        }else if (type==1){
            netBtn.setText("您当前使用的是wifi网络");
            netBtn.setTextColor(Color.WHITE);
            //netBtn.setBackgroundColor(Color.BLUE);
            //初始化
            Animation alphaAnimation  = new AlphaAnimation(1.0f,0.0f);
            //设置动画时间
            alphaAnimation .setDuration(10000);
            netBtn.startAnimation(alphaAnimation);
            //netBtn.setBackgroundColor(Color.TRANSPARENT);
            netBtn.setVisibility(View.INVISIBLE);
        }
    }
//    持仓盈利定时器
    private void chicangyingliTimeDingshi(){
        final String method = "TransformData";//参数设置
//        final JSONObject parma = new JSONObject();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    parma.put("DriverID", driverId);
                    parma.put("TaskGuid", TaskGuid);
                    parma.put("DataType", "ClientOpenTrades");
                    parma.put("LoginAccount", loginStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SoapObject soapObject = request.getResult(method, parma.toString());//请求网络，返回的值是soapObject类型
                List list = getZaicangDingDan(soapObject);//解析返回的值
                Message message = new Message();
//                Bundle bundle = new Bundle();
                bundle.putString("zaicangkey", list.toString());
                message.setData(bundle);
                zaicanghandler.sendMessage(message);
            }
        }, 1000, 3000);
    }

//  最新行情定时器
    private void zuixinhangqingtimeDingshi(){
        final String method = "TransformData";
//        final JSONObject parma = new JSONObject();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    parma.put("TaskGuid", TaskGuid);
                    parma.put("DataType", "MT4Data");
                    parma.put("DriverID", driverId);
                    parma.put("Type", NAME1 + "," + NAME2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SoapObject string = request.getResult(method, parma.toString());
                String jsonRequest = string.getProperty(0).toString();
                Message message = new Message();
//                Bundle bundle = new Bundle();
                bundle.putString("value", jsonRequest);
                message.setData(bundle);
                latestPriceHandler.sendMessage(message);
            }
        }, 1000, 1000);

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
                    hblist.add(Bh);//协议名字
                    nameList.add(Name);//汉语名字
                }
                NAME1 = hblist.get(0);
                NAME2 = hblist.get(1);
                itemName = NAME1;//进入主界面的时候默认刷新美原油
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Runnable HBListRunnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
//            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid","b4026263-704e-4e12-a64d-f79cb42962cc");
                parma.put("DataType","HBList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SoapObject string = request.getResult(method, parma.toString());
            String jsonRequest = string.getProperty(0).toString();
            Message message = new Message();
//            Bundle bundle = new Bundle();
            bundle.putString("HBListkey",jsonRequest);
            message.setData(bundle);
            HBListhandler.sendMessage(message);
        }
    };






//    主界面根据在仓订单刷新界面
    Runnable zaicangRunnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","ClientOpenTrades");
                parma.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SoapObject soapObject = request.getResult(method, parma.toString());
            List list = getZaicangDingDan(soapObject);
            Message message = new Message();
//            Bundle bundle = new Bundle();
            bundle.putString("zaicangkey", list.toString());
            message.setData(bundle);
            zaicanghandler.sendMessage(message);
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
                    map.put("Volume",jsonObject.getInt("Volume"));
                    profit +=jsonObject.getInt("Profit");
                    list.add(map);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return list;
    }
//    根据在仓订单改变主界面显示
    Handler zaicanghandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("zaicangkey");
            int shoushu = 0;
            try {
                JSONArray jsonArray = new JSONArray(string);
                if (jsonArray.length()==0){
                    duoOrkongText.setText("");
                    buyMoreButton.setText(BUYMORE);
                    buyLessButton.setText(BUYLESS);
                }else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sym = jsonObject.getString("Symbol");
                        if (sym.equals(itemName)) {
                            String type = jsonObject.getString("TypeName");
                            if (type.equals("多")) {
                                duoOrkongText.setText(BUYMORE);
                                buyMoreButton.setText(BUYONCE);
                                buyLessButton.setText(FANXIANG);
                            } else if (type.equals("空")) {
                                duoOrkongText.setText(BUYLESS);
                                buyLessButton.setText(BUYONCE);
                                buyMoreButton.setText(FANXIANG);
                            }
                            break;//如果有一个这样的就跳出循环
                        } else {
                            duoOrkongText.setText("");
                            buyMoreButton.setText(BUYMORE);
                            buyLessButton.setText(BUYLESS);
                        }
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        shoushu += jsonObject.getInt("Volume");
                    }
                }
                mairuduoshaoshouTex.setText(String.valueOf(shoushu));
                yingliText.setText(String.valueOf(profit));
                if (profit<0){//根据正负设置颜色
                    yingliText.setTextColor(Color.parseColor("#0069d5"));
                }else {
                    yingliText.setTextColor(Color.parseColor("#fe0000"));
                }
                profit = 0;//清零，否则会累加
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };




//    界面创建
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
        //最新行情数据
        PriceTxt = (TextView)findViewById(R.id.textView_priceText);
        //网络button
        netBtn =(Button)findViewById(R.id.netImgBtn);
        //主界面盈利的number
        yingliText = (TextView)findViewById(R.id.textView10);
//        主界面显示买入的是看多买入还是看空买入
        duoOrkongText = (TextView)findViewById(R.id.textView34);
//        显示买入多少手
        mairuduoshaoshouTex = (TextView)findViewById(R.id.textView6);
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
            Log.v("++++++++++++", string);
            if (string.equals("连接超时")){}
            else if (string.equals("@")){}
            else {
                String[] strArray = null;
                strArray = string.split(",");
                if(strArray.length>1) CLF6Price = strArray[2].toString();
                else  CLF6Price = "0.00";
                if(strArray.length>4) HKZ5Price = strArray[5].toString();
                else  HKZ5Price = "0.00";
                if (nametextView.getText().toString().equals(nameList.get(0)))
                      PriceTxt.setText(CLF6Price);
                else  PriceTxt.setText(HKZ5Price);

            }
        }
    };
    Runnable latestPriceRunnable = new Runnable() {
        @Override
        public void run() {
            zuixinhangqingtimeDingshi();
        }
    };
    //    持仓按钮点击事件，并传递数据
    View.OnClickListener holdButtonClick =(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer.cancel();//进入下一界面关闭定时器
            Intent intent = new Intent(MainActivity.this,holdActivity.class);
            String name = itemName;
            intent.putExtra("name",name);//把本界面选择的货币传递给持仓界面
            startActivityForResult(intent, 0);
        }
    });
//    从第二界面返回的数据在这里
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        timer = new Timer();//开启定时器
        zuixinhangqingtimeDingshi();
        chicangyingliTimeDingshi();
//        String s = data.getStringExtra("change");
//        判断持仓是否把这个货币平仓，如果全部平仓则刷新买入按钮
//        if(s.equals("true")){
//            buyLessButton.setText(BUYLESS);
//            buyMoreButton.setText(BUYMORE);
//        }
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
                //wv2.setItems(hblist);
                wv2.setItems(nameList);
                wv2.setSeletion(category);
                wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item1) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item1);
                        nametextView.setText(item1);
                        itemName = hblist.get(selectedIndex - 2);
                        category = selectedIndex - 2;
                    }
                });
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("设置您委托的产品类型和手数")
                        .setView(outerView)
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new Thread(genjuzaicangdingdangaibianmairuanniumingziRunnable).start();//选择商品的时候根据在仓订单里面要选择的商品的购买的情况改变按钮的名字
//                                progressDialog = ProgressDialog.show(MainActivity.this, "", "正在切换中...");
                            }
                        })
                        .show();
            }
        });

//    Runnable genjuzaicangdingdangaibianmairuanniumingziRunnable = new Runnable() {
//        @Override
//        public void run() {
//            String TransformData = "TransformData";
//            JSONObject zaicang = new JSONObject();
//            try {
//                zaicang.put("DriverID",driverId);
//                zaicang.put("TaskGuid",TaskGuid);
//                zaicang.put("DataType","ClientOpenTrades");
//                zaicang.put("LoginAccount",loginStr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            request request = new request();
//            SoapObject soapObject = request.getResult(TransformData, zaicang.toString());
//            String duokong = genjuzaicangChangeButton(soapObject);
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putString("duokong",duokong);
//            message.setData(bundle);
//            duokongHandler.sendMessage(message);
//        }
//    };
//    private String genjuzaicangChangeButton(SoapObject soapObject){
//        String s = soapObject.getProperty(0).toString();
//        String duoOrkong = "";
//        if (s.equals("[]")){}else if (s.equals("连接超时")){duoOrkong = "连接超时";}else {
//            try {
//                JSONArray jsonArray = new JSONArray(s);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    String name = jsonObject.getString("Symbol");
//                    if (name.equals(itemName)){
//                        duoOrkong = jsonObject.getString("TypeName");
//                        break;
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return duoOrkong;
//    }
//    Handler duokongHandler = new Handler(){
//        @Override
//        public void handleMessage(Message message) {
//            super.handleMessage(message);
//            Bundle bundle = message.getData();
//            String s = bundle.getString("duokong");
//            if (s.equals("")){
//                nametextView.setText(chinaName);
//                buyLessButton.setText(BUYLESS);
//                buyMoreButton.setText(BUYMORE);
//            }else if (s.equals("空")){
//                nametextView.setText(chinaName);
//                buyMoreButton.setText(FANXIANG);
//                buyLessButton.setText(BUYONCE);
//            }else if (s.equals("多")){
//                nametextView.setText(chinaName);
//                buyMoreButton.setText(BUYONCE);
//                buyLessButton.setText(FANXIANG);
//            }else{
//                Toast.makeText(MainActivity.this,"切换失败",Toast.LENGTH_SHORT).show();
//            }
//            progressDialog.dismiss();
//        }
//    };


//    全部卖出按钮点击事件
    View.OnClickListener allSellClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(">>>>>", "全部卖出");
            buttonCanNotClick();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("是否全部卖出")
                    .setCancelable(false)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonCanClick();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(orderNumbersRunnable).start();
                            progressDialog = ProgressDialog.show(MainActivity.this,"","全部卖出...");
                        }
                    })
                    .show();
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
                progressDialog.dismiss();
            }else {
                new Thread(allSellRunnable).start();
            }
        }
    };
    Runnable orderNumbersRunnable = new Runnable() {
        @Override
        public void run() {
            String TransformData = "TransformData";
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","ClientOpenTrades");
                parma.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SoapObject soapObject = request.getResult(TransformData, parma.toString());
            orderNumbersList = getOrderNumberS(soapObject);
            Message message = new Message();
//            Bundle bundle = new Bundle();
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
            progressDialog.dismiss();
        }
    };
//    全部卖出数据请求
    Runnable allSellRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
//            JSONObject allSellParma = new JSONObject();
            String s = "";
                for (int i = 0; i < orderNumbersList.size(); i++) {
                    s += "," + orderNumbersList.get(i);
                }
                try {
                    parma.put( "DriverID",driverId);
                    parma.put("TaskGuid", TaskGuid);
                    parma.put("DataType", "CloseOrderS");
                    parma.put("OrderNumberS", s.substring(1));
                    parma.put("LoginAccount", loginStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                request request = new request();
                SoapObject soapObject = request.getResult(SetData, parma.toString());
                Message message =new Message();
//                Bundle bundle = new Bundle();
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
            new Thread(yuerunnable).start();//请求余额线程
           if (LowestMoney>500) {
               if (buyMoreButton.getText().toString().equals(BUYMORE)) {
                   buyMoreButtonClick();
               } else if (buyMoreButton.getText().toString().equals(BUYONCE)) {
                   buyMoreButtonClick();
               } else if (buyMoreButton.getText().toString().equals(FANXIANG)) {
                   buyMoreButtonReverse();
               }
           }else {
               Toast.makeText(MainActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
           }
        }
    };
       //请求余额线程
       Runnable yuerunnable = new Runnable() {
           @Override
           public void run() {
               String method = "TransformData";
               try{
                   parma.put("TaskGuid",TaskGuid);
                   parma.put("DataType","ClientRecord");
                   parma.put("LoginAccount",loginStr);
               }catch (JSONException e){
                   e.printStackTrace();
               }
               try{
                   String str_json=parma.toString();
                   SoapObject string = request.getResult(method, str_json);
                   String jsonRequest = string.getProperty(0).toString();
                   //将解析的字符串转换成json对象
                   JSONObject jsonObject=new JSONObject(jsonRequest);
                   LowestMoney =jsonObject.getInt("Balance");

               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       };

//    看多买入
    private void buyMoreButtonClick() {
        getNowTime();//现在的时间
        if (itemName.equals(NAME2)){//判断选择的是不是恒生指数
            if (NowHour>=9&&NowHour<=12){//判断是不是在交易时间
                if (NowHour==9 && NowMinute<=15){
                    Toast.makeText(MainActivity.this, "不在交易时间", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(">>>>>>>>>>>>>>>", "看多买入");
                    new Thread(kanduoRunnable).start();
                    progressDialog = ProgressDialog.show(MainActivity.this,"","下单中...");
                    buttonCanNotClick();
                }
            }else if (NowHour>=13&&NowHour<=16){
                if (NowHour==16 && NowMinute>=10){
                    Toast.makeText(MainActivity.this, "不在交易时间", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(">>>>>>>>>>>>>>>", "看多买入");
                    new Thread(kanduoRunnable).start();
                    progressDialog = ProgressDialog.show(MainActivity.this,"","下单中...");
                    buttonCanNotClick();
                    Log.i("eeee", Integer.toString(NowHour));
                }
            }else {
                Toast.makeText(MainActivity.this, "不在交易时间", Toast.LENGTH_SHORT).show();
            }

        }else {
            Log.i(">>>>>>>>>>>>>>>", "看多买入");
            new Thread(kanduoRunnable).start();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "下单中...");
            buttonCanNotClick();
        }
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
            progressDialog.dismiss();
            buttonCanClick();
        }
    };
//    看多数据请求
    Runnable kanduoRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
//            JSONObject parma = new JSONObject();
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","OpenBuy-New");
                parma.put("LoginAccount",loginStr);
                parma.put("Symbol",itemName);
                parma.put("Volume",shouTxt.getText().toString());
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message = new Message();
//            Bundle bundle = new Bundle();
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
        progressDialog = ProgressDialog.show(MainActivity.this,"","下单中...");
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
//            JSONObject zaicang = new JSONObject();
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","ClientOpenTrades");
                parma.put("LoginAccount",loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            request request = new request();
            SoapObject soapObject = request.getResult(TransformData, parma.toString());
            List list = getxiangtongdeOrderNums(soapObject);
            Message message = new Message();
//            Bundle bundle = new Bundle();
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
                    if (name.equals(itemName)){
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
                progressDialog.dismiss();
            }
            buttonCanClick();

        }
    };
    //    全部卖出数据请求
    Runnable kanduoallSellRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
//            JSONObject allSellParma = new JSONObject();
            String s = "";
            for (int i = 0; i < SymbolNumberSList.size(); i++) {
                s += "," + SymbolNumberSList.get(i);
            }
            Log.i("eeeeee",s);
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid", TaskGuid);
                parma.put("DataType", "CloseOrderS");
                parma.put("OrderNumberS", s.substring(1));
                parma.put("LoginAccount", loginStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message =new Message();
//            Bundle bundle = new Bundle();
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
//            JSONObject parma = new JSONObject();
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType",kanduoOrkankong);
                parma.put("LoginAccount",loginStr);
                parma.put("Symbol",itemName);
                parma.put("Volume",fanxianggoumaishoushu);
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message = new Message();
//            Bundle bundle = new Bundle();
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
            progressDialog.dismiss();
        }
    };

//  看空按钮点击事件
    View.OnClickListener buyLessButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(yuerunnable).start();//请求余额线程
            if (LowestMoney>500) {
                if (buyLessButton.getText().toString().equals(BUYLESS)) {
                    buyLessButtonClick();
                } else if (buyLessButton.getText().toString().equals(BUYONCE)) {
                    buyLessButtonClick();
                } else if (buyLessButton.getText().toString().equals(FANXIANG)) {
                    buyLessButtonReverse();
                }
            } else {
                Toast.makeText(MainActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void buyLessButtonClick() {
        getNowTime();
        if (itemName.equals(NAME2)) {
            if (NowHour >= 9 && NowHour <= 12) {
                if (NowHour == 9 && NowMinute <= 15) {
                    Toast.makeText(MainActivity.this, "不在交易时间", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(">>>>>", "看空买入");
                    new Thread(kankongRunnable).start();
                    progressDialog = ProgressDialog.show(MainActivity.this, "", "下单中...");
                    buttonCanNotClick();
                }
            }else if (NowHour>=13 && NowHour<=16){
                if (NowHour==16 && NowMinute>=10){
                    Toast.makeText(MainActivity.this, "不在交易时间", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(">>>>>", "看空买入");
                    new Thread(kankongRunnable).start();
                    progressDialog = ProgressDialog.show(MainActivity.this, "", "下单中...");
                    buttonCanNotClick();
                }
            }else {
                Toast.makeText(MainActivity.this, "不在交易时间", Toast.LENGTH_SHORT).show();
            }
        }else {
            Log.i(">>>>>", "看空买入");
            new Thread(kankongRunnable).start();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "下单中...");
            buttonCanNotClick();
        }
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
            progressDialog.dismiss();
            buttonCanClick();
        }
    };
    Runnable kankongRunnable = new Runnable() {
        @Override
        public void run() {
            String SetData = "SetData";
//            JSONObject parma = new JSONObject();
            try {
                parma.put("DriverID",driverId);
                parma.put("TaskGuid",TaskGuid);
                parma.put("DataType","OpenSell-New");
                parma.put("LoginAccount",loginStr);
                parma.put("Symbol",itemName);
                parma.put("Volume",shouTxt.getText().toString());
                parma.put("StopLoss","0");
                parma.put("TakeProfit","0");
                parma.put("Comment","Android");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            request request = new request();
            SoapObject soapObject = request.getResult(SetData, parma.toString());
            Message message = new Message();
//            Bundle bundle = new Bundle();
            bundle.putString("buyLess", soapObject.getProperty(0).toString());
            message.setData(bundle);
            kankonghandle.sendMessage(message);
        }
    };


    private void  buyLessButtonReverse() {
        Log.i(">>>>>>","看空反向开仓");
        kanduoOrkankong = OpenSell_New;
        new Thread(fanxiangOrderNumRunnable).start();
        progressDialog = ProgressDialog.show(MainActivity.this,"","下单中...");
    }


//左下角返回按钮点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("是否退出登录？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //退出登录
                            timer.cancel();
                            finish();
                            onBackPressed();
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
