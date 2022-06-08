package com.example.imchat.chat.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.imchat.R;
import com.example.imchat.base.BaseActivity;
import com.example.imchat.util.ChatUiHelper;
import com.example.imchat.widget.RecordButton;
import com.example.imchat.widget.StateButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

public class ChatActivity extends BaseActivity {

	//代替findView
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


	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		initUI();


	}

	@Override public int getLayoutId() {
		return 0;
	}

	@Override protected void initView() {

	}

	@Override protected void initData() {

	}

	private void initUI() {
		//绑定控件
		ButterKnife.bind(this);

		//底部的ui操作
		final ChatUiHelper mUiHelper= ChatUiHelper.with(this);
		mUiHelper.bindContentLayout(mLlContent)
				.bindttToSendButton(mBtnSend)
				.bindEditText(mEtContent)
				.bindBottomLayout(mRlBottomLayout)
				.bindEmojiLayout(mLlEmotion)
				.bindAddLayout(mLlAdd)
				.bindToAddButton(mIvAdd)
				.bindToEmojiButton(mIvEmo)
				.bindAudioBtn(mBtnAudio)
				.bindAudioIv(mIvAudio);
//				.bindEmojiData();


		//底部布局弹出,聊天列表上滑
		mRvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//				if (bottom < oldBottom) {
//					mRvChat.post(new Runnable() {
//						@Override
//						public void run() {
//							if (mAdapter.getItemCount() > 0) {
//								mRvChat.smoothScrollToPosition(mAdapter.getItemCount() - 1);
//							}
//						}
//					});
//				}
			}
		});
		//点击空白区域关闭键盘
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


		//发送信息
		mBtnSend.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {

			}
		});


	}


}