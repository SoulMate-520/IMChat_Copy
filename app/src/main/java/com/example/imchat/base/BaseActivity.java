package com.example.imchat.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imchat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.ChatRoomNotificationEvent;
import cn.jpush.im.android.api.event.CommandNotificationEvent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.GroupAnnouncementChangedEvent;
import cn.jpush.im.android.api.event.GroupApprovalEvent;
import cn.jpush.im.android.api.event.GroupApprovalRefuseEvent;
import cn.jpush.im.android.api.event.GroupApprovedNotificationEvent;
import cn.jpush.im.android.api.event.GroupBlackListChangedEvent;
import cn.jpush.im.android.api.event.GroupMemNicknameChangedEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageReceiptStatusChangeEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.MyInfoUpdatedEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public abstract class BaseActivity extends AppCompatActivity {



    public abstract int getLayoutId();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(getLayoutId());


        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this);



//
//        initData();
//        initView();




    }



//    protected abstract void initView();
//
//    protected abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //注销消息接收
        JMessageClient.unRegisterEventReceiver(this);
    }


    public void onEventMainThread(LoginStateChangeEvent event){
        LoginStateChangeEvent.Reason reason = event.getReason();
        if(reason == LoginStateChangeEvent.Reason.user_logout){
//            L.t("登录失效，重新登录");
        }else if(reason == LoginStateChangeEvent.Reason.user_password_change){
//            L.t("修改密码，重新登录");
        }

        if(!isFinishing()){
            JMessageClient.logout();
/*            Intent myIntent = new Intent(this, LoginRegisterActivity.class);
            startActivity(myIntent);

            App.app().getUser().clearUser();
            finish();*/
        }



    }


    public static List<ContactNotifyEvent> friendApply = new ArrayList<>();

    public void onEvent(ContactNotifyEvent event){
        if(event.getType() == ContactNotifyEvent.Type.invite_received){

            boolean has = false;

            for(int i= 0 ;i < friendApply.size() ;i++){
                if(friendApply.get(i).getFromUsername().equals(event.getFromUsername())){
                    has = true;
                }
            }
            if(!has){
                friendApply.add(event);
                LogUtil.d("收到了好友邀请");
            }

        }
    }



    /**
     * 消息/会话事件
     * 在线消息事件实体类 MessageEvent
     * @param event
     */
    public void onEvent(MessageEvent event){
//        Log.i(TAG,"onEvent:"+"MessageEvent");

//        final Message message = event.getMessage();//获取消息对象
//        TextContent textContent = (TextContent) message.getContent();
//        Log.i("接收到的对方的消息：",""+ textContent.getText());
//
//
//        //获取消息类型，如text voice image eventNotification等。
//        switch (message.getContentType()) {
//        //处理事件提醒消息，此处message的contentType类型为eventNotification。
//        case eventNotification:
//            //获取事件发生的群的群信息
//            GroupInfo groupInfo = (GroupInfo) message.getTargetInfo();
//            //获取事件具体的内容对象
//            EventNotificationContent eventNotificationContent = (EventNotificationContent)message.getContent();
//            //获取事件具体类型
//            switch (eventNotificationContent.getEventNotificationType()){
//            case group_member_added:
//                //群成员加群事件
//                break;
//            case group_member_removed:
//                //群成员被踢事件
//                break;
//            case group_member_exit:
//                //群成员退群事件
//                break;
//            case group_info_updated://since 2.2.1
//                //群信息变更事件
//                break;
//
//            }
//            break;
//
//        case file://文件
//
//            break;
//        case text://文本
//
//            break;
//        case image://图片
//
//            break;
//        case video://视频
//
//            break;
//        case location:
//
//            break;
//        case voice://声音
//
//            break;
//        }

    }



    /**
     * 当前登录用户信息被更新事件实体类 MyInfoUpdatedEvent
     * @param event
     */
    public void onEvent(MyInfoUpdatedEvent event){
//        Log.i(TAG,"onEvent:"+"MyInfoUpdatedEvent");

        UserInfo userInfo = event.getMyInfo();
        if (TextUtils.isEmpty( userInfo.getUserName())){
//            Log.i(TAG,"onEvent:"+"当前登录用户信息被更新事件实体类:"+userInfo.getUserName());
        }
    }

    /**
     * 用户下线事件UserLogoutEvent
     * (已过时，请使用LoginStateChangeEvent代替)
     * @param event
     * 用户被删除事件UserDeletedEvent
     * (已过时，请使用LoginStateChangeEvent代替)
     *
     *用户登录状态变更事件LoginStateChangeEvent
     *
     */
    public void onEvent(LoginStateChangeEvent event){
//        Log.i(TAG,"onEvent:"+"LoginStateChangeEvent");

        UserInfo userInfo = event.getMyInfo();
        if (TextUtils.isEmpty( userInfo.getUserName())){
//            Log.i(TAG,"onEvent:"+"用户下线事件:"+userInfo.getUserName());
        }
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        switch (reason) {
        case user_password_change:
            //用户密码在服务器端被修改
            break;
        case user_logout:
            //用户换设备登录
            break;
        case user_deleted:
            //用户被删除
            break;
        }


        //getMyInfo()	UserInfo	获取当前被登出账号的信息

        //getMyInfo()	UserInfo	获取当前被删除账号的信息

        //getMyInfo()	UserInfo	获取当前登录状态改变的账号的信息
        //getReason()	Reason	获取登录状态变更原因
    }


    /**
     * 离线消息事件实体类 OfflineMessageEvent
     * @param event
     */
    public void onEvent(OfflineMessageEvent event){
//        Log.i(TAG,"onEvent:"+"OfflineMessageEvent");
        //getConversation()	Conversation	获取收到离线消息的会话对象
        //getNewMessageList()	List	获取收到的离线消息列表,包含了该会话此次离线收到的所有离线消息列表。其中也有可能包含自己发出去的消息。
        //getOfflineMsgCnt()	int	获取此次事件中该会话的离线消息总数。


        //用户离线期间，如果群组中发生了成员变化事件，sdk也会通过上抛OfflineMessageEvent
        //的方式来通知上层，处理方式类似上面的MessageEvent
        List<Message> msgs = event.getOfflineMessageList();
        for (Message msg:msgs) {
            //...
        }

    }

    /**
     * 会话刷新事件实体类 ConversationRefreshEvent
     * @param event
     */
    public void onEvent(ConversationRefreshEvent event){
//        Log.i(TAG,"onEvent:"+"ConversationRefreshEvent");
        //getConversation()	Conversation	获取需要被刷新的会话对象
        //getReason()	Reason	获取事件发生的原因，包括消息漫游完成、会话信息更新等
    }


    /**
     * 消息被对方撤回通知事件MessageRetractEvent
     * @param event
     */
    public void onEvent(MessageRetractEvent event){
//        Log.i(TAG,"onEvent:"+"MessageRetractEvent");

        //getConversation()	Conversation	获取被撤回消息所属的会话对象
        //getRetractedMessage()	Message	获取被撤回的message对象.
        //(注意!此时获取到的Message的MessageContent对象已经从撤回前的真正的消息
        // 内容变为了PromptContent类型的提示文字)
    }

    /**
     * 消息未回执人数变更事件MessageReceiptStatusChangeEvent
     * @param event
     */
    public void onEvent(MessageReceiptStatusChangeEvent event){
//        Log.i(TAG,"onEvent:"+"MessageReceiptStatusChangeEvent");
        //getConversation()	Conversation	获取未回执数变更的消息所属的会话对象
        //getMessageReceiptMetas()	List<MessageReceiptMeta>	获取未回执数发生变化的消息的MessageReceiptMeta。
        //其中包括了消息的serverMsg Id、当前的未回执人数、以及未回执人数更新的时间
    }

    /**
     * 群成员审批事件GroupApprovalEvent
     * @param event
     */
    public void onEvent(GroupApprovalEvent event){
//        Log.i(TAG,"onEvent:"+"GroupApprovalEvent");

        //getType()	Type	获取群成员审批通知事件类型，主动申请入群是Type.apply_join_group，邀请入群是Type.invited_into_group
        //getFromUserInfo()	UserInfo	获取群成员审批事件发起方UserInfo，主动申请入群时是申请人UserInfo，邀请入群时是邀请人UserInfo
        //getApprovalUserInfoList()	List<UserInfo>	获取需要审批入群的用户UserInfo
        //getApprovalUserCount()	int	获取需要审批入群的用户的人数
        //getReason()	String	获取事件发生的理由，主动申请入群时是申请理由(可为null)，邀请入群时是null
        //acceptGroupApproval()	void	入群审批同意,需要指定username，appKey
        //refuseGroupApproval()	void	入群审批拒绝,需要指定username，appKey，reason(可为null)

    }


    /**
     * 群成员审批拒绝事件GroupApprovalRefuseEvent
     */
    public void onEvent(GroupApprovalRefuseEvent event){
//        Log.i(TAG,"onEvent:"+"GroupApprovalRefuseEvent");
        //getFromUserInfo()	UserInfo	获取事件发起方userInfo，在本事件中为群主信息
        //getToUserInfoList()	List<UserInfo>	获取事件对象用户信息列表，在本事件中为被拒绝入群的用户UserInfo列表
        //getReason()	String	获取事件发生的理由, 在本事件中为群主审批拒绝的理由
        //getGid()	long	返回实际群组Gid
    }


    /**
     * 已审批事件通知GroupApprovedNotificationEvent
     */
    public void onEvent(GroupApprovedNotificationEvent event){
//        Log.i(TAG,"onEvent:"+"GroupApprovedNotificationEvent");

        //getApprovalEventID()	long	获取对应的入群审批事件ID
        //getApprovalResult()	boolean	获取入群审批结果
        //getGroupID()	long	获取入群审批事件对应的群组ID
        //getOperator()	UserInfo	获取该次入群审批的操作者用户信息
        //getApprovedUserInfoList()	List<UserInfo>	获取已被审批过的用户信息，这些用户的入群审批已经被审批
    }


    /**
     * 群成员昵称修改事件GroupMemNicknameChangedEvent
     * @param event
     */
    public void onEvent(GroupMemNicknameChangedEvent event){
//        Log.i(TAG,"onEvent:"+"GroupMemNicknameChangedEvent");
        //getGroupID()	long	获取群组id
        //getChangeEntities()	List<ChangeEntity>	获取昵称修改事件列表,按照时间升序排列

    }


    /**
     * 群公告变更事件GroupAnnouncementChangedEvent
     * @param event
     */
    public void onEvent(GroupAnnouncementChangedEvent event){
//        Log.i(TAG,"onEvent:"+"GroupAnnouncementChangedEvent");
        //getGroupID()	long	获取群组id
        //getChangeEntities()	List<ChangeEntity>	获取公告变更事件列表，按照时间升序排列

    }

    /**
     * 群黑名单变更事件GroupBlackListChangedEvent
     * @param event
     */
    public void onEvent(GroupBlackListChangedEvent event){
//        Log.i(TAG,"onEvent:"+"GroupBlackListChangedEvent");
        //getGroupID()	long	获取群组id
        //getChangeEntities()	List<ChangeEntity>	获取黑名单变更事件列表，按照时间升序排列

    }


    /**
     * 聊天室事件
     * 聊天室消息事件ChatRoomMessageEvent
     * Since 2.4.0 聊天室消息因为sdk不会入库，所以没有走正常的消息事件，
     * 而是单独的聊天室消息事件。注意和消息事件做区分
     * @param event
     */
    public void onEvent(ChatRoomMessageEvent event){
//        Log.i(TAG,"onEvent:"+"ChatRoomMessageEvent");
        //getMessages()	List<Message>	获取聊天室消息事件中包含的消息列表

    }

    /**
     * 聊天室通知事件ChatRoomNotificationEvent
     * @param event
     */
    public void onEvent(ChatRoomNotificationEvent event){
//        Log.i(TAG,"onEvent:"+"ChatRoomNotificationEvent");
        //getEventID()	long	获取事件ID
        //getRoomID()	long	获取事件对应聊天室的房间ID
        //getType()	Type	获取事件类型
        //getOperator(GetUserInfoCallback callback)	void	获取事件操作者用户信息
        //getTargetUserInfoList(GetUserInfoListCallback callback)	void	获取目标用户信息列表
        //getCtime()	long	取事件发生时间，单位-毫秒
    }


    /**
     * 好友事件
     * 好友相关事件通知实体类ContactNotifyEvent
     */
    /*public void onEvent(ContactNotifyEvent event) {
     getType()	Type	获取好友通知事件的具体类型。
      getReason()	String	获取事件发生的理由，该字段由对方发起请求时所填。
      getFromUsername()	String	获取事件发起者用户的username
      getfromUserAppKey()	String	获取事件发起者用户所属应用的appKey
    }*/


    /**
     * 命令透传事件
     * 命令透传事件实体类CommandNotificationEvent
     * @param event
     */
    public void onEvent(CommandNotificationEvent event){
//        Log.i(TAG,"onEvent:"+"CommandNotificationEvent");

        //getSenderUserInfo()	UserInfo	获取命令透传消息发送者的UserInfo
        //getType()	Type	获取命令透传消息对象的类型，单聊是Type.single,群聊则是Type.group,如果是自己已登录设备间的命令透传则是Type.self
        //getTargetInfo()	Objcet	获取命令透传消息发送对象的Info。若对象是单聊用户则是UserInfo,对象是群组则是GroupInfo，使用时强制转型
        //getMsg()	String	获取命令透传消息的实际内容
    }


    /**
     * 通知栏点击事件
     * 通知栏点击事件实体类NotificationClickEvent
     * @param event
     */
    public void onEvent(NotificationClickEvent event){
//        Log.i(TAG,"onEvent:"+"NotificationClickEvent");

        //getMessage()	Message	获取点击的通知所对应的消息对象
    }



}
