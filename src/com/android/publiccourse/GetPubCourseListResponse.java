package com.android.publiccourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class GetPubCourseListResponse extends JsonAndXmlBusinessResponse {

	ExtArrayList<VideoItem> coursesList;
	

	public ExtArrayList<VideoItem> getCoursesList() {
	
		return coursesList;
	}
	
	

	public GetPubCourseListResponse() {
	}

	public GetPubCourseListResponse(String jsonContent) {
		super(jsonContent);
	}
	


	@Override
	public void parseJsonDataToArray(JSONArray dataItems) {
		// TODO Auto-generated method stub
		super.parseJsonDataToArray(dataItems);
		if (dataItems != null) {
			for (int index = 0; index < dataItems.length(); index++) {
				try {
					VideoItem category = dataToCourse((JSONObject) dataItems
							.get(index));
					coursesList.add(category);
				} catch (JSONException e) {
					throw new BarException("解析SubItems出错", e);
				}
			}
		}
	}

	@Override
	public void parseDataToObject(JSONObject json) {
		coursesList = new ExtArrayList<VideoItem>();
		JSONArray array = JsonHelper.getSubArrayObject(json, "Data");
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				try {
					VideoItem category = dataToCourse((JSONObject) array
							.get(i));
					coursesList.add(category);
				} catch (JSONException e) {
					throw new BarException("解析SubItems出错", e);
				}
			}
		}
	}

	public VideoItem dataToCourse(JSONObject json) {
		Log.i("coursedata", json.toString());
		VideoItem videoItem = new VideoItem();
		videoItem.videoId = JsonHelper.jsonToInt(json, "VideoID");
		videoItem.videoName =  JsonHelper.jsonToString(json, "VideoName");
		videoItem.videoUrl =  JsonHelper.jsonToString(json, "VideoUrl");
		videoItem.VideoPicUrl = JsonHelper.jsonToString(json, "VideoPicUrl");
		videoItem.videoTime = JsonHelper.jsonToInt(json, "VideoSize");
		videoItem.VideoPlayCount = JsonHelper.jsonToString(json, "ClickCount");		
		return videoItem;
	}
	
}
