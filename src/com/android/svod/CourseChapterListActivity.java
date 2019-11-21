package com.android.svod;

import com.android.domain.Course;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.domain.ClassBean;
import com.android.domain.Dtcourse;
import com.android.domain.Dtplaycourse;
import com.android.domain.Dtstructure;
import com.android.network.DownloadProgressListener;
import com.android.network.FileDownloader;
import com.android.sql.CourseService;
import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.DtstructureService;
import com.android.sql.FileService;
import com.android.sql.FileService2;
import com.android.updater.UpdateConfig;
import com.android.updater.UpdateService;
import com.android.updater.UpdateUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.SlidingDrawer.OnDrawerCloseListener;

public class CourseChapterListActivity extends Activity implements
        ViewSwitcher.ViewFactory {

    private static final String TAG = "CourseChapterListActivity";

    public static final int FINISHDOWNLOAD = 1;
    public static final int NOTFINISH = 2;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;


    private ExpandableListView chapterList;
    //	private Button iv;
    private ArrayList<ClassBean> cb;
    private int temp;
    private String studentcode;
    private String courseName;
    private String courseid;
    private DtstructureService dtstructureService;
    private DtplaycourseService dtplaycourseService;
    private DtcourseService dtcourseService;
    private CourseService courseService;


    FileService fileService;
    FileService2 fileService2;

    boolean flag = false;
    private Toast toast = null;
    String Tag = "System.out";

    //ArrayList<Dtstructure> dtstructures = new ArrayList<Dtstructure>();
    ArrayList<Dtcourse> dtcourses = new ArrayList<Dtcourse>();
    //ArrayList<Course> dtcourses = new ArrayList<Course>();
    ArrayList<Dtplaycourse> dtplaycourses = new ArrayList<Dtplaycourse>();
    Map<String, childView> childViewMap = new HashMap<String, childView>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        studentcode = bundle.getString("studentcode");
        temp = bundle.getInt("temp");
        courseName = bundle.getString("courseName");
        courseid = bundle.getString("courseid");

        setContentView(R.layout.coursechapter);

        fileService = new FileService(this.getApplicationContext());
        fileService2 = new FileService2(this.getApplicationContext());

        //dtstructureService = new DtstructureService(this.getApplicationContext());

        dtplaycourseService = new DtplaycourseService(this.getApplicationContext());
        dtcourseService = new DtcourseService(this.getApplicationContext());
        courseService = new CourseService(this.getApplicationContext());

        dtcourses = dtcourseService.find();
        //dtcourses = courseService.findCS();

        chapterList = (ExpandableListView) findViewById(R.id.ChapterList);
        chapterList.setGroupIndicator(null);
        cb = loadXML();
        // chapterList.setDivider(getResources().getDrawable(R.color.divider));
        chapterList.setChildDivider(getResources().getDrawable(
                R.color.textcolor));

        //	iv = (Button) findViewById(R.id.imageViewIcon);

        final Cadapter cadapter = new Cadapter();

        DirectoryHelper fileHelper = new DirectoryHelper();
        fileHelper.getSdCardPath();

        dtplaycourses = dtplaycourseService.findC(dtcourses.get(temp).getcno());
        if (dtplaycourses.size() == 0) {
            Toast.makeText(CourseChapterListActivity.this, "课程资源建设中",
                    Toast.LENGTH_SHORT).show();
        } else {
            chapterList.setAdapter(cadapter);
            chapterList.setOnGroupExpandListener(new OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    for (int i = 0; i < cb.get(temp).getSubClass().size(); i++) {
                        if (groupPosition != i) {
                            // chapterList.collapseGroup(i);
                        }
                    }
                }

            });
            chapterList.removeAllViewsInLayout();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mNoticeStartIndex = 1;
        // mNoticeHandle.initNoticeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // Log.d(TAG, "The OBLCourseCategoryActivity is pause");
        super.onPause();
        MobclickAgent.onPause(this);

    }

    private class groupView {
        public TextView textView;
    }

    private class childView {
        public Handler downloadhandler = new Handler() {
            public void handleMessage(Message msg) {
                View view = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.sectionlist1, null);
                switch (msg.what) {
                    case FINISHDOWNLOAD:
                        button1.setVisibility(View.GONE);
                        button2.setVisibility(View.VISIBLE);
                        //button2.setText("本地播放");
                        button2.setClickable(true);
                        button3.setVisibility(View.GONE);
                        button4.setVisibility(View.VISIBLE);
                        //button4.setText("删除");
                        button4.setClickable(true);
                        break;
                    case NOTFINISH:

                        button1.setVisibility(View.VISIBLE);
                        button1.setClickable(true);
                        button2.setVisibility(View.GONE);
                        button3.setVisibility(View.VISIBLE);
                        button3.setClickable(true);
                        button4.setVisibility(View.GONE);
                        break;

                }

            }
        };

        public childView(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            this.view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.sectionlist1, null);
            this.view1 = (TextView) this.view.findViewById(R.id.section);
            this.button1 = (Button) view.findViewById(R.id.download);
            this.button2 = (Button) view.findViewById(R.id.play);
            this.button3 = (Button) view.findViewById(R.id.playonline);
            this.button4 = (Button) view.findViewById(R.id.delete);
            this.pb = (ProgressBar) view.findViewById(R.id.downloadbar);

            this.resultView = (TextView) view.findViewById(R.id.resultView);

            this.se = cb.get(temp).getsubSubClass().get(groupPosition)
                    .get(childPosition);
            this.id = cb.get(temp).getpath().get(groupPosition)
                    .get(childPosition);
            this.className = courseName;
            this.path = "mnt/sdcard/Skyclass"
                    + "/"
                    + this.className.replace("/", "").replace("\\", "")
                    .replace("*", "").replace(":", "").replace("|", "")
                    .replace("\"", "").replace("<", "")
                    .replace(">", "").replace("?", "")
                    + "_"
                    + se.replace("/", "").replace("\\", "").replace("*", "")
                    .replace(":", "").replace("|", "")
                    .replace("\"", "").replace("<", "")
                    .replace(">", "").replace("?", "") + ".mp4";
            // �ļ�����·��
//			Log.d(TAG,"" + path);
            this.uri = Uri.parse(path);
            this.filesave = new File(path);
            this.view1.setText(se);
            this.view1.setTextSize(15);
            this.view1.setTextColor(getResources().getColor(R.color.textcolor));
            // ��
            final String filePath = "https://" + id.substring(5);
            if (!filesave.exists()) {
                Message message = new Message();
                message.what = NOTFINISH;
                downloadhandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = FINISHDOWNLOAD;
                downloadhandler.sendMessage(message);
            }
            this.button1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ConnectivityManager manager =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    State mobile = manager.getNetworkInfo(
                            ConnectivityManager.TYPE_MOBILE).getState();
                    State wifi = manager.getNetworkInfo(
                            ConnectivityManager.TYPE_WIFI).getState();

                    if ((mobile == State.CONNECTED || mobile == State.CONNECTING)
                            || (wifi == State.CONNECTED || wifi == State.CONNECTING)) {

                        pb.setVisibility(ProgressBar.VISIBLE);
                        resultView.setVisibility(View.VISIBLE);

                        System.out.println(Environment
                                .getExternalStorageState()
                                + "------"
                                + Environment.MEDIA_MOUNTED);
                        if (Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            // 开始下载文件
                            System.out.println("......." + id + "...........");
                            System.out.println("......." + filesave + "...........");
                            String downPath = path.substring(20, path.length());
                            download(filePath, filesave, pb, resultView, button1, downPath);
                            button1.setClickable(false);
                        } else {
                            // 显示SDCard错误信息
                            Toast.makeText(CourseChapterListActivity.this,
                                    R.string.sdcarderror, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showTips();
                    }

                }
            });
            //本地播放
            this.button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecordDialogFragment recordDialog2 = new RecordDialogFragment();
                    Bundle args2 = new Bundle();
                    args2.putString("mpath", path);
                    System.out.println("............" + path + "..............");
                    args2.putString("title", "Video.mp4");
                    args2.putString("studentcode", studentcode);
                    args2.putString("courseid", courseid);
                    args2.putString("path", se);
                    args2.putBoolean("isOnlinePlay", false);
                    recordDialog2.setArguments(args2);
                    recordDialog2.show(getFragmentManager(), "Record");

                }
            });
            //在线播放
            this.button3.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ConnectivityManager manager =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    State mobile = manager.getNetworkInfo(
                            ConnectivityManager.TYPE_MOBILE).getState();
                    State wifi = manager.getNetworkInfo(
                            ConnectivityManager.TYPE_WIFI).getState();

                    if ((mobile == State.CONNECTED || mobile == State.CONNECTING)
                            || (wifi == State.CONNECTED || wifi == State.CONNECTING)) {
                        RecordDialogFragment recordDialog2 = new RecordDialogFragment();
                        Bundle args2 = new Bundle();
                        args2.putString("mpath", id);
                        args2.putString("studentcode", studentcode);
                        args2.putString("courseid", courseid);
                        args2.putString("path", se);
                        args2.putBoolean("isOnlinePlay", true);
                        recordDialog2.setArguments(args2);
                        recordDialog2.show(getFragmentManager(), "Record");
                    } else {
                        //此时不存在网络的情况
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                CourseChapterListActivity.this);
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setTitle("无法连接网络");
                        builder.setMessage("检测到您当前网络处于未连接状态，是否设置网络？");
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 用户设置网络
                                        startActivity(new Intent(
                                                Settings.ACTION_WIRELESS_SETTINGS));
                                    }
                                });
                        builder.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                });
                        builder.create();
                        builder.show();
                    }
                }
            });
            this.button4.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    // 弹出确认框
                    Builder builder = new Builder(
                            CourseChapterListActivity.this);
                    View view =
                            LayoutInflater.from(getApplicationContext()).inflate(R.layout.deletedialog, null);
                    builder.setView(view);
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    if (filesave.exists()) {
                                        filesave.delete();
                                        Toast.makeText(
                                                CourseChapterListActivity.this,
                                                "成功删除" + filesave.getName(),
                                                Toast.LENGTH_SHORT).show();
                                        Message message = new Message();
                                        message.what = NOTFINISH;
                                        downloadhandler.sendMessage(message);
                                    }

                                }
                            }).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            });

                    builder.create().show();
                }
            });

        }

        public View view;
        public int groupPosition;
        public int childPosition;
        public TextView view1;
        public Button button1;
        public Button button2;
        public Button button3;
        public Button button4;
        public ProgressBar pb;
        public String se;
        public String id;
        public String className;
        public String path;

        public TextView resultView;
        public Uri uri;
        public File filesave;
        /**
         * @param path
         * @param savedir
         */
        private DownloadTask task;

        // 进度条线程
        private void download(final String path, final File savedir,
                              ProgressBar progressBar, TextView resultView, Button button,
                              String filename) {
            task = new DownloadTask(path, savedir, progressBar, resultView,
                    button, filename);
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
            FileDownloader loader;
            String filename;

            public DownloadTask(String path, File savedir, ProgressBar progressBar,
                                TextView resultView, Button button, String filename) {
                this.path = path;
                this.savedir = savedir;
                System.out.println("........" + savedir + ".......");
                this.progressBar = progressBar;
                this.resultView = resultView;
                this.button = button;
                this.filename = filename;
            }

            public void exit() {
                if (loader != null) {
                    loader.exit();// �˳�����
                }
            }

            @Override
            public void run() {
                try {
                    loader = new FileDownloader(getApplicationContext(), path,
                            savedir, 3, 0, filename);
                    progressBar.setMax(loader.getFileSize());// 设置进度条最大刻度
                    loader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int size) {
                            Message msg = new Message();
                            msg.what = 1;// 定义消息的ID，以便区别哪个消息发过来的
                            msg.getData().putInt("size", size);
                            handler.sendMessage(msg);

                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(-1));// 发送编号为-1的空消息
                }

            }

            /**
             *
             */
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
                            float num = (float) progressBar.getProgress()
                                    / (float) progressBar.getMax();
                            int result = (int) (num * 100);

                            if (result != 100) {
                                resultView.setText(result + "%");
                                button.setClickable(false);
                            }
                            if (progressBar.getProgress() == progressBar.getMax()) {
                                flag = true;
                                AsyncTask<String, Integer, String> multiTask =
                                        new AsyncTask<String, Integer, String>() {

                                            @Override
                                            protected void onPostExecute(String result) {
                                                // TODO Auto-generated method stub
                                                super.onPostExecute(result);
                                                Message message = new Message();
                                                message.what = FINISHDOWNLOAD;
                                                downloadhandler.sendMessage(message);
                                            }

                                            @Override
                                            protected String doInBackground(
                                                    String... params) {
                                                // TODO Auto-generated method stub
                                                String path = loader.saveFile.getPath();
                                                System.out.println("啊啊啊啊啊啊啊" + path + "啊啊啊啊啊啊啊啊啊");
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
                            /* Toast.makeText(Page1.this, R.string.error, 1).show(); */
                            showTextToast(filesave.getName()
                                    + getString(R.string.error));
                            resultView.setVisibility(TextView.INVISIBLE);
                            progressBar.setVisibility(progressBar.INVISIBLE);
                            button.setClickable(true);
                            break;
                    }
                }
            };

        }

    }

    class Cadapter extends BaseExpandableListAdapter {

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return cb.get(temp).getSubClass().get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return cb.get(temp).getSubClass().size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View contentView, ViewGroup parent) {
            // TODO Auto-generated method stub
            String ch = cb.get(temp).getSubClass().get(groupPosition);
            groupView group;
            if (contentView == null) {
                contentView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.chapterlist, null);
                group = new groupView();
                group.textView = (TextView) contentView
                        .findViewById(R.id.chapter);
                contentView.setTag(group);
            } else
                group = (groupView) contentView.getTag();

            group.textView.setText(ch);
            group.textView.setTextSize(17);
            group.textView.setTextColor(getResources().getColor(R.color.textcolor));
            // group.textView.setTextColor(android.graphics.Color.BLUE);
            return contentView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return cb.get(temp).getsubSubClass().get(groupPosition)
                    .get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition,
                                 final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            String tempTag = temp + String.valueOf(groupPosition) + "-"
                    + String.valueOf(childPosition);
            if (childViewMap.get(tempTag) == null) {
                childView child = new childView(groupPosition, childPosition);
                childViewMap.put(tempTag, child);
                return child.view;
            } else
                return childViewMap.get(tempTag).view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return cb.get(temp).getsubSubClass().get(groupPosition).size();
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            // Toast.makeText(getBaseContext(),"nihao",Toast.LENGTH_SHORT).show();

            return false;
        }

        @Override
        public boolean isChildSelectable(final int groupPosition,
                                         final int childPosition) {

            // TODO Auto-generated method stub
            return true;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
        }

        return super.onKeyDown(keyCode, event);
    }

    // 检查网络状态
    public void CheckNetworkState() {

        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // State mobile =
        // manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        // .getState();
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        // 网络状态连接则退出，否则进入网络设置页面
        // if (mobile == State.CONNECTED || mobile == State.CONNECTING)
        // /* {if(userservice.findExist(studentcode))

        // showSendTips();}*/
        // return;
        if (wifi == State.CONNECTED || wifi == State.CONNECTING)
            /*
             * {if(userservice.findExist(studentcode)) System.out.println("记录");
             * showSendTips();}
             */
            return;
        showTips();
    }

    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(CourseChapterListActivity.this, msg,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("无法连接网络");
        builder.setMessage("检测到您当前网络处于未连接状态，是否设置网络？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户设置网络
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(CourseChapterListActivity.this, "请您设置网络...",
                        Toast.LENGTH_SHORT).show();

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(36);
        return t;
    }

    // 解析xml获取当前课程科目
    public ArrayList<ClassBean> loadXML() {
        ArrayList<ClassBean> CB = new ArrayList<ClassBean>();
        //ArrayList<Dtcourse> dtcourses = CourseService.findCS();
        ArrayList<Dtcourse> dtcourses = dtcourseService.find();
        String path = "";
        for (int i = 0; i < dtcourses.size(); i++) {
            ArrayList<String> temp = new ArrayList<String>();
            ArrayList<ArrayList<String>> temp2 = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> temp4 = new ArrayList<ArrayList<String>>();
            //ArrayList<Dtstructure> dtstructures = dtstructureService.find(dtcourses.get(i)
            // .getid());
            String first = dtcourses.get(i).getname();

            CB.add(new ClassBean(first));
            //if (dtstructures.size() == 0) {
            ArrayList<Dtplaycourse> dtplaycourses =
                    dtplaycourseService.findC(dtcourses.get(i).getcno());
            for (int j = 0; j < 1; j++) {
                String second = first;
                temp.add(second);
                ArrayList<String> temp3 = new ArrayList<String>();
                ArrayList<String> temp5 = new ArrayList<String>();
                for (int k = 0; k < dtplaycourses.size(); k++) {
                    String third = dtplaycourses.get(k).getname();
                    temp3.add(third);
                    path = dtplaycourses.get(k).getsvpath();
                    //path = dtplaycourses.get(k).getsvpath_abs();
                    temp5.add(path);
                }
                temp2.add(temp3);
                temp4.add(temp5);
            }
			/*} else {
				for (int j = 0; j < dtstructures.size(); j++) {
					ArrayList<Dtplaycourse> dtplaycourses = dtplaycourseService.find(dtstructures
					.get(j).getid());
					String second = dtstructures.get(j).getname();
					System.out.println(second);
					temp.add(second);
					ArrayList<String> temp3 = new ArrayList<String>();
					ArrayList<String> temp5 = new ArrayList<String>();
					for (int k = 0; k < dtplaycourses.size(); k++) {
						String third = dtplaycourses.get(k).getname();
						System.out.println(third);
						temp3.add(third);
						path = dtplaycourses.get(k).getsvpath();
						temp5.add(path);
					}
					temp2.add(temp3);
					temp4.add(temp5);

				}
			}*/
            CB.get(i).setSubClass(temp);
            CB.get(i).setsubSubClass(temp2);
            CB.get(i).setpath(temp4);
        }

        return CB;
    }

}