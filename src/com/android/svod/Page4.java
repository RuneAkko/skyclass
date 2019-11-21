package com.android.svod;

import com.umeng.analytics.MobclickAgent;

import java.io.File;

import com.android.adapter.svod.PersonalCenterAdapter;
import com.android.svod.R;
import com.android.updater.UpdateConfig;
import com.android.updater.UpdateService;
import com.android.updater.UpdateUtil;
import com.android.svod.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.view.ViewGroup.LayoutParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.connection.SendUserFile;
import com.android.domain.Course;
import com.android.domain.User;
import com.android.domain.studyProgress;
import com.android.sensorecord.uploadService;
import com.android.sql.CourseService;
import com.android.sql.DocService;
import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.DtstructureService;
import com.android.sql.Userservice;
import com.android.sql.studyProcessService;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CompoundButton;

import org.w3c.dom.Text;

public class Page4 extends Activity implements ViewSwitcher.ViewFactory {

	private ProgressBar pb;
	private TextSwitcher downloading_kb;
	private TextView downloading_percent;
	private TextView versionname,centerText;
	private TextView gradeTotalView, gradeSumView;
	private TextView resultView;
	private TextView TRsendLog,TRsendSensor;
	private PopupWindow topRight;
	private ProgressBar mstudyProgress;
	//private ExpandableListView expandableListView;
	private Button systemSettings,presonalButton,sendLog, onlineService,sendSensor, updateCourse, delete;
	private Button scoreButton,priceButton,personButton;
	private LinearLayout commonLinearLayout,personalLinearLayout,backLinearLayout,sendLogLayout,sendSensorLayout;
	private ArrayList<String> parentContent=new ArrayList<String>();
	private ArrayList<ArrayList<String>> childContent=new ArrayList<ArrayList<String>>();
	ConnectivityManager cm = null;
	State wifi = null;
	private SendUserFile sendUserFile;
	private Userservice userservice;
	private ArrayList<Course> courseList;
	private CourseService courseService;
	private DtcourseService dtcourseService;
	private DocService docservice;
	private DtstructureService dtstructureService;
	private DtplaycourseService dtplaycourseService;
	private studyProcessService studyprocessService;
	private UploadReceiver uploadReceiver;
	private String studentcode;
	List<User> usrs;
	studyProgress iProgress;
	int flag;// 学历or非学历
	Button quitReg, update;
	private ProgressDialog pd;
	HashMap<String, String> mHashMap;
	SharedPreferences sp;

	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mstudyProgress.setVisibility(ProgressBar.VISIBLE);
				gradeSumView.setVisibility(View.VISIBLE);
				gradeTotalView.setVisibility(View.VISIBLE);
				resultView.setVisibility(View.VISIBLE);
				gradeSumView.setText("您当前学习进度为" + iProgress.getGradeTotal() + "学分");
				gradeTotalView.setText("总学业进度为" + iProgress.getGradeSum() + "学分");
				mstudyProgress.setMax(Integer.parseInt(iProgress.getGradeSum()));
				mstudyProgress.setProgress(Integer.parseInt(iProgress.getGradeTotal()));
				float num = (float) mstudyProgress.getProgress() / (float) mstudyProgress.getMax();
				int result = (int) (num * 100);

				resultView.setText(result + "%");

