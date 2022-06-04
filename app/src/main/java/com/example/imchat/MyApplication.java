package com.example.imchat;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

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

	@Override
	public void onCreate() {
		super.onCreate();
		//LitePal初始化，需要context
		context = getApplicationContext();
		LitePal.initialize(context);
	}


	public static Context getContext() {
		return context;
	}

}
