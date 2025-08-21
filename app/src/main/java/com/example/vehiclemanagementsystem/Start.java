package com.example.vehiclemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {
//启动界面
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_start);
        Thread myThread=new Thread(){
            @Override
            public void run(){
                try {
                    sleep(5000);//使程序休眠五秒
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);//启动MainActivity
                    startActivity(intent);
                    finish();//关闭当前活动

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        myThread.start();

    }

}
