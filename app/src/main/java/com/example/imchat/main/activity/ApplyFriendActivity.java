package com.example.imchat.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;

import com.example.imchat.R;
import com.example.imchat.adapter.ApplyFriendAdapter;
import com.example.imchat.bean.User;
import com.example.imchat.util.DataBaseHelper;
import com.example.imchat.util.LogUtil;

import java.util.ArrayList;
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

        applyFriendAdapter = new ApplyFriendAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyc);
        recyclerView.setAdapter(applyFriendAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



        list = DataBaseHelper.getAllUser();
        if(!list.isEmpty()){
            for(User user:list){
                JMessageClient.getUserInfo(user.getUserName(), null, new GetUserInfoCallback() {
                    @Override public void gotResult(int i, String s, UserInfo userInfo) {
                        LogUtil.d(i+"");
                        if(i==0){
                            applyFriendAdapter.addData(userInfo);
                        }
                    }
                });
            }

            for (int j =0;j<list.size();j++){
                User user = list.get(j);

                int finalJ = j;
                JMessageClient.getUserInfo(user.getUserName(), null, new GetUserInfoCallback() {
                    @Override public void gotResult(int i, String s, UserInfo userInfo) {
                        LogUtil.d(i+"");
                        if(i==0){
                            applyFriendAdapter.addData(userInfo);

                            //获取头像
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override public void gotResult(int i, String s, Bitmap bitmap) {
                                    if(bitmap!=null){
                                        runOnUiThread(new Runnable() {
                                            @Override public void run() {
                                                applyFriendAdapter.notifyItemChanged(finalJ,bitmap);
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    }
                });

            }
        }






    }
}