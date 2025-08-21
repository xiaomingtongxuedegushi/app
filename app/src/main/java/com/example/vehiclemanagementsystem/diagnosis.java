package com.example.vehiclemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vehiclemanagementsystem.ui.adddiagnosis.DiagnosisFragment;

public class diagnosis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnosis_activity);
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, DiagnosisFragment.newInstance())
//                    .commitNow();
        }
    }
}