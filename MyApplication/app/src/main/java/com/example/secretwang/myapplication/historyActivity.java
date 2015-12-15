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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class historyActivity extends Activity {

    private ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        listView = (ListView)findViewById(R.id.listView_historyHold);
        List<Map<String,Object>> list = getData();
        listView.setAdapter(new historyHoldAdspter(this, list));

        new Thread(runnable).start();
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
            String url = "http://139.196.32.138:10011/WebService.asmx";
            String method = "TransformData";
            String str_json = "{" +
                    "\"TaskGuid\":\"ab8495db-3a4a-4f70-bb81-8518f60ec8bf\"," +
                    "\"DataType\":\"ClientCloseTrades\"," +
                    "\"LoginAccount\":\"1317\"," +
                    "\"StartTime\":\"1448961616\"," +
                    "\"EndTime\":\"1449134416\"" +
                    "}";
            request request = new request();
            String string = request.getResult(url, method, str_json);
            System.out.println(string);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("value",string);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };

    public List<Map<String,Object>> getData() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<10;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("nameText","恒生指数");
            map.put("MoreOrLessText","看多");
            map.put("shoNumText","22手");
            map.put("feesText","手续费-800");
            map.put("DataText","2015-12-12");
            map.put("openTimeText","12:12");
            map.put("CloseTimeText","12:15");
            map.put("priceText","+999999");
            map.put("openPriceText","22222");
            map.put("closePriceText","33333");
            list.add(map);
        }
        return list;
    }
}
