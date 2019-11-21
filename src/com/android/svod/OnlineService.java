package com.android.svod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import android.graphics.drawable.ColorDrawable;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/8/28.
 */

public class OnlineService extends Activity {
    private TextView servicename;
    private WebView webView = null;
    private Button downlistButton;
    private PopupWindow serviceSelect;
    private ArrayList<String> servicelist;
    private ArrayList<String> urllist;


    //当前选中的列表项位置
    int clickPsition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.online_service);
        servicename = (TextView) findViewById(R.id.servicename);
        downlistButton = (Button) findViewById(R.id.downlistButton);
        init();
        servicelist = getServiceList();
        urllist = getUrlList();
        downlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View myView = getLayoutInflater().inflate(R.layout.popuplist, null);
                DisplayMetrics  dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                serviceSelect = new PopupWindow(myView, screenWidth, 595, true);
                serviceSelect.setBackgroundDrawable(new ColorDrawable(0x00000000));;
                serviceSelect.setFocusable(true);
                serviceSelect.showAsDropDown(servicename);
                ListView lv = (ListView) myView.findViewById(R.id.serviceList);
                lv.setAdapter(new ListViewAdapter(OnlineService.this, servicelist));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        servicename.setText(servicelist.get(position));
                        webView.loadUrl(urllist.get(position));
                        if (clickPsition != position) {
                            clickPsition = position;
                        }
                        serviceSelect.dismiss();
                    }
                });
            }
        });

    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296");
    }

    public ArrayList<String> getServiceList() {
        ArrayList<String> servicelist = new ArrayList<String>();
        servicelist.add("非学历综合服务");
        servicelist.add("综合服务大唐");
        servicelist.add("综合服务王老师");
        servicelist.add("综合服务苗老师");
        servicelist.add("技术支持冯老师");
        servicelist.add("班主任于老师");
        servicelist.add("班主任李老师");
        servicelist.add("班主任刘老师");
        servicelist.add("招生王老师");
        servicelist.add("招生李老师");

        return servicelist;

    }

    public ArrayList<String> getUrlList() {
        ArrayList<String> urllist = new ArrayList<String>();
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=1404496");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=721040");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=1132266");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=721037");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=922717");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=982947");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=982945");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=826496");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=844621");
        urllist.add("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=60296&clerkid=844622");

        return urllist;

    }

    public class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<String> list;
        public ListViewAdapter(Context context, ArrayList<String> list) {
            super();
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lv_items, null);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.text);
            tv.setText(list.get(position));
            return convertView;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
