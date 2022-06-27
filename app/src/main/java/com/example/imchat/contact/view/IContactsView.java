package com.example.imchat.contact.view;

import com.example.imchat.bean.ContactBean;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: yzy
 * @date: 2022/6/12 14:00
 * @description: IContactsView
 * @version: 1.0
 */
public interface IContactsView {

    void setContactsList(List<ContactBean> userList);

    /**
     * 处理字母导航与列表的连接
     * @param userList
     */
    void initEvents(List<ContactBean> userList);

}
