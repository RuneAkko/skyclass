package com.android.svod.intelligentroom;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {


	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
			
		}
	};
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction("EXIT");
		this.registerReceiver(this.broadcastReceiver, filter);
	}
}