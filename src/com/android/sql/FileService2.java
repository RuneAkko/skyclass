package com.android.sql;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FileService2 {
	private DownloadDBOpenHelper openHelper;
	private SQLiteDatabase db;  
	public FileService2(Context context) {
		openHelper = DownloadDBOpenHelper.getInstance(context);
		db=openHelper.getWritableDatabase();
	}
	
	/**
	 * 获取文件是否下载完毕
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path){
		 
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		synchronized (openHelper) {  
            if (!db.isOpen()) {  
                db = openHelper.getReadableDatabase();  
            }  
		Cursor cursor = db.rawQuery("select isOver, downlength from filelog where downpath=?", new String[]{path});
		
		
		while(cursor.moveToNext()){
			data.put(cursor.getInt(0), cursor.getInt(1));
		}
		
		cursor.close();
		db.close();}
		return data;
	}
	//保存文件下载状态
		/**
		 * 保存是否下载完毕
		 * @param path
		 * @param map
		 */
	public void save(String path,  Integer isOver, int fileLength){
		//int threadid, int position
		 
		 synchronized (openHelper) {  
	            //
	            if (!db.isOpen()) {  
	                db = openHelper.getWritableDatabase();  
	            }  
		db.beginTransaction();
		
		try{
		
			
				db.execSQL("insert into filelog(downpath, isOver, downlength) values(?,?,?)",
						new Object[]{path, isOver, fileLength});
			
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();}
	}
	
	/**
	 * 更新下载状态
	 * @param path
	 * @param map
	 */
	public void update(String path,  Integer isOver, Integer fileLength){
		 synchronized (openHelper) {  
	            //
	            if (!db.isOpen()) {  
	                db = openHelper.getWritableDatabase();  
	            }  
		db.beginTransaction();
		
		try{
			
					db.execSQL("update filelog set isOver=?, downlength=? where downpath=?",
						new Object[]{isOver,fileLength, path});
			
			
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		db.close();}
	}
	
	/**
	 * 删除下载记录
	 * @param path
	 */
	public void delete(String path){
		 
		 synchronized (openHelper) {  
	            //
	            if (!db.isOpen()) {  
	                db = openHelper.getWritableDatabase();  
	            }  
		db.execSQL("delete from filelog where downpath=?", new Object[]{path});
		db.close();}
	}
}
