package com.android.publiccourse;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JsonAndXmlBusinessResponse extends BusinessResponse {
	protected boolean isJson = true;

	public JsonAndXmlBusinessResponse() {
	}

	public JsonAndXmlBusinessResponse(String content) {
		this(content, true);
	}

	public JsonAndXmlBusinessResponse(String content, boolean isJson) {
		super(content);
		this.isJson = isJson;
		StringToObject(content);
	}

	@Override
	public void StringToObject(String content) {
		if (isJson) {
			jsonToObject(content);
		} else {
			XmlToObject(content);
		}
	}

	@Override
	public String ObjectToString() {
		if (isJson) {
			return ObjectToJsonString();
		} else {
			return ObjectToXmlString();
		}
	}

	public void XmlToObject(String xmlData) {
	}

	public String ObjectToXmlString() {
		return "";
	}

	public void jsonToObject(String jsonData) {

		JSONObject jsonObject;
		try {
			// Log.e(TAG, "-------" + jsonData);
			jsonObject = new JSONObject(jsonData);

		} catch (JSONException ex) {
			// Log.e(TAG, "json parse error");
//			Log.e(TAG, ex.toString());
			return;
		}
		this.status = JsonHelper.jsonToBoolean(jsonObject, "isSuccess");
		if(!this.status){
			this.status = JsonHelper.jsonToBoolean(jsonObject, "status");
		}
		this.code = JsonHelper.jsonToString(jsonObject, "Code");
		this.message = JsonHelper.jsonToString(jsonObject, "Message");
		this.totalRecords = JsonHelper.jsonToInt(jsonObject, "totalCount");
		this.currentTotal = JsonHelper.jsonToInt(jsonObject, "totalRecords");
		parseDataToObject(jsonObject);
	}

	public String ObjectToJsonString() {
		Map map = new HashMap();
		map.put("status", status);
		map.put("code", code);
		Map dMap = toDataMap();
		if (dMap == null) {
			map.put("data", "");
		} else {
			map.put("data", dMap);
		}
		return "";
	}

	public void parseDataToObject(JSONObject json) {
		if (!status) {
			return;
		}
		parseJsonDataObject(json);
	}

	public Map toDataMap() {
		Map map = new HashMap();
		return null;
	}

	public void parseJsonDataObject(JSONObject json) {
	}

	public void parseJsonDataToArray(JSONArray array) {

	}

}
