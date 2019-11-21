package com.android.publiccourse;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * 
 * @ClassName: MainApp
 * @Description: 当前应用程序APP
 * @author zhangchunzhe
 * @date 2013-1-14 下午2:33:26
 * 
 */
public class OBMainApp extends Application {

    /**
     * 用户是否登录当前程序
     * 
     */
    public static boolean isLogin = false;
    /**
     * 当前用户的帐户信�?     */

    private List<Activity> mList = new LinkedList<Activity>();

    private OBSoftReference mObSoftRefrences = new OBSoftReference();

    private static OBMainApp mInst;
    
    private int onlineNumber;
    private int noOnlineNumber;
    private int studentNumber;
    private int position;
    private String studentcode;
    public String getStudentcode() {
		return studentcode;
	}

	public void setStudentcode(String  studentcode) {
		this.studentcode = studentcode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	private String courseName;
    private String courseId;
    public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public static boolean isForeground() {
        if (mInst != null) {
            ActivityManager am = (ActivityManager) mInst.getSystemService(ACTIVITY_SERVICE);
            List<RunningTaskInfo> list = am.getRunningTasks(100);
            return list.size() > 0 && list.get(0).topActivity.getPackageName().equals(mInst.getPackageName());
        }
        return false;
    }
 
    public OBSoftReference getmObSoftRefrences() {
        return mObSoftRefrences;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    // delete Activity
  	public void exit() {
  	    if (mList != null && mList.size() > 0) {
  	        for (Activity activity : mList) {
  	          activity.finish();
  	          
  	        }
  	      }
  	     System.exit(0);
  	}
    public static OBMainApp getInstance() {
        return mInst;
    }

    public int getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}
	public int getOnlineNumber() {
		return onlineNumber;
	}
	public void setOnlineNumber(int onlineNumber) {
		this.onlineNumber = onlineNumber;
	}
	public int getNoOnlineNumber() {
		return noOnlineNumber;
	}
	public void setNoOnlineNumber(int noOnlineNumber) {
		this.noOnlineNumber = noOnlineNumber;
	}
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc(); // 告诉系统回收

    }

    @Override
    public void onCreate() {
        super.onCreate();
        // JPushInterface.setDebugMode(true);
        // JPushInterface.init(this);
        //System.out.println("?????????执行到此???????");
        //CrashReport.initCrashReport(getApplicationContext(), "8bbbd0b4de", true);
        //System.out.println("?????????执行到此???????");
        mInst = this;
        final String pkgName = getPackageName();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                android.util.Log.e(pkgName, thread.getName() + " " + thread.getId() + " " + thread.getPriority());
                ex.printStackTrace();
            }

        });
    }
}
