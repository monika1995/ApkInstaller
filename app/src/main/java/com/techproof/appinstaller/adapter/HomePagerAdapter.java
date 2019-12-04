package com.techproof.appinstaller.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.techproof.appinstaller.fragment.ApksFragment;
import com.techproof.appinstaller.fragment.AppsFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    int totalTabs;

    public HomePagerAdapter(Context context, @NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AppsFragment();
            case 1:
                return new ApksFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
