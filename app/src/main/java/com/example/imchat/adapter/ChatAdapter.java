package com.example.imchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imchat.R;
import com.example.imchat.util.AudioPlayManager;
import com.example.imchat.util.GlideUtils;
import com.example.imchat.util.IAudioPlayListener;
import com.example.imchat.util.LogUtil;
import com.example.imchat.util.TimeFormat;
import com.example.imchat.widget.BubbleImageView;
import com.example.imchat.widget.CircleImageView;
import com.example.imchat.widget.ImageDialog;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;
    private static final int SEND_VOICE = R.layout.item_voice_send;
    private static final int RECEIVE_VOICE = R.layout.item_voice_receive;
    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;

    private LinkedList<Message> mLinkedList = new LinkedList();

    private Context mContext;

    private Bitmap left;
    private Bitmap right;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setLeft(Bitmap left) {
        this.left = left;
    }

    public void setRight(Bitmap right) {
        this.right = right;
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
    }

    //首部增添数据（用于上滑刷新）
    public void addDataFirst(List<Message> messages) {
        if (messages != null) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                mLinkedList.addFirst(messages.get(i));
                notifyItemInserted(0);
            }
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
        } else if (message.getContentType() == ContentType.voice) {
            if (message.getDirect() == MessageDirect.send)
                return SEND_VOICE;
            else if (message.getDirect() == MessageDirect.receive)
                return RECEIVE_VOICE;
        } else if (message.getContentType() == ContentType.image) {
            if (message.getDirect() == MessageDirect.send)
                return SEND_IMAGE;
            else if (message.getDirect() == MessageDirect.receive)
                return RECEIVE_IMAGE;
        }
        return super.getItemViewType(position);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SEND_TEXT: {
                return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_send, parent, false));
            }
            case RECEIVE_TEXT: {
                return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_receive, parent, false));
            }
            case SEND_VOICE: {
                return new VoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_send, parent, false));
            }
            case RECEIVE_VOICE: {
                return new VoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_receive, parent, false));
            }
            case SEND_IMAGE: {
                return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_send, parent, false));
            }
            case RECEIVE_IMAGE: {
                return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_receive, parent, false));
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindChatViewHolder((ChatViewHolder) holder, position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case SEND_TEXT:
            case SEND_VOICE:
            case SEND_IMAGE:
                onBindSendViewHolder((SendViewHolder) holder, position);
                break;
        }
        switch (itemViewType) {
            case SEND_TEXT:
            case RECEIVE_TEXT: {
                onBindTextViewHolder((TextViewHolder) holder, position);
                return;
            }
            case SEND_VOICE:
            case RECEIVE_VOICE: {
                onBindVoiceViewHolder((VoiceViewHolder) holder, position);
                return;
            }
            case SEND_IMAGE:
            case RECEIVE_IMAGE: {
                onBindImageViewHolder((ImageViewHolder) holder, position);
                return;
            }
        }
    }

    //消息状态（用于新发消息）
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads);
            return;
        }
        SendViewHolder holder = (SendViewHolder) viewHolder;
        for (Object payload : payloads) {
            LogUtil.d("发送状态" + payload);
            switch (String.valueOf(payload)) {
                case "going": {
                    holder.fail.setVisibility(View.INVISIBLE);
                    holder.progress.setVisibility(View.VISIBLE);
                    break;
                }
                case "fail": {
                    holder.fail.setVisibility(View.VISIBLE);
                    holder.progress.setVisibility(View.INVISIBLE);
                    break;
                }
                case "success": {
                    holder.progress.setVisibility(View.INVISIBLE);
                    holder.progress.setVisibility(View.INVISIBLE);
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mLinkedList.size();
    }

    private void onBindChatViewHolder(ChatViewHolder holder, int position) {
        Message message = mLinkedList.get(position);
        if (message == null)
            return;
        //设置时间
        if (position == 0) {
            TimeFormat timeFormat = new TimeFormat(mContext, message.getCreateTime());
            holder.time.setText(timeFormat.getDetailTime());
            holder.time.setVisibility(View.VISIBLE);
        } else {
            Message oldMessage = mLinkedList.get(position - 1);

            if (oldMessage != null) {

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
        if(message.getDirect()==MessageDirect.receive){
            holder.header.setImageBitmap(left);
        }else{
            holder.header.setImageBitmap(right);

        }
    }

    private void onBindSendViewHolder(SendViewHolder holder, int position) {
        Message message = mLinkedList.get(position);

        //消息状态（用于历史消息）
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

    public void onBindTextViewHolder(TextViewHolder holder, int position) {
        Message message = mLinkedList.get(position);

        //文本内容
        TextContent textContent = (TextContent) message.getContent();
        //设置文本消息
        holder.content.setText((textContent.getText()));

    }

    public void onBindVoiceViewHolder(VoiceViewHolder holder, int position) {
        Message message = mLinkedList.get(position);

        //语音内容
        VoiceContent voiceContent = (VoiceContent) message.getContent();

        //设置语音播放
        holder.voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里设置。。。
                LogUtil.d("路径" + voiceContent.getLocalPath());
                MessageDirect direct = message.getDirect();
                AudioPlayManager.getInstance().startPlay(mContext, Uri.parse(voiceContent.getLocalPath()), new IAudioPlayListener() {
                    @Override
                    public void onStart(Uri var1) {
                        //声音播放动画
                        if (direct == MessageDirect.send) {
                            holder.ivVoice.setBackgroundResource(R.drawable.audio_animation_right_list);
                        } else
                            holder.ivVoice.setBackgroundResource(R.drawable.audio_animation_left_list);
                        AnimationDrawable drawable = (AnimationDrawable) holder.ivVoice.getBackground();
                        drawable.start();
                    }

                    @Override
                    public void onStop(Uri var1) {
                        if (direct == MessageDirect.send) {
                            holder.ivVoice.setBackgroundResource(R.mipmap.voice_animation_list_right_3);
                        } else
                            holder.ivVoice.setBackgroundResource(R.mipmap.voice_animation_list_left_3);
                    }

                    @Override
                    public void onComplete(Uri var1) {
                        if (direct == MessageDirect.send) {
                            holder.ivVoice.setBackgroundResource(R.mipmap.voice_animation_list_right_3);
                        } else
                            holder.ivVoice.setBackgroundResource(R.mipmap.voice_animation_list_left_3);
                    }
                });
            }
        });

        //设置语音长度
        holder.duration.setText(String.valueOf(voiceContent.getDuration()));

    }

    public void onBindImageViewHolder(ImageViewHolder holder, int position) {
        Message message = mLinkedList.get(position);

        //图片内容
        ImageContent imageContent = (ImageContent) message.getContent();

        LogUtil.d("suo" + imageContent.getLocalThumbnailPath());
        //设置缩略图
        if (imageContent.getLocalThumbnailPath() != null)
            GlideUtils.loadChatImage(mContext, imageContent.getLocalThumbnailPath(), holder.picture);
        else
            holder.picture.setImageResource(R.mipmap.default_img_failed);
        //设置图片点击
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里设置。。。
                ImageDialog imageDialog = new ImageDialog(mContext);
                imageDialog.show();

                View view = imageDialog.getView();
                LogUtil.d("suo1"+imageContent.getLocalPath());
                PhotoView photoView = view.findViewById(R.id.photo_view);
                Button originPicture = view.findViewById(R.id.bt_originPicture);

                LogUtil.d(imageContent.getLocalThumbnailPath() + "diansuo");
                LogUtil.d(imageContent.getLocalPath() + "dianyuan");

                if (imageContent.getLocalPath() == null) {
                    photoView.setImageBitmap(BitmapFactory.decodeFile(imageContent.getLocalThumbnailPath()));
                    originPicture.setVisibility(View.VISIBLE);
                } else {
                    photoView.setImageBitmap(BitmapFactory.decodeFile(imageContent.getLocalPath()));
                    originPicture.setVisibility(View.INVISIBLE);
                }

                photoView.setOnClickListener(v1 -> imageDialog.dismiss());

                originPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageContent.downloadOriginImage(message, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                LogUtil.d(s);
                                if (i == 0) {
                                    photoView.setImageBitmap(BitmapFactory.decodeFile(imageContent.getLocalPath()));
                                    originPicture.setVisibility(View.INVISIBLE);
                                } else {
                                    Toast.makeText(mContext, "查看失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    static class TextViewHolder extends SendViewHolder {
        TextView content;

        public TextViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.chat_item_content_text);
        }
    }

    static class VoiceViewHolder extends SendViewHolder {
        TextView duration;
        RelativeLayout voice;
        ImageView ivVoice;

        public VoiceViewHolder(View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.tvDuration);
            voice = itemView.findViewById(R.id.rlVoice);
            ivVoice = itemView.findViewById(R.id.ivVoice);
        }
    }

    static class ImageViewHolder extends SendViewHolder {
        BubbleImageView picture;

        public ImageViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.bivPic);
        }
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        CircleImageView header;

        public ChatViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_tv_time);
            header = itemView.findViewById(R.id.chat_item_header);
        }
    }

    static class SendViewHolder extends ChatViewHolder {
        ImageView fail;
        ProgressBar progress;

        public SendViewHolder(View itemView) {
            super(itemView);
            fail = itemView.findViewById(R.id.chat_item_fail);
            progress = itemView.findViewById(R.id.chat_item_progress);
        }
    }

}

