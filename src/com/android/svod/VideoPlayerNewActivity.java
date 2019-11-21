package com.android.svod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnCompletionListener;

import com.android.sql.Userservice;

import com.android.svod.changednative.VideoView;
import com.android.svod.changednative.MediaController;

public class VideoPlayerNewActivity extends Activity
        implements OnCompletionListener {

    private static final String TAG = "VideoPlayerNewActivity";

    private String mPath;
    private String Path;
    private String mTitle;
    private String studentcode;
    private String courseid;
    private String path;
    private Boolean isOnlinePlay;
    private Boolean isLive;
    private String activity = null;

    private Userservice usrService;
    private static String time = "";
    SensorService sensorService = null;
    ConnectivityManager cm = null;
    NetworkInfo.State wifi = null;
    Intent uploadIntent = null;
    private boolean binded = false;

    private VideoView videoview;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = getIntent();
        mPath = intent.getStringExtra("mpath");
        mTitle = intent.getStringExtra("title");
        usrService = new Userservice(this.getApplicationContext());
        studentcode = intent.getStringExtra("studentcode");
        courseid = intent.getStringExtra("courseid");
        path = intent.getStringExtra("path");
        isOnlinePlay = intent.getBooleanExtra("isOnlinePlay", false);
        isLive = intent.getBooleanExtra("isLive", false);
        activity = intent.getStringExtra("activity");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.videoplay);

        //本地的视频 需要在手机SD卡根目录添加一个Demo.mp4 视频

//         String videoUrl1 = Environment.getExternalStorageDirectory().getPath()+"/Demo.mp4" ;

        //网络视频
        videoview = (VideoView) findViewById(R.id.public_videoView);

        //设置视频控制器
        videoview.setMediaController(new MediaController(this));

        //设置视频路径
        //videoview.setVideoURI(Uri.parse(mPath));

        if (mPath.startsWith("http:")) {
            Path = mPath.replace("http", "https");
        }

        if (mPath.startsWith("https:")) {
            Path = mPath;
        }


        if (Path == null) {
            videoview.setVideoPath(isLive, studentcode, courseid, path, mPath);
        } else if (Path.startsWith("https:")) {
            videoview.setVideoURI(isLive, studentcode, courseid, path, Uri.parse(Path));
        } else {
            videoview.setVideoPath(isLive, studentcode, courseid, path, mPath);
        }


        //这里用相对布局包裹videoview 实现视频全屏播放
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        videoview.setLayoutParams(layoutParams);


        //开始播放视频
        videoview.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoview != null) {
            videoview.stopPlayback();
            videoview = null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (videoview.isCancelable()
                && (KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode)) {
            videoview.stopPlayback();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // 视频播放到终点
    @Override
    public void onCompletion(MediaPlayer player) {
        finish();
    }
   /* @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        System.out.println("!!!!!!!!!!!!!right here");
        if (arg0 != null) {
            if (arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_START
                    && videoview.getmCurrentBufferPercentage() <= 5) {// 缓冲区的数据少于5%时才停止播放；
                stopPlayer();
                needResume = true;
                mLoadingView.setVisibility(View.VISIBLE);
                mMediaController.mPauseButton.setClickable(false);
            }

            else if (arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_END
                    || mVideoView.getmCurrentBufferPercentage() > 30) {// 缓冲区数据大于30%的时候即恢复播放
                if (needResume)
                    startPlayer();
                mLoadingView.setVisibility(View.GONE);
                mMediaController.mPauseButton.setClickable(true);
            }
        }

        return true;
    }

    // 开始播放
    private void startPlayer() {
        if (videoview != null) {
            videoview.start();

        }
    }*/

}




