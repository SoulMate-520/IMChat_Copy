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
import com.example.imchat.widget.CircleImageView;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class NewFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        EditText editText = findViewById(R.id.et_userName);
        RelativeLayout newFriend=findViewById(R.id.rl_new_friend);
        CircleImageView header=findViewById(R.id.iv_header);
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

                                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Bitmap bitmap) {
                                            if (i == 0){
                                                //更改头像布局
                                                header.setImageBitmap(bitmap);
                                            }else {
                                                Toast.makeText(MyApplication.getContext(), "头像获取失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    name.setText(userName);

                                    confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

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