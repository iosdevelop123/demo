package com.red263.commmodule;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.red263.Edaijia.Activity2;

import android.app.Application;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsynOrderList extends AsyncTask<String, Void, List<ListImageAndText>>{
	private ProgressBar Pb;
	private Application App;
	List<ListImageAndText> list;
	RefreshableListView Orderlist;
	Activity2 mActivity;
	TextView noinfo;
	public AsynOrderList(Application app,Activity2 act,ProgressBar pb,RefreshableListView orderlist,TextView noinfo){
		this.Pb=pb;
		this.App=app;
		this.Orderlist=orderlist;
		this.mActivity=act;
		this.noinfo=noinfo;
	}
	
	
	@Override
	protected List<ListImageAndText> doInBackground(String... params) {
		// TODO Auto-generated method stub
		String jsonData = FunUtils.getJsonforGet(App, params[0].toString());
		if (!"false".equals(jsonData)) {
			OrderJsonUtils jsonUtils = new OrderJsonUtils();
			List<OrderListOrder> order = jsonUtils.parseUserFromJson(jsonData);
			list = new ArrayList<ListImageAndText>();
			for (@SuppressWarnings("rawtypes")
			Iterator iterator = order.iterator(); iterator.hasNext();) {
				OrderListOrder orderL = (OrderListOrder) iterator.next();
           			String score = "";
					String state = "";
					String imageurl = "";
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
					String adate=orderL.getDate();
					String driverid=orderL.getFordriverid();
					String orderid=orderL.getId();
					String fuserid= orderL.getUserid();
					
					ListImageAndText stringdate = new ListImageAndText(
							imageurl, realname, state, score, adate, driverid,orderid,fuserid);
					list.add(stringdate);
				}

		}
		return list;
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.Pb.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}


	@Override
	protected void onPostExecute(List<ListImageAndText> result) {
		// TODO Auto-generated method stub
		
		if(result!=null){
		ListImageAndTextListAdapter adapter = new ListImageAndTextListAdapter(mActivity, result, Orderlist);
		Orderlist.setAdapter(adapter);
		}else{
		Orderlist.setVisibility(View.GONE);
		noinfo.setVisibility(View.VISIBLE);
		noinfo.setText("暂时没有任何订单信息");
		}
		this.Pb.setVisibility(View.GONE);
		
		super.onPostExecute(result);
	}


	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
