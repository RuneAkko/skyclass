package com.android.sql;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FileService {
	private DownloadDBOpenHelper openHelper;
	private SQLiteDatabase db;
	private  DatabaseManager databaseManager;
	public FileService(Context context) {
		openHelper = DownloadDBOpenHelper.getInstance(context);
		databaseManager=DatabaseManager.getDownloadInstance(openHelper);
		db=databaseManager.getWritableDatabase();
	}
	
	/**
	 * 获取每条线程已经下载的文件长度
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path){
		 
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		synchronized (openHelper) {  
            if (!db.isOpen()) {  
                db = openHelper.getReadableDatabase();  
            }  
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?", new String[]{path});
		
		
		while(cursor.moveToNext()){
			data.put(cursor.getInt(0), cursor.getInt(1));
		}
		
		cursor.close();
		db.close();}
		return data;
	}
	//每个数据库操作都要加同步锁 防止读取数据库被锁
			/**
			 * 保存每条线程已经下载的文件长度
			 * @param path
			 * @param map
			 */
	public void save(String path,  Map<Integer, Integer> map){
		//int threadid, int position
		 
		 synchronized (openHelper) {  
			// 看数据库是否关闭  
	            if (!db.isOpen()) {  
	                db = openHelper.getWritableDatabase();  
	            }  
		db.beginTransaction();
		
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			
				db.execSQL("insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
						new Object[]{path, entry.getKey(), entry.getValue()});
			}
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();}
	}
	
	/**
	 * 实时更新每条线程已经下载的文件长度
	 * @param path
	 * @param map
	 */
	public void update(String path, Map<Integer, Integer> map){
		 synchronized (openHelper) {  
			  // 看数据库是否关闭  
	            if (!db.isOpen()) {  
	                db = openHelper.getWritableDatabase();  
	            }  
		db.beginTransaction();
		
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){
					db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
						new Object[]{entry.getValue(), path, entry.getKey()});
			}
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();}
	}
	/**
	 * 当文件下载完成后，删除对应的下载记录
	 * @param path
	 */
	public void delete(String path){
		 
		 synchronized (openHelper) {  
			// 看数据库是否关闭 
	            if (!db.isOpen()) {  
	                db = openHelper.getWritableDatabase();  
	            }  
		db.execSQL("delete from filedownlog where downpath=?", new Object[]{path});
		db.close();}
	}
}
