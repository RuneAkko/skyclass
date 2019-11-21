/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 */

package com.android.svod.intelligentroom;

import com.umeng.analytics.MobclickAgent;

import com.android.svod.R;
import java.io.IOException;
import java.util.List;

import com.android.ui.vitamio.LibsChecker;

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

@SuppressWarnings("deprecation")
public class VideoPlayerActivity extends Activity
		implements OnCompletionListener, OnInfoListener, OnBufferingUpdateListener {

	public boolean isFinish = false;
	private String mPath;
	private String mTitle;
    
	// 存放在数据库中的进行保存的数据
	private String studentcode;
	private String courseid;
	private String path;
	private Boolean isOnlinePlay;
	private Boolean isLive;
	private String activity = null;
	// private Userservice usrService;

	// 视频播放使用到的东西
	private VideoView mVideoView;
	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;

	// 屏幕关闭时启动
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				// 停止视频的播放
				if (mVideoView.isPlaying()) {
					// usrService.save(new
					// User(studentcode,coursecode,path,"��ͣѧϰ",StringUtility.getDateTime()));
					stopPlayer();
				}
			}
		}
	};

	/** 最大声音 */
	private int mMaxVolume;
	/** 初始声音 */
	private int mVolume = -1;
	/** 初始亮度 */
	private float mBrightness = -1f;
	/** 缩放参数 */
	private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	// 手势识别
	private GestureDetector mGestureDetector;
	// 对视频播放器呢进行控制，需要对其进行初始化，并有自己的xml文件
	private MediaController mMediaController;

	private View mLoadingView;
	private static final int MSG_NOLOAD = 0;
	private static final int MSG_LOAD = 1;
	private Thread mThread;
	private Toast toast;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {// 此方法在ui线程运行
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
		// 判断是不是视频播放器已经初始化
		if (!LibsChecker.checkVitamioLibs(this, R.string.init_decoders))
			return;

		Intent intent = getIntent();
		// 得到视频播放路径
		mPath = intent.getStringExtra("mpath");
		Log.i(mPath);
		System.out.println("mpath------>"+mPath);
		mTitle = intent.getStringExtra("title");
		Log.i(mTitle);
		
		//是否是在线播放
		isOnlinePlay = intent.getBooleanExtra("isOnlinePlay", false);
		Log.i(String.valueOf(isOnlinePlay));
		// 是否是直播
		isLive = intent.getBooleanExtra("isLive", false);
		Log.i(String.valueOf(isLive));
		// usrService = new Userservice(this.getApplicationContext());
		/*studentcode = intent.getStringExtra("studentcode");
		Log.i(studentcode);
		courseid = intent.getStringExtra("courseid");
		Log.i(courseid);
		// 得到视频的名称
		path = intent.getStringExtra("path");
		Log.i(path);
		
		
		// 确定生活场景
		/*activity = intent.getStringExtra("activity");
		Log.i(activity);*/

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mBatInfoReceiver, filter);

		setContentView(R.layout.videoview);
		// 创建新的videview
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		// 调节声音和亮度
		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		// 进行声音的调控
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		// 视频播放的进度
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
		mLoadingView = findViewById(R.id.video_loading);

		// mLoadingView.setVisibility(View.GONE);
		// 播放完成进行调用
		mVideoView.setOnCompletionListener(this);
		if (isOnlinePlay)
			mVideoView.setBufferSize(100);
		else
			mVideoView.setBufferSize(0);

		//视频缓冲进行调用
		mVideoView.setOnInfoListener(this);
		// 设置最大的声音
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// 进行相似的调用
		if (mPath.startsWith("http:"))
			mVideoView.setVideoURI(isLive,Uri.parse(mPath));
		else
			mVideoView.setVideoPath(isLive,mPath);

		//mVideoView.setActivity(activity);

		mMediaController = new MediaController(this);
		
		//设置显示文本控件的内容
		mMediaController.setFileName(mTitle);
		mVideoView.setMediaController(mMediaController);
		mVideoView.requestFocus();

		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		if (mThread == null) {
			mThread = new Thread(runnable);
			mThread.start();// 线程启动
		}

		Log.i("create");

	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					mThread.sleep(2000);// 每两秒执行一次
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
		Log.i("start");
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
		Log.i("resume");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mVideoView != null) {
			mVideoView.stopPlayback();
			mVideoView = null;
		}

		//取消广播的侦听
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

	// 完成手势识别的任务
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;

		//进行任务的判别 
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}

	/** 结束手势识别*/
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// ����
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 500);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		/**双击事件*/
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

		/** 屏幕滑动事件 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			if (mOldX > windowWidth * 4.0 / 5)//声音的控制
				onVolumeSlide((mOldY - y) / windowHeight);
			else if (mOldX < windowWidth / 5.0)//亮度的控制
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
	 * 对声音进行控制
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
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 对亮度进行控制
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
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
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

	// 视频暂停播放
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
			toast = Toast.makeText(VideoPlayerActivity.this, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	/** 是否需要自动恢复播放，用于自动暂停，恢复播放 */
	private boolean needResume;

	@Override
	public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
		System.out.println("!!!!!!!!!!!!!right here");
		if (arg0 != null) {
			if (arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_START && mVideoView.getmCurrentBufferPercentage() <= 5) {// 缓冲区的数据少于5%时才停止播放；
				stopPlayer();
				needResume = true;
				mLoadingView.setVisibility(View.VISIBLE);
				mMediaController.mPauseButton.setClickable(false);
			}

			else if (arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_END || mVideoView.getmCurrentBufferPercentage() > 7) {// 缓冲区数据大于30%的时候即恢复播放
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
		if (mVideoView.isCancelable() && (KeyEvent.KEYCODE_HOME == keyCode || KeyEvent.KEYCODE_BACK == keyCode)) {
			mVideoView.stopPlayback();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mVideoView.setmCurrentBufferPercentage(percent);
		if (mVideoView.getmOnBufferingUpdateListener() != null)
			mVideoView.getmOnBufferingUpdateListener().onBufferingUpdate(mp, percent);
	}

}
