package com.red263.commmodule;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.app.Application;
import android.content.Context;

import android.util.Log;

public class FunUtils {

	/**
	 * get方式获得网页反馈信息
	 * 
	 * @param app
	 * @param UrlInfo
	 * @return
	 */
	public static String getJsonforGet(Application app, String UrlInfo) {
		// TODO Auto-generated method stub

		String strData = "";

		HttpResponse httpResponse = null;

		HttpClient client = new DefaultHttpClient();

		HttpGet myget = new HttpGet(LinkUrl.GetUrl(app, UrlInfo));

		try {
			httpResponse = client.execute(myget);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				strData = convertStreamToString(inputStream);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
			httpResponse = null;
		}

		return strData;

	}

	
	/**
	 * get方式获得网页反馈信息，通过Contex得到
	 * 
	 * @param app
	 * @param UrlInfo
	 * @return
	 */
	public static String getJsonforGetByContex(Context context, String UrlInfo) {
		// TODO Auto-generated method stub

		String strData = "";

		HttpResponse httpResponse = null;

		HttpClient client = new DefaultHttpClient();

		HttpGet myget = new HttpGet(LinkUrl.GetUrlByContex(context, UrlInfo));

		try {
			httpResponse = client.execute(myget);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				strData = convertStreamToString(inputStream);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
			httpResponse = null;
		}

		return strData;

	}
	
	/**
	 * POST方式获得反馈信息
	 * 
	 * @param params
	 * @param validateUrl
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getJsonforPost(List params, String validateUrl) {
		// 用于标记登陆状态
		HttpPost httpRequest = new HttpPost(validateUrl);
		// Post运作传送变数必须用NameValuePair[]阵列储存
		try {
			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP response
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出回应字串
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				return strResult;
			} else {
				return "false";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}

	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"),
					512 * 1024);
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			Log.e("DataProvier convertStreamToString", e.getLocalizedMessage(),
					e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 初始化一下本地定位
	 * @param context
	 * @return
	 */
	public static LocationClient startlocal(Context context) {
		LocationClient mLocationClient = null;
		mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
		option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setProdName("edaijialocal"); // 设置产品线名称
		option.setScanSpan(5000); // 定时定位，每隔5秒钟定位一次。
		mLocationClient.setLocOption(option);

		return mLocationClient;
	}

	
	

}
