package com.android.publiccourse;

import java.util.Formatter;
import java.util.Locale;

import com.android.svod.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class OBVideoController extends FrameLayout {
    private static final String TAG = "OBVideoController";

    private SimplePlayerControl mPlayer;
    private Context mContext;
    private View mAnchor;
    private View mRoot;
    private WindowManager mWindowManager;
    private Window mWindow;
    private View mDecor;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 6000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private LinearLayout mInlargeBack;
    private LinearLayout mInlargePause;
    private TextView mShowVideoName;
    private TextView mShowCurrentTime;
    private TextView mShowTotalTime;
    private ImageButton mSoundButton;
    private ImageView mPauseButton;
    private ImageView mDownLoadButton;
    private ProgressBar mProgress;
    private ProgressBar mSoundProgress;
    private int screenWidth;
    private int screenHeight;
    private AudioManager soundManager;
    private String sVideoName;

    // 下载相关
    private OBDataUtils mDb;
    private String sVideoId;
    private String sVideoUrl;
    private int sVideoSize;
    private boolean mNetChangeState;

    Handler sendHandler;
    private boolean mIsExistNet;

    public int y = 450; // lixl
    public int x = 0;
    LinearLayout linearLayoutMain; // lixl

    public OBVideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
        soundManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mRoot = this;
        mContext = context;

        mDb = OBDataUtils.getInstance(context);
        Log.d("DOWNLOAD", "mDb = " + mDb);
    }

    public String getsVideoName() {
        return sVideoName;
    }

    public void setsVideoName(String sVideoName) {
        this.sVideoName = sVideoName;
    }

    public String getsVideoId() {
        return sVideoId;
    }

    public void setsVideoId(int sVideoId) {
        this.sVideoId = String.valueOf(sVideoId);
    }

    public String getsVideoUrl() {
        return sVideoUrl;
    }

    public void setsVideoUrl(String sVideoUrl) {
        this.sVideoUrl = sVideoUrl;
    }

    public void setVideoSize(int videoSize) {
        this.sVideoSize = videoSize;
    }

    public void setmIsExistNet(boolean mIsExistNet) {
        this.mIsExistNet = mIsExistNet;
    }

    public void setmNetChangeState(boolean mNetChangeState) {
        this.mNetChangeState = mNetChangeState;
    }

    public OBVideoController(Context context, boolean useFastForward, int width, int height) {
        super(context);
        soundManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mContext = context;
        screenWidth = width;
        screenHeight = height;
        // mUseFastForward = useFastForward;
        initFloatingWindow();
        mDb = OBDataUtils.getInstance(context);

    }

    public OBVideoController(Context context) {
        super(context);
        mContext = context;
        // mUseFastForward = true;
        initFloatingWindow();
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
    }

    private void initFloatingWindow() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        while(mWindow==null)
        {
            mWindow = PhoneWindowUtil.makeNewWindow(mContext);
        }
        mWindow.setWindowManager(mWindowManager, null, null);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mDecor = mWindow.getDecorView();
        mWindow.setContentView(this);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        // While the media controller is up, the volume control keys should
        // affect the media stream type
        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }

    public void setMediaPlayer(SimplePlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    /**
     * Set the view that acts as the anchor for the control view. This can for
     * example be a VideoView, or your Activity's main view.
     * 
     * @param view
     *            The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(View view) {
        mAnchor = view;
        // FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
        // ViewGroup.LayoutParams.FILL_PARENT,
        // ViewGroup.LayoutParams.FILL_PARENT
        // );
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(screenWidth, screenHeight);

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     * 
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.res_videoview_simple, null);

        initControllerView(mRoot);

        return mRoot;
    }

    private void initControllerView(View v) {
        mInlargeBack = (LinearLayout) v.findViewById(R.id.video_back_navigation_large);
        if (mInlargeBack != null) {
            mInlargeBack.requestFocus();
            mInlargeBack.setOnClickListener(mFinhListener);
        }
        mShowVideoName = (TextView) v.findViewById(R.id.video_play_text_name);
        if (sVideoName != null) {
            mShowVideoName.setText(sVideoName);
        }
        mShowCurrentTime = (TextView) v.findViewById(R.id.video_current_time);
        mShowTotalTime = (TextView) v.findViewById(R.id.video_total_time);
        mInlargePause = (LinearLayout) v.findViewById(R.id.video_bottom_play_layout);
        mPauseButton = (ImageView) v.findViewById(R.id.video_bottom_play_button);
        mDownLoadButton = (ImageView) v.findViewById(R.id.video_bottom_download_button);

        if (mInlargePause != null) {
            mInlargePause.requestFocus();
            mInlargePause.setOnClickListener(mPauseListener);
        }

        mProgress = (SeekBar) v.findViewById(R.id.video_play_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }
        mSoundButton = (ImageButton) v.findViewById(R.id.video_sound_status_tag);
        if (mSoundButton != null) {
            mSoundButton.requestFocus();
            mSoundButton.setOnClickListener(mSoundListener);
            if (soundManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                mSoundButton.setBackgroundResource(R.drawable.img_video_sound_silent);
            } else {
                mSoundButton.setBackgroundResource(R.drawable.img_video_sound_tag);
            }
        }
        mSoundProgress = (SeekBar) v.findViewById(R.id.video_sound_progress);
        if (mSoundProgress != null) {
            if (mSoundProgress instanceof SeekBar) {
                SeekBar soundSeeker = (SeekBar) mSoundProgress;
                soundSeeker.setOnSeekBarChangeListener(mSoundProgressListener);
            }
            if (soundManager != null) {
                mSoundProgress.setMax(soundManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                mSoundProgress.setProgress(soundManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            }
        }

        // 对于竖屏 动�?设置中间进度条的长度 以显示出两边的两个按�?        // linearLayoutMain = (LinearLayout)
        // v.findViewById(R.id.linearLayoutMain);
        // if (linearLayoutMain != null && screenWidth > 0) {
        // LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
        // linearLayoutMain
        // .getLayoutParams();
        // lp.width = screenWidth * 4 / 5;
        // linearLayoutMain.setLayoutParams(lp);
        // }
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        // installPrevNextListeners();
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        try {
            if (mInlargePause != null && !mPlayer.canPause()) {
                mInlargePause.setEnabled(false);
            }
            // if (mRewButton != null && !mPlayer.canSeekBackward()) {
            // mRewButton.setEnabled(false);
            // }
            // if (mFfwdButton != null && !mPlayer.canSeekForward()) {
            // mFfwdButton.setEnabled(false);
            // }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't
            // disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     * 
     * @param timeout
     *            The timeout in milliseconds. Use 0 to show the controller
     *            until hide() is called.
     */
    public void show(int timeout) {

        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mInlargePause != null) {
                mInlargePause.requestFocus();
            }
            if (soundManager != null) {
                if (soundManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
                    if (mSoundButton != null && mSoundProgress != null) {
                        mSoundButton.setBackgroundResource(R.drawable.img_video_sound_silent);
                        mSoundProgress.setProgress(soundManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    }
                } else {
                    if (mSoundButton != null && mSoundProgress != null) {
                        mSoundButton.setBackgroundResource(R.drawable.img_video_sound_tag);
                        mSoundProgress.setProgress(soundManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    }
                }
            }
            disableUnsupportedButtons();

            int[] anchorpos = new int[2];
            mAnchor.getLocationOnScreen(anchorpos);

            WindowManager.LayoutParams p = new WindowManager.LayoutParams();
            p.gravity = Gravity.TOP;
            p.width = screenWidth; // mAnchor.getWidth();
            p.height = screenHeight; // LayoutParams.WRAP_CONTENT;
            p.x = x;
            p.y = y;// anchorpos[1] + mAnchor.getHeight() - p.height;
            p.format = PixelFormat.TRANSLUCENT;
            p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            p.token = null;
            p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
            mWindowManager.addView(mDecor, p);

            mShowing = true;
        }
        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true. This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                mWindowManager.removeView(mDecor);
            } catch (IllegalArgumentException ex) {
                // Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
            case FADE_OUT:
                hide();
                break;
            case SHOW_PROGRESS:
                pos = setProgress();
                if (!mDragging && mShowing && mPlayer.isPlaying()) {
                    msg = obtainMessage(SHOW_PROGRESS);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
                // if(!mPlayer.isPlaying()){ //已播放进度到100�?                // mPlayer.seekTo(0); //将播放进度跳会起始位�?                // }
                break;
            }
        }
    };

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        // 进度更新的时候刷新点击时�?        // CountCourseScore.perClickUpdate(((OBLMediaPlayer)mContext).getmDb(),
        // (OBLMediaPlayer)mContext, ((OBLMediaPlayer)mContext).getCourseId());
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        // //for HTC mobile,when jump to the end ,it's not has thevideo
        // if( position == duration )
        // {
        // doAginResume();
        // show(sDefaultTimeout);
        // }
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                if (pos >= 998)
                    pos = 0;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }
        if (mShowCurrentTime != null)
            mShowCurrentTime.setText(stringForTime(position));
        if (mShowTotalTime != null)
            mShowTotalTime.setText(stringForTime(duration));
        return position;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        }
    }

  

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.d("trial", " onTouchEvent  =  " + event.getRawY());
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP && mShowing && ((event.getRawY() > 100 && event.getRawY() < 400) || (event.getRawY() > 500 && event.getRawY() < 600))) {
            hide();
        }
        // show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // don't show the controls for volume adjustment
            if (mPlayer != null) {
                if (soundManager != null) {
                    int currentInt = soundManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (currentInt == 0) {
                        mSoundButton.setBackgroundResource(R.drawable.img_video_sound_tag);
                    } else {
                        mSoundButton.setBackgroundResource(R.drawable.img_video_sound_tag);
                    }
                    mSoundProgress.setProgress(currentInt);

                }
            }
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setNetChanged(boolean netChangeState) {
        mNetChangeState = netChangeState;
    }

 

 
    public void setHandler(Handler handler) {

        this.sendHandler = handler;

    }


    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            // 暂停播放的时候进行一次时间刷�?        
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                if (mHandler != null)
                    mHandler.removeMessages(FADE_OUT);
            } else {
                mPlayer.start();
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(SHOW_PROGRESS);
                    mHandler.removeMessages(FADE_OUT);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT), sDefaultTimeout);
                }
            }
            updatePausePlay();
            // show(sDefaultTimeout);
        }
    };

    private View.OnClickListener mFinhListener = new View.OnClickListener() {
        public void onClick(View v) {

            mContext.sendBroadcast(new Intent("open.video.finish"));
        }
    };

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.img_video_tag_pause);
        } else {
            mPauseButton.setImageResource(R.drawable.img_video_tag_play);
        }
    }

    public void doPause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        updatePausePlay();
    }

    public void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void doAginResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.seekTo(0);
            mPlayer.pause();
        }
        updatePausePlay();
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by
    // onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the
    // dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch
    // notifications,
    // we will simply apply the updated position without suspending regular
    // updates.
    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            long mCurrentTime = mPlayer.getCurrentPosition();
            if (mShowCurrentTime != null)
                mShowCurrentTime.setText(stringForTime((int) mCurrentTime));
            if (mShowTotalTime != null)
                mShowTotalTime.setText(stringForTime((int) duration));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };
    private View.OnClickListener mSoundListener = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };
    private OnSeekBarChangeListener mSoundProgressListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 界面进行了操作需要刷新点击时�?     
            if (soundManager != null) {
                int currentValue = soundManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seekBar.setProgress(currentValue);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            int maxValue = soundManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentValue = (maxValue * progress) / maxValue;
            soundManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentValue, 0);
            if (progress == 0) {
                mSoundButton.setBackgroundResource(R.drawable.img_video_sound_silent);
            } else {
                mSoundButton.setBackgroundResource(R.drawable.img_video_sound_tag);
            }

        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mInlargePause != null) {
            mInlargePause.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    // private View.OnClickListener mRewListener = new View.OnClickListener() {
    // public void onClick(View v) {
    // int pos = mPlayer.getCurrentPosition();
    // pos -= 5000; // milliseconds
    // mPlayer.seekTo(pos);
    // setProgress();
    //
    // show(sDefaultTimeout);
    // }
    // };
    //
    // private View.OnClickListener mFfwdListener = new View.OnClickListener() {
    // public void onClick(View v) {
    // int pos = mPlayer.getCurrentPosition();
    // pos += 15000; // milliseconds
    // mPlayer.seekTo(pos);
    // setProgress();
    //
    // show(sDefaultTimeout);
    // }
    // };
    public interface SimplePlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();
    }

}
