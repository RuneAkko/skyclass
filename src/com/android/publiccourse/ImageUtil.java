package com.android.publiccourse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.android.svod.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


/**
 * @author szk 主要用来显示头部图片
 * 
 */
public class ImageUtil {

	private final String TAG = "ImageUtil";
	private HashMap<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
	private File cacheDir;
	private static ImageUtil instance;
	private Context mContext;
	private int taskCount = 0;
	private Object lock = new Object();

	/**
	 * 合成带阴影的图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap converShadowBitmap(Bitmap bitmap) {
		BlurMaskFilter blurFilter = new BlurMaskFilter(10,
				BlurMaskFilter.Blur.SOLID);
		Paint shadowPaint = new Paint();
		shadowPaint.setMaskFilter(blurFilter);
		shadowPaint.setColor(Color.BLACK);
		int[] offsetXY = new int[2];
		Bitmap originalBitmap = bitmap;
		Bitmap shadowImage = originalBitmap.extractAlpha(shadowPaint, offsetXY);
		Bitmap shadowImage32 = shadowImage.copy(Bitmap.Config.ARGB_8888, true);

		Canvas c = new Canvas(shadowImage32);
		c.drawBitmap(originalBitmap, -offsetXY[0], -offsetXY[1], null);
		return shadowImage32;
	}

	private ImageUtil(Context context) {
		mContext = context;
		String sdState = android.os.Environment.getExternalStorageState();
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = context.getExternalFilesDir(null);
			cacheDir = new File(sdDir, "/open/codehenge");
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public static ImageUtil getInstance(Context context) {
		if (instance == null)
			instance = new ImageUtil(context);
		return instance;
	}

	public void displayImage(final ImageView imageView, String url) {
	    imageView.setImageResource(R.drawable.img_theme_person_default);// 设置默认图片
	    if (! TextUtils.isEmpty(url)) {
			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... urls) {
					
					try{
						URL url = new URL(urls[0]);
						
						String filename = String.valueOf(urls[0].hashCode());
						final File file = new File(cacheDir, filename);
						Bitmap bitmap;
						synchronized (ImageUtil.class) {
							// bitmap在缓存中�?						
							bitmap = BitmapFactory.decodeFile(file.getPath());
						}
						if (bitmap != null) {
							return bitmap;
						}else{// 否则，下载之
							try {
								URLConnection conn = url.openConnection();
								InputStream in = null;
								try{
									in = conn.getInputStream();
									bitmap = BitmapFactory.decodeStream(in);
								}finally{
									if(in != null){
										in.close();
									}
								}
								synchronized ((ImageUtil.class)) {
									writeFile(bitmap, file);
								}
								return bitmap;
							} catch (Exception e) {
								e.printStackTrace();
								//Log.e(TAG, "getBitmap() " + e.getMessage(), e);
								//return null;
							}
						}
					}catch(MalformedURLException e){
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {
						// imageMap.put(url, result);//TODO maintains imageMap?
						imageView.setImageBitmap(result);
					}
				}
			}.execute(url);
		}

	}

	public void displaySetDrawable(final ImageView imageView, final String url) {
		if (url == null) {
			// imageView.setImageResource(Type);// 设置默认图片
		} else if (imageMap.containsKey(url)) {
			Drawable drawable = new BitmapDrawable(imageMap.get(url));
			imageView.setBackgroundDrawable(drawable);
		} else {
			// imageView.setImageResource(Type);// 设置默认图片

			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... urls) {
					String filename = String.valueOf(url.hashCode());
					final File file = new File(cacheDir, filename);
					Bitmap bitmap;
					synchronized (ImageUtil.class) {
						// bitmap在缓存中�?					
						bitmap = BitmapFactory.decodeFile(file.getPath());
					}
					if (bitmap != null) {
						return bitmap;
					}
					// 否则，下载之
					try {
						URLConnection conn = new URL(urls[0]).openConnection();
						InputStream in = null;
						try{
							in = conn.getInputStream();
							bitmap = BitmapFactory.decodeStream(in);							
						}finally{
							if(in != null){
								in.close();
							}
						}
						/*
						bitmap = BitmapFactory.decodeStream(new URL(urls[0])
								.openConnection().getInputStream());
								*/
						synchronized ((ImageUtil.class)) {
							writeFile(bitmap, file);
						}
						return bitmap;
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, "getBitmap() " + e.getMessage(), e);
						return null;
					}
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {
						imageMap.put(url, result);
						Drawable drawable = new BitmapDrawable(result);
						imageView.setBackgroundDrawable(drawable);
					}
				}
			}.execute(url);
		}

	}

	// szk 2012 -3-15 添加,因为图片默认显示不同
	public void displayImageTopic(final ImageView imageView, final String url) {
		if (url == null) {
			// imageView.setImageResource(Type);// 设置默认图片
		} else if (imageMap.containsKey(url)) {
			imageView.setImageBitmap(imageMap.get(url));
		} else {
			// imageView.setImageResource(Type);// 设置默认图片

			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... urls) {
					String filename = String.valueOf(url.hashCode());
					final File file = new File(cacheDir, filename);
					Bitmap bitmap;
					synchronized (ImageUtil.class) {
						// bitmap在缓存中�?						
						bitmap = BitmapFactory.decodeFile(file.getPath());
					}
					if (bitmap != null) {
						return bitmap;
					}
					// 否则，下载之
					try {

						bitmap = BitmapFactory.decodeStream(new URL(urls[0])
								.openConnection().getInputStream());
						synchronized ((ImageUtil.class)) {
							writeFile(bitmap, file);
						}
						return bitmap;
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, "getBitmap() " + e.getMessage(), e);
						return null;
					}
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {
						imageMap.put(url, result);
						imageView.setImageBitmap(result);
					}
				}
			}.execute(url);
		}

	}

	public void displayImageAccount(final ImageView imageView,
			final String url, int id) {
		if (url.equals("")) {
			imageView.setImageResource(id);// 设置默认图片
		} else if (imageMap.containsKey(url)) {
			imageView.setImageBitmap(imageMap.get(url));
		} else {
			imageView.setImageResource(id);// 设置默认图片

			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... urls) {
					String filename = String.valueOf(url.hashCode());
					final File file = new File(cacheDir, filename);
					Bitmap bitmap;
					synchronized (ImageUtil.class) {
						// bitmap在缓存中�?					
						bitmap = BitmapFactory.decodeFile(file.getPath());
					}
					if (bitmap != null) {
						return bitmap;
					}
					// 否则，下载之
					try {
						bitmap = BitmapFactory.decodeStream(new URL(urls[0])
								.openConnection().getInputStream());
						synchronized ((ImageUtil.class)) {
							writeFile(bitmap, file);
						}
						return bitmap;
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, "getBitmap() " + e.getMessage(), e);
						return null;
					}
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {
						// imageMap.put(url, result);
						imageView.setImageBitmap(result);
					}
				}
			}.execute(url);
		}

	}

	public void displayVideoList(final View imageView, final String url, int id) {
		Log.d(TAG, "The current url" + url + taskCount);
		if (url == null) {
			imageView.setBackgroundResource(id);// 设置默认图片
		} else if (imageMap.containsKey(url)) {
			imageView.setBackgroundDrawable(new BitmapDrawable(imageMap
					.get(url)));
		} else {
			imageView.setBackgroundResource(id);// 设置默认图片
			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... urls) {
					String filename = String.valueOf(url.hashCode());
					final File file = new File(cacheDir, filename);
					Bitmap bitmap;
					synchronized (ImageUtil.class) {
						// bitmap在缓存中�?						
						bitmap = BitmapFactory.decodeFile(file.getPath());
					}
					if (bitmap != null) {
						return bitmap;
					}
					// 否则，下载之
					try {
						bitmap = BitmapFactory.decodeStream(new URL(urls[0])
								.openConnection().getInputStream());
						synchronized ((ImageUtil.class)) {
							writeFile(bitmap, file);
						}
						return bitmap;
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, "getBitmap() " + e.getMessage(), e);
						return null;
					}
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {

						imageMap.put(url, result);
						imageView.setBackgroundDrawable(new BitmapDrawable(
								result));
					}
				}
			}.execute(url);
		}
	}

	/**
	 * 
	 * @param imageView
	 * @param url
	 */
	public void setDrawableFromUrl(final ImageView imageView, final String url) {
		if (url == null) {
			return;
		} else {

			new AsyncTask<String, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(String... urls) {
					// String filename = String.valueOf(url.hashCode());
					// final File file = new File(cacheDir, filename);
					Bitmap bitmap;
					// synchronized (ImageUtil.class) {
					// // bitmap在缓存中�?					// bitmap = BitmapFactory.decodeFile(file.getPath());
					// }
					// if (bitmap != null) {
					// return bitmap;
					// }
					// 否则，下载之
					try {

						bitmap = BitmapFactory.decodeStream(new URL(urls[0])
								.openConnection().getInputStream());
						// synchronized ((ImageUtil.class)) {
						// writeFile(bitmap, file);
						// }
						return bitmap;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {
						imageMap.put(url, result);
						Drawable drawable = new BitmapDrawable(result);
						imageView.setBackgroundDrawable(drawable);
					}
				}
			}.execute(url);
		}

	}

	// 保存bitmap到缓�?	
	private void writeFile(Bitmap bitmap, File file) {
		//Bitmap bitmap1 = BitmapFactory.decodeFile(file.getPath());
		//if (bitmap1 == null) {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e(TAG, "writeFile() " + ex.getMessage(), ex);
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (Exception ex2) {
					Log.e(TAG, "writeFile() " + ex2.getMessage(), ex2);
				}
			}
		//}
	}

	// 照片保存的路�?	
public File bornSolePath(String chatedId, String saveType, String fromType,String fileName) {
		// 如果在sd卡上不能创建成功，直接创建在程序内部
		final File savaPath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable() ? getDerectory(chatedId)
				: mContext.getFilesDir();
		// 如果不能获取到正确的文件路径
		if (savaPath == null)
			return savaPath;
		File subDirectory = new File(savaPath, saveType);
		if (!subDirectory.exists())
			subDirectory.mkdir();

		File currentFile = null;
		if (fromType.equals(Constants.MessageFromType.MINE)) {
			try {
				currentFile = createImageFile(subDirectory);
			} catch (Exception e) {

				e.printStackTrace();

			}
		} else if (fromType.equals(Constants.MessageFromType.FRIEND)) {
			currentFile = new File(subDirectory, fileName);
		} else if (fromType.equals(Constants.MessageFromType.FRIEND)) {
			currentFile = new File(subDirectory, fileName);
		}

		return currentFile;
	}

	private File createImageFile(File directory) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = timeStamp + "_";
		File image = File.createTempFile(imageFileName, ".jpg", directory);
		return image;
	}

	// 如果sdk版本大于9
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	

	public File getDerectory(String chatedId) {
		// 如果在sd卡上不能创建成功，直接创建在程序内部
		File fileDirectory = mContext.getExternalFilesDir(chatedId);
		if (!fileDirectory.exists()) {
			fileDirectory = mContext.getFilesDir();
		}
		return fileDirectory;
	}

	
	/**
	 * 
	 * @ClassName: OBAsyncTask
	 * @Description: TODO
	 * @author zhangchunzhe
	 * @date 2013-12-11 下午7:25:18
	 * 
	 */
	private class OBAsyncTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
