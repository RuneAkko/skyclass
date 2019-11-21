package com.android.domain;

import android.app.Application;
import android.content.Context;

public class TextApplication extends Application {
	private static Context context; 

	public void onCreate() {
		super.onCreate();
		TextApplication.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return TextApplication.context;
	}

}
