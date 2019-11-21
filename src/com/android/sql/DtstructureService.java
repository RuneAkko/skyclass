package com.android.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.Dtstructure;

public class DtstructureService {
	private DBOpenHelper databaseHelper;
    private Context context;
	private SQLiteDatabase db;
    private DatabaseManager databaseManager;
	public DtstructureService(Context context) {
          this.context=context;
          databaseHelper = DBOpenHelper.getInstance(context);
		 databaseManager=DatabaseManager.getInstance(databaseHelper);
	}

	public void save(Dtstructure c) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("replace into dtstructure(id,courseid,name) values (?,?,?)",
				new Object[] {c.getid(),c.getcourseid(),c.getname()});
		//db.close();
		databaseManager.closeDatabase();
	 
	}

	public void update(Dtstructure c) {
		db = databaseManager.getWritableDatabase();
	    //db.close();
		databaseManager.closeDatabase();
	}


	public ArrayList<Dtstructure> find(String courseid) {
		ArrayList<Dtstructure> cs = new ArrayList<Dtstructure>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select id,name from dtstructure where courseid=?", new String[]{courseid});
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Dtstructure c = new Dtstructure();
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


	public void delete(String id) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from dtstructure where id = ?", new Object[] { id });
		//db.close();
		databaseManager.closeDatabase();
	}

		public void deleteAll( ) {
			db = databaseManager.getWritableDatabase();
			db.execSQL("delete from dtstructure ");
			//db.close();
			databaseManager.closeDatabase();
		}
}
