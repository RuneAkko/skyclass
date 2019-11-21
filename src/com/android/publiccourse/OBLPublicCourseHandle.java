package com.android.publiccourse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.svod.R;
import com.android.svod.VideoPlayerActivity;
import com.android.svod.VideoPlayerNewActivity ;

public class OBLPublicCourseHandle implements OnItemClickListener,OnScrollListener {

	public static final String TAG = "OBLPublicCourseHandle";
	public static final int MAX_ADV_COUNT = 5;
	
	private OBLCourseMainActivity mActivity;
	private GridViewWithHeaderAndFooter mGridView;
	private OBLPubCourseListAdapter mAdapter;

	//旧版公开�?
	private ArrayList<VideoItem> mCourseItem;		
	private ArrayList<VideoItem> mTopCourseItem;
	private VideoItem mCouItem[];
	
	private View mView;
	private View mHeaderView;
	
	
	
	
    private ImageView[] imageViews = null;  
    private List<FrameLayout> advPics ; 
    private ImageView imageView = null; 
    
    private ViewPager advPager = null;  
    private AtomicInteger what = new AtomicInteger(0);  
    private boolean isContinue = true;  
    private int topImagCount;
	
	public OBLPublicCourseHandle (Activity activity){
		this.mActivity = (OBLCourseMainActivity) activity;
		mView = mActivity.getLayoutInflater().inflate(
				R.layout.course_pub_list_fragment, null);
		
		mHeaderView = mActivity.getLayoutInflater().inflate(
				R.layout.course_pub_grid_header, null);
		initViewPager();  
		initView();
	}
	
	private void initView() {
		
		mGridView = (GridViewWithHeaderAndFooter) mView.findViewById(R.id.course_pub_listview);
		mGridView.addHeaderView(mHeaderView);
		mGridView.setOnItemClickListener(this);		
		//comment by renkf at 2015-3-25
		//reason: 新版的公�?��不再�?��滚动分页加载
		mGridView.setOnScrollListener(this);
		//comment end	
		setCourseData();
	}
	
	public View findView() {
		return mView;
	}
	
	/**
	 * 旧版公开课，填充数据接口
	 * @param courseItems
	 */
	public void fillCourseData(ArrayList<VideoItem> courseItems) {
		mCourseItem = courseItems;
		mTopCourseItem = new ArrayList<VideoItem>();
		setCourseData();
	}

	//旧版公开课：填充分页数据接口
	public void fillMoreCourseData(ExtArrayList<VideoItem> coursesList) {
		mAdapter.fiiiMoreCourseDate(coursesList);
		mAdapter.notifyDataSetChanged();
		mActivity.isRuquestDate = false;
	}
	
	 
	//旧版公开课：数据生成UI
	public void setCourseData() {
		if(mCourseItem != null && mCourseItem.size()>=topImagCount){
			for(int i=0;i<topImagCount;i++){
				String s = mCourseItem.get(i).getVideoName();
				Log.d("imageimage",s);
				ImageUtil.getInstance(mActivity).displayImageAccount((ImageView)advPics.get(i).getChildAt(0), mCourseItem.get(i).VideoPicUrl, R.drawable.public_course_def);
				//ImageUtil.getInstance(mActivity).displayImageAccount((ImageView)advPics.get(i).getChildAt(0), "https://publiccourselist.xjtudlc.com/pic/fdc.jpg", R.drawable.public_course_def);
				((TextView)advPics.get(i).getChildAt(1)).setText(mCourseItem.get(i).getVideoName());
				mTopCourseItem.add(mCourseItem.get(i));
//				mCouItem[i] =  mCourseItem.get(i);
				final int temp = i;
				advPics.get(i).getChildAt(0).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {						
						VideoItem videoItem = mTopCourseItem.get(temp);
						Intent playIntent = new Intent();
						Bundle playBundle = new Bundle();
						playBundle.putString("mpath", videoItem.getVideoUrl());
						playBundle.putString("title", videoItem.getVideoName());
						playBundle.putBoolean("isOnlinePlay",true);
						playBundle.putBoolean("isLive",false);
						playBundle.putInt("videoSize", videoItem.getVideoSize());
						playBundle.putInt("videoId", videoItem.getVideoId());
						playBundle.putInt("fromWhere", 1);//区分首页视频   课程视频 标志
						playBundle.putInt("isnet", Constants.NETGOOD);
						playIntent.putExtras(playBundle);
//						playIntent.setClass(mActivity, VideoPlayerActivity.class);
						playIntent.setClass(mActivity, VideoPlayerNewActivity .class);
				        mActivity.startActivity(playIntent);				       
					}
				});
			}
		}
		
		if (mCourseItem != null&& mCourseItem.size()>=topImagCount) {
			for(int  i=0;i<topImagCount;i++){
				mCourseItem.remove(0);
			}
			
			mAdapter = new OBLPubCourseListAdapter(mActivity, mCourseItem);
			mGridView.setAdapter(mAdapter);
		}
	}
	
	

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//comment by renkf at 2015-3-27
		VideoItem videoItem = mCourseItem.get(position-2);
    	Intent playIntent = new Intent();
 		Bundle playBundle = new Bundle();
 		playBundle.putString("mpath", videoItem.getVideoUrl());
 		playBundle.putString("title", videoItem.getVideoName());
		playBundle.putBoolean("isOnlinePlay",true);
		playBundle.putBoolean("isLive",false);
 		playBundle.putInt("videoSize", videoItem.getVideoId());
 		playBundle.putInt("videoId", videoItem.getVideoId());
 		playBundle.putInt("isnet", Constants.NETGOOD);
 		playBundle.putInt("fromWhere", 1);//区分首页视频   课程视频 标志
 		playIntent.putExtras(playBundle);
 		playIntent.setClass(mActivity, VideoPlayerNewActivity.class);
