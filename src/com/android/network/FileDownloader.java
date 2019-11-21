package com.android.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.FileService;
import com.android.sql.FileService2;
import com.android.svod.Page1;
import com.android.svod.R;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FileDownloader {
	private static final String TAG = "FileDownloader";
	private Context context;
	private FileService fileService;
	private FileService2 fileService2;
	private DtcourseService dtcourseService;
	private DtplaycourseService dtplaycoursesService;; 

	/* 已下载文件长度 */
	private int downloadSize = 0;

	/* 原始文件长度 */
	private int fileSize = 0;
	/* 停止下载 */
	private boolean exit;

	/* 线程数 */
	private DownloadThread[] threads;
	
	/* 本地保存文件 */
	 public File saveFile;
	
	/* 缓存各线程下载的长度*/
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
	
	/* 每条线程下载的长度 */
	private int block;
	
	/* 下载路径  */
	private String downloadUrl;
	//下载是否完成
	boolean notFinish = true;//FileService2

	/**
	 * 获取线程数
	 */
	public int getThreadSize() {
		return threads.length;
	}
	/**
	      * 退出下载
	      */
	     public void exit(){
	          this.exit = true;
	      }
	public boolean getExit() {
		return this.exit;
	}

	/**
	 * 获取文件大小
	 * @return
	 */
	public int getFileSize() {
		return fileSize;
	}
	
	/**
	 * 累计已下载大小
	 * @param size
	 */
	protected synchronized void append(int size) {
		downloadSize += size;
	}
	/**
	 * 更新指定线程最后下载的位置
	 * @param threadId 线程id
	 * @param pos 最后下载的位置
	 */
	protected synchronized void update(int threadId, int pos) {
		this.data.put(threadId, pos);
		this.fileService.update(this.downloadUrl, this.data);
	}




	/**
	 * 构建文件下载器
	 * @param downloadUrl 下载路径
	 * @param fileSaveDir 文件保存目录
	 * @param threadNum 下载线程数
	 * @param mode 下载模式，0表示课程文件，1表示导学资料
	 */
	public FileDownloader(Context context, String downloadUrl,File fileSaveDir,
			 int threadNum, int mode, String fileName) {



		try {

			this.context = context;
			this.downloadUrl = downloadUrl;

			fileService = new FileService(this.context);
			fileService2 = new FileService2(this.context);
			dtcourseService=new DtcourseService(this.context);
			dtplaycoursesService=new DtplaycourseService(this.context);





			String filename = null;
		    fileSaveDir = new File("mnt/sdcard/Skyclass/");


			if (!fileSaveDir.exists())
				fileSaveDir.mkdirs();
			this.threads = new DownloadThread[threadNum];


			Log.i(TAG, downloadUrl);


			URL url = new URL(downloadUrl);


			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", downloadUrl);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.connect();

			Log.i(TAG, "test" + conn.getResponseCode());




			if (conn.getResponseCode() == 200) {
				this.fileSize = conn.getContentLength();//根据响应获取文件大小
				Log.i(TAG, "200: " + this.fileSize);


				System.out.println("agagagaga"+this.fileSize);

				if (this.fileSize <= 0||this.fileSize==161)
					throw new RuntimeException("Unkown file size ");

				filename = fileName;
				this.saveFile = new File(fileSaveDir, filename);//构建保存文件
				Map<Integer, Integer> logdata = fileService
						.getData(downloadUrl);//获取下载记录

				if (logdata.size() > 0) {//如果存在下载记录
					for (Map.Entry<Integer, Integer> entry : logdata.entrySet())
						data.put(entry.getKey(), entry.getValue());//把各条线程已经下载的数据长度放入data中
				}
				if (this.data.size() == this.threads.length) {//下面计算所有线程已经下载的数据长度
					for (int i = 0; i < this.threads.length; i++) {
						this.downloadSize += this.data.get(i + 1);
					}

					print("已下载长度" + this.downloadSize);
				}

				//计算每条线程下载的数据长度
				this.block = (this.fileSize % this.threads.length) == 0 ? this.fileSize
						/ this.threads.length
						: this.fileSize / this.threads.length + 1;
			} else {
				throw new RuntimeException("fuck");
			}
		} catch (Exception e) {
			print(e.toString());
			throw new RuntimeException("don't connection this url");
		}
	}


	/**
	 *  开始下载文件
	 * @param listener 监听下载数量的变化,如果不需要了解实时下载的数量,可以设置为null
	 * @return 已下载文件大小
	 * @throws Exception
	 */
	public int download(DownloadProgressListener listener) throws Exception {
		try {

			RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");
		
			if (this.fileSize > 0)
				randOut.setLength(this.fileSize);
			randOut.close();
			URL url = new URL(this.downloadUrl);

			if (this.data.size() != this.threads.length) {
				this.data.clear();   
				for (int i = 0; i < this.threads.length; i++) {
					this.data.put(i + 1, 0);//初始化每条线程已经下载的数据长度为0
				}
			}

			//
			for (int i = 0; i < this.threads.length; i++) {//开启线程进行下载
				int downLength = this.data.get(i + 1);

				if (downLength < this.block
						&& this.downloadSize < this.fileSize) {//判断线程是否已经完成下载,否则继续下载	
					this.threads[i] = new DownloadThread(this, url,
							this.saveFile, this.block, this.data.get(i + 1),
							i + 1);
					this.threads[i].setPriority(7);
					this.threads[i].start();
				} else {
					this.threads[i] = null;
				}
			}

			this.fileService.save(this.downloadUrl, this.data);
			

			while (notFinish) {// 循环判断所有线程是否完成下载
			 	Thread.sleep(900);		

				 notFinish = false;//假定全部线程下载完成
				 
				for (int i = 0; i < this.threads.length; i++) {
					if (this.threads[i] != null && !this.threads[i].isFinish()) {//如果发现线程未完成下载
						notFinish = true;//设置标志为下载没有完成
						if (this.threads[i].getDownLength() == -1) {//如果下载失败,再重新下载
							this.threads[i] = new DownloadThread(this, url,
									this.saveFile, this.block,
									this.data.get(i + 1), i + 1);
							this.threads[i].setPriority(7);
							this.threads[i].start();
						}
					}
				}
     
				if (listener != null)
					listener.onDownloadSize(this.downloadSize);//通知目前已经下载完成的数据长度				 
			}
			
	          	 
	      
		      fileService.delete(this.downloadUrl);

		      fileService2.save(this.downloadUrl, 1, this.downloadSize);
			  
      
			
        
		} catch (Exception e) {
			print(e.toString());
			if(fileService2.getData(this.downloadUrl).size()>0)
				fileService2.delete(this.downloadUrl);
			
			throw new Exception("file download fail");
			
		}
		return this.downloadSize;
	}
	/**
	 * 获取Http响应头字段
	 * @param http
	 * @return
	 */
	public static Map<String, String> getHttpResponseHeader(
			HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();

		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}

	/**
	 * 打印Http头字段
	 * @param http
	 */
	public static void printResponseHeader(HttpURLConnection http) {
		Log.i(TAG,"TRY");
		Map<String, String> header = getHttpResponseHeader(http);

		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() + ":" : "";

			Log.i(TAG, key + entry.getValue());
//			print(key + entry.getValue());
		}
	}

	public boolean isGet() {
		if (this.fileSize == downloadSize)
			return true;
		else
			return false;
	}

	/**
	 * 打印日志信息
	 * @param msg
	 */
	private static void print(String msg) {
		Log.i(TAG, msg);
	}
}
