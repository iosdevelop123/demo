package com.red263.commmodule;

import com.red263.Edaijia.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MapListViewCache {

	    private View baseView;
	    //
	    private TextView itemid;
	    private TextView realname;
	    private TextView state;
	    private TextView score;
	    private TextView nowlocal;
        //
	    private ImageView headimgView;

	    public MapListViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public ImageView getHeadimgView() {
	        if (headimgView == null) {
	        	headimgView = (ImageView) baseView.findViewById(R.id.headimg);
	        }
	        return headimgView;
	    }
	    
	    public TextView getItemid() {
	        if (itemid == null) {
	        	itemid = (TextView) baseView.findViewById(R.id.itemid);
	        }
	        return itemid;
	    }
	    
	    public TextView getRealname() {
	        if (realname == null) {
	        	realname = (TextView) baseView.findViewById(R.id.realname);
	        }
	        return realname;
	    }
	    
	    public TextView getState() {
	        if (state == null) {
	        	state = (TextView) baseView.findViewById(R.id.state);
	        }
	        return state;
	    }
	    
	    public TextView getScore() {
	        if (score == null) {
	        	score = (TextView) baseView.findViewById(R.id.score);
	        }
	        return score;
	    }
	    
	    public TextView getNowlocal() {
	        if (nowlocal == null) {
	        	nowlocal = (TextView) baseView.findViewById(R.id.nowlocal);
	        }
	        return nowlocal;
	    }

}