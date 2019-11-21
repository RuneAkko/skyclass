package com.android.publiccourse;

/**
 * 
 * @ClassName: Contsants
 * @Description: 常量�? * @author zhangchunzhe
 * @date 2013-1-14 下午3:52:38
 * 
 */
public final class Constants {
    public static final int MQTT_SYSTEM_FORCE_UPDATE = 201411171;
    public static final int MQTT_SYSTEM_CLOSE = 201411172;
    public static final int MQTT_CONNECTION_LOST = 201411173;

    public final class Intent_Action {
        public static final String CLOSE_ACTION = "meos.closeapp";
        public static final String LOAGOUT_ACTION = "meos.logout";
        public static final String OB_VIERSION_UPDATE = "android.action.version_download";
        public static final String OB_RECIVER_MESSAGE_ACTION = "cn.com.learningbar.action.recevierMessage";
        public static final String OB_RECIVER_NMESSAGE_ACTION = "cn.com.learningbar.action.recevierNMessage";
        public static final String USER_LOGIN_AGAIN = "android.action.user.login.again";
    }

    public static final String PHONE_SYSTEM_INFO = "Android_Phone";
    public static final String PHONE_INFO_SYSTEMTYPE = "TerminalType";
    public static final String PHONE_INFO_SYSTEMVERSION = "SystemVersion";
    public static final String PHONE_INFO_PHONETYPE = "Model";
    public static final String PHONE_INFO_PHONEID = "DeviceID";
    public static final String PHONE_INFO_APPVERSION = "ApplicationVersion";
    public static final String PHONE_INFO_NUMBER = "PhoneNumber";
    public static final String PHONE_SCREEN_SIZE = "PhoneScreenSize";

    public static final String DEFAULT_INTENT_EXTRA_TYPE = "tasktype"; // 任务类型
    public static final String DEFAULT_INTENT_EXTRA_ACTION = "action"; // 动作
    public static final String DEFAULT_INTENT_EXTRA_RESPONSE = "response"; // 响应
    public static final String DEFAULT_LOG_TAG = "open.learningbar";
    public static final String DEFAULT_SDK_LOG_TAG = DEFAULT_LOG_TAG + ".sdk";
    public static final String TASK_LOG_TAG = "cn.com.open.learningbarapp.service.Learntask";
    public static final CharSequence SER_SYS_ERR = "SystemInnerException"; // //
                                                                           // 服务端系统内部错�?
    //

    public static final String METHOD_NAME = "dispatchSafely";

    public static final String VERSION_PATH = "http://www.open.com.cn/topic/down/OpenLearningBar.apk";
    public static final String VERSION_CHECK_TIME_KEY = "versionCheckTime";

    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";

    public static final int COURSE_SORT_BY_NAME = 2;
    public static final int COURSE_SORT_BY_CREATEDATE = 0;
    public static final int COURSE_SORT_BY_ID = 1;
    public static final int COURSE_SORT_BY_URL = 3;

    public static final int HOMEWORK_STATUS_NEW = 1;
    public static final int HOMEWORK_STATUS_SUBMIT_CORRECT = 6;
    public static final int HOMEWORK_STATUS_SUBMIT_UNCORRECT = 2;
    public static final int HOMEWORK_STATUS_SAVED_UNSUBMIT = 4;
    public static final int HOMEWORK_STATUS_FULL = 5;
    // 课件状�?�? �?新课�?2 为已读课�?   
    public static final int COURSE_COURSEWARE_STATE_NEW = 0;
    public static final int COURSE_COURSEWARE_STATE_OLD = 1;

    //  公告分页显示个数
    public static final int NOTICE_SCROLL_COUNT = 15;

