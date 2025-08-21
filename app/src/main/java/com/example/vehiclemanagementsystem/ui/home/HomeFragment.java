package com.example.vehiclemanagementsystem.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehiclemanagementsystem.R;
import com.example.vehiclemanagementsystem.Register;
import com.example.vehiclemanagementsystem.Set;
import com.example.vehiclemanagementsystem.databinding.FragmentDashboardBinding;
import com.example.vehiclemanagementsystem.databinding.FragmentHomeBinding;
import com.example.vehiclemanagementsystem.ui.addbotton.Password;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private SuperTextView txt;
    private HomeViewModel homeViewModel;
    private WebView web_view;
    ProgressBar progressBar;
    private SuperTextView super_home_kc;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        progressBar = (ProgressBar) root.findViewById(R.id.progressbar1);
        web_view = root.findViewById(R.id.hom_webview);
        progressBar.setVisibility(View.VISIBLE);
        web_view.setWebViewClient(new Browser_home());
        web_view.setWebChromeClient(new MyChrome());
        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
//        // 设置可以支持缩放
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setUseWideViewPort(true);
        if(savedInstanceState==null){
            web_view.post(new Runnable() {
                @Override
                public void run() {
                    loadWebsite();
                }
            });
        }else {
            web_view.restoreState(savedInstanceState);
        }
        return root;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        web_view.saveState(outState);
    }


    private void loadWebsite() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            web_view.loadUrl("http://pdf.car.com/");
        }else {
            web_view.setVisibility(View.GONE);
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
//            setTitle(view.getTitle());
            progressBar.setVisibility(view.GONE);

            super.onPageFinished(view, url);

        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        return BitmapFactory.decodeResource(getResources(), 2130837573);
    }

    public void onHideCustomView()
    {
        ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
        this.mCustomView = null;
        getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
        getActivity().setRequestedOrientation(this.mOriginalOrientation);
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
        this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        this.mOriginalOrientation = getActivity().getRequestedOrientation();
        this.mCustomViewCallback = paramCustomViewCallback;
        ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}


}