package com.android.adapter.intelligentroom;

import java.util.ArrayList;

import com.android.svod.R;
import com.android.domain.intelligentroom.*;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
    private ArrayList<GridViewBean> sourceList;
    private Context context;
	
    public  GridViewAdapter(ArrayList<GridViewBean> sourceList,Context context) {
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
		View v=View.inflate(context, R.layout.girdviewsub, null);	
		TextView textView=(TextView) v.findViewById(R.id.gridViewText);
		TextView textViewClassRoom=(TextView) v.findViewById(R.id.gridViewClassRoom);
		TextView textViewTeacherName=(TextView) v.findViewById(R.id.gridViewTeacherName);
		textViewClassRoom.setText(sourceList.get(position).getClassRoom());
		textViewTeacherName.setText(sourceList.get(position).getTeacherName());
		textView.setText(sourceList.get(position).getCourseName());
		return v;
	}

}
