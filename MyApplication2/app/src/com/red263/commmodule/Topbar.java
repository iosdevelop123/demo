package com.red263.commmodule;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Topbar extends LinearLayout {

	private Button Btnback;
	private TextView  titletext;
	int tabindex;
	
	public Topbar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	} 
	
	public Topbar(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		  LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		          inflater.inflate(R.layout.topbar, this);
		          Btnback=(Button)findViewById(R.id.Btnback);
		          titletext=(TextView)findViewById(R.id.activitytitle);

		         
		          //设置返回按钮
              	        
                    Btnback.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//MainActivity.instance.finish();
						Intent intent=new Intent();
						
						Bundle bundle = new Bundle();
						// 传递id给主截面判断应该载入那个tab
						bundle.putInt("MAP_TABINDEX", tabindex);
						intent.putExtras(bundle);			
						
						intent.setClass(getContext(),MainActivity.class);
						getContext().startActivity(intent);
					  //	System.exit(0);  
					  //	android.os.Process.killProcess(android.os.Process.myPid()) ;
						
						
					}
				});
				//*/
	} 
	
	
	
	    /** 
	     * 直接设置显示的文字 
	     */  
	    public void setTextViewText(String text) {  
	    	titletext.setText(text);  
	    }  
	    
	    /** 
	     * 通过string中的id设置显示的文字 
	     */  
	    public void setTextViewById(int resid) {  
	    	titletext.setText(resid);
	    }  

	    public void setBackActivity(int tabindexval){
	    	tabindex=tabindexval;
	    }

	
}
