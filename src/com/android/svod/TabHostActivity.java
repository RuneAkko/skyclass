package com.android.svod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.android.domain.studyProgress;
import com.android.domain.intelligentroom.Task;
import com.android.service.intelligentroom.MainService;
import com.android.svod.Constant.ConValue;
import com.android.svod.intelligentroom.MainActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

import org.giles.ui.widget.*;
public class TabHostActivity extends TabActivity {

	private  TabHost	m_tabHost;
	private LayoutInflater mLayoutInflater;
	private GestureDetector gestureDetector;
	int count=0;
	int flag;

	private int currentTabID = 0;
	@SuppressWarnings("deprecation")
	LocalActivityManager manager = null;
	private Intent tabItemIntent;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_tab_host);		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		flag = bundle.getInt("flag");
		init();
	}

	@SuppressWarnings("deprecation")
	private void init()
	{
		m_tabHost = (TabHost) findViewById(android.R.id.tabhost);
		mLayoutInflater = LayoutInflater.from(this);

		
		count = ConValue.mTabClassArray.length;		
		for(int i = 0; i < count; i++)
		{
			Intent iTent=getTabItemIntent(i);
			Bundle args2 = new Bundle();
			args2.putInt("flag", flag);
			iTent.putExtras(args2);
			TabSpec tabSpec = m_tabHost.newTabSpec(ConValue.mTextviewArray[i]).setIndicator(getTabItemView(i)).setContent(iTent);
			m_tabHost.addTab(tabSpec);
			//m_tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);	
			m_tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
			m_tabHost.getTabWidget().setDividerDrawable(null);
		}
	
	}
	
	private View getTabItemView(int index)
	{
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

		if (imageView != null)
		{
			imageView.setImageResource(ConValue.mImageViewArray[index]);
		}
		
		TextView textView = (TextView) view.findViewById(R.id.textview);
		
		textView.setText(ConValue.mTextviewArray[index]);
		textView.setTextSize(13);
	
		return view;
	}
	
	private Intent getTabItemIntent(int index)
	{
		Intent intent = new Intent(this, ConValue.mTabClassArray[index]);
		return intent;
	}
	
}

