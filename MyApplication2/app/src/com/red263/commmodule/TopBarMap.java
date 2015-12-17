package com.red263.commmodule;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;
import com.red263.Edaijia.Sub_Activity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TopBarMap extends LinearLayout {

	Button Btnback;
	Button orderBtn;
    private String Driverid="1";
    private String ActName="0";
	public TopBarMap(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	/*
	 * 获取司机id
	 */
	public void setDriverid(String driverid) {
		this.Driverid=driverid;
	}
	

	/*
	 * 获取所在页面id
	 */
	public void setActivityOn(String actname) {
		this.ActName=actname;
	}

	public TopBarMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.topbar_map_activity, this);
		Btnback = (Button) findViewById(R.id.Btnback);
		orderBtn = (Button) findViewById(R.id.orderBtn);

		// 设置返回按钮

		Btnback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if("order".equals(ActName)){
				//	MainActivity.instance.finish();
					Intent intent =new Intent();
					Bundle bundle = new Bundle();
					// 传递id给主截面判断应该载入那个tab
					bundle.putInt("MAP_TABINDEX", 1);
					intent.putExtras(bundle);
					intent.setClass(getContext(), MainActivity.class);
					getContext().startActivity(intent);
				}else{
					
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					// 传递id给主截面判断应该载入那个tab
					bundle.putString("MAP_DRIVERID", Driverid);
					intent.putExtras(bundle);
					intent.setClass(getContext(), Sub_Activity.class);
					getContext().startActivity(intent);
					//System.exit(CONTEXT_INCLUDE_CODE);
				}
		
				

			}
		});
		// */

		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 传递用户id，用户名及用户类型
				bundle.putString("MAP_DRIVERID",Driverid );
				// 通知传入的activity本界面的tabid
				bundle.putInt("MAP_INDEXTAB", 0);
				intent.putExtras(bundle);
				intent.setClass(getContext(), Sub_Activity.class);
				// 转向登陆后的页面
				getContext().startActivity(intent);
				
			}
		});
	}
}
