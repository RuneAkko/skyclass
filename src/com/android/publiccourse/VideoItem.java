package com.android.publiccourse;

import java.util.Date;



public class VideoItem extends Model<String> {
	public int rowNum; // 行号
	public int videoId;// 视频ID
	public String videoName;// 视频名称
	public String videoUrl;// 视频地址
	public int videoSize;// 视频大小
	public int videoTime;// 视频时长
	public int videoStudyStatus;// 视频的学习状�?是否为新视频)
	public Date VideoUploadDate;// 视频上传时间
	public Date VideoCreateDate;// 视频的创建时�?	
	public int VideoAuthorID;// 视频发布人ID
	public String videoAuthorName;// 视频发布�?	
	public String VideoPicUrl;//视频截图地址
	public String VideoPlayCount;//视频播放次数

	public String getVideoPlayCount() {
		return VideoPlayCount;
	}

	public void setVideoPlayCount(String videoPlayCount) {
		VideoPlayCount = videoPlayCount;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public int getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(int videoSize) {
		this.videoSize = videoSize;
	}

	public int getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(int videoTime) {
		this.videoTime = videoTime;
	}

	public int getVideoStudyStatus() {
		return videoStudyStatus;
	}

	public void setVideoStudyStatus(int videoStudyStatus) {
		this.videoStudyStatus = videoStudyStatus;
	}

	public Date getVideoUploadDate() {
		return VideoUploadDate;
	}

	public void setVideoUploadDate(Date videoUploadDate) {
		VideoUploadDate = videoUploadDate;
	}

	public Date getVideoCreateDate() {
		return VideoCreateDate;
	}

	public void setVideoCreateDate(Date videoCreateDate) {
		VideoCreateDate = videoCreateDate;
	}

	public int getVideoAuthorID() {
		return VideoAuthorID;
	}

	public void setVideoAuthorID(int videoAuthorID) {
		VideoAuthorID = videoAuthorID;
	}

	public String getVideoAuthorName() {
		return videoAuthorName;
	}

	public void setVideoAuthorName(String videoAuthorName) {
		this.videoAuthorName = videoAuthorName;
	}

}
