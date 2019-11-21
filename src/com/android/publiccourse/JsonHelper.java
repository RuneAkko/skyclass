package com.android.publiccourse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

//User: szk Date: 13-3-6 Time: 下午9:56
public class JsonHelper {
	public static int jsonToInt(JSONObject json, String name) {
		return jsonToInt(json, name, -1);
	}

	public static int jsonToInt(JSONObject json, String name, int defaltValue) {
		if (json == null)
			return defaltValue;
		try {
			return json.getInt(name);
		} catch (JSONException e) {
			return defaltValue;
		}
	}

	public static double jsonToDouble(JSONObject json, String name) {
		return jsonToDouble(json, name, 0);
	}

	
	public static double jsonToDouble(JSONObject json, String name,
			double defaltValue) {
		if (json == null)
			return defaltValue;
		try {
			return json.getDouble(name);
		} catch (JSONException e) {
			return defaltValue;
		}
	}

	public static String jsonToString(JSONObject json, String name) {
		if (jsonToString(json, name, "").equalsIgnoreCase("null")) {
			return "";
		}
		return jsonToString(json, name, "");
	}

	public static Boolean jsonToBoolean(JSONObject json, String name) {
		String s = JsonHelper.jsonToString(json, name);
		if ("true".equals(s.toLowerCase()) || "1".equalsIgnoreCase(s)) {
			return true;
		}
		return false;
	}

	public static Date jsonToDate(JSONObject json, String name) {
		String value = JsonHelper.jsonToString(json, name);
		if ("".equals(value))
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = format.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static Date jsonToLongDate(JSONObject json, String name) {
		String value = JsonHelper.jsonToString(json, name);
		if ("".equals(value))
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.valueOf(value));
		String dateString = format.format(date);
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date jsonToSpeakDate(JSONObject json, String name) {
		String value = JsonHelper.jsonToString(json, name);
		if ("".equals(value))
			return null;
		Date date = new Date(Long.parseLong(value));
		return date;
	}

	public static Date jsonToDateTime(JSONObject json, String name) {
		String value = JsonHelper.jsonToString(json, name);
		if ("".equals(value))
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = value.substring(value.indexOf("(") + 1,
				value.indexOf("+0800"));
		Date date = new Date(Long.parseLong(str));
		// try {
		// date = format.parse(str);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		return date;
	}

	public static Date jsonToDateLong(JSONObject json, String name) {
		String value = JsonHelper.jsonToString(json, name);
		if ("".equals(value))
			return null;
		Date date = new Date(Long.parseLong(value));
		// try {
		// date = format.parse(str);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		//Log.d("The date=", date.toString());
		return date;
	}

	public static String jsonToString(JSONObject json, String name,
			String defaltValue) {
		if (json == null)
			return defaltValue;
		try {
			return json.getString(name);
		} catch (JSONException e) {
			return defaltValue;
		}
	}

	public static JSONObject getSubObject(JSONObject json, String name) {
		if (json == null)
			return null;
		try {
			return json.getJSONObject(name);
		} catch (JSONException e) {
			return null;
		}
	}

	public static JSONArray getSubArrayObject(JSONObject json, String name) {
		if (json == null)
			return null;
		try {
			return json.getJSONArray(name);
		} catch (JSONException e) {
			return null;
		}
	}
}
