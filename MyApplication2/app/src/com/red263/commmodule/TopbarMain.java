package com.red263.commmodule;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;
import com.red263.Edaijia.Tel_Activity;
import com.red263.commmodule.AsyncImageLoader.ImageCallback;
import com.red263.login.Login_Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopbarMain extends LinearLayout {

	private ImageView headimg;
	private Button telorderBtn;
	private TextView usernametext;

	private Boolean islogin;

	String userid;
	String usertype;
	String username;
	private String imageUrl="";

	public TopbarMain(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * 直接设置显示的文字
	 */
	public void setTextViewText(String text) {
		usernametext.setText(text);
	}

	// 设置是否为登录状态
	public void setLoginState(boolean state) {
		islogin = state;
	}

	public void setHeadImg(String imgurl){
		
		imageUrl=imgurl;
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,new ImageCallback() {
			       
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {

						if (headimg != null) {
							headimg.setImageDrawable(imageDrawable);
							
						}
					}
				});
		if (cachedImage == null) {
			headimg.setImageResource(R.drawable.find_friend_contact_icon);
		}else{
			headimg.setImageDrawable(cachedImage);
			
		}
	}
	/**
	 * 通过string中的id设置显示的文字
	 */
	public void setTextViewById(int resid) {
		usernametext.setText(resid);
	}


	public TopbarMain(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.topbar_main, this);
		telorderBtn = (Button) findViewById(R.id.telorderBtn);
		usernametext = (TextView) findViewById(R.id.username);

		headimg = (ImageView) findViewById(R.id.headimg);
		

		

		headimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (islogin) {
				//	MainActivity.instance.finish();
					Intent intent1 = new Intent();
					Bundle bundle = new Bundle();
					// 传递id给主截面判断应该载入那个tab
					bundle.putInt("MAP_TABINDEX", 2);
					intent1.putExtras(bundle);
					intent1.setClass(getContext(), MainActivity.class);
					getContext().startActivity(intent1);
					//System.exit(0);
				 // 	android.os.Process.killProcess(android.os.Process.myPid()) ;


				} else {
					Intent intent1 = new Intent();
					intent1.setClass(getContext(), Login_Activity.class);
					getContext().startActivity(intent1);
				//	System.exit(0);
				 // 	android.os.Process.killProcess(android.os.Process.myPid()) ;


				}
			}
		});

		// 设置电话预约按钮
		telorderBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent();
				intent2.setClass(getContext(), Tel_Activity.class);
				getContext().startActivity(intent2);
			}
		});
		// */
	}

}
