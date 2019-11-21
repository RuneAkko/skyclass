package com.android.svod;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.sql.CourseService;
import com.android.sql.studyProcessService;
import com.android.connection.GetRequest;
import com.android.connection.LoginCheck;
import com.android.domain.Course;
import com.android.domain.TableData;
import com.android.domain.courseDoc;
import com.android.domain.studyProgress;
import com.android.encrypt.MD5;
import com.android.json.JsonParse;
import com.android.connection.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.crashreport.CrashReport;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Login extends AppCompatActivity {


	public static Login login1 = null;

	private AutoCompleteTextView cardNumAuto;
	private EditText passwordET;
	private ImageView logo;
	private Button logBT;
	private CourseService courseService;
	private CheckBox savePasswordCB;
	private SharedPreferences sp;
	private String cardNumStr;
	public static String userNumber,studentcode ;
	private String passWordStr;
	private String passmd5;
	private String passencrypt;
	
	//该学生信息
	private String studentCode;
	private studyProgress progress = new studyProgress();
	private studyProcessService sPS;

	private Boolean FLAG = false;
	private Boolean ENCRYPT = false;
	private LoginCheck login;
	private int flag_xueli;//标识学历or非学历
	private ProgressDialog pd;


	private static String TAG = "Login";

	

	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case LoginCheck.Login_state:

		}
		return super.onCreateDialog(id);
	}
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case LoginCheck.Login_state:
					break;
				default:
					break;
				}
			}
		}
	};

	private void initYoumeng() {
		//------------------友盟统计----------------------
		MobclickAgent.setScenarioType(this,MobclickAgent.EScenarioType.E_UM_NORMAL);//设置场景模式为普通
		MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计方式
		MobclickAgent.setCatchUncaughtExceptions(true);//捕获程序崩溃日志
		MobclickAgent.setDebugMode(true);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		ContextCompat.checkSelfPermission()
		//
		if(
				ContextCompat.checkSelfPermission(Login.this, Manifest.permission.RECORD_AUDIO)
						!=
						PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.ACCESS_FINE_LOCATION)
								!=
								PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.ACCESS_COARSE_LOCATION)
								!=
								PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.READ_PHONE_STATE)
								!=
								PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.READ_CONTACTS)
								!=
								PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.CALL_PHONE)
								!=
								PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.READ_EXTERNAL_STORAGE)
								!=
								PackageManager.PERMISSION_GRANTED
						||
						ContextCompat.checkSelfPermission(Login.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
								!=
								PackageManager.PERMISSION_GRANTED

		){
			ActivityCompat.requestPermissions(
					Login.this,
					new String[]{
							Manifest.permission.RECORD_AUDIO,
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION,
							Manifest.permission.READ_PHONE_STATE,
							Manifest.permission.READ_CONTACTS,
							Manifest.permission.CALL_PHONE,
							Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE},
					1
			);
		}






		CrashReport.initCrashReport(getApplicationContext(), "8bbbd0b4de", false);
		login1 = this;	
		String strVer = GetSystemVersion();
		strVer = strVer.trim();
