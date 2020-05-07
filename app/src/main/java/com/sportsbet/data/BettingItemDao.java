package com.sportsbet.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BettingItemDao {
    @Query("SELECT * FROM bettingitem")
    List<BettingItem> getAll();

    @Query("SELECT * FROM bettingitem WHERE outcome<>3")
    List<BettingItem> getAllPredicted();

    @Query("DELETE FROM bettingitem")
    void cleanTable();

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    long insert(BettingItem bettingItem);

    @Update
    void update(BettingItem bettingItem);

    @Delete
    void deleteItem(BettingItem bettingItem);
}