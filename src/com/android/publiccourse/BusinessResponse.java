package com.android.publiccourse;

import java.io.Serializable;



import android.util.Log;

/**
 * User: sunzk Date: 13-1-15 Time: 上午8:59
 */
public class BusinessResponse implements Serializable {
	protected static final String TAG = "BusinessResponse";
	Boolean status;
	String code;
	String message;
	Object data;
	int totalRecords;
	int currentTotal;

	public BusinessResponse() {
		status = false;
		code = "";
		message = "";
		data = null;
		totalRecords = -1;
		currentTotal = -1;
	}

	public BusinessResponse(String jsonContent) {
		this();
		StringToObject(jsonContent);
	}

	// 成功true 失败 false
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	

	public int getCurrentTotal() {
		return currentTotal;
	}

	public void setCurrentTotal(int currentTotal) {
		this.currentTotal = currentTotal;
	}

	public void StringToObject(String data) {
	}

	public String ObjectToString() {
		return "";
	}
}