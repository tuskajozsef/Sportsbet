package com.sportsbet.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(tableName = "bettingitem")
public class BettingItem{

        public BettingItem(String sport, String league, String homeTeam, String awayTeam, String date, double homeOdds, double drawOdds, double awayOdds) {
            this.sport=sport;
            this.league=league;
            this.homeTeam=homeTeam;
            this.awayTeam=awayTeam;
            this.date=date;
            this.homeOdds=homeOdds;
            this.drawOdds=drawOdds;
            this.awayOdds=awayOdds;
        }

    public enum Outcome {
        HOME, DRAW, AWAY, NONE;

        @TypeConverter
        public static Outcome getByOrdinal(int ordinal) {
            Outcome ret = null;
            for (Outcome out : Outcome.values()) {
                if (out.ordinal() == ordinal) {
                    ret = out;
                    break;
                }
            }
            return ret;
        }

        @TypeConverter
        public static int toInt(Outcome outcome) {
            return outcome.ordinal();
        }
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "sport")
    public String sport="Labdarúgás";

    @ColumnInfo(name = "league")
    public String league="La Liga";

    @ColumnInfo(name = "homeTeam")
    public String homeTeam="Real Madrid";

    @ColumnInfo(name = "awayTeam")
    public String awayTeam = "Barcelona";

    @ColumnInfo(name="date")
    public String date="1521020";

    @ColumnInfo(name="homeOdds")
    public double homeOdds=1.20;

    @ColumnInfo(name="drawOdds")
    public double drawOdds=4.20;

    @ColumnInfo(name="awayOdds")
    public double awayOdds=8.00;

    @ColumnInfo(name = "outcome")
    public Outcome outcome=Outcome.valueOf("NONE");

}