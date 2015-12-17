package com.red263.commmodule;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.red263.Edaijia.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FileDialog extends Activity {
	 GridView gridview;
	    SimpleAdapter gridviewAdapter;
	    ImageButton backFolder,CloseBtn;
	    String sdcardFilePath,thisFilePath,selectFilePath;
	    
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
	        this.setContentView(R.layout.menuaddgridview);
	        
	        sdcardFilePath=Environment.getExternalStorageDirectory().getAbsolutePath();//得到sdcard目录
	        Intent getpathIntent=getIntent();
	        if(getpathIntent.getStringExtra("filepath")!=null){
	        	
	        	File file = new File(getpathIntent.getStringExtra("filepath"));
	        	//判断文件夹是否存在,如果不存在则创建文件夹
	        	if (!file.exists()) {
	        		//不存在目录就打开sd卡目录
	        		thisFilePath=sdcardFilePath;
	        	}else{
	        		
	            	thisFilePath=getpathIntent.getStringExtra("filepath");
	        	}
	        }else{
	                thisFilePath=sdcardFilePath;
	        }
	        //*/

	        CloseBtn=(ImageButton)this.findViewById(R.id.MenuClose_button);
	        CloseBtn.setOnClickListener(new buttonOnClickListener());
	  
	        backFolder=(ImageButton) this.findViewById(R.id.MenuAddGridView_button_backFolder);
	        backFolder.setOnClickListener(new buttonOnClickListener());
	        
	        gridview=(GridView) this.findViewById(R.id.MenuAddGridView_gridview);
	        //设置gridView的数据
	        updategridviewAdapter(thisFilePath);
	        gridview.setOnItemClickListener(new OnItemClickListener(){
	            @SuppressWarnings("unchecked")
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                    long arg3) {
	                HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
	                selectFilePath=(String) item.get("ItemFilePath");
	                
	                if(item.get("type").equals("isDirectory"))//判断是否是文件夹
	                {
	                	
	                    updategridviewAdapter(selectFilePath);//获取文件夹下数据并显示
	                    thisFilePath=selectFilePath;//记录当前目录路径

	                }
	                else if(item.get("type").equals("isImg"))
	                {

	                	//获取当前文件路径
	                	String FilePath=thisFilePath+"/"+item.get("ItemText").toString();
	                	
	                	
	                
	                	Intent intent = new Intent();  
	                	intent.putExtra("ReturnRes", FilePath);
	                	intent.putExtra("ImgName", item.get("ItemText").toString());
	                	setResult(RESULT_OK, intent);
	                	Toast.makeText(getApplicationContext(), "图片选择成功!",
	                  		     Toast.LENGTH_SHORT).show();
	                	finish();
	                }
	                else
	                {
	                  
	                	Toast.makeText(getApplicationContext(), "只能选择图片类型!",
	                		     Toast.LENGTH_SHORT).show();
	                }
	                
	            }});
	            this.setResult(0);
	    }
	    private File[] folderScan(String path)
	    {
	        File file=new File(path);
	        File[] files=file.listFiles();
	        return files;
	    }
	    //设置gridView的数据
	    private void updategridviewAdapter(String filePath)
	    {
	        File[] files=folderScan(filePath);
	        ArrayList<HashMap<String, Object>> lstImageItem = getFileItems(files);
	        gridviewAdapter = new SimpleAdapter(FileDialog.this,lstImageItem,R.layout.menuaddgridview_item,new String[] {"ItemImage","ItemText"}, new int[] {R.id.MenuAddGridView_ItemImage,R.id.MenuAddGridView_ItemText});
	        gridview.setAdapter(gridviewAdapter);
	        gridviewAdapter.notifyDataSetChanged();
	    }
	    //列表循环判断文件类型然后提供数据给Adapter用
	    private ArrayList<HashMap<String, Object>> getFileItems(File[] files)
	    {
	        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
	        //循环加入listImageItem数据
	        if(files==null)
	        {
	            return null;
	        }
	        for(int i=0;i<files.length;i++)
	        {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            String fileName=files[i].getName();//得到file名
	            map.put("ItemText", fileName);
	            if(files[i].isDirectory())//判断是否是文件夹
	            {
	                map.put("ItemImage", R.drawable.folder);//显示文件夹图标
	                map.put("type", "isDirectory");
	            }
	            else if(files[i].isFile())//判断是否是文件
	            {
	            
	            	if(fileName.contains(".jpg")||fileName.contains(".JPG")||fileName.contains(".gif")||fileName.contains(".GIF")||fileName.contains(".PNG")||fileName.contains(".png"))//判断是否是图片文件
	                {
	            		map.put("ItemImage",files[i].getAbsolutePath());//加入图片图标
	                    map.put("type", "isImg");
	                }
	                else
	                {
	                    map.put("ItemImage", R.drawable.otherfile);//加入非图片文件图标
	                    map.put("type", "isOthers");
	                }
	                //*/
	            }
	            map.put("ItemFilePath", files[i].getAbsolutePath());//保存文件绝对路径
	            
	            lstImageItem.add(map);
	        }
	        return lstImageItem;
	    }

	    class buttonOnClickListener implements OnClickListener
	    {
	        ArrayList<String> musicResult;
	        public void onClick(View v) {
	            switch(v.getId())
	            {
	                case R.id.MenuClose_button://关闭窗口
	                    finish();
	                    break;
	                case R.id.MenuAddGridView_button_backFolder://返回上级目录
	                    if(!thisFilePath.equals(sdcardFilePath))
	                    {
	                        File thisFile=new File(thisFilePath);//得到当前目录
	                        String parentFilePath=thisFile.getParent();//上级的目录路径
	                        updategridviewAdapter(parentFilePath);//获得上级目录数据并显示
	                        thisFilePath=parentFilePath;//设置当前目录路径 
	                    }
	                    else
	                    {
	                        FileDialog.this.onDestroy();
	                    }
	                    break;
	            }
	        }
	        
	    }
	    protected void onDestroy() {
	        this.finish();
	        super.onDestroy();
	    }
	    
	}
