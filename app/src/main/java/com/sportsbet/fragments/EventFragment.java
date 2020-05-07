package com.sportsbet.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsbet.R;
import com.sportsbet.adapter.BettingEventAdapter;
import com.sportsbet.data.BettingItem;
import com.sportsbet.data.BettingListDatabase;
import com.sportsbet.model.Answer;
import com.sportsbet.model.BettingData;
import com.sportsbet.network.NetworkManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends Fragment implements DataChangedListener{

    private RecyclerView recyclerView;
    static private BettingEventAdapter adapter;
    private BettingListDatabase database;
    private static Answer bettingData;
    private static final String TAG = "EVENT";

    OnEventSelectedListener callback;

    public  void setOnEventSelectedListener(OnEventSelectedListener listener) {
        callback = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = BettingListDatabase.getInstance(this.getContext());

        loadBettingData();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events,
                container, false);

        recyclerView = view.findViewById(R.id.MainRecyclerView);
        adapter = new BettingEventAdapter(callback);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        loadItemsInBackground();

        return view;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<BettingItem>>() {

            @Override
            protected List<BettingItem> doInBackground(Void... voids) {
                return database.bettingItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<BettingItem> bettingItems) {
                adapter.update(bettingItems);
            }
        }.execute();
    }


    public void onItemInserted(final BettingItem item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                long id = database.bettingItemDao().insert(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("Event", "BettingItem insert was successful");
            }
        }.execute();
    }

    private void loadBettingData() {
        NetworkManager.getInstance().getOdds("soccer").enqueue(new Callback<Answer>() {

            @Override
            public void onResponse(@NonNull Call<Answer> call,
                                   @NonNull Response<Answer> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    bettingData = response.body();

                    for (BettingData bd : bettingData.data) {
                        BettingItem newItem = new BettingItem("Football", bd.sport_nice, bd.home_team, bd.getAwayTeam(), bd.getDate(), bd.getHomeOdds(), bd.getDrawOdds(), bd.getAwayOdds());
                        onItemInserted(newItem);
                        adapter.addItem(newItem);
                    }

                } else {
                    Toast.makeText(getActivity(),
                            "Error: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Answer> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.networkError),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItemsInBackground();
    }

    @Override
    public void refreshData() {
        loadItemsInBackground();
    }

    public interface OnEventSelectedListener {
        public void onEventSelected(BettingItem item);
    }
}