package com.example.imchat.conversation.view;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

/**
 * @author: yzy
 * @date: 2022/6/15 17:06
 * @description:
 * @version:
 */
public interface IConversationView {
    void setConversation(List<Conversation> list);
    void addConversation(List<Conversation> list);
}
