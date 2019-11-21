package com.android.svod;

import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by ycm on 2017/5/3.
 */
public class PersonalActivity extends Activity{
    private String studentCode;
    private WebView webView;
    private LinearLayout linearLayout,personLinearLayout;
    private TextView textView;
    private String scoreUrl="https://weixin.xjtudlc.com/WeixinServer/stuexam.aspx?stucode=";
    private String priceUrl="https://weixin.xjtudlc.com/WeixinServer/stuexpenses.aspx?stucode=";
    private String personalUrl="https://weixin.xjtudlc.com/WeixinServer/Studentbaseinfo.aspx?stucode=";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalcenter);
        Bundle bundle=getIntent().getExtras();
        String stucode=bundle.getString("stucode");
        String catagory=bundle.getString("catagory");
        webView=(WebView) findViewById(R.id.webViewPersonal);
        textView=(TextView) findViewById(R.id.center);
        linearLayout=(LinearLayout) findViewById(R.id.back1);
        personLinearLayout=(LinearLayout) findViewById(R.id.personalLinearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalActivity.this.finish();
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    personLinearLayout.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        if(catagory.equals("score")){
            scoreUrl=scoreUrl+stucode;
            textView.setText("成绩信息");
            System.out.println("scoreUrl:"+scoreUrl);
            webView.loadUrl(scoreUrl);
        }else if(catagory.equals("price")){
           priceUrl=priceUrl+stucode;
            textView.setText("费用信息");
            System.out.println("priceUrl:"+priceUrl);
            webView.loadUrl(priceUrl);
        }else{
            personalUrl=personalUrl+stucode;
            textView.setText("个人信息");
            System.out.println("personalUrl:"+personalUrl);
             webView.loadUrl(personalUrl);
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
