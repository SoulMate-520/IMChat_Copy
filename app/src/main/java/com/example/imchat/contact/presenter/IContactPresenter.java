package com.example.imchat.contact.presenter;

import com.example.imchat.bean.ContactBean;
import com.example.imchat.chat.presenter.IChatPresenter;
import com.example.imchat.chat.view.IChatView;
import com.example.imchat.contact.model.IContactModel;
import com.example.imchat.contact.view.IContactsView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: yzy
 * @date: 2022/6/13 22:33
 * @description:
 * @version:
 */
public class IContactPresenter {

    private IContactsView mContView;
    private IContactModel mContModel;

    public IContactPresenter(IContactsView mContView, IContactModel mContModel){
        this.mContView = mContView;
        this.mContModel = mContModel;
    }

    public void getContactsList(){
        List<ContactBean> list = new ArrayList<>();
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    list.clear();
                    for(UserInfo userInfo : userInfoList){
                        ContactBean bean = new ContactBean();
                        bean.setNickName(userInfo.getNickname());
                        bean.setUserName(userInfo.getUserName());
                        list.add(bean);
                    }
                    mContModel.sortData(list);
                    mContView.setContactsList(list);
                } else {
                    //获取好友列表失败
                }
            }
        });
    }
}
