package com.android.domain;



public class LiveFile {
	private String classid=null;
	private String classname=null;
	private String teachername=null;
	private String videopath=null;
	private String Starttime=null;
	private String screenpath=null;
	private String serverpath=null;
	private String location = null;
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClassid() {
		return classid;
	}
	
	public void setClassid(String classid) {
		this.classid = classid;
	}
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getTeachername() {
		return teachername;
	}
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}
	public String getVideopath() {
		return videopath;
	}
	public void setVideopath(String videopath) {
		this.videopath = videopath;
	}
	public void setVideo(String videopath) {
		this.videopath = videopath;
	}
	public void setStarttime(String Starttime){
	this.Starttime=Starttime;
	}
	public String getStarttime(){
		return Starttime;
	}

	public String getScreenpath() {
		return screenpath;
	}

	public void setScreenpath(String screenpath) {
		this.screenpath = screenpath;
	}

	public String getServerpath() {
		return serverpath;
	}

	public void setServerpath(String serverpath) {
		this.serverpath = serverpath;
	}
}
