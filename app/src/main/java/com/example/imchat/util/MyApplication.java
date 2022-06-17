package com.example.imchat.util;

import android.app.Application;
import android.content.Context;

/**
 * @author: yzy
 * @date: 2022/6/17 18:20
 * @description:
 * @version:
 */
public class MyApplication extends Application {
    private static Context context;
    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext() {
        return context;
    }
}