package com.example.imchat.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class ApplyFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserInfo> mUserInfoList=new ArrayList<>();

    public void setData(List<UserInfo> userInfoList){
        if(userInfoList.size()!=0) {
            mUserInfoList.clear();
            mUserInfoList.addAll(userInfoList);
            notifyDataSetChanged();
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApplyFriendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder viewHolder, int position) {
        ApplyFriendViewHolder holder=(ApplyFriendViewHolder) viewHolder;
        UserInfo userInfo=mUserInfoList.get(position);
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (i == 0){
                    //更改头像布局
                    holder.header.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(MyApplication.getContext(), "头像获取失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.name.setText(userInfo.getUserName());
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserInfoList.size();
    }

    static class ApplyFriendViewHolder extends RecyclerView.ViewHolder{
        CircleImageView header;
        TextView name;
        TextView confirm;

        public ApplyFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            header=itemView.findViewById(R.id.iv_header);
            name=itemView.findViewById(R.id.tv_name);
            confirm=itemView.findViewById(R.id.tv_confirm);
        }
    }
}
