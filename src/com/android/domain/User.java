package com.android.domain;

public class User {
	private String operationcode;
	private String studentcode;
	private String courseid;
	private String path;
	private String time;
    private long len;
	public User() {
	}	 
	public User(String operationcode,String studentcode, String courseid, String path,
			String time,long len) {
		this.operationcode = operationcode;
		this.studentcode = studentcode;
		this.courseid = courseid;
		this.path = path;
	    this.time = time;
	    this.len = len;
	}

	public String getstudentcode() {
		return studentcode;
	}

	public void setstudentcode(String studentcode) {
		this.studentcode = studentcode;
	}

	public String getcourseid() {
		return courseid;
	}

	public void setcourseid(String courseid) {
		this.courseid = courseid;
	}

	public void setpath(String path) {
		this.path = path;
	}

	public String getpath() {
		return path;
	}

	public long getlen() {
		return len;
	}

	public void setlen(long len) {
		this.len = len;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOperationCode() {
		return operationcode;
	}

	public void setOperationCode(String operation) {
		this.operationcode = operation;
	};

}
