package com.android.svod;

import com.android.sensorecord.SensorLogger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
/**
 * record the sensor data
 * @author Administrator
 *
 */
public class SensorService extends Service{

	private final IBinder mBinder = new LocalBinder();

	SensorManager sm = null;
	SensorLogger sL = null;
	String studentcode,courseid,path,activity;


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
	    sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);	 
		super.onCreate();
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub	
		studentcode = intent.getStringExtra("studentcode");
		courseid = intent.getStringExtra("courseid");
		path = intent.getStringExtra("path");
		activity = intent.getStringExtra("activity");	
		sL = new SensorLogger(sm,studentcode,courseid,path,activity);
		sL.start();
		return mBinder;
	}
	
	 @Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		 sL.stop();
		return super.onUnbind(intent);
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	   /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SensorService getService() {
            // Return this instance of LocalService so clients can call public
            // methods        	
            return SensorService.this;
        }
    }

	
	

}
