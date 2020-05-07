package com.sportsbet.network;

import com.sportsbet.model.Answer;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static final String SERVICE_URL = "https://api.the-odds-api.com";
    private static final String APP_ID = "aea62fe20d5851748fa97928df8d9db9";

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    private Retrofit retrofit;
    private OddsApi oddsApi;

    private NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SERVICE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        oddsApi = retrofit.create(OddsApi.class);
    }

    public Call<Answer> getOdds(String sport) {
        return oddsApi.getOdds(sport,"uk", "h2h", APP_ID);
    }
}