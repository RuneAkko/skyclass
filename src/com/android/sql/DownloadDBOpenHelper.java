package com.android.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author Administrator
 *
 */
public class DownloadDBOpenHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "download1.db";
	private static final int VERSION = 1;
	
	/**
	 *
	 * @param context
	 */
	public DownloadDBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	//
	 private static DownloadDBOpenHelper mInstance;  
	  
	    public synchronized static DownloadDBOpenHelper getInstance(Context context) {  
	        if (mInstance == null) {  
	            mInstance = new DownloadDBOpenHelper(context);  
	        }  
	        return mInstance;  
	    };  
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
		
		////////////////////////////////////////////////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS filelog (id integer primary key autoincrement, downpath varchar(100), isOver INTEGER,downlength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS filedownlog");
		db.execSQL("DROP TABLE IF EXISTS filelog");
		onCreate(db);
	}
}