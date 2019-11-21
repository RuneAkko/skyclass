package com.android.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import com.android.domain.intelligentroom.*;
public class CourseServiceIntelligentRoom {
   private DBOpenHelper mySQLiteHelper;
   public CourseServiceIntelligentRoom(Context context){
	   mySQLiteHelper=DBOpenHelper.getInstance(context);
   }
   public void insertCourse(ArrayList<Course> courseList){
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   for(int i=0;i<courseList.size();i++){
	   Course course=courseList.get(i);
	   String sql="INSERT INTO onlinecourse(courseId,courseName,collegeName,teacherName,className,startTime,endTime,videoUrl,screenUrl,server,location)VALUES("+course.getCourseId()+",'"+course.getCourseName()
	              +"','"+course.getCollegeName()+"','"+course.getTeacherName()+"','"+course.getClassName()+"','"+course.getStartTime()
	              +"','"+course.getEndTime()+"','"+course.getVideoUrl()+"','"+course.getScreenUrl()+"','"+course.getServer()+"','"+course.getLocation()+"')";
	   db.execSQL(sql);
	   }
	   System.out.println("����ɹ�");
	   db.close();
   }
   public void deleteCourseByCourseId(Course course){
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   /*String sql="Delete from onlinecourse where courseId="+course.getCourseId();
	   db.execSQL(sql);*/
	   db.delete("onlinecourse", "courseId=?", new String[]{String.valueOf(course.getCourseId())});
	   db.close();
   }
   public void deleteAllCourse(){
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   /*String sql="Delete from onlinecourse where courseId="+course.getCourseId();
	   db.execSQL(sql);*/
	   db.delete("onlinecourse",null,null);
	   db.close();
   }
   public void deleteAllNoCourse(){
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   /*String sql="Delete from onlinecourse where courseId="+course.getCourseId();
	   db.execSQL(sql);*/
	   db.delete("noonlinecourse",null,null);
	   db.close();
   }
   public ArrayList<Course> queryCourse(String collegeName){
	   ArrayList<Course> courseList=new ArrayList<Course>();
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   Cursor cursor=db.query("onlinecourse", new String[]{"courseId","courseName","collegeName","teacherName",
			   "className","startTime","endTime","videoUrl","screenUrl","server","location"}, "collegeName=?", new String[]{collegeName}, null, 
			   null,null);
	   while(cursor.moveToNext()){
		   Course course=new Course();
		   course.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
		   course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
		   course.setCollegeName(cursor.getString(cursor.getColumnIndex("collegeName")));
		   course.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
		   course.setClassName(cursor.getInt(cursor.getColumnIndex( "className")));
		   course.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
		   course.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
		   course.setVideoUrl(cursor.getString(cursor.getColumnIndex("videoUrl")));
		   course.setScreenUrl(cursor.getString(cursor.getColumnIndex("screenUrl")));
		   course.setServer(cursor.getString(cursor.getColumnIndex("server")));
		   course.setLocation(cursor.getString(cursor.getColumnIndex("location")));
		   courseList.add(course);
	   }
	   cursor.close();
	   db.close();

	   return courseList;
   }
   public ArrayList<Course> queryAllCourse(){
	   ArrayList<Course> courseList=new ArrayList<Course>();
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   Cursor cursor=db.query("onlinecourse", new String[]{"courseId","courseName","collegeName","teacherName",
			   "className","startTime","endTime","videoUrl","screenUrl","server","location"}, null,null, null, 
			   null,null);
	   while(cursor.moveToNext()){
		   Course course=new Course();
		   course.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
		   course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
		   course.setCollegeName(cursor.getString(cursor.getColumnIndex("collegeName")));
		   course.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
		   course.setClassName(cursor.getInt(cursor.getColumnIndex( "className")));
		   course.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
		   course.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
		   course.setVideoUrl(cursor.getString(cursor.getColumnIndex("videoUrl")));
		   course.setScreenUrl(cursor.getString(cursor.getColumnIndex("screenUrl")));
		   course.setServer(cursor.getString(cursor.getColumnIndex("server")));
		   course.setLocation(cursor.getString(cursor.getColumnIndex("location")));
		   courseList.add(course);
	   }
	   cursor.close();
	   db.close();
	   return courseList;
   }

   public void insertNoOnlineCourse(ArrayList<Course> courseList){
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   for(int i=0;i<courseList.size();i++){
	   Course course=courseList.get(i);
	   String sql="INSERT INTO noonlinecourse(courseId,courseName,collegeName,teacherName,className,startTime,endTime) VALUES("+course.getCourseId()+",'"+course.getCourseName()
	              +"','"+course.getCollegeName()+"','"+course.getTeacherName()+"','"+course.getClassName()+"','"+course.getStartTime()
	              +"','"+course.getEndTime()+"')";
	   db.execSQL(sql);
	   }
	   db.close();
   }
   public ArrayList<Course> queryNoOnlineCourse(String collegeName){
	   ArrayList<Course> courseList=new ArrayList<Course>();
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   Cursor cursor=db.query("noonlinecourse", new String[]{"courseId","courseName","collegeName","teacherName",
			   "className","startTime","endTime"}, "collegeName=?", new String[]{collegeName}, null, 
			   null,null);
	   while(cursor.moveToNext()){
		   Course course=new Course();
		   course.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
		   course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
		   course.setCollegeName(cursor.getString(cursor.getColumnIndex("collegeName")));
		   course.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
		   course.setClassName(cursor.getInt(cursor.getColumnIndex( "className")));
		   course.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
		   course.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
		   courseList.add(course);
	   }
	   cursor.close();
	   db.close();
	   System.out.println("��ѯ�ɹ�1");
	   return courseList;
   }
   public ArrayList<Course> queryAllNoOnlineCourse(){
	   ArrayList<Course> courseList=new ArrayList<Course>();
	   SQLiteDatabase db=mySQLiteHelper.getWritableDatabase();
	   Cursor cursor=db.query("noonlinecourse", new String[]{"courseId","courseName","collegeName","teacherName",
			   "className","startTime","endTime"},null, null, null, 
			   null,null);
	   while(cursor.moveToNext()){
		   Course course=new Course();
		   course.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
		   course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
		   course.setCollegeName(cursor.getString(cursor.getColumnIndex("collegeName")));
		   course.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
		   course.setClassName(cursor.getInt(cursor.getColumnIndex( "className")));
		   course.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
		   course.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
		   courseList.add(course);
	   }
	   cursor.close();
	   db.close();
	   return courseList;
   }
}
