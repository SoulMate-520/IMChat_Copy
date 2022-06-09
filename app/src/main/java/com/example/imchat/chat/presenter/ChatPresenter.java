package com.example.imchat.chat.presenter;

import androidx.annotation.Nullable;

import com.example.imchat.chat.view.IChatView;
import com.example.imchat.util.LogUtil;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * @author Soul Mate
 * @brief 简单的功能介绍
 * @date 2022-06-09 17:12
 */

public class ChatPresenter implements IChatPresenter {

	private IChatView mChatView;

	//目标用户
	private String userName;

	//会话
	private Conversation conversation;

	public ChatPresenter(IChatView mChatView,String userName) {
		this.mChatView = mChatView;
		conversation = Conversation.createSingleConversation(userName, null);
		this.userName = userName;


	}

	/**
	 * 1 为文本
	 * @param type
	 * @param text
	 * @param file
	 */
	@Override public void doSend(int type, String text, File file) {


		Message message = null;

		if(type==1){
			message = JMessageClient.createSingleTextMessage(userName, null,  text);

			//监听
			message.setOnSendCompleteCallback(new BasicCallback() {
				@Override public void gotResult(int i, String s) {
					LogUtil.d(s);
					if(i==0){ //成功
						mChatView.sendSuccess();
					}else{

						mChatView.sendFailed();
					}

				}
			});

		}else if(type == 2){ //语音

		}else if(type == 3){//图片

		}


		JMessageClient.sendMessage(message);

	}

	public UserInfo getUserInfo(){
		return (UserInfo) conversation.getTargetInfo();
	}


}
