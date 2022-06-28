//package com.example.imchat.contact.view;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.imchat.R;
//import com.example.imchat.adapter.ContactAdapter;
//import com.example.imchat.base.BaseFragment;
//import com.example.imchat.bean.ContactBean;
//import com.example.imchat.contact.model.IContactModel;
//import com.example.imchat.contact.presenter.ContactPresenter;
//import com.example.imchat.util.ActivityUtil;
//
//import java.util.List;
//
//import butterknife.BindView;
//
///**
// * @author: yzy
// * @date: 2022/6/14 14:57
// * @description:
// * @version:
// */
//public class ContactFragment extends BaseFragment implements IContactsView, IContactModel {
//    @BindView(R.id.recyc_cont)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.relat_new_friend)
//    RelativeLayout newFriend;
//
//    private ContactAdapter adapter;
//    private ContactPresenter presenter;
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        presenter = new ContactPresenter(this,this);
//        initData();
//        initView();
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_content2;
//    }
//
//    @Override
//    protected void initView() {
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityUtil.getCurrentActivity()));
//    }
//
//    @Override
//    protected void initData() {
////        newFriend.setOnClickListener(view -> ActivityUtil.startActivity());
//    }
//
//
//    @Override
//    public void setContactsList(List<ContactBean> userList) {
//        adapter = new ContactAdapter(userList);
//        mRecyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void initEvents(List<ContactBean> userList) {
//
//    }
//
//    @Override public void updateHead(int pos) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override public void run() {
//                adapter.notifyItemChanged(pos,null);
//            }
//        });
//    }
//
//    @Override
//    public void sortData(List<ContactBean> list) {
//
//    }
//
//    /**
//     * 第一次启动前获取好友列表
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//}
