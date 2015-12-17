package com.red263.commmodule;

import java.util.Iterator;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;

//异步加载头部导航
public class AsyncUserInfo extends AsyncTask<String, Void, String> {

	Application Ap;
	TopbarMain Topbar;


	public AsyncUserInfo(Application ap,TopbarMain topbar) {
		this.Ap = ap;
		this.Topbar=topbar;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String Url=params[0].toString();
		String jsonData = FunUtils.getJsonforGet(Ap, Url);
		return jsonData;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(!"false".equals(result)){
			UserJsonUtils UserJsonUtils = new com.red263.commmodule.UserJsonUtils();
			List<UserOrder> uorder = UserJsonUtils.parseUserFromJson(result);
			Iterator iterator = uorder.iterator();
			iterator.hasNext();
			UserOrder orderL = (UserOrder) iterator.next();
			this.Topbar.setHeadImg(LinkUrl.GetUrl(Ap, "upload/"+ orderL.getHeadimg()));
			
			String username=orderL.getName();
			if (username != null && !"".equals(username)) {
				// */
				Topbar.setTextViewText(username);
				Topbar.setLoginState(true);
			} else {
				// 未登录状态下显示为
				Topbar.setTextViewText("游客");
				Topbar.setLoginState(false);
			}
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
