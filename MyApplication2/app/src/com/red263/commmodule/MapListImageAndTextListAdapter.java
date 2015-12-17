package com.red263.commmodule;

import java.util.List;

import com.red263.Edaijia.R;
import com.red263.commmodule.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MapListImageAndTextListAdapter extends ArrayAdapter<MapListImageAndText> {

    private ListView listView;
    private AsyncImageLoader asyncImageLoader;

    public MapListImageAndTextListAdapter(Activity activity, List<MapListImageAndText> imageAndTexts, ListView listView) {
        super(activity, 0, imageAndTexts);
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
    }


	public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();

        // Inflate the views from XML
        View rowView = convertView;
        MapListViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.driver_list_item, null);
            viewCache = new MapListViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (MapListViewCache) rowView.getTag();
        }
        MapListImageAndText imageAndText = getItem(position);

        // Load the image and set it on the ImageView
        String imageUrl = imageAndText.getHeadimgUrl();
        ImageView headimg = viewCache.getHeadimgView();
     //   headimg.setTag(imageUrl);
        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
        	
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                
            	ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
                
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
  
                }
            }
        });
		if (cachedImage == null) {
			headimg.setImageResource(R.drawable.find_friend_contact_icon);
		}else{
			headimg.setImageDrawable(cachedImage);

			//headimg.setImageResource(R.drawable.img_loading);
		}
        // Set the text on the TextView
		TextView itemid = viewCache.getItemid();
		itemid.setText(imageAndText.getItemid());
        
        TextView realname = viewCache.getRealname();
        realname.setText(imageAndText.getRealname());
        
        TextView state = viewCache.getState();
        state.setText(imageAndText.getState());
        
        TextView score = viewCache.getScore();
        score.setText(imageAndText.getScore());
        
        TextView nowlocal = viewCache.getNowlocal();
        nowlocal.setText(imageAndText.getNowlocal());
        
        return rowView;
    }

}