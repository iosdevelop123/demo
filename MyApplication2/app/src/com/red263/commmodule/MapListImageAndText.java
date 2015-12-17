package com.red263.commmodule;



public class MapListImageAndText {
	private String headimgUrl;
	private String realname;
	private String state;
	private String score;
    private String nowlocal;
	private String itemid;


	public MapListImageAndText(String headimgUrl,String realname, String state, String score,String nowlocal, String itemid) {
		super();
		this.headimgUrl=headimgUrl;
		this.realname=realname;
		this.state=state;
		this.score=score;
		this.nowlocal=nowlocal;
		this.itemid=itemid;
	}
	public String getHeadimgUrl() {
		return headimgUrl;
	}


	public void setHeadimg(String headimgUrl) {
		this.headimgUrl = headimgUrl;
	}


	public String getRealname() {
		return realname;
	}


	public void setRealname(String realname) {
		this.realname = realname;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getScore() {
		return score;
	}


	public void setScore(String score) {
		this.score = score;
	}


	public String getNowlocal() {
		return nowlocal;
	}


	public void setNowlocal(String nowlocal) {
		this.nowlocal = nowlocal;
	}


	public String getItemid() {
		return itemid;
	}


	public void setItemid(String itemid) {
		this.itemid = itemid;
	}





}
