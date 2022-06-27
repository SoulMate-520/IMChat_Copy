package com.example.imchat.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.example.imchat.MyApplication;
import com.example.imchat.R;
import com.example.imchat.adapter.viewPageAdapter;
import com.example.imchat.base.BaseActivity;
import com.example.imchat.chat.view.ChatActivity;
import com.example.imchat.contact.view.ContactFragment;
import com.example.imchat.main.fragment.ContentFragment1;
import com.example.imchat.main.fragment.ContentFragment2;
import com.example.imchat.main.fragment.ContentFragment3;
import com.example.imchat.util.ActivityUtil;
import com.example.imchat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends BaseActivity {

	//底部导航栏三方件
	private BottomNavigationBar bottomNavigationBar;
	private TextView mTitle;
	private ViewPager mViewPager;
	private List<Fragment> fragmentList = new ArrayList<>();

	private ContentFragment1 contentFragment1;
	private ContentFragment2 contentFragment2;
	private ContentFragment3 contentFragment3;

	private androidx.fragment.app.FragmentManager fragmentManager;



	@Override public int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

		// 申请多个权限。
		requestPower();

		initComponent();

		//初始化viewPager
		setViewPager();
		//底部导航栏
		setBottomNavigationBar();

		//测试用
		//i=0才成功 密码错误：Invalid password

		JMessageClient.login("654321", "654321", new BasicCallback() {
			@Override public void gotResult(int i, String s) {
				LogUtil.d(""+i);
				LogUtil.d(s);

				Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
				intent.putExtra("userName","123456");

				startActivity(intent);

//				ActivityUtil.actionStart(ChatActivity.class,"123456","userName");

			}
		});





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


	private void setViewPager() {
		fragmentManager = getSupportFragmentManager();

		//相关业务逻辑数据传入碎片
		contentFragment1 = ContentFragment1.newInstance("","");
		contentFragment2 = ContentFragment2.newInstance("","");
		contentFragment3 = ContentFragment3.newInstance("","");

		fragmentList.add(contentFragment1);
		fragmentList.add(contentFragment2);
		fragmentList.add(contentFragment3);


		mViewPager.setAdapter(new viewPageAdapter(fragmentManager,fragmentList));

		//滑动切换监听
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override public void onPageSelected(int position) {
				bottomNavigationBar.selectTab(position);
			}

			@Override public void onPageScrollStateChanged(int state) {

			}
		});

	}

	/**
	 * findView
	 */
	private void initComponent() {
		bottomNavigationBar =  findViewById(R.id.bottom_navigation_bar);
		mTitle = findViewById(R.id.tv_title);
		mViewPager = findViewById(R.id.viewPager);
	}

	/**
	 * 设置底部导航栏
	 */
	private void setBottomNavigationBar() {

		bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING)
				.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

		bottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
				.setActiveColor(R.color.green)//选中颜色 图标和文字
				//                .setInActiveColor("#8e8e8e")//默认未选择颜色
				.setBarBackgroundColor("#ECECEC");//默认背景色

		bottomNavigationBar //设置选中和未选中时的图片展示
				.addItem(new BottomNavigationItem(R.drawable.weixin_pressed, "微信").setInactiveIconResource(R.drawable.weixin_normal))
				.addItem(new BottomNavigationItem(R.drawable.contact_list_pressed, "通讯录").setInactiveIconResource(R.drawable.contact_list_normal))
				.addItem(new BottomNavigationItem(R.drawable.profile_pressed, "我").setInactiveIconResource(R.drawable.profile_normal))

				.setFirstSelectedPosition(0)//设置默认选择的按钮
				.initialise();//所有的设置需在调用该方法前完成

		//        bottomNavigationBar.setAutoHideEnabled(true);

		bottomNavigationBar //设置tab点击事件
				.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
					@Override public void onTabSelected(int position) {

						switch (position) {

						case 0:
							mViewPager.setCurrentItem(0,true);
							mTitle.setText("微信");

							break;
						case 1:
							mViewPager.setCurrentItem(1,true);
							mTitle.setText("通讯录");

							break;
						case 2:
							mViewPager.setCurrentItem(2,true);
							mTitle.setText("我的");

							break;

						}

					}

					@Override public void onTabUnselected(int position) {

					}

					@Override public void onTabReselected(int position) {

					}

				});
	}

	/**
	 * 退出
	 */
	@Override protected void onDestroy() {
		super.onDestroy();

		JMessageClient.logout();
	}





}