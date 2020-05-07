package com.sportsbet.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportsbet.R;
import com.sportsbet.adapter.BettingEventAdapter;
import com.sportsbet.adapter.BettingTicketAdapter;
import com.sportsbet.data.BettingItem;
import com.sportsbet.data.BettingListDatabase;
import com.sportsbet.data.Ticket;

import java.text.DecimalFormat;
import java.util.List;

public class TicketFragment extends Fragment implements DataChangedListener {

    private RecyclerView recyclerView;
    TextView prizeTextView;
    EditText stakeInput;
    Button bSave;

    private static TicketSavedListener listener;
    private BettingListDatabase database;
    private BettingTicketAdapter adapter;
    double odds = 1;
    int stake = 0;

    public void setTicketSavedListener(TicketSavedListener callback){
        listener=callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = BettingListDatabase.getInstance(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket,
                container, false);

        recyclerView = view.findViewById(R.id.TicketRecyclerView);
        adapter = new BettingTicketAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setAdapter(adapter);
        prizeTextView = view.findViewById(R.id.TicketPrize);
        stakeInput = view.findViewById(R.id.TicketInput);
        bSave = view.findViewById(R.id.TicketSaveButton);

        loadItemsInBackground();

        stakeInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try {
                    stake = Integer.parseInt(stakeInput.getText().toString());
                    updateTextView();
                } catch (Exception e) {
                    System.out.println("Érvénytelen tét");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });


        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Ticket newTicket = new Ticket(stake, odds, adapter.getItems());
                if(listener!=null)
                listener.OnTicketSave(newTicket);
            }
        });

        return view;
    }


    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<BettingItem>>() {

            @Override
            protected List<BettingItem> doInBackground(Void... voids) {
                return database.bettingItemDao().getAllPredicted();
            }

            @Override
            protected void onPostExecute(List<BettingItem> bettingItems) {
                if(adapter!=null)
                adapter.update(bettingItems);
                odds = 1;
                for (
                        BettingItem i : bettingItems) {
                    switch (i.outcome) {
                        case HOME:
                            odds *= i.homeOdds;
                            break;

                        case DRAW:
                            odds *= i.drawOdds;
                            break;

                        case AWAY:
                            odds *= i.awayOdds;
                            break;

                        default:
                            odds *= 0;
                            break;
                    }

                }
                updateTextView();
            }
        }.execute();
    }

    private void updateTextView() {
        DecimalFormat df = new DecimalFormat("#");
        prizeTextView.setText(String.format(getActivity().getResources().getString(R.string.ticketFragmentPrize), df.format(stake * odds)));
    }


    @Override
    public void refreshData() {
        loadItemsInBackground();
    }

    public interface TicketSavedListener{
        public void OnTicketSave(Ticket t);
    }
}
