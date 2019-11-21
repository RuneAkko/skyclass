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

public class GetCourseDoc {
	private static final int REQUEST_TIMEOUT = 5*1000;
	private static final int SO_TIMEOUT = 10*1000;
    public HttpClient getHttpClient()
{
    BasicHttpParams httpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
    HttpClient client = new DefaultHttpClient(httpParams);
    return client;
}
	 public String request(String courseid){
   	  String responseMsg="";
   	  String urlStr = "https://xueli.xjtudlc.com/MobileLearning/Get_AttachmentList.aspx?CourseID="+courseid;
       HttpPost request = new HttpPost(urlStr);
       InputStream is = null;
       try
       {

           HttpClient client = getHttpClient();

           HttpResponse response = client.execute(request);

           if(response.getStatusLine().getStatusCode()==200)
           {

               responseMsg = EntityUtils.toString(response.getEntity());
            }else
               {
           	   responseMsg=null;
           	}
       }catch(Exception e)
       {
           e.printStackTrace();
       }
       String end = (responseMsg.trim().substring(responseMsg.length()-4, responseMsg.length()));
       if(responseMsg.equals("null"))
       {
    	   return null;
       }
       else if(end.equals("null")){
           return responseMsg.trim().substring(10,responseMsg.length()-5).replace(":]", ":[]").replace(" ", "");}
       else
    	   return responseMsg.trim().substring(10,responseMsg.length()-1).replace(":]", ":[]").replace(" ", "");
   }


}
