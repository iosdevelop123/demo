package com.example.secretwang.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class historyActivity extends Activity {

    private ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        listView = (ListView)findViewById(R.id.listView_historyHold);
        new Thread(runnable).start();
    }
        Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("value");
            try {
                List<Map<String,Object>> listMap = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(string);
                for (int i=0;i<jsonArray.length();i++){
                    Map<String,Object> map = new HashMap<String, Object>();
                    JSONObject resultStr = jsonArray.getJSONObject(i);
                    map.put("nameText", resultStr.getString("Symbol"));
                    map.put("MoreOrLessText", "看" + resultStr.getString("TypeName"));
                    map.put("shoNumText",resultStr.getString("Volume") + "手");
                    map.put("feesText", "手续费" + resultStr.getString("Commission"));
                    map.put("DataText", resultStr.getString("CloseTime"));
                    map.put("openTimeText" ,resultStr.getString("openTimeHour") + ":" +resultStr.getString("openTimeMin") );
                    map.put("CloseTimeText",resultStr.getString("closeTimeHour") + ":" + resultStr.getString("closeTimeMin"));
                    map.put("priceText",resultStr.getString("Profit"));
                    map.put("openPriceText",resultStr.getString("OpenPrice"));
                    map.put("closePriceText",resultStr.getString("ClosePrice"));
                    listMap.add(0, map);
                }
                listView.setAdapter(new historyHoldAdspter(getApplicationContext(), listMap));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            int timeLong = (int) (System.currentTimeMillis()/1000+60*60*24);
            int starTime = (int) (System.currentTimeMillis()/1000-60*60*24*2);
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid","ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                parma.put("DataType","ClientCloseTrades");
                parma.put("LoginAccount","1317");
                parma.put("StartTime",starTime);
                parma.put("EndTime",timeLong);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String str_json = parma.toString();
            request request = new request();
            SoapObject requestResult = request.getResult(method, str_json);
            List list = data(requestResult);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("value",list.toString());
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    private List data(SoapObject soapObject){
        List list = new ArrayList<>();
        String string = soapObject.getProperty(0).toString();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i=0;i<jsonArray.length();i++){
                Map<String,Object> map = new HashMap<String, Object>();
                JSONObject myjson = jsonArray.getJSONObject(i);
                map.put("TypeName",myjson.getString("TypeName"));
                map.put("Symbol", myjson.getString("Symbol"));
                map.put("Volume", myjson.getString("Volume"));
                map.put("Commission", myjson.getString("Commission"));
//                截取卖出时间字符串
                String closeTime = myjson.getString("CloseTime");
                map.put("CloseTime", closeTime.substring(0, 10));
                map.put("closeTimeHour",closeTime.substring(11,13));
                map.put("closeTimeMin",closeTime.substring(14,16));
//                截取买入时间字符串
                String openTime = myjson.getString("OpenTimeMRSF");
                map.put("openTimeHour", openTime.substring(6,8));
                map.put("openTimeMin",openTime.substring(9,11));
                map.put("Profit",myjson.getString("Profit"));
                map.put("OpenPrice",myjson.getString("OpenPrice"));
                map.put("ClosePrice",myjson.getString("ClosePrice"));
                list.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
}
