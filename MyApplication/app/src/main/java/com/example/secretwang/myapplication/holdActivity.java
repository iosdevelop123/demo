package com.example.secretwang.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class holdActivity extends Activity {
    private Button historyButton;
    private ListView listView = null;
    private TextView priceTextView = null;
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
        new Thread(runnable).start();
        createUI();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
//    AdapterView.OnItemClickListener itemClickLis = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            list.remove(position);
//            Log.d("position" + position, "id" + id);
//            Log.i(">>>>>", "sssss");
//            listView.notify();
//        }
//    };

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
//        List list = getData();
//        listView.setAdapter(new HoldAdspter(getApplicationContext(),list));
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            String string = bundle.getString("key");
            int price = 0;
            try {
                JSONArray jsonArray = new JSONArray(string);
                List<Map<String,Object>> list = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++) {
                    Map<String,Object> map = new HashMap<>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    map.put("textView_name",jsonObject.getString("Symbol"));
                    map.put("textView_buyNum",jsonObject.getString("Volume"));
                    map.put("textView_counterFee",jsonObject.getString("Commission"));
                    map.put("textView_buyMoreOrLess",jsonObject.getString("TypeName"));
                    map.put("textView_price",jsonObject.getString("Profit"));
                    map.put("textView_openPrice",jsonObject.getString("OpenPrice"));
                    map.put("textView_closePrice",jsonObject.getString("ClosePrice"));
                    map.put("textView_OrderNumber",jsonObject.getString("OrderNumber"));
                    price += jsonObject.getInt("Profit");
                    list.add(map);
                }
                if (price < 0){
                    priceTextView.setTextColor(Color.parseColor("#0069d5"));
                }else {
                    priceTextView.setTextColor(Color.parseColor("#fe0000"));
                }
                priceTextView.setText(String.valueOf(price));
                listView.setAdapter(new HoldAdspter(getApplicationContext(),list));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String method = "TransformData";
            JSONObject parma = new JSONObject();
            try {
                parma.put("TaskGuid","ab8495db-3a4a-4f70-bb81-8518f60ec8bf");
                parma.put("DriverID","1234567890");
                parma.put("DataType","ClientOpenTrades");
                parma.put("LoginAccount","1317");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String str_json = parma.toString();
            request request = new request();
            SoapObject soapObject = request.getResult(method,str_json);
            List list = data(soapObject);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("key",list.toString());
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };

    private List data(SoapObject soapObject) {
        List list = new ArrayList();
        String string = soapObject.getProperty(0).toString();

        try {
            JSONArray jsonArray = new  JSONArray(string);
            for (int i = 0;i<jsonArray.length();i++){
                Map<String,Object> map = new HashMap<String, Object>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("Symbol",jsonObject.getString("Symbol"));
                map.put("Volume",jsonObject.getString("Volume"));
                map.put("Commission",jsonObject.getString("Commission"));
                map.put("TypeName","看" + jsonObject.getString("TypeName"));
                map.put("Profit",jsonObject.getString("Profit"));
                map.put("OpenPrice",jsonObject.getString("OpenPrice"));
                map.put("ClosePrice",jsonObject.getString("ClosePrice"));
                map.put("OrderNumber",jsonObject.getString("OrderNumber"));
                list.add(map);
                System.out.println(jsonObject);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

//    public List<Map<String,Object>> getData(){
//        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//        for (int i = 0;i < 10;i++){
//            Map<String,Object> map = new HashMap<String, Object>();
//            map.put("textView_name","美原油"+i);
//            map.put("textView_buyNum","2222");
//            map.put("textView_counterFee","-800");
//            map.put("textView_buyMoreOrLess","看多");
//            map.put("textView_price","-999999");
//            map.put("textView_openPrice","22222");
//            map.put("textView_closePrice","33333");
//            list.add(map);
//        }
//        return list;
//    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "hold Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.secretwang.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "hold Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.secretwang.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
