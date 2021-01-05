package com.bibi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bibi.base.BaseFragment;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/15
 */
public class PagerSimpleAdapter  extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public PagerSimpleAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }



    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