    // 下载管理状�?
    public static final int DOWNLOAD_NONE = 0;
    public static final int DOWNLOAD_START = 1;
    public static final int DOWNLOAD_WAIT = 2;
    public static final int DOWNLOAD_DONE = 3;
    public static final int DOWNLOAD_PAUSE = 4;
    public static final int DOWNLOAD_DELETE = 5;
    // 资源文件在数据库中三种第2状�?
    public static final int DOWNLOAD_SECOND_STATUS1 = 1;
    public static final int DOWNLOAD_SECOND_STATUS2 = 2;
    public static final int DOWNLOAD_SECOND_STATUS3 = 3;
    // 文件类型
    public static final int TYPE_JPG = 10010;
    public static final int TYPE_PDF = 10011;
    public static final int TYPE_GIF = 10012;
    public static final int TYPE_OTHER = 10013;

    // 文件下载过程MESSAGE
    public final static int IMAGE_DOWNLOADED = 10001;
    public final static int IMAGE_DOWNLOAD_READY = 10004;
    public final static int IMAGE_DOWNLOAD_FAILED = 10002;
    public final static int IMAGE_DOWNLOAD_DOING = 10003;

    // 评测题型
    public static final String TYPE_HOMEWORK_SINGLE = "SingleChoice";
    public static final String TYPE_HOMEWORK_MULTIPLE = "MultipleChoice";
    public static final String TYPE_HOMEWORK_SUBJECTIVETEXTENTRY = "SubjectiveTextEntry";
    public static final String TYPE_HOMEWORK_COMPOSITE = "CompositeChoice";
    public static final String TYPE_HOMEWORK_EXTENDEDTEXT = "ExtendedText";

    public static final int TYPE_NOTICE = 3;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_DOC = 2;

    public static final int TYPE_COURSEWARE_DOC = 1;
    public static final int TYPE_COURSEWARE_ADJUNCGT = 2;

    // 私信消息类型�?,文字�?,语音�?，图片）

    public static final int MESSAGE_TYPE_TEXT = 20;
    public static final int MESSAGE_TYPE_AUDIO = 21;
    public static final int MESSAGE_TYPE_IMG = 23;
    public static final int MESSAGE_TYPE_VALID_FRIEND = 81;

    // 用户角色
    public static final String USER_ROLE_TEACHER = "teacher";
    public static final String USER_ROLE_STUDENT = "student";

    // SharePreference 背景图片的键�?  
    public static final String SHAREDPREFERENCE_FRIEND_BG_KEY = "frinedbg";

    // 个人信息编辑类型
    public static final String NICKENAME_FLAG = "nickNameflag";
    public static final String EMAIL_FLAG = "emailflag";
    public static final String PHONE_FLAG = "phoneNumberflag";
    public static final String DETAIL_FLAG = "personDetailFlag";

    // handler 的message 类型
    public static interface HANDLER {

        public static final int MESSAGE_STOP = 300001;
        public static final int MESSAGE_SUCCESS = 300002;
        public static final int MESSAGE_PROGRESS = 300003;
        public static final int MESSAGE_START = 300004;
        public static final int MESSAGE_TOTALNUM_DELETE = 300005;
        public static final int MESSAGE_TOTALNUM_UPLOAD = 300006;
        public static final int MESSAGE_WAIT = 300007;
        public static final int MESSAGE_UPLOADING = 300008;
        public static final int MESSAGE_DOWNLOAD_WAIT = 300009;
        public static final int MESSAGE_DOWNLOAD_SUCCESS = 300010;
        public static final int MESSAGE_DOWNLOAD_DATA = 300011;
        public static final int MESSAGE_INT_DOWNLOAD_NOSIZE = 300012;
        public static final int MESSAGE_INT_DOWNLOAD_FILELAGER = 300013;
        public static final int MESSAGE_INT_DOWNLOAD_FAIL = 300014;
        public static final int MESSAGE_INT_DOWNLOAD_SUCCESS = 300015;
        public static final int MESSAGE_TEMP_DOWNLOAD_SUCCESS = 300016;
        public static final int MESSAGE_TEMP_DOWNLOAD_FAIL = 300017;
        public static final int MESSAGE_DOWNLOAD_START = 300018;
        public static final int MESSAGE_DOWNLOAD_ERROR = 300019;
        public static final int MESSAGE_DOWNLOAD_MANAGER_START = 300020;
        public static final int MESSAGE_DOWNLOAD_MANAGER_SUCCESS = 300021;
        public static final int MESSAGE_RESOURSE_DOWNLOAD_WAIT = 300022;
        public static final int MESSAGE_RESOURSE_DOWNLOAD_START = 300023;
        public static final int MESSAGE_RESOURSE_DOWNLOAD_PAUSE = 300024;
        public static final int MESSAGE_DOWNLOAD_SUCCESS_CHANGE = 300025;
        public static final int MESSAGE_DOWNLOAD_MANAGER_TEMP = 300026;
        public static final int MESSAGE_DOWNLOAD_ALL_PAUSE = 300027;
        public static final int MESSAGE_COURSE_STUDY_SUCCESS = 300028;
        public static final int MESSAGE_COURSE_STUDY_ASYNCTASK = 300099;
        public static final int MESSAGE_COURSE_STUDY_UPDATE = 300029;
        public static final int MESSAGE_MANAGER_DOWNLOAD_SUCCESS = 300030;
        public static final int MESSAGE_MANAGER_DOENLOAD_UPDATE = 300031;
        public static final String DOWNLOAD_BROADCAST_START = "cn.com.open.meos.logic.thread.start";
        public static final String DOWNLOAD_BROADCAST_SUCCESS = "cn.com.open.meos.logic.thread.success";
        public static final String DOWNLOAD_BROADCAST_SUCCESS_UPLOAD = "cn.com.open.meos.logic.thread.upload";
        public static final String DOWNLOAD_BROADCAST_ERROR = "cn.com.open.meos.logic.thread.error";
        public static final String SEND_BUNDLE = "bundle";
        public static final int DOWNLOAD_NET_WIFI = 101010;
        public static final int DOWNLOAD_NET_MOBILE = 110101;
        public static final String HANDLER_OBJECT = "handler_object";
        public static final String UPLOAD_MESSAGE_STATUS = "upload_status";
        public static final String UPlOAD_SUCCESS = "upload_success";
        public static final String UPLOAD_PROGRESS = "upload_progress";
        public static final String UPLOAD_START = "upload_start";
        public static final String UPLOAD_ACTION = "cn.com.open.meos.ui.act.apps.UploadAudioService";
        public static final String UPLOAD_ACTIVITY = "cn.com.open.meos.ui.act.apps.UploadManager";
        public static final String UPLOAD_THREAD_STATUS = "upload_thread_success_or_faile";
        public static final String UPLOAD_SUCCESSED = "The upload audio is success";
        public static final String UPLOAD_ACTIVITY_STATUS = "upload_activity_status";
        public static final String UPLOAD_ACTIVITY_STOP_THREAD = "upload_activity_stop_thread";
        public static final String UPLOAD_CURRENT_OBJECT = "upload_current_object";
        public static final String UPLOAD_NOTIFY_NUM = "upload_notify_num";
        public static final String UPLOAD_SERVICE_STOP = "upload_service_stop";
        public static final int PROGRESS_BAR = 100001;
    }

    // 下载线程的�?数量
    public static final int DOWNLOAD_MAX_COUNT = 1;
    // Item类别
    public static final int ITEM_TYPE_T = 1;// 文字
    public static final int ITEM_TYPE_P = 2;// 图片
    public static final int ITEM_TYPE_Q = 3;// 试题
    public static final int ITEM_TYPE_V = 4;// 视频

    /** 资源学习类型 **/
    public static final int STUDY_RESOURCE_MOV = 1;
    public static final int STUDY_RESOURCE_TXT = 2;
    public static final int STUDY_RESOURCE_MP3 = 3;
    public static final int STUDY_RESOURCE_SWF = 4;
    public static final int STUDY_RESOURCE_PIC = 5;
    public static final int STUDY_RESOURCE_OTHER = 10;

    public final static int NETGOOD = 90001; // 资源列表中网络情况良好的
    public final static int NETERROR = 90002;// 资源列表中网络不可用的时�?  
    public final static int NETTHIRD = 90003;// 资源处于下载管理中的时�?
    public final static int TEMPTHREAD = 90004;// 文档资源的临时下�?    // add by szk
    public final static int NET_CONNECT_ERROR_TAG = 40001;// 网络不可用的标志

    // 通过地址上的此字符串来判断是否为附件。（文档功能临时处理�?  
    public final static String SERVER_ADJUNCT_URL = "lms/article";

    // 汉语拼音字母
    public final static String[] CHAR_ARRAY = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    // 用户类型
    public final static int USER_TYPE_ALL = 0;
    public final static int USER_TYPE_TEACHER = 1;
    public final static int USER_TYPE_STUDENT = 2;

    // 发言、评论�?赞跟用户ID有关�?   
    public final class Speak_Comment_By_USEID {
        public static final int SPEAK_ID = 1;
        public static final int COMMENT_ID = 2;
        public static final int FAVORITE_ID = 3;
    }

    // public enum SpeakType {
    // SPEAK_TYPE_SPEAK, // 参与发言
    // SPEAK_TYPE_FIRST_FORWARD, // 原创转发
    // SPEAK_TYPE_SECOND_FORWARD, // 二次转发
    // SPEAK_TYPE_COMMENT, // 评论
    // SPEAK_TYPE_REPLY, // 回复
    // SPEAK_TYPE_MODIFY_SPEAK, // 修改发言
    // SPEAK_TYPE_MODIFY_COMMENT, // 修改评论
    // };

    public final class SpeakType {
        public static final int SPEAK_TYPE_SPEAK = 1; // 参与发言
        public static final int SPEAK_TYPE_FIRST_FORWARD = 2; // 原创转发
        public static final int SPEAK_TYPE_SECOND_FORWARD = 4; // 二次转发
        public static final int SPEAK_TYPE_COMMENT = 8; // 评论
        public static final int SPEAK_TYPE_REPLY = 16; // 回复
        public static final int SPEAK_TYPE_MODIFY_SPEAK = 32; // 修改发言
        public static final int SPEAK_TYPE_MODIFY_COMMENT = 64; // 修改评论
    }

    /**
     * 用户积分�?     * 
     * @ClassName: UserScoreValue
     * @Description: TODO
     * @author zhangchunzhe
     * @date 2013-11-15 下午4:42:02
     * 
     */
    public interface UserScoreValue {
        public static final int SCORE_VALUE_LOGIN = 5;
        public static final int SCORE_VALUE_SEEVIDEO = 1;
        public static final int SCORE_VALUE_DOWNLOADVIDEO = 10;
        public static final int SCORE_VALUE_HOMEWORK_SUBMIT = 5;
        public static final int SCORE_VALUE_DOWNLOADPDF = 2;
        public static final int SCORE_VALUE_THEME_ATTENTION = 10;
        public static final int SCORE_VALUE_SPEAK = 3;
        public static final int SCORE_VALUE_SEND_MSG = 1;
        public static final int SCORE_VALUE_TOPIC = 1;
    };

    /**
     * 用户的积分类�?     * 
     * @ClassName: UserScoreType
     * @Description: TODO
     * @author zhangchunzhe
     * @date 2013-11-15 下午4:41:49
     * 
     */
    public interface UserScoreType {
        public static final String SCORE_TYPE_LOGIN = "login";
        public static final String SCORE_TYPE_SEEVIDEO = "lookVideo";
        public static final String SCORE_TYPE_DOWNLOADVIDEO = "downloadVideo";
        public static final String SCORE_TYPE_HOMEWORK_SUBMIT = "submitHomework";
        public static final String SCORE_TYPE_DOWNLOADPDF = "downloadDocument";
        public static final String SCORE_TYPE_THEME_ATTENTION = "followTopic";
        public static final String SCORE_TYPE_SPEAK = "speak";
        public static final String SCORE_TYPE_SEND_MSG = "sendMessage";
        public static final String SCORE_TYPE_TOPIC = "publishTopic";
    };

