package com.android.publiccourse;

import java.util.ArrayList;
import java.util.Date;

import com.android.svod.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author zhangchunzhe
 * @ClassName: OBCourseMainActivity
 * @Description: TODO
 * @date 2013-1-15 下午2:50:42
 */

//skyclass 首页
public class OBLCourseMainActivity extends OBLServiceMainActivity implements IScrollLoadListener,
        OnClickListener {

    public static final int ADD_FRAGMENT = 1001;
    public static final int REPLACE_FRAGMENT = 1002;
    public static final int REMOVE_FRAGMENT = 1003;

    public static final int mRequestCodePubTheme = 9001;

    private final static int btnPubSubjectId = 100000010;

    private static final int MENU_INIT_VALUE = 1000011;

    private FrameLayout mMianLayout;

    private OBLPublicCourseHandle mPublicCourseHandle;

    private View mCourseView;
    private View mGroupView;
    private View mFriendsView;
    private View mMoreView;

    private View pubSubjectView = null;// add by renkf 2015-4-3 发布主题用的按钮


    private ViewGroup mNavigationParent;

    private View mNewMsgCountView;
    private TextView mNewMsgCountText;
    private View mNewSysNCountView;
    private TextView mNewSysNCountText;


    public ArrayList<CourseItem> mCourseList;
    public int mIndex;
    public int isStudy;
    private boolean AttentionThemeStatus;
    public boolean isThemeList;
    // 添加标志位，判断用户按下手机返回键后对应的规�?   
    private int flag_back = 1;

    private int mBackFlag = 1;// �?��返回标示

    private int notifiNumber;
    private int pageSize = 15; // 主题每次请求15�?   
    public int themeTotalCount = 0; // 服务器端的主题�?�?    
    public int pageNumber = 1;
    public boolean isRuquestDate;
    public boolean isRefreshTheme = false; // 刷新主题列表
    public int gotThemeCount = 0; // 已请求主题的数量

    private  final  static  String TAG = "Mqtt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //








//        setClassName(this);
        setTitleBarContentView(R.layout.course_main_list);
        getIntentData();
        findView();
        Log.d("Mqtt", "OBLCourseMainActivity-- onCreate" + " taskID  = " + getTaskId() + " " +
                "isRootTask = " + isTaskRoot() + " this = " + this);
    }


    // 设置消息监听
    @Override
    protected void onRestart() {
        super.onRestart();
        // MessageHandler.getInstance().setMessageCallBack(this);
        // MessageHandler.getInstance().sendEmptyMessage(Constants.UPDATE_MESSAGE_NUMBER);
        // MessageHandler.getInstance().sendEmptyMessage(Constants.UPDATE_SYSN_MORE);
        Log.d("Mqtt", "OBLCourseMainActivity-- onRestart");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }


    // 移除消息监听
    @Override
    protected void onStop() {
        super.onStop();
        // MessageHandler.getInstance().setMessageCallBack(null);
    }


    @Override
    protected void onDestroy() {
        Log.d("Mqtt", "OBLCourseMainActivity-- onDestroy");
        super.onDestroy();
    }


    private void getIntentData() {

    }


    private void findView() {

        // mHandler.setMessageCallBack(this);
        notifiNumber = this.getIntent().getIntExtra("notification", 1111111);

        mMianLayout = (FrameLayout) findViewById(R.id.MainFragmentViews);

        mPublicCourseHandle = new OBLPublicCourseHandle(this);

        //MessageHandler.getInstance().sendEmptyMessage(Constants.UPDATE_MESSAGE_NUMBER);

        handleRole();
    }

    /**
     *
     */
    private void handleRole() {


        //     changeMainView(mPublicCourseHandle.findView());
        mMianLayout.addView(mPublicCourseHandle.findView());
        //  updateNavigation(mPubCourseNavLayout);
        return;

        /*
         * 注册或登录界�?if("guide".equals(getIntent().getAction())){
         *
         * setActionBarTitle(OBLCourseMainActivity.this.getResources()
         * .getString(R.string.ob_login_acty_btn_login));
         * changeMainView(mRegistGuideHandle.findView());
         * updateNavigation(mFriendNavLayout); return; }
         */
        // 判断是否从消息过�?        if (Constants.Inte

    }


    @Override
    protected void initLoadData() {
        super.initLoadData();
        requestPubCourseData();
    }


    public void requestPubCourseData() {
        showPubLoadingProgress(this, R.string.ob_loading_tips);
        Log.d("------", "requestPubCourseData------");
        //旧版获取公开课，comment by renkf at 2015-3-25
        mService.getPubCourseList(OBLCourseMainActivity.class, "", "1", "10");
    }

    public void requestPubCourseData(int pageIndex) {
        showPubLoadingProgress(this, R.string.ob_loading_tips);
        mService.getPubCourseList(OBLCourseMainActivity.class, "", String.valueOf(pageIndex), "10");
    }

    /*
     * 公共课视频第�?��，现在没用，保留�?��时间，删除！ public void requestFirstThemeClassList(){
     * showLoadingProgress(this, R.string.ob_loading_tips);
     * mService.getThemeClass(OBLCourseMainActivity.class, getUserID(),"1",
     * getStudentCode()); } public void requestFirstThemeClassList(int
     * PageNumber) { showLoadingProgress(this, R.string.ob_loading_tips);
     * mService.getThemeClass(OBLCourseMainActivity.class,
     * getUserID(),String.valueOf(pageSize), getStudentCode()); }
     */


    @Override
    public void receiveResponse(Intent intent, TaskType taskType, String action,
                                BusinessResponse response) {
        super.receiveResponse(intent, taskType, action, response);
        if (!response.getStatus()) {
            cancelLoadingProgress();
            UIUtils.getInstance().showToast(this, "获取课程数据失败");
            finish();
            return;
        }
        if (taskType == TaskType.Get_Pub_Course_List) {
            //旧版公开�?
            GetPubCourseListResponse pubCourseListResponse = (GetPubCourseListResponse) response;
            if (isRuquestDate) {
                ExtArrayList<VideoItem> List = pubCourseListResponse.getCoursesList();
                mPublicCourseHandle.fillMoreCourseData(pubCourseListResponse.getCoursesList());
            } else {
                ExtArrayList<VideoItem> List = pubCourseListResponse.getCoursesList();
                mPublicCourseHandle.fillCourseData(pubCourseListResponse.getCoursesList());
            }
            cancelPubLoadDialog();
        }
    }


    @Override
    protected void onResume() {
        /* 点击主题actionBar上返回箭�?*/
        super.onResume();
        Log.d("Mqtt", "OBLCourseMainActivity-- onResume");
    }


    @Override
    protected void handlerRequestFailed(TaskType taskType, BusinessResponse response) {
        // TODO Auto-generated method stub

        if (taskType == TaskType.Ob_Auto_Check_Version) {
            return;
        }
        super.handlerRequestFailed(taskType, response);
    }


    @Override
    public void onListViewScrollLoadingData() {
        // TODO Auto-generated method stub

    }

    // 课程、更多应用主页面按手机返回键规则：前者弹出AlertDialog；后者返回课程主页面�?  
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        View quitView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.quit, null);
        new AlertDialog.Builder(this).setView(quitView)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();

        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}
