package com.example.imchat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import java.util.List;

/**
 * @author Soul Mate
 * @brief 简单的功能介绍
 * @date 2022-06-04 15:06
 */

public class viewPageAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;

	public viewPageAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;

	}

	@NonNull @Override public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override public int getCount() {
		return fragmentList.size();
	}
}