    /**
     * 用户行为统计类型定义
     * 
     * @ClassName: UserActionCountType
     * @Description: TODO
     * @author zhangchunzhe
     * @date 2013-11-15 下午4:42:58
     * 
     */
    public interface UserActionCountType {
        public static final int ACTION_TYPE_NOTICE_READ = 1;
        public static final int ACTION_TYPE_DOC_READ = 2;
        public static final int ACTION_TYPE_HOMEWORK_DO = 3;
        public static final int ACTION_TYPE_HOMEWORK_SUBMIT = 4;
        public static final int ACTION_TYPE_HOMEWORK_SAVE = 5;
        public static final int ACTION_TYPE_VIDEO_LOOK = 6;
        public static final int ACTION_TYPE_VIDEO_DOWNLOAD = 7;
        public static final int ACTION_TYPE_TIPIC_READ = 8;
        public static final int ACTION_TYPE_TOPIC_SPEAK = 9;
        public static final int ACTION_TYPE_TOPIC_COMMENT = 10;
        public static final int ACTION_TYPE_TOPIC_DELETE = 11;
        public static final int ACTION_TYPE_OPEN_VIDEO = 12;
    }

    // http的请求方�?  
    public static final int HTTP_GET = 1;
    public static final int HTTP_POST = 2;

    public static final String CONNECTSTRING = "tcp://10.96.142.93:1883"; // 即使通讯服务端ip
    /**
     * 即时通讯 ：�?知客户端获取新消�?     */
    public static final int NEW_MESSAGE = 20131101;
    /**
     * 即时通讯 ：处理服务端发�?的消�?     */
    public static final int DEAL_NEW_MESSAGE = 20131102;
    /**
     * 即时通讯 ：处理服务端发�?的消�?     */
    public static final int DEAL_TOKEN_LOCKED = 20140101;
    /**
     * 即时通讯 ：客户端发�?消息是否成功
     */
    public static final int RESPONSE_SEND_MESSAGE = 20131120;

    /**
     * 即时通讯：更新消�?     */
    public static final int UPDATE_MESSAGE_CHAT = 20131106;
    /**
     * 即时通讯：更新未读消息数�?     */
    public static final int UPDATE_MESSAGE_NUMBER = 20131107;
    /**
     * 即时通讯：更新未读消息列表显示页�?     */
    public static final int UPDATE_MESSAGE_LETTER = 20131112;
    /**
     * 即时通讯：更新未读系统消息coursemain页面
     */
    public static final int UPDATE_SYSN_MORE = 20140718;
    /**
     * 即时通讯：更新未读系统消息列表显示页�?     */
    public static final int UPDATE_SYSN_LETTER = 20140719;
    /**
     * 即时通讯：聊天详细页�?     */
    public static final String FROM_CHAT_VIEW = "20131108";
    /**
     * system notice �?     */
    public static final String FROM_NOTICE_VIEW = "20140704";
    /**
     * 即时通讯：私信未读消息数量显示页�?     */
    public static final String FROM_UNREAD_NUMBER_VIEW = "20131109";
    /**
     * 即时通讯：私信未读消息列表显示页�?     */
    public static final String FROM_LETTER_LIST = "20131111";
    /**
     * 即时通讯：直接又MessageHandler处理消息
     */
    public static final String FROM_MESSAEHANDLER = "20131204";

    public static final int CHAT_SERVICE_CLOSED = 20141030;

