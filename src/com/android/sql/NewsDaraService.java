package com.android.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.NewsDataFile;

public class NewsDaraService {
	private DBOpenHelper databaseHelper;
    private Context context;
	private SQLiteDatabase db;
    private DatabaseManager databaseManager;
	public NewsDaraService(Context context) {
          this.context=context;
		  databaseHelper =DBOpenHelper.getInstance(this.context);
		databaseManager=DatabaseManager.getInstance(databaseHelper);

		 
	}

	public void save(NewsDataFile c) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("insert into newsData(newsContent) values(?)",
				new Object[] { c.getContent()});
		databaseManager.closeDatabase();
	}

	public void update(NewsDataFile c) {
		db = databaseManager.getWritableDatabase();
	}


	public ArrayList<NewsDataFile> find() {
		ArrayList<NewsDataFile> cs = new ArrayList<NewsDataFile>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select newsContent from news",
				null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				NewsDataFile c = new NewsDataFile();
				c.setContent(cursor.getString(cursor
						.getColumnIndex("newsContent")));
				 
         
				cs.add(c);
			}
		}
		cursor.close();
		//db.close();
		databaseManager.closeDatabase();
		return cs;
	}


	public void delete(String newsTitle) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from newsData where newsContent = ?",
				new Object[] { newsTitle });
		databaseManager.closeDatabase();
	}
}
