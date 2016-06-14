package com.example.demoviewpagerbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fangxu.view.ViewPagerBar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

@SuppressLint("NewApi")
public class TestActivity extends FragmentActivity {

	private ViewPagerBar idTest;
	private List<String> mDatas = Arrays.asList("title1", "title22222", "title3", "title4", "title5", "title6",
			"title7", "title8", "title9");
	private ViewPager vPager;
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		idTest = (ViewPagerBar) findViewById(R.id.idTest);
		vPager = (ViewPager) findViewById(R.id.id_vp);
		initDatas();
		vPager.setAdapter(mAdapter);
		idTest.setViewPager(vPager);
	}

	private void initDatas() {

		for (String data : mDatas) {
			VpSimpleFragment fragment = VpSimpleFragment.newInstance(data);
			mTabContents.add(fragment);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabContents.get(position);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				return mDatas.get(position);
			}
		};
	}

}
