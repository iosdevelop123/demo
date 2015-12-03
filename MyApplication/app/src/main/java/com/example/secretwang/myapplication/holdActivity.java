package com.example.secretwang.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret Wang on 2015/12/3.
 */
public class holdActivity extends ListActivity {
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getDate()));
        setContentView(listView);
    }
    private List<String> getDate() {
        List<String> data = new ArrayList<String>();
        data.add("1111111");
        data.add("2222222");
        data.add("3333333");
        data.add("44444444");
        return data;
    }
}
