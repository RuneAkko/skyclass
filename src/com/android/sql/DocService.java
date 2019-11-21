package com.android.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.Dtcourse;
import com.android.domain.courseDoc;

public class DocService {
	private DBOpenHelper databaseHelper;
	private DatabaseManager databaseManager;
    private Context context;
	private SQLiteDatabase db;
   
	public DocService(Context context) {
          this.context=context;
          databaseHelper = DBOpenHelper.getInstance(context);
		  databaseManager=DatabaseManager.getInstance(databaseHelper);
		 
	}

	/*public void save(String c,String cno)
	{
		long rowid;
		if(c==null)
		{
			db = databaseHelper.getWritableDatabase();
			ContentValues cValue = new ContentValues();
			cValue.put("courseId", "null");
			cValue.put("name", "null");
			rowid = db.insert("courseDoc", null, cValue);
		}
		db.close();
	}*/
	public void save(courseDoc c,String id) {
		Long rowid;
		db = databaseManager.getWritableDatabase();
		ContentValues cValue = new ContentValues();
		cValue.put("courseid", id);
		cValue.put("ArticleID", c.getArticleID());
		cValue.put("ArticleTypeName", c.getArticleTypeName());
		cValue.put("MainHead", c.getMainHead());
		rowid = db.insert("courseDoc", null, cValue);
		databaseManager.closeDatabase();
		
	}

	public void update(courseDoc c) {
		db = databaseManager.getWritableDatabase();
		databaseManager.closeDatabase();
	}

	public ArrayList<courseDoc> find() {
		ArrayList<courseDoc> cs = new ArrayList<courseDoc>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select courseid,ArticleID,MainHead,ArticleTypeName from courseDoc",
				null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				courseDoc c = new courseDoc();
				
				c.setcourseid(cursor.getString(cursor
						.getColumnIndex("courseid")));

				c.setArticleID(cursor.getString(cursor
						.getColumnIndex("ArticleID")));
				c.setArticleTypeName(cursor.getString(cursor
						.getColumnIndex("ArticleTypeName")));
				c.setMainHead(cursor.getString(cursor
						.getColumnIndex("MainHead")));
         
				cs.add(c);
			}
		}
		cursor.close();
		databaseManager.closeDatabase();
		return cs;
	}
	public ArrayList<courseDoc> findC(String id) {
		ArrayList<courseDoc> cs = new ArrayList<courseDoc>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select courseid,ArticleID,MainHead,ArticleTypeName from courseDoc where courseid=? ",
				new String[]{id});
		if (cursor != null) {
			while (cursor.moveToNext()) {
				courseDoc c = new courseDoc();

				c.setcourseid(cursor.getString(cursor
						.getColumnIndex("courseid")));;
				c.setArticleID(cursor.getString(cursor
						.getColumnIndex("ArticleID")));
				c.setArticleTypeName(cursor.getString(cursor
						.getColumnIndex("ArticleTypeName")));
				c.setMainHead(cursor.getString(cursor
						.getColumnIndex("MainHead")));
			 
				cs.add(c);
			}
		}
		cursor.close();
		databaseManager.closeDatabase();
		return cs;
	}
	public void delete(String id) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from courseDoc where courseid = ?",
				new Object[] { id });
		databaseManager.closeDatabase();
	 
	}
		public void deleteAll() {
			db = databaseManager.getWritableDatabase();
			db.execSQL("delete from courseDoc ");
			databaseManager.closeDatabase();
		}
}
