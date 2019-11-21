package com.android.svod.intelligentroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.android.svod.R;
import com.android.adapter.intelligentroom.GridViewAdapter;
import com.android.adapter.intelligentroom.ListViewAdapter;
import com.android.publiccourse.OBMainApp;
import com.android.domain.intelligentroom.Course;
import com.android.domain.intelligentroom.GridViewBean;
import com.android.domain.intelligentroom.ListViewBean;
import com.android.domain.intelligentroom.Task;
import com.android.service.intelligentroom.MainService;
import com.android.sql.CourseServiceIntelligentRoom;
import com.android.util.intelligentroom.DisplayUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;

import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends ActivityGroup {
	private Uri uri;
	private ImageView logoImageView;
	private static TabHost tabHost;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	//private Button jumpButton;
	private static LinearLayout progressLinearLayout;
	private static LinearLayout tipLinearLayout;
	private static GridView gridView;
	private static TextView onlineTextView;
	private static TextView noonlineTextView;
	private static TextView numberTextView;
	private static LinearLayout progressLinearLayout_no;
	private static LinearLayout mainLinearLayout_no;
	private static LinearLayout tipLinearLayout_no;
	private static ListView listView_no;
	private static GridViewAdapter gridviewAdapter;
	private CourseServiceIntelligentRoom courseService = new CourseServiceIntelligentRoom(this);
	private static ArrayList<Course> courseList = new ArrayList<Course>();
	private static ArrayList<Course> nocourseList = new ArrayList<Course>();
	private static ArrayList<ListViewBean> list1 = new ArrayList<ListViewBean>();
    private String college="所有课程";
	private static ArrayList<GridViewBean> list = new ArrayList<GridViewBean>();
	private static ListViewAdapter listviewAdapter;
	private static OBMainApp myApplication = OBMainApp.getInstance();
	private static Resources resources;
	private static Context context;
	private Intent service;
	private static LinearLayout linearLayout;
	private static LinearLayout linearLayoutNo;
	private Spinner spinner;
	private static ImageView refreshImageView;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			refreshImageView.clearAnimation();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_ACTION_BAR);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_activity);
		myApplication.addActivity(new MainActivity());
		service = new Intent(this, MainService.class);
		startService(service);
		resources = this.getApplication().getResources();
		context = this.getApplicationContext();
		studentNumberTimer();
		initActionbar();
		init();
		onlineTextView = (TextView) this.findViewById(R.id.onlinecoursenumber);
		noonlineTextView = (TextView) this.findViewById(R.id.noonlinecoursenumber);
		numberTextView = (TextView) this.findViewById(R.id.onlinestudentnumber);
		if(!checkNetworkState()){	
			Task task = new Task();
			task.setTaskId(Task.TASK_REQUESTONLINECOURSE);
			MainService.newTask(task);
			Task task1 = new Task();
			task1.setTaskId(Task.TASK_REQUESTNOONLINECOURSE);
			MainService.newTask(task1);		
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopService(service);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	//��⵱ǰ������״̬�Ƿ����
	public boolean checkNetworkState(){
		ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		State mobile=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if(!(wifi==State.CONNECTED)&&!(mobile==State.CONNECTED)){
			AlertDialog.Builder builder=new AlertDialog.Builder(this); 
			builder.setMessage("�뿪������");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (android.os.Build.VERSION.SDK_INT > 10)
					{
					    // 3.0���ϴ����ý���
					    startActivity(new Intent(
					            android.provider.Settings.ACTION_SETTINGS));
					}
					else
					{
					    startActivity(new Intent(
					            android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					}
					
				}
			});
			builder.show();
			return true;
		}
		return false;
	}
	
	public void initActionbar() {
		
		spinner=(Spinner) this.findViewById(R.id.spinner);
		refreshImageView=(ImageView) this.findViewById(R.id.refresh);
		// �����µ�SpinnerAdapter
		final String[] spinnerList = this.getResources().getStringArray(R.array.list_item);
		// final Typeface typeface=Typeface.createFromAsset(getAssets(),
		// "fonts/miaomiao.ttf");

		@SuppressWarnings("unchecked")
		ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.spinnerlist, spinnerList) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				TextView text;
				if (convertView == null)
					view = View.inflate(MainActivity.this, R.layout.spinnerlist, null);
				else
					view = convertView;
				try {

					text = (TextView) view;

				} catch (ClassCastException e) {
					Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
					throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
				}
				text.setTextSize(18);
				text.setPadding(27, 0, 27, 0);
				text.setText(spinnerList[position]);
				return view;
			}
		};
		spinnerAdapter.setDropDownViewResource(R.layout.spinnerlist_dropdown);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				college=spinnerList[position];
				fresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		refreshImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("ִ��ˢ������");
				Task task = new Task();
				task.setTaskId(Task.TASK_REFRESH);
				HashMap<String,Object> params=new HashMap<String,Object>();
				params.put("college", college);
				task.setParams(params);
				MainService.newTask(task);
				showRefreshAnimation(refreshImageView);
				
			}
		});
	}

	public void init() {
		tabHost = (TabHost) this.findViewById(R.id.tabHost);
		tabHost.setup(this.getLocalActivityManager());
		TabSpec tabSpec_online = tabHost.newTabSpec("����ֱ��");
		TabSpec tabSpec_noonline = tabHost.newTabSpec("׼��ֱ��");

		/*
		 * Intent intent_online=new Intent(this,OnlineActivity.class);
		 * tabSpec_online=tabSpec_online.setIndicator("����ֱ��").setContent(
		 * intent_online);
		 */

		linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout);
		View view = View.inflate(this, R.layout.activity_online, null);
		initOnlineView(view);
		linearLayout.addView(view);
		tabSpec_online = tabSpec_online.setIndicator("����ֱ��").setContent(R.id.linearLayout);

		/*
		 * Intent intent_noonline=new Intent(this,NoOnlineActivity.class);
		 * tabSpec_noonline=tabSpec_noonline.setIndicator("׼��ֱ��").setContent(
		 * intent_noonline);
		 */

		linearLayoutNo = (LinearLayout) this.findViewById(R.id.linearLayout_no);
		View view1 = View.inflate(this, R.layout.activity_noonine, null);
		initNoOnlineView(view1);
		linearLayoutNo.addView(view1);
		tabSpec_noonline = tabSpec_noonline.setIndicator("׼��ֱ��").setContent(R.id.linearLayout_no);
		tabHost.addTab(tabSpec_online);
		tabHost.addTab(tabSpec_noonline);
		tabHost.setCurrentTabByTag("����ֱ��");
		RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.radioGruop);
		radioButton1 = (RadioButton) this.findViewById(R.id.radioButton1);
		radioButton2 = (RadioButton) this.findViewById(R.id.radioButton2);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				System.out.println("���ڸı䡣��������");
				switch (checkedId) {
				case R.id.radioButton1:
					tabHost.setCurrentTabByTag("����ֱ��");
					radioButton1.setTextColor(getResources().getColor(R.color.red));
					radioButton2.setTextColor(getResources().getColor(R.color.gray));
					if(college.equals("���пγ�"))
					    courseList = courseService.queryAllCourse();
					else
						courseList = courseService.queryCourse(college);
					if (courseList.size() == 0) {
						linearLayout.setGravity(Gravity.CENTER);
						progressLinearLayout.setVisibility(View.GONE);
						tipLinearLayout.setVisibility(View.VISIBLE);
						gridView.setVisibility(View.GONE);
					} else {
						progressLinearLayout.setVisibility(View.GONE);
						// mainLinearLayout.setVisibility(View.VISIBLE);
						gridView.setVisibility(View.VISIBLE);
						tipLinearLayout.setVisibility(View.GONE);
						linearLayout.setGravity(Gravity.TOP);
						list.clear();
						// SimpleAdapter simpleAdapter=new SimpleAdapter(this,);
						for (int i = 0; i < courseList.size(); i++) {
							GridViewBean gridViewBean = new GridViewBean();
							gridViewBean.setClassRoom(String.valueOf(courseList.get(i).getClassName()));
							gridViewBean.setTeacherName(courseList.get(i).getTeacherName());
							gridViewBean.setCourseName(courseList.get(i).getCourseName());
							list.add(gridViewBean);
						}
						if (gridviewAdapter == null) {
							gridviewAdapter = new GridViewAdapter(list, MainActivity.this);
							gridView.setAdapter(gridviewAdapter);
						} else
							gridviewAdapter.notifyDataSetChanged();
					}
					break;
				case R.id.radioButton2:
					tabHost.setCurrentTabByTag("׼��ֱ��");
					radioButton1.setTextColor(getResources().getColor(R.color.gray));
					radioButton2.setTextColor(getResources().getColor(R.color.red));
					if(college.equals("���пγ�"))
						nocourseList = courseService.queryAllNoOnlineCourse();
					else
						nocourseList = courseService.queryNoOnlineCourse(college);
					
					if (nocourseList.size() == 0) {
						linearLayoutNo.setGravity(Gravity.CENTER);
						progressLinearLayout_no.setVisibility(View.GONE);
						mainLinearLayout_no.setVisibility(View.GONE);
						tipLinearLayout_no.setVisibility(View.VISIBLE);
					} else {
						linearLayoutNo.setGravity(Gravity.TOP);
						progressLinearLayout_no.setVisibility(View.GONE);
						tipLinearLayout_no.setVisibility(View.GONE);
						mainLinearLayout_no.setVisibility(View.VISIBLE);
						list1.clear();
						// SimpleAdapter simpleAdapter=new SimpleAdapter(this,);
						for (int i = 0; i <nocourseList.size(); i++) {
							ListViewBean listViewBean = new ListViewBean();
							listViewBean.setCourseName(nocourseList.get(i).getCourseName());
							listViewBean.setCollege(nocourseList.get(i).getCollegeName());
							listViewBean.setClassName(nocourseList.get(i).getClassName());
							listViewBean.setStartTime(nocourseList.get(i).getStartTime());
							listViewBean.setTeacherName(nocourseList.get(i).getTeacherName());
							list1.add(listViewBean);
						}
						if (listviewAdapter == null) {
							listviewAdapter = new ListViewAdapter(list1, MainActivity.this);
							listView_no.setAdapter(listviewAdapter);
						} else
							listviewAdapter.notifyDataSetChanged();
					}
					break;
				default:
					break;
				}
			}
		});
		radioButton1.setChecked(true);
		radioButton2.setChecked(false);

	}

	public void initOnlineView(View v) {
		progressLinearLayout = (LinearLayout) v.findViewById(R.id.progress);
		// mainLinearLayout=(LinearLayout) v.findViewById(R.id.main);
		tipLinearLayout = (LinearLayout) v.findViewById(R.id.tip);
		gridView = (GridView) v.findViewById(R.id.onlineGirdView);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
				final String string = courseList.get(position).getCourseName();
				final String videoUrl=courseList.get(position).getVideoUrl();
				final String screenUrl=courseList.get(position).getScreenUrl();
				final String server=courseList.get(position).getServer();
				final String location=courseList.get(position).getLocation();
				View v = View.inflate(MainActivity.this, R.layout.dialog_course, null);			
				Button classButton = (Button) v.findViewById(R.id.classButton);
				Button screenButton = (Button) v.findViewById(R.id.screenButton);
				LinearLayout dialogTextView=(LinearLayout) v.findViewById(R.id.dialogTextView);
				LinearLayout dialogImageView=(LinearLayout) v.findViewById(R.id.dialogImageView);
				dialog.setContentView(v);
				Window dialogWindow=dialog.getWindow();
				WindowManager.LayoutParams layoutParams=dialogWindow.getAttributes();
				layoutParams.width=DisplayUtil.dip2px(MainActivity.this, 250.0f);
				layoutParams.height=DisplayUtil.dip2px(MainActivity.this, 140.0f);
				dialogWindow.setGravity(Gravity.CENTER);
				dialogWindow.setAttributes(layoutParams);
				dialog.show();		
				classButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
						//intent.putExtra("mpath", "http://202.117.16.20/Classroom/11111111.mp4");
						String path="http://" + server
								+ ":8134/hls-live/" + location
								+ "/_definst_/" + videoUrl+ "/"
								+ videoUrl + ".m3u8";
						System.out.println("path------>"+path);
						intent.setDataAndType(uri, "video/mp4");
						intent.putExtra("mpath",path);
						intent.putExtra("title", string);
						intent.putExtra("isLive", true);
						intent.putExtra("isOnlinePlay", true);
						startActivity(intent);
					}
				});
				screenButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// ������Ƶ�Ĳ���
						Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
						//intent.putExtra("mpath", "http://202.117.16.20/Classroom/11111111.mp4");
						String path="http://" + server
								+ ":8134/hls-live/" + location
								+ "/_definst_/" + screenUrl+ "/"
								+ screenUrl + ".m3u8";
						System.out.println("path------>"+path);
						intent.setDataAndType(uri, "video/mp4");
						intent.putExtra("mpath",path);
						intent.putExtra("title", string);
						intent.putExtra("isLive", true);
						intent.putExtra("isOnlinePlay", true);
						startActivity(intent);
					}
				});
			}

		});
	}

	public void initNoOnlineView(View v) {
		progressLinearLayout_no = (LinearLayout) v.findViewById(R.id.progress_no);
		mainLinearLayout_no = (LinearLayout) v.findViewById(R.id.main_no);
		tipLinearLayout_no = (LinearLayout) v.findViewById(R.id.tip_no);
		listView_no = (ListView) v.findViewById(R.id.onlineGirdView_no);
	}

	public void studentNumberTimer() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Task task = new Task();
				task.setTaskId(Task.TASK_NUMBER);
				MainService.newTask(task);
			}

		};
		timer.schedule(timerTask, 0, 300000);
	}

	public static void refresh(Object... objects) {
		System.out.println("ִ�е���");
		HashMap<String, Object> map = (HashMap<String, Object>) objects[0];
		switch ((Integer) (map.get("taskId"))) {
		case Task.TASK_REQUESTONLINECOURSE:
			onlineTextView.setText(String.valueOf(map.get("Number")));

			// onlineTextView.setText("1");
			break;
		case Task.TASK_REQUESTNOONLINECOURSE:
			noonlineTextView.setText(String.valueOf(map.get("Number")));
			break;
		case Task.TASK_NUMBER:
			numberTextView.setText(String.valueOf(map.get("Number")));
			break;
		case Task.TASK_REFRESH:
			onlineTextView.setText(String.valueOf(map.get("onLine")));
			noonlineTextView.setText(String.valueOf(map.get("noOnline")));
			numberTextView.setText(String.valueOf(map.get("number")));
			courseList = (ArrayList<Course>) map.get("course");
			nocourseList = (ArrayList<Course>) map.get("courseNo");
			fresh();
			hideRefreshAnimation(refreshImageView);
			break;
		default:
			break;
		}
	}

	public static void fresh() {
		if (tabHost.getCurrentTabTag() == "����ֱ��") {
			System.out.println("courseList---->" + courseList.size());
			if (courseList.size() == 0) {
				linearLayout.setGravity(Gravity.CENTER);
				progressLinearLayout.setVisibility(View.GONE);
				tipLinearLayout.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.GONE);
			} else {
				linearLayout.setGravity(Gravity.TOP);
				progressLinearLayout.setVisibility(View.GONE);
				tipLinearLayout.setVisibility(View.GONE);
				gridView.setVisibility(View.VISIBLE);
				list.clear();
				for (int i = 0; i<courseList.size(); i++) {
					GridViewBean gridViewBean = new GridViewBean();
					gridViewBean.setClassRoom(String.valueOf(courseList.get(i).getClassName()));
					gridViewBean.setTeacherName(courseList.get(i).getTeacherName());
					gridViewBean.setCourseName(courseList.get(i).getCourseName());
					list.add(gridViewBean);
				}
				if (gridviewAdapter == null) {
					gridviewAdapter = new GridViewAdapter(list, context);
					gridView.setAdapter(gridviewAdapter);
				} else
					gridviewAdapter.notifyDataSetChanged();

			}
		} else {
			if (nocourseList.size() == 0) {
				progressLinearLayout_no.setVisibility(View.GONE);
				mainLinearLayout_no.setVisibility(View.GONE);
				tipLinearLayout_no.setVisibility(View.VISIBLE);
				linearLayoutNo.setGravity(Gravity.CENTER);
			} else {
				linearLayoutNo.setGravity(Gravity.TOP);
				progressLinearLayout_no.setVisibility(View.GONE);
				tipLinearLayout_no.setVisibility(View.GONE);
				mainLinearLayout_no.setVisibility(View.VISIBLE);
				// SimpleAdapter simpleAdapter=new SimpleAdapter(this,);
				list1.clear();
				for (int i = 0; i <nocourseList.size(); i++) {
					ListViewBean listViewBean = new ListViewBean();
					listViewBean.setCourseName(nocourseList.get(i).getCourseName());
					listViewBean.setCollege(nocourseList.get(i).getCollegeName());
					listViewBean.setClassName(nocourseList.get(i).getClassName());
					listViewBean.setStartTime(nocourseList.get(i).getStartTime());
					listViewBean.setTeacherName(nocourseList.get(i).getTeacherName());
					list1.add(listViewBean);
				}
				if (listviewAdapter == null) {
					listviewAdapter = new ListViewAdapter(list1, context);
					listView_no.setAdapter(listviewAdapter);
				} else
					listviewAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	public void showRefreshAnimation(ImageView imageView){
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.refreshanimation);
		animation.reset();
		imageView.startAnimation(animation);
	}
	public static void hideRefreshAnimation(ImageView imageView){
		imageView.clearAnimation();
			
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK || keyCode==KeyEvent.KEYCODE_HOME){
			AlertDialog.Builder dialog=new AlertDialog.Builder(this);
			dialog.setMessage("ȷ��Ҫ�˳���¼��");
			dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 dialog.dismiss();
				}
			
			});
			dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 dialog.dismiss();
					 Intent intent = new Intent(Intent.ACTION_MAIN);  
					 intent.addCategory(Intent.CATEGORY_HOME);
					 startActivity(intent);
					 OBMainApp.getInstance().exit();
				}
			});
			dialog.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
