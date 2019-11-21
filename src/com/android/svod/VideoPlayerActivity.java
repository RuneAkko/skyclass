/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 */

package com.android.svod;

import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;

import com.android.domain.User;
import com.android.sensorecord.SensorLogger;
import com.android.sensorecord.uploadService;
import com.android.sql.Userservice;
import com.android.svod.R;
import com.android.ui.vitamio.LibsChecker;
import com.android.updater.UpdateUtil;
import com.android.domain.StringUtility;
import com.android.domain.Preference;

import io.vov.utils.Log;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.VideoView;
//import android.widget.MediaController;

@SuppressWarnings("deprecation")
public class VideoPlayerActivity extends Activity
        implements OnCompletionListener, OnInfoListener, OnBufferingUpdateListener {

    public boolean isFinish = false;
    private String mPath;
    private String mTitle;

    //
    private String studentcode;
    private String courseid;
    private String path;
    private Boolean isOnlinePlay;
    private Boolean isLive;
    private String activity = null;
    private Userservice usrService;

    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;

    //
    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // ��ͣ����
                if (mVideoView.isPlaying()) {
                    // usrService.save(new
                    // User(studentcode,coursecode,path,"��ͣѧϰ",StringUtility.getDateTime()));
                    stopPlayer();
                }
            }
        }
    };


    /**
     * �������
     */
    private int mMaxVolume;
    /**
     * ��ǰ����
     */
    private int mVolume = -1;
    /**
     * ��ǰ����
     */
    private float mBrightness = -1f;
    /**
     * ��ǰ����ģʽ
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;

    private View mLoadingView;
    private static final int MSG_NOLOAD = 0;
    private static final int MSG_LOAD = 1;
    private Thread mThread;
    private Toast toast;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case MSG_NOLOAD:

                    mLoadingView.setVisibility(View.GONE);

                    break;

                case MSG_LOAD:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (!LibsChecker.checkVitamioLibs(this, R.string.init_decoders))
            return;


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

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mBatInfoReceiver, filter);

        setContentView(R.layout.videoview);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mLoadingView = findViewById(R.id.video_loading);

        // mLoadingView.setVisibility(View.GONE);

        mVideoView.setOnCompletionListener(this);
        if (isOnlinePlay)
            mVideoView.setBufferSize(100);
        else
            mVideoView.setBufferSize(0);

        mVideoView.setOnInfoListener(this);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        if (mPath.startsWith("http:")) {
            mVideoView.setVideoURI(isLive, studentcode, courseid, path, Uri.parse(mPath));
        }
        //mVideoView.setVideoURI(isLive, studentcode, courseid, path,Uri.parse("http://www
		// .modrails.com/videos/passenger_nginx.mov"));
        //mVideoView.setVideoURI(Uri.parse("http://www.modrails.com/videos/passenger_nginx.mov"));
        else
            mVideoView.setVideoPath(isLive, studentcode, courseid, path, mPath);


        mVideoView.setActivity(activity);

        mMediaController = new MediaController(this);
        mMediaController.setFileName(mTitle);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //mVideoView.start();

        if (mThread == null) {

            mThread = new Thread(runnable);
            mThread.start();//线程启动
        }


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    mThread.sleep(2000);//每两秒执行一次
                    if (mVideoView != null && mVideoView.isPlaying()) {
                        Message msg = new Message();
                        msg.what = MSG_NOLOAD;
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    };

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mVideoView != null) {
            mVideoView.pause();
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }

        if (mBatInfoReceiver != null) {
            try {
                unregisterReceiver(mBatInfoReceiver);
            } catch (Exception e) {
            }
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // �������ƽ���
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * ���ƽ���
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // ����
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }


    private class MyGestureListener extends SimpleOnGestureListener {

        /**
         * ˫��
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }

        /**
         * ����
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// �ұ߻���
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// ��߻���
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    /**
     * �����ı�������С
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // ��ʾ
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * �����ı�����
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // ��ʾ
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width =
				(int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }

    // 视频播放到终点
    @Override
    public void onCompletion(MediaPlayer player) {
        finish();
    }

    private void stopPlayer() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    // 开始播放
    private void startPlayer() {
        if (mVideoView != null) {
            mVideoView.start();

        }
    }

    private boolean isPlaying() {
        return mVideoView != null && mVideoView.isPlaying();
    }

    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(VideoPlayerActivity.this, msg,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 是否需要自动恢复播放，用于自动暂停，恢复播放
     */
    private boolean needResume;

    @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        System.out.println("!!!!!!!!!!!!!right here");
        if (arg0 != null) {
            if (arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_START
                    && mVideoView.getmCurrentBufferPercentage() <= 5) {// 缓冲区的数据少于5%时才停止播放；
                stopPlayer();
                needResume = true;
                mLoadingView.setVisibility(View.VISIBLE);
                mMediaController.mPauseButton.setClickable(false);
            } else if (arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_END
                    || mVideoView.getmCurrentBufferPercentage() > 30) {// 缓冲区数据大于30%的时候即恢复播放
                if (needResume)
                    startPlayer();
                mLoadingView.setVisibility(View.GONE);
                mMediaController.mPauseButton.setClickable(true);
            }
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (mVideoView.isCancelable()
                && (KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode)) {
            mVideoView.stopPlayback();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mVideoView.setmCurrentBufferPercentage(percent);
        if (mVideoView.getmOnBufferingUpdateListener() != null)
            mVideoView.getmOnBufferingUpdateListener().onBufferingUpdate(mp,
                    percent);
    }

}
