package com.android.svod;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.giles.ui.widget.GestureExpandableListView;
import org.giles.ui.widget.WebImageView;

import android.widget.ViewSwitcher;
import android.widget.ImageButton;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.util.DisplayMetrics;

import com.android.network.DownloadProgressListener;
import com.android.network.FileDownloader;
import com.android.publiccourse.OBMainApp;
import com.android.sql.CourseService;
import com.android.sql.DocService;
import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.DtstructureService;
import com.android.sql.FileService;
import com.android.sql.FileService2;
import com.android.sql.Userservice;
import com.android.sql.studyProcessService;
import com.android.adapter.intelligentroom.GridViewAdapter;
import com.android.adapter.svod.ShelfAdapter;
import com.android.connection.SendUserFile;
import com.android.domain.ClassBean;
import com.android.domain.Course;
import com.android.domain.Dtcourse;
import com.android.domain.Dtplaycourse;
import com.android.domain.Dtstructure;
import com.android.domain.User;
import com.android.domain.intelligentroom.GridViewBean;
import com.android.fragment.Fragment1;
import com.android.fragment.Fragment2;
import com.android.svod.Page1;
import com.android.svod.R;
import com.android.updater.UpdateConfig;
import com.android.updater.UpdateService;
import com.android.updater.UpdateUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhai.utils.VersionUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Page1 extends ActivityGroup implements ViewSwitcher.ViewFactory{



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
				case UpdateConfig.DIALOG_DOWNLOADING:
					showDialog(UpdateConfig.DIALOG_NOUPDATE);
					break;
				case UpdateConfig.DIALOG_RUNNING:
					showDialog(UpdateConfig.DIALOG_NONETWORK);
					break;
				case 1:
					pb.setProgress(msg.arg1);
					UpdateConfig.loading_process = msg.arg1;

					if (UpdateConfig.MB < 1024) {
						UpdateConfig.total = String.format("%.2f",
								UpdateConfig.MB) + "B";
					} else if (UpdateConfig.MB < 1024 * 1024
							&& UpdateConfig.MB > 1024) {
						UpdateConfig.total = String.format("%.2f",
								UpdateConfig.MB / 1024) + "K";
					} else {
						UpdateConfig.total = String.format("%.2f",
								UpdateConfig.MB / 1024 / 1024) + "M";
					}

					if (UpdateConfig.KB < 1024) {
						UpdateConfig.now = String.format("%.2f",
								UpdateConfig.KB) + "B";
					} else if (UpdateConfig.KB < 1024 * 1024
							&& UpdateConfig.KB > 1024) {
						UpdateConfig.now = String.format("%.2f",
								UpdateConfig.KB / 1024) + "K";
					} else {
						UpdateConfig.now = String.format("%.2f",
								UpdateConfig.KB / 1024 / 1024) + "M";
					}

					String text = UpdateConfig.now + "/" + UpdateConfig.total;

					downloading_kb.setText(text);
					downloading_percent.setText(UpdateConfig.loading_process
							+ "%");
					
					break;
				case 2:
					finish();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), getResources()
							.getString(R.string.appname) + ".apk")),
							"application/vnd.android.package-archive");
					startActivity(intent);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(Page1.this, error, Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
				}
			}
		}

	};
	
	private ProgressBar pb;
	private TextSwitcher downloading_kb;
	private TextView downloading_percent;
    private GridView shelf;
	int flag;//学历or非学历
	
	List<User> usrs;
	private Toast toast = null;
	String Tag = "System.out";
    private FragmentManager fragmentManager=getFragmentManager();
    private Fragment1 fragment1=new Fragment1();
    private Fragment2 fragment2=new Fragment2();
	// 发送日志用到的
	SharedPreferences sp;
	private CourseService courseService; 
	private Userservice userservice;
	private SendUserFile sendUserFile;
	private String studentcode;
	String result = "";
	private ArrayList<Course> courseList;
	private Button onlineService,MBBSbutton;
	private boolean isclick;
	
	String se = "";
   
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.page1);
		setContentView(R.layout.page1);
		/*fragmentManager.beginTransaction().add(R.id.fragmentLinearLayout, fragment1).commit();
		fragmentManager.beginTransaction().add(R.id.fragmentLinearLayout, fragment2).commit();
		fragmentManager.beginTransaction().show(fragment1).commit();
		fragmentManager.beginTransaction().hide(fragment2).commit();*/
		//CrashReport.testJavaCrash();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		flag = bundle.getInt("flag");
		courseService= new CourseService(this.getApplicationContext());
		userservice=new Userservice(this.getApplicationContext());
		courseList = courseService.find();

		onlineService =(Button)findViewById(R.id.online_service);//在线客服拖拽按钮
		onlineServiceButtonInit();

		MBBSbutton=(Button)findViewById(R.id.MBBS);
		MBBSbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Page1.this,MBBS.class);
				startActivity(intent);
			}
		});

		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 自动检测有网的话 就发送学习日志
		sp = getSharedPreferences("passwordFile", 0);
		sendUserFile = new SendUserFile();
		if (courseList.size() > 0) {
			studentcode = courseList.get(0).getStudentCode();
			usrs = userservice.find(studentcode);
			//找到当前用户的名字
			String qNM = sp.getString("nm", "");
			int count = sp.getInt("count", 0);
			String[] qnM = qNM.split("#");
			String username = qnM[count];
			// (mobile == State.CONNECTED||mobile==State.CONNECTING)||(
			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				if (userservice.findExist(studentcode) && (usrs.size() > 0)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setIcon(android.R.drawable.ic_dialog_alert);
					builder.setTitle("提示");
					builder.setMessage(username + ":  检测到您当前处于网络连接状态，是否发送您的学习记录？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (sendUserFile.Send(usrs)) {
										Toast.makeText(Page1.this, "发送成功",
												Toast.LENGTH_SHORT).show();

										userservice.delete(studentcode);
									} else {
										Toast.makeText(Page1.this,
												"发送失败,请下次登录时再试...",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									Toast.makeText(Page1.this,
											"将在您下次登录时提醒您...",
											Toast.LENGTH_SHORT).show();
								}
							});
					builder.create();
					builder.show();

				}
			}
			
			SharedPreferences prefs = getPreferences(0);
			long lastUpdateTime = prefs.getLong("lastUpdateTime",
					System.currentTimeMillis());
			
		}

		else {
			Toast.makeText(Page1.this, "亲，您还没有选课信息", Toast.LENGTH_SHORT).show();
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				UpdateUtil.checkUpdateThread(getApplicationContext(),
						updateHandler);

			}
		}).start();
		shelf=(GridView)findViewById(R.id.shelf);
		ShelfAdapter shelfAdapter=new ShelfAdapter(this, courseList);
		shelf.setAdapter(shelfAdapter);
    	shelf.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			    Intent intent=new Intent(Page1.this,CourseMenuActivity.class);
			    Bundle bundle=new Bundle();
			    bundle.putInt("flag",flag);
			    bundle.putString("studentcode", studentcode);
			    bundle.putInt("temp", arg2);
			    bundle.putString("courseName", courseList.get(arg2).getCourseName());
			    bundle.putString("courseid", courseList.get(arg2).getCourseId());
			    intent.putExtras(bundle);
			    startActivity(intent);
				
			}
		});

	}
	private void showTextToast(String msg) {
		if (toast == null) {
			toast = Toast.makeText(Page1.this, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
	public ArrayList<Course> getCourseList(){
		return courseList;
	}
	
	public int getFlag(){
		return flag;
	}
	public Fragment1 getFragment1(){
		return fragment1;
	}
	public Fragment2 getFragment2(){
		return fragment2;
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case UpdateConfig.DIALOG_NOUPDATE:
			return null;
		case UpdateConfig.DIALOG_NONETWORK:
			return null;
		case UpdateConfig.DIALOG_DOWNLOADING:
			LayoutInflater inflater2 = LayoutInflater
					.from(getApplicationContext());
			View view2 = inflater2.inflate(R.layout.update_loading, null);
			pb = (ProgressBar) view2.findViewById(R.id.down_pb);
			downloading_kb = (TextSwitcher) view2
					.findViewById(R.id.downloading_kb);

			downloading_kb.setFactory(Page1.this);

			Animation in = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in);
			Animation out = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out);
			downloading_kb.setInAnimation(in);
			downloading_kb.setOutAnimation(out);

			downloading_percent = (TextView) view2
					.findViewById(R.id.downloading_percent);
			Builder builder = new Builder(Page1.this);
			builder.setView(view2);
			builder.setTitle("版本更新进度提示");
			builder.setNegativeButton("后台下载",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(getApplicationContext(),
									UpdateService.class);
							startService(intent);
							dialog.dismiss();
						}
					});
			return builder.create();
		case UpdateConfig.DIALOG_UPDATE:
			LayoutInflater inflater = LayoutInflater.from(Page1.this);
			View view = inflater.inflate(R.layout.update_dialog, null);
			WebView webview = (WebView) view.findViewById(R.id.webView1);
			webview.loadUrl(UpdateConfig.info);

			AlertDialog.Builder alert = new Builder(Page1.this);
			alert.setCancelable(false);
			alert.setView(view);
			alert.setTitle("发现新版本:" + UpdateConfig.version_name);
			alert.setPositiveButton("立即升级",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							showDialog(UpdateConfig.DIALOG_DOWNLOADING);
							new Thread() {
								public void run() {
									UpdateUtil.loadFile(Page1.this,
											UpdateConfig.apk, updateHandler);
								}
							}.start();
							
						}
					});

			if (UpdateConfig.force_update) {
				alert.setNegativeButton("退出",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}
						});
			} else {
				alert.setNegativeButton("先不升级",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			}
			return alert.create();

		case UpdateConfig.DIALOG_RUNNING:
			AlertDialog.Builder alert2 = new Builder(Page1.this);
			alert2.setMessage("正在更新");
			alert2.setTitle("提示");
			alert2.setPositiveButton("停止",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(getApplicationContext(),
									UpdateService.class);
							// stopService(intent);
							dialog.dismiss();
						}
					});
			return alert2.create();
		}
		return super.onCreateDialog(id);

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
	@Override
	public View makeView() {
		TextView t = new TextView(this);
		t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		t.setTextSize(36);
		return t;
	}


	public void onlineServiceButtonInit(){

		DisplayMetrics dm = getResources().getDisplayMetrics();
		final int screenWidth = dm.widthPixels;
		final int screenHeight = dm.heightPixels;

		// Toast.makeText(getActivity(), screenWidth + "==" + screenHeight + "="
		// + vHeight, 0).show();

		// 拖动的按钮
		onlineService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Page1.this,OnlineService.class);
				startActivity(intent);
			}
		});

		onlineService.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY,moveDownX;
			int btnHeight;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 获取Action
				int ea = event.getAction();
				long startTime = 0;
				long endTime = 0;
				switch (ea) {
					case MotionEvent.ACTION_DOWN: // 按下
						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();

						isclick= false;

						btnHeight = onlineService.getHeight();
						// Toast.makeText(getActivity(), "ACTION_DOWN：" + lastX + ",
						// " + lastY, 0).show();
						break;
					case MotionEvent.ACTION_MOVE: // 移动
						// 移动中动态设置位置
						int dx = (int) event.getRawX() - lastX;
						int dy = (int) event.getRawY() - lastY;
						int left = v.getLeft() + dx;
						int top = v.getTop() + dy;
						int right = v.getRight() + dx;
						int bottom = v.getBottom() + dy;
						if (left < 0) {
							left = 0;
							right = left + v.getWidth();
						}
						if (right > screenWidth) {
							right = screenWidth;
							left = right - v.getWidth();
						}
						if (top < 0) {
							top = 0;
							bottom = top + v.getHeight();
						}
						if (bottom > screenHeight) {
							bottom = screenHeight;
							top = bottom - v.getHeight();
						}
						v.layout(left, top, right, bottom);
						// Toast.makeText(getActivity(), "position：" + left + ", " +
						// top + ", " + right + ", " + bottom, 0)
						// .show();
						// 将当前的位置再次设置
						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP: // 抬起
						endTime = System.currentTimeMillis();
						//当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
						if ((endTime - startTime) > 0.5 * 1000) {
							isclick = false;
						} else {
							isclick = true;
						};
						// 向四周吸附
//                  int dx1 = (int) event.getRawX() - lastX;
//                  int dy1 = (int) event.getRawY() - lastY;
//                  int left1 = v.getLeft() + dx1;
//                  int top1 = v.getTop() + dy1;
//                  int right1 = v.getRight() + dx1;
//                  int bottom1 = v.getBottom() + dy1;
//                  if (left1 < (screenWidth / 2)) {
//                      if (top1 < 100) {
//                          v.layout(left1, 0, right1, btnHeight);
//                      } else if (bottom1 > (screenHeight - 200)) {
//                          v.layout(left1, (screenHeight - btnHeight), right1, screenHeight);
//                      } else {
//                          v.layout(0, top1, btnHeight, bottom1);
//                      }
//                  } else {
//                      if (top1 < 100) {
//                          v.layout(left1, 0, right1, btnHeight);
//                      } else if (bottom1 > (screenHeight - 200)) {
//                          v.layout(left1, (screenHeight - btnHeight), right1, screenHeight);
//                      } else {
//                          v.layout((screenWidth - btnHeight), top1, screenWidth, bottom1);
//                      }
//                  }
                    break;
				}
				return isclick;
			}
		});

	}

}
