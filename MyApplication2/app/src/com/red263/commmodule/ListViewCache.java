package com.red263.commmodule;

import com.red263.Edaijia.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewCache {

	    private View baseView;
	    //
	    private TextView realname;
	    private TextView state;
	    private TextView score;
	    private TextView adate;
	    private TextView orderid;
	    private TextView driverid;
	    private TextView userid;
	    
        //
	    private ImageView headimgView;

	    public ListViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public ImageView getHeadimgView() {
	        if (headimgView == null) {
	        	headimgView = (ImageView) baseView.findViewById(R.id.headimg);
	        }
	        return headimgView;
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
	    
	    public TextView getAdate() {
	        if (adate == null) {
	        	adate = (TextView) baseView.findViewById(R.id.adate);
	        }
	        return adate;
	    }
	    
	    public TextView getOrderid() {
	        if (orderid == null) {
	        	orderid = (TextView) baseView.findViewById(R.id.orderid);
	        }
	        return orderid;
	    }
	    public TextView getDriverid() {
	        if (driverid == null) {
	        	driverid = (TextView) baseView.findViewById(R.id.driverid);
	        }
	        return driverid;
	    }
	    public TextView getUserid() {
	        if (userid == null) {
	        	userid = (TextView) baseView.findViewById(R.id.userid);
	        }
	        return userid;
	    }
	    
	 
	    

}