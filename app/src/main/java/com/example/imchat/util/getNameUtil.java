package com.example.imchat.util;

import android.text.TextUtils;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: yzy
 * @date: 2022/6/16 16:15
 * @description:
 * @version:
 */
public class getNameUtil {
    public static String getName(UserInfo userInfo){
        if(userInfo == null){
            return "";
        }

        if(TextUtils.isEmpty(userInfo.getNickname())){
            return userInfo.getUserName();
        }else{
            return userInfo.getNickname();
        }
    }

    public static boolean notNull(List list){
        if(list!=null&&list.size()>0){
            return true;
        }else{
            return false;
        }
    }
}
