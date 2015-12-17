package com.red263.commmodule;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Application;
import android.os.AsyncTask;

public class AsynBDLocal extends AsyncTask<String, Void, Void>{

	Application App;
	public AsynBDLocal(Application app){
		this.App=app;
	}
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		collectcheck(App,params[0].toString(),params[1].toString(),params[2].toString(),params[3].toString(),params[4].toString());
	
		
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean collectcheck(Application mApp,String userId,String usertype,String localAddr,String LocalLatitude,String localLongitude){
		List params = new ArrayList();
		params.add(new BasicNameValuePair("userid", userId));
		params.add(new BasicNameValuePair("usertype", usertype));
		params.add(new BasicNameValuePair("localaddr", localAddr));
		params.add(new BasicNameValuePair("latitude", LocalLatitude));
		params.add(new BasicNameValuePair("longitude", localLongitude));
		// 这里换成你的验证地址
		String validateURL = com.red263.commmodule.LinkUrl
				.GetUrl(mApp,"uplocal.php");
		String State =FunUtils.getJsonforPost(params, validateURL);
		if("true".equals(State)){
			return true;
		}else{
			return false;
		}
	 //	return false;
	}
}
