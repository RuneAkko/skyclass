package com.android.domain;

public class Course {
	private String courseId;
	private String courseName;
	private String courseCode;
	private String studentCode;
 

	public Course() {
	}

	public Course(String courseId, String courseName, String courseCode,
			String studentCode) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseCode = courseCode;
		this.studentCode = studentCode;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getStudentCode() {
		return studentCode;
	}


}
