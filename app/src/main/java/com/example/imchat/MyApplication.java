package com.example.imchat;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import cn.jpush.im.android.api.JMessageClient;

/**
 * @author Soul Mate
 * @brief
 * @date 2022-06-04 0:31
 */

public class MyApplication extends Application {
	/**
	 * 保证唯一一个context
	 */
	private static Context context;
	private static Application mApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication=this;
		//LitePal初始化，需要context
		context = getApplicationContext();
		LitePal.initialize(context);

		//初始化SDK
		JMessageClient.init(context);
	}


	public static Context getContext() {
		return context;
	}

	public static Application getmApplication() {
		return mApplication;
	}

}
