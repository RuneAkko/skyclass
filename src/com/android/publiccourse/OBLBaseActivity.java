package com.android.publiccourse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.android.svod.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: MainActivity
 * @Description:
 * @author zhangchunzhe
 * @date 2013-1-14 下午2:34:12
 * 
 */

public abstract class OBLBaseActivity extends FragmentActivity {

    public static final String TAG = "MainActivity";
    private static final int MENU_REFRESH_ITEM = 1;
    private Activity currentAct;
    protected String className = null; // 放在IntentFilter中使用，用来过滤广播
    protected boolean isCheckLogin = false; // 是否�?��登陆状�?,有些功能在未登录时时不能使用的，因此�?��马上finish
    protected boolean isIndex = false;
    protected boolean isRetToIndex = false;
    protected OBSharedPreferences mPreferences;
    private OBSoftReference mSoftRefrences;
    protected String mobileIMEI;
    protected String sdkIP;
    public String title;
    public String args[];
    private String totalAction;
    HashMap<String, String> userActionMap = new HashMap<String, String>();

   
    private ViewGroup mMainView;
    private FrameLayout mContentLayout;
     
    protected boolean isPlayVideo = false; // 是否为播放器
    protected boolean isCountIntegral = true;// 课程累计时间计算积分的标志，true为不计算activity,false为计算的activity
    private Intent updateIntent;

    public static volatile boolean isMqttClosed = true;

    // 是否创建默认刷新菜单
    private boolean isCreateRefreshMenu;

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!isPlayVideo)
            getWindow().setBackgroundDrawableResource(R.drawable.img_main_bg);
        if (mobileIMEI == null) {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            mobileIMEI = tm.getDeviceId();
        }
        sdkIP = this.getString(R.string.learningbar_sdk_http_url).substring(7);//拿到域名 basic.open.com.cn
