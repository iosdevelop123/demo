package com.red263.commmodule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import com.red263.Edaijia.R;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
//异步上传
public class Upadateimg_asynctask extends AsyncTask<String, Void, Boolean>{

	private ProgressBar Pb;
	private ImageView Img;
	public Upadateimg_asynctask(ProgressBar pb,ImageView img) {
		this.Pb=pb;
		this.Img=img;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//上传开始时，显示ProgressBar
		this.Pb.setVisibility(View.VISIBLE);
		this.Img.setVisibility(View.GONE);
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		
		//String ImageName=uploadFile(params[0].toString(),params[1].toString());
		String ImageName=upimg(params[0].toString(),params[1].toString());
		
		if("false".equals(ImageName)){
			//Toast.makeText(Upload_Activity.this, "上传失败！", Toast.LENGTH_LONG).show();
			return false;
		}else{
			
			if(upimgdatebase(params[2].toString(),params[3].toString(),params[4].toString(),ImageName)){
				return true;

			}else{				
                return false;
			}
		}
		
		
//	return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		this.Pb.setVisibility(View.GONE);
		this.Img.setVisibility(View.VISIBLE);
		if(!result){
			this.Img.setImageResource(R.drawable.tribe_mute);
		}
	
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean upimgdatebase(String UrlInfo,String userid,String usertype,String img) {
		List params = new ArrayList();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("usertype", usertype));
		params.add(new BasicNameValuePair("headimg", img));
		// 这里换成你的验证地址

		String State = FunUtils.getJsonforPost(params, UrlInfo);
		if ("true".equals(State)) {
			return true;
		} else {
			return false;
		}
	}

	
	private String upimg(String postURL,String imgpath){
		File file = new File(imgpath);
		try {
		         HttpClient client = new DefaultHttpClient();  
		        // String postURL = "http://someposturl.com";
		         HttpPost post = new HttpPost(postURL); 
		     FileBody bin = new FileBody(file);
		     MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
		     reqEntity.addPart("uploadedfile", bin);
		     post.setEntity(reqEntity);  
		     HttpResponse response = client.execute(post);  
		     HttpEntity resEntity = response.getEntity();  
		     if (resEntity != null) {    
		            //   Log.i(">>>>>>>>>>>>>>>>>",EntityUtils.toString(resEntity));
		               return EntityUtils.toString(resEntity);
		         }else{
		        	 return "false";
		         }
		} catch (Exception e) {
		    e.printStackTrace();
		    return "false";
		}
	}
	
	


	
}
