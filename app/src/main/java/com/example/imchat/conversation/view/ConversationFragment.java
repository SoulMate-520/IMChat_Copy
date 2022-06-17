package com.example.imchat.conversation.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.imchat.R;
import com.example.imchat.adapter.ConversationAdapter;
import com.example.imchat.base.BaseFragment;
import com.example.imchat.contact.presenter.ContactPresenter;
import com.example.imchat.conversation.model.IConversationModel;
import com.example.imchat.conversation.presenter.ConversationPresenter;
import com.example.imchat.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

/**
 * @author: yzy
 * @date: 2022/6/16 13:24
 * @description:
 * @version:
 */
public class ConversationFragment extends BaseFragment implements IConversationView {

    @BindView(R.id.recyc_conv)
    RecyclerView mRecyclerView;

    ConversationAdapter adapter;
    ConversationPresenter presenter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView();
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
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    public void setConversation(List<Conversation> list) {
        adapter = new ConversationAdapter(R.layout.item_conversation,list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(ActivityUtil.getCurrentActivity(), "点击了第" + (position + 1) + "条条目", Toast.LENGTH_SHORT).show();
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
