package com.android.connection;

import com.android.sql.CourseService;
import com.android.sql.DocService;
import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.DtstructureService;
import com.android.svod.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.android.domain.Course;
import com.android.domain.courseDoc;
import com.android.json.JsonParse;

import android.content.Context;
import android.util.Log;

/**
 * @author administrator
 *
 */
public class LoginCheck {
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时5秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	public static final int Login_state = 100;
	private Context context;
	//private static SSLSocketFactory socketFactory;
	GetCourseTree courseTree = new GetCourseTree();
	JsonParse jpcourse;
	JsonParse jpdoc;
	String studentCode;
	int flag = 0;// 标识学历or非学历,默认学历（0表示学历）
	ArrayList<Integer> studyProcess = new ArrayList<Integer>();
	ArrayList<Course> courseList = new ArrayList<Course>();
	ArrayList<courseDoc> doclist = new ArrayList<courseDoc>();
	private CourseService courseService;
	private DocService docservice;
	//private DtstructureService dtstructureService;
	private DtplaycourseService dtplaycourseService;
	private DtcourseService dtcourseService;

	public LoginCheck(Context context) {
		this.context = context;
		courseService = new CourseService(context);
		jpcourse = new JsonParse(context);
		jpdoc = new JsonParse(context);
	}

	public boolean validate(String name, String password) {

		//表明用户登录是否正确
		boolean loginValidate = false;
		// 登录成功，保存学生学习进度相关数据
       //学历
		this.flag = 0;
		String result = request(name, password, this.flag);// 先检查学历的数据库
		System.out.println("result"+result);
		// 登录成功 则保存课程数据
		if (result.contains("[") && result.contains("]")) {
			loginValidate = true;
			Log.e("result", result);
			// 调用activity的函数
			Login.login1.saveCourse(this.flag);
			courseList = courseService.find();
			if(courseList!=null&&courseList.size()!=0) {
				studentCode = courseList.get(0).getStudentCode();
			}
			else{
				try {
					JSONArray jsonArray = new JSONArray(result);
					JSONObject jsonObject=jsonArray.getJSONObject(0);
					studentCode=jsonObject.getString("StudentCode");
				}catch (Exception e){

				}

			}
			for (int i = 0; i < courseList.size(); i++) {
				String currentCourseCode = courseList.get(i).getCourseCode();
				String r1 = courseTree.request(courseList.get(i).getCourseCode(), this.flag);
				//String r2 = courseTree.requestDoc("161", this.flag);//test
				String r2 = courseTree.requestDoc(courseList.get(i).getCourseId(), this.flag);
				if (r2.contains("{") && r2.contains("}"))
					jpdoc.JsonCourseDoc(r2, courseList.get(i).getCourseId());
					//jpdoc.JsonCourseDoc(r2, "161");
				/*else{
					r2 = "{\"Name\":\"Table\",\"Rows\":[{\"ArticleID\":\"null \",\"InfoLevel\":\"null\",\"MainHead\":\"null\",\"CreateTime\":\"null\",\"BeginDate\":\"null\",\"EndDate\":\"null\",\"SendObjectID\":\"null\",\"ArticleTypeName\":\"null\"}]}"; //没有课件
					jpdoc.JsonCourseDoc(r2, courseList.get(i).getCourseId()); //test
				}*/

				if (r1 == null)// 只有第一层目录,针对只有课程没有课件的情况
					jpcourse.JsonCourseS(r1, courseList.get(i).getCourseCode());
				else
					jpcourse.JsonCourse(r1,currentCourseCode);

			}
			// json(name,password);
			//非学历
		} else {
			this.flag = 1;
			String result1 = request(name, password, this.flag);// 学历没有结果，请求非学历
			System.out.println("result1"+result1);
			if (result1.contains("[") && result1.contains("]")) {
				loginValidate = true;
				Log.e("result", result1);
				// 调用activity的函数
				Login.login1.saveCourse(this.flag);
				courseList = courseService.find();
				if(courseList!=null&&courseList.size()!=0) {
					studentCode = courseList.get(0).getStudentCode();
				}else {
					try {
						JSONArray jsonArray = new JSONArray(result);
						JSONObject jsonObject = jsonArray.getJSONObject(0);
						studentCode = jsonObject.getString("StudentCode");

					} catch (Exception e) {

					}
				}
				for (int i = 0; i < courseList.size(); i++) {
					String r1 = courseTree.request(courseList.get(i).getCourseCode(), this.flag);
					String r2 = courseTree.requestDoc(courseList.get(i).getCourseId(), this.flag);
					if (r2.contains("{") && r2.contains("}")) {
						jpdoc.JsonCourseDoc(r2, courseList.get(i).getCourseId());
					}

					if (r1 == null)// 只有第一层目录,针对只有课程没有课件的情况
						jpcourse.JsonCourseS(r1, courseList.get(i).getCourseCode());
					else
						jpcourse.JsonCourse(r1);

				}

			} else {
				loginValidate = false;
			}
		}
		return loginValidate;
	}

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	//登录成功获得相应的课程数据
	public String request(String name, String password, int flag) {
		String responseMsg = "";
		String urlStr = "";
		// 使用apache HTTP客户端实现
		if (flag == 0) {// 学历通道
			urlStr = "https://xueli.xjtudlc.com/MobileLearning/loginCheck.aspx";
		} else if (flag == 1) {// 非学历通道
			urlStr = "https://feixueli.xjtudlc.com/MobileLearning/loginCheck.aspx";
		}
		//socketFactory = SSLUtil.getSSLSocketFactory(context.getAssets().open("public.cer"));
		HttpPost request = new HttpPost(urlStr);
		InputStream is = null;
		// 如果传递参数多的话，可以丢传递的参数进行封装װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加用户名和密码
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("password", password));
		try {
			// 设置请求参数项
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// 执行请求返回相应
			HttpResponse response = client.execute(request);
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());

			} else {
				responseMsg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseMsg;
	}

	public int usrType() {
		return this.flag;
	}

	public String getStudentCode() {
		return this.studentCode;
	}

}