//		strVer = strVer.substring(0, 3).trim();

		float fv = Float.valueOf(strVer);
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																			// 就包括了磁盘读写和网络I/O
					.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
					.penaltyLog() // 打印logcat
					.penaltyDeath().build());
		}

		/****************************/
		
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        initYoumeng();//初始化友盟统计
		setContentView(R.layout.remeberpwdactivity);
		courseService = new CourseService(this.getApplicationContext());
		sPS = new studyProcessService(this.getApplicationContext());
		cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		cardNumAuto.setThreshold(1);
		passwordET = (EditText) findViewById(R.id.passwordET);
		logBT = (Button) findViewById(R.id.logBT);
		logo = (ImageView) findViewById(R.id.logo);
		savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);
		
		passwordET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		pd = new ProgressDialog(Login.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度
		pd.setMessage("登录中，请稍后。。。");
		pd.setCancelable(false);
		pd.setOnKeyListener(onKeyListener);
		
		login = new LoginCheck(this.getApplicationContext());
		
		LoadUserDate();

		if (sp.getBoolean("isSaved", false)) {
			// Toast.makeText(Login.this, "登陆成功，正在加载请稍候...",
			// Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(getApplication(), TabHostActivity.class);
			flag_xueli = login.usrType();
			
			//获取该学生的学习进度数据；
			studentCode = login.getStudentCode();
			getStudentProgress(studentCode,flag_xueli);

			
			Bundle args = new Bundle();
			
			args.putInt("flag", flag_xueli);
			
			intent.putExtras(args);
			startActivity(intent);
			Login.this.finish();
		}

		
		cardNumAuto.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

				passwordET.setText("");
				ENCRYPT = false;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String qNM = sp.getString("nm", "");
				String[] qnM = qNM.split("#");

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(Login.this, android.R.layout.simple_dropdown_item_1line, qnM);
				cardNumAuto.setAdapter(adapter);
			}

		});

		
		logBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				System.out.println("执行到此");
				Log.i(TAG,"log BT IS CLICK");
				// TODO Auto-generated method stub
				ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				State mobile = manager.getNetworkInfo(
						ConnectivityManager.TYPE_MOBILE).getState();
				State wifi = manager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState();

				if ((mobile == State.CONNECTED || mobile == State.CONNECTING)
						|| (wifi == State.CONNECTED || wifi == State.CONNECTING)) {

					pd.show();
					Log.i(TAG, "test is called -1");

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Looper.prepare();
							//判定用户是否登陆成功并获取到用户的相关数据；
							SaveUserDate();
							userNumber=cardNumStr;
							if (FLAG) {
								flag_xueli = login.usrType();
								
								//获取该学生的学习进度数据；
								studentCode = login.getStudentCode();
								studentcode =studentCode;
								getStudentProgress(studentCode,flag_xueli);

								Intent intent = new Intent(getApplication(),
										TabHostActivity.class);
								Bundle args = new Bundle();
								args.putInt("flag", flag_xueli);
								intent.putExtras(args);
								startActivity(intent);
								handler.sendEmptyMessage(0);
								Login.this.finish();
								Log.i(TAG, "test is called 0");
							} else {

								Log.i(TAG, "test is called 1");
								handler.sendEmptyMessage(1);

							}

						}
					}).start();
				}
                //否则进行网络的设置
				else
					showTips();

			}
		});


		//启动权限检查函数





	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
			switch (msg.what) {
			case 0:
				pd.dismiss();// 关闭ProgressDialog
				Toast.makeText(Login.this, "登录成功，正在加载请稍候...",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				pd.dismiss();
				Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};




	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				pd.dismiss();
			}
			return false;
		}
	};

	public static String GetSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			View view= LayoutInflater.from(getApplication()).inflate(R.layout.quit,null);
			new AlertDialog.Builder(this)
					.setView(view)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 用户登录获取用户的相关的信息
	 */
	public void SaveUserDate() {
		cardNumStr = cardNumAuto.getText().toString();
		passWordStr = passwordET.getText().toString();
		passmd5 = MD5.MD5(passWordStr);
		passencrypt = MD5.encryptmd5(passmd5);

		// String testmd5 = md5.MD5("testpwd");
		// String testencrypt = md5.encryptmd5(testmd5);
		//
		// String test2md5 = md5.MD5("testpwd2");
		// String test2encrypt = md5.encryptmd5(test2md5);
		//
		// if(!(((cardNumStr.equals("test"))&&(passWordStr.equals("testpwd"))||(passWordStr.equals(testencrypt)))||((cardNumStr.equals("test2"))&&(passWordStr.equals("testpwd2"))||(passWordStr.equals(test2encrypt))))){
		// Toast.makeText(RemeberPwdActivity.this,"��֤��������������",Toast.LENGTH_SHORT).show();
		// FLAG = false;
		// return;
		// }

		if (ENCRYPT) {
			if (!login.validate(cardNumStr, passWordStr)) {
				 Toast.makeText(Login.this,"验证失败",Toast.LENGTH_SHORT).show();
				FLAG = false;
				logBT.setClickable(true);
				return;
			}
		} else {
			if (!login.validate(cardNumStr, passencrypt)) {
				Toast.makeText(Login.this,"验证失败",Toast.LENGTH_SHORT).show();
				FLAG = false;
				logBT.setClickable(true);
				return;
			}
		}

		sp = getSharedPreferences("passwordFile", 0);

		Editor spEd = sp.edit();
		String qNM = sp.getString("nm", "");
		String qPWD = sp.getString("pwd", "");
		int qCOUNT = sp.getInt("count", 0);

		if (savePasswordCB.isChecked()) {
			int count;
			spEd.putBoolean("isSaved", true);
			if (!qNM.contains(cardNumStr)) {
				qNM += "#" + cardNumStr;
				qPWD += "#" + passencrypt;
				spEd.putInt("count", qCOUNT + 1);
			}
			spEd.putString("nm", qNM);
			spEd.putString("pwd", qPWD);
			String[] qnM = qNM.split("#");
			for (count = 0; count < qnM.length; count++)
				if (qnM[count].compareTo(cardNumStr) == 0)
					break;
			spEd.putInt("count", count);
		} else {
			spEd.putBoolean("isSaved", false);
			spEd.putString("nm", qNM);
			spEd.putString("pwd", qPWD);
			spEd.putInt("count", qCOUNT);
		}
		spEd.commit();
		FLAG = true;
	}

	public boolean getisSaved() {
		return FLAG;
	}

	/**
	 * 初始化用户数据,用户点击记住用户的密码后进行的操作
	 */
	private void LoadUserDate() {
		sp = getSharedPreferences("passwordFile", 0);
		if (sp.getBoolean("isSaved", false)) {
			String qNM = sp.getString("nm", "");
			String qPWD = sp.getString("pwd", "");
			int count = sp.getInt("count", 0);
			String[] qnM = qNM.split("#");
			String[] qpWD = qPWD.split("#");

			if (!("".equals(cardNumStr) && "".equals(passWordStr))) {
				cardNumAuto.setText(qnM[count]);
				passwordET.setText(qpWD[count]);
				savePasswordCB.setChecked(true);
			}

			ENCRYPT = true;
		}
	}
    //获取到学生的进度的数据
	public void getStudentProgress(String studentCode,int flag){
		GetRequest rt = new GetRequest();
		String url ="";
		if(flag == 0){
	    	url = "https://xueli.xjtudlc.com/mobilelearning/LearningProgress.aspx?studentcode="+studentCode;

		}
		else
	        url = "https://feixueli.xjtudlc.com/mobilelearning/LearningProgress.aspx?studentcode="+studentCode;

    	String result = rt.request(url);
    	
    	try{
			Gson gson = new Gson();
			TableData tableDatadoc = gson.fromJson(result,  
	                new TypeToken<TableData>() {  
	                }.getType());  
			List tableProgresslist = tableDatadoc.getRows();
			String strtmp = gson.toJson(tableProgresslist.get(0));
		    Map<String,String> AllProgress =  gson.fromJson(strtmp, new TypeToken<Map<String,String>>(){
		    	
		    }.getType());
		    progress.setGradeSum(AllProgress.get("SUMCREDITHOUR"));
		    progress.setGradeTotal(AllProgress.get("TOTALCREDITHOUR"));
		    progress.setGraduatehighlimDays(AllProgress.get("GRADUATEHIGHLIM"));
            progress.setGraduatelowlimDays(AllProgress.get("GRADUATELOWLIM"));
            progress.setStudyDays(AllProgress.get("STUDYDAYS"));
		    
            sPS.save(progress);
			
			
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	//保存下载下来的数据到数据库中
	public void saveCourse(int flag) {

		ArrayList<Course> courses = json(cardNumStr, passencrypt,flag);
		if (courses.size() > 0) {
			System.out.println(courses.size());
			for (int i = 0; i < courses.size(); i++) {
				courseService.save(courses.get(i));
			}
		}
		System.out.println("0000");
		// return courses;
	}

	
	public ArrayList<Course> json(String name, String password,int flag) {
		ArrayList<Course> courseList = null;

		String s = login.request(name, password,flag);
		try {

			JsonParse testJson = new JsonParse(this.getApplicationContext());

			testJson.JSONToBean(s);

			courseList = testJson.getCourse();

			for (Course temp : courseList) {
				Log.e("log_tag", temp.getCourseId());
				Log.e("log_tag", temp.getStudentCode());
				Log.e("log_tag", temp.getCourseCode());
				Log.e("log_tag", temp.getCourseName());

			}

		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
		}
		return courseList;
	}

	// 检查网络状态̬
//	public void CheckNetworkState() {
//		// logBT.setClickable(false);
//		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		// State mobile =
//		// manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
//		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
//		/*
//		 * if(mobile == State.CONNECTED||mobile==State.CONNECTING) return;
//		 */
//		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
//			return;
//		showTips();
//	}


	// 检查网络状态 更新代码
	public void CheckNetworkState(){

		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
		boolean netWorkState = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

		if(netWorkState){
			return;
		}
		showTips();

	}

	
	public void showTips() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Login.login1);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("无法连接网络");
		builder.setMessage("检测到您当前网络处于未连接状态，是否设置网络？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 用户设置网络
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if (sp.getBoolean("isSaved", false)) {

					Toast.makeText(Login.this, "登录成功，正在加载请稍候...",
							Toast.LENGTH_SHORT).show();
					
					
					flag_xueli = login.usrType();
					
					//获取该学生的学习进度数据；
					studentCode = login.getStudentCode();
					getStudentProgress(studentCode,flag_xueli);

					Intent intent = new Intent(getApplication(),
							TabHostActivity.class);
					Bundle args = new Bundle();
					
					args.putInt("flag", flag_xueli);
					intent.putExtras(args);
					startActivity(intent);
					Login.this.finish();
				} else {
					Toast.makeText(Login.this, "请您设置网络...", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		builder.create();
		builder.show();
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		pd.dismiss();
	}



}
