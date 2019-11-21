package com.android.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.Dtcourse;

public class DtcourseService {
	private DBOpenHelper databaseHelper;
    private Context context;
	private SQLiteDatabase db;
    private DatabaseManager databaseManager;
	public DtcourseService(Context context) {
		this.context = context;
		databaseHelper = DBOpenHelper.getInstance(context);
		databaseManager=DatabaseManager.getInstance(databaseHelper);
	}

	public void save(String c,String cno)
	{
		long rowid;
		if(c==null)
		{
			db = databaseManager.getWritableDatabase();
			ContentValues cValue = new ContentValues();
			cValue.put("id", "null");
			cValue.put("name", "null");
			cValue.put("cno", cno);
			rowid = db.insert("dtcourse", null, cValue);
		}
		databaseManager.closeDatabase();
	}
	public void save(Dtcourse c) {
		long rowid;
		db = databaseManager.getWritableDatabase();
		ContentValues cValue = new ContentValues();
		cValue.put("id", c.getid());
		cValue.put("name", c.getname());
		cValue.put("cno", c.getcno());
		rowid = db.insert("dtcourse", null, cValue);
		//db.close();
		databaseManager.closeDatabase();
		
	}

	public void update(Dtcourse c) {
		db = databaseManager.getWritableDatabase();
	    //db.close();
		databaseManager.closeDatabase();
	}


	public ArrayList<Dtcourse> find() {
		ArrayList<Dtcourse> cs = new ArrayList<Dtcourse>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id,name,cno from dtcourse", null);
        if (cursor != null) {
			while (cursor.moveToNext()) {
				Dtcourse c = new Dtcourse();
				c.setid(cursor.getString(cursor.getColumnIndex("id")));
				c.setname(cursor.getString(cursor.getColumnIndex("name")));
				c.setcno(cursor.getString(cursor.getColumnIndex("cno")));
				cs.add(c);
			}
		}
		cursor.close();
		//db.close();
		databaseManager.closeDatabase();
		return cs;
	}

	public ArrayList<Dtcourse> findC(String id) {
		ArrayList<Dtcourse> cs = new ArrayList<Dtcourse>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select cno,name  from dtcourse where id=? ", new String[]{id});
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Dtcourse c = new Dtcourse();

				c.setid(cursor.getString(cursor.getColumnIndex("cno")));
				c.setname(cursor.getString(cursor.getColumnIndex("name")));
			 
				cs.add(c);
			}
		}
		cursor.close();
	    //db.close();
		databaseManager.closeDatabase();
		return cs;
	}

		public ArrayList<Dtcourse> find(String cno) {
			ArrayList<Dtcourse> cs = new ArrayList<Dtcourse>();
			db = databaseManager.getWritableDatabase();
			Cursor cursor = db.rawQuery("select id,name  from dtcourse where cno=? ", new String[]{cno});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					Dtcourse c = new Dtcourse();

					c.setid(cursor.getString(cursor
							.getColumnIndex("id")));
					c.setname(cursor.getString(cursor
							.getColumnIndex("name")));
				 
					cs.add(c);
				}
			}
			cursor.close();
		   // db.close();
			databaseManager.closeDatabase();
			return cs;
		}

	public void delete(String id) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from dtcourse where id = ?",
				new Object[] { id });
		databaseManager.closeDatabase();
	 
	}

		public void deleteAll() {
			db = databaseManager.getWritableDatabase();
			db.execSQL("delete from dtcourse ");
			//db.close();
			databaseManager.closeDatabase();
		}
}
