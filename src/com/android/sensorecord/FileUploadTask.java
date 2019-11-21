package com.android.sensorecord;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

class FileUploadTask extends AsyncTask<Object,Integer,Void>{ 
	    private File uploadFile = new File(Environment.getExternalStorageDirectory().getPath(),"record_skyclass");
	    File[]  files = uploadFile.listFiles();  
	@Override
	protected Void doInBackground(Object... params) {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		
		for(int i=0;i<files.length;i++){
			HttpPost httppost = new HttpPost("http://202.117.16.193/mo/upload.php");        		
    		MultipartEntity entity = new MultipartEntity();
		/*String srcPath=Environment.getExternalStorageDirectory().getPath()+"/pic.jpg";*/
		System.out.println("lujing"+files[i].getName()+files[i]);	   
		FileBody fileBody = new FileBody(files[i]);
		entity.addPart("uploadedfile", fileBody);
			
		httppost.setEntity(entity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			System.out.println("eeeeeeeee1"+e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("eeeeeeeee2"+e1);
			e1.printStackTrace();
		}
		
		HttpEntity resEntity = response.getEntity();
	      
		if (resEntity != null) {			
			try {
				String s=EntityUtils.toString(resEntity);
				System.out.println("responseresponseresponse"+s);
				if(s.contains("true"))					
				     System.out.print("ddddddddd"+s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("eeeeeeeee3"+e);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("eeeeeeeee4"+e);
				e.printStackTrace();
			}finally{
				files[i].delete();
			}
		}

		
		}
		httpclient.getConnectionManager().shutdown();
		return null;
	}
	
}