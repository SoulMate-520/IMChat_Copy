package com.example.imchat.chat.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.imchat.R;
import com.example.imchat.adapter.ChatAdapter;
import com.example.imchat.base.BaseActivity;
import com.example.imchat.chat.presenter.ChatPresenter;
import com.example.imchat.chat.presenter.IChatPresenter;
import com.example.imchat.util.ChatUiHelper;
import com.example.imchat.util.LogUtil;
import com.example.imchat.widget.RecordButton;
import com.example.imchat.widget.StateButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

public class ChatActivity extends BaseActivity implements IChatView {

    //代替findView
    @BindView(R.id.iv_chat_back)
    ImageView mIvBack;
    @BindView(R.id.common_toolbar_title)
    TextView mTitle;
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    @BindView(R.id.rv_chat_list)
    RecyclerView mRvChat;//recyclerview
    @BindView(R.id.et_content)
    EditText mEtContent;//输入框
    @BindView(R.id.bottom_layout)
    RelativeLayout mRlBottomLayout;//表情,添加底部布局
    @BindView(R.id.ivAdd)
    ImageView mIvAdd;
    @BindView(R.id.ivEmo)
    ImageView mIvEmo;
    @BindView(R.id.btn_send)
    StateButton mBtnSend;//发送按钮
    @BindView(R.id.ivAudio)
    ImageView mIvAudio;//录音图片
    @BindView(R.id.btnAudio)
    RecordButton mBtnAudio;//录音按钮
    @BindView(R.id.rlEmotion)
    LinearLayout mLlEmotion;//表情布局
    @BindView(R.id.llAdd)
    LinearLayout mLlAdd;//添加布局
    @BindView(R.id.swipe_chat)
    SwipeRefreshLayout mSwipeRefresh;//下拉刷新

    private IChatPresenter mPresenter;

    private ChatAdapter chatAdapter = new ChatAdapter(this);

    //需要传进来的参数
    //目标用户
    private String userName = "123456";
    private String userNameTitle = "传过来";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_chat);


//		userName = savedInstanceState.getString("userName");

        //目标user账号
        mPresenter = new ChatPresenter(this, "654321");

        initUI();

        //已有账户 123456  654321
        //user exist Success Invalid username.


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }


    private void initUI() {
        //绑定控件
        ButterKnife.bind(this);

        //底部的ui操作
        final ChatUiHelper mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(mLlContent)
                .bindttToSendButton(mBtnSend)
                .bindEditText(mEtContent)
                .bindBottomLayout(mRlBottomLayout)
                .bindEmojiLayout(mLlEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(mIvAdd)
                .bindToEmojiButton(mIvEmo)
                .bindAudioBtn(mBtnAudio)
                .bindAudioIv(mIvAudio)
				.bindEmojiData();


        //对方
        mTitle.setText(userNameTitle);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(linearLayoutManager);


        chatAdapter.setData(mPresenter.getListMessage());
        mRvChat.setAdapter(chatAdapter);

        //底部布局弹出,聊天列表上滑
        mRvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				if (bottom < oldBottom) {
					mRvChat.post(new Runnable() {
						@Override
						public void run() {
							if (chatAdapter.getItemCount() > 0) {
								mRvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
							}
						}
					});
				}
            }
        });

        //点击空白区域关闭键盘和底部栏
        mRvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUiHelper.hideBottomLayout(false);
                mUiHelper.hideSoftInput();
                mEtContent.clearFocus();
                mIvEmo.setImageResource(R.mipmap.ic_emoji);
                return false;
            }
        });

        //返回按钮
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //结束
                finish();
            }
        });

        //发送文本信息
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEtContent.getText().toString();
                mEtContent.setText("");

                Message message = JMessageClient.createSingleTextMessage(userName, null, text);

//                if (message != null)
                //RecyclerView视图回到底部
                chatAdapter.addData(message);
                //正在发送转圈圈？

                mPresenter.doSend(message, chatAdapter.getItemCount() - 1);

//				mPresenter.doSend(1,text);


            }
        });


    }


    /**
     *  接收在线消息
     **/
    public void onEvent(MessageEvent event) {

        //判断是否当前会话
        if(userName.equals(event.getMessage().getFromUser().getUserName())){

            Message message = event.getMessage();
            //设置已读
            message.setHaveRead(new BasicCallback() {
                @Override public void gotResult(int i, String s) {
                }
            });

//            获取消息类型，如text voice image eventNotification等。
            switch (message.getContentType()) {
            case file://文件

                break;
            case text://文本

                chatAdapter.addData(message);

                break;
            case image://图片

                break;
            case video://视频

                break;
            case location://位置

                break;
            case voice://声音

                break;
            }


        }

//        //获取事件发生的会话对象
//        Conversation conversation = event.getConversation();
//        Message newMessage = event.getMessage();//获取此次离线期间会话收到的新消息列表
//        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到一条来自%s的在线消息。\n", conversation.getTargetId()));
//
//
//                final Message message = event.getMessage();//获取消息对象
//                TextContent textContent = (TextContent) message.getContent();
//                Log.i("接收到的对方的消息：",""+ textContent.getText());
//
//
//                //获取消息类型，如text voice image eventNotification等。
//                switch (message.getContentType()) {
//                //处理事件提醒消息，此处message的contentType类型为eventNotification。
//                case eventNotification:
//                    //获取事件发生的群的群信息
//                    GroupInfo groupInfo = (GroupInfo) message.getTargetInfo();
//                    //获取事件具体的内容对象
//                    EventNotificationContent eventNotificationContent = (EventNotificationContent)message.getContent();
//                    //获取事件具体类型
//                    switch (eventNotificationContent.getEventNotificationType()){
//                    case group_member_added:
//                        //群成员加群事件
//                        break;
//                    case group_member_removed:
//                        //群成员被踢事件
//                        break;
//                    case group_member_exit:
//                        //群成员退群事件
//                        break;
//                    case group_info_updated://since 2.2.1
//                        //群信息变更事件
//                        break;
//
//                    }
//                    break;
//
//                case file://文件
//
//                    break;
//                case text://文本
//
//                    break;
//                case image://图片
//
//                    break;
//                case video://视频
//
//                    break;
//                case location:
//
//                    break;
//                case voice://声音
//
//                    break;
//                }

    }


    @Override
    public void sendSuccess(int index) {
        //转圈圈消失
        chatAdapter.notifyItemChanged(chatAdapter.getItemCount()-1,"success");
    }

    @Override
    public void sendFailed(int index) {
        //转圈圈变感叹号
        chatAdapter.notifyItemChanged(chatAdapter.getItemCount()-1,"fail");

    }
}