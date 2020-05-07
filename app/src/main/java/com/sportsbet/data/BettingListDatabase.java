package com.sportsbet.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {BettingItem.class, Ticket.class},
        version = 1
)

@TypeConverters(value = {BettingItem.Outcome.class, Ticket.DataConverter.class})
public abstract class BettingListDatabase extends RoomDatabase {

    private static BettingListDatabase INSTANCE;

    public static BettingListDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BettingListDatabase.class,
                    "betting-list"
            ).build();
        }

        return INSTANCE;
    }

    public abstract BettingItemDao bettingItemDao();
    public abstract TicketDao ticketDao();
}
