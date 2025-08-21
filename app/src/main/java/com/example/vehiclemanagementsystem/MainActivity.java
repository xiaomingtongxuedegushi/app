package com.example.vehiclemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.IOException;
//import c
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="MainActivity";
    private ButtonView btn_login,txt_cp;
    private EditText edit_user;
    private EditText edit_password;

    private ButtonView btn_register;
    private Button btn;
    private MaterialSpinner spinner_grade;
    private MaterialSpinner spinner_gender;
    private CheckBox checkBox1;
    private SharedPreferences sp;
    private static boolean isExit=false;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            isExit=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        sp=this.getSharedPreferences("config",0);//获得简单存储
        String sid =sp.getString("sp_user","");//得到账号
        String pwderror=sp.getString("sp_password","");//得到密码
        edit_user.setText(sid);
        edit_password.setText(pwderror);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit() {
        if (!isExit){
            isExit=true;
            Toast.makeText(getApplicationContext(),"在按一次退出该程序",Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0,2000);
        }else {
            finish();
            System.exit(0);
        }
    }

    private void initView() {
        btn_login = findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
//下拉框
        spinner_gender=findViewById(R.id.spinner_gender);
        spinner_grade=findViewById(R.id.spinner_grade);

        txt_cp=findViewById(R.id.dl_btn_cp);

////输入框
        edit_user = findViewById(R.id.edit_user);
        edit_password = findViewById(R.id.edit_password);
        String mxsss="w";
        checkBox1=findViewById(R.id.remember);//声明一个简单存储sharedpreferences 来记住密码


        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        txt_cp.setOnClickListener(this);
//        imgbtn_set.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                if(edit_user.getText().toString()=="" && edit_password.getText().toString()==""){
                    new MaterialDialog.Builder(MainActivity.this)
                                    .iconRes(R.drawable.icon_arrow_right_red)
                            .title(R.string.tip_infos)
                            .content("学号或密码不能为空")
                            .positiveText(R.string.lab_submit)
                            .show();
                }else {
                    posttest();
                    }
                break;

            case R.id.btn_register:
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(), Register.class);
                startActivity(intent);
                break;

            case R.id.dl_btn_cp:
                Intent inten=new Intent();
                inten.setClass(getApplicationContext(),Judge.class);
                startActivity(inten);
                break;


        }
    }

    private void posttest(){

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("sid",edit_user.getText().toString())
                .addFormDataPart("password",edit_password.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(Constant.Post_url)
                .method("POST", body)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e(TAG, "失败进入Failure" );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(MainActivity.this)
//                                    .iconRes(R.drawable.icon_tip)
                                .title(R.string.tip_infos)
                                .content("请先检查网络或联系教师")
                                .positiveText(R.string.lab_submit)
                                .show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String sp_user=edit_user.getText().toString().trim();
                String sp_password=edit_password.getText().toString().trim();
                runOnUiThread(new Runnable() {
                    final String res=response.body().string();

                    @Override
                    public void run() {


                        System.out.println(res);
                        if (res.equals("\"pwderror\"")){
//                            txt_state.setText("登录失败");
                            new MaterialDialog.Builder(MainActivity.this)
//                                    .iconRes(R.drawable.icon_tip)
                                    .title(R.string.tip_infos)
                                    .content("密码错误")
                                    .positiveText(R.string.lab_submit)
                                    .show();
                            Log.e(TAG, "登录未出结果" );
                        }else if (res.equals("\"None\"")){
                            new MaterialDialog.Builder(MainActivity.this)
//                                    .iconRes(R.drawable.icon_tip)
                                    .title(R.string.tip_infos)
                                    .content("学号错误")
                                    .positiveText(R.string.lab_submit)
                                    .show();
//                            txt_state.setText("学号错误");

                            Log.e(TAG, "登录已出结果" );
//                            res.equals("\"ok\"")
                        }else  if(res.length()>10){
//                            txt_state.setText("登录成功");

                            student.setRess(res);
                            Intent longin_intent=new Intent();
                            longin_intent.setClass(MainActivity.this,Navigation_botton.class);
                            MainActivity.this.startActivity(longin_intent);

                        }
                        if (checkBox1.isChecked()){//判断是否记住密码
                            Log.i(TAG, "run: 执行记住密码");
                            SharedPreferences.Editor editor =sp.edit();//得到编辑框的内容
                            editor.putString("sp_user",sp_user);//写入账号
                            editor.putString("sp_password", sp_password);//写入密码
                            System.out.println(sp_user);
                            System.out.println(sp_password);
                            editor.commit();//保存完数据后，一定要记得提交

                        }else {
                            Log.i(TAG, "run: 不需要记住密码");
                        }

                    }

                });


            }
        });
    }

}

