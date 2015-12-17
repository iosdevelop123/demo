package com.red263.commmodule;



public class ListImageAndText {

	private String headimgUrl;
	private String realname;
	private String state;
	private String score;
    private String adate;
	private String driverid;
	private String orderid;
	private String userid;
	


	public ListImageAndText(String headimgUrl,String realname, String state, String score,String adate, String driverid, String orderid, String userid) {
		super();
		this.headimgUrl=headimgUrl;
		this.realname=realname;
		this.state=state;
		this.score=score;
		this.adate=adate;
		this.driverid=driverid;
		this.orderid=orderid;
		this.userid=userid;
	}
	
	public String getHeadimgUrl() {
		return headimgUrl;
	}



	public void setHeadimgUrl(String headimgUrl) {
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



	public String getAdate() {
		return adate;
	}



	public void setAdate(String adate) {
		this.adate = adate;
	}



	public String getDriverid() {
		return driverid;
	}



	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}



	public String getOrderid() {
		return orderid;
	}



	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}



	public String getUserid() {
		return userid;
	}



	public void setUserid(String userid) {
		this.userid = userid;
	}



	
	




}
