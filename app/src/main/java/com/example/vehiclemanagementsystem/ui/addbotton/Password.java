package com.example.vehiclemanagementsystem.ui.addbotton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehiclemanagementsystem.Constant;
import com.example.vehiclemanagementsystem.MainActivity;
import com.example.vehiclemanagementsystem.R;
import com.example.vehiclemanagementsystem.Set;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.example.vehiclemanagementsystem.student;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Password extends AppCompatActivity implements View.OnClickListener {
    private ButtonView ps_btn;
    private PasswordEditText new_password,former_password;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        initView();
        getstudent();

    }
    //获取学号 
    private void getstudent() {
        try {
            JSONObject js=new JSONObject(student.getRess());
            user=js.getString("sid");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //初始化控件
    private void initView() {
        new_password=findViewById(R.id.ps_new_password);
        former_password=findViewById(R.id.ps_former_password);
        ps_btn=findViewById(R.id.ps_btn);
        ps_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ps_btn:
                postnewpassword();
                break;
        }
    }

    private void postnewpassword() {
        String newpassword=new_password.getText().toString().trim();
        String formerpassword=former_password.getText().toString().trim();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("sid",user)
                .addFormDataPart("oldpassword",formerpassword)
                .addFormDataPart("password",newpassword)
                .build();
        Request request = new Request.Builder()
                .url(Constant.post_set_Password)
                .method("POST", body)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(Password.this)
//                                    .iconRes(R.drawable.icon_tip)
                                .title(R.string.tip_infos)
                                .content("请先检查网络或联系教师")
                                .positiveText(R.string.lab_submit)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("记录的账号是："+user);
                System.out.println("记录的旧账号是："+formerpassword);
                System.out.println("记录的新账号是："+newpassword);
                final String res=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("进入修改环节");
                         if (res.equals("\"ok\"")) {
                             new MaterialDialog.Builder(Password.this)
//                                    .iconRes(R.drawable.icon_tip)
                                     .title(R.string.tip_infos)
                                     .content("修改成功")
                                     .positiveText(R.string.lab_submit)
                                     .show();
                             Intent longin_intent=new Intent();
                             longin_intent.setClass(getApplication(), MainActivity.class);
                             startActivity(longin_intent);
                         }
                         if (res.equals("\"olderror\"")){
                            new MaterialDialog.Builder(Password.this)
//                                    .iconRes(R.drawable.icon_tip)
                                    .title(R.string.tip_infos)
                                    .content("旧密码错误")
                                    .positiveText(R.string.lab_submit)
                                    .show();
                        }
                         else{
                             new MaterialDialog.Builder(Password.this)
//                                    .iconRes(R.drawable.icon_tip)
                                     .title(R.string.tip_infos)
                                     .content("请先检查网络或联系教师")
                                     .positiveText(R.string.lab_submit)
                                     .show();
                         }


//                        else if(res.equals("error")){
//                            new MaterialDialog.Builder(Password.this)
////                                    .iconRes(R.drawable.icon_tip)
//                                    .title(R.string.tip_infos)
//                                    .content("修改失败")
//                                    .positiveText(R.string.lab_submit)
//                                    .show();
//                        }

                    }
                });
            }
        });
    }
}