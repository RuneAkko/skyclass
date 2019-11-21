package com.android.publiccourse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;

public class PhoneWindowUtil {
	final static String TAG ="PhoneWindowUtil";
	
	public static Window makeNewWindow(Context context) {
		LayoutInflater mlayoutInflater=null;
		Window window=null;
		try {
			  Class clazz = Class.forName("com.android.internal.policy.PolicyManager");
			  Method makeNewWindowMethod = clazz.getDeclaredMethod("makeNewWindow",Context.class);
			  makeNewWindowMethod.setAccessible(true);
			  window = (Window) makeNewWindowMethod.invoke(window, context);
			 /* Class windowClass = Class.forName("com.android.internal.policy.PhoneWindow");
			  Constructor<?> localConstructor = windowClass.getConstructor(new Class[]{Context.class});
			  windows = (Window) localConstructor.newInstance(new Object[]{context});//实例化Window，如果传的context不是Application的Context,就会奔溃
			  /*Field field = windowClass.getDeclaredField("mLayoutInflater");
			  field.setAccessible(true);
			  mlayoutInflater = (LayoutInflater) field.get(windows);//取得Application的LayoutInflater，而不是插件的LayoutInflater

              //往windows设置插件的LayoutInflater
			  Class policyClass = Class.forName("com.android.internal.policy.PhoneLayoutInflater");
			  Constructor<?> policyLocalConstructor = policyClass.getConstructor(new Class[]{Context.class});
			  mlayoutInflater = (LayoutInflater) policyLocalConstructor.newInstance(new Object[]{context});
			  field.set(windows,mlayoutInflater);*/
		}catch (Exception e) {
			e.printStackTrace();
		}
    	return window;
    }


}
