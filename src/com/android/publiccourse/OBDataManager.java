package com.android.publiccourse;

import com.android.publiccourse.OBDataManager.LoginInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.provider.BaseColumns;
import android.util.Log;

public class OBDataManager {
	private DBHelper mDBHelper;

	private final static String DB_NAME = "learnbar.db";

	private final static int VERSION = 4;

	private Context mContext;

	private SQLiteDatabase mDatabase;

	public OBDataManager(Context context) {
		mContext = context;
		mDBHelper = new DBHelper(context, DB_NAME, null, VERSION);
		mDatabase = mDBHelper.getWritableDatabase();
	}

	public interface LoginInfo extends BaseColumns {
		public static final String TABLE_NAME = "logininfo";

		public static final String USER_ID = "u_id";
		public static final String USER_BASE_ID = "u_base_id";
		public static final String USER_ROLE = "u_role";
		public static final String USER_NAME = "u_name";
		public static final String USER_PWD = "u_pwd";
		public static final String USER_STUDENT_CODE = "u_studentcode";
		public static final String USER_PROFESSION_NAME = "u_professionName";
		public static final String USER_PROFESSION_STATE = "u_professionState";
		public static final String USER_ISSAVED = "u_issaved";
		public static final String USER_ICON = "u_icon";
		public static final String USER_NICENAME = "u_nick";
		public static final String USER_TOKEN = "u_token";

		public static final String CREATE_TABLE = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s text,%s text, %s text, %s text,%s text,%s text, %s text, %s text,%s text, %s text, %s text,%s text);",
						TABLE_NAME, USER_ID, USER_BASE_ID, USER_NAME, USER_PWD,
						USER_ROLE, USER_STUDENT_CODE, USER_PROFESSION_NAME,
						USER_PROFESSION_STATE, USER_ISSAVED, USER_ICON,
						USER_NICENAME, USER_TOKEN);

		// 数据库版�? �?版本3 添加字段
		public static final String ALERT_TABLE_USERID = "ALERT TABLE ["
				+ TABLE_NAME + "] add [" + USER_ID + "] varchar(300);";

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public interface DownloadManager extends BaseColumns {
		public static final String TABLE_NAME = "DownloadManager";
		public static final String USER_ID = "user_id";
		public static final String COURSER_ID = "courser_id";
		public static final String COURSER_NAME = "course_name";
		public static final String STUDENT_CODE = "student_code";
		public static final String FILE_ID = "file_id";
		public static final String FILE_URL = "file_url";
		public static final String FILE_NAME = "file_name";
		public static final String FILE_TYPE = "file_type";
		public static final String FILE_SIZE = "file_size";
		public static final String FILE_START_POS = "file_start_pos";
		public static final String FILE_ENd_POS = "file_end_pos";
		public static final String FILE_STATUS = "file_status";
		public static final String FILE_SECOND_STATUS = "file_second_status";
		public static final String UPLOAD_DATE = "upload_date";
		public static final String PROGRESS_PAR = "progress_par";
		public static final String REMAIN_SIZE = "remain_size";
		public static final String REMAIN_TIME = "remain_time";
		public static final String REMAIN_RATE = "remain_rate";
		public static final String DATA_SIZE = "data_size";

		public static final String CREATE_TABLE = String
				.format("CREATE TABLE IF NOT EXISTS %s(%s integer primary key autoincrement,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s integer,%s integer,%s integer,%s integer,%s integer,%s integer,%s text,%s integer,%s integer,%s integer,%s integer,%s integer);",
						TABLE_NAME, _ID, USER_ID, COURSER_ID, COURSER_NAME,
						STUDENT_CODE, FILE_ID, FILE_URL, FILE_NAME, FILE_TYPE,
						FILE_SIZE, FILE_START_POS, FILE_ENd_POS, FILE_STATUS,
						FILE_SECOND_STATUS, UPLOAD_DATE, PROGRESS_PAR,
						REMAIN_SIZE, REMAIN_TIME, REMAIN_RATE, DATA_SIZE);
		public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;

		// public static final String ALERT_TABLE_CLUM = "alter table ["
		// + TABLE_NAME + "] add [" + UPDATE_VIEW + "] nvarchar(300)";
		//
		// public static final String UPDATE_TABLE_CLUM = "update " + TABLE_NAME
		// + " set " + UPDATE_VIEW + " = 'true'";
	}

	// 具体课程的积�?	
	public interface SpicificCourse extends BaseColumns {
		public static final String TABLE_NAME = "SpicificCourse";

		public static final String USER_ID = "u_id";
		public static final String STUDENT_CODE = "s_code";
		public static final String COURESE_ID = "c_id";
		public static final String INCOME_COUNT = "i_count";
		public static final String TAG_TYPE = "t_type";
		public static final String START_TIME = "s_time";
		public static final String LOCK_TIME = "l_time";
		public static final String NEAR_CLICK = "n_click";
		public static final String VIDEO_ID = "v_id";

		public static final String CREATE_TABLE = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s integer primary key autoincrement,%s text, %s text, %s text,%s integer,%s integer,%s integer,%s integer,%s integer,%s text);",
						TABLE_NAME, _ID, USER_ID, STUDENT_CODE, COURESE_ID,
						INCOME_COUNT, TAG_TYPE, START_TIME, LOCK_TIME,
						NEAR_CLICK, VIDEO_ID);

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS"
				+ TABLE_NAME;
	}

	// 记录�?��聊天消息
	public interface ChatMessageRecord extends BaseColumns {

		public static final String TABLE_NAME = "MessageRecord";

		public static final String SESSION_ID = "session_id";
		public static final String TO_USER = "to_user_id";
		public static final String FROM_USER = "from_user_id";
		public static final String FROM_USER_NAME = "from_user_name";
		public static final String FROM_USER_FACE = "from_user_face";
		public static final String READ_STATUS = "c_status";
		public static final String SEND_STATUS = "send_status";
		public static final String CODE = "c_code";
		public static final String TYPE = "c_type";
		public static final String IMAGEURL = "c_iurl";
		public static final String AUDIOURL = "c_aurl";
		public static final String AUDIOPATH = "c_apath";
		public static final String IMAGEPATH = "c_ipath";
		public static final String RECEIVETIME = "r_time";
		public static final String LOCALTIME = "l_time";
		public static final String AUDIODURATION = "c_duration";
		public static final String WHERECOME = "c_where";
		public static final String CONTENT = "c_content";

		// public static final String UNIQUE_NAME = "friend_validation";

		public static final String CREATE_TABLE = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s integer primary key autoincrement, %s text, %s text,%s text, %s text, %s text, %s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text);",
						TABLE_NAME, _ID, SESSION_ID, TO_USER, FROM_USER,
						FROM_USER_NAME, FROM_USER_FACE, READ_STATUS,
						SEND_STATUS, CODE, TYPE, IMAGEURL, AUDIOURL,AUDIOPATH, IMAGEPATH,
						RECEIVETIME, LOCALTIME,AUDIODURATION, WHERECOME, CONTENT);

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS"
				+ TABLE_NAME;
		// 创建唯一索引
		// public static final String CREATE_UNIQUE = String
		// .format("CREATE UNIQUE INDEX IF NOT EXISTS %s  ON %s (%s ,%s);",
		// UNIQUE_NAME,TABLE_NAME,SESSION_ID,CODE);
		// public static final String DROP_UNIQUE = "DROP INDEX IF EXISTS"
		// + UNIQUE_NAME;
	}

	/**
	 * 
	 * 系统通知消息�?	 * @ClassName: OBLFriendMessage
	 * @Description: TODO
	 * @author wangpan
	 * @date 2014-7-3 上午11:06:44
	 * 
	 */
	public interface NoticeMessageRecord extends BaseColumns{
		
		public static final String TABLE_NAME = "NoticeMessageRecord";
		
		public static final String NID = "id";
		public static final String CODE = "code";
		public static final String TYPE = "type";
		public static final String MMSGTITTLE = "title"; 
		public static final String READ_STATUS = "status";
		public static final String MMSGBEGINTIME = "b_time";
		public static final String MMSGENDTIME = "s_time";
		public static final String MMSGSOURCE= "source";
		public static final String MMSGCREATESENDNAME = "c_s_name";
		public static final String MMSGCREATEMAN = "c_man";
		public static final String MMSGCREATEDATE= "c_date";
		public static final String MMSGCONTENT= "c_content";
		public static final String MMSGSESSIONID ="sessionid";
		public static final String MMSGPOSTTIME = "p_time";
		
		public static final String CREATE_TABLE = String
		.format("CREATE TABLE IF NOT EXISTS %s (%s integer primary key autoincrement, %s text,%s text, %s text,%s text, %s text,%s text, %s text, %s text, %s text,%s text,%s text,%s text,%s text,%s text);",
				TABLE_NAME, _ID, NID, CODE, TYPE,
				MMSGTITTLE, READ_STATUS, MMSGBEGINTIME,
				MMSGENDTIME, MMSGSOURCE, MMSGCREATESENDNAME, MMSGCREATEMAN, MMSGCREATEDATE,MMSGCONTENT,MMSGSESSIONID,MMSGPOSTTIME);

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS"
		+ TABLE_NAME;
	}
	
	/**
	 * 好友之间关系�?	 * 
	 * @ClassName: FriendInfo
	 * @Description: TODO
	 * @author zhangchunzhe
	 * @date 2013-12-9 上午11:06:44
	 * 
	 */
	public interface FriendInfo extends BaseColumns {

		public static final String TABLE_NAME = "FriendInfo";

		// 好友的ID
		public static final String FRIEND_USER_ID = "friend_id";
		// 好友之间
		public static final String FRIEND_SESSION_ID = "friend_session_id";
		public static final String FRIEND_USER_NAME = "friend_name";
		public static final String FRIEND_USER_URL = "friend_icon_url";
		public static final String FRIEND_USER_LOCAL_URL = "friend_user_local_url";

		public static final String CREATE_TABLE = String
				.format("CREATE TABLE IF NOT EXISTS %s (%s integer primary key autoincrement, %s text, %s text, %s text, %s text,%s text);",
						TABLE_NAME, _ID, FRIEND_USER_ID, FRIEND_USER_NAME,
						FRIEND_SESSION_ID, FRIEND_USER_URL,
						FRIEND_USER_LOCAL_URL);

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS"
				+ TABLE_NAME;

	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			// db.execSQL(CourseInfo.CREATE_TRIGGER_INSERT);
			db.execSQL(LoginInfo.CREATE_TABLE);
			db.execSQL(DownloadManager.CREATE_TABLE);
			db.execSQL(SpicificCourse.CREATE_TABLE);
			db.execSQL(ChatMessageRecord.CREATE_TABLE);
			db.execSQL(NoticeMessageRecord.CREATE_TABLE);
			// db.execSQL(ChatMessageRecord.CREATE_UNIQUE);
			db.execSQL(FriendInfo.CREATE_TABLE);

		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			// 1.1版本更新�?.2版本
			if (oldVersion == 1 && newVersion == 2) {
				db.execSQL(SpicificCourse.CREATE_TABLE);
			}


			// 1.1版本更新�?.0版本
			if (oldVersion == 2 && newVersion == 3) {
				db.execSQL(SpicificCourse.CREATE_TABLE);
				db.execSQL(LoginInfo.DROP_TABLE);
				db.execSQL(LoginInfo.CREATE_TABLE);
				db.execSQL(SpicificCourse.CREATE_TABLE);
				db.execSQL(ChatMessageRecord.CREATE_TABLE);
				db.execSQL(FriendInfo.CREATE_TABLE);
			}
			// 1.0 版本升级�?.0版本
			if (oldVersion == 1 && newVersion == 3) {
				db.execSQL(LoginInfo.DROP_TABLE);
				db.execSQL(LoginInfo.CREATE_TABLE);
				db.execSQL(SpicificCourse.CREATE_TABLE);
				db.execSQL(ChatMessageRecord.CREATE_TABLE);
				db.execSQL(FriendInfo.CREATE_TABLE);
			}
			if(oldVersion == 1 && newVersion == 4){
				db.execSQL(LoginInfo.DROP_TABLE);
				db.execSQL(LoginInfo.CREATE_TABLE);
				db.execSQL(SpicificCourse.CREATE_TABLE);
				db.execSQL(ChatMessageRecord.CREATE_TABLE);
				db.execSQL(NoticeMessageRecord.CREATE_TABLE);
				
				db.execSQL(FriendInfo.CREATE_TABLE);
			}
			if(oldVersion == 2 && newVersion == 4){
				db.execSQL(LoginInfo.DROP_TABLE);
				db.execSQL(LoginInfo.CREATE_TABLE);
				db.execSQL(SpicificCourse.CREATE_TABLE);
				db.execSQL(ChatMessageRecord.CREATE_TABLE);
				db.execSQL(NoticeMessageRecord.CREATE_TABLE);
				db.execSQL(FriendInfo.CREATE_TABLE);
			}
			if(oldVersion == 3 && newVersion == 4){
	
				db.execSQL(NoticeMessageRecord.CREATE_TABLE);
		
			}
		}
	}

	public Cursor queryUnReadMsgBySessionId(String sessionID) {
		Cursor rawQuery = mDatabase
				.rawQuery(
						"select * from MessageRecord where c_status = ? and session_id = ?",
						new String[] { "1", sessionID });
		return rawQuery;
	}

	public Cursor qureyUnReadMessageCount(String status, String toUser) {
		Cursor rawQuery = mDatabase
				.rawQuery(
						"select * from MessageRecord where c_status = ? and to_user_id = ?",
						new String[] { status, toUser });
		return rawQuery;

	}

	public Cursor qureyUnReadSysNCount(String status, String sessionid) {
		Cursor rawQuery = mDatabase
				.rawQuery(
						"select * from NoticeMessageRecord where status = ? and sessionid = ?",
						new String[] { status, sessionid });
		return rawQuery;

	}
	
	public Cursor qureySysNoticeSort(String sessionID) {
		Cursor rawQuery = mDatabase.rawQuery(
				"select * from NoticeMessageRecord where sessionid = ? order by p_time DESC ",
				new String[] { sessionID });
      return rawQuery;
      }
	
	
	public Cursor qureyRecentMsgSort(String toUser) {
		Cursor rawQuery = mDatabase
				.rawQuery(
						"select * from MessageRecord where to_user_id = ? group by session_id order by r_time DESC ",
						new String[] { toUser });
		return rawQuery;
	}

	public Cursor qureyRecentMsgLikeSort(String toUser, String likeName) {
		Cursor rawQuery = mDatabase
				.rawQuery(
						"select * from MessageRecord where to_user_id = ? and from_user_name like ? group by session_id order by r_time DESC ",
						new String[] { toUser, likeName });
		return rawQuery;
	}

	public void updateReadStatus(String sessionID) {
		mDatabase.execSQL(
				"update MessageRecord set c_status = 2 where session_id = ?",
				new String[] { sessionID });

	}

	public void updateReadSysNoticeStatus(String msgId) {
		mDatabase.execSQL(
				"update NoticeMessageRecord set status = 2 where id = ?",
				new String[] { msgId });

	}
	
	public int deleteMsgBatch(String[] args) {
		mDatabase.beginTransaction();
		try {
			for (int i = 0; i < args.length; i++) {
				String where = OBDataManager.ChatMessageRecord.SESSION_ID
						+ "='" + args[i] + "'";
				mDatabase.delete(OBDataManager.ChatMessageRecord.TABLE_NAME,
						where, null);
			}
			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();

		}
		return 0;
	}

	// public long replace(String table, String nullColumnHack, ContentValues
	// values){
	//
	// if (isNull(mDatabase)) {
	// return mDatabase.replace(table, nullColumnHack, values);
	// }
	// return 0;
	// }

	public Cursor qureyAndSort(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		Cursor cousor = null;
		if (isNotNull(mDatabase)) {
			cousor = mDatabase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy, limit);
		}
		return cousor;

	}

	public Cursor qurey(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cousor = null;
		if (isNotNull(mDatabase)) {
			cousor = mDatabase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
		}
		return cousor;

	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		if (isNotNull(mDatabase)) {
			return mDatabase.delete(table, whereClause, whereArgs);
		}
		return 0;

	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		if (isNotNull(mDatabase)) {
			return mDatabase.insert(table, nullColumnHack, values);
		}
		return 0;

	}

	/**
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		if (isNotNull(mDatabase)) {
			return mDatabase.update(table, values, whereClause, whereArgs);
		}
		return 0;

	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	public boolean isNotNull(SQLiteDatabase db) {
		return db == null ? false : true;
	}

}
