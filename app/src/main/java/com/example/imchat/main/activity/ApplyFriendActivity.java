package com.example.imchat.main.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imchat.R;
import com.example.imchat.adapter.ApplyFriendAdapter;
import com.example.imchat.bean.User;
import com.example.imchat.util.DataBaseHelper;
import com.example.imchat.util.LogUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class ApplyFriendActivity extends AppCompatActivity {

    List<User> list;
    ApplyFriendAdapter applyFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_friend);

        List<UserInfo> userInfos=new ArrayList<>();

        applyFriendAdapter = new ApplyFriendAdapter();

        RecyclerView recyclerView = findViewById(R.id.recyc);
        recyclerView.setAdapter(applyFriendAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        list = DataBaseHelper.getAllUser(user);
        if (!list.isEmpty()) {
            for (User user : list) {
                JMessageClient.getUserInfo(user.getTargetUserName(), null, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        LogUtil.d(i + "");
                        if (i == 0) {
                            userInfos.add(userInfo);
                        }
                    }
                });
            }
        }

        userInfos.sort((o1, o2) -> o1.getUserName().compareTo(o2.getUserName()));

        applyFriendAdapter.setData(userInfos);

        for (int j=0;j<userInfos.size();j++){
            UserInfo userInfo=userInfos.get(j);
            int finalJ = j;
            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if(bitmap!=null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                applyFriendAdapter.notifyItemChanged(finalJ,bitmap);
                            }
                        });
                    }
                }
            });
        }
    }
}