package com.red263.commmodule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.red263.Edaijia.Activity4;

import android.app.Application;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsynCollectList extends AsyncTask<String, Void, List<MapListImageAndText>>{
	private ProgressBar Pb;
	private Application App;
	List<MapListImageAndText> list;
	RefreshableListView ListView;
	Activity4 mActivity;
	TextView noinfo;

	public AsynCollectList(Application app,Activity4 context,ProgressBar pb,RefreshableListView listview,TextView noinfo){
		this.Pb=pb;
		this.App=app;
		this.ListView=listview;
		this.mActivity=context;
		this.noinfo=noinfo;
	}
	@Override
	protected List<MapListImageAndText> doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App,params[0].toString());
				if (!"false".equals(jsonData)) {
					JsonUtils jsonUtils = new JsonUtils();
					List<DriverListOrder> order = jsonUtils.parseUserFromJson(jsonData);

					list = new ArrayList<MapListImageAndText>();

					for (@SuppressWarnings("rawtypes")
					Iterator iterator = order.iterator(); iterator.hasNext();) {

						DriverListOrder orderL = (DriverListOrder) iterator.next();

						if (orderL.getAstate().equals("1")) {
							String score = "";
							String nowlocal = "";
							String state = "";
							String imageurl = "";
							String itemid = String.valueOf(orderL.getId());

							imageurl = LinkUrl.GetUrl(App, "upload/"
									+ orderL.getHeadimg());

							String realname = orderL.getRealname();

							if (orderL.getState().equals("0")) {
								state = "预约中";
							} else if (orderL.getState().equals("1")) {
								state = "服务中";
							} else {
								state = "空闲中";
							}

							if (!"".equals(orderL.getScore())) {
								score = "评分：" + orderL.getScore() + "分";
							} else {
								score = "评分：" + 0 + "分";
							}

							if ("".equals(orderL.getLocaladdr())) {
								nowlocal = "暂时无法定位";
							} else {
								nowlocal = orderL.getLocaladdr();
							}

							MapListImageAndText stringdate = new MapListImageAndText(
									imageurl, realname, state, score, nowlocal, itemid);
							list.add(stringdate);
						}

					}
				}
		return list;
	}



	@Override
	protected void onPostExecute(List<MapListImageAndText> result) {
		// TODO Auto-generated method stub
		if(result!=null){
		MapListImageAndTextListAdapter adapter = new MapListImageAndTextListAdapter(mActivity, result, ListView);
		ListView.setAdapter(adapter);
		}else{
			ListView.setVisibility(View.GONE);
			noinfo.setVisibility(View.VISIBLE);
			noinfo.setText("暂时没有任何收藏信息");
		}
		this.Pb.setVisibility(View.GONE);
//		if(proDialog!=null){
//		proDialog.dismiss();
//		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.Pb.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
