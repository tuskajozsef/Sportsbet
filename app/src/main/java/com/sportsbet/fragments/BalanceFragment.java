package com.sportsbet.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sportsbet.R;
import com.sportsbet.data.BettingListDatabase;
import com.sportsbet.data.Ticket;

import java.util.ArrayList;
import java.util.List;

public class BalanceFragment extends Fragment implements DataChangedListener {
    private BettingListDatabase database;
    List<Ticket> tickets=new ArrayList<>();

    int allStake = 0;
    int allPrize = 0;

    TextView allStakeText;
    TextView allPrizeText;
    TextView balance;
    GraphView graph;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = BettingListDatabase.getInstance(getContext());
        loadItemsInBackground();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_balance,
                container, false);

        allStakeText = view.findViewById(R.id.AllStake);
        allPrizeText = view.findViewById(R.id.AllPrize);
        balance = view.findViewById(R.id.Balance);
        graph = view.findViewById(R.id.ProfitGraph);

        countStatistics();

        return view;
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Ticket>>() {

            @Override
            protected List<Ticket> doInBackground(Void... voids) {
                return database.ticketDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Ticket> _tickets) {
                tickets=_tickets;
                countStatistics();
            }
        }.execute();
    }

    private void countStatistics() {
        allStake=0;
        allPrize=0;
        int index=1;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();;
        List<DataPoint> points=new ArrayList<>();


        if(tickets.size()!=0) {
            series.appendData(new DataPoint(0, 0),true,tickets.size()+1, false);
            for (Ticket t : tickets) {
                allStake += t.stake;
                if (t.winning)
                    allPrize += (int) (t.odds * t.stake);

                series.appendData((new DataPoint(index++, allPrize - allStake)), false, tickets.size()+1, false);
            }


            graph.removeAllSeries();
            graph.addSeries(series);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(tickets.size());
            graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            graph.getGridLabelRenderer().setNumHorizontalLabels(tickets.size());

        }

        allStakeText.setText(String.format(getActivity().getResources().getString(R.string.balanceAllStake), allStake));
        allPrizeText.setText(String.format(getActivity().getResources().getString(R.string.balanceAllPrize), allPrize));
        balance.setText(String.format(getActivity().getResources().getString(R.string.balanceAllProfit), allPrize - allStake));
    }

    @Override
    public void refreshData() {
        loadItemsInBackground();
    }
}
