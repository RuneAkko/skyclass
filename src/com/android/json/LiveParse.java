package com.android.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.android.domain.LiveFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//直播Json数据解析类
public class LiveParse {

	public static String liveUrl;
	private Gson liveGson=new Gson();
	private ArrayList<LiveFile> myList = new ArrayList<LiveFile>();

	public ArrayList<LiveFile> getMyList() {
		return myList;
	}

	public void setMyList(ArrayList<LiveFile> myList) {
		this.myList = myList;
	}

	public LiveParse() {
		super();
	}
	public void JSONToBean(String json) {

		try {
			Type type=new TypeToken<List<HashMap<String, Object>>>(){}.getType();
			List<HashMap<String, Object>> list=liveGson.fromJson(json, type);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				Set<String> set = map.keySet();
				LiveFile temp = new LiveFile();
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String key = it.next();
					if (key.compareTo("classid") == 0)
						temp.setClassid(map.get(key).toString());
					if (key.compareTo("classname") == 0)
						temp.setClassname(map.get(key).toString());
					if (key.compareTo("teachername") == 0)
						temp.setTeachername(map.get(key).toString());
					if (key.compareTo("videopath") == 0)
						temp.setVideo(map.get(key).toString());
					if (key.compareTo("starttime") == 0)
						temp.setStarttime(map.get(key).toString());
					if (key.compareTo("screenpath") == 0)
						temp.setScreenpath(map.get(key).toString());
					if (key.compareTo("serverpath") == 0)
						temp.setServerpath(map.get(key).toString());
					if (key.compareTo("location") == 0)
						temp.setLocation(map.get(key).toString());
				}
				myList.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
