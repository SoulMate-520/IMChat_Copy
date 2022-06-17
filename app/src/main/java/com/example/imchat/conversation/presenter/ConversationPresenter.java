package com.example.imchat.conversation.presenter;

import com.example.imchat.bean.ContactBean;
import com.example.imchat.constant.Constant;
import com.example.imchat.conversation.view.IConversationView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: yzy
 * @date: 2022/6/16 13:02
 * @description:
 * @version:
 */
public class ConversationPresenter {

    private IConversationView mConversationView;

    public ConversationPresenter(IConversationView mConversationView){
        this.mConversationView = mConversationView;
    }
    public void getConversation(MessageEvent event, List<Conversation> list){
        boolean handlable = false;
        Message msg = event.getMessage();
        if (msg.getTargetType() == ConversationType.single){
            UserInfo userInfo = (UserInfo) msg.getTargetInfo();

            for(Conversation conversation : list){
                if (conversation.getType() == ConversationType.single) {
                    UserInfo userI = (UserInfo) conversation.getTargetInfo();
                    if(userI.getUserName().equals(userInfo.getUserName())){
                        conversation.updateConversationExtra(Constant.NEW_MESSAGE);
                        handlable = true;
                        mConversationView.setConversation(list);
                    }
                }
            }
            if(!handlable){
                Conversation conversation = JMessageClient.getSingleConversation(userInfo.getUserName());
                if(conversation.getTargetInfo() instanceof UserInfo){
                    Conversation bean = conversation;
                    bean.updateConversationExtra(Constant.NEW_MESSAGE);
                    list.add(bean);
                }
            }
        }

    }
    public void getConversation(){
        List<Conversation> list = new ArrayList<>();
        list.clear();
        list.addAll(JMessageClient.getConversationList());
        mConversationView.setConversation(list);
    }
}
