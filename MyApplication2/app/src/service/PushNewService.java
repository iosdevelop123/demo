package service;

import com.red263.Edaijia.MainActivity;
import com.red263.Edaijia.R;
import com.red263.commmodule.FunUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.app.Notification;

public class PushNewService extends Service {

	public String userId = "0";
	public String usertype = "0";

	public String type = "";

	public String url = "hasnew.php?userid=";

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
		// userId = intent.getStringExtra("MAP_USERID");
		// usertype = intent.getStringExtra("MAP_USERTYPE");
		init();
			if (!t2.isAlive()) {
				t2.start();
			}

		super.onStart(intent, startId);

	}

	Handler h2 = new Handler() {
		public void handleMessage(Message msg) {
			int nownum = msg.getData().getInt("sdgnum");
			NotificationMessage(MainActivity.class, userId, "你有一条新的预约",
					"你有一条新的预约", "点击此处查看", nownum);
		}
	};

	// */

	Runnable runnable = new Runnable() {
		@Override
		public void run() {

			String dgval = FunUtils.getJsonforGet(getApplication(), url
					+ userId + "&usertype=" + usertype);
			int dgnum = Integer.parseInt(dgval);
			//
			// System.out.println("第一次启动");

			while (true) {
				String sdgnumx = FunUtils.getJsonforGet(getApplication(), url
						+ userId + "&usertype=" + usertype);
				Integer sdgnum ;
				if(sdgnumx==""){
					sdgnum=0;
				}else{
					sdgnum = Integer.parseInt(sdgnumx);
				}
				
				if (sdgnum > dgnum) {
					// NotificationMessage
					// (MainActivity.class,userId,"你有一条新的预约","你有一条新的预约","点击此处查看",sdgnum);
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("sdgnum", sdgnum);
					message.setData(bundle);
					h2.sendMessage(message);
					dgnum = sdgnum;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} //

		}
	};

	Thread t2 = new Thread(runnable);

	/**
	 * 
	 * @param cls
	 *            //点击消息启动的页面
	 * @param iuserId
	 *            //传递的参数
	 * @param tickerText
	 *            //提示消息
	 * @param title
	 *            //消息标题
	 * @param contentText
	 *            //消息内容
	 */
	public void NotificationMessage(Class<?> cls, String iuserId,
			String tickerText, String title, String contentText, Integer ins) {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.main_menu_9_pay,
				tickerText, System.currentTimeMillis());

		n.flags = Notification.FLAG_AUTO_CANCEL;
		
		Intent i = new Intent(getApplicationContext(), cls);
		i.putExtra("MAP_TABINDEX", 1);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(getApplicationContext(), title, contentText,
				contentIntent);
		nm.notify(ins, n);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

}
