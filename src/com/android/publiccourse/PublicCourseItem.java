package com.android.publiccourse;




/**
 * 公开�? * @author wallace0615
 *	createDate: 2015-03-25
 */
public class PublicCourseItem extends Model<String>{
	private static final long serialVersionUID = -4574136278394094212L;
	
	public int id;
	public String name;				//名称
	public String iconUrl;			//图片地址
	public String playCount;			//播放次数
	public String dateCreated;		//创建时间
	public String avgRatingScore;	//平均得分
}
