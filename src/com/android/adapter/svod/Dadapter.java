package com.android.adapter.svod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.connection.GetRequest;
import com.android.network.DownloadProgressListener;
import com.android.network.FileDownloader;
import com.android.svod.CourseDocActivity;
import com.android.svod.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Dadapter extends BaseAdapter {

	private static final String TAG = "Dadapter";
	private Context context;
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private ArrayList<HashMap<String, Object>> datalist;
	private int flag;
	public static final int FINISHDOWNLOAD = 1;
	public static final int NOTFINISH = 2;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	/** 构造函数*/
	public Dadapter(Context context, ArrayList<HashMap<String, Object>> datalist, int flag) {
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.datalist = datalist;
		this.flag = flag;
	}

	// 返回这个BaseAdapter处理数据源的总数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	//返回数据源中的某个位置的对象
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	// 返回数据源中的某个位置的对象id
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 决定了我们在list布局上展示的效果
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.docsection, null);
			holder = new ViewHolder(convertView, position);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}

		return convertView;
	}

	/** 存放控件 */
	private class ViewHolder {
		public Handler downloadhandler = new Handler() {
			public void handleMessage(Message msg) {
				View view = LayoutInflater.from(context).inflate(R.layout.docsection, null);
				switch (msg.what) {
				case FINISHDOWNLOAD:
					btdown.setVisibility(View.GONE);

					btdelete.setVisibility(View.VISIBLE);
					btdelete.setClickable(true);
					break;
				case NOTFINISH:

					btdown.setVisibility(View.VISIBLE);
					btdown.setClickable(true);

					btdelete.setVisibility(View.GONE);
					break;

				}

			}
		};

		public TextView title;
		public Button btdown;
		public Button btdelete;
		public ProgressBar pb;// 下载进度条
		public TextView resultView;// 存放进度条进度数据
		public String path;// 文件保存路径后半边名字
		public String downUrl;
		public File filesave;
		public String filename;
        public boolean boolean_flag=false;
		public ViewHolder(View view, int position) {
			this.title = (TextView) view.findViewById(R.id.sectiondoc);
			this.btdown = (Button) view.findViewById(R.id.downloaddoc);
			this.btdelete = (Button) view.findViewById(R.id.delete);
			this.pb = (ProgressBar) view.findViewById(R.id.downloadbar);
			this.resultView = (TextView) view.findViewById(R.id.resultView);
			// this.downUrl =
			// requestDocURl("http://xueli.xjtudlc.com/MobileLearning/Get_Attachment.aspx?ArticleID="+datalist.get(position).get("articleId").toString());
			String temp = "";
			GetRequest rt = new GetRequest();
			if (flag == 0) {
				temp = rt.request("https://xueli.xjtudlc.com/MobileLearning/Get_Attachment.aspx?ArticleID="
						+ datalist.get(position).get("articleId").toString());
			} else if (flag == 1) {
				temp = rt.request("https://feixueli.xjtudlc.com/MobileLearning/Get_Attachment.aspx?ArticleID="
						+ datalist.get(position).get("articleId").toString());
			}
			int begin_url = (temp.indexOf("<A href=", 0) == -1) ? temp.indexOf("<a href=", 0)
					: temp.indexOf("<A href=", 0);

			int last_url = temp.indexOf("target", begin_url);

//			Log.i(TAG, " begin_url " + begin_url);
//			Log.i(TAG, "last_url " + last_url);Log.i(TAG, " begin_url " + begin_url);
//			Log.i(TAG, "last_url " + last_url);
			Log.i(TAG, temp);

//			this.downUrl = temp.substring(begin_url + 9, last_url - 2);
			this.downUrl = "https:" + temp.substring(begin_url + 14, last_url - 2);


			Log.i(TAG, downUrl);

			int begin_name = temp.indexOf(">", last_url);
			int last_name = (temp.indexOf("</A>", begin_name) == -1) ? temp.indexOf("</a>", begin_name)
					: temp.indexOf("</A>", begin_name);
			this.filename = temp.substring(begin_name + 1, last_name);
			this.path = "mnt/sdcard/Skyclass/" + filename;
			this.filesave = new File(this.path);

			/** 设置TextView显示的内容*/
			this.title.setText(datalist.get(position).get("mainHead").toString());
			this.title.setTextSize(15);
			this.title.setTextColor(context.getResources().getColor(R.color.textcolor));

			// 读
			final String filePath = downUrl;
			if (!filesave.exists()) {
				Message message = new Message();
				message.what = NOTFINISH;
				downloadhandler.sendMessage(message);
			} else {
				Message message = new Message();
				message.what = FINISHDOWNLOAD;
				downloadhandler.sendMessage(message);
			}

			/** 为Button添加点击事件 */
			this.btdown.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					ConnectivityManager manager = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
					State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

					if ((mobile == State.CONNECTED || mobile == State.CONNECTING)
							|| (wifi == State.CONNECTED || wifi == State.CONNECTING)) {

						pb.setVisibility(ProgressBar.VISIBLE);
						resultView.setVisibility(View.VISIBLE);

						System.out
								.println(Environment.getExternalStorageState() + "------" + Environment.MEDIA_MOUNTED);
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							// 开始现在文件
							download(downUrl, filesave, pb, resultView, btdown, filename);
							btdown.setClickable(false);
						} else {
							// 显示SDCard错误信息
							Toast.makeText(context, R.string.sdcarderror,Toast.LENGTH_SHORT).show();

						}
					} else {
						showTips();
					}

				}
			});


			this.btdelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// 弹出确认框
					Builder builder = new Builder(context);
					View view=LayoutInflater.from(context).inflate(R.layout.deletedialog,null);
					builder.setView(view);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (filesave.exists()) {
								filesave.delete();
								Toast.makeText(context, "成功删除" + filesave.getName(), Toast.LENGTH_SHORT).show();
								Message message = new Message();
								message.what = NOTFINISH;
								downloadhandler.sendMessage(message);

							}

						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});

					builder.create().show();
				}
			});
		}

		/**
		 *
		 * 
		 * @param path
		 * @param savedir
		 */
		private DownloadTask task;

		// 进度条进程
		private void download(final String path, final File savedir, ProgressBar progressBar, TextView resultView,
				Button button, String filename) {
			task = new DownloadTask(path, savedir, progressBar, resultView, button, filename);
			new Thread(task).start();
		}

		private void exit() {
			// 退出下载
			if (task != null) {
				task.exit();
			}
		}

		public class DownloadTask implements Runnable {
			String path;
			File savedir;
			ProgressBar progressBar;
			TextView resultView;
			Button button;
			String filename;
			FileDownloader loader;

			public DownloadTask(String path, File savedir, ProgressBar progressBar, TextView resultView, Button button,
					String filename) {
				this.path = path;
				this.savedir = savedir;
				this.progressBar = progressBar;
				this.resultView = resultView;
				this.button = button;
				this.filename = filename;
			}

			public void exit() {
				if (loader != null) {
					loader.exit();// 退出下载
				}
			}

			@Override
			public void run() {
				try {
					loader = new FileDownloader(context, path, savedir, 4, 1, filename);
					progressBar.setMax(loader.getFileSize());// 设置进度条的最大刻度

					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					Log.i(TAG , e.getMessage());
					handler.sendMessage(handler.obtainMessage(-1));
				}

			}

			private Handler handler1 = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					resultView.setText("100%");
					resultView.setVisibility(TextView.INVISIBLE);
					resultView.setText("0%");

					progressBar.setVisibility(ProgressBar.INVISIBLE);
					progressBar.setProgress(0);
				}
			};
			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						int size = msg.getData().getInt("size");
						progressBar.setProgress(size);
						float num = (float) progressBar.getProgress() / (float) progressBar.getMax();
						int result = (int) (num * 100);

						if (result != 100) {
							resultView.setText(result + "%");
							button.setClickable(false);
						}
						if (progressBar.getProgress() == progressBar.getMax()) {
							boolean_flag = true;
							AsyncTask<String, Integer, String> multiTask = new AsyncTask<String, Integer, String>() {

								@Override
								protected void onPostExecute(String result) {
									// TODO Auto-generated method stub
									super.onPostExecute(result);
									Message message = new Message();
									message.what = FINISHDOWNLOAD;
									downloadhandler.sendMessage(message);
								}

								@Override
								protected String doInBackground(String... params) {
									// TODO Auto-generated method stub
									String path = loader.saveFile.getPath();

									path = path.replace("temp", "");
									File dfile = new File(path);
									loader.saveFile.renameTo(dfile);
									Message message = new Message();

									handler1.sendMessage(message);

									return null;
								}
							};
							multiTask.execute();

						}

						break;

					case -1:
						showTextToast(filesave.getName() + context.getString(R.string.error));
						resultView.setVisibility(TextView.INVISIBLE);
						progressBar.setVisibility(progressBar.INVISIBLE);
						button.setClickable(true);
						break;
					}
				}
			};

		}
		private void showTextToast(String msg) {
			Toast toast=null;
			if (toast == null) {
				toast = Toast.makeText(context, msg,
						Toast.LENGTH_SHORT);
			} else {
				toast.setText(msg);
			}
			toast.show();
		}

		private void showTips() {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("无法连接网络");
			builder.setMessage("检测到您当前网络处于未连接状态，是否设置网络？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 用户设置网络
					context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					Toast.makeText(context, "请您设置网络...",
							Toast.LENGTH_SHORT).show();

				}
			});
			builder.create();
			builder.show();
		}

	}
}
