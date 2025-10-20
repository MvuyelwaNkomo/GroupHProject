package com.example.farmerweathernotes.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.farmerweathernotes.model.LocationProfile;
import java.util.List;

@Dao
public interface LocationProfileDao {
    @Insert
    long insert(LocationProfile location);

    @Update
    void update(LocationProfile location);

    @Delete
    void delete(LocationProfile location);

    @Query("SELECT * FROM location_profile ORDER BY name")
    List<LocationProfile> getAll();

    @Query("SELECT * FROM location_profile WHERE id = :id")
    LocationProfile getById(int id);
}