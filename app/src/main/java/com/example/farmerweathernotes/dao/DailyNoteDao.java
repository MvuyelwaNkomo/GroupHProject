package com.example.farmerweathernotes.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.farmerweathernotes.model.DailyNote;
import java.util.Date;
import java.util.List;

@Dao
public interface DailyNoteDao {
    @Insert
    long insert(DailyNote note);

    @Update
    void update(DailyNote note);

    @Delete
    void delete(DailyNote note);

    @Query("SELECT * FROM daily_notes WHERE locationId = :locationId AND date = :date")
    DailyNote getByLocationAndDate(int locationId, Date date);

    @Query("SELECT * FROM daily_notes WHERE locationId = :locationId ORDER BY date DESC")
    List<DailyNote> getByLocation(int locationId);

    @Query("SELECT * FROM daily_notes WHERE locationId = :locationId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<DailyNote> getBySeason(int locationId, Date startDate, Date endDate);

    @Query("SELECT * FROM daily_notes WHERE locationId = :locationId ORDER BY date DESC LIMIT :limit")
    List<DailyNote> getLatestNotes(int locationId, int limit);
}