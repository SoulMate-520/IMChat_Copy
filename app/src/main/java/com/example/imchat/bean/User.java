package com.example.imchat.bean;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


/**
 * @author Soul Mate
 * @brief 简单的功能介绍
 * @date 2022-06-03 22:13
 */

public class User  extends LitePalSupport {


	private String userName;

	private String passWord;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
