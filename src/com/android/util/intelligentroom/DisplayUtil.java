package com.android.util.intelligentroom;

import android.content.Context;

public class DisplayUtil {
    public static int dip2px(Context context,float dpValue){
    	float scale=context.getResources().getDisplayMetrics().density;
    	return (int)(dpValue*scale+0.5);
    }
    public static int px2dip(Context context,float pxValue){
    	float scale=context.getResources().getDisplayMetrics().density;
    	return (int)(pxValue/scale+0.5);
    }
}
