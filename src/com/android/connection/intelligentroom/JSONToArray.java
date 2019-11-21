package com.android.connection.intelligentroom;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.domain.intelligentroom.Course;

public class JSONToArray {
	private static SimpleDateFormat simpleDateFormat;
    
	public JSONToArray(){
		simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static ArrayList<Course> jsonToCourse(String json){
		ArrayList<Course> list=new ArrayList<Course>();
		try {
			JSONObject jsonObject=new JSONObject(json);
			JSONArray jsonArray=jsonObject.getJSONArray("livecourseinfo");
			
			for(int i=0;i<jsonArray.length();i++){
			     JSONObject object=jsonArray.getJSONObject(i);
			     Course liveCourse=new Course();
			     liveCourse.setCourseId(object.getInt("courseId"));
			     liveCourse.setCourseName(object.getString("courseName"));
			     liveCourse.setClassName(object.getInt("className"));
			     liveCourse.setCollegeName(object.getString("collegeName"));
			     liveCourse.setTeacherName(object.getString("teacherName"));
			     liveCourse.setStartTime(object.getString("startTime"));
			     liveCourse.setEndTime(object.getString("endTime"));
			     if(!object.isNull("video")){
			    	  liveCourse.setServer(object.getString("server"));
			    	  liveCourse.setLocation(object.getString("location"));
			          liveCourse.setVideoUrl(object.getString("video"));
			          System.out.println("video"+object.getString("video"));
			     }
			     if(!object.isNull("screen")){
			          liveCourse.setScreenUrl(object.getString("screen"));
			          System.out.println("screen"+object.getString("screen"));
			          
			     }
			     list.add(liveCourse);
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

		public static ArrayList<Course> jsonToNoOnlineCourse(String json){
			ArrayList<Course> list=new ArrayList<Course>();
			try {
				JSONObject jsonObject=new JSONObject(json);
				JSONArray jsonArray=jsonObject.getJSONArray("nolivecourseinfo");
				
				for(int i=0;i<jsonArray.length();i++){
				     JSONObject object=jsonArray.getJSONObject(i);
				     Course noliveCourse=new Course();
				     noliveCourse.setCourseId(object.getInt("courseId"));
				     noliveCourse.setCourseName(object.getString("courseName"));
				     noliveCourse.setClassName(object.getInt("className"));
				     noliveCourse.setCollegeName(object.getString("collegeName"));
				     noliveCourse.setTeacherName(object.getString("teacherName"));
				     noliveCourse.setStartTime(object.getString("startTime"));
				     noliveCourse.setEndTime(object.getString("endTime"));			     
				     list.add(noliveCourse);
				}
				return list;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
}
