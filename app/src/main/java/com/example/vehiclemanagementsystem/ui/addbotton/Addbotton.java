package com.example.vehiclemanagementsystem.ui.addbotton;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.vehiclemanagementsystem.MainActivity;
import com.example.vehiclemanagementsystem.student;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vehiclemanagementsystem.R;
import com.example.vehiclemanagementsystem.Register;
import com.example.vehiclemanagementsystem.Set;
import com.example.vehiclemanagementsystem.ui.home.HomeViewModel;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
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

public class Addbotton extends Fragment {
    private static final String TAG="MainActivity";
    private SuperTextView txt_class;
    private SuperTextView txt_score;
    private SuperTextView txt_updata;
    private SuperTextView txt_password;
    private SuperTextView txt_name;
    private SuperTextView txt_bj;
    private SuperTextView txt_student;
    private SuperTextView txt_gender;
    private HomeViewModel homeViewModel;
    private MiniLoadingDialog miniLoadingDialog;
    public static Addbotton newInstance() {
        return new Addbotton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.addbotton_fragment,container,false);

        txt_class=root.findViewById(R.id.super_txt_class);
//        txt_score=root.findViewById(R.id.super_txt_score);
        txt_updata=root.findViewById(R.id.super_txt_updata);
        txt_password=root.findViewById(R.id.super_txt_password);
        txt_name=root.findViewById(R.id.super_txt_name);
        txt_gender=root.findViewById(R.id.super_txt_gender);
        txt_student=root.findViewById(R.id.super_txt_student);
        txt_bj=root.findViewById(R.id.super_txt_bj);
         getscore();
        initview();



        return root;
    }
    private void getscore() {
        System.out.println(student.getRess());
        try {
            JSONObject js = new JSONObject(student.getRess());
            txt_student.setLeftString("学号:"+js.getString("sid"));
            txt_name.setLeftString("姓名:"+js.getString("name"));
            txt_gender.setLeftString("性别:"+js.getString("sex"));
            JSONObject belong_class = js.getJSONObject("belong_class");
            txt_bj.setLeftString("班级:"+belong_class.getString("cl_name"));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initview() {

        txt_class.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Set.class);
                startActivity(intent);
            }
        });

//        txt_score.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new MaterialDialog.Builder(getContext())
//                        .iconRes(R.drawable.icon_arrow_right_red)
//                        .title(R.string.tip_infos)
//                        .content("本次的考试成绩是")
//                        .positiveText(R.string.lab_submit)
//                        .show();
//            }
//        });
        txt_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(getContext())
//                                    .iconRes(R.drawable.icon_tip)
                        .title(R.string.tip_infos)
                        .content("当前已是最新版本，无需更新")
                        .positiveText(R.string.lab_submit)
                        .show();
            }


        });
        txt_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Password.class);
                startActivity(intent);


            }

            private void Toast(String s) {
            }
        });
    }


}