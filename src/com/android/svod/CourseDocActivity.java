package com.android.svod;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.domain.courseDoc;
import com.android.publiccourse.OBMainApp;
import com.android.sql.DocService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;
import com.android.adapter.svod.Dadapter;

public class CourseDocActivity extends Activity {

	String CourseId;
	private int position;
	private int flag_xueli;

	DocService docservice;
	private ArrayList<courseDoc> CourseDocList = new ArrayList<courseDoc>();
	private ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String, Object>>();
	private ListView DoclistView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coursedoc);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		flag_xueli = bundle.getInt("flag");
		docservice = new DocService(this.getApplicationContext());
		CourseId = bundle.getString("courseId");
		position = bundle.getInt("position");

		System.out.println("CourseId is"+CourseId);
		System.out.println("CourseId is"+position);
		while(CourseId==null)
			System.out.printf("111111111");
		CourseDocList = docservice.findC(CourseId);
		if (CourseDocList.size() == 0) {
			Toast.makeText(CourseDocActivity.this, "课程资源正在建设中", Toast.LENGTH_SHORT).show();

		} else {
			datalist = getData(CourseDocList);
			Dadapter dadapter = new Dadapter(this, datalist, flag_xueli);
			DirectoryHelper fileHelper = new DirectoryHelper();
			fileHelper.getSdCardPath();
			DoclistView = (ListView) this.findViewById(R.id.courseDocument);
			DoclistView.setAdapter(dadapter);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private ArrayList<HashMap<String, Object>> getData(ArrayList<courseDoc> CourseDocList) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i != CourseDocList.size(); ++i) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mainHead", CourseDocList.get(i).getMainHead());
			map.put("articleId", CourseDocList.get(i).getArticleID());
			result.add(map);
		}
		return result;
	}

}
