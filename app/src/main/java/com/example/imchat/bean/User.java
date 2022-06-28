package com.example.imchat.bean;


import org.litepal.crud.DataSupport;


/**
 * @author Soul Mate
 * @brief 简单的功能介绍
 * @date 2022-06-03 22:13
 */

public class User  extends DataSupport {


	private String targetUserName;

	private String myUserName;

	public User(String targetUserName, String myUserName) {
		this.targetUserName = targetUserName;
		this.myUserName = myUserName;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getMyUserName() {
		return myUserName;
	}

	public void setMyUserName(String myUserName) {
		this.myUserName = myUserName;
	}
}
