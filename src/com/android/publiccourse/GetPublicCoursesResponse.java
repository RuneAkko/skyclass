package com.android.publiccourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class GetPublicCoursesResponse extends JsonAndXmlBusinessResponse {
	private static final long serialVersionUID = 4768835923182478016L;
	
	
	ExtArrayList<PublicCourseItem> fineCourses = null;
	public ExtArrayList<PublicCourseItem> getFineCourses() {
		return fineCourses;
	}

	public ExtArrayList<PublicCourseItem> getRecommendCourses() {
		return recommendCourses;
	}


	ExtArrayList<PublicCourseItem> recommendCourses = null;
	
	public GetPublicCoursesResponse() {
	}

	public GetPublicCoursesResponse(String jsonContent) {
		super(jsonContent);
	}
	
	@Override
	public void parseJsonDataObject(JSONObject json) {
		fineCourses = new ExtArrayList<PublicCourseItem>();
		recommendCourses = new ExtArrayList<PublicCourseItem>();

		JSONObject data = JsonHelper.getSubObject(json, "data");
		JSONArray fineArray = JsonHelper.getSubArrayObject(data, "fineCourses");
		JSONArray recomArray = JsonHelper.getSubArrayObject(data, "recommendCourses");
		
		if (fineArray != null) {
			for (int i = 0; i < fineArray.length(); i++) {
				try {
					PublicCourseItem courseItem = dataToCourse((JSONObject) fineArray
							.get(i));
					fineCourses.add(courseItem);
				} catch (JSONException e) {
					throw new BarException("解析SubItems出错", e);
				}
			}
		}
		
		if(recomArray != null){
			for (int i=0; i<recomArray.length(); i++){
				try{
					recommendCourses.add(dataToCourse((JSONObject)recomArray.get(i)));
				}catch(JSONException e){
					throw new BarException("解析SubItems出错", e);
				}
			}
		}
		
	}	


	public PublicCourseItem dataToCourse(JSONObject json) {
		PublicCourseItem courseItem = new PublicCourseItem();
		courseItem.id = JsonHelper.jsonToInt(json, "id");
		courseItem.name = JsonHelper.jsonToString(json, "name");
		courseItem.avgRatingScore = JsonHelper.jsonToString(json, "avgRatingScore");
		courseItem.dateCreated = JsonHelper.jsonToString(json, "dataCreated");
		courseItem.iconUrl = JsonHelper.jsonToString(json, "iconUrl");
		courseItem.playCount = JsonHelper.jsonToString(json, "playingCount");
		
		return courseItem;
	}

}
