package com.example.imchat.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.imchat.R;
import com.example.imchat.constant.Constant;
import com.example.imchat.util.getNameUtil;

import java.util.List;

import butterknife.BindView;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: yzy
 * @date: 2022/6/16 13:48
 * @description:
 * @version:
 */
public class ConversationAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {

    public ConversationAdapter(int layoutResId, @Nullable List<Conversation> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        if(item.getTargetInfo() instanceof UserInfo){
            UserInfo userInfo = (UserInfo)item.getTargetInfo() ;
            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback(){

                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if(i==0){
                        ((ImageView)helper.getView(R.id.iv_head)).setImageBitmap(bitmap);
                    }else{
                        helper.setImageResource(R.id.iv_head,R.mipmap.user_head);
                    }
                }

            });

            helper.setText(R.id.tv_conv_name, getNameUtil.getName(userInfo));
        }





//        else{
//            GroupInfo groupInfo = (GroupInfo)item.getTargetInfo() ;
//            groupInfo.getAvatarBitmap(new GetAvatarBitmapCallback(){
//
//                @Override
//                public void gotResult(int i, String s, Bitmap bitmap) {
//                    if(i==0){
//                        ((ImageView)helper.getView(R.id.iv_head)).setImageBitmap(bitmap);
//                    }else{
//                        helper.setImageResource(R.id.iv_head,R.mipmap.head_default);
//                    }
//                }
//
//            });
//
//            helper.setText(R.id.tv_name,groupInfo.getGroupName());}

    Message lastMsg=item.getLatestMessage();
        if(lastMsg!=null){
        String contentStr;

        switch (lastMsg.getContentType()){
            case image:
                contentStr = "[??????]";
                break;
            case voice:
                contentStr = "[??????]";
                break;
            case location:
                contentStr = "[??????]";
                break;
            case file:
                contentStr = "[??????]";
                break;
            case video:
                contentStr = "[??????]";
                break;
            case eventNotification:
                contentStr = "[????????????]";
                break;


            case custom:

                CustomContent addressContent =   (CustomContent)lastMsg.getContent();
                String type = addressContent.getStringValue(Constant.TYPE);
                if(TextUtils.isEmpty(type)){
                    contentStr = "";
                    helper.setText(R.id.tv_content,contentStr);
                    return;
                }
                if(type.equals(Constant.RED_PACKEGE)){
                    contentStr = "[??????]";
                }else if(type.equals(Constant.ADDRESS)){
                    contentStr = "[??????]";
                }else if(type.equals(Constant.CARD)){
                    contentStr = "[????????????]";
                }else if(type.equals(Constant.INVITATION)){
                    contentStr = "[?????????]";
                }else {
                    contentStr = "[????????????]";
                }
                break;
            case prompt:
                contentStr =  ((PromptContent) lastMsg.getContent()).getPromptText();
                break;
            default:
                contentStr = ((TextContent) lastMsg.getContent()).getText();
                break;
        }

        helper.setText(R.id.tv_conv_mess,contentStr);
    }


        //?????????????????????
        int count = item.getUnReadMsgCnt();
        if(count>0){

            if(count>=99){
                helper.setText(R.id.tv_new,String.valueOf(99));
            }else{

                helper.setText(R.id.tv_new,String.valueOf(count));
            }

        }else{
            //?????????
            helper.getView(R.id.rl_unread).setVisibility(View.INVISIBLE);
        }



        //        if(item.getExtra().equals(Constant.NEW_MESSAGE)){
//        helper.getView(R.id.v_new).setVisibility(View.VISIBLE);
//
//    }else{
//        helper.getView(R.id.v_new).setVisibility(View.GONE);
//    }

}

}
