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

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class uploadService extends IntentService{
	//  private MyBinder myBinder = new MyBinder();
	private static final String TAG ="uploadService";
	private Intent intent1 ;
	private final OkHttpClient client = new OkHttpClient();
//	public static final MediaType MEDIA_TYPE_CSV = MediaType.parse("text/x-csv;charset=utf-8");


	public uploadService(){
		super("uploadService");
	}
public uploadService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

FileUploadTask uploadTask = null;
//	private static final String TAG = "MyService";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();	
		 Log.i(TAG, "onCreate");
	
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
    	super.onDestroy();
    	 Log.i(TAG, "onDestroy");
    	//uploadTask.cancel(false);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
   	    Log.i(TAG, "onStart");
		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub		
		Log.i(TAG, "onHandleIntent");
		    File uploadFile = new File("mnt/sdcard/record_skyclass/");
			intent1 = new Intent();
		    if(!uploadFile.exists()){  
		    	System.out.println("haimei jilu");
		    	intent1.setAction("nostart");
		    	sendBroadcast(intent1);
		    }
		    else 
		    {		    	  
			    File[]  files = uploadFile.listFiles();  
				int fileCount = files.length;
				//int uploadCount = 0; //new 优化循环
		    	if(fileCount==0){		    
							intent1.setAction("nolog");
							sendBroadcast(intent1);
		    	} else {
		    	 	//HttpClient已被废止
//		  		    final HttpClient httpclient = new DefaultHttpClient();
//		  			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

					for(int i=0;i<fileCount;i++){
//		  					    HttpPost httppost = new HttpPost("https://sensorlog.xjtudlc.com/record/upload.php");
//		  					    MultipartEntity entity = new MultipartEntity();
							File fileBody = files[i];
							MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
							builder.addFormDataPart("uploadedfile",fileBody.getName(),RequestBody.create(MediaType.parse("text/csv"),fileBody));
							MultipartBody requestBody = builder.build();
//							RequestBody requestBody = new MultipartBody.Builder()
//									.setType(MultipartBody.FORM).addFormDataPart(
//											"uploadedfile",fileBody.getName(),RequestBody.create(MediaType.parse("text/csv"),fileBody))
//									.
							Request request = new Request.Builder()
									.url("http://118.190.245.63:5000/sensor_log_post/")
									.post(requestBody)
									.build();


//								entity.addPart("uploadedfile", fileBody);
//								httppost.setEntity(entity);
//								HttpResponse response = null;
						    Response response = null;
						    String result = null;
						    try{
						    	response = client.newCall(request).execute();
						    	result = response.body().string();
						    	Log.i("请求结果：",result);
						    	response.body().close();
							}catch (IOException e){
						    	e.printStackTrace();
							}

                            if(response != null&&response.code() == 200){
                                files[i].delete();
                                fileCount -= 1;
                            }

                            if(fileCount ==0) //有多个传感器日志时，不能一次发送完
                            //if (uploadCount == fileCount)
                            {
                                intent1.setAction("finishupload");
                                sendBroadcast(intent1);
                            }
                            else
                            {
                                intent1.setAction("notfinish");
                                sendBroadcast(intent1);
                            }

//								try(Response res) {
//									System.out.println("开始上传啦");
//									response = httpclient.execute(httppost);
//								} catch (Exception ec) {
//									  ec.printStackTrace();
//								}
//							    HttpEntity resEntity = null;





//							    if (resEntity != null) {
//										try {
//											String s=EntityUtils.toString(resEntity);
//											if(s.contains("true"))
//											{
//												files[i].delete();
//												fileCount -= 1;
//												//uploadCount += 1;
//											}
//										} catch (ParseException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										} catch (IOException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//							    }

							}


		  			}
		     
		    }
		 
		  
	}

}
