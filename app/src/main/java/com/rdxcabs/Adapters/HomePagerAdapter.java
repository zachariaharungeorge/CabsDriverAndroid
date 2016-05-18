package com.rdxcabs.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rdxcabs.Constants.Constants;
import com.rdxcabs.Fragments.AcceptedTripListFragment;
import com.rdxcabs.Fragments.ProfileFragment;
import com.rdxcabs.Fragments.TripListFragment;

/**
 * Created by arung on 17/5/16.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new TripListFragment();
            case 2:
                return new AcceptedTripListFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "Rides Availabe";
            case 2:
                return "Rides Done";
        }
        return null;
    }
}
