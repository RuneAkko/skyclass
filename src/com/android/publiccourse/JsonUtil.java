package com.android.publiccourse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * User: 马生录（mason Date: 13-9-30 Time: 上午9:06
 */
public class JsonUtil {
	public static JSONObject parseObjectfromJSONString(String jsonString) {
		if (jsonString == null) {
			return new JSONObject();
		}
		JSONObject jsonObject = new JSONObject();
		JSONTokener jsonTokener = new JSONTokener(jsonString);
		try {
			return (JSONObject) jsonTokener.nextValue();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONArray parseArrayfromJSONString(String jsonString) {
		if (jsonString == null) {

			return new JSONArray();
		}
		JSONArray jsonObject = new JSONArray();
		JSONTokener jsonTokener = new JSONTokener(jsonString);
		try {
			return (JSONArray) jsonTokener.nextValue();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List toListFromArray(JSONArray array) {
		List list = new ArrayList();
		for (int index = 0; index < array.length(); index++) {
			try {
				Object value = array.get(index);
				if (value instanceof JSONObject) {
					list.add(toMapFromObject((JSONObject) value));
				} else if (value instanceof JSONArray) {
					list.add(toListFromArray((JSONArray) value));
				} else {
					list.add(value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
		}
		return list;
	}

	public static Map toMapFromObject(JSONObject json) {
		Map map = new LinkedHashMap();
		Iterator iterator = json.keys();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			try {
				Object value = json.get(key);
				if (value instanceof JSONObject) {
					map.put(key, toMapFromObject((JSONObject) value));
				} else if (value instanceof JSONArray) {
					map.put(key, toListFromArray((JSONArray) value));
				} else {
					map.put(key, value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
		}
		return map;
	}

	public static String toJsonStringFromList(Collection collection) {
		JSONArray json = getArrayFromList(collection);
		return json.toString();
	}

	public static String toJsonStringFromMap(Map map) {
		JSONObject json = getObjectFromMap(map);
		return json.toString();
	}

	public static String toJsonString(Object obj) {
		if (obj instanceof Collection || obj instanceof Object[]) {
			return toJsonStringFromList((List) obj);
		} else {
			return toJsonStringFromMap((Map) obj);
		}
	}

	public static JSONArray getArrayFromList(Collection list) {
		JSONArray array = new JSONArray();
		for (Object item : list) {
			if (item instanceof Collection || item instanceof Object[]) {
				array.put(getArrayFromList((Collection) item));
			} else if (item instanceof Map) {
				array.put(getObjectFromMap((Map) item));
			} else {
				array.put(item);
			}
		}
		return array;
	}

	public static JSONObject getObjectFromMap(Map map) {
		JSONObject json = new JSONObject();
		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			boolean bypass = false;
			Map.Entry entry = (Map.Entry) entries.next();
			Object item = entry.getValue();
			String key = entry.getKey().toString();
			if (item instanceof Collection || item instanceof Object[]) {
				try {
					json.put(key, getArrayFromList((Collection) item));
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
			} else if (item instanceof Map) {
				try {
					json.put(key, getObjectFromMap((Map) item));
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				try {
					json.put(key, item);
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return json;
	}

}