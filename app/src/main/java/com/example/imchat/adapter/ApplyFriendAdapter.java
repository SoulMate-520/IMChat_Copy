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
import com.example.imchat.util.DataBaseHelper;
import com.example.imchat.util.LogUtil;
import com.example.imchat.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class ApplyFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserInfo> mUserInfoList = new ArrayList<>();
    private String userName;

    public void addData(UserInfo userInfo){

        LogUtil.d("setData"+userInfo);

        mUserInfoList.add(userInfo);
        notifyItemChanged(mUserInfoList.size()-1);


    }

    public ApplyFriendAdapter(String userName){
        this.userName = userName;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApplyFriendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder viewHolder, int position) {
        ApplyFriendViewHolder holder=(ApplyFriendViewHolder) viewHolder;
        UserInfo userInfo=mUserInfoList.get(position);

        holder.name.setText(userInfo.getUserName());
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactManager.acceptInvitation(userInfo.getUserName(), null, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                            if(i==0){
                                holder.confirm.setText("已同意");
                                holder.confirm.setClickable(false);

                                //删除数据库
                                DataBaseHelper.deleteUser(userName,userInfo.getUserName());

                            }else{
                                Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
                                LogUtil.d(s);
                            }
                    }
                });
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position, @NonNull  List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        ApplyFriendViewHolder applyFriendViewHolder=(ApplyFriendViewHolder) holder;
        for(Object o:payloads){
            Bitmap bitmap=(Bitmap) o;
            applyFriendViewHolder.header.setImageBitmap(bitmap);
        }
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
