package com.red263.Edaijia;

import java.util.ArrayList;
import java.util.HashMap;

import com.red263.commmodule.Topbar;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Tel_Activity extends Activity {

	ListView tellist=null;
	Topbar topbar=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tel_activity);
        
        //初始topbar
        topbar=(Topbar)findViewById(R.id.topbar);
        topbar.setTextViewById(R.string.title_activity_tel);
        
        //设置电话列表
        tellist=(ListView)findViewById(R.id.ListView_tel);
		ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
		String[] telstr={"18768129536","18768129536"};
		for (String tel : telstr) {
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("bimg", R.drawable.earmode);
			map.put("telinfo", tel);
			list.add(map);
		}
		
	
		  //生成适配器的Item和动态数组对应的元素  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,list,//数据源   
            R.layout.tel_list_item,//ListItem的XML实现  
            //动态数组与ImageItem对应的子项          
            new String[] {"bimg", "telinfo"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.bimg,R.id.telinfo}  
        );  
		
        tellist.setAdapter(listItemAdapter);
        
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// 关闭该页面获取数据的线程
		finish();
		super.onDestroy();
	}



}
