package com.android.adapter.intelligentroom;

import java.util.ArrayList;

import com.android.svod.R;
import com.android.domain.intelligentroom.GridViewBean;
import com.android.domain.intelligentroom.ListViewBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewBean> sourceList;
    private Context context;
	
    public  ListViewAdapter(ArrayList<ListViewBean> sourceList,Context context) {
		// TODO Auto-generated constructor stub
    	this.sourceList=sourceList;
    	this.context=context;
	}
    @Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sourceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sourceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v=View.inflate(context, R.layout.listviewsub, null);
		TextView courseNameTextView=(TextView) v.findViewById(R.id.courseName);
		TextView classNameTextView=(TextView) v.findViewById(R.id.className);
		TextView collegeNameTextView=(TextView) v.findViewById(R.id.college);
		TextView teacherNameTextView=(TextView) v.findViewById(R.id.teacher);
		TextView startTimeTextView=(TextView) v.findViewById(R.id.startTime);
		courseNameTextView.setText(sourceList.get(position).getCourseName());
		classNameTextView.setText(String.valueOf(sourceList.get(position).getClassName()));
		collegeNameTextView.setText(sourceList.get(position).getCollege());
		teacherNameTextView.setText(sourceList.get(position).getTeacherName());
		startTimeTextView.setText(sourceList.get(position).getStartTime());
		
		return v;
	}

}
