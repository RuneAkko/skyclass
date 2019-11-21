package com.android.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.studyProgress;

public class studyProcessService {
	private DBOpenHelper databaseHelper;
	private Context context;
	private SQLiteDatabase db;
    private DatabaseManager databaseManager;
	public studyProcessService(Context context) {
		this.context = context;
		databaseHelper = DBOpenHelper.getInstance(context);
        databaseManager=DatabaseManager.getInstance(databaseHelper);
	}

	public void save(studyProgress c) {
		db = databaseManager.getWritableDatabase();
		db.execSQL(
				"insert into studyProcess(GradeTotal,GradeSum,GraduatelowlimDays,GraduatehighlimDays,StudyDays) values (?,?,?,?,?)",
				new Object[] { c.getGradeTotal(), c.getGradeSum(), c.getGraduatelowlimDays(),
						c.getGraduatehighlimDays(), c.getStudyDays() });
		//db.close();
		databaseManager.closeDatabase();
	}

	public void update(studyProgress c) {
		db = databaseManager.getWritableDatabase();
	    //db.close();
		databaseManager.closeDatabase();
	}


	public ArrayList<studyProgress> find() {
		ArrayList<studyProgress> cs = new ArrayList<studyProgress>();
		db = databaseManager.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select GradeTotal,GradeSum,GraduatelowlimDays,GraduatehighlimDays,StudyDays from studyProcess",null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				studyProgress c = new studyProgress();
				c.setGradeTotal(cursor.getString(cursor.getColumnIndex("GradeTotal")));
				c.setGradeSum(cursor.getString(cursor.getColumnIndex("GradeSum")));
				c.setGraduatelowlimDays(cursor.getString(cursor.getColumnIndex("GraduatelowlimDays")));
				c.setGraduatehighlimDays(cursor.getString(cursor.getColumnIndex("GraduatehighlimDays")));
				c.setStudyDays(cursor.getString(cursor.getColumnIndex("StudyDays")));
				cs.add(c);
			}
		}
		cursor.close();
		//db.close();
		databaseManager.closeDatabase();
	 	return cs;
	}



	public void deleteAll() {
		db = databaseManager.getWritableDatabase();
		db.execSQL("delete from studyProcess");
	    //db.close();
		databaseManager.closeDatabase();
	}

}