//		playIntent.setClass(mActivity, VideoPlayerActivity.class);
//		playIntent.setClass(mActivity, VideoSimpleActivity.class);
		mActivity.startActivity(playIntent);
		//comment end
	}
	private void initViewPager(){
		initViewPager(4);
	}
	
    private void initViewPager(int adCount) {  

    	topImagCount = adCount;
    	if(advPics == null){
    		advPics = new ArrayList<FrameLayout>();
    	}
    	advPics.clear();
    	advPics = prePareViews();  
         
        advPager = (ViewPager) mHeaderView.findViewById(R.id.adv_pager);  
        ViewGroup group = (ViewGroup) mHeaderView.findViewById(R.id.viewGroup);  
        group.removeAllViews();
        
        //对imageviews进行填充  
        imageViews = new ImageView[advPics.size()];  
        //小图�? 
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.leftMargin = 10;
		params.rightMargin = 10;
        for (int i = 0; i < advPics.size(); i++) {  
            imageView = new ImageView(mActivity);  

            imageView.setLayoutParams(params);  
            imageViews[i] = imageView;  
            if (i == 0) {  
                imageViews[i] .setBackgroundResource(R.drawable.guideline_progress_img_f);  
            } else {  
                imageViews[i].setBackgroundResource(R.drawable.guideline_progress_img_uf);  
            }  
            group.addView(imageViews[i]);  
        }  
  
        advPager.setAdapter(new AdvAdapter(advPics));  
        advPager.setOnPageChangeListener(new GuidePageChangeListener());  
        advPager.setOnTouchListener(new OnTouchListener() {  
              
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                switch (event.getAction()) {  
                case MotionEvent.ACTION_DOWN:  
                case MotionEvent.ACTION_MOVE:  
                    isContinue = false;  
                    break;  
                case MotionEvent.ACTION_UP:  
                    isContinue = true;  
                    break;  
                default:  
                    isContinue = true;  
                    break;  
                }  
                return false;  
            }  
        });  
        new Thread(new Runnable() {  
  
            @Override  
            public void run() {  
                while (true) {  
                    if (isContinue) {  
                        viewHandler.sendEmptyMessage(what.get());  
                        whatOption();  
                    }  
                }  
            }  
  
        }).start();  
    }

	public List<FrameLayout> prePareViews() {
		// 这里存放的是四张广告背景  
        for(int i =0;i<topImagCount;i++){
        	//播放按钮img
	        ImageView playBtn = new ImageView(mActivity);  
	        playBtn.setBackgroundResource(R.drawable.play_btn);
	        FrameLayout.LayoutParams img2tparams = new FrameLayout.LayoutParams(
	   				FrameLayout.LayoutParams.WRAP_CONTENT,
	   				FrameLayout.LayoutParams.WRAP_CONTENT);
	        img2tparams.gravity = Gravity.BOTTOM;
	        img2tparams.leftMargin = 16;
	        img2tparams.bottomMargin =10;
	        playBtn.setLayoutParams(img2tparams);
	        
	        //背景图img
	        ImageView img = new ImageView(mActivity);  
	        img.setBackgroundResource(R.drawable.public_course_def); 
	       
	        
	        //视频名字textview
	        FrameLayout layout = new FrameLayout(mActivity);
	        TextView textView = new TextView(mActivity);
	        FrameLayout.LayoutParams tparams = new FrameLayout.LayoutParams(
	   				FrameLayout.LayoutParams.WRAP_CONTENT,
	   				FrameLayout.LayoutParams.WRAP_CONTENT);
	   		tparams.gravity = Gravity.BOTTOM;
	   		textView.setTextColor(Color.BLACK);
	   		textView.getPaint().setFakeBoldText(true);
	   		tparams.leftMargin = 161;
	   		tparams.bottomMargin = 20;
	   		textView.setTextSize(20);
	   		textView.setLayoutParams(tparams);
	   		
	        layout.addView(img);
	        layout.addView(textView);
	        layout.addView(playBtn);
	        advPics.add(layout);  
	        
        }
		return advPics;
	}  
	
    private void whatOption() {  
        what.incrementAndGet();  
        if (what.get() > imageViews.length - 1) {  
            what.getAndAdd(-4);  
        }  
        try {  
            Thread.sleep(5000);  
        } catch (InterruptedException e) {  
              
        }  
    }  
  
    private final Handler viewHandler = new Handler() {  
  
        @Override  
        public void handleMessage(Message msg) {  
            advPager.setCurrentItem(msg.what);  
            super.handleMessage(msg);  
        }  
  
    };
	private int lastVisibleIndex;  
  
    private final class GuidePageChangeListener implements OnPageChangeListener {  
  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
            what.getAndSet(arg0);  
            for (int i = 0; i < imageViews.length; i++) {  
                imageViews[arg0]  
                        .setBackgroundResource(R.drawable.guideline_progress_img_f);  
                if (arg0 != i) {  
                    imageViews[i]  
                            .setBackgroundResource(R.drawable.guideline_progress_img_uf);  
                }  
            }  
  
        }  
  
    }  
  
    private final class AdvAdapter extends PagerAdapter {  
        private List<FrameLayout> views = null;  
  
        public AdvAdapter(List<FrameLayout> views) {  
            this.views = views;  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
        	if (views.size()>arg1){
                ((ViewPager) arg0).removeView(views.get(arg1));  
        	}
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
  
        }  
  
        @Override  
        public int getCount() {  
            return views.size();  
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            ((ViewPager) arg0).addView(views.get(arg1), 0);  
            return views.get(arg1);  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
  
        }  
  
    }

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
//		Log.d("TEST", "------scrollState------- = "+scrollState);
//      Log.d("TEST", "------SCROLL_STATE_IDLE------- = "+OnScrollListener.SCROLL_STATE_IDLE);
// 		Log.d("TEST", "------lastVisibleIndex------- = "+lastVisibleIndex);
//		Log.d("TEST", "------mAdapter.getCount()------- = "+mAdapter.getCount());
		
		 // 滑到底部后自动加载，判断GridView已经停止滚动并且�?��可视的条目等于adapter的条�?
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == mAdapter.getCount()-1 && !mActivity.isRuquestDate) {
            // 当滑到底部时自动加载
        	mActivity.pageNumber++;
        	mActivity.isRuquestDate = true;
        	mActivity.requestPubCourseData(mActivity.pageNumber);
        }
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	    // 计算�?��可见条目的索�?
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;		
// 		Log.e("TEST", "firstVisibleItem = "+firstVisibleItem);
//		Log.e("TEST", "visibleItemCount = "+visibleItemCount);
//		Log.e("TEST", "totalItemCount = "+totalItemCount);
	}   
    
}
