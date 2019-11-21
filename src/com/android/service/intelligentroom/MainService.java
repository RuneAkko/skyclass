package com.android.service.intelligentroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.android.svod.intelligentroom.*;
import com.android.publiccourse.OBMainApp;
import com.android.domain.intelligentroom.Course;
import com.android.domain.intelligentroom.Task;
import com.android.connection.intelligentroom.HttpGetCourse;
import com.android.connection.intelligentroom.JSONToArray;
import com.android.sql.CourseServiceIntelligentRoom;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;

public class MainService extends Service {
	private static BlockingQueue<Task> taskQueue = new ArrayBlockingQueue<Task>(50);
	private static ArrayList<Activity> activityList = new ArrayList<Activity>();
	private CourseServiceIntelligentRoom courseService = new CourseServiceIntelligentRoom(this);
	private OBMainApp myApplication = OBMainApp.getInstance();

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			HashMap<String,Object> object = new HashMap<String,Object>();
			switch (msg.what) {
			case Task.TASK_REQUESTONLINECOURSE:
				courseService.deleteAllCourse();
				if (((ArrayList<Course>) msg.obj).size() != 0) {

					courseService.insertCourse((ArrayList<Course>) msg.obj);
					myApplication.setOnlineNumber(((ArrayList<Course>) msg.obj).size());
					object.clear();
					object.put("taskId", Task.TASK_REQUESTONLINECOURSE);
					object.put("Number", ((ArrayList<Course>) msg.obj).size());
					MainActivity.refresh(object);
				} else {
					myApplication.setOnlineNumber(0);
					object.clear();
					object.put("taskId", Task.TASK_REQUESTONLINECOURSE);
					object.put("Number", 0);
					MainActivity.refresh(object);
				}
				break;
			case Task.TASK_REQUESTNOONLINECOURSE:
				courseService.deleteAllNoCourse();
				if (((ArrayList<Course>) msg.obj).size() != 0) {
					courseService.insertNoOnlineCourse((ArrayList<Course>) msg.obj);
					myApplication.setNoOnlineNumber(((ArrayList<Course>) msg.obj).size());
					object.clear();
					object.put("taskId", Task.TASK_REQUESTNOONLINECOURSE);
					object.put("Number", ((ArrayList<Course>) msg.obj).size());
					MainActivity.refresh(object);
				} else {
					myApplication.setNoOnlineNumber(0);
					object.clear();
					object.put("taskId", Task.TASK_REQUESTNOONLINECOURSE);
					object.put("Number", 0);
					MainActivity.refresh(object);
				}
				break;
			case Task.TASK_NUMBER:
				myApplication.setStudentNumber(msg.arg1);
				object.clear();
				object.put("taskId", Task.TASK_NUMBER);
				object.put("Number", msg.arg1);
				MainActivity.refresh(object);
				break;
			case Task.TASK_REFRESH:
				courseService.deleteAllCourse();
				courseService.deleteAllNoCourse();
				ArrayList<Course> course = new ArrayList<Course>();
				course = (ArrayList<Course>) msg.obj;
				ArrayList<Course> courseNo = new ArrayList<Course>();
				courseNo = (ArrayList<Course>) msg.getData().get("noonline");
				int number;
				number=msg.arg1;
				if (course != null)
					courseService.insertCourse(course);
				myApplication.setNoOnlineNumber(course.size());

				if (courseNo != null) {
					courseService.insertNoOnlineCourse(courseNo);

				}
				myApplication.setNoOnlineNumber(courseNo.size());
				if(!msg.getData().get("college").equals("���пγ�")){
					course=courseService.queryCourse((String)msg.getData().get("college"));
					courseNo=courseService.queryNoOnlineCourse(((String)msg.getData().get("college")));
				}
				object.clear();
				object.put("taskId", Task.TASK_REFRESH);
				object.put("onLine", course.size());
				object.put("noOnline", courseNo.size());
				object.put("course", course);
				object.put("courseNo", courseNo);
				object.put("number", number);
				MainActivity.refresh(object);
				break;
			case Task.TASK_ONLINESTATE:
				break;
			case Task.TASK_LEARNLOG:
				break;
			}
		};
	};



	public static void newTask(Task task) {
		taskQueue.add(task);
	}



	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Thread thread = new TaskThread();
		thread.start();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	public class TaskThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Task newtask = null;
			while (true) {
				if ((newtask = taskQueue.poll()) != null) {
					doTask(newtask);
				}
			}
		}
	}

	public void doTask(Task task) {
		int taskId = task.getTaskId();
		HashMap<String,Object> params=task.getParams();
		HttpGetCourse httpGetCourse = new HttpGetCourse();
		JSONToArray jsonToArray = new JSONToArray();
		String json = null;
		Message msg = Message.obtain();
		switch (taskId) {
		case Task.TASK_REQUESTONLINECOURSE:
			ArrayList<Course> course = new ArrayList<Course>();
			json = httpGetCourse.getCourseJson("http://202.117.16.20/Classroom/livecourse.php");
			System.out.println("json----->" + json);
			course = jsonToArray.jsonToCourse(json);
			msg.what = taskId;
			msg.obj = course;
			handler.sendMessage(msg);
			break;
		case Task.TASK_REQUESTNOONLINECOURSE:
			ArrayList<Course> noonlinecourse = new ArrayList<Course>();
			json = httpGetCourse.getCourseJson("http://202.117.16.20/Classroom/currentnolivecourse.php");
			System.out.println("json----->" + json);
			noonlinecourse = jsonToArray.jsonToNoOnlineCourse(json);
			if (noonlinecourse != null) {
				for (int i = 0; i < noonlinecourse.size(); i++) {
					System.out.println(i + "��׼��ֱ��" + noonlinecourse.get(i).getCourseName());
				}
			}
			msg.what = taskId;
			msg.obj = noonlinecourse;
			handler.sendMessage(msg);
			break;
		case Task.TASK_NUMBER:
			int number = 0;
			number = httpGetCourse.getNumber("http://202.117.16.20/Classroom/numbercompute.php");
			System.out.println("Number:" + number);
			msg.what = taskId;
			msg.arg1 = number;
			handler.sendMessage(msg);
			break;
		case Task.TASK_REFRESH:
			ArrayList<Course> refreshCourse = new ArrayList<Course>();
			ArrayList<Course> refreshNoOnlineCourse = new ArrayList<Course>();
			int number1 = 0;
			number1 = httpGetCourse.getNumber("http://202.117.16.20/Classroom/numbercompute.php");
			System.out.println("Number:" + number1);
			json = httpGetCourse.getCourseJson("http://202.117.16.20/Classroom/livecourse.php");
			System.out.println("json----->" + json);
			refreshCourse = jsonToArray.jsonToCourse(json);
			for (int i = 0; i < refreshCourse.size(); i++) {
				refreshCourse.get(i).setVideoUrl("url");
			}
			json = httpGetCourse.getCourseJson("http://202.117.16.20/Classroom/currentnolivecourse.php");
			System.out.println("json----->" + json);
			refreshNoOnlineCourse = jsonToArray.jsonToNoOnlineCourse(json);
			msg.what = taskId;
			msg.arg1=number1;
			msg.obj = refreshCourse;
			Bundle bundle = new Bundle();
			bundle.putSerializable("noonline", refreshNoOnlineCourse);
			bundle.putString("college", (String) params.get("college"));
			msg.setData(bundle);
			handler.sendMessage(msg);
			break;
		case Task.TASK_ONLINESTATE:
			break;
		case Task.TASK_LEARNLOG:
			break;
		}
	}
}
