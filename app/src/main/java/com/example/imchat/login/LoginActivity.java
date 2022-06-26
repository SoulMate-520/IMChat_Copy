package com.example.imchat.login;

import static com.example.imchat.util.MyApplication.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.main.activity.MainActivity;

import org.w3c.dom.Text;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends AppCompatActivity {

    //控件
    private TextView loginSub, loginGoRegister;
    private EditText loginUserNameETxt, loginPwdETxt;

    //数据
    private String loginUserName, loginPwd;

    /**
     * 登录按钮点击方法
     * @return 0 登陆成功; -1 账号或密码错误,登录失败;
     */
    private int loginClicked(){
        //获取文本框数据
        loginUserName = loginUserNameETxt.getText().toString();
        loginPwd = loginPwdETxt.getText().toString();

        //文本数据判空
        if (TextUtils.isEmpty(loginPwd) || TextUtils.isEmpty(loginUserName)){
            return -1;
        }

        //申请后台
        JMessageClient.login(loginUserName, loginPwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0){
                    //登录成功,跳转页面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userName", loginUserName);
                    startActivity(intent);
                }else{
                    Toast.makeText(MyApplication.getContext(), "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return 0;
    }

    /**
     * 去注册按钮点击方法
     */
    private void goRegister(){
        //跳转注册页面
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取控件
        loginSub = findViewById(R.id.login_sub);
        loginGoRegister = findViewById(R.id.login_goregister);

        loginUserNameETxt = findViewById(R.id.login_username);
        loginPwdETxt = findViewById(R.id.login_pwd);

        //登录按钮点击事件
        loginSub.setOnClickListener(view -> {
            if (loginClicked() == -1){
                Toast.makeText(getContext(),"账号或密码输入有误！",Toast.LENGTH_SHORT).show();
            }
        });

        //去注册按钮点击事件
        loginGoRegister.setOnClickListener(view -> goRegister());
    }
}