package com.android.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class GetCourseTree {
	private static final int REQUEST_TIMEOUT = 10000;//5 * 1000;// 设置请求超时5秒钟
	private static final int SO_TIMEOUT = 20000;//10 * 1000; // 设置等待数据超时时间10秒钟

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		//设置请求超时
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		//设置等待数据超时
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
    //获得课程详细的章节目录
	public String request(String courseCode,int flag) {
		String responseMsg = "";
		// 使用apache HTTP客户端实现
		String urlStr ="";
		if(flag ==0 ){//学历通道
			//urlStr = "http://xueli.xjtudlc.com/MobileLearning/Get_Coursetree.aspx?cno=" + courseCode;
		    urlStr = "https://kjguanli.xjtudlc.com/index.php/Xjtudlc/xueli/get_course/cno/"+courseCode;
		}
		else if(flag==1){//非学历通道
			urlStr = "https://feixueli.xjtudlc.com/MobileLearning/Get_Coursetree.aspx?cno="+courseCode;
		}
		
		HttpPost request = new HttpPost(urlStr);
		InputStream is = null;
		try {
			// 设置请求参数项
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
		String end = (responseMsg.trim().substring(responseMsg.length() - 4,
				responseMsg.length()));
		//对返回数据进行的处理
		if (responseMsg.equals("null")) {
			return null;
		} else if (end.equals("null")) {
			return responseMsg.trim().substring(10, responseMsg.length() - 5)
					.replace(":]", ":[]").replace(" ", "");
		} else
			return responseMsg.trim().substring(10, responseMsg.length() - 1)
					.replace(":]", ":[]").replace(" ", "");
	}
    //获得课程资料等相关的信息
	public String requestDoc(String courseID,int flag) {
		String responseMsg = "";
		String urlStr ="";
		// 使用apache HTTP客户端实现
		if(flag==0){
			urlStr = "https://xueli.xjtudlc.com/MobileLearning/Get_AttachmentList.aspx?CourseID="
					+ courseID;
		}
		else if(flag ==1){
			urlStr = "https://feixueli.xjtudlc.com/MobileLearning/Get_AttachmentList.aspx?CourseID="
					+ courseID;
		}
		
		HttpPost request = new HttpPost(urlStr);
		InputStream is = null;
		try {
			// 设置请求参数项

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
		
		String end = (responseMsg.trim().substring(responseMsg.length() - 4,
				responseMsg.length()));
		if (responseMsg.equals("null")) {
			return null;
		} else if (end.equals("null")) {
			return responseMsg.trim().substring(0, responseMsg.length() - 4)
					.replace(":]", ":[]").replace(" ", "");
		} else
			return responseMsg.trim().substring(0, responseMsg.length())
					.replace(":]", ":[]").replace(" ", "");
	}
}
