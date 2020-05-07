package com.sportsbet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sportsbet.R;
import com.sportsbet.adapter.BettingTicketAdapter;
import com.sportsbet.adapter.BettingTicketListAdapter;
import com.sportsbet.data.BettingItem;
import com.sportsbet.data.BettingListDatabase;
import com.sportsbet.data.Ticket;

import java.util.List;

public class TicketListFragment extends Fragment implements DataChangedListener {

    private BettingTicketListAdapter parentAdapter;
    private BettingListDatabase database;
    BettingTicketListAdapter.TicketCheckboxChangedListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = BettingListDatabase.getInstance(this.getContext());
    }

    public void setListener(BettingTicketListAdapter.TicketCheckboxChangedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_ticket_list,
                container, false);
        RecyclerView parentRecyclerView = view.findViewById(R.id.SavedTicketParentRecyclerView);
        parentAdapter = new BettingTicketListAdapter(listener);
        loadItemsInBackground();
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        parentRecyclerView.setAdapter(parentAdapter);

        return view;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Ticket>>() {

            @Override
            protected List<Ticket> doInBackground(Void... voids) {
                return database.ticketDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Ticket> tickets) {
                parentAdapter.update(tickets);
                System.out.println("Loading tickets was successful");
            }
        }.execute();
    }

    @Override
    public void refreshData() {
        loadItemsInBackground();
    }
}
