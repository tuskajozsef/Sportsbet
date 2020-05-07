package com.sportsbet.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sportsbet.R;
import com.sportsbet.fragments.BalanceFragment;
import com.sportsbet.fragments.EventFragment;
import com.sportsbet.fragments.TicketFragment;
import com.sportsbet.fragments.TicketListFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    // This method is called whenever the adapter needs a Fragment for a certain position
    @Override
    public Fragment getItem(int position) {
        Fragment ret = null;
        switch (position) {
            case 0:
                ret = new EventFragment();
                break;
            case 1:
                ret = new TicketFragment();
                break;

            case 2:
                ret = new TicketListFragment();
                break;

            case 3:
                ret = new BalanceFragment();
                break;
        }
        return ret;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch (position) {
            case 0:
                title = context.getString(R.string.events);
                break;
            case 1:
                title = context.getString(R.string.ticket);
                break;
            case 2:
                title = context.getString(R.string.previous_tickets);
                break;
            case 3:
                title = context.getString(R.string.balance);
                break;
            default:
                title = "";
        }
        return title;
    }

    @Override
    public int getCount() {
        return 4;
    }
}