				break;

			}

		}
	};
	Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case UpdateConfig.DIALOG_UPDATE:
					showDialog(UpdateConfig.DIALOG_UPDATE);
					break;
				case UpdateConfig.DIALOG_NOUPDATE:
					showDialog(UpdateConfig.DIALOG_NOUPDATE);
					break;
				case UpdateConfig.DIALOG_NONETWORK:
					showDialog(UpdateConfig.DIALOG_NONETWORK);
					break;
				case 1:
					pb.setProgress(msg.arg1);
					UpdateConfig.loading_process = msg.arg1;

					if (UpdateConfig.MB < 1024) {
						UpdateConfig.total = String.format("%.2f", UpdateConfig.MB) + "B";
					} else if (UpdateConfig.MB < 1024 * 1024 && UpdateConfig.MB > 1024) {
						UpdateConfig.total = String.format("%.2f", UpdateConfig.MB / 1024) + "K";
					} else {
						UpdateConfig.total = String.format("%.2f", UpdateConfig.MB / 1024 / 1024) + "M";
					}

					if (UpdateConfig.KB < 1024) {
						UpdateConfig.now = String.format("%.2f", UpdateConfig.KB) + "B";
					} else if (UpdateConfig.KB < 1024 * 1024 && UpdateConfig.KB > 1024) {
						UpdateConfig.now = String.format("%.2f", UpdateConfig.KB / 1024) + "K";
					} else {
						UpdateConfig.now = String.format("%.2f", UpdateConfig.KB / 1024 / 1024) + "M";
					}

					String text = UpdateConfig.now + "/" + UpdateConfig.total;

					downloading_kb.setText(text);
					downloading_percent.setText(UpdateConfig.loading_process + "%");
					break;
				case 2:
					finish();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									getResources().getString(R.string.appname) + ".apk")),
							"application/vnd.android.package-archive");
					startActivity(intent);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(Page4.this, error, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTheme(R.style.Theme.Sherlock.Light.WallPaper);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page4);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		flag = bundle.getInt("flag");

		studyprocessService = new studyProcessService(this.getApplicationContext());
		if (studyprocessService.find().size() != 0) {
			iProgress = studyprocessService.find().get(0);
		}
		gradeTotalView = (TextView) findViewById(R.id.gradeTotal);
		gradeSumView = (TextView) findViewById(R.id.gradeSum);
		resultView = (TextView) findViewById(R.id.resultView);
		centerText=(TextView) findViewById(R.id.center);
		systemSettings=(Button)findViewById(R.id.system_settings);

		presonalButton=(Button) findViewById(R.id.personalCenter);
		if (iProgress != null) {
			
			mstudyProgress = (ProgressBar) findViewById(R.id.studyProgressBar);
			new Thread(new Runnable() {
				public void run() {
					Message msg = new Message();
					msg.what = 1;
					progressHandler.handleMessage(msg);
				}
			}).start();
		}

		processJudge(iProgress);
		uploadReceiver = new UploadReceiver();
		onlineService =(Button)findViewById(R.id.onlineservice);
		//sendLog = (Button) findViewById(R.id.sendLog);
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        systemSettings=(Button) findViewById(R.id.system_settings);
		//sendSensor = (Button) findViewById(R.id.sendSensor);
		updateCourse = (Button) findViewById(R.id.updatecourses);
		// delete=(Button)findViewById(R.id.filedelete);
		update = (Button) findViewById(R.id.update);
		quitReg = (Button) findViewById(R.id.quitReg);
		scoreButton=(Button)findViewById(R.id.score);
		priceButton=(Button)findViewById(R.id.price);
		personButton=(Button)findViewById(R.id.personal);
		commonLinearLayout=(LinearLayout) findViewById(R.id.commonLinearLayout);
		personalLinearLayout=(LinearLayout) findViewById(R.id.personalLinearLayout);
		backLinearLayout=(LinearLayout) findViewById(R.id.back);
		versionname = (TextView) findViewById(R.id.versionname);
		String version = getVersion();
		versionname.setText("SkyClass移动端" + version);

		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度
		pd.setMessage("课程同步中，请稍后。。。");
		pd.setCancelable(false);
		pd.setOnKeyListener(onKeyListener);
		sendUserFile = new SendUserFile();
		courseService = new CourseService(this.getApplicationContext());
		dtstructureService = new DtstructureService(this.getApplicationContext());
		dtplaycourseService = new DtplaycourseService(this.getApplicationContext());
		dtcourseService = new DtcourseService(this.getApplicationContext());
		docservice = new DocService(this.getApplicationContext());
		courseList = courseService.find();
		userservice = new Userservice(this.getApplicationContext());

		if (courseList.size() > 0) {
			studentcode = courseList.get(0).getStudentCode();

		} else
			studentcode = "000";

        //系统设置 右上角菜单
		systemSettings.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				View contentView = LayoutInflater.from(Page4.this).inflate(R.layout.popupmenu, null);
				topRight = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
				topRight.setContentView(contentView);
				topRight.setFocusable(true);


				TextView TRsendLog =(TextView)contentView.findViewById(R.id.sendlog);
				TextView TRsendSensor =(TextView)contentView.findViewById(R.id.sendsensor);
				TRsendLog.setOnClickListener(this);
				TRsendSensor.setOnClickListener(this);

				topRight.showAsDropDown(systemSettings,0,50);

				TRsendLog.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (studentcode != "000") {
							usrs = userservice.find(studentcode);

							if (userservice.findExist(studentcode) && (usrs.size() > 0)) {
								if (sendUserFile.Send(usrs)) {

									Toast.makeText(Page4.this, "发送成功", Toast.LENGTH_SHORT).show();
									userservice.delete(studentcode);
								} else {
									Toast.makeText(Page4.this, "发送失败,请下次登录时再试...", Toast.LENGTH_SHORT).show();
								}
							} else
								Toast.makeText(Page4.this, "亲，您目前没有未发送的学习记录哦", Toast.LENGTH_SHORT).show();

						} else
							Toast.makeText(Page4.this, "亲，您没有可发送的学习记录", Toast.LENGTH_SHORT).show();

						topRight.dismiss();

					}
				});

				TRsendSensor.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
						if (wifi == android.net.NetworkInfo.State.CONNECTED
								|| wifi == android.net.NetworkInfo.State.CONNECTING) {
							Intent i = new Intent(Page4.this, uploadService.class);
							startService(i);
						} else
							Toast.makeText(Page4.this, "亲，请连接网络...", Toast.LENGTH_SHORT).show();

						topRight.dismiss();

					}
				});

			}
		});




	//增加个人中心的主要的功能
        //addListContent();

		//PersonalCenterAdapter personalCenterAdapter=new PersonalCenterAdapter(parentContent,childContent,this);
		//expandableListView.setAdapter(personalCenterAdapter);

        presonalButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!studentcode.equals("000")) {
					personalLinearLayout.setVisibility(View.VISIBLE);
					commonLinearLayout.setVisibility(View.GONE);
					backLinearLayout.setVisibility(View.VISIBLE);
					centerText.setText("个人中心");
				}else{
                    AlertDialog.Builder  builder=new AlertDialog.Builder(Page4.this);
					View view=LayoutInflater.from(Page4.this).inflate(R.layout.dialog_nopersonal,null);
					builder.setView(view);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					builder.show();
				}
			}
		});
		backLinearLayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				personalLinearLayout.setVisibility(View.GONE);
				commonLinearLayout.setVisibility(View.VISIBLE);
				backLinearLayout.setVisibility(View.GONE);
				centerText.setText("关于我们");
			}
		});
		scoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putString("catagory","score");
				bundle.putString("stucode",studentcode);
				Intent intent=new Intent(Page4.this,PersonalActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		priceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putString("catagory","price");
				bundle.putString("stucode",studentcode);
				Intent intent=new Intent(Page4.this,PersonalActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		personButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putString("catagory","person");
				bundle.putString("stucode",studentcode);
				Intent intent=new Intent(Page4.this,PersonalActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		onlineService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Page4.this,OnlineService.class);
				startActivity(intent);
			}
		});

		/*sendLog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (studentcode != "000") {
					usrs = userservice.find(studentcode);

					if (userservice.findExist(studentcode) && (usrs.size() > 0)) {
						if (sendUserFile.Send(usrs)) {
							Toast.makeText(Page4.this, "发送成功", Toast.LENGTH_SHORT).show();
							userservice.delete(studentcode);
						} else {
							Toast.makeText(Page4.this, "发送失败,请下次登录时再试...", Toast.LENGTH_SHORT).show();
						}
					} else
						Toast.makeText(Page4.this, "亲，您目前没有未发送的学习记录哦", Toast.LENGTH_SHORT).show();

				} else
					Toast.makeText(Page4.this, "亲，您没有可发送的学习记录", Toast.LENGTH_SHORT).show();

			}
		});

		sendSensor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
				if (wifi == android.net.NetworkInfo.State.CONNECTED
						|| wifi == android.net.NetworkInfo.State.CONNECTING) {
					Intent i = new Intent(Page4.this, uploadService.class);
					startService(i);
				} else
					Toast.makeText(Page4.this, "亲，请连接网络...", Toast.LENGTH_SHORT).show();

			}

		});*/
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 检测更新
				UpdateUtil.checkUpdateThread(Page4.this, updateHandler);
				System.out.println(UpdateConfig.version_name + " " + UpdateConfig.apk);
			}
		});

		updateCourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pd.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						try {
							Login.login1.SaveUserDate();
							if (Login.login1.getisSaved()) {
								Intent intent = new Intent(getParent(), TabHostActivity.class);
								System.out.println("intent" + Page4.this.getApplicationContext().toString());
								Bundle args = new Bundle();
								args.putSerializable("progress", iProgress);
								args.putInt("flag", flag);
								intent.putExtras(args);
								startActivity(intent);
								handler.sendEmptyMessage(0);
								Page4.this.finish();
							} else {

								handler.sendEmptyMessage(1);
							}
						}

						catch (Exception e) {

							e.printStackTrace();
						}

					}
				}).start();

			}
		});

		sp = getSharedPreferences("passwordFile", 0);

		quitReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showTips();
			}

		});

	}



	//判断当前的学习进度是否良好
	public void processJudge(studyProgress p) {
		if (p != null) {
			String sTip = "";
			int iTotalCreditHour = Integer.parseInt(p.getGradeTotal());
			int iSumCreditHour = Integer.parseInt(p.getGradeSum());
			int iGraduatelowlimDays = (int) Double.parseDouble(p.getGraduatelowlimDays());
			int iGraduatehighlimDays = (int) Double.parseDouble(p.getGraduatehighlimDays());
			int iStudyDays = (int) Double.parseDouble(p.getStudyDays());
			int iProgress = 1;
			int iStudySpeed = 1;
			int iTotalSpeed = 1;
			if (iTotalCreditHour >= iSumCreditHour) {
				iProgress = 100;
				sTip = "你已完成教学计划规定总学分！";
			} else {
				if (iGraduatehighlimDays < iStudyDays) {
					sTip = "你的学习已结束！";
					if (iTotalCreditHour > iSumCreditHour) {
						iProgress = 100;
					} else {
						if (iSumCreditHour != 0) {
							iProgress = iTotalCreditHour * 100 / iSumCreditHour;
						} else {

							iProgress = 1;
						}
					}
				} else if (iGraduatelowlimDays < iStudyDays) {
					sTip = "学习进度已落后，请加油！";
					if (iTotalCreditHour > iSumCreditHour) {
						iProgress = 100;
					} else {
						if (iSumCreditHour != 0) {
							iProgress = iTotalCreditHour * 100 / iSumCreditHour;
						} else {

							iProgress = 1;
						}
					}
				} else {
					if (iTotalCreditHour == 0) {
						sTip = "欢迎选课学习！";
					} else {
						if (iTotalCreditHour > iSumCreditHour) {
							iProgress = 100;
						} else {
							if (iSumCreditHour != 0) {
								iProgress = iTotalCreditHour * 100 / iSumCreditHour;
							} else {

								iProgress = 1;
							}
						}
						if (iGraduatelowlimDays != 0) {
							iStudySpeed = iTotalCreditHour * 1000 / iGraduatelowlimDays; // 标准速度
						} else {
							iStudySpeed = 0;
						}
						if (iGraduatelowlimDays != 0) {
							iTotalSpeed = iSumCreditHour * 1000 / iStudyDays; // 现有速度
						} else {
							iTotalSpeed = 1;

						}
						int iOverSpeed = (iStudySpeed - iTotalSpeed) * 100 / iTotalSpeed;

						if (iOverSpeed == 0) {
							sTip = "学习进度不错，继续保持！";
						} else if (iOverSpeed < 50) {
							sTip = "学习进度很好，继续保持！";
						} else if (iOverSpeed >= 50) {
							sTip = "学习进度非常好，继续保持！";
						} else if (iOverSpeed > -50 && iOverSpeed < 0) {
							sTip = "学习进度已落后，请加油！";
						} else if (iOverSpeed > -100 && iOverSpeed <= -50) {
							sTip = "学习进度落后很多，请抓紧时间学习！";
						} else if (iTotalSpeed == 0 || iStudySpeed == 0) {
							sTip = "欢迎选课学习！";
						}
					}
				}
			}

			Toast.makeText(Page4.this, sTip, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		processJudge(iProgress);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("nostart");
		intentFilter.addAction("nolog");
		intentFilter.addAction("finishupload");
		intentFilter.addAction("notfinish");
		registerReceiver(uploadReceiver, intentFilter);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void addListContent(){
		parentContent.add("个人中心");
		ArrayList<String> child=new ArrayList<String>();
		child.add("成绩信息");
		child.add("费用信息");
		child.add("个人信息");
		childContent.add(child);
	}
	public class UploadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("nolog") || intent.getAction().equals("nostart")) {
				Toast.makeText(context, "没有可上传的传感数据", Toast.LENGTH_SHORT).show();
			} else if (intent.getAction().equals("notfinish")) {
				Toast.makeText(context, "未上传完成，请下次再试。。。", Toast.LENGTH_SHORT).show();
			} else if (intent.getAction().equals("finishupload")) {
				Toast.makeText(context, "传感数据已上传", Toast.LENGTH_SHORT).show();
			}

		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
			switch (msg.what) {
			case 0:

				pd.dismiss();// 关闭ProgressDialog
				Toast.makeText(Page4.this, "已获取最新的课程列表", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				pd.dismiss();// 关闭ProgressDialog
				Toast.makeText(Page4.this, "同步失败，请稍后再试。。。", Toast.LENGTH_SHORT).show();
				break;
			default:
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

	private void showTips() {
		String qNM = sp.getString("nm", "");
		int count = sp.getInt("count", 0);
		String[] qnM = qNM.split("#");

		String username = qnM[count];

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		/*builder.setTitle("提示");
		builder.setMessage(username + ":  退出登录后您的操作将不再被记录，确定退出么？");*/
		View quit=LayoutInflater.from(getApplicationContext()).inflate(R.layout.quitapp,null);
		builder.setView(quit);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 监听SharePreference
				// sp.registerOnSharedPreferenceChangeListener(myListener); //
				// 注册

				sp.edit().putBoolean("isSaved", false).commit();
				// sp.unregisterOnSharedPreferenceChangeListener(myListener); //
				// 取消监听
				courseService.delete(studentcode);
				dtcourseService.deleteAll();
				docservice.deleteAll();
				dtstructureService.deleteAll();
				dtplaycourseService.deleteAll();
				studyprocessService.deleteAll();
				Toast.makeText(Page4.this, "退出登录成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplication(), Login.class);
				startActivity(intent);
				Page4.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create();
		builder.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			View quitView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.quit,null);
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

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.page4, menu); return true; }
	 */

	@SuppressLint("WrongViewCast")
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case UpdateConfig.DIALOG_NOUPDATE:
			Builder builder1 = new AlertDialog.Builder(Page4.this);
			View noUpdate=LayoutInflater.from(getApplicationContext()).inflate(R.layout.no_update,null);
			/*builder1.setTitle("版本更新提示");
			builder1.setMessage("亲，当前已是最新版本");
			builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});*/
			builder1.setView(noUpdate).setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			return builder1.create();
		case UpdateConfig.DIALOG_NONETWORK:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			/*builder2.setIcon(android.R.drawable.ic_dialog_alert);
			builder2.setTitle("无法连接网络");
			builder2.setMessage("检测到您当前网络处于未连接状态，是否设置网络？");
			builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 用户设置网络
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});
			builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();

				}
			});*/
			View noNetwork=LayoutInflater.from(getApplicationContext()).inflate(R.layout.no_network,null);
			builder2.setView(noNetwork).setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 用户设置网络
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});
			builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();

				}
			});
			return builder2.create();

		case UpdateConfig.DIALOG_DOWNLOADING:
			LayoutInflater inflater2 = LayoutInflater.from(getApplicationContext());
			View view2 = inflater2.inflate(R.layout.update_loading, null);
			pb = (ProgressBar) view2.findViewById(R.id.down_pb);
			downloading_kb = (TextSwitcher) view2.findViewById(R.id.downloading_kb);

			downloading_kb.setFactory(Page4.this);

			Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
			Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
			downloading_kb.setInAnimation(in);
			downloading_kb.setOutAnimation(out);

			downloading_percent = (TextView) view2.findViewById(R.id.downloading_percent);
			Builder builder = new Builder(Page4.this);
			builder.setView(view2);
			builder.setTitle("版本更新进度提示");
			builder.setNegativeButton("后台下载", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getApplicationContext(), UpdateService.class);
					startService(intent);
					dialog.dismiss();
				}
			});
			return builder.create();

		case UpdateConfig.DIALOG_UPDATE:
			LayoutInflater inflater = LayoutInflater.from(Page4.this);
			View view = inflater.inflate(R.layout.update_dialog, null);
			WebView webview = (WebView) view.findViewById(R.id.webView1);
			webview.loadUrl(UpdateConfig.info);

			AlertDialog.Builder alert = new Builder(Page4.this);
			alert.setCancelable(false);
			alert.setView(view);
			alert.setTitle("发现新版本:" + UpdateConfig.version_name);
			alert.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					showDialog(UpdateConfig.DIALOG_DOWNLOADING);
					new Thread() {
						public void run() {
							UpdateUtil.loadFile(Page4.this, UpdateConfig.apk, updateHandler);
						}
					}.start();
				}
			});

			if (UpdateConfig.force_update) {
				alert.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				});
			} else {
				alert.setNegativeButton("先不升级", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}
			return alert.create();

		case UpdateConfig.DIALOG_RUNNING:
			AlertDialog.Builder alert2 = new Builder(Page4.this);
			//alert2.setMessage("正在更新");
			//alert2.setTitle("提示");
			View dialogRunning=LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_running,null);
			alert2.setView(dialogRunning);
			alert2.setPositiveButton("停止", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getApplicationContext(), UpdateService.class);
					// stopService(intent);
					dialog.dismiss();
				}
			});
			return alert2.create();
		}

		return super.onCreateDialog(id);

	}

	@Override
	public View makeView() {
		TextView t = new TextView(this);
		t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		t.setTextSize(36);
		return t;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		pd.dismiss();
		unregisterReceiver(uploadReceiver);
		super.onDestroy();

	}

	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			System.out.println(info.versionName);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
