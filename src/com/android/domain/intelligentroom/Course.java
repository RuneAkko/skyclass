package com.android.domain.intelligentroom;

public class Course {
	private int courseId;
	private String courseName;
	private String teacherName;
	private String collegeName;
	public String getCollegeName() {
		return collegeName;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	private int className;
	private String startTime;
	private String endTime;
	private String server;
	private String location;
	private String videoUrl;
    private String screenUrl;
	public String getScreenUrl() {
		return screenUrl;
	}

	public void setScreenUrl(String screenUrl) {
		this.screenUrl = screenUrl;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getClassName() {
		return className;
	}

	public void setClassName(int className) {
		this.className = className;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

}