    // ----------------- 统计用户行为 start --------------------------
    public static final String LEARNBAR_HOME_PV = "learnbar_home_pv";
    public static final String LEARNBAR_COURSE_PV = "learnbar_course_pv";
    public static final String LEARNBAR_GROUP_PV = "learnbar_group_pv";
    public static final String LEARNBAR_PERSONAL_PV = "learnbar_personal_pv";
    public static final String LEARNBAR_MORE_PV = "learnbar_more_pv";
    public static final String LEARNBAR_VIDEO_PV = "pv_video";
    public static final String LEARNBAR_PUSH_MSG = "learnbar_push_msg";
    public static final String LEARNBAR_PUSH_MSG_VIEW = "learnbar_push_msg_view";
    public static final String LEARNBAR_USER_ACTIONGURL = "http://mpv.open.com.cn/razor/";

    // ----------------- 统计用户行为 end --------------------------

    /**
     * 
     * @ClassName: SpeakStatus
     * @Description: TODO
     * @author zhangchunzhe
     * @date 2013-12-9 上午10:18:38
     * 
     */
    public class SpeakStatus {
        public static final int OPEN = 2; // �?��
        public static final int TEST = 3; // 审核�?      
        public static final int ISOLATION = 4; // 屏蔽
        public static final int CLOSE = 5; // 关闭
        public static final int LOCK_AUDIT = 6;// 审核锁定
        public static final int LOCK_SHIELD = 7;// 屏蔽锁定
        public static final int DELETE = 9;// 删除
    };

    public class MessageFromType {
        public static final String FRIEND = "2";
        public static final String MINE = "1";
    }

    public class MessageReadStatus {
        public static final int RECEIVE = 1;// 消息未读
        public static final int SUCCESS = 2;// 消息已读
    }

    public class MessageSendStatus {
        public static final int SEND = 1; // 发�?�?     
        public static final int SUCCESS = 2;// 发�?成功
        public static final int ERROR = 3;// 发�?失败

    }

    // ----------------- 新消息设�?start --------------------------
    public static final String IS_SHOW_NOTICE_SP = "IS_SHOW_NOTICE_SP";
    public static final String IS_SHOW_VOICE_SP = "IS_SHOW_VOICE_SP";
    public static final String IS_SHOW_VIBRATTION_SP = "IS_SHOW_VIBRATTION_SP";
    public static boolean IS_SHOW_NOTICE = true;
    public static boolean IS_SHOW_VOICE = true;
    public static boolean IS_SHOW_VIBRATTION = true;
    // ----------------- 新消息设�?end --------------------------

    /*
     * 消息类型 1（系统相关） 2（私信相关） 3（发�?��关） 4（评论相关） 5（好友相关） 6（用户相关）
     */
    public static final int MES_ABOUT_SYSTEM = 1;
    public static final int MES_ABOUT_PRIVATE = 2;

    // public static final int MES_ABOUT_FRIEND = 5;

    /*
     * 消息编码 （系统相关）1 系统通知 2系统消息 （私信相关）20 文本 21 语音 22 视频 23图片 （发�?��关）40 待审核发�?��转发�?     * 41 屏蔽发言（转发） （评论相关）60 待审核评论（回复�?61 屏蔽评论（回复） （好友相关）80 接收到好友申�?81 好友验证通过 82
     * 好友验证不�?�?（用户相关）90 该用户设置了屏蔽规则 91 该用户取消了屏蔽规则 92 该用户被删除 93 该用户被禁用
     */

    /**
     * * DELETE("删除", 9), OPEN("�?��", 2), TO_AUDIT("待审�?, 3), SHIELD("屏蔽", 4),
     * CLOSE("关闭", 5), LOCK_AUDIT("审核锁定", 6), LOCK_SHIELD("屏蔽锁定", 7);
     * 
     * @ClassName: ThemeState
     * @Description: TODO
     * @author zhangchunzhe
     * @date 2013-12-9 上午10:24:36
     * 
     */
    public interface ThemeState {

    }

}
