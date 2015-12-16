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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class historyActivity extends Activity {

    private ListView listView = null;
    private ArrayList arrayList = new ArrayList();

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
            String method = "TransformData";
            String str_json = "{" +
                    "\"TaskGuid\":\"ab8495db-3a4a-4f70-bb81-8518f60ec8bf\"," +
                    "\"DataType\":\"ClientCloseTrades\"," +
                    "\"LoginAccount\":\"1317\"," +
                    "\"StartTime\":\"1448961616\"," +
                    "\"EndTime\":\"1449134416\"" +
                    "}";
            request request = new request();
            SoapObject requestResult = request.getResult(method, str_json);
            data(requestResult);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("value",requestResult.toString());
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
    private List<Map<String,Object>> data(SoapObject soapObject){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String string = soapObject.getProperty(0).toString();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i=0;i<jsonArray.length();i++){
                Map<String,Object> map = new HashMap<String,Object>();
                JSONObject myjson = jsonArray.getJSONObject(i);
                String TypeName = myjson.getString("TypeName");
                map.put("MoreOrLessText","看" + TypeName);
                
                arrayList.add(0,TypeName);
                list.add(map);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        System.out.println(string);
        return list;
    }

    public List<Map<String,Object>> getData() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<arrayList.size();i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("nameText","恒生指数");
            map.put("MoreOrLessText","看" + arrayList.get(i));
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
