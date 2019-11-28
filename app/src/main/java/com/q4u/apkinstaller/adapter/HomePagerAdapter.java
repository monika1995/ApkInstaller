package com.q4u.apkinstaller.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.q4u.apkinstaller.Common.DebugLogger;
import com.q4u.apkinstaller.fragment.ApksFragment;
import com.q4u.apkinstaller.fragment.AppsFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public HomePagerAdapter(Context context,@NonNull FragmentManager fm,int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AppsFragment appsFragment = new AppsFragment();
                return appsFragment;
            case 1:
                ApksFragment apksFragment = new ApksFragment();
                return apksFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
