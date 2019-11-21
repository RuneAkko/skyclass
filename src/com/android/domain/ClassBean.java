package com.android.domain;

import java.util.ArrayList;

public class ClassBean {

	private String className;

	private ArrayList<String> subClass;

	private ArrayList<ArrayList<String>> subSubClass;

	private ArrayList<ArrayList<String>> path;

	public ClassBean() {
	}

	public ClassBean(String className) {
		super();
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ArrayList<String> getSubClass() {
		return subClass;
	}

	public void setSubClass(ArrayList<String> subClass) {
		this.subClass = subClass;
	}

	public ArrayList<ArrayList<String>> getsubSubClass() {
		return subSubClass;
	}

	public void setsubSubClass(ArrayList<ArrayList<String>> subSubClass) {
		this.subSubClass = subSubClass;
	}

	public void setpath(ArrayList<ArrayList<String>> path) {
		this.path = path;
	}

	public ArrayList<ArrayList<String>> getpath() {
		return path;
	}

}
