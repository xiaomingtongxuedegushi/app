package com.example.vehiclemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.ClearEditText;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.toast.XToast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="MainActivity";
    private ButtonView btn_register_zc;
    private ClearEditText edit_register_user;
    private ClearEditText edit_register_name;
    private PasswordEditText edit_register_password;
    private MaterialSpinner spinner_gender;
    private MaterialSpinner spinner_grade;
    private JSONArray jx_grade;
//    private String right_an[] = null;
    private int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getgrade();
        initview();



    }

    private void getgrade() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Constant.get_grade)
                .method("GET", null)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(Register.this)
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


                final String string = response.body().string();
                System.out.println("第一次的结果"+string);
                try {
                    jx_grade=new JSONArray(string);
                jsongrade();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void jsongrade() {
        List classlist = new ArrayList();
        classlist.add(0,"");

        for (i = 0; i < jx_grade.length(); i++) {
            try {
                JSONObject object = jx_grade.getJSONObject(i);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            classlist.add(object.getString("cl_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner_grade.setItems(classlist);

        }



    private void initview() {
        //button
        btn_register_zc = findViewById(R.id.btn_register_zc);
        btn_register_zc.setOnClickListener(this);
        //输入框
        edit_register_user=findViewById(R.id.edit_register_user);
        edit_register_password=findViewById(R.id.edit_register_password);
        edit_register_name=findViewById(R.id.edit_register_name);
        //下拉框
        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_grade = findViewById(R.id.spinner_grade);

//        txt_js = findViewById(R.id.txt_js);


    }

    @Override
    public void onClick(View v) {
        //点击注册  获取学号、密码、性别、班级的内容 保存到一个类，  之后上传到数据库 注册成功显示提示框点击返回登录界面
        String user=edit_register_user.getText().toString().trim();
        String password=edit_register_password.getText().toString().trim();
        String name=edit_register_name.getText().toString().trim();
        String gender=spinner_gender.getText().toString();
        String grade=spinner_grade.getText().toString();
         switch (v.getId()){
             case R.id.btn_register_zc:


                 if(TextUtils.isEmpty(user)) {
                     Toast.makeText(this, "学号输入为空", Toast.LENGTH_SHORT).show();
                 }
                 else {

                     if (TextUtils.isEmpty(password)){
                         Toast.makeText(this, "密码输入为空", Toast.LENGTH_SHORT).show();
                     }
                     if (TextUtils.isEmpty(name)){
                         Toast.makeText(this, "姓名输入为空", Toast.LENGTH_SHORT).show();
                     }
                     if (TextUtils.isEmpty(gender)){
                         Toast.makeText(this, "性别输入为空", Toast.LENGTH_SHORT).show();
                     }
                     if (TextUtils.isEmpty(grade)){
                         Toast.makeText(this, "班级输入为空", Toast.LENGTH_SHORT).show();
                     }
                     else{
                         Toast.makeText(this, "正在注册请稍后", Toast.LENGTH_SHORT).show();
                         postRegister();
                         System.out.println("存储的学号是："+user );
                         System.out.println("存储的密码是"+password);
                         System.out.println("存储的账号是："+name );
                         System.out.println("存储的性别是"+gender);
                         System.out.println("存储的班级是"+grade);

                        break;
                         //写post   网络故障  2 学号已存在   3注册成功点击返回主界面

                     }

                 }
         }
    }

    public void postRegister() {

        String user=edit_register_user.getText().toString().trim();
        String password=edit_register_password.getText().toString().trim();
        String name=edit_register_name.getText().toString().trim();
        String gender=spinner_gender.getText().toString();
        String grade=spinner_grade.getText().toString();
        int classid = 0;



        if (jx_grade.length()!=0) {



            for (i = 0; i < jx_grade.length(); i++) {
                try {
                    JSONObject jx = jx_grade.getJSONObject(i);
                    if (jx.getString("cl_name").equals(grade)) {
                        classid = jx.getInt("id");
                        System.out.println(classid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("sid",user)
                    .addFormDataPart("password",password)
                    .addFormDataPart("name",name)
                    .addFormDataPart("sex",gender)
                    .addFormDataPart("class", String.valueOf(classid))
                    .build();
            Request request = new Request.Builder()
                    .url(Constant.Post_Register)
                    .method("POST", body)
                    .build();
            Call call=client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new MaterialDialog.Builder(Register.this)
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
                    final String res=response.body().string();
                    System.out.println("返回的："+res);
                    runOnUiThread(new Runnable() {


                        @Override

                        public void run() {
                            if (res.equals("\"register\"")){
                                Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            if (res.equals("\"error\"")) {
                                new MaterialDialog.Builder(Register.this)
//                                    .iconRes(R.drawable.icon_tip)
                                        .title(R.string.tip_infos)
                                        .content("注册失败，请联系教师")
                                        .positiveText(R.string.lab_submit)
                                        .show();
                            }
                            if (res.equals("\"same\"")){
                                new MaterialDialog.Builder(Register.this)
//                                    .iconRes(R.drawable.icon_tip)
                                        .title(R.string.tip_infos)
                                        .content("用户已存在请联系管理员和老师")
                                        .positiveText(R.string.lab_submit)
                                        .show();

                            }
                            else {
                                new MaterialDialog.Builder(Register.this)
//                                    .iconRes(R.drawable.icon_tip)
                                        .title(R.string.tip_infos)
                                        .content("请检查下网络或联系老师")
                                        .positiveText(R.string.lab_submit)
                                        .show();
                            }


                        }

                    });
                }
            });
        }
        else {
            new MaterialDialog.Builder(Register.this)
//                                    .iconRes(R.drawable.icon_tip)
                    .title(R.string.tip_infos)
                    .content("请检查下网络或联系老师")
                    .positiveText(R.string.lab_submit)
                    .show();
        }

    }

}
