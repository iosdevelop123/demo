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
import com.red263.commmodule.DriverListOrder;
import com.red263.commmodule.FunUtils;
import com.red263.commmodule.JsonUtils;
import com.red263.commmodule.TopBarMap;

public class MapLocal_Activity extends Activity {
	//
	String driverid;
	String userid;
	String usertype;
	String username;
	boolean isDestroy=false;
	//
	BMapManager mBMapMan = null;
	public MapView mMapView = null;
	// 定位
	LocationClient mLocationClient = null;
	MapController mMapController;

	private TopBarMap topbar;
	bindthread bdt;
	//
	MyLocationOverlay myLocationOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		startlocal();
		mBMapMan = new BMapManager(getApplication());
		// 初始化滴入api key
		mBMapMan.init("AFCF531F5FF1F26E8C36B37BD972764484B082DD", null);
		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错

		setContentView(R.layout.maplocal_activity);
		topbar = (TopBarMap) findViewById(R.id.topbar);
		topbar.setDriverid(driverid);

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
		bdt = new bindthread();

	}

	private void getdriverlocal(MapController mMapController,
			String driverlocallongitude, String driverloacllatitude) {
		/**************************************/
		if(!isDestroy){
		 
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(myLocationOverlay);

		List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		GeoPoint p1;
		if (driverlocallongitude != null && driverloacllatitude != null
				&& driverlocallongitude != "" && driverloacllatitude != "") {
			p1 = new GeoPoint(
					(int) (Double.parseDouble(driverloacllatitude) * 1E6),
					(int) (Double.parseDouble(driverlocallongitude) * 1E6));
		} else {
			p1 = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
		}
		mMapController.setCenter(p1);// 设置地图中心点
		GeoList.add(new OverlayItem(p1, "P1", "point1"));
		Drawable marker = getResources().getDrawable(R.drawable.lbs_selected); // 得到需要标在地图上的资源
		OverItemT ov = new OverItemT(marker, GeoList, this, driverid);
		mMapView.getOverlays().add(ov); // 添加ItemizedOverlay实例到mMapView
		GeoList.remove("P1");
		mMapView.refresh();// 刷新地图
		// mMapController.animateTo(p1);
		}
		/**************************************/
	}

	private void init() {
		Intent intent = getIntent();
		driverid = intent.getStringExtra("MAP_DRIVERID");
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
				JsonUtils UserOrderJsonUtils = new com.red263.commmodule.JsonUtils();
				List<DriverListOrder> uorder = UserOrderJsonUtils
						.parseUserFromJson(jData);
				Iterator iterator = uorder.iterator();
				iterator.hasNext();
				DriverListOrder orderL = (DriverListOrder) iterator.next();

				getdriverlocal(mMapController, orderL.getLocallongitude(),
						orderL.getLoacllatitude());

				// 执行完一次就停止
				bdt.setStopState();
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
					String jsonData = FunUtils.getJsonforGet(getApplication(),
							"driverinfo.php?id=" + driverid);
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("jsonData", jsonData);
					message.setData(bundle);
					hd2.sendMessage(message);

				}
			}
		}
	}

	private void startlocal() {

		mLocationClient = FunUtils.startlocal(this);

		mLocationClient.registerLocationListener(new GetLocalListener());

		if (mLocationClient == null)
			return;
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}

	}

	@Override
	protected void onDestroy() {
		// 注销后移除定时器
		// handler.removeCallbacks(runnable);
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
		finish();
		super.onDestroy();
		// 关闭该页面获取数据的线程
	 //	android.os.Process.killProcess(android.os.Process.);
		// System.exit(CONTEXT_INCLUDE_CODE);
		

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
			myLocationOverlay = new MyLocationOverlay(mMapView);
			LocationData locData = new LocationData();
			// 百度定位SDK获取位置信息，要在SDK中显示一个位置，需要
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			myLocationOverlay.setData(locData);
			
			if (!bdt.isAlive()) {		
				bdt.start();
			}
			// 判断登录用户是否改变，目前所在坐标是否改变，一旦改变就更新数据库中驾用户的坐标和地址
			bdt.setStartState();
			// mMapView.getOverlays().add(myLocationOverlay);
		
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}
	


}
