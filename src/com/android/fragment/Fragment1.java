package com.android.fragment;

import java.util.ArrayList;

import com.android.adapter.svod.ShelfAdapter;
import com.android.domain.Course;
import com.android.publiccourse.OBMainApp;
import com.android.svod.CourseMenuActivity;
import com.android.svod.Page1;
import com.android.svod.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment1 extends Fragment {
	private GridView shelf;
	private ArrayList<Course> courseList;
	private int position=-1;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View v=inflater.inflate(R.layout.page_fragment1, container, false);
    	shelf=(GridView) v.findViewById(R.id.shelf);
    	courseList=((Page1) getActivity()). getCourseList();
    	shelf.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				position=arg2;
			    OBMainApp.getInstance().setPosition(position);
			    OBMainApp.getInstance().setStudentcode(courseList.get(position).getStudentCode());
			    OBMainApp.getInstance().setCourseName(courseList.get(position).getCourseName());
			    OBMainApp.getInstance().setCourseId(courseList.get(position).getCourseId());
				getActivity().getFragmentManager().beginTransaction().hide(((Page1)getActivity()).getFragment1()).commit();
				getActivity().getFragmentManager().beginTransaction().show(((Page1)getActivity()).getFragment2()).commit();
				
			}
		});

    	return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		ShelfAdapter shelfAdapter=new ShelfAdapter(getActivity(), courseList);
		shelf.setAdapter(shelfAdapter);
	}
}
