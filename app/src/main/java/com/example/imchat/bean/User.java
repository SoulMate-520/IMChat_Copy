package com.example.imchat.bean;


import org.litepal.crud.DataSupport;


/**
 * @author Soul Mate
 * @brief 简单的功能介绍
 * @date 2022-06-03 22:13
 */

public class User  extends DataSupport {


	private String userName;



	//改造为存别人发来的邀请
	public User(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
