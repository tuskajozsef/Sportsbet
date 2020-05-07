package com.sportsbet.network;

import com.sportsbet.model.Answer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OddsApi {
    @GET("/v3/odds/")
    Call<Answer> getOdds(
            @Query("sport") String sportName,
            @Query("region") String region,
            @Query("mkt") String format,
            @Query("apiKey") String apiKey
    );
}
