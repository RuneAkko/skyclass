package com.android.adapter.svod;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.svod.R;

import java.util.ArrayList;

/**
 * Created by ycm on 2017/5/3.
 */
public class PersonalCenterAdapter  extends BaseExpandableListAdapter{
    private ArrayList<String> parentContent;
    private ArrayList<ArrayList<String>> childContent;
    private Context context;
    public PersonalCenterAdapter(ArrayList<String> parentContent,ArrayList<ArrayList<String>> childContent,Context context){
        this.parentContent=parentContent;
        this.childContent=childContent;
        this.context=context;
    }

    @Override
    public int getGroupCount() {
        return parentContent.size() ;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childContent.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentContent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childContent.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String string=parentContent.get(groupPosition);
        AbsListView.LayoutParams layoutParams=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView=new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0,32,0,32);
        textView.setTextSize(16);
        textView.setText(string);
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       String string=childContent.get(groupPosition).get(childPosition);
       AbsListView.LayoutParams layoutParams=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
       TextView textView=new TextView(context);
       textView.setLayoutParams(layoutParams);
       textView.setGravity(Gravity.CENTER);
       textView.setTextSize(16);
       textView.setPadding(0,32,0,32);
       textView.setText(string);
       return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
