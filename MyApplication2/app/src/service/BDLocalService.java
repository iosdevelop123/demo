package service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import com.red263.commmodule.FunUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class BDLocalService extends Service {
	public String userId = "0";
	public String usertype = "0";
	public String louserid;
	public String lousertype;
	boolean State;
	private LocationClient mLocationClient = null;

	public String localAddr;
	public Double LocalLatitude = 0.00;
	public Double localLongitude = 0.00;
	BDLocation vLocation;

	public String url = "uplocal.php?userid=";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userId = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		// username = sharedPreferences.getString("m_username", "");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		init();
		startlocal();
	}

	private void startlocal() {
		mLocationClient = FunUtils.startlocal(this);

		mLocationClient.registerLocationListener(new BaiduLocalListener());

		if (mLocationClient == null)
			return;

		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}

	}

	public class BaiduLocalListener implements BDLocationListener {

		public BDLocation mLocation;
		bdlocalserT t;

		private BaiduLocalListener() {
			t = new bdlocalserT();
			if (!t.isAlive()) {
				t.start();
			}
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			} else {
				mLocation = location;
			}

			// 判断登录用户是否改变，目前所在坐标是否改变，一旦改变就更新数据库中驾用户的坐标和地址
			if (LocalLatitude != mLocation.getLatitude()
					|| localLongitude != mLocation.getLongitude()
					|| !userId.equals(louserid) || !usertype.equals(lousertype)) {

				t.setStartState();

			}
		}

		Handler bdlocalserH = new Handler() {
			public void handleMessage(Message msg) {
				State = msg.getData().getBoolean("State");
				if (State) {
					LocalLatitude = mLocation.getLatitude();
					localLongitude = mLocation.getLongitude();
					localAddr = mLocation.getAddrStr();
					louserid = userId;
					lousertype = usertype;

					t.setStopState();
				} else {
					Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>", "写入位置失败了！");
					t.setStopState();
				}
			}
		};

		class bdlocalserT extends Thread {

			private boolean isRun = false;

			public bdlocalserT() {
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
				// TODO Auto-generated method stub
				while (true) {

					if (isRun == true) {
						if (localcheck(userId, usertype,
								mLocation.getAddrStr(),
								String.valueOf(mLocation.getLatitude()),
								String.valueOf(mLocation.getLongitude()))) {
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putBoolean("State", true);
							message.setData(bundle);
							bdlocalserH.sendMessage(message);
						} else {
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putBoolean("State", false);
							message.setData(bundle);
							bdlocalserH.sendMessage(message);

						}
					}

				}

			}
		};

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private boolean localcheck(String userId, String usertype,
				String localAddr, String LocalLatitude, String localLongitude) {
			List params = new ArrayList();
			params.add(new BasicNameValuePair("userid", userId));
			params.add(new BasicNameValuePair("usertype", usertype));
			params.add(new BasicNameValuePair("localaddr", localAddr));
			params.add(new BasicNameValuePair("latitude", LocalLatitude));
			params.add(new BasicNameValuePair("longitude", localLongitude));
			// 这里换成你的验证地址
			String validateURL = com.red263.commmodule.LinkUrl.GetUrl(
					getApplication(), "uplocal.php");
			String State = FunUtils.getJsonforPost(params, validateURL);
			if ("true".equals(State)) {
				return true;
			} else {
				return false;
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

}
