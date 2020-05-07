package com.sportsbet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportsbet.R;
import com.sportsbet.data.BettingItem;
import com.sportsbet.fragments.EventFragment;

import java.util.ArrayList;
import java.util.List;

public class BettingEventAdapter
        extends RecyclerView.Adapter<BettingEventAdapter.BettingViewHolder> {

    private final List<BettingItem> items = new ArrayList<>();

    private EventFragment.OnEventSelectedListener listener;

    public BettingEventAdapter(EventFragment.OnEventSelectedListener callback) {
        listener=callback;
    }

    @NonNull
    @Override
    public BettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_event_list, parent, false);
        return new BettingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BettingViewHolder holder, int position) {
        BettingItem item = items.get(position);
        holder.homeTextView.setText(item.homeTeam);
        holder.awayTextView.setText(item.awayTeam);
        holder.dateTextView.setText(item.date);
        holder.leagueTextView.setText(item.league);
        holder.sportsTextView.setText(item.sport);
        holder.homeOddsTextView.setText(Double.toString(item.homeOdds));
        holder.drawOddsTextView.setText(Double.toString(item.drawOdds));
        holder.awayOddsTextView.setText(Double.toString(item.awayOdds));

        switch (item.outcome) {
            case HOME:
                holder.homeCheckBox.setChecked(true);
                holder.drawCheckBox.setChecked(false);
                holder.awayCheckBox.setChecked(false);
                break;

            case DRAW:
                holder.drawCheckBox.setChecked(true);
                holder.homeCheckBox.setChecked(false);
                holder.awayCheckBox.setChecked(false);
                break;

            case AWAY:
                holder.awayCheckBox.setChecked(true);
                holder.homeCheckBox.setChecked(false);
                holder.drawCheckBox.setChecked(false);
                break;

            default:
                holder.homeCheckBox.setChecked(false);
                holder.drawCheckBox.setChecked(false);
                holder.awayCheckBox.setChecked(false);
                break;
        }

        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BettingItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<BettingItem> bettingItems) {
        items.clear();
        items.addAll(bettingItems);
        notifyDataSetChanged();
    }

    class BettingViewHolder extends RecyclerView.ViewHolder {

        TextView sportsTextView;
        TextView homeTextView;
        TextView awayTextView;
        TextView dateTextView;
        TextView leagueTextView;
        TextView homeOddsTextView;
        TextView drawOddsTextView;
        TextView awayOddsTextView;
        CheckBox homeCheckBox;
        CheckBox drawCheckBox;
        CheckBox awayCheckBox;

        BettingItem item;

        BettingViewHolder(View itemView) {
            super(itemView);
            sportsTextView = itemView.findViewById(R.id.BettingItemSports);
            homeTextView = itemView.findViewById(R.id.BettingItemHomeTeam);
            awayTextView = itemView.findViewById(R.id.BettingItemAwayTeam);
            dateTextView = itemView.findViewById(R.id.BettingItemDate);
            homeOddsTextView = itemView.findViewById(R.id.BettingItemHomeOdds);
            drawOddsTextView = itemView.findViewById(R.id.BettingItemDrawOdds);
            awayOddsTextView = itemView.findViewById(R.id.BettingItemAwayOdds);
            leagueTextView = itemView.findViewById(R.id.BettingItemLeague);
            sportsTextView = itemView.findViewById(R.id.BettingItemSports);
            homeCheckBox = itemView.findViewById(R.id.BettingItemHomeCheckbox);
            drawCheckBox = itemView.findViewById(R.id.BettingItemDrawCheckbox);
            awayCheckBox = itemView.findViewById(R.id.BettingItemAwayCheckbox);

            homeCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if (item != null && buttonView.isPressed()) {
                        homeCheckBox.setChecked(isChecked);
                        if (isChecked) {
                            item.outcome = BettingItem.Outcome.valueOf("HOME");
                            drawCheckBox.setChecked(false);
                            awayCheckBox.setChecked(false);
                        } else {
                            if (item.outcome == BettingItem.Outcome.valueOf("HOME"))
                                item.outcome = BettingItem.Outcome.valueOf("NONE");
                        }

                        if(listener!=null)
                        listener.onEventSelected(item);
                    }
                }
            });

            drawCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if (item != null && buttonView.isPressed()) {
                        drawCheckBox.setChecked(isChecked);

                        if (isChecked) {
                            item.outcome = BettingItem.Outcome.valueOf("DRAW");
                            homeCheckBox.setChecked(false);
                            awayCheckBox.setChecked(false);
                        } else {
                            if (item.outcome == BettingItem.Outcome.valueOf("DRAW"))
                                item.outcome = BettingItem.Outcome.valueOf("NONE");
                        }
                        if(listener!=null)
                        listener.onEventSelected(item);
                    }
                }
            });

            awayCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if (item != null && buttonView.isPressed()) {
                        awayCheckBox.setChecked(isChecked);
                        if (isChecked) {
                            item.outcome = BettingItem.Outcome.valueOf("AWAY");
                            drawCheckBox.setChecked(false);
                            homeCheckBox.setChecked(false);
                        } else {
                            if (item.outcome == BettingItem.Outcome.valueOf("AWAY"))
                                item.outcome = BettingItem.Outcome.valueOf("NONE");
                        }

                        if(listener!=null)
                        listener.onEventSelected(item);
                    }
                }
            });
        }
    }

}