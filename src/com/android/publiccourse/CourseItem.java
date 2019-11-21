package com.android.publiccourse;

import java.util.Date;



public class CourseItem extends Model<String>{
	public int  rowNum; //行号
    public int courseId; //课程ID
    public String courseName;//课程名称
    public Date courseCreateDate;//课程创建时间
    public Date courseSelectDate;//课程的�?课时�?  
    public int videoCount;//课程下的视频�?   
    public int pdfCount;//课程下的文档�?    
    public int examTaskCount;//课程下的评测�?   
    public int noticeCount;//课程下的公告�?   
    public int newVideoCount;//课程下的未读视频�?  
    public int newPDFCount;//课程下的未读文档�?  
    public int newExamTaskCount;//课程下的未读评测�?    
    public int newNoticeCount;//课程下的未读公告�?
    
}
