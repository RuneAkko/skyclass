package com.android.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.NewsFile;;

public class NewsService {
	private DBOpenHelper databaseHelper;
    private Context context;
	private SQLiteDatabase db;
    private DatabaseManager databaseManager;
	public NewsService(Context context) {
          this.context=context;
		  databaseHelper = DBOpenHelper.getInstance(this.context);
		  databaseManager=DatabaseManager.getInstance(databaseHelper);
	}

	public void save(NewsFile c) {
		db = databaseManager.getWritableDatabase();
		db.execSQL("insert into news(newsID,newsTitle,newsKeyword,newsDescription) values (?,?,?,?)",
				new Object[] { c.getId(),c.getTitle(),c.getKeywords(),c.getDescription()});
		databaseManager.closeDatabase();
	}

	public void update(NewsFile c) {
		db = databaseManager.getWritableDatabase();
	}


	public ArrayList<NewsFile> find() {
		ArrayList<NewsFile> cs = new ArrayList<NewsFile>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db.rawQuery("select newsID,newsTitle,newsKeyword,newsDescription from news",
				null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				NewsFile c = new NewsFile();
				c.setId(cursor.getString(cursor
						.getColumnIndex("newsID")));
				c.setTitle(cursor.getString(cursor
						.getColumnIndex("newsTitle")));
				c.setKeywords(cursor.getString(cursor
						.getColumnIndex("newsKeyword")));
				c.setDescription(cursor.getString(cursor
						.getColumnIndex("newsDescription")));
         
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
		db.execSQL("delete from news where newsID = ?",
				new Object[] { newsTitle });
		databaseManager.closeDatabase();
	}
}
