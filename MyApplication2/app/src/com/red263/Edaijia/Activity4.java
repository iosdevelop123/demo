package com.red263.Edaijia;

import java.util.List;

import com.red263.Edaijia.R;
import com.red263.commmodule.AsynCollectList;
import com.red263.commmodule.AsyncUserInfo;
import com.red263.commmodule.MapListImageAndText;
import com.red263.commmodule.RefreshableListView;
import com.red263.commmodule.TopbarMain;
import com.red263.commmodule.RefreshableListView.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Activity4 extends Activity {

	private TopbarMain topbar;
	private RefreshableListView collectlist;

	String jsonData;
	String userid;
	String username;
	String usertype;
	TextView noinfo;
	TextView lv_id;
	ProgressBar pb;
	List<MapListImageAndText> list;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity4_layout);
		getview();
	}

	private void init() {

		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");

	}

	private void getview() {
		init();
		this.topbar = (TopbarMain) findViewById(R.id.topbarmain);
		
		AsyncUserInfo auf=new AsyncUserInfo(getApplication(),topbar);
		auf.execute("userinfo.php?id="+ userid + "&usertype="+usertype);

		collectlist = (RefreshableListView) findViewById(R.id.collectlist);
		setlistview();
		// Callback to refresh the list
		collectlist.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh(RefreshableListView listView) {
				// TODO Auto-generated method stub
				new NewDataTask().execute();

			}

		});
	}
	

	private void setlistview(){
	//	list = bind();

		collectlist.setOnItemClickListener(new ItemClick());
		
		pb=(ProgressBar)findViewById(R.id.pb);
        noinfo=(TextView)findViewById(R.id.noinfo);

		AsynCollectList asc=new AsynCollectList(getApplication(), this, pb, collectlist,noinfo);
		asc.execute("collectlist.php?userid=" + userid+ "&usertype=" + usertype);
	}


	private class NewDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			return "A new list item";
		}

		@Override
		protected void onPostExecute(String result) {
			 setlistview();
			// This should be called after refreshing finished
			collectlist.completeRefreshing();
			super.onPostExecute(result);
		}
	}

	/*
	 * item上的OnClick事件
	 */
	public final class ItemClick implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long id) {
			TextView tv_id = (TextView) arg1.findViewById(R.id.itemid);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			// 传递用户id，用户名及用户类型
			bundle.putString("MAP_DRIVERID", tv_id.getText().toString());
			// 通知传入的activity本界面的tabid
			bundle.putInt("MAP_INDEXTAB", 3);
			// bundle.putString("MAP_USERID", userid);
			// bundle.putString("MAP_USERTYPE", usertype);
			// bundle.putString("MAP_USERNAME", username);
			intent.putExtras(bundle);
			intent.setClass(getApplicationContext(), Sub_Activity.class);
			// 转向登陆后的页面
			startActivity(intent);
		}

	}
	

}