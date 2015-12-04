package com.example.secretwang.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Secret Wang on 04/12/2015.
 */
public class MyAdspter extends BaseAdapter {
    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    protected MyAdspter(Context context, List<Map<String, Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = layoutInflater.from(context);
    }
    public final class Hold {
        public TextView textView_name;
        public TextView textView_buyMoreOrLess;
    }
    @Override
    public int getCount() {
        return data.size();
    }
        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
                return data.get(position);
            }
      /**
          * 获得唯一标识
          */
      @Override
      public long getItemId(int position) {
          return position;
      }
    @Override public View getView(int position,View contextView,ViewGroup parent){
         Hold hold = null;
        if (contextView == null){
            hold = new Hold();
            contextView = layoutInflater.inflate(R.layout.list, null);
            hold.textView_name = (TextView)contextView.findViewById(R.id.textView);
            hold.textView_buyMoreOrLess = (TextView)contextView.findViewById(R.id.textView14);
            contextView.setTag(hold);
        }else {
            hold = (Hold)contextView.getTag();
        }
        hold.textView_name.setText((String)data.get(position).get("textView_name"));
        hold.textView_buyMoreOrLess.setText((String)data.get(position).get("textView_buyMoreOrLess"));
        return contextView;
    }
}
