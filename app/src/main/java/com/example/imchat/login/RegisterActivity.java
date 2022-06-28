package com.example.imchat.login;

import static com.example.imchat.MyApplication.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imchat.R;
import com.example.imchat.main.activity.MainActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class RegisterActivity extends AppCompatActivity {

    //控件
    private TextView registerSub;
    private EditText registerUserNameETxt, registerPwdETxt, registerPwd1ETxt;

    //数据
    private String registerUserName, registerPwd, registerPwd1;

    /**
     * 注册按钮点击方法
     *
     * @return 0 注册成功; -1 账号密码错误，注册失败;
     */
    private int registerClicked() {
        //获取文本框数据
        registerUserName = registerUserNameETxt.getText().toString();
        registerPwd = registerPwdETxt.getText().toString();
        registerPwd1 = registerPwd1ETxt.getText().toString();

        //文本数据判空
        if (TextUtils.isEmpty(registerPwd) || TextUtils.isEmpty(registerUserName) || TextUtils.isEmpty(registerPwd1)) {
            return -1;
        }

        //密码一致与否
        if (!(registerPwd.equals(registerPwd1))) {
            return -1;
        }

        //申请后台
        JMessageClient.register(registerUserName, registerPwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast toast = Toast.makeText(getContext(), "注册成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //注册成功即登录
                    JMessageClient.login(registerUserName, registerPwd, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                        }
                    });
                    //关闭其他所有activity
                    //跳转页面
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); ;
                    intent.putExtra("userName", registerUserName);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getContext(), "注册失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取控件
        registerSub = findViewById(R.id.register_sub);

        registerUserNameETxt = findViewById(R.id.register_username);
        registerPwdETxt = findViewById(R.id.register_pwd);
        registerPwd1ETxt = findViewById(R.id.register_pwd1);

        //注册按钮点击事件
        registerSub.setOnClickListener(view -> {
            if (registerClicked() == -1) {
                Toast toast = Toast.makeText(getContext(), "账号或密码输入有误！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

    }
}