package com.example.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Shroff on 13-Oct-17.
 */

public class profilePagesAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> listFragments;

    public profilePagesAdapter(FragmentManager fm, ArrayList<Fragment> listFragments) {
        super(fm);

        this.listFragments = listFragments;

    }

    // This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        return listFragments.get(position);
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return listFragments.size();
    }
}
