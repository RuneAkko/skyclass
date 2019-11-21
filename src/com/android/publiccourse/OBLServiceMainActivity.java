package com.android.publiccourse;



import com.android.publiccourse.BindDataService.LocalBinder;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public abstract class OBLServiceMainActivity extends OBLBaseActivity {

    private static final String TAG = "ServiceMainActivity";
    public boolean mBound = false; // 是否已绑定服�?  
    public BindDataService mService; // 服务
    protected boolean connectionInited = false;
    private boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.d(TAG, "the service is onCreate");

    }

    /**
     * 在start阶段绑定服务
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Log.d(TAG, "The service is onStart");
        createServiceConnction();
    }

    // 建立Service连接
    private void createServiceConnction() {
   
        // Bind to MeosDataService
        // Log.d(TAG, "The service is send intent to bind");
        Intent intent = new Intent(this, BindDataService.class);
        this.getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 避免动�?加载页面的问�?       
        if (isLoading) {
            if (mService != null) {
                if (connectionInited) {

                 //  initLoadData();
                }
            }
        }

    }

    /**
     * 设置是否页面显示时重新刷�?     * 
     * @param isLoading
     */
    public void setReLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * 在stop阶段，松绑service
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, BindDataService.class));
        if(mBound){
        this.getApplicationContext().unbindService(mConnection);
        mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get
            // MeosDataService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            doInit();
            // Log.i(TAG, "service connected mBound is true");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	mService = null;
            mBound = false;
        }
    };

    private void doInit() {
        if (!connectionInited) {
            initLoadData();
            connectionInited = true;
        }
    }

    /**
     * 首次加载Activity时，异步加载数据，请在此方法中调用mService
     */
    protected void initLoadData() {
        // mService.api(params)--> sendBroadCast --> handleMsg -->
        // receiveResponse()
    }

    public boolean isConnectionInited() {
        return connectionInited;
    }

    public void setConnectionInited(boolean connectionInited) {
        this.connectionInited = connectionInited;
    }
    
}
