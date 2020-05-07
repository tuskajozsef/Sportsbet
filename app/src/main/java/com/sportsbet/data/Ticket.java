package com.sportsbet.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "tickets")
public class Ticket {

    public Ticket(int stake, double odds, List<BettingItem> items){
        this.stake=stake;
        this.odds=odds;
        this.items=items;
    }

    public static class DataConverter {

        @TypeConverter
        public static String fromBettingItemList(List<BettingItem> itemList) {
            if (itemList == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<BettingItem>>() {}.getType();
            String json = gson.toJson(itemList, type);
            return json;
        }

        @TypeConverter
        public static List<BettingItem> toBettingItemList(String bettingItemString) {
            if (bettingItemString == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<BettingItem>>() {}.getType();
            List<BettingItem> itemList = gson.fromJson(bettingItemString, type);
            return itemList;
        }
    }

    @TypeConverters(DataConverter.class)
    public List<BettingItem> getItems() {
        return items;
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "stake")
    public int stake=0;

    @ColumnInfo(name = "odds")
    public double odds=0;

    @ColumnInfo(name = "winning")
    public boolean winning=false;

    @ColumnInfo(name = "items")
    public List<BettingItem> items=new ArrayList<>();
}
