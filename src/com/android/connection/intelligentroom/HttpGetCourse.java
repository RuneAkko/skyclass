package com.android.connection.intelligentroom;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpGetCourse {
	private URI url;
	private HttpClient httpClient;
	public HttpGetCourse(){	
		httpClient=new DefaultHttpClient();
	}

	public String getCourseJson(String urlString) {
    	try {
			url=new URI(urlString);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	HttpGet get=new HttpGet(url);

		try {
			HttpResponse httpResponse=httpClient.execute(get);
			if(httpResponse.getStatusLine().getStatusCode()!=200){

				return null;
			}
			else{


				String str=EntityUtils.toString(httpResponse.getEntity());

				return str;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getNumber(String urlString){
		int number=0;
		try {
			url=new URI(urlString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpGet httpGet=new HttpGet(url);
		try {
			HttpResponse httpResponse=httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode()==200){

				String string=EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonObject=new JSONObject(string);

				number=jsonObject.getInt("onlinenumber");
				return number;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	public void openCourse(String urlString,int courseId,int studentId){
    	urlString=urlString+"?action=online&courseId="+courseId+"&studentId="+studentId;
    	try {
			url=new URI(urlString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpGet httpGet=new HttpGet(url);
        try {
			HttpResponse httpResponse=httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode()==200)
                 ;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }

    public void exitCourse(String urlString,int courseId,int studentId,String startDate){
    	try {
			url=new URI(urlString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	HttpPost post=new HttpPost(url);
    	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	ArrayList list=new ArrayList();
    	list.add(new BasicNameValuePair("studentId",String.valueOf(studentId)));
    	list.add(new BasicNameValuePair("courseId",String.valueOf(courseId)));
    	list.add(new BasicNameValuePair("startTime", startDate));
    	list.add(new BasicNameValuePair("endTime", simpleDateFormat.format(new Date(System.currentTimeMillis()))));
    	list.add(new BasicNameValuePair("action","offline"));
    	try {
			UrlEncodedFormEntity urlEncodedFormEntity=new UrlEncodedFormEntity(list,"UTF-8");
			post.setEntity(urlEncodedFormEntity);
			HttpResponse httpResponse=httpClient.execute(post);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				System.out.println(EntityUtils.toString(httpResponse.getEntity()));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
}

