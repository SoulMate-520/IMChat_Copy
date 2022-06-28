package com.example.imchat.bean;

import android.graphics.Bitmap;

/**
 * @author: yzy
 * @date: 2022/6/13 9:36
 * @description：联系人bean类
 * @version: 1.0
 */
public class ContactBean  {
    private String indexTag;
    private String nickName;
    private String userName;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String name) {
        this.nickName = name;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
