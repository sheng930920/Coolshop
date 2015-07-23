package com.coolshop.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import com.coolshop.activity.OrderFragment;
import com.coolshop.task.OrderData;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 * viewpager  ≈‰∆˜
 * 
 */
public class OrderAdapter extends FragmentStatePagerAdapter {


	private ArrayList<Fragment> fragments;
	private OrderData orderdata;
	FragmentManager fm;

	public OrderAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
}
