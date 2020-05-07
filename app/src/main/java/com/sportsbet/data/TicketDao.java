package com.sportsbet.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TicketDao {
    @Query("SELECT * FROM tickets")
    List<Ticket> getAll();

    @Insert
    long insert(Ticket ticket);

    @Update
    void update(Ticket ticket);

    @Delete
    void deleteItem(Ticket ticket);
}