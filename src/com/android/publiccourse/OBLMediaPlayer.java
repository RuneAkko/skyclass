package com.android.publiccourse;

import com.android.svod.R;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


public class OBLMediaPlayer extends OBLServiceMainActivity {

    // ------------------------变量
    private static final String TAG = "OBLMediaPlayer";
    private String playUrl;
    private String playName;
    private OBSimpleVideoView trialVideoView;
    private OBVideoController trialCon;
    // 学生创建的标签截图状态View
    private int screenWidth;
    private int screenHeight;
    private Dialog mLoadingDia;
    private int isNet;
    private int videoId;
    private int fromWhere;
    private String courseId;
    private String studentCode;
    private String mUserId;
    private int reStart;
    private OBDataUtils mDb;

    // 下载相关
    private boolean netChangeState = false;// 判定当前处于�?��网络
    private boolean isExistNet = true;// 是否存在网络
    private int videoSize;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setClassName(this);
        this.setContentView(R.layout.video_simple_play_media);
        isCountIntegral = false;
        isPlayVideo = true;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Log.d(TAG, "The trialVideoView is start init");
        trialVideoView = (OBSimpleVideoView) this.findViewById(R.id.trial_simple_videoview);
        // Log.d(TAG, "The trialVideoView is start end");
        getScreenSize();
        trialVideoView.measuredW = screenWidth;
        trialVideoView.measuredH = screenHeight;
        trialCon = new OBVideoController(getApplicationContext(),true, screenWidth, screenHeight);
        playName = this.getIntent().getExtras().getString("videoname");
        isNet = this.getIntent().getExtras().getInt("isnet");
        videoId = this.getIntent().getExtras().getInt("videoId");
        courseId = this.getIntent().getExtras().getString("courseId");
        studentCode = this.getIntent().getExtras().getString("studentCode");
        playUrl = this.getIntent().getExtras().getString("videopath");
        videoSize = this.getIntent().getExtras().getInt("videoSize");
        fromWhere = this.getIntent().getExtras().getInt("fromWhere");

       
        if (trialCon != null) {            
            trialCon.setsVideoName(playName);
            trialCon.setsVideoId(videoId);
            trialCon.setsVideoUrl(playUrl);
            trialCon.setVideoSize(videoSize);
        }

        trialVideoView.setMediaController(trialCon);
        mLoadingDia = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mLoadingDia.setContentView(R.layout.video_loading_dialog);
        mLoadingDia.setCancelable(true);
        mLoadingDia.show();

        trialVideoView.setVideoURI(Uri.parse(playUrl));
        trialVideoView.start();
        mDb = OBDataUtils.getInstance(this);
        

        if (fromWhere == 1) {
            args = new String[] { "10100", String.valueOf(videoId) };
            title = Constants.LEARNBAR_HOME_PV;

        } else {

            args = new String[] { "10200", courseId, "1", String.valueOf(videoId) };
            title = Constants.LEARNBAR_COURSE_PV;
        }
      
        title = Constants.LEARNBAR_VIDEO_PV;
    }

    public OBDataUtils getmDb() {
        return mDb;
    }

    public void setmDb(OBDataUtils mDb) {
        this.mDb = mDb;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getIsNet() {
        return isNet;
    }

    public void setIsNet(int isNet) {
        this.isNet = isNet;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (netReceiver != null) {
            this.unregisterReceiver(netReceiver);
        }

       

      
    }

    /**
     * 广播接收器：编程方式注册
     */
    protected BroadcastReceiver finReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String actionStr = intent.getAction();
            // Log.d(TAG, "actionStr = " + actionStr);

            // 如果收到的广播是�?��，则摘除接收器，finish
            if (actionStr.equalsIgnoreCase("open.video.finish")) {

                finish(); // 结束Activity

                return;
            }

            if (actionStr.equalsIgnoreCase("open.video.dialog")) {

                if (mLoadingDia != null && mLoadingDia.isShowing()) // 结束dialog
                {
                    mLoadingDia.dismiss();
                    // Log.d("pdfstep", " broadcast");
                }

                if (!trialVideoView.isPlaying()) {
                    trialCon.doPause();
                    trialCon.show();
                }
                return;
            }
            if (actionStr.equalsIgnoreCase(Intent.ACTION_SCREEN_ON)) {
                Log.d(TAG, "The current screen is on");
                if (trialVideoView != null) {
                    if (trialCon != null)
                        trialCon.show();
                }
                return;
            }
            if (actionStr.equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)) {
                Log.d(TAG, "The current screen is off");
                return;
            }

        }
    };

    @Override
    protected void onResume() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.HANDLER.DOWNLOAD_BROADCAST_START);
        intentFilter.addAction(Constants.HANDLER.DOWNLOAD_BROADCAST_ERROR);
        intentFilter.addAction(Constants.HANDLER.DOWNLOAD_BROADCAST_SUCCESS);
        this.registerReceiver(netReceiver, intentFilter);

        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction("open.video.dialog");
        ifilter.addAction("open.video.finish");
        ifilter.addAction(Intent.ACTION_SCREEN_ON);
        ifilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(finReceiver, ifilter);
      

        super.onResume();
        // Log.d(TAG, "The OBLMediaPlayer  is start resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "The OBLMediaPlayer  is start Stop");
        unregisterReceiver(finReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();     
        // if (this.getResources().getConfiguration().orientation ==
        // Configuration.ORIENTATION_LANDSCAPE) {
        // Log.d(TAG, "The Destroy current screen is ==landscape");
        // } else if (this.getResources().getConfiguration().orientation ==
        // Configuration.ORIENTATION_PORTRAIT) {
        // Log.d(TAG, "The Destroy current screen is ==portrait");
        // }
    }

    // the method to get screen width height
    private void getScreenSize() {

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        // Log.d(TAG, "screen width  =" + screenWidth + "  screen height  ="
        // + screenHeight + "  density = " + density);

    }

    // the status Height is 25 px
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;
    }

    public void receiveResponse(Intent intent, TaskType taskType, String action, BusinessResponse response) {

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void initLoadData() {
        super.initLoadData();

     
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Log.d(TAG, "The newConfig current screen is ==landscape");
            if (trialVideoView != null) {
                trialVideoView.seekTo(reStart);
                if (trialCon != null)
                    trialCon.show();

            }
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Log.d(TAG, "The newConfig current screen is ==portrait");
            if (trialVideoView != null)
                trialVideoView.pause();
        }
        // Log.d(TAG, "The config content is " + newConfig.toString());
    }

 
    // 视频返回键点击的时�?要进行一次点击积分计�? 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

  
    // 注册广播通知，监听网络状态的切换
    protected BroadcastReceiver netReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (State.CONNECTED == state) {
                        Log.d(TAG, "当前连接网络为MOBILE网络");
                    }
                    State wifiSatte = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (State.CONNECTED == wifiSatte) {
                        Log.d(TAG, "当前连接网络为wifi网络");
                        if (netChangeState) {
                            netChangeState = false;

                        }
                    }
                    isExistNet = true;

                } else {
                    // Log.d(TAG, "当前没有可用的网�?);
                    isExistNet = false;
                    // aboutNoNetUpdate();
                }

            }
            if (Constants.HANDLER.DOWNLOAD_BROADCAST_START.equals(intent.getAction()))

            {

                // 通知对应的界面进行更�?                // mUpdateViewListener.onUpdateViewListener();

            }
            if (Constants.HANDLER.DOWNLOAD_BROADCAST_ERROR.equals(intent.getAction())) {
                // 通知对应的界面进行更�?                // Log.d(TAG, "The service is send broadcast == error");
                // mUpdateViewListener.onUpdateViewListener();

            }
            if (Constants.HANDLER.DOWNLOAD_BROADCAST_SUCCESS.equals(intent.getAction())) {
                // mUpdateViewListener.onUpdateViewListener();
            }

        }
    };

  

}
