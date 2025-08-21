package com.example.vehiclemanagementsystem;

import android.app.Application;

public class student  extends Application {
    private static String ress;

    public static String getRess() {
        return ress;
    }

    public static void setRess(String ress) {
        student.ress = ress;
    }
}
