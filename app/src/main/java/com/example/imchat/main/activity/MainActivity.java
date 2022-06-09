package com.example.imchat.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.imchat.R;
import com.example.imchat.adapter.viewPageAdapter;
import com.example.imchat.base.BaseActivity;
import com.example.imchat.chat.view.ChatActivity;
import com.example.imchat.main.fragment.ContentFragment1;
import com.example.imchat.main.fragment.ContentFragment2;
import com.example.imchat.main.fragment.ContentFragment3;
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

		initComponent();

		//初始化viewPager
		setViewPager();
		//底部导航栏
		setBottomNavigationBar();

		//测试用
		//i=0才成功 密码错误：Invalid password
		JMessageClient.login("123456", "123456", new BasicCallback() {
			@Override public void gotResult(int i, String s) {
				LogUtil.d(""+i);
				LogUtil.d(s);
			}
		});

//		startActivity(new Intent(this, ChatActivity.class));


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