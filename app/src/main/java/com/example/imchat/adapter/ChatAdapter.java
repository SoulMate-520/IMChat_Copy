package com.example.imchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imchat.R;
import com.example.imchat.util.LogUtil;
import com.example.imchat.util.TimeFormat;
import com.example.imchat.widget.CircleImageView;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;

    LinkedList<Message> mLinkedList = new LinkedList();

    Context mContext;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    //设置数据
    public void setData(List<Message> messages) {
        if (messages != null) {
            mLinkedList.clear();
            mLinkedList.addAll(messages);
            notifyDataSetChanged();
        }
    }

    //尾部增添数据（用于新发消息）
    public void addDataLast(Message message) {
        mLinkedList.addLast(message);
        notifyItemInserted(mLinkedList.size());
    }

    //首部增添数据（用于上滑刷新）
    public void addDataFirst(List<Message> messages) {
        if (messages != null) {
            for (int i = messages.size() - 1; i >= 0; i--){
                mLinkedList.addFirst(messages.get(i));
            }
            notifyItemRangeInserted(0,messages.size()-1);
        }
    }

    //判断消息类型，返回不同布局ID
    @Override
    public int getItemViewType(int position) {
        Message message = mLinkedList.get(position);
        if (message.getContentType() == ContentType.text) {
            if (message.getDirect() == MessageDirect.send)
                return SEND_TEXT;
            else if (message.getDirect() == MessageDirect.receive)
                return RECEIVE_TEXT;
        }
        return super.getItemViewType(position);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SEND_TEXT: {
                return new SendTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_send, parent, false));
            }
            case RECEIVE_TEXT: {
                return new ReceiveTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_receive, parent, false));
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case SEND_TEXT: {
                onBindSendTextViewHolder(holder, position);
                return;
            }
            case RECEIVE_TEXT: {
                onBindReceiveTextViewHolder(holder, position);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mLinkedList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }
        SendTextViewHolder sendHolder = (SendTextViewHolder) holder;
        for (Object payload : payloads) {
            LogUtil.d("发送状态" + String.valueOf(payload));
            switch (String.valueOf(payload)) {
                case "going": {
                    sendHolder.progress.setVisibility(View.VISIBLE);
                    break;
                }
                case "fail": {
                    sendHolder.fail.setVisibility(View.VISIBLE);
                    sendHolder.progress.setVisibility(View.INVISIBLE);
                    break;
                }
                case "success": {
                    sendHolder.progress.setVisibility(View.INVISIBLE);
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void onBindSendTextViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SendTextViewHolder holder = (SendTextViewHolder) viewHolder;
        Message message = mLinkedList.get(position);
        //设置时间
        if (position == 0) {
            TimeFormat timeFormat = new TimeFormat(mContext, message.getCreateTime());
            holder.time.setText(timeFormat.getDetailTime());
            holder.time.setVisibility(View.VISIBLE);
        } else {
            Message oldMessage = mLinkedList.get(position - 1);

            if (message != null && oldMessage != null) {

                long oldTime = oldMessage.getCreateTime();
                long newTime = message.getCreateTime();

                // 如果两条消息之间的间隔超过五分钟则显示时间
                if (newTime - oldTime > 300000) {
                    TimeFormat timeFormat = new TimeFormat(mContext, newTime);
                    holder.time.setText(timeFormat.getDetailTime());
                    holder.time.setVisibility(View.VISIBLE);
                } else {
                    holder.time.setVisibility(View.GONE);
                }

            } else {
                holder.time.setVisibility(View.GONE);
            }
        }

        //头像》》

        //设置文本消息
        if (message != null) {
            holder.content.setText(((TextContent) message.getContent()).getText());
        }

        switch (message.getStatus()) {
            case created:
            case send_going: {
                holder.progress.setVisibility(View.VISIBLE);
                break;
            }
            case send_fail: {
                holder.fail.setVisibility(View.VISIBLE);
                holder.progress.setVisibility(View.INVISIBLE);
                break;
            }
            case send_success: {
                holder.progress.setVisibility(View.INVISIBLE);
                break;
            }
            default:
                break;
        }
    }

    public void onBindReceiveTextViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ReceiveTextViewHolder holder = (ReceiveTextViewHolder) viewHolder;
        Message message = mLinkedList.get(position);
        //设置时间
        if (position == 0) {
            TimeFormat timeFormat = new TimeFormat(mContext, message.getCreateTime());
            holder.time.setText(timeFormat.getDetailTime());
            holder.time.setVisibility(View.VISIBLE);
        } else {
            Message oldMessage = mLinkedList.get(position - 1);

            if (message != null && oldMessage != null) {

                long oldTime = oldMessage.getCreateTime();
                long newTime = message.getCreateTime();

                // 如果两条消息之间的间隔超过五分钟则显示时间
                if (newTime - oldTime > 300000) {
                    TimeFormat timeFormat = new TimeFormat(mContext, newTime);
                    holder.time.setText(timeFormat.getDetailTime());
                    holder.time.setVisibility(View.VISIBLE);
                } else {
                    holder.time.setVisibility(View.GONE);
                }

            } else {
                holder.time.setVisibility(View.GONE);
            }
        }

        //头像》》

        //设置文本消息
        if (message != null) {
            holder.content.setText(((TextContent) message.getContent()).getText());
        }

    }

    static class SendTextViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        CircleImageView header;
        TextView content;
        ImageView fail;
        ProgressBar progress;

        public SendTextViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_tv_time);
            header = itemView.findViewById(R.id.chat_item_header);
            content = itemView.findViewById(R.id.chat_item_content_text);
            fail = itemView.findViewById(R.id.chat_item_fail);
            progress = itemView.findViewById(R.id.chat_item_progress);
        }
    }

    static class ReceiveTextViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        CircleImageView header;
        TextView content;

        public ReceiveTextViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_tv_time);
            header = itemView.findViewById(R.id.chat_item_header);
            content = itemView.findViewById(R.id.chat_item_content_text);
        }
    }

}

