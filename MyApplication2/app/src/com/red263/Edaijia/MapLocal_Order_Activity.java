package com.red263.Edaijia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.MapController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.red263.commmodule.FunUtils;
import com.red263.commmodule.TopBarMap;
import com.red263.commmodule.UserJsonUtils;
import com.red263.commmodule.UserOrder;
public class MapLocal_Order_Activity extends Activity {

	 //
		String driverid ;
		String orderid ;
		String fuserid ;
		String userid ;
		String usertype;
		String username;
		//
		BMapManager mBMapMan = null;
		MapView mMapView = null;
		// 定位
		private LocationClient mLocationClient = null;
		MapController mMapController;
//		//初始化一下位置
		
	   //
		MyLocationOverlay myLocationOverlay;
		bindthread bdt2;


		boolean isDestroy=false;
		
		private TopBarMap topbar;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			init();
			startlocal();

			mBMapMan = new BMapManager(getApplication());
			//初始化滴入api key
			mBMapMan.init("AFCF531F5FF1F26E8C36B37BD972764484B082DD", null);
			// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错

			setContentView(R.layout.maplocal_activity);
			topbar=(TopBarMap)findViewById(R.id.topbar);
			topbar.setActivityOn("order");

			mMapView = (MapView) findViewById(R.id.bmapsView);
			mMapView.setBuiltInZoomControls(true);
			// 设置启用内置的缩放控件
			mMapController = mMapView.getController();
			// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
			// GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
			// (int) (116.404 * 1E6));
			// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
			// mMapController.setCenter(point);// 设置地图中心点
			mMapController.setZoom(15);// 设置地图zoom级别
			//
	
			bdt2 = new bindthread();
		
		//	handler.postDelayed(runnable, 2000);// 每两秒执行一次runnable.

//			getdriverlocal(mMapController);

		
		}
		
		private void getdriverlocal(MapController mMapController,
				String driverlocallongitude, String driverloacllatitude){

	        /**************************************/
			if(!isDestroy){
			mMapView.getOverlays().clear();
			
			mMapView.getOverlays().add(myLocationOverlay);
			//	mMapView.refresh();
//				mMapView.getController().animateTo(
//						new GeoPoint((int) (locData.latitude * 1e6),
//								(int) (locData.longitude * 1e6)));
				
			List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
			GeoPoint p1 = new GeoPoint((int) (Double.parseDouble( driverloacllatitude)* 1E6),
					(int) (Double.parseDouble( driverlocallongitude) * 1E6));
			mMapController.setCenter(p1);// 设置地图中心点
			
			GeoList.add(new OverlayItem(p1, "P1", "point1"));
			Drawable marker = getResources().getDrawable(R.drawable.lbs_selected); // 得到需要标在地图上的资源
			OverItemO ov;
			if("0".equals(usertype)){
				//用户看驾驶员
				 ov= new OverItemO(marker, GeoList, this,driverid,usertype);

			}else{
				//驾驶员看用户的
				ov= new OverItemO(marker, GeoList, this,fuserid,usertype);
			}
			
			mMapView.getOverlays().add(ov); // 添加ItemizedOverlay实例到mMapView
			GeoList.remove("P1");

			mMapView.refresh();// 刷新地图
			mMapController.animateTo(p1);
			}
	        /**************************************/
		}
		
		
		
		// 定时器
//		Handler handler = new Handler();
//		Runnable runnable = new Runnable() {
//			@Override
//			public void run() {
//
//				//
//			    
//	            bind();
//
//	            getdriverlocal(mMapController);
//				handler.postDelayed(this, 9999);
//			}
//		};
//		
		private void init(){
			Intent intent = getIntent();
			

			driverid = intent.getStringExtra("MAP_DRIVERID");
			orderid = intent.getStringExtra("MAP_ORDERID");
			fuserid = intent.getStringExtra("MAP_FUSERID");

			SharedPreferences sharedPreferences = getSharedPreferences("userdate",
					Context.MODE_PRIVATE);
			// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
			userid = sharedPreferences.getString("m_userid", "");
			usertype = sharedPreferences.getString("m_usertype", "");
			username = sharedPreferences.getString("m_username", "");
		}
		


		Handler hd2 = new Handler() {
			@SuppressWarnings("rawtypes")
			public void handleMessage(Message msg) {
				String jData = msg.getData().getString("jsonData");

				if (!"false".equals(jData)) {
					UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
					List<UserOrder> uorder = UserJsonUtils.parseUserFromJson(jData);
					Iterator iterator = uorder.iterator();
					iterator.hasNext();
					UserOrder orderL = (UserOrder) iterator.next();

					getdriverlocal(mMapController, orderL.getLocallongitude(),
							orderL.getLoacllatitude());

					// 执行完一次就停止
					bdt2.setStopState();
				}

			}
		};
		

		class bindthread extends Thread {
			private boolean isRun = false;

			public bindthread() {
				isRun = false;

			}

			public void setStopState() {
				isRun = false;
			}

			public void setStartState() {
				isRun = true;
			}

			public boolean getrunstate() {
				return isRun;
			}

			@Override
			public void run() {
				
				while (true) {
					if (isRun) {
						
			
						String jsonData;
						if("1".equals(usertype)){
				            //驾驶员看用户的
							 jsonData = FunUtils.getJsonforGet(getApplication(),"userinfo.php?id="
										+ fuserid + "&usertype=0");
						}else{
							//用户看驾驶员的
							 jsonData = FunUtils.getJsonforGet(getApplication(),"userinfo.php?id="
										+ driverid + "&usertype=1");
						
			
						}
						
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("jsonData", jsonData);
						message.setData(bundle);
						hd2.sendMessage(message);
					}
				}
			}
		}
//		
//		@SuppressWarnings("rawtypes")
//		private void bind() {
//		
//			String jsonData2;
//			if("1".equals(usertype)){
//	            //驾驶员看用户的
//				 jsonData2 = FunUtils.getJsonforGet(getApplication(),"userinfo.php?id="
//							+ fuserid + "&usertype=0");
//			}else{
//				//用户看驾驶员的
//				 jsonData2 = FunUtils.getJsonforGet(getApplication(),"userinfo.php?id="
//							+ driverid + "&usertype=1");
//			
//
//			}
//
//
//			UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
//			List<UserOrder> uorder = UserJsonUtils.parseUserFromJson(jsonData2);
//			Iterator iterator = uorder.iterator();
//			iterator.hasNext();
//			UserOrder orderL = (UserOrder) iterator.next();
//			driverloacllatitude=orderL.getLoacllatitude();
//			driverlocallongitude=orderL.getLocallongitude();
//			driverlocaladdr=orderL.getLocaladdr();
//
//
//		}
		

		private void startlocal() {

			mLocationClient=FunUtils.startlocal(this);

			mLocationClient.registerLocationListener(new GetLocalListener());

			if (mLocationClient == null)
				return;
			if (!mLocationClient.isStarted()) {
				mLocationClient.start();
			}

		}

		@Override
		protected void onDestroy() {
			//注销后移除定时器
	//		handler.removeCallbacks(runnable);
			isDestroy=true;
			mMapView.destroy();
			if (mBMapMan != null) {
				mBMapMan.destroy();
				mBMapMan = null;
			}

			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.stop();
				mLocationClient = null;
			}
			//关闭该页面获取数据的线程
		//	android.os.Process.killProcess(android.os.Process.myPid()) ;
			finish();
			super.onDestroy();
		}

		@Override
		protected void onPause() {
			mMapView.onPause();
			if (mBMapMan != null) {
				mBMapMan.stop();
			}
			super.onPause();
		}

		@Override
		protected void onResume() {
			mMapView.onResume();
			if (mBMapMan != null) {
				mBMapMan.start();
			}
			super.onResume();
		}

		public class GetLocalListener implements BDLocationListener {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;

				myLocationOverlay = new MyLocationOverlay(
						mMapView);
				LocationData locData = new LocationData();
				// 百度定位SDK获取位置信息，要在SDK中显示一个位置，需要
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				myLocationOverlay.setData(locData);
				
				if (!bdt2.isAlive()) {		
					bdt2.start();
				}
				
				bdt2.setStartState();
				// 判断登录用户是否改变，目前所在坐标是否改变，一旦改变就更新数据库中驾用户的坐标和地址
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub

			}

		}
}