//        Log.i(TAG, sdkIP);
        mSoftRefrences = ((OBMainApp) getApplication()).getmObSoftRefrences();       
        mPreferences = OBSharedPreferences.getInstance(this);     
    }

  
  
    /**
     * 在子类调用次方法设置�?��带标题栏的Activity
     * 
     * @param layoutResID
     */
    public void setTitleBarContentView(int layoutResID) {
        mMainView = (ViewGroup) (getLayoutInflater().inflate(R.layout.main_layout, null));
       
        View contentView = getLayoutInflater().inflate(layoutResID, null);
        mContentLayout = (FrameLayout) mMainView.findViewById(R.id.contentLayout);
        mContentLayout.addView(contentView);
        this.setContentView(mMainView);
    }

    /**
     * 在子类调用次方法设置�?��带标题栏的Activity
     * 
     * @param layoutResID
     */
    public void setTitleBarContentView(View view) {
        mMainView = (ViewGroup) (getLayoutInflater().inflate(R.layout.main_layout, null));
       
        mContentLayout = (FrameLayout) mMainView.findViewById(R.id.contentLayout);
        mContentLayout.addView(view);
        setContentView(mMainView);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }



    /**
     * 显示网络加载进度�?     * 
     * @param context
     * @param str
     */
    public void showLoadingProgress(Context context, String str) {
        UIUtils.getInstance().showNetLoadDialog(context, str);
    }

    /**
     * 显示网络加载进度�?     * 
     * @param context
     * @param str
     */
    public void showPubLoadingProgress(Context context, int str) {
        UIUtils.getInstance().showPubLoadDialog(context, context.getString(str));
    }

    /**
     * 显示网络加载进度�?     * 
     * @param context
     * @param str
     */
    public void showLoadingProgress(Context context, int str) {
        UIUtils.getInstance().showNetLoadDialog(context, context.getString(str));
    }

    /**
     * 取消加载进度条显�?     */

    public void cancelPubLoadDialog() {
        UIUtils.getInstance().cancelPubLoadDialog();
    }

    /**
     * 取消加载进度条显�?     */

    public void cancelLoadingProgress() {
        UIUtils.getInstance().cancelNetLoadDialog();
    }

    /**
     * 添加Bitmap到弱引用
     * 
     * @param key
     * @param bitmap
     */
    public void addBitmapSoftReference(String key, Bitmap bitmap) {
        mSoftRefrences.addBitmapSoftReference(key, bitmap);
    }

    /**
     * 获取弱引用中的bitmap
     * 
     * @param key
     * @return
     */

    public Bitmap getBitmapSoftReference(String key) {
        return mSoftRefrences.getBitmapSoftReference(key);
    }

    /**
     * 获取弱引用中的bitmap
     * 
     * @param key
     * @return
     */

    public void clearBitmapSoftReference(String key) {
        mSoftRefrences.recycleSoftReferenceBitmap(key);
    }


    @Override
    protected void onStart() {
       
        super.onStart();
      
    }
    /**
     * activity开始执行时，注册广播接收器
     */
    @Override
    protected void onResume() {
        super.onResume();
        setClassName(this);
        // MobclickAgent.onResume(this);
  
        // MobclickAgent.onResume(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(className);
        intentFilter.addAction(Constants.Intent_Action.OB_VIERSION_UPDATE);
        intentFilter.addAction(Constants.Intent_Action.CLOSE_ACTION);
        intentFilter.addAction(Constants.HANDLER.DOWNLOAD_BROADCAST_SUCCESS);
        // intentFilter.addAction(Constants.Intent_Action.USER_LOGIN_AGAIN);
        this.registerReceiver(msgReceiver, intentFilter);
 
        this.registerReceiver(exitReceiver, new IntentFilter(Constants.Intent_Action.USER_LOGIN_AGAIN));
      
        if (className == null) {
            throw new RuntimeException("must set className at onCreate method");
        }
        setStatus();

       
    }

    @Override
    protected void onPause() {

        super.onPause();
        // MobclickAgent.onPause(this);
        if(msgReceiver !=null)
       	 this.unregisterReceiver(msgReceiver);
    
    }

    @Override
    protected void onDestroy() {
        if (exitReceiver != null) {

            this.unregisterReceiver(exitReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
         
        super.onStop();
       

    }


    /**
     * 设置ClassName 所有继承的子类必须调用此方法
     * 
     * @param className
     */
    private void setClassName(Activity acty) {
        this.className = acty.getClass().getName();
        this.currentAct = acty;
        ((OBMainApp) getApplication()).addActivity(acty);
    }

    /**
     * 设置状�?：比如导航条的状�?�?��在子类中实现
     */
    public void setStatus() {

    }

    private BroadcastReceiver exitReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionStr = intent.getAction();
            Log.d(TAG, "action is: " + actionStr);
            if (actionStr.equals(Constants.Intent_Action.USER_LOGIN_AGAIN)) {
                /*
                 * NotificationManager nm = (NotificationManager)
                 * OBLBaseActivity
                 * .this.getSystemService(Context.NOTIFICATION_SERVICE);
                 * nm.cancelAll(); exitDownloadDeal(); mChatClient.stopSelf();
                 * if (isBindMsgService) {
                 * OBLBaseActivity.this.unbindService(connection);
                 * isBindMsgService = false; }
                 * OBSharedPreferences.getInstance(OBLBaseActivity
                 * .this).clearSharedPreferences(); int node =
                 * intent.getExtras().getInt("node"); startLoginActivity(node);
                 */

                // changeUserLogin(OBUtil.getString(currentAct.getBaseContext(),
                // R.string.alertdialog_switch_user));
                return;
            }

        }

    };

    /**
     * 广播接收器：编程方式注册
     */
    protected BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String actionStr = intent.getAction();
            Log.d(TAG, "action is: " + actionStr);

            // 如果收到的广播是CLOSE_ACTION，则摘除接收器，finish activity
            if (actionStr.endsWith(Constants.Intent_Action.CLOSE_ACTION)) {
                unregisterReceiver(this);
                finish(); // 结束Activity
                return;
            } else if (actionStr.equals(Constants.Intent_Action.OB_VIERSION_UPDATE)) {
                // 如果是版本更新，则获取安裝路徑
            	String installPath = intent.getStringExtra("installPath");
                installApk(installPath);
                return;
            } else if (actionStr.equals(Constants.HANDLER.DOWNLOAD_BROADCAST_SUCCESS)) {
                addUserDownloadSocre(intent);
                return;
            }


            handleMsg(intent);
        }
    };


    private DialogInterface.OnClickListener mNetFailedListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
                startActivity(i);

            }
            if (which == DialogInterface.BUTTON_NEUTRAL) {
                Intent i = new Intent(OBLBaseActivity.this, OBLNetworkAbortDetails.class);
                startActivity(i);
            }
            dialog.dismiss();
        }
    };

    /**
     * 取出公共元素，转接到receiveResponse方法
     *
     * @param intent
     */
    private void handleMsg(Intent intent) {
        if (intent == null)
            return;

        String typeValue = intent.getStringExtra(Constants.DEFAULT_INTENT_EXTRA_TYPE);
        String action = intent.getStringExtra(Constants.DEFAULT_INTENT_EXTRA_ACTION);
        BusinessResponse response = (BusinessResponse) intent.getSerializableExtra(Constants.DEFAULT_INTENT_EXTRA_RESPONSE);
        TaskType taskType = TaskType.valueOf(typeValue);

        if (taskType == TaskType.Meos_Ex) {



            cancelLoadingProgress();


            if (response.getCode().equals(String.valueOf(Constants.NET_CONNECT_ERROR_TAG))) {
                UIUtils.getInstance().showNetworkExceptionDialog(this, OBUtil.getString(this, R.string.ob_string_network_tips), mNetFailedListener);
            } else {
//                 Log.d(TAG, "The other message is show");

                Log.d(TAG, "handleMsg is called,  " +  response.getMessage());
                UIUtils.getInstance().showToast(this, R.string.ob_string_network_request_failed);
            }

            return;
        } else {

            if (response.getCode().equals("800")) {

                return;
            } else {
                if (taskType == TaskType.Set_Courseware_Study_Status) {

                    return;
                } else if (taskType == TaskType.User_Login || taskType == TaskType.Get_Obs_Login) {
                    // 仅仅为不执行handlerRequestFailed，去子类中执行子类的方法
                    receiveResponse(intent, taskType, action, response);
                    return;
                } else {
                    // 临时注释
                    boolean isSuccess = response.getStatus();
                    if (!isSuccess) {
                        cancelLoadingProgress();// TODO
                        handlerRequestFailed(taskType, response);
                        return;
                    }

                }
            }
        }

        cancelLoadingProgress();
        receiveResponse(intent, taskType, action, response);

    }



    /**
     *
     * @param taskType
     * @param response
     */

    protected void handlerRequestFailed(TaskType taskType, BusinessResponse response) {




        if (taskType == TaskType.User_Regist && response.getMessage() != null) {
            UIUtils.getInstance().showToast(this, response.getMessage());
        } else {

            Log.d(
                    TAG,
                    taskType.toString() + " handle request failed called  "+response.toString()
            );
            UIUtils.getInstance().showToast(this, R.string.ob_string_network_request_failed);


        }

        return;

    }


    /**
     * 根据接收到的数据，更新UI数据 更新UI时，应该在Activity中覆写此方法
     *
     * @param intent
     * @param taskType
     * @param action
     * @param response
     */
    protected void receiveResponse(Intent intent, TaskType taskType, String action, BusinessResponse response) {
        // 在子类中实现更新UI逻辑
    };

    /**
     * 安装应用程序�?     *
     * @param path
     */
    public void installApk(String path) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        startActivity(i);
    }

    /**
     * 保存当前activity 是否�?��验证登陆的状�?     */
    @Override
    public void onSaveInstanceState(Bundle stateB) {
        super.onSaveInstanceState(stateB);
        stateB.putBoolean("isCheckLogin", isCheckLogin);
    }

    /**
     * 恢复isCheckLogin状�?
     */
    @Override
    public void onRestoreInstanceState(Bundle stateB) {
        super.onRestoreInstanceState(stateB);
        if (stateB != null) {
            this.isCheckLogin = stateB.getBoolean("isCheckLogin");
        }
    }


    /**
     * ActionMenu点击版本更新
     */
    protected void checkVersionUpdate() {
        // 子类重写
    }

    /**
     * 添加积分
     */
    public void addUserDownloadSocre(Intent i) {
        // TODO:子类重写
    }






    public void onHomeClicked() {

    }



}