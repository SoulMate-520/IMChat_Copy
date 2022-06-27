package com.example.imchat.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;

import com.example.imchat.R;
import com.example.imchat.adapter.ApplyFriendAdapter;
import com.example.imchat.bean.User;
import com.example.imchat.util.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class ApplyFriendActivity extends AppCompatActivity {

    List<User> list;
    List<UserInfo> userInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_friend);

        list = DataBaseHelper.getAllUser();
        if(!list.isEmpty()){
            for(User user:list){
                JMessageClient.getUserInfo(user.getUserName(), null, new GetUserInfoCallback() {
                    @Override public void gotResult(int i, String s, UserInfo userInfo) {
                        if(i==0){

                            userInfoList.add(userInfo);
                        }
                    }
                });
            }
        }

        ApplyFriendAdapter applyFriendAdapter = new ApplyFriendAdapter();
        applyFriendAdapter.setData(userInfoList);
        RecyclerView recyclerView = findViewById(R.id.recyc);
        recyclerView.setAdapter(applyFriendAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
}