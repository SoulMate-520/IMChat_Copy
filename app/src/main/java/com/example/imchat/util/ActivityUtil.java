package com.example.imchat.util;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;

/**
 * @author: yzy
 * @date: 2022/6/14 15:20
 * @description:
 * @version:
 */
public class ActivityUtil {

    private static Stack<Activity> activityStack = new Stack<>();

    /**
     * 得到当前的 Activity
     *
     * @return 当前 Activity
     */
    public static Activity getCurrentActivity() {
        Activity activity = null;
        if (!activityStack.isEmpty()) {
            activity = activityStack.peek();
        }
        return activity;
    }
    /**
     * 结结束当前Activity
     *
     * @param activity 当前Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }

    }


    /**
     * 不用 finish 当前 Activity 时直接调用此方法
     *
     * @param classes
     */
    public static void startActivity(Class classes) {
        startActivity(classes, false);
    }


    /**
     * 需要 finish 当前 Activity 时调用此方法，布尔值参数传入 true
     *
     * @param classes  需要打开的 activity
     * @param isFinish 是否 finish 当前 activity
     */
    public static void startActivity(Class classes, boolean isFinish) {
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent(currentActivity, classes);
        currentActivity.startActivity(intent);
        if (isFinish) {
            finishActivity(currentActivity);
        }
    }




    /**
     * 关闭所有 Activity
     */
    public static void closeAllActivity() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (null == activity) {
                return;
            }
            finishActivity(activity);
        }
    }


    /**
     * 携带一个数据跳转
     *
     * @param classes 需要跳转过去的Activity
     * @param data 传过去的数据
     */
    public static void actionStart(Class classes, String data){
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent(currentActivity, classes);
        intent.putExtra("param1",data);
        currentActivity.startActivity(intent);
    }


    /**
     * 携带两个数据跳转
     *
     * @param classes 需要跳转过去的Activity
     * @param data1 传过去的数据1
     * @param data2 传过去的数据2
     */
    public static void actionSecondStart(Class classes, String data1, String data2){
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent(currentActivity, classes);
        intent.putExtra("param1",data1);
        intent.putExtra("param2",data2);
        currentActivity.startActivity(intent);
    }

    public static void actionThirdStart(Class classes, String data1, String data2, int path){
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent(currentActivity, classes);
        intent.putExtra("param1",data1);
        intent.putExtra("param2",data2);
        intent.putExtra("param3",path);
        currentActivity.startActivity(intent);
    }

    /**
     * 获取数据
     *
     * @return 返回接收到的数据
     */
    public static String getIntentData(){
        Intent intent = getCurrentActivity().getIntent();
        return intent.getStringExtra("param1");
    }

    /**
     * 获取第二个数据
     *
     * @return 返回接收到的第二个数据
     */
    public static String getIntentSecondData(){
        Intent intent = getCurrentActivity().getIntent();
        return intent.getStringExtra("param2");
    }

    public static int getIntentThirdData(){
        Intent intent = getCurrentActivity().getIntent();
        return intent.getIntExtra("param3",0);
    }
}
