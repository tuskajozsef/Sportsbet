package com.sportsbet.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BettingData {
    String sport_key;
    public String sport_nice;
    String[] teams;
    public String home_team;
    public int commence_time;
    public List<Site> sites;

    public String getAwayTeam(){
        return teams[0].equals(home_team) ? teams[1] : teams[0];
    }

    public double getHomeOdds(){
        return teams[0].equals(home_team) ? sites.get(0).odds.h2h[0] : sites.get(0).odds.h2h[1];
    }

    public double getDrawOdds(){
        return sites.get(0).odds.h2h[2];
    }

    public double getAwayOdds(){
        return teams[0].equals(home_team) ? sites.get(0).odds.h2h[1] : sites.get(0).odds.h2h[0];
    }

    public String getDate(){
        Date date = new java.util.Date(commence_time*1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));
        return  sdf.format(date);
    }
}
