package com.android.sql;

import java.util.ArrayList;
import java.util.List;

import com.android.domain.Course;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CourseService {
	private DBOpenHelper dbOpenHelper;
	private DatabaseManager databaseHelper;
    private Context context;
	private SQLiteDatabase db;
   
	public CourseService(Context context) {
          this.context=context;
		  dbOpenHelper=DBOpenHelper.getInstance(this.context);
		  databaseHelper = DatabaseManager.getInstance(dbOpenHelper);
		 
	}

	public void save(Course c) {
		db = databaseHelper.getWritableDatabase();
		db.execSQL("replace  into courses(studentCode,courseid,courseCode,courseName) values (?,?,?,?)",
				new Object[] {c.getStudentCode(),c.getCourseId(),c.getCourseCode(),c.getCourseName()});
		databaseHelper.closeDatabase();
	}

	public void update(Course c) {
		db = databaseHelper.getWritableDatabase();
		databaseHelper.closeDatabase();
	}

	public ArrayList<Course> find() {
		ArrayList<Course> cs = new ArrayList<Course>();
		db = databaseHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select courseid,courseCode,studentCode,courseName from courses", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Course c = new Course();
                c.setCourseId(cursor.getString(cursor.getColumnIndex("courseid")));
				c.setStudentCode(cursor.getString(cursor.getColumnIndex("studentCode")));
				c.setCourseCode(cursor.getString(cursor.getColumnIndex("courseCode")));
				c.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
         
				cs.add(c);
			}
		}
		cursor.close();
		databaseHelper.closeDatabase();
		return cs;
	}

	public ArrayList<Course> findCS() {
		ArrayList<Course> cs = new ArrayList<Course>();
		db = databaseHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select courseid,courseCode,studentCode,courseName from courses", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Course c = new Course();
				c.setCourseId(cursor.getString(cursor.getColumnIndex("courseid")));
				c.setCourseCode(cursor.getString(cursor.getColumnIndex("courseCode")));
				c.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));

				cs.add(c);
			}
		}
		cursor.close();
		databaseHelper.closeDatabase();
		return cs;
	}

	public void delete(String studentcode) {
		db = databaseHelper.getWritableDatabase();
		db.execSQL("delete from courses where studentcode = ?",
				new Object[] { studentcode });
		databaseHelper.closeDatabase();
	}
}
