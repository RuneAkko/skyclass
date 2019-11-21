package com.android.domain;

import java.io.Serializable;

public class studyProgress implements Serializable{
	private String GradeTotal;
	private String GradeSum;
	private String GraduatelowlimDays;
	private String GraduatehighlimDays;
	private String StudyDays;
	
	public void setGradeTotal(String total){
		GradeTotal = total;
	}
	public String getGradeTotal(){
		return GradeTotal;
	}
	
	public void setGradeSum(String total){
		GradeSum = total;
	}
	public String getGradeSum(){
		return GradeSum;
	}
	
	public void setGraduatelowlimDays(String total){
		GraduatelowlimDays = total;
	}
	public String getGraduatelowlimDays(){
		return GraduatelowlimDays;
	}
	
	public void setGraduatehighlimDays(String total){
		GraduatehighlimDays = total;
	}
	public String getGraduatehighlimDays(){
		return GraduatehighlimDays;
	}
	
	public void setStudyDays(String total){
		StudyDays = total;
	}
	public String getStudyDays(){
		return StudyDays;
	}
	

}
