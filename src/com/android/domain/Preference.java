/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.android.domain;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class  Preference{

	 public static float gyroscopex;
	public static float gyroscopey;
	public static float gyroscopez;
	 public  static float rotationx,rotationy,rotationz,rotationm,rotationn;
	public Preference(){}

	public float getGyroscopex() {
		return gyroscopex;
	}
	public void setGyroscopex(float gyroscopex) {
		this.gyroscopex = gyroscopex;
	}
	public float getGyroscopey() {
		return gyroscopey;
	}
	public void setGyroscopey(float gyroscopey) {
		this.gyroscopey = gyroscopey;
	}
	public float getGyroscopez() {
		return gyroscopez;
	}
	public void setGyroscopez(float gyroscopez) {
		this.gyroscopez = gyroscopez;
	}
	public float getRotationx() {
		return rotationx;
	}
	public void setRotationx(float rotationx) {
		this.rotationx = rotationx;
	}
	public float getRotationy() {
		return rotationy;
	}
	public void setRotationy(float rotationy) {
		this.rotationy = rotationy;
	}
	public float getRotationz() {
		return rotationz;
	}
	public void setRotationz(float rotationz) {
		this.rotationz = rotationz;
	}
	public float getRotationm() {
		return rotationm;
	}
	public void setRotationm(float rotationm) {
		this.rotationm = rotationm;
	}
	public float getRotationn() {
		return rotationn;
	}
	public void setRotationn(float rotationn) {
		this.rotationn = rotationn;
	}
	
	public static List<Activity> activities = new ArrayList<Activity>();
}