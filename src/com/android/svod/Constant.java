package com.android.svod;

import com.android.publiccourse.OBLCourseMainActivity;
import com.android.svod.intelligentroom.MainActivity;
//TabHost类所用常量
public class Constant {

	public static String filePath = "";
	public final static String HOST = "116.228.159.6";
	public final static int PORT = 1445;
	public final static String HOST_IP = "http://" + HOST + ":" + PORT;
	public static String imageURL = HOST_IP + "/DemoPortalBbs/dpDownload.bbscs";
	public final static String videoURL = HOST_IP + "/DemoPortalBbs/dpDownload.bbscs";
	public static final class ConValue{
		
	 	public static int   mImageViewArray[] = {
	 		                                R.drawable.public_course, 
	 		                                R.drawable.vodicon,										
											R.drawable.liveicon,
											R.drawable.newsicon,
											R.drawable.seticon
											}; 

		public static String mTextviewArray[] = {"首页","点播","直播","新闻","我的"};
		
		
		public static Class mTabClassArray[]= { OBLCourseMainActivity.class,
			                                    Page1.class,
												Page2.class,
												Page3.class,
												Page4.class
												};
	}
}
