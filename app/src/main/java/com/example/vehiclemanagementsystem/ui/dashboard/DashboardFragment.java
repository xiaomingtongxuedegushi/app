package com.example.vehiclemanagementsystem.ui.dashboard;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclemanagementsystem.student;
import com.example.vehiclemanagementsystem.R;
import com.example.vehiclemanagementsystem.databinding.FragmentDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class DashboardFragment extends Fragment  {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private WebView web_view;
    private Object view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        web_view = root.findViewById(R.id.web_view);
        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        getstudent();

//        web_view.loadUrl("https://www.baidu.com/");
        web_view.loadUrl("file:///android_asset/dist/index.html");

        web_view.setWebViewClient(new WebViewClient(){
            //加载完成的时候会回调
            @Override
            public void onPageFinished(WebView webView, String s) {
                System.out.println("over");
                String classid = getclass();
                System.out.println(classid);
                web_view.evaluateJavascript("javascript:scanCallBack('"+classid+"')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
                String sid = getstudent();
                System.out.println(sid);
                web_view.evaluateJavascript("javascript:testCallBack('"+sid+"')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        });
        return root;

    }

    private String getstudent() {
        //获取当前的学号
        String user;
            try {
                JSONObject js=new JSONObject(student.getRess());
                user=js.getString("sid");

                return user;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
    }
    private String getclass() {
        //获取当前的学号
        String user;
        String classid;
        try {
            JSONObject js=new JSONObject(student.getRess());
            user=js.getString("belong_class");
            JSONObject classes = new JSONObject(user);
            classid = classes.getString("id");
            return classid;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //加载完成的时候会回调




}