package com.sportsbet.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportsbet.R;
import com.sportsbet.data.Ticket;

import java.util.ArrayList;
import java.util.List;

public class BettingTicketListAdapter
        extends RecyclerView.Adapter<BettingTicketListAdapter.BettingViewHolder> {

    private final List<Ticket> items = new ArrayList<>();
    private BettingTicketAdapter childAdapter;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private TicketCheckboxChangedListener listener;

    public BettingTicketListAdapter(TicketCheckboxChangedListener _listener) {
        this.listener = _listener;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public BettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_ticket_list, parent, false);
        return new BettingViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final BettingViewHolder holder, int position) {
        holder.ticket = items.get(position);
        Ticket actual = items.get(position);

        childAdapter = new BettingTicketAdapter();
        holder.childRecyclerView.setAdapter(childAdapter);
        holder.childRecyclerView.setRecycledViewPool(recycledViewPool);
        childAdapter.update(actual.getItems());

        holder.savedStake.setText(String.format(holder.itemView.getContext().getResources().getString(R.string.adapterStake), actual.stake));
        holder.savedPrize.setText(String.format(holder.itemView.getContext().getResources().getString(R.string.holderPrize), (int) (actual.stake * actual.odds)));

        holder.savedWinnerCheckbox.setChecked(actual.winning);

        if (actual.winning)
            holder.savedWinnerCheckbox.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorLightGreen));
        else
            holder.savedWinnerCheckbox.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorLightRed));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Ticket ticket) {
        items.add(ticket);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<Ticket> _items) {
        items.clear();
        items.addAll(_items);
        notifyDataSetChanged();
    }


    public interface TicketCheckboxChangedListener {
        public void onTicketCheckboxChanged(Ticket t);
    }


    class BettingViewHolder extends RecyclerView.ViewHolder {
        Ticket ticket;
        private RecyclerView childRecyclerView;
        private TextView savedStake;
        private TextView savedPrize;
        private CheckBox savedWinnerCheckbox;

        BettingViewHolder(View itemView, final Context context) {
            super(itemView);

            childRecyclerView = itemView.findViewById(R.id.SavedTicketChildRecyclerView);
            childRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            savedPrize = itemView.findViewById(R.id.SavedTicketPrize);
            savedStake = itemView.findViewById(R.id.SavedTicketStake);
            savedWinnerCheckbox = itemView.findViewById(R.id.SavedTicketWinCheckbox);

            savedWinnerCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    savedWinnerCheckbox.setChecked(isChecked);
                    ticket.winning = isChecked;
                    if (isChecked)
                        savedWinnerCheckbox.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
                    else
                        savedWinnerCheckbox.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));

                    if (listener != null)
                        listener.onTicketCheckboxChanged(ticket);
                }
            });
        }
    }

}