package com.android.svod;

import com.android.svod.R;
import com.android.svod.R.drawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.android.domain.*;
import com.android.json.JsonParse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.WebSettings.*;

public class Page3 extends Activity {
	/** Called when the activity is first created. */
	private WebView webView;
	private InputStream is = null;
	String result = "";
	//获取相关新闻的主题等相关信息
	ArrayList<NewsFile> mylist = null;
	public final static String CSS_STYLE = "<style>* {font-size:16px;line-height:20px;}p {color:#FFFFFF;}</style>";
	//获取新闻内容信息
	NewsDataFile myData = new NewsDataFile();
	private Handler mHandler = new Handler();
	private JsonParse testJson;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page3);
		webView = (WebView) this.findViewById(R.id.webview);
		webView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bg_home));
		String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setAppCachePath(appCacheDir);
		testJson = new JsonParse(this.getApplicationContext());
		AsyncTask<Void, Integer, Boolean> getNewsTask = new AsyncTask<Void, Integer, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				mylist = conn();
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				webView.addJavascriptInterface(new SharpJavaScript(), "sharp");
				webView.getSettings().setJavaScriptEnabled(true);
				//加载自己定义的html界面
				webView.loadUrl("file:///android_asset/index.html");
				webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				webView.getSettings().setSaveFormData(false);
				webView.getSettings().setSavePassword(false);
				webView.getSettings().setSupportZoom(false);
				// webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
				webView.getSettings().setUseWideViewPort(true);
				webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setAllowFileAccess(true);
				webView.getSettings().setAppCacheEnabled(true);
				System.out.println(mylist.size());
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						// TODO Auto-generated method stub
						view.loadUrl(url);
						return true;
					}
				});
			}
		};

		getNewsTask.execute();
	}
	// HTML调用Android内部定义的程序
	public class SharpJavaScript {

		public void newslist() {
			try {

				mHandler.post(new Runnable() {
					public void run() {
						String json = null;
						try {
							json = buildJson(mylist);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						webView.loadUrl("javascript:sh('" + json + "')");
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// index-bakҳ��������õĶ���ݺ���
		public void call2(final String Id) {
			Toast toast = Toast.makeText(Page3.this, "加载中，请稍后...", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			mHandler.post(new Runnable() {
				public void run() {
					myData = conn(Id);
					webView.loadUrl("file:///android_asset/content.html");
				}
			});

		}

		public void newsDataList() {
			try {
				// String json = buildJson2(myDataList);
				mHandler.post(new Runnable() {
					public void run() {
						String json = null;
						try {
							json = buildJson2(myData);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						webView.loadUrl("javascript:sh2('" + json + "')");
					}
				});
				// webView.loadUrl("javascript:sh2('" + json + "')");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 网上获取相应的列表信息
	public ArrayList<NewsFile> conn() {
		ArrayList<NewsFile> myList2 = null;
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.xjtudlc.com/iphone_dlc/newlist.php");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
				result = EntityUtils.toString(entity, HTTP.UTF_8);

			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result" + e.toString());
		}
		try {
			testJson.NewsJSONToBean(result);
			myList2 = testJson.getMyNewsList();

		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
		}
		return myList2;
	}

	// 将相应的字符变为JSON字符串
	public String buildJson(ArrayList<NewsFile> myList) throws Exception {
		JSONArray array = new JSONArray();
		for (NewsFile newsFile : myList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Id", newsFile.getId());
			jsonObject.put("Title", newsFile.getTitle());
			jsonObject.put("Keywords", newsFile.getKeywords());
			jsonObject.put("Description", newsFile.getDescription());
			array.put(jsonObject);

		}
		return array.toString();

	}

	// 网络上获取相应的新闻内容的具体信息
	public NewsDataFile conn(String Id) {
		NewsDataFile myDataList2 = new NewsDataFile();
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.xjtudlc.com/iphone_dlc/news.php?id=" + Id);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
				result = EntityUtils.toString(entity, HTTP.UTF_8);
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result" + e.toString());
		}

		try {
			myDataList2 = testJson.DataJSONToBean(result);
		} catch (Exception e) {
			Log.e("log_tag", "Error parsing data" + e.toString());
		}

		return myDataList2;
	}

	public String buildJson2(NewsDataFile myDataList) throws Exception {

		return myDataList.getContent();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			View quitView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.quit,null);
			new AlertDialog.Builder(this).setView(quitView)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
		}
		// TODO Auto-generated method stub
		if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			webView.goBack();
			return true;
		}
		if (!webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			View quitView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.quit,null);
			new AlertDialog.Builder(this).setView(quitView)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
		}
		return super.onKeyDown(keyCode, event);
	}

}