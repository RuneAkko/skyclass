package com.android.svod;

import com.umeng.analytics.MobclickAgent;

import com.android.svod.CourseChapterListActivity;
import com.android.svod.Page1;
import com.android.svod.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 
 * @ClassName: OBCourseCategoryActivity
 * @Description: TODO
 * @author zhangchunzhe
 * @date 2013-2-5
 * ????????????????
 * 
 */
// public class OBLCourseCategoryActivity extends OBLServiceMainActivity
// implements OnTabClickListener {
public class CourseMenuActivity extends ActivityGroup {
	private static final String TAG = "CourseMenuActivity";

	public static final int REQUST_RESULT_CODE = 10001;
	private RadioGroup radioGroup;
	private RadioButton videoRadioButton;
	private RadioButton docRadioButton;
	private Button backButton;
	private TextView courseTextView;
	TabHost tabHost = null;
	private TabHost.TabSpec mVideoTab = null;
	private TabHost.TabSpec mDocTab = null;
	public ActionBar mActionBar;


	private int temp;
	private String studentcode;
	private String courseName;
	private String courseid;
	private int flag;

	private boolean netChangeState = false;
	private boolean isExistNet = true;

	public boolean isNetChangeState() {
		return netChangeState;
	}

	public void setNetChangeState(boolean netChangeState) {
		this.netChangeState = netChangeState;
	}

	public boolean isExistNet() {
		return isExistNet;
	}

	public void setExistNet(boolean isExistNet) {
		this.isExistNet = isExistNet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coursemenu);


		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		flag = bundle.getInt("flag");
		studentcode = bundle.getString("studentcode");
		temp = bundle.getInt("temp");
		courseName = bundle.getString("courseName");
		courseid = bundle.getString("courseid");

		tabHost = (TabHost) findViewById(R.id.tab_host);
		backButton = (Button) findViewById(R.id.backButton);
		courseTextView = (TextView) findViewById(R.id.courseTextView);
		radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
		videoRadioButton=(RadioButton) findViewById(R.id.videoRadioButton);
		docRadioButton=(RadioButton) findViewById(R.id.docRadioButton);

		courseTextView.setText(courseName);

		backButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tabHost.setup();
		tabHost.setup(this.getLocalActivityManager());

		mVideoTab = tabHost.newTabSpec("???");
		mDocTab = tabHost.newTabSpec("???????");

		mVideoTab.setIndicator("???", getResources().getDrawable(R.drawable.icon_2));
		mDocTab.setIndicator("???????", getResources().getDrawable(R.drawable.icon_4));

		Intent intentVideo = new Intent(this, CourseChapterListActivity.class);
		Bundle bundleVideo=new Bundle();
		bundleVideo.putInt("temp", temp);
		bundleVideo.putString("studentcode", studentcode);
		bundleVideo.putString("courseName", courseName);
		bundleVideo.putString("courseid", courseid);
		intentVideo.putExtras(bundleVideo);
		mVideoTab.setContent(intentVideo);

		Intent intentDoc = new Intent(this, CourseDocActivity.class);
		Bundle bundle1 = new Bundle();
		bundle1.putInt("flag", flag);
		bundle1.putString("courseId", courseid);
		bundle1.putInt("position", temp);
		intentDoc.putExtras(bundle1);
		mDocTab.setContent(intentDoc);
		tabHost.addTab(mVideoTab);
		tabHost.addTab(mDocTab);
		tabHost.setCurrentTab(0);
	
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.videoRadioButton:
					videoRadioButton.setTextColor(getResources().getColor(R.color.title));
					docRadioButton.setTextColor(getResources().getColor(R.color.title));
					tabHost.setCurrentTab(0);
					break;
				case R.id.docRadioButton:
					videoRadioButton.setTextColor(getResources().getColor(R.color.title));
					docRadioButton.setTextColor(getResources().getColor(R.color.title));
					tabHost.setCurrentTab(1);
					break;
				}	
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// mNoticeStartIndex = 1;
		// mNoticeHandle.initNoticeData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// Log.d(TAG, "The OBLCourseCategoryActivity is pause");
		super.onPause();
		MobclickAgent.onPause(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}


}
