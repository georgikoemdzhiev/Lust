package com.studentsins.lust.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.studentsins.lust.UI.EventsFragment;
import com.studentsins.lust.UI.FeedFragment;
import com.studentsins.lust.UI.SnapshotFragment;

/**
 * Fragment pager adapter to handle the
 * functionality of the viewpager
 * Created by koemdzhiev on 07/02/16.
 */
public class LustFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Snapshot","Feed","Events"};
    private Context mContext;

    public LustFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }
//Instantiate the fragments...
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SnapshotFragment.newInstance(0);
            case 1:
                return FeedFragment.newInstance(1);
            case 2:
                return EventsFragment.newInstance(2);

        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
