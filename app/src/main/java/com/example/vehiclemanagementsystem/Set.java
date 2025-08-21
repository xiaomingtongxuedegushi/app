package com.example.vehiclemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehiclemanagementsystem.ui.addbotton.Addbotton;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.example.vehiclemanagementsystem.student;
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

import static com.xuexiang.xui.XUI.getContext;

public class Set extends AppCompatActivity implements View.OnClickListener {
    private MaterialSpinner set_class;
    private ButtonView set_btn;
    private JSONArray jx_grade;
    private int i=0;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        initview();
        getgrade();
        getstudent();
    }
//获取当前的学号
    private void getstudent() {
        try {
            JSONObject js=new JSONObject(student.getRess());
            user=js.getString("sid");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取班级列表
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
                        new MaterialDialog.Builder(Set.this)
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
    //json解析班级列表
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
        set_class.setItems(classlist);
    }
    private void initview() {
     set_btn=findViewById(R.id.set_btn);
     set_btn.setOnClickListener(this);
     set_class=findViewById(R.id.set_grade);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.set_btn:
                postclass();
                break;

        }
    }
//修改班级
    private void postclass() {
        String grade=set_class.getText().toString();
        int classid = 0;
        for (i=0;i<jx_grade.length();i++){
            try {
                JSONObject jx = jx_grade.getJSONObject(i);
                if(jx.getString("cl_name").equals(grade)){
                    classid=jx.getInt("id");
                    System.out.println("记录的班级号是"+classid);
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
                .addFormDataPart("class", String.valueOf(classid))
                .build();
        Request request = new Request.Builder()
                .url(Constant.post_set_class)
                .method("POST", body)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(Set.this)
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
                final String res=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("error")){
                            new MaterialDialog.Builder(Set.this)
//                                    .iconRes(R.drawable.icon_tip)
                                    .title(R.string.tip_infos)
                                    .content("请先检查网络或联系教师")
                                    .positiveText(R.string.lab_submit)
                                    .show();
                        }
                        else {
                            Intent longin_intent=new Intent();
                            longin_intent.setClass(getApplication(), MainActivity.class);
                            startActivity(longin_intent);
                            new MaterialDialog.Builder(Set.this)
//                                    .iconRes(R.drawable.icon_tip)
                                    .title(R.string.tip_infos)
                                    .content("修改成功")
                                    .positiveText(R.string.lab_submit)
                                    .show();

                        }
                    }
                });
            }
        });
    }


}
