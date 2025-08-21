package com.example.vehiclemanagementsystem;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Judge extends AppCompatActivity {
    private WebView judge_webview;
    ProgressBar judge_progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.judge);
        judge_progressBar = (ProgressBar)  findViewById(R.id.judge_progressbar);
        judge_webview = findViewById(R.id.judge_webview);

        //开始
        judge_progressBar.setVisibility(View.VISIBLE);
        judge_webview.setWebViewClient(new Browser_home());
        judge_webview.setWebChromeClient(new MyChrome());
        WebSettings webSettings = judge_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
//        // 设置可以支持缩放
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//
//        webSettings.setUseWideViewPort(true);
        if(savedInstanceState==null){
            judge_webview.post(new Runnable() {
                @Override
                public void run() {
                    loadWebsite();
                }
            });
        }

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        judge_webview.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        judge_webview.restoreState(savedInstanceState);
    }

    private void loadWebsite() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            judge_webview.loadUrl("file:///android_asset/refe/index.html");

        }else {
            judge_webview.setVisibility(View.GONE);
        }
    }
    class Browser_home extends WebViewClient {
        Browser_home(){

        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
        }

        @Override
        public void onPageFinished(WebView view,String url){
            setTitle(view.getTitle());
            judge_progressBar.setVisibility(view.GONE);
            String classid = "2";
            judge_webview.evaluateJavascript("javascript:scanCallBack('"+classid+"')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
            String sid = "20197578";
            judge_webview.evaluateJavascript("javascript:testCallBack('"+sid+"')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });

            super.onPageFinished(view, url);

        }


    }

    private class MyChrome extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    }

