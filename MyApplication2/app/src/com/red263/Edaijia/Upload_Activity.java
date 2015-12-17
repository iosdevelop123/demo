package com.red263.Edaijia;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.red263.commmodule.LinkUrl;
import com.red263.commmodule.Upadateimg_asynctask;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Upload_Activity extends Activity {

	// 要上传的文件路径，理论上可以传输任何文件，实际使用时根据需要处理
	// 服务器上接收文件的处理页面，这里根据需要换成自己的
	private String actionUrl;

	private Button mButton;
	ImageView SelectImg;
	ProgressBar pb;
	ImageView stateimg;

	private String ImagePath = "";

	private String pathname;
	String validateURL;

	String usertype;
	String userid;
	String username;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_activity);

		init();
		pathname = Environment.getExternalStorageDirectory() + "";

		actionUrl = LinkUrl.GetUrl(getApplication(), "upimg.php");

		pb = (ProgressBar) findViewById(R.id.upstatebar);
		stateimg = (ImageView) findViewById(R.id.stateimg);

		validateURL = com.red263.commmodule.LinkUrl.GetUrl(getApplication(),
				"upimgindatabase.php");
		/* 设置mButton的onClick事件处理 */
		mButton = (Button) findViewById(R.id.myButton);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Upadateimg_asynctask up = new Upadateimg_asynctask(pb, stateimg);
				up.execute(actionUrl, ImagePath, validateURL, userid, usertype);

			}
		});

		SelectImg = (ImageView) findViewById(R.id.SelectImg);
		SelectImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("filepath", pathname);
				intent.setClass(getApplicationContext(),
						com.red263.commmodule.FileDialog.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	private void init() {
		SharedPreferences sharedPreferences = getSharedPreferences("userdate",
				Context.MODE_PRIVATE);
		// getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		userid = sharedPreferences.getString("m_userid", "");
		usertype = sharedPreferences.getString("m_usertype", "");
		username = sharedPreferences.getString("m_username", "");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {

		case RESULT_OK:
			ImagePath = data.getStringExtra("ReturnRes");// str即为回传的值
			// ImageName= data.getStringExtra("ImgName");// str即为回传的值
			// Log.v(">>>>>>>>>>>>", ImageName);
			// Log.v(">>>>>>>>>>>>", ImagePath);
			Bitmap bitmap = BitmapFactory.decodeFile(ImagePath);
			switch (requestCode) {
			case 0:
				SelectImg.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		default:
			break;

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// 关闭该页面获取数据的线程
		finish();
		super.onDestroy();
	}


}
