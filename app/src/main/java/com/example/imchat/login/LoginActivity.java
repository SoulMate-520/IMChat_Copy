package com.example.imchat.login;

import static com.example.imchat.util.MyApplication.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imchat.R;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private TextView loginSub;
    private EditText loginUserNameETxt, loginPwdETxt;
    
    private String loginUserName, loginPwd;

    //登录点击
    private int loginClicked(){
        //获取文本框数据
        loginUserName = loginUserNameETxt.getText().toString();
        loginPwd = loginPwdETxt.getText().toString();

        //文本数据判空
        if (loginPwd == null || loginUserName == null){
            return -1;
        }

        //申请后台


        //登录成功 跳转页面



        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取控件
        loginSub = findViewById(R.id.login_sub);
        loginUserNameETxt = findViewById(R.id.login_username);
        loginPwdETxt = findViewById(R.id.login_pwd);

        //点击事件
        loginSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginClicked() == -1){
                    Toast.makeText(getContext(),"账号或密码输入有误！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}