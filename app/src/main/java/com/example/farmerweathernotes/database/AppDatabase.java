package com.example.farmerweathernotes.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.dao.DailyNoteDao;
import com.example.farmerweathernotes.dao.LocationProfileDao;

@Database(entities = {LocationProfile.class, DailyNote.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract LocationProfileDao locationProfileDao();
    public abstract DailyNoteDao dailyNoteDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "farmer_notes.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}