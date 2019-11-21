package com.android.fragment;

import java.util.ArrayList;

import com.android.domain.Course;
import com.android.publiccourse.OBMainApp;
import com.android.svod.CourseChapterListActivity;
import com.android.svod.CourseDocActivity;
import com.android.svod.Page1;
import com.android.svod.R;

import android.app.ActionBar;
import android.app.ActivityGroup;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

public class Fragment2 extends Fragment {
	public static final int REQUST_RESULT_CODE = 10001;
	private TabHost tabHost;
	private RadioGroup radioGroup;
	private RadioButton videoRadioButton;
	private RadioButton docRadioButton;
	private Button backButton;
	private TextView courseTextView;
	private TabHost.TabSpec mVideoTab;
	private TabHost.TabSpec mDocTab;

	private int position;
	private int flag;
	private ArrayList<Course> courseList;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.page_fragment2, container, false);
		tabHost = (TabHost) v.findViewById(R.id.tab_host);
		//backButton = (Button) v.findViewById(R.id.backButton);
		//courseTextView = (TextView) v.findViewById(R.id.courseTextView);
		//radioGroup=(RadioGroup) v.findViewById(R.id.radioGroup);
		//videoRadioButton=(RadioButton) v.findViewById(R.id.videoRadioButton);
		//docRadioButton=(RadioButton) v.findViewById(R.id.docRadioButton);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		flag = ((Page1) getActivity()).getFlag();
		/*
        backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().getFragmentManager().beginTransaction().hide(((Page1)getActivity()).getFragment2()).commit();
				getActivity().getFragmentManager().beginTransaction().show(((Page1)getActivity()).getFragment1()).commit();
			}
		});*/
		tabHost.setup();
		tabHost.setup(((Page1)getActivity()).getLocalActivityManager());
		mVideoTab = tabHost.newTabSpec("��Ƶ");
		mDocTab = tabHost.newTabSpec("��ѧ����");

		mVideoTab.setIndicator("��Ƶ", getResources().getDrawable(R.drawable.icon_2));
		mDocTab.setIndicator("��ѧ����", getResources().getDrawable(R.drawable.icon_4));

		Intent intentVideo = new Intent(getActivity(), CourseChapterListActivity.class);
		mVideoTab.setContent(intentVideo);

		Intent intentDoc = new Intent(getActivity(), CourseDocActivity.class);
		Bundle bundle1 = new Bundle();
		bundle1.putInt("flag", flag);
		intentDoc.putExtras(bundle1);
		mDocTab.setContent(intentDoc);
		tabHost.addTab(mVideoTab);
		tabHost.addTab(mDocTab);
		tabHost.setCurrentTab(0);
		/*
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.videoRadioButton:
					videoRadioButton.setTextColor(getActivity().getResources().getColor(R.color.tabgreen));
					docRadioButton.setTextColor(getActivity().getResources().getColor(R.color.buttoncolor));
					tabHost.setCurrentTab(0);
					break;
				case R.id.docRadioButton:
					videoRadioButton.setTextColor(getActivity().getResources().getColor(R.color.buttoncolor));
					docRadioButton.setTextColor(getActivity().getResources().getColor(R.color.tabgreen));
					tabHost.setCurrentTab(1);
					break;
				}	
			}
		});*/
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);	
		courseTextView.setText(OBMainApp.getInstance().getCourseName());
		if(tabHost.getCurrentTab()==0){
			tabHost.setCurrentTab(1);
		    tabHost.setCurrentTab(0);
		}
		else{
			tabHost.setCurrentTab(0);
		}
	}
}
