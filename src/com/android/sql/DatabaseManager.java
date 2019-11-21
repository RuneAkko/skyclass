package com.android.sql;

import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ycm on 2017/5/16.
 */
public class DatabaseManager {
    private AtomicInteger mOpenCounter=new AtomicInteger();
    private AtomicInteger mOpenCounter1=new AtomicInteger();
    private static DatabaseManager instance;
    private static DatabaseManager downloadInstance;
    private static DBOpenHelper mDatabaseHelper;
    private static DownloadDBOpenHelper downloadDBOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase sqLiteDownloadDatabase;
    public static synchronized DatabaseManager getInstance(DBOpenHelper DatabaseHelper){
        if(instance==null){
            if(mDatabaseHelper==null){
                mDatabaseHelper=DatabaseHelper;
            }
            instance=new DatabaseManager();
        }
        return instance;
    }

    public static synchronized DatabaseManager getDownloadInstance(DownloadDBOpenHelper DatabaseHelper){
        if(downloadInstance==null){
            if(downloadDBOpenHelper==null){
                downloadDBOpenHelper=DatabaseHelper;
            }
            downloadInstance=new DatabaseManager();
        }
        return downloadInstance;
    }
    public synchronized SQLiteDatabase getWritableDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }
    public synchronized SQLiteDatabase getReadableDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            sqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        }
        return sqLiteDatabase;
    }
    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            sqLiteDatabase.close();
        }
    }
    public synchronized SQLiteDatabase getDownloadWritableDatabase() {
        if(mOpenCounter1.incrementAndGet() == 1) {
            // Opening new database
            sqLiteDownloadDatabase = downloadDBOpenHelper.getWritableDatabase();
        }
        return sqLiteDownloadDatabase;
    }
    public synchronized SQLiteDatabase getDownloadReadableDatabase() {
        if(mOpenCounter1.incrementAndGet() == 1) {
            // Opening new database
            sqLiteDownloadDatabase = downloadDBOpenHelper.getReadableDatabase();
        }
        return sqLiteDownloadDatabase;
    }
    public synchronized void closeDownloadDatabase() {
        if(mOpenCounter1.decrementAndGet() == 0) {
            // Closing database
            sqLiteDownloadDatabase.close();
        }
    }
}
