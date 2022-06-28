package com.example.imchat.util;

import com.example.imchat.bean.User;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.litepal.LitePalBase;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * @author Soul Mate
 * @brief
 * 使用LitePal对数据库进行操作
 * 对数据库相关的相关操作（增删查改）写进Helper类中再调用
 * @date 2022-06-03 23:53
 */

public class DataBaseHelper {

	/**
	 * example
	 * @return
	 */
	public static void insertUser(User user) {

		//查重
		if (DataSupport.where("myUserName = ? and targetUserName = ?",user.getMyUserName(),user.getTargetUserName()).find(User.class).isEmpty()){

			user.save();
		}

	}

	public static void deleteUser(User user){
		user.delete();
	}

	public static List<User> getAllUser(String userName){



		List<User> users = DataSupport.where("myUserName = ?", userName).find(User.class);

		return users;
	}


}
