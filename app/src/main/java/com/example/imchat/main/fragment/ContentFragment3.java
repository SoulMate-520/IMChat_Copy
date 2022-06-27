package com.example.imchat.main.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.base.BaseFragment;
import com.example.imchat.login.LoginActivity;
import com.example.imchat.main.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment3 extends BaseFragment {
    MainActivity activity;

    //获取控件
    TextView exitTV;
    TextView userNameTV;
    RelativeLayout updatePwdRL;

    //数据
    String userName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContentFragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentFragment3 newInstance(String param1, String param2) {
        ContentFragment3 fragment = new ContentFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content3, container, false);

        //获取控件
        userNameTV = view.findViewById(R.id.tv_username);
        updatePwdRL = view.findViewById(R.id.rl_scanner);
        exitTV = view.findViewById(R.id.tv_exit);

        //登出按钮点击事件
        exitTV.setOnClickListener(v -> activity.onDestroy());
//        Intent intent = new Intent(activity.getBaseContext(), LoginActivity.class);
//        startActivity(intent);

        //修改密码点击事件
        updatePwdRL.setOnClickListener(v -> updatePwd());

        return view;
    }

    private void updatePwd() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_password, null);
        final EditText updatePwd1ETxt = (EditText) dialogView.findViewById(R.id.update_pwd1);
        final EditText updatePwd2ETxt = (EditText) dialogView.findViewById(R.id.update_pwd2);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("修改密码")//设置对话框的标题
                .setView(dialogView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String updatePwd1 = updatePwd1ETxt.getText().toString();
                        String updatePwd2 = updatePwd2ETxt.getText().toString();
                        JMessageClient.updateUserPassword(updatePwd1, updatePwd2, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0){
                                    Toast.makeText(MyApplication.getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(MyApplication.getContext(), "修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).create();
        dialog.show();

    }

    @Override
    protected void initData() {
        activity = (MainActivity) getActivity();
        userName = activity.getUserName();
    }

    @Override
    protected void initView() {
        userNameTV.setText(userName);
    }

}