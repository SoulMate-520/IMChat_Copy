package com.example.imchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imchat.R;
import com.example.imchat.bean.ContactBean;
import com.example.imchat.util.ActivityUtil;
import com.example.imchat.util.MyApplication;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: yzy
 * @date: 2022/6/13 9:38
 * @description： 联系人列表Adapter
 * @version: 1.0
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyRecycleHolder> {

    private List<ContactBean> contactBeanList;
//    private Context mContext;

    public ContactAdapter(List<ContactBean> contactBeanList) {
//        this.mContext = context;
          this.contactBeanList = contactBeanList;
    }


    @NonNull
    @Override
    public ContactAdapter.MyRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, parent, false);
        MyRecycleHolder holder = new MyRecycleHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2022/6/13 跳转逻辑
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyRecycleHolder holder, int position) {
        //若无数据源
        if (contactBeanList == null || contactBeanList.size() == 0 || contactBeanList.size() <= position)
            return;
        ContactBean bean = contactBeanList.get(position);
        if (bean != null) {
            holder.tv_name.setText(bean.getNickName());
        }
    }

    @Override
    public int getItemCount() {
        return contactBeanList.size();
    }

    public void refreshList() {
        LitePal.deleteAll(ContactBean.class);
        LitePal.saveAll(contactBeanList);
    }

    public static class MyRecycleHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_cont_name)
        TextView tv_name;
        @BindView(R.id.iv_cont_img)
        ImageView iv_img;

        public MyRecycleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
