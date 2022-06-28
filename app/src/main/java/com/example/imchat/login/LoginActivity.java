package com.example.imchat.login;

import static com.example.imchat.util.MyApplication.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
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
     *
     * @return 0 登陆成功; -1 账号或密码错误,登录失败;
     */
    private int loginClicked() {
        //获取文本框数据
        loginUserName = loginUserNameETxt.getText().toString();
        loginPwd = loginPwdETxt.getText().toString();

        //文本数据判空
        if (TextUtils.isEmpty(loginPwd) || TextUtils.isEmpty(loginUserName)) {
            return -1;
        }

        //登录申请后台
        JMessageClient.login(loginUserName, loginPwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    //登录成功
                    //判断密码正确与否
                    if (JMessageClient.isCurrentUserPasswordValid(loginPwd)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class) .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); ;
                        intent.putExtra("userName", loginUserName);
                        startActivity(intent);

//                        //关闭其他所有activity
//                        Intent intent = new Intent(getActivity(), LoginActivity.class) .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                    } else {
                        JMessageClient.logout();
                        Toast toast = Toast.makeText(MyApplication.getContext(), "密码错误，登录失败！", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();                    }
                } else {
                    Toast toast = Toast.makeText(MyApplication.getContext(), "登录失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();                }
            }
        });

        return 0;
    }

    /**
     * 去注册按钮点击方法
     */
    private void goRegister() {
        //跳转注册页面
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //权限
        requestPower();

        //获取控件
        loginSub = findViewById(R.id.login_sub);
        loginGoRegister = findViewById(R.id.login_goregister);

        loginUserNameETxt = findViewById(R.id.login_username);
        loginPwdETxt = findViewById(R.id.login_pwd);

        //登录按钮点击事件
        loginSub.setOnClickListener(view -> {
            if (loginClicked() == -1) {
                Toast toast = Toast.makeText(MyApplication.getContext(), "账号或密码输入有误！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();            }
        });

        //去注册按钮点击事件
        loginGoRegister.setOnClickListener(view -> goRegister());
    }


    public void requestPower() {

        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECORD_AUDIO)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO,}, 1);
            }
        }

        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            }
        }

        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }


    }

}