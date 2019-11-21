package com.android.adapter.svod;

import java.util.ArrayList;

import org.giles.ui.widget.WebImageView;

import com.android.domain.Course;
import com.android.svod.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShelfAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Course> courseList;

	public ShelfAdapter(Context context, ArrayList<Course> courseList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.courseList = courseList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return courseList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return courseList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		v = LayoutInflater.from(context).inflate(R.layout.sliding_item, null);
		WebImageView cover = (WebImageView) v.findViewById(R.id.imageView);
		/* courseList.get(arg0).getCourseCode() */
		cover.setImageURL("https://kj.xjtudlc.com/YC/js015/cover.jpg");
		cover.setErrorImageDrawable(context.getResources().getDrawable(R.drawable.icon1));
		TextView view = (TextView) v.findViewById(R.id.textview);
		/*String courseString=courseList.get(arg0).getCourseName();
		if(courseString.length()>5){
			courseString=courseString.substring(0, 5)+"...";
		}*/
		view.setText(courseList.get(arg0).getCourseName());
		view.setTextSize(11);
		view.setTextColor(context.getResources().getColor(R.color.textcolor));
		return v;
	}
}
