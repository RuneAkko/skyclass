package com.android.publiccourse;

import java.util.HashMap;

import com.android.svod.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


//by szk 服务类，主要用来就行网络数据的请求
public class BindDataService extends Service {

    // Binder given to clients(Activity)
    private final IBinder mBinder = new LocalBinder();

    protected LearnbarServiceApi sdkClient = null;

    private final static String TAG = "asyncExcutePubCourseHttpTask";

    public BindDataService() {
        sdkClient = new LearnbarServiceApi();
        Log.d(Constants.DEFAULT_SDK_LOG_TAG, "BindDataService new service contruct...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(Constants.DEFAULT_SDK_LOG_TAG, "BindDataService onCreate...");
        Configuration.setResources(getResources());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public BindDataService getService() {
            // Return this instance of LocalService so clients can call public
            // methods
            return BindDataService.this;
        }
    }

    /**
     * 启动一个新线程，访问MAPIServer，线程结束后，将得到的数据广播出去
     *
     * @param intentAction : used in intent filter
     * @param taskType
     * @param paramsMap    : {request params}
     */
    public BusinessResponse asyncExcuteTask(Class<? extends Context> intentAction,
                                            TaskType taskType, int soapMethodUrl, Class<?
            extends BusinessResponse> responseClass, HashMap<String, String> paramsMap) {
        String soapNameSapce = Configuration.getSoapNameSapce_skyclass();// soap的命名空间
        String soapMethod = Configuration.getProperty(soapMethodUrl); // soap的方法
        String soapUrl = ""; // soap的方法访问地址
        if (soapMethod != null) {
            if (soapMethod.equals(OBUtil.getString(this,
                    R.string.learningbar_sdk_method_User_Login)) || soapMethod.equals(OBUtil.getString(this, R.string.learningbar_sdk_method_addUserActionCount)) || soapMethod.equals(OBUtil.getString(this, R.string.learningbar_sdk_method_SubmitUserFeedback)) || soapMethod.equals(OBUtil.getString(this, R.string.learningbar_sdk_method_GetOBVersionInfo))
                    || soapMethod.equals(OBUtil.getString(this,
                    R.string.learningbar_sdk_method_User_Register))) {
                soapUrl = Configuration.getLoginUrl();
            } else {
                soapUrl = Configuration.getCourseUrl_skyclass();
            }
        }

        String soapHeaderName = Configuration.getDefaultSoapHeader();
        HashMap<String, String> soapHeaderItem = null;
        if (soapHeaderName != null && soapHeaderName.equals("CredentialSoapHeader")) {
            soapHeaderItem = defaultSoapHeaderValue(paramsMap);
        }
        return asyncExcuteTask(intentAction, taskType, soapUrl, responseClass, soapMethod,
                soapNameSapce, soapHeaderName, soapHeaderItem, paramsMap);
    }

    /**
     * * 直接的http请求 启动一个新线程，访问MAPIServer，线程结束后，将得到的数据广播出去
     *
     * @param intentAction : used in intent filter
     * @param taskType
     * @param paramsMap    : {request params}
     */
    public BusinessResponse asyncExcuteHttpTask(Class<? extends Context> intentAction,
                                                TaskType taskType, Class<?
            extends BusinessResponse> responseClass, int httpUrl,
                                                HashMap<String, String> headerPara,
                                                HashMap<String, String> paramsMap) {

        String requestUrl = Configuration.getHttpUrl(httpUrl);
        headerPara = initHeaderPara();
        HttpTask mTask = new HttpTask(this, intentAction, taskType, responseClass, requestUrl,
                headerPara, paramsMap, Constants.HTTP_POST);
        Thread taskR = new Thread(mTask);
        taskR.setName("service thread" + taskType);
        taskR.start();
        return null;
    }

    /**
     * @return
     */
    private HashMap<String, String> initHeaderPara() {
        HashMap<String, String> headerPara = new HashMap<String, String>();
       /* if (((OBMainApp) getApplicationContext()).currentUser != null) {
            headerPara.put("token", ((OBMainApp) getApplicationContext()).currentUser.token);
            // Log.d("--------------", headerPara.get("token") + "---------------");
        }*/

        return headerPara;
    }

    /**
     * 公开课使用的server地址不同
     *
     * @param intentAction
     * @param taskType
     * @param responseClass
     * @param httpUrl
//     * @param headerPara
     * @param paramsMap
     * @return add by renkf 2015-3-25
     */
    public BusinessResponse asyncExcutePubCourseHttpTask(Class<? extends Context> intentAction,
                                                         TaskType taskType, Class<?
            extends BusinessResponse> responseClass, int httpUrl,
                                                         HashMap<String, String> paramsMap) {

        String requestUrl = Configuration.getOpenCourseHttpUrl(httpUrl);
        Log.d("asyncExcutePubCourseHttpTask ", "request url is: " + requestUrl);
        HashMap<String, String> headerPara = initHeaderPara();
        HttpTask mTask = new HttpTask(this, intentAction, taskType, responseClass, requestUrl,
                headerPara, paramsMap, Constants.HTTP_GET);
        Thread taskR = new Thread(mTask);
        taskR.setName("service thread" + taskType);
        taskR.start();
        return null;
    }


    /**
     * 因为soap方法的地址不一样
     *
     * @param intentAction : used in intent filter
     * @param taskType
     * @param paramsMap    : {request params}
     */
    public BusinessResponse asyncExcuteTaskGroup(Class<? extends Context> intentAction,
                                                 TaskType taskType, int soapMethodUrl, Class<?
            extends BusinessResponse> responseClass, HashMap<String, String> paramsMap) {
        String soapNameSapce = Configuration.getSoapNameGroupSapce();// soap的命名空间
        String soapMethod = Configuration.getProperty(soapMethodUrl); // soap的方法
        String soapUrl = Configuration.getGroupUrl(); // soap的方法访问地址
        String soapHeaderName = Configuration.getDefaultSoapHeader();
        HashMap<String, String> soapHeaderItem = null;
        if (soapHeaderName != null && soapHeaderName.equals("CredentialSoapHeader")) {
            soapHeaderItem = defaultSoapHeaderValue(paramsMap);
        }
        return asyncExcuteTask(intentAction, taskType, soapUrl, responseClass, soapMethod,
                soapNameSapce, soapHeaderName, soapHeaderItem, paramsMap);
    }

    /**
     * 因为soap方法的地址不一样
     *
     * @param intentAction : used in intent filter
     * @param taskType
     * @param paramsMap    : {request params}
     */
    public BusinessResponse asyncExcuteTaskPersonal(Class<? extends Context> intentAction,
                                                    TaskType taskType, int soapMethodUrl, Class<?
            extends BusinessResponse> responseClass, HashMap<String, String> paramsMap) {
        String soapNameSapce = Configuration.getSoapNameFriendSapce();// soap的命名空间
        String soapMethod = Configuration.getProperty(soapMethodUrl); // soap的方法
        String soapUrl = Configuration.getPersonalUrl(); // soap的方法访问地址
        String soapHeaderName = Configuration.getDefaultSoapHeader();
        HashMap<String, String> soapHeaderItem = null;
        if (soapHeaderName != null && soapHeaderName.equals("CredentialSoapHeader")) {
            soapHeaderItem = defaultSoapHeaderValue(paramsMap);
        }
        return asyncExcuteTask(intentAction, taskType, soapUrl, responseClass, soapMethod,
                soapNameSapce, soapHeaderName, soapHeaderItem, paramsMap);
    }

    public HashMap<String, String> defaultSoapHeaderValue(HashMap<String, String> paramsMap) {
        HashMap<String, String> soapHeaderContent = new HashMap<String, String>();
        soapHeaderContent.put(Configuration.getProperty(R.string.learningbar_sdk_soap_soapheader_key1), Configuration.getProperty(R.string.learningbar_sdk_soap_soapheader_value1));
        soapHeaderContent.put(Configuration.getProperty(R.string.learningbar_sdk_soap_soapheader_key2), Configuration.getProperty(R.string.learningbar_sdk_soap_soapheader_value2));
        if (paramsMap.get(Constants.PHONE_INFO_SYSTEMTYPE) != null) {
            soapHeaderContent.put(Constants.PHONE_INFO_SYSTEMTYPE,
                    paramsMap.get(Constants.PHONE_INFO_SYSTEMTYPE));
        }
        if (paramsMap.get(Constants.PHONE_INFO_SYSTEMVERSION) != null) {
            soapHeaderContent.put(Constants.PHONE_INFO_SYSTEMVERSION,
                    paramsMap.get(Constants.PHONE_INFO_SYSTEMVERSION));
        }
        if (paramsMap.get(Constants.PHONE_INFO_PHONETYPE) != null) {
            soapHeaderContent.put(Constants.PHONE_INFO_PHONETYPE,
                    paramsMap.get(Constants.PHONE_INFO_PHONETYPE));
        }
        if (paramsMap.get(Constants.PHONE_INFO_PHONEID) != null) {
            soapHeaderContent.put(Constants.PHONE_INFO_PHONEID,
                    paramsMap.get(Constants.PHONE_INFO_PHONEID));
        }
        if (paramsMap.get(Constants.PHONE_INFO_APPVERSION) != null) {
            soapHeaderContent.put(Constants.PHONE_INFO_APPVERSION,
                    paramsMap.get(Constants.PHONE_INFO_APPVERSION));
        }
        if (paramsMap.get(Constants.PHONE_INFO_NUMBER) != null) {
            soapHeaderContent.put(Constants.PHONE_INFO_NUMBER,
                    paramsMap.get(Constants.PHONE_INFO_NUMBER));
        }
        if (paramsMap.get(Constants.PHONE_SCREEN_SIZE) != null) {
            soapHeaderContent.put(Constants.PHONE_SCREEN_SIZE,
                    paramsMap.get(Constants.PHONE_SCREEN_SIZE));
        }

        return soapHeaderContent;
    }

    /**
     * 启动一个新线程，访问MAPIServer，线程结束后，将得到的数据广播出去
     *
     * @param intentAction : used in intent filter
     * @param taskType
     * @param paramsMap    : {request params}
     */
    public BusinessResponse asyncExcuteTask(Class<? extends Context> intentAction,
                                            TaskType taskType, String soapUrl, Class<?
            extends BusinessResponse> responseClass, String soapMethod, String soapNameSapce,
                                            String soapHeaderName,
                                            HashMap<String, String> soapHeaderItem,
                                            HashMap<String, String> paramsMap) {

        HttpSoapTask mTask = new HttpSoapTask(this, intentAction, taskType, soapUrl,
                responseClass, soapMethod, soapNameSapce, soapHeaderName, soapHeaderItem,
                paramsMap);
        Log.d("----soapurl=---", soapUrl);
        Thread taskR = new Thread(mTask);
        taskR.setName("service thread" + taskType);
        taskR.start();
        return null;
    }


    /**
     * 获取公共课程列表
     *
     * @param intentAction
//     * @param studentCode
//     * @param orderByType
//     * @param orderType
     * @param PageIndex
     * @param PageSize
     */
    public GetPubCourseListResponse getPubCourseList(Class<? extends Context> intentAction,
                                                     String categoryCode, String PageIndex,
                                                     String PageSize) {
        HashMap<String, String> paras = new HashMap<String, String>();

        //  paras.put("categoryCode", "");
        //  paras.put("pageIndex", PageIndex);
        //   paras.put("pageSize", PageSize);
        return (GetPubCourseListResponse) asyncExcuteTask(intentAction,
                TaskType.Get_Pub_Course_List, R.string.learningbar_sdk_method_GetPublicCourseList
                , GetPubCourseListResponse.class, paras);
    }

    /**
     * 首页公开课
     * 同时包含了滚动列表，和下拉列表
     *
     * @param intentAction
     * @return add by renkf at 2015-3-25
     */
    public GetPublicCoursesResponse retrivePublicCourses(Class<? extends Context> intentAction) {
        HashMap<String, String> paras = new HashMap<String, String>();
        return (GetPublicCoursesResponse) asyncExcutePubCourseHttpTask(intentAction,
                TaskType.Get_Public_Courses, GetPublicCoursesResponse.class,
                R.string.getHomeCourses, paras);
    }


}
