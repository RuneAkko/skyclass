package com.android.sql;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	

	private static final String DBNAME = "vod5.db";
	private static final int version = 1;
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DBOpenHelper(Context context) {
		super(context,DBNAME, null, version);
	}
	 private static DBOpenHelper mInstance = null;
	  public  static DBOpenHelper getInstance(Context context) {  
	        if (mInstance == null) {  
	            mInstance = new  DBOpenHelper(context);  
	        }  
	        return mInstance;  
	    };  


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//ע�ⵥ��ƿа create
		db.execSQL("CREATE TABLE IF NOT EXISTS courses (studentCode varchar(30),courseid varchar(30),courseCode varchar(30) primary key,courseName varchar(30))");
		db.execSQL("CREATE TABLE IF NOT EXISTS news (newsID varchar(30) primary key,newsTitle varchar(30),newsKeyword varchar(30),newsDescription varchar(30))");
		db.execSQL("CREATE TABLE IF NOT EXISTS newsData (newsContent varchar(30))");
		db.execSQL("CREATE TABLE IF NOT EXISTS dtcourse (id varchar(30),name varchar(30),cno varchar(30))");
		db.execSQL("CREATE TABLE IF NOT EXISTS courseDoc (courseid varchar(30),ArticleID varchar(30) primary key,ArticleTypeName varchar(50),MainHead varchar(50))");
		db.execSQL("CREATE TABLE IF NOT EXISTS dtplaycourse (id varchar(30) primary key,name varchar(200),svpath varchar(200),svpath_abs varchar(200),cno varchar(30))");
		//db.execSQL("CREATE TABLE IF NOT EXISTS dtplaycourse (id varchar(30) primary key,name varchar(200),svpath varchar(200),cno varchar(30))");
		//db.execSQL("CREATE TABLE IF NOT EXISTS dtplaycourse (cid varchar(30),id varchar(30) primary key,name varchar(30),svpath varchar(30),structureid varchar(30))");
		db.execSQL("CREATE TABLE IF NOT EXISTS dtstructure (id varchar(30) primary key,courseid varchar(30),name varchar(30))");
		db.execSQL("CREATE TABLE IF NOT EXISTS operation (id integer primary key autoincrement,operationcode varchar(15),studentcode varchar(30),courseid varchar(30),path varchar(30),time varchar(30),len integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS studyProcess (GradeTotal varchar(30),GradeSum varchar(30),GraduatelowlimDays varchar(30),GraduatehighlimDays varchar(30),StudyDays varchar(30))");
		String sql="CREATE TABLE IF NOT EXISTS "
				+ "onlinecourse(_id INTEGER primary key AUTOINCREMENT,courseId INTEGER,courseName varchar(50) not null,collegeName varchar(50) not null,"
				+ " teacherName varchar(50) not null,"
				+ "className INTEGER,startTime varchar(100),endTime varchar(100),videoUrl varchar(80),screenUrl varchar(80),server varchar(30),location varchar(30))";
        db.execSQL(sql);
        
        String sql1="CREATE TABLE IF NOT EXISTS "
        		+ "noonlinecourse (_id INTEGER primary key AUTOINCREMENT,courseId INTEGER,courseName varchar(50) not null,collegeName varchar(50) not null,"
        		+ "teacherName varchar(50) not null,"
				+ "className INTEGER,startTime varchar(50),endTime varchar(50))";
        db.execSQL(sql1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS courses");
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS newsData");
		db.execSQL("DROP TABLE IF EXISTS dtcourse");
		db.execSQL("DROP TABLE IF EXISTS courseDoc");
		db.execSQL("DROP TABLE IF EXISTS dtplaycourse");
		db.execSQL("DROP TABLE IF EXISTS dtstructure");
		db.execSQL("DROP TABLE IF EXISTS operation");
		db.execSQL("DROP TABLE IF EXISTS onlinecourse");
		db.execSQL("DROP TABLE IF EXISTS noonlinecourse");
		onCreate(db);

	}
	@Override
	public  void close() {
		// TODO Auto-generated method stub
		
		super.close();
	}

}
