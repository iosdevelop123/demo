package com.red263.Edaijia;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.red263.commmodule.DriverListOrder;
import com.red263.commmodule.FunUtils;
import com.red263.commmodule.JsonUtils;

public class OverItemT extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> GeoList;
	private Context mContext;
	private String Driverid;
	
	String tel;
	String realname;

	public OverItemT(Drawable marker, List<OverlayItem> geoList, Context context,String driverid) {
		super(marker);

		this.mContext = context;
		this.GeoList = geoList;
		this.Driverid=driverid;

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
		
		bind(mContext,this.Driverid);
		Toast.makeText(this.mContext,"姓名："+this.realname+"\n电话:"+this.tel,
				Toast.LENGTH_SHORT).show();
		
		return true;
	}
	

	@SuppressWarnings("rawtypes")
	private void bind(Context contex,String driverid) {
		String jsonData = FunUtils.getJsonforGetByContex(contex,"driverinfo.php?id="
				+ driverid);
		JsonUtils UserOrderJsonUtils = new com.red263.commmodule.JsonUtils();
		List<DriverListOrder> uorder = UserOrderJsonUtils
				.parseUserFromJson(jsonData);
		Iterator iterator = uorder.iterator();
		iterator.hasNext();
		DriverListOrder orderL = (DriverListOrder) iterator.next();
		this.realname=orderL.getRealname();
		this.tel=orderL.getTel();

	}
	


	
	
}
