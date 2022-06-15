package com.example.imchat.contact.model;

import com.example.imchat.bean.ContactBean;

import java.util.List;

/**
 * @author: yzy
 * @date: 2022/6/13 23:07
 * @description:
 * @version:
 */
public interface IContactModel {
    //给用户名排序
    void sortData(List<ContactBean> list);
}
