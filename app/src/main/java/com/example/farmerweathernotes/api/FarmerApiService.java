package com.example.farmerweathernotes.api;

import com.example.farmerweathernotes.model.ApiResponse;
import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.DailyNoteRequest;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.model.LocationRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FarmerApiService {

    // Location endpoints
    @GET("locations")
    Call<ApiResponse<List<LocationProfile>>> getAllLocations();

    @POST("locations")
    Call<ApiResponse<LocationProfile>> createLocation(@Body LocationRequest location);

    // Daily Notes endpoints
    @GET("notes")
    Call<ApiResponse<List<DailyNote>>> getAllNotes();

    @GET("notes/location/{locationId}")
    Call<ApiResponse<List<DailyNote>>> getNotesByLocation(@Path("locationId") int locationId);

    @POST("notes")
    Call<ApiResponse<DailyNote>> createNote(@Body DailyNoteRequest note);

    @GET("notes/date")
    Call<ApiResponse<List<DailyNote>>> getNotesByDateRange(
            @Query("locationId") int locationId,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );
}