package com.example.imchat.chat.presenter;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public interface IChatPresenter {



	void doSend(Message message,int index);

	UserInfo getUserInfo();
	List<Message> getListMessage();
}
