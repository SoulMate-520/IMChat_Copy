package com.example.imchat.main.activity;

import android.content.Intent;
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
	String userName;
	RecyclerView recyclerView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");

		setContentView(R.layout.activity_apply_friend);

		List<UserInfo> userInfos = new ArrayList<>();

		applyFriendAdapter = new ApplyFriendAdapter(userName);

		recyclerView = findViewById(R.id.recyc);
		recyclerView.setAdapter(applyFriendAdapter);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);

		list = DataBaseHelper.getAllUser(userName);
		LogUtil.d(list + "");
		if (!list.isEmpty()) {
			for (int j = 0; j < list.size(); j++) {
				User user = list.get(j);
				int finalJ = j;
				JMessageClient
						.getUserInfo(user.getTargetUserName(), null, new GetUserInfoCallback() {
							@Override public void gotResult(int i, String s, UserInfo userInfo) {
								LogUtil.d(i + "");
								if (i == 0) {
									applyFriendAdapter.addData(userInfo);
									userInfos.add(userInfo);

									//获取头像
									userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
										@Override public void gotResult(int i, String s,
												Bitmap bitmap) {

											LogUtil.d("applyHead" + bitmap);

											if (bitmap != null) {

												LogUtil.d(userInfos.indexOf(userInfo)+"d1");
												LogUtil.d(finalJ+"d2");
												LogUtil.d(applyFriendAdapter.getItemCount()+"d3");
												applyFriendAdapter.notifyItemChanged(
														userInfos.indexOf(userInfo), bitmap);

											}
										}
									});

								}
							}
						});
			}
		}

		//        userInfos.sort((o1, o2) -> o1.getUserName().compareTo(o2.getUserName()));
		//
		//        applyFriendAdapter.setData(userInfos);

		//        for (int j=0;j<userInfos.size();j++){
		//            UserInfo userInfo=userInfos.get(j);
		//            int finalJ = j;
		//            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
		//                @Override
		//                public void gotResult(int i, String s, Bitmap bitmap) {
		//
		//                    LogUtil.d("applyHead"+bitmap);
		//
		//                    if(bitmap!=null){
		//                        runOnUiThread(new Runnable() {
		//                            @Override
		//                            public void run() {
		//                                applyFriendAdapter.notifyItemChanged(finalJ,bitmap);
		//                            }
		//                        });
		//                    }
		//                }
		//            });
		//        }
		//

	}

	@Override public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}