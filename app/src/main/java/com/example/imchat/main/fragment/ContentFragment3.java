package com.example.imchat.main.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.base.BaseFragment;
import com.example.imchat.login.LoginActivity;
import com.example.imchat.main.activity.MainActivity;
import com.example.imchat.util.LogUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment3 extends BaseFragment {
    MainActivity activity;

    //获取控件
    TextView exitTV, userNameTV;
    ImageView head;
    RelativeLayout updatePwdRL;

    //数据
    String userName;
    Bitmap headImage;
    String headImagePath;

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
        head = view.findViewById(R.id.iv_head);

        //登出按钮点击事件
        exitTV.setOnClickListener(v -> {
//
//
//            getActivity().onBackPressed();


            //关闭其他所有activity
            Intent intent = new Intent(getActivity(), LoginActivity.class) .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        //修改密码点击事件
        updatePwdRL.setOnClickListener(v -> updatePwd());

        //修改头像点击事件
        head.setOnClickListener(v -> updateHead());

        return view;
    }

    /**
     * 更改头像，开图库
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            //图库
            String path = null;
            try {
                Uri uri = data.getData();
                Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    try {
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = new File(path);

            String finalPath = path;
            JMessageClient.updateUserAvatar(file, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    LogUtil.d("修改头像"+s);
                    if (i == 0) {
                        Toast.makeText(MyApplication.getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                        head.setImageBitmap(BitmapFactory.decodeFile(finalPath));
                    } else {
                        Toast.makeText(MyApplication.getContext(), "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * 更改头像
     */
    private void updateHead() {
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, 0);
    }

    /**
     * 更改密码
     */
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
                                if (i == 0) {
                                    Toast.makeText(MyApplication.getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(MyApplication.getContext(), "修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).create();
        dialog.show();

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        //获取activity实例
        activity = (MainActivity) getActivity();

        //初始化获取账号
        userName = activity.getUserName();

        //初始化获取头像数据
        JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                System.out.println(s);
                if (i == 0) {
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            if (i == 0) {
                                headImage = bitmap;
                                //更改头像布局
                                head.setImageBitmap(headImage);
                            }
                        }
                    });
                } else {
                    Toast.makeText(MyApplication.getContext(), "初始化头像失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        //更改账号布局
        userNameTV.setText(userName);
    }

    @Override public void onResume() {
        super.onResume();
        initData();
    }
}