package com.android.publiccourse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.svod.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StatFs;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class OBLPubCourseListAdapter extends BaseAdapter {

	private Context mContext;
	
	//comment by renkf 2015-3-26
	private ArrayList<VideoItem> mCourseItems;
	//comment end
	 
	
	private String badUrl;
	private int taskCount ;
	private Map<String, SoftReference<Bitmap>>	imgCacheMap ;
	private File cacheDir;
	private StatFs mStatFs; // 管理 SDCard 容量对象
	

	public OBLPubCourseListAdapter(Context mContext, ArrayList<VideoItem> items) {
		this.mContext = mContext;
		this.mCourseItems = items;
		initStatAndCache();		
	}
	
	
	
	//初始化图片缓存空�?
	public void initStatAndCache(){
		this.imgCacheMap = new HashMap<String, SoftReference<Bitmap>>();		
		String sdState = android.os.Environment.getExternalStorageState();
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();
			cacheDir = new File(sdDir, "/open/temp");
			mStatFs = new StatFs(sdDir.getPath());
		} else {
			cacheDir = mContext.getCacheDir();
		}
		if (!cacheDir.exists())
			cacheDir.mkdirs();			
	}
	

	@Override
	public int getCount() {
		//return recommendCourses.size();
		return mCourseItems.size();
	}

	@Override
	public Object getItem(int position) {
		//return recommendCourses.get(position);
		return mCourseItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.course_pub_list_item, null);
			viewHolder.videoImgView = (ImageView) convertView.findViewById(R.id.imageView1);
			viewHolder.videoNameTextView = (TextView) convertView.findViewById(R.id.pub_course_name);
			viewHolder.videoCategoryTextView = (TextView) convertView.findViewById(R.id.pub_course_category);		
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.videoNameTextView.setText(mCourseItems.get(position).getVideoName());
		//PublicCourseItem temp = recommendCourses.get(position);
		//viewHolder.videoNameTextView.setText(temp.name);
		//String countText = String.format(Configuration.getProperty(R.string.public_course_view_count_fmt), temp.playCount); 
		//viewHolder.videoPlayCountTextView.setText(countText);
		//String scoreText = String.format(Configuration.getProperty(R.string.public_course_view_score_fmt), temp.avgRatingScore);
		//viewHolder.videoStatusTextView.setText(scoreText);
		
		viewHolder.videoCategoryTextView.setText(Html.fromHtml(OBUtil.getString(mContext,R.string.public_course_play_count,
											mCourseItems.get(position).getVideoSize())));
		
		
		//if map has the bitmap   comment by renkf 2015-3-26  旧版公开�?
		if( imgCacheMap.containsKey( mCourseItems.get(position).VideoPicUrl) && 
				imgCacheMap.get(mCourseItems.get(position).VideoPicUrl)!= null &&
				imgCacheMap.get(mCourseItems.get(position).VideoPicUrl).get()!= null){
			
		viewHolder.videoImgView.setImageBitmap(imgCacheMap.get(mCourseItems.get(position).VideoPicUrl).get());
			
		}else{
			
				viewHolder.videoImgView.setImageResource(R.drawable.img_default_image_bg);
				if( taskCount == 0 && !mCourseItems.get(position).VideoPicUrl.equals(badUrl)){
					 new LoadImgTask().execute(mCourseItems.get(position).VideoPicUrl);
					 taskCount++;
				}
			
		}
		//comment end
		/*
		//新版公开�?
		if( imgCacheMap.containsKey( temp.iconUrl) && 
				imgCacheMap.get(temp.iconUrl)!= null &&
				imgCacheMap.get(temp.iconUrl).get()!= null){
			
			viewHolder.videoImgView.setImageBitmap(imgCacheMap.get(temp.iconUrl).get());
			
		}else{
				viewHolder.videoImgView.setImageResource(R.drawable.img_default_image_bg);
				if( taskCount == 0 && !temp.iconUrl.equals(badUrl)){
					 new LoadImgTask().execute(temp.iconUrl);
					 taskCount++;
				}
			
		}
		*/
		
		
		
 		/*viewHolder.videoImgView.setImageResource(R.drawable.img_default_image_bg);
 		OBFileUtil.getInstance(mContext).displayImage(viewHolder.videoImgView, mCourseItems.get(position).VideoPicUrl);*/
		return convertView;
	}

	class ViewHolder{
		ImageView videoImgView;
		ImageView videoDownLoadImgView;
		TextView  videoNameTextView;
		TextView  videoCategoryTextView;
		TextView  videoPlayCountTextView;
		TextView  videoSizeTextView;
		TextView  videoStatusTextView;
	}
	
	
	private class LoadImgTask extends AsyncTask<String, Void, Bitmap> {

		// 保存bitmap到缓�?
		private void writeFile(Bitmap bitmap, File file) {
			Bitmap bitmap1 = BitmapFactory.decodeFile(file.getPath());
			if (bitmap1 == null) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
				} catch (Exception ex) {
					ex.printStackTrace();
					
				} finally {
					try {
						if (out != null)
							out.close();
					} catch (Exception ex2) {
						
					}
				}
			}

		}
		
		String downdloadU;
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null ;
			downdloadU = params[0];
			
			String filename = String.valueOf(downdloadU.hashCode());
			final File file = new File(cacheDir, filename);
			synchronized (OBLPubCourseListAdapter.class) {
				// bitmap在缓存中�?
				bitmap = BitmapFactory.decodeFile(file.getPath());
			}
			if (bitmap != null) {
				
				return bitmap;
			}
			
			try {
				
			URL url = new URL(downdloadU);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位�?
		    options.inInputShareable = true;
		    options.inPurgeable = true;// 设置图片可以被回�?
		    bitmap =  BitmapFactory.decodeStream(is, null, options);
			conn.disconnect();
			is.close();
			
			synchronized ((OBLPubCourseListAdapter.class)) {
				writeFile(bitmap, file);
			}
			return bitmap;
			
			} catch (IOException e) {
				badUrl = downdloadU; 
			
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
		
			if( result != null ){
				imgCacheMap.put(downdloadU, new SoftReference<Bitmap>(result));
			}
			taskCount--;
			OBLPubCourseListAdapter.this.notifyDataSetChanged();
		}
		
	}


	public void fiiiMoreCourseDate(ExtArrayList<VideoItem> coursesList) {
		mCourseItems.addAll(mCourseItems.size(), coursesList);
	}
	
}
