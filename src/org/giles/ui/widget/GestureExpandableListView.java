package org.giles.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

public class GestureExpandableListView extends ExpandableListView{

	
	// 手势识别器
	private GestureDetector mGestureDetector;

	// 在构造函数中初始化手势识别器并注册手势监听
	public GestureExpandableListView(Context context, AttributeSet attrs) {
	super(context, attrs);
	mGestureDetector = new GestureDetector(new myGestureListener());
	setFadingEdgeLength(0);
	this.setLongClickable(true);
	}

	//复写此方法以让手势生效
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	return super.onInterceptTouchEvent(ev)
	&& mGestureDetector.onTouchEvent(ev);
	}


	// 创建手势监听器
	private class myGestureListener extends SimpleOnGestureListener {
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
	float distanceX, float distanceY) {
	if (Math.abs(distanceY) >= Math.abs(distanceX)&&Math.abs(e1.getY()-e2.getY()) >= Math.abs(e1.getX()-e2.getX())) {
	// 手势纵向移动距离大，返回true,代表ListView获取到焦点。
	return true;
	} else {
	// 反之，ListView失去焦点
	return false;
	}
	}
	}

}
