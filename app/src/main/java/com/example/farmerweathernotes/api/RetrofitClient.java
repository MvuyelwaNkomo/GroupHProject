package com.example.farmerweathernotes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://your-server-ip:3000/api/"; // Change to your server IP
    private static Retrofit retrofit = null;

    public static FarmerApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(FarmerApiService.class);
    }
}