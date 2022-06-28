package com.example.imchat.main.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.bean.Constant;
import com.example.imchat.util.LogUtil;
import com.example.imchat.widget.CircleImageView;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class NewFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        EditText editText = findViewById(R.id.et_userName);
        RelativeLayout newFriend=findViewById(R.id.rl_new_friend);
        CircleImageView header=findViewById(R.id.iv_new_friend_header);
        TextView name=findViewById(R.id.tv_name);
        TextView confirm=findViewById(R.id.tv_confirm);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0) {
                    if (v.length() != 0) {
                        String userName=v.getText().toString();
                        JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
                            @Override
                            public void gotResult(int i, String s, UserInfo userInfo) {
                                if(i==0){
                                    newFriend.setVisibility(View.VISIBLE);
                                    confirm.setText("申请");
                                    confirm.setClickable(true);

                                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Bitmap bitmap) {


                                            LogUtil.d("获取头像："+s+"\n"+bitmap);
                                            if (i == 0){
                                                runOnUiThread(new Runnable() {
                                                    @Override public void run() {
                                                        //更改头像布局
                                                        try {
                                                            header.setImageBitmap(bitmap);

                                                        }catch (Exception e){
                                                            LogUtil.d("cnmmmmmmm"+e);
                                                        }

                                                    }
                                                });

                                            }else {

                                                runOnUiThread(new Runnable() {
                                                    @Override public void run() {
                                                        //更改头像布局
                                                        Toast.makeText(MyApplication.getContext(), "该用户没头像！", Toast.LENGTH_SHORT).show();
                                                        header.setImageResource(R.mipmap.ic_head_default_right);
                                                    }
                                                });

                                            }
                                        }
                                    });

                                    name.setText(userName);

                                    confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ContactManager.sendInvitationRequest(userName, null, "", new BasicCallback() {
                                                @Override
                                                public void gotResult(int i, String s) {
                                                    if(i==0){
                                                        confirm.setText("已申请");
                                                        confirm.setClickable(false);
                                                    }else{
                                                        Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }else
                                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getBaseContext(), "请输入用户名", android.widget.Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return false;
            }
        });
    }
}