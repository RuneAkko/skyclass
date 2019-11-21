package com.android.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.*;

public class DtplaycourseService {
	private DBOpenHelper databaseHelper;
	private Context context;
	private SQLiteDatabase db;
    private DatabaseManager databaseManager;
	public DtplaycourseService(Context context) {
		this.context = context;
		databaseHelper = DBOpenHelper.getInstance(context);
        databaseManager=DatabaseManager.getInstance(databaseHelper);
	}

	public void save(Dtplaycourse c) {
		db = databaseManager.getWritableDatabase();
		//db.execSQL("insert into dtplaycourse(id,name,svpath) values (?,?,?)", new Object[] { c.getid(), c.getname(), c.getsvpath()});
		db.execSQL("insert into dtplaycourse(id,name,svpath,svpath_abs) values (?,?,?,?)", new Object[] { c.getid(), c.getname(), c.getsvpath(),c.getsvpath_abs()});
		//db.execSQL("insert into dtplaycourse(cid,id,name,svpath,structureid) values (?,?,?,?,?)", new Object[] { c.getcid(), c.getid(), c.getname(), c.getsvpath(), c.getstructureid() });
        //db.execSQL("insert into dtplaycourse(cid,id,name,svpath) values (?,?,?,?)", new Object[] { c.getcid(), c.getid(), c.getname(), c.getsvpath()});
        //db.close();
		databaseManager.closeDatabase();
	}

	public void save(Dtplaycourse c, String cno) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("insert into dtplaycourse(id,name,svpath,cno) values (?,?,?,?)", new Object[] { c.getid(), c.getname(), c.getsvpath(),cno});
		//db.execSQL("insert into dtplaycourse(id,name,svpath,svpath_abs ,cno) values (?,?,?,?,?)", new Object[] { c.getid(), c.getname(), c.getsvpath(),c.getsvpath_abs(),cno});
		//db.execSQL("insert into dtplaycourse(cid,id,name,svpath,structureid) values (?,?,?,?,?)", new Object[] { c.getcid(), c.getid(), c.getname(), c.getsvpath(), c.getstructureid() });
		//db.execSQL("insert into dtplaycourse(cid,id,name,svpath) values (?,?,?,?)", new Object[] { c.getcid(), c.getid(), c.getname(), c.getsvpath()});
		//db.close();
		databaseManager.closeDatabase();
	}

	public void update(Dtplaycourse c) {
		db = databaseManager.getWritableDatabase();
	    //db.close();
		databaseManager.closeDatabase();
	}


	public ArrayList<Dtplaycourse> find(String id) {
		ArrayList<Dtplaycourse> cs = new ArrayList<Dtplaycourse>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id,name,svpath from dtplaycourse where id=?", new String[] { id });
		//Cursor cursor = db.rawQuery("select cid,id,name,svpath,structureid from dtplaycourse where structureid=?", new String[] { structureid });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Dtplaycourse c = new Dtplaycourse();
				//c.setcid(cursor.getString(cursor.getColumnIndex("cid")));
				c.setid(cursor.getString(cursor.getColumnIndex("id")));
				c.setname(cursor.getString(cursor.getColumnIndex("name")));
				c.setsvpath(cursor.getString(cursor.getColumnIndex("svpath")));
				cs.add(c);
			}
		}
		cursor.close();
		//db.close();
		databaseManager.closeDatabase();
	 	return cs;
	}


		public ArrayList<Dtplaycourse> findC(String cno) {
			ArrayList<Dtplaycourse> cs = new ArrayList<Dtplaycourse>();
			
				db = databaseManager.getWritableDatabase();
				//Cursor cursor = db.rawQuery("select cid,id,name,svpath,structureid from dtplaycourse where cid=?", new String[] { cid });
                Cursor cursor = db.rawQuery("select id,name,svpath from dtplaycourse where cno=?", new String[]{cno});
			    //Cursor cursor = db.rawQuery("select id,name,svpath_abs from dtplaycourse where cno=?", new String[]{cno});
			    //if (cursor != null && cursor.moveToFirst()) {
			    if (cursor.moveToFirst()) {
                    //cursor.moveToFirst();
                        do {
                            Dtplaycourse c = new Dtplaycourse();
                            //c.setstructureid(cursor.getString(cursor.getColumnIndex("structureid")));
                            c.setid(cursor.getString(cursor.getColumnIndex("id")));
                            c.setname(cursor.getString(cursor.getColumnIndex("name")));
                            c.setsvpath(cursor.getString(cursor.getColumnIndex("svpath")));
							//c.setsvpath(cursor.getString(cursor.getColumnIndex("svpath_abs")));
                            cs.add(c);
                        } while (cursor.moveToNext());
                }
				cursor.close();
				//db.close();
			databaseManager.closeDatabase();
			 return cs;
		}
		public ArrayList<Dtplaycourse> findName(String svpath)
		{
			ArrayList<Dtplaycourse> cs = new ArrayList<Dtplaycourse>();
			db = databaseManager.getWritableDatabase();
			Cursor cursor = db.rawQuery("select cid,id,name,svpath,structureid from dtplaycourse where svpath=?", new String[] { svpath });
            //Cursor cursor = db.rawQuery("select cid,id,name,svpath from dtplaycourse where svpath=?", new String[] { svpath });
			if (cursor != null) {
				while (cursor.moveToNext()) {
					Dtplaycourse c = new Dtplaycourse();
					c.setsvpath(cursor.getString(cursor.getColumnIndex("cid")));
					//c.setstructureid(cursor.getString(cursor.getColumnIndex("structureid")));
					c.setid(cursor.getString(cursor.getColumnIndex("id")));
					c.setname(cursor.getString(cursor.getColumnIndex("name")));
					cs.add(c);
				}
			}
			cursor.close();
			//db.close();
			databaseManager.closeDatabase();
			return cs;
			
		}


	public void delete(String name) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from dtplaycourse where name = ?",
				new Object[] { name });
	    //db.close();
		databaseManager.closeDatabase();
	}

	public void deleteAll() {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from dtplaycourse");
	   // db.close();
		databaseManager.closeDatabase();
	}
}
