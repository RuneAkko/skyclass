package com.android.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.domain.User;

public class Userservice {
    private DBOpenHelper databaseHelper;
    private Context context;
    private SQLiteDatabase db;
    private DatabaseManager databaseManager;

    public Userservice(Context context) {
        this.context = context;
        databaseHelper = DBOpenHelper.getInstance(context);
        databaseManager = DatabaseManager.getInstance(databaseHelper);
    }

    public void saveStartOperation(User usr) {
        db = databaseManager.getWritableDatabase();
        db.execSQL("insert into operation(operationcode,studentcode,courseid,path,time,len) " +
				"values (?,?,?,?,?,?)", new Object[]{usr.getOperationCode(), usr.getstudentcode(),
				usr.getcourseid(), usr.getpath(), usr.getTime(), usr.getlen()});
        // db.close();
        databaseManager.closeDatabase();
    }

    //�����û�����
    public void save(User usr) {
        db = databaseManager.getWritableDatabase();
        db.execSQL("insert into operation(operationcode,studentcode,courseid,path,time,len) " +
				"values (?,?,?,?,?,?)", new Object[]{usr.getOperationCode(), usr.getstudentcode(),
				usr.getcourseid(), usr.getpath(), usr.getTime(), usr.getlen()});
        //db.close();
        databaseManager.closeDatabase();
    }


    //����
    public void update(User usr) {
        db = databaseManager.getWritableDatabase();

    }

    //��ѯ�û����в�����¼
    public List<User> find(String studentcode) {
        List<User> usrs = new ArrayList<User>();
        db = databaseManager.getWritableDatabase();
        Cursor cursor = db.query("operation", new String[]{"operationcode", "studentcode",
				"courseid", "path", "time", "len"}, "studentcode = ?", new String[]{studentcode},
				null, null, null, null);

        while (cursor.moveToNext()) {
            User usr = new User();
            usr.setOperationCode(cursor.getString(cursor.getColumnIndex("operationcode")));
            usr.setstudentcode(cursor.getString(cursor.getColumnIndex("studentcode")));
            usr.setcourseid(cursor.getString(cursor.getColumnIndex("courseid")));
            usr.setpath(cursor.getString(cursor.getColumnIndex("path")));
            usr.setTime(cursor.getString(cursor.getColumnIndex("time")));
            usr.setlen(cursor.getLong(cursor.getColumnIndex("len")));
            usrs.add(usr);
        }
        cursor.close();
        //db.close();
        databaseManager.closeDatabase();
        return usrs;
    }

    //��ѯ�û���¼�Ƿ����
    public Boolean findExist(String studentcode) {
        db = databaseManager.getReadableDatabase();
        Cursor cursor = db.query("operation", new String[]{"operationcode", "studentcode",
				"courseid", "path", "time", "len"}, "studentcode = ?", new String[]{studentcode},
				null, null, null, null);

        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        //db.close();
        databaseManager.closeDatabase();
        return false;
    }

    //ɾ���û����в�����¼
    public void delete(String studentcode) {
        db = databaseManager.getWritableDatabase();
        db.execSQL("delete from operation where studentcode = ?", new Object[]{studentcode});
        databaseManager.closeDatabase();
    }

}
