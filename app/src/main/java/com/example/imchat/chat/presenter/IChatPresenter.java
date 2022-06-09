package com.example.imchat.chat.presenter;

import androidx.annotation.Nullable;

import java.io.File;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

public interface IChatPresenter {


	void doSend(int type, @Nullable String text, @Nullable File file);

	UserInfo getUserInfo();
}
