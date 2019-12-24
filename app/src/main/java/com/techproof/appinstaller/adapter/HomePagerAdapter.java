package com.techproof.appinstaller.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.techproof.appinstaller.activity.HomeActivity;
import com.techproof.appinstaller.fragment.ApksFragment;
import com.techproof.appinstaller.fragment.AppsFragment;

import java.util.ArrayList;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mFragmentTitleList = new ArrayList<>();

    public HomePagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    public Fragment getCurrent(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        return mFragmentList.get(position);
    }

    public int getItem(Fragment fragment) {
        //Returning the current tabs
        return mFragmentList.indexOf(fragment);
    }

    public void add(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
