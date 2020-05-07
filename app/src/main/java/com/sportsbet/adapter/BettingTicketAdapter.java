package com.sportsbet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportsbet.R;
import com.sportsbet.data.BettingItem;
import com.sportsbet.data.Ticket;

import java.util.ArrayList;
import java.util.List;

public class BettingTicketAdapter
        extends RecyclerView.Adapter<BettingTicketAdapter.BettingViewHolder> {

    private final List<BettingItem> items;

    private TicketSaveClickListener listener;

    public BettingTicketAdapter() {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public BettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new BettingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BettingViewHolder holder, int position) {
        BettingItem item = items.get(position);
        holder.homeTextView.setText(item.homeTeam);
        holder.awayTextView.setText(item.awayTeam);
        holder.dateTextView.setText(item.date);

        switch (item.outcome) {
            case HOME:
                holder.oddsTextView.setText(Double.toString(item.homeOdds));
                holder.betTextView.setText(item.homeTeam);
                break;

            case DRAW:
                holder.oddsTextView.setText(Double.toString(item.drawOdds));
                holder.betTextView.setText(holder.itemView.getContext().getResources().getString(R.string.ticketDraw));
                break;

            case AWAY:
                holder.oddsTextView.setText(Double.toString(item.awayOdds));
                holder.betTextView.setText(item.awayTeam);
                break;

            default:
                holder.oddsTextView.setText(holder.itemView.getContext().getResources().getString(R.string.noBet));
                holder.betTextView.setText(holder.itemView.getContext().getResources().getString(R.string.noBet));
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

    public List<BettingItem> getItems(){
        return items;
    }

    class BettingViewHolder extends RecyclerView.ViewHolder {

        TextView homeTextView;
        TextView awayTextView;
        TextView dateTextView;
        TextView oddsTextView;
        TextView betTextView;

        BettingItem item;

        BettingViewHolder(View itemView) {
            super(itemView);

            homeTextView = itemView.findViewById(R.id.TicketItemHomeTeam);
            awayTextView = itemView.findViewById(R.id.TicketItemAwayTeam);
            dateTextView = itemView.findViewById(R.id.TicketItemDate);
            oddsTextView = itemView.findViewById(R.id.TicketBetOdds);
            betTextView = itemView.findViewById(R.id.TicketBetTeam);

        }
    }

    public interface TicketSaveClickListener{
        public void onTicketSaved(Ticket ticket);
    }
}
