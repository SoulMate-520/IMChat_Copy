package com.example.imchat.chat.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.adapter.ChatAdapter;
import com.example.imchat.base.BaseActivity;
import com.example.imchat.chat.presenter.ChatPresenter;
import com.example.imchat.chat.presenter.IChatPresenter;
import com.example.imchat.util.AudioPlayManager;
import com.example.imchat.util.ChatUiHelper;
import com.example.imchat.util.LogUtil;
import com.example.imchat.widget.RecordButton;
import com.example.imchat.widget.StateButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class ChatActivity extends BaseActivity implements IChatView, SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.ivPhoto)
    ImageView mIvPhoto;//相册


    private ChatUiHelper mUiHelper;

    private IChatPresenter mPresenter;

    private ChatAdapter chatAdapter;

    //需要传进来的参数
    //目标用户
    private String targetUserName ;
    private String myUserName ;

    //会话聊天消息
    List<Message> messageList;

    //顶部消息索引
    int topIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_chat);


        Intent intent = getIntent();

		myUserName = intent.getStringExtra("myUserName");
        targetUserName = intent.getStringExtra("targetUserName");

        //目标user账号
        mPresenter = new ChatPresenter(this, targetUserName);

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
        mUiHelper = ChatUiHelper.with(this);
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


        //标题
        mTitle.setText(targetUserName);



        chatAdapter = new ChatAdapter(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(linearLayoutManager);

        messageList = mPresenter.getListMessage();

        LogUtil.d(messageList.size()+"ccccccccccccccc");

        //初始化时最多加载15条消息
        if (messageList != null) {
            //获取顶部索引
            topIndex = messageList.size();
            //不够15条全部加载
            if (topIndex < 15) {
                chatAdapter.setData(messageList);
                //加载完毕
                topIndex = -1;
            } else {
                chatAdapter.setData(messageList.subList(topIndex - 15, topIndex));
                //减去加载条数
                topIndex -= 15;
            }
        }

        mSwipeRefresh.setOnRefreshListener(this);



        //获取头像
        JMessageClient.getUserInfo(myUserName, new GetUserInfoCallback() {
            @Override public void gotResult(int i, String s, UserInfo userInfo) {
                if(i==0){
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override public void gotResult(int i, String s, Bitmap bitmap) {
                            chatAdapter.setRight(bitmap);

                            JMessageClient.getUserInfo(targetUserName, new GetUserInfoCallback() {
                                @Override public void gotResult(int i, String s,
                                        UserInfo userInfo) {
                                    if(i==0){
                                        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                            @Override public void gotResult(int i, String s,
                                                    Bitmap bitmap) {
                                                chatAdapter.setLeft(bitmap);
                                                runOnUiThread(new Runnable() {
                                                    @Override public void run() {
                                                        mRvChat.setAdapter(chatAdapter);
                                                    }
                                                });

                                            }
                                        });
                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override public void run() {
                                                mRvChat.setAdapter(chatAdapter);
                                            }
                                        });

                                    }
                                }
                            });

                        }
                    });

                }else{


                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            mRvChat.setAdapter(chatAdapter);
                        }
                    });

                }
            }
        });






        //底部布局弹出,聊天列表上滑
        mRvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRvChat.post(new Runnable() {
                        @Override
                        public void run() {
                            updateRecordToBottom();
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

        //左上角返回按钮
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置所有消息已读
                JMessageClient.getSingleConversation(targetUserName).resetUnreadCount();

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

                Message message = JMessageClient.createSingleTextMessage(targetUserName, null, text);

//                messageList.add(message);
                chatAdapter.addDataLast(message);

                updateRecordToBottom();

                mPresenter.doSend(message, chatAdapter.getItemCount() - 1);

            }
        });

        updateRecordToBottom();

        //录音结束回调
        ((RecordButton) mBtnAudio).setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int duration) {
                LogUtil.d("录音结束回调");

                File file = new File(audioPath);
                if (file.exists()) {

                    //发送语音
                    Message message = null;

                    try {
                        message = JMessageClient.createSingleVoiceMessage(targetUserName, null, file, duration);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    chatAdapter.addDataLast(message);

                    updateRecordToBottom();

                    mPresenter.doSend(message, chatAdapter.getItemCount() - 1);

                } else {

                    Toast.makeText(getBaseContext(), "请重新录音", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //打开相册
        mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            //图库

            String path = null;
            try {
                Uri uri = data.getData();
                Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    try {
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = new File(path);

            LogUtil.d(path);
            if (file.exists()) {

                //发送图片
                Message message = null;

                try {
                    message = JMessageClient.createSingleImageMessage(targetUserName, null, file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                chatAdapter.addDataLast(message);

                updateRecordToBottom();

                mPresenter.doSend(message, chatAdapter.getItemCount() - 1);

            } else {
                Toast.makeText(getBaseContext(), "请重新选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }



    /**
     * 聊天记录显示到最下方
     */
    private void updateRecordToBottom() {
        //初始化的聊天记录更新到最底部
        if (chatAdapter.getItemCount() > 0) {
            mRvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    /**
     * 系统返回键收起键盘
     */
    public void onBackPressed() {

        if (mUiHelper.isSoftInputShown() || mUiHelper.isBottomLayoutShown()) {
            //收起
            mUiHelper.hideBottomLayout(false);
            mUiHelper.hideSoftInput();
            mEtContent.clearFocus();
            mIvEmo.setImageResource(R.mipmap.ic_emoji);

        } else {
            //退出

            //关闭语音播放
            AudioPlayManager.getInstance().stopPlay();

            //设置所有消息已读
            if(JMessageClient.getSingleConversation(targetUserName)!=null){
                JMessageClient.getSingleConversation(targetUserName).resetUnreadCount();
            }


            super.onBackPressed();

        }


    }


    /**
     * 接收在线消息
     **/
    public void onEvent(MessageEvent event) {

        //判断是否当前会话
        if (targetUserName.equals(event.getMessage().getFromUser().getUserName())) {

            Message message = event.getMessage();
            //设置已读
            message.setHaveRead(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                }
            });

//            messageList.add(message);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.addDataLast(message);
                    chatAdapter.notifyItemChanged(chatAdapter.getItemCount() - 1);
                    updateRecordToBottom();
                }
            });


//            //            获取消息类型，如text voice image eventNotification等。
//            switch (message.getContentType()) {
//                case file://文件
//
//                    break;
//                case text://文本
//
//
//                    break;
//                case image://图片
//
//                    break;
//                case video://视频
//
//                    break;
//                case location://位置
//
//                    break;
//                case voice://声音
//
//                    break;
//            }


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
        chatAdapter.notifyItemChanged(index, "success");
    }

    @Override
    public void sendFailed(int index) {
        //转圈圈变感叹号
        chatAdapter.notifyItemChanged(index, "fail");
    }

    @Override
    public void onRefresh() {

        //上滑刷新10条消息
        //-1表示加载完成
        if (topIndex != -1) {
            //不够10条全部加载
            if (topIndex < 10) {
                chatAdapter.addDataFirst(messageList.subList(0, topIndex));
                //滚动至顶部
                mRvChat.scrollToPosition(0);
                //已加载全部消息
                topIndex = -1;
            } else {
                chatAdapter.addDataFirst(messageList.subList(topIndex - 10, topIndex));
                //滚动显示加载出来的最后一条
                mRvChat.scrollToPosition(9);
                //减去加载条数
                topIndex -= 10;
            }
        }
        //转圈圈消失
        mSwipeRefresh.setRefreshing(false);
    }
}