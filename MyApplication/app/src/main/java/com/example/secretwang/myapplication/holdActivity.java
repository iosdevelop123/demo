package com.example.secretwang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class holdActivity extends Activity {
    private Button historyButton;
    private ListView listView;
    private static final String[] strs = new String[]{
            "11111","222222","33333","444444"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hold);
//        持仓
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strs));
//        历史纪录Button
        historyButton = (Button)findViewById(R.id.button5);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holdActivity.this,historyActivity.class);
                startActivity(intent);
            }
        });
    }
}
