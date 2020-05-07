package com.sportsbet.fragments;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.widget.Toast;

import com.sportsbet.R;
import com.sportsbet.adapter.BettingEventAdapter;
import com.sportsbet.adapter.BettingTicketListAdapter;
import com.sportsbet.adapter.TabPagerAdapter;
import com.sportsbet.data.BettingItem;
import com.sportsbet.data.BettingListDatabase;
import com.sportsbet.data.Ticket;

import java.util.ArrayList;
import java.util.List;


public class PagerActivity extends AppCompatActivity implements EventFragment.OnEventSelectedListener, TicketFragment.TicketSavedListener, BettingTicketListAdapter.TicketCheckboxChangedListener {

    BettingListDatabase database;
    List<DataChangedListener> listeners=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        database = BettingListDatabase.getInstance(getBaseContext());
        clearTable();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ViewPager mainViewPager = findViewById(R.id.mainViewPager);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);
        mainViewPager.setAdapter(tabPagerAdapter);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof EventFragment) {
            EventFragment eventFragment = (EventFragment) fragment;
            eventFragment.setOnEventSelectedListener(this);
            listeners.add(eventFragment);
        }

        if (fragment instanceof TicketFragment) {
            TicketFragment ticketFragment = (TicketFragment) fragment;
            ticketFragment.setTicketSavedListener(this);
            listeners.add(ticketFragment);
        }

        if (fragment instanceof TicketListFragment) {
            TicketListFragment ticketListFragment = (TicketListFragment) fragment;
            ticketListFragment.setListener(this);
            listeners.add(ticketListFragment);
        }

        if (fragment instanceof BalanceFragment) {
            BalanceFragment balanceFragment = (BalanceFragment) fragment;
            listeners.add(balanceFragment);
        }
    }

    public void clearTable() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                database.bettingItemDao().cleanTable();
                return true;
            }
        }.execute();
    }

    @Override
    public void onEventSelected(final BettingItem item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.bettingItemDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("Event", "BettingItem update was successful");
            }
        }.execute();

        for(DataChangedListener l:listeners)
            l.refreshData();
    }

    @Override
    public void OnTicketSave(final Ticket newTicket) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                database.ticketDao().insert(newTicket);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                if (isSuccessful) {
                    for(DataChangedListener l:listeners)
                        l.refreshData();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.messageTicketSave) ,
                            Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }

    @Override
    public void onTicketCheckboxChanged(final Ticket t) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                database.ticketDao().update(t);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                if (isSuccessful) {
                    for(DataChangedListener l:listeners)
                        l.refreshData();
                    Log.d("Ticket", "Ticket insert was successful");
                }
            }

        }.execute();

    }
}

