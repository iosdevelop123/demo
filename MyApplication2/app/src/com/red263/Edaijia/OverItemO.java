package com.red263.Edaijia;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.red263.commmodule.FunUtils;
import com.red263.commmodule.UserJsonUtils;
import com.red263.commmodule.UserOrder;

public class OverItemO extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> GeoList;
	private Context mContext;
	private String Uid;
	private String Usertype;
	
	String tel;
	String realname;

	public OverItemO(Drawable marker, List<OverlayItem> geoList, Context context,String uid,String usertype) {
		super(marker);

		this.mContext = context;
		this.GeoList = geoList;
		this.Uid=uid;
		this.Usertype=usertype;

		populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
	}

	@Override
	protected OverlayItem createItem(int i) {
		return GeoList.get(i);
	}

	@Override
	public int size() {
		return GeoList.size();
	}

	@Override
	// 处理当点击事件
	protected boolean onTap(int i) {
		
		bind(mContext,this.Uid,Usertype);
		Toast.makeText(this.mContext,"姓名："+this.realname+"\n电话:"+this.tel,
				Toast.LENGTH_SHORT).show();
		
		return true;
	}
	

	@SuppressWarnings("rawtypes")
	private void bind(Context contex,String uid,String usertype) {

		String jsonData;
		if("0".equals(usertype)){

			 jsonData = FunUtils.getJsonforGetByContex(contex,"userinfo.php?id="
					+ uid + "&usertype=1");
	
		}else{

			 jsonData = FunUtils.getJsonforGetByContex(contex,"userinfo.php?id="
					+ uid + "&usertype=0");
		}
		
		UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
		List<UserOrder> uorder = UserJsonUtils.parseUserFromJson(jsonData);
		Iterator iterator = uorder.iterator();
		iterator.hasNext();
		UserOrder orderL = (UserOrder) iterator.next();
		
		
		this.realname=orderL.getRealname();
		this.tel=orderL.getTel();

	}
	

}
