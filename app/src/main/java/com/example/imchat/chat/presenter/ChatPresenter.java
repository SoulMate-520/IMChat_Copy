package com.example.imchat.chat.presenter;

import androidx.annotation.Nullable;

import com.example.imchat.chat.view.IChatView;
import com.example.imchat.util.LogUtil;

import java.io.File;
import java.util.List;

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
		conversation = Conversation.createSingleConversation(userName);

		this.userName = userName;


	}

	/**。
	 *
	 * @param
	 */
	@Override
	public void doSend(Message message,int index) {

		//监听
		message.setOnSendCompleteCallback(new BasicCallback() {
			@Override public void gotResult(int i, String s) {
				LogUtil.d("消息发送状态"+i+s);
				if (i == 0) { //成功
					mChatView.sendSuccess(index);
				} else {
					mChatView.sendFailed(index);
				}

			}
		});

		JMessageClient.sendMessage(message);

	}

//	@Override public void doSend(int type, String text) {
//
//		Message message = JMessageClient.createSingleTextMessage(userName,null);
//
//		//监听
//		message.setOnSendCompleteCallback(new BasicCallback() {
//			@Override public void gotResult(int i, String s) {
//				LogUtil.d(s);
//				if(i==0){ //成功
//					LogUtil.d(i+"");
//					mChatView.sendSuccess();
//				}else{
//
//					mChatView.sendFailed();
//				}
//
//			}
//		});
//		JMessageClient.sendMessage(message);
//
//
//	}

	public UserInfo getUserInfo(){
		return (UserInfo) conversation.getTargetInfo();
	}

	@Override
	public List<Message> getListMessage() {

//		if(conversation==null){
//			return null;
//
//		}else{
//
//		}

		return conversation.getAllMessage();

	}

//	/**
//	 * 昵称或username
//	 * @return
//	 */
//	@Override public String getTitle() {
////		UserInfo userInfo = conversation.getTitle()
//		return conversation.getTitle();
//	}

}
