package com.android.svod;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.giles.ui.widget.*;
import org.giles.ui.widget.OrientListView.OnRefreshListener;

import com.android.domain.Course;
import com.android.domain.LiveFile;
import com.android.json.LiveParse;
import com.android.sql.CourseService;
import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.DtstructureService;
import com.android.sql.Userservice;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.net.Uri;
import android.net.http.*;

public class Page2 extends Activity {
	/** Called when the activity is first created. */
	private EditText pathEdit;
	private InputStream is = null;
	private OrientListView listView;
	private LinearLayout noLivecourseLinearLayout;
	private LinearLayout livecourseLinearLayout;
	HashMap<String, Object> mapnew = new HashMap<String, Object>();
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	String result = "";
	private ArrayList<Course> courseList;
	private CourseService courseService;
	private String studentcode;
	SimpleAdapter adapter;
	// 直播的文件列表
	ArrayList<LiveFile> myList = null;
	String path = null;
	String Class = null;
	String Id = null;
	String Video = null;
	String Screen = null;
	String Server = null;
	String Location = null;
	Intent intent;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			listView.onRefreshComplete();
			for (LiveFile temp : myList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("classname", temp.getClassname());
				map.put("classid", temp.getClassid());
				map.put("teachername", temp.getTeachername());
				map.put("videopath", temp.getVideopath());
				map.put("starttime", temp.getStarttime());
				map.put("screenpath", temp.getScreenpath());
				map.put("serverpath", temp.getServerpath());
				map.put("location", temp.getLocation());
				map.put("IMG", R.drawable.live3);
				map.put("teacherimg", R.drawable.person);
				map.put("timeimg", R.drawable.time);
				data.add(map);
			}
			adapter.notifyDataSetChanged();
			/*if(data==null){
				noLivecourseLinearLayout.setVisibility(View.VISIBLE);
				livecourseLinearLayout.setVisibility(View.GONE);
			}
			else{
				noLivecourseLinearLayout.setVisibility(View.GONE);
				livecourseLinearLayout.setVisibility(View.VISIBLE);
			}*/
				
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		String strVer = GetSystemVersion();
		strVer = strVer.trim();
//		strVer = strVer.substring(0, 3).trim();
		float fv = Float.valueOf(strVer);
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(
					new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()

							.penaltyLog().build());
			StrictMode.setVmPolicy(
					new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page2);
		init();

	}

	public void init() {
		listView = (OrientListView) findViewById(R.id.ListView02);
		noLivecourseLinearLayout=(LinearLayout) findViewById(R.id.noLivecourseLinearLayout);
		livecourseLinearLayout=(LinearLayout)findViewById(R.id.livecourseLinearLayout);
		courseService = new CourseService(this.getApplicationContext());
		courseList = courseService.find();
		if (courseList.size() > 0) {
			studentcode = courseList.get(0).getStudentCode();
		} else
			studentcode = "000";
		AsyncTask<Void, Integer, Boolean> getLiveTask = new AsyncTask<Void, Integer, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				myList = conn();
				System.out.println("myList size is"+myList.size());
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				int number=1;
				for (LiveFile temp : myList) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("classname", temp.getClassname());
					System.out.println("classname is"+temp.getClassname());
					map.put("classid", temp.getClassid());
					System.out.println("classid is"+temp.getClassid());
					map.put("teachername", temp.getTeachername());
					System.out.println("teachername is"+temp.getTeachername());
					map.put("starttime", temp.getStarttime());
					map.put("videopath", temp.getVideopath());
					map.put("screenpath", temp.getScreenpath());
					map.put("serverpath", temp.getServerpath());
					map.put("location", temp.getLocation());
					if(number==1)
					     map.put("IMG", R.drawable.live1);
					else if(number==2)
						 map.put("IMG", R.drawable.live2);
					else
						 map.put("IMG", R.drawable.live3);
					map.put("teacherimg", R.drawable.person);
					map.put("timeimg", R.drawable.time);
					data.add(map);
					number++;
				}
				adapter = new SimpleAdapter(Page2.this, data, R.layout.item2,
						new String[] { "IMG", "teacherimg", "timeimg", "classname", "classid", "teachername", "starttime",
								"videopath", "screenpath", "serverpath", "location" },
						new int[] { R.id.imageView1, R.id.teacher, R.id.time, R.id.textView1, R.id.textView2, R.id.textView3,
								R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8 });
				listView.setAdapter(adapter);
			}
		};
		getLiveTask.execute();	
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				data.clear();
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						myList = conn();
						handler.sendEmptyMessageDelayed(0, 1000);
					}
				}.start();
				
			}
			public void onNextPage() {

			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ListView listView = (ListView) parent;
				HashMap<String, Object> itemData = (HashMap<String, Object>) data.get(position - 1);
				Class = itemData.get("classname").toString();
				Id = itemData.get("classid").toString();
				Video = itemData.get("videopath").toString();
				Screen = itemData.get("screenpath").toString();
				Server = itemData.get("serverpath").toString();
				Location = itemData.get("location").toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(Page2.this);
				if ("0000-00-00 00:00:00".equals(itemData.get("starttime").toString())) {
					//builder.setTitle("提示").setMessage("尚未开始，当前视频无法播放！").setCancelable(true);
					View nostartView= LayoutInflater.from(getApplication()).inflate(R.layout.nostart,null);
					builder.setView(nostartView);
					AlertDialog alert = builder.create();
					alert.show();
				} else {
					//builder.setTitle("提示").setMessage("请选择您要播放的视频：").setCancelable(true)；
					View selectView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.selectvideo,null);
					builder.setView(selectView);
					builder.setPositiveButton("教师视频", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
//									intent = new Intent(Page2.this, VideoPlayerActivity.class);
									intent = new Intent(Page2.this, VideoPlayerNewActivity .class);
									path = "http://" + Server + ":8134/hls-live/" + Location + "/_d efinst_/" + Video
											+ "/" + Video + ".m3u8";
									Log.i("path", path);
									intent.putExtra("mpath", path);
									intent.putExtra("title", Class);
									intent.putExtra("studentcode", studentcode);
									intent.putExtra("courseid", "0");
									intent.putExtra("path", Class);
									intent.putExtra("isOnlinePlay", true);
									intent.putExtra("isLive", true);
									startActivity(intent);
								}
							}).setNegativeButton("屏幕视频", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
//									intent = new Intent(Page2.this, VideoPlayerActivity.class);
									intent = new Intent(Page2.this, VideoPlayerNewActivity .class);
									path = "http://" + Server + ":8134/hls-live/" + Location + "/_definst_/" + Screen
											+ "/" + Screen + ".m3u8";
									//path="http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
									Log.i("path", path);
									intent.putExtra("mpath", path);
									intent.putExtra("title", Class);
									intent.putExtra("studentcode", studentcode);
									intent.putExtra("courseid", "0");
									intent.putExtra("path", Class);
									intent.putExtra("isOnlinePlay", true);
									intent.putExtra("isLive", true);
									startActivity(intent);
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});
	}

	// 获取到当前正在直播的课程,并且进行处理
	public ArrayList<LiveFile> conn() {
		ArrayList<LiveFile> myList2 = null;
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		try {
			HttpClient httpclient = new DefaultHttpClient(params);
			//获得直播流具体信息所在的界面
			SharedPreferences urlPreferences = getSharedPreferences("liveUrl", 0);
			String liveUrl = urlPreferences.getString("liveurl", "http://202.117.16.193/iphone/hls.php");
			HttpPost httppost = new HttpPost(liveUrl);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
            is = entity.getContent();
			BufferedReader bin=new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line=null;
			while ((line = bin.readLine())!=null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			System.out.println("result is"+result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result" + e.toString());
		}
		try {

			LiveParse liveParse = new LiveParse();
			liveParse.JSONToBean(result);
			myList2 = liveParse.getMyList();

		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
		}
		return myList2;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			View quitView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.quit,null);
			new AlertDialog.Builder(this).setView(quitView)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
		}

		return super.onKeyDown(keyCode, event);
	}

	public static String GetSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

}
