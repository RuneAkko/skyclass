package com.android.sensorecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class UploadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("upload");
	}

}
