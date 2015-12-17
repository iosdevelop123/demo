package com.red263.Edaijia;

import java.util.List;

import com.red263.Edaijia.R;
import com.red263.commmodule.AsynOrderList;
import com.red263.commmodule.AsyncUserInfo;
import com.red263.commmodule.ListImageAndText;
import com.red263.commmodule.RefreshableListView;
import com.red263.commmodule.TopbarMain;
import com.red263.commmodule.RefreshableListView.OnRefreshListener;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class Activity2 extends Activity {

	private RefreshableListView orderlist;
	private TopbarMain topbar;

	List<ListImageAndText> list;
	
	TextView noinfo;
	String jsonData;

	String userid;
	String usertype;
	String username;
	ProgressBar pb;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2_layout);
		findViews();

	}

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}

	private void findViews() {
		init();
		topbar = (TopbarMain) findViewById(R.id.topbarmainb);
		
		AsyncUserInfo auf=new AsyncUserInfo(getApplication(),topbar);
		auf.execute("userinfo.php?id="+ userid + "&usertype="+usertype);

//		if (username != null && !"".equals(username)) {
//			// */
//			topbarmain.setTextViewText(username);
//			topbarmain.setLoginState(true);
//		} else {
//			// 未登录状态下显示为
//			topbarmain.setTextViewText("游客");
//			topbarmain.setLoginState(false);
//		}
		orderlist = (RefreshableListView) findViewById(R.id.orderlist);

        setlistview();

		orderlist.setOnItemClickListener(new ItemClick());

		// Callback to refresh the list
		orderlist.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh(RefreshableListView listView) {
				// TODO Auto-generated method stub
				new NewDataTask().execute();

			}
		});
	}
	
	private void setlistview(){
		pb=(ProgressBar)findViewById(R.id.pb);
		noinfo=(TextView) findViewById(R.id.noinfo);
		//list = bind();
		// driverlist.setAdapter(listItemAdapter);
	
		AsynOrderList aso=new AsynOrderList(getApplication(), this, pb, orderlist,noinfo);
        aso.execute("orderlist.php?id=" + userid+ "&usertype=" + usertype);
		orderlist.setOnItemClickListener(new ItemClick());
		
//		ListImageAndTextListAdapter adapter = new ListImageAndTextListAdapter(
//				this, list, orderlist);
//		orderlist.setAdapter(adapter);
	}

	// @Override
	// protected void onRestart() {
	// // TODO Auto-generated method stub
	// super.onRestart();
	//
	// new NewDataTask().execute();
	// }

	/*
	 * item上的OnClick事件
	 */
	public final class ItemClick implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long id) {
			TextView tv_driverid = (TextView) arg1.findViewById(R.id.driverid);
			TextView tv_orderid = (TextView) arg1.findViewById(R.id.orderid);
			TextView tv_userid = (TextView) arg1.findViewById(R.id.userid);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			// 传递用户id，用户名及用户类型
			bundle.putString("MAP_DRIVERID", tv_driverid.getText().toString());
			bundle.putString("MAP_ORDERID", tv_orderid.getText().toString());
			bundle.putString("MAP_FUSERID", tv_userid.getText().toString());

			intent.putExtras(bundle);
			intent.setClass(getApplicationContext(), Order_Activity.class);
			// 转向登陆后的页面
			startActivity(intent);
		}

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
			orderlist.completeRefreshing();
			super.onPostExecute(result);
		}
	}
	
}