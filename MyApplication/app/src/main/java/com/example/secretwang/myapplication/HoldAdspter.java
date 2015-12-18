package com.example.secretwang.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Secret Wang on 04/12/2015.
 */
public class HoldAdspter extends BaseAdapter {
    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    protected HoldAdspter(Context context, List<Map<String, Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public final class Hold {
        public TextView textView_name;
        public TextView textView_buyMoreOrLess;
        public TextView textView_buyNum;
        public TextView textView_counterFee;
        public TextView textView_price;
        public TextView textView_openPrice;
        public TextView textView_closePrice;
        public Button button_sell;
        public Number orderNum;
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
            contextView = layoutInflater.inflate(R.layout.list,null);
            hold.textView_name = (TextView)contextView.findViewById(R.id.textView);
            hold.textView_buyMoreOrLess = (TextView)contextView.findViewById(R.id.textView14);
            hold.textView_buyNum = (TextView)contextView.findViewById(R.id.textView_buyNum);
            hold.textView_counterFee = (TextView)contextView.findViewById(R.id.textView_counterFee);
            hold.textView_price = (TextView)contextView.findViewById(R.id.textView_price);
            hold.textView_openPrice = (TextView) contextView.findViewById(R.id.textView_openPrice);
            hold.textView_closePrice = (TextView) contextView.findViewById(R.id.textView_closePrice);
            hold.button_sell = (Button)contextView.findViewById(R.id.button_sell);
//            hold.button_sell.setOnClickListener(sellButtonClick);
            contextView.setTag(hold);
        }else {
            hold = (Hold)contextView.getTag();
        }
//        得到数据
        hold.textView_name.setText((String)data.get(position).get("textView_name"));
        hold.textView_buyMoreOrLess.setText((String)data.get(position).get("textView_buyMoreOrLess"));
        hold.textView_buyNum.setText((String)data.get(position).get("textView_buyNum"));
        hold.textView_counterFee.setText((String)data.get(position).get("textView_counterFee"));
        hold.textView_price.setText((String)data.get(position).get("textView_price"));
        hold.textView_openPrice.setText((String)data.get(position).get("textView_openPrice"));
        hold.textView_closePrice.setText((String)data.get(position).get("textView_closePrice"));

//        判断是看多还是看空，显示背景颜色
        if (hold.textView_buyMoreOrLess.getText().toString().equals("看多")) {
            hold.textView_buyMoreOrLess.setBackgroundColor(Color.parseColor("#7c0000"));
        }else {
            hold.textView_buyMoreOrLess.setBackgroundColor(Color.parseColor("#333399"));
        }
//        根据盈利判断字体颜色。
        if (!hold.textView_price.getText().toString().startsWith("-")){
            hold.textView_price.setTextColor(Color.parseColor("#ff4320"));
        }else {
            hold.textView_price.setTextColor(Color.parseColor("#0000cc"));
        }

        return contextView;
    }
    View.OnClickListener sellButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.findViewById(R.id.button_sell);

        }
    };

}
