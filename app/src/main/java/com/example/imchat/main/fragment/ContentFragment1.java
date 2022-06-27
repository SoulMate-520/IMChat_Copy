package com.example.imchat.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.adapter.ConversationAdapter;
import com.example.imchat.base.BaseFragment;
import com.example.imchat.bean.User;
import com.example.imchat.chat.view.ChatActivity;
import com.example.imchat.conversation.presenter.ConversationPresenter;
import com.example.imchat.conversation.view.IConversationView;
import com.example.imchat.util.ActivityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment1 extends BaseFragment implements IConversationView {

	@BindView(R.id.recyc_conv)
	RecyclerView mRecyclerView;


	ConversationAdapter adapter;
	ConversationPresenter presenter;



	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public ContentFragment1() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ContentFragment1.
	 */
	// TODO: Rename and change types and number of parameters
	public static ContentFragment1 newInstance(String param1, String param2) {
		ContentFragment1 fragment = new ContentFragment1();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_content1;
	}

	@Override
	protected void initView() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityUtil.getCurrentActivity()));
	}

	@Override
	protected void initData() {
		//订阅接收消息,子类只要重写onEvent就能收到消息
		presenter = new ConversationPresenter(this);
//		JMessageClient.registerEventReceiver(this);
	}

	@Override
	public void setConversation(List<Conversation> list) {
		adapter = new ConversationAdapter(R.layout.item_conversation,list);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setHasFixedSize(true);
		adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				// TODO: 2022/6/17 跳转

				//获取聊天的对象账号
				Conversation conversation = (Conversation) adapter.getItem(position);
				UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
				String userName = userInfo.getUserName();

				Intent intent = new Intent(MyApplication.getContext(),ChatActivity.class);
				intent.putExtra("userName","123456");

				startActivity(intent);

//				ActivityUtil.actionStart(ChatActivity.class,userName,"userName");

			}
		});
	}

	@Override
	public void addConversation(List<Conversation> list) {

	}

	@Override
	public void onResume() {
		super.onResume();
		presenter.getConversation();
	}
}