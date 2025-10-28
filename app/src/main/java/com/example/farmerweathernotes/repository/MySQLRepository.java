package com.example.farmerweathernotes.repository;

import com.example.farmerweathernotes.api.FarmerApiService;
import com.example.farmerweathernotes.api.RetrofitClient;
import com.example.farmerweathernotes.model.ApiResponse;
import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.DailyNoteRequest;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.model.LocationRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySQLRepository {
    private FarmerApiService apiService;

    public MySQLRepository() {
        apiService = RetrofitClient.getApiService();
    }

    // Location operations
    public void getAllLocations(RepositoryCallback<List<LocationProfile>> callback) {
        apiService.getAllLocations().enqueue(new Callback<ApiResponse<List<LocationProfile>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LocationProfile>>> call, Response<ApiResponse<List<LocationProfile>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(new Exception("Failed to fetch locations"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LocationProfile>>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    public void createLocation(LocationRequest location, RepositoryCallback<LocationProfile> callback) {
        apiService.createLocation(location).enqueue(new Callback<ApiResponse<LocationProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<LocationProfile>> call, Response<ApiResponse<LocationProfile>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(new Exception("Failed to create location"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LocationProfile>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    // Daily Note operations
    public void createNote(DailyNoteRequest note, RepositoryCallback<DailyNote> callback) {
        apiService.createNote(note).enqueue(new Callback<ApiResponse<DailyNote>>() {
            @Override
            public void onResponse(Call<ApiResponse<DailyNote>> call, Response<ApiResponse<DailyNote>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(new Exception("Failed to create note"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DailyNote>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    public void getNotesByLocation(int locationId, RepositoryCallback<List<DailyNote>> callback) {
        apiService.getNotesByLocation(locationId).enqueue(new Callback<ApiResponse<List<DailyNote>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DailyNote>>> call, Response<ApiResponse<List<DailyNote>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(new Exception("Failed to fetch notes"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DailyNote>>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}