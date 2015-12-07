package com.example.secretwang.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

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
    }

//    @Override
//       public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//                return true;
//           }

    public List<Map<String,Object>> getData() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<10;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("nameText","恒生指数");
            map.put("MoreOrLessText","看多");
            map.put("shoNumText","22手");
            map.put("feesText","手续费-800");
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
