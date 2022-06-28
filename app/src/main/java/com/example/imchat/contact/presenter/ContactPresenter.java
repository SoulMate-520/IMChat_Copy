package com.example.imchat.contact.presenter;

import android.graphics.Bitmap;

import com.example.imchat.MyApplication;
import com.example.imchat.bean.ContactBean;
import com.example.imchat.chat.presenter.IChatPresenter;
import com.example.imchat.chat.view.IChatView;
import com.example.imchat.contact.model.IContactModel;
import com.example.imchat.contact.view.IContactsView;
import com.example.imchat.util.getNameUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: yzy
 * @date: 2022/6/13 22:33
 * @description:
 * @version:
 */
public class ContactPresenter {

    private IContactsView mContView;
    private IContactModel mContModel;

    private List<ContactBean> list = new ArrayList<>();

    public ContactPresenter(IContactsView mContView,IContactModel mContModel){
        this.mContView = mContView;
        this.mContModel = mContModel;
    }


    public void getContactsList(){
        list.clear();
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    //获取好友列表成功
                    list.clear();
                    for(int j=0;j<userInfoList.size();j++)
                    {
                        UserInfo userInfo =userInfoList.get(j);
                        ContactBean bean = new ContactBean();
                        bean.setNickName(getNameUtil.getName(userInfo));
                        bean.setUserName(userInfo.getUserName());
                        list.add(bean);

                        int finalJ = j;
                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                            @Override public void gotResult(int i, String s, Bitmap bitmap) {
                                    if(bitmap!=null){

                                        bean.setBitmap(bitmap);


                                        mContView.updateHead(finalJ);
                                    }

                            }
                        });


                    }

                    mContModel.sortData(list);
                    mContView.setContactsList(list);
                    mContView.initEvents(list);
                } else {
                    //获取好友列表失败
                }
            }
        });

    }

    //运行时更新
//   public void updateData(){
//        list.clear();
//        ContactManager.getFriendList(new GetUserInfoListCallback() {
//            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
//                if (0 == responseCode) {
//                    //获取好友列表成功
//                    list.clear();
//                    for(UserInfo userInfo : userInfoList){
//                        ContactBean bean = new ContactBean();
//                        bean.setNickName(getNameUtil.getName(userInfo));
//                        bean.setUserName(userInfo.getUserName());
//                        list.add(bean);
//                    }
//
//                    mContView.update();
//
//
//                } else {
//                    //获取好友列表失败
//                }
//            }
//        });
//
//    }
//
//    public void updateContact(){
//        mContModel.sortData(list);
//        mContView.setContactsList(list);
//        mContView.initEvents(list);
//    }

}
