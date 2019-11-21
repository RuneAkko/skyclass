package com.android.svod;

/**
 * Created by Administrator on 2017/12/22.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JsResult;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.os.Bundle;
import com.android.encrypt.MD5;
import com.android.svod.Login;

import com.android.connection.GetRequest;
import com.android.connection.LoginCheck;


import com.umeng.analytics.MobclickAgent;

//skyclass首页
public class MBBS extends Activity {
    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpage);

        init();

        String stdent = Login.studentcode;
        String username=Login.userNumber;
        String usertype="0";
        String from = "202.117.16.41";
        String password = "coursebbs.open.com.cn";
        String dyurl=username+usertype+from+password;
        String key=MD5.MD5(dyurl);
        String MBBSUrl="https://mbbs.xjtudlc.com/coursebbs/login.aspx?username="+username+"&usertype=0&from=202.117.16.41&key="+key+"&cmd=main&parm=&studentcode="+stdent;

        //webView.loadDataWithBaseURL(MBBSUrl,getNewContent(getContent()),"text/html","utf-8",null);
        String MBBSUrl_2019_11_test = "https://www.imooc.xjtudlc.com/course/explore";
        webView.loadUrl(MBBSUrl_2019_11_test);
    }


    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setSupportZoom(true);
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgReset();//重置webview中img标签的图片大小
                // html加载完成之后，添加监听图片的点击js函数
                //addImageClickListner();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });


    }


    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
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

