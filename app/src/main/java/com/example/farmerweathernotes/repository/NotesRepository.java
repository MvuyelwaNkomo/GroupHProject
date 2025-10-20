package com.example.farmerweathernotes.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.farmerweathernotes.database.AppDatabase;
import com.example.farmerweathernotes.dao.DailyNoteDao;
import com.example.farmerweathernotes.dao.LocationProfileDao;
import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.LocationProfile;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepository {
    private LocationProfileDao locationProfileDao;
    private DailyNoteDao dailyNoteDao;
    private ExecutorService executorService;

    public NotesRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        locationProfileDao = database.locationProfileDao();
        dailyNoteDao = database.dailyNoteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Location operations
    public void insertLocation(LocationProfile location, RepositoryCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = locationProfileDao.insert(location);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getAllLocations(RepositoryCallback<List<LocationProfile>> callback) {
        executorService.execute(() -> {
            try {
                List<LocationProfile> locations = locationProfileDao.getAll();
                callback.onSuccess(locations);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // DailyNote operations
    public void insertNote(DailyNote note, RepositoryCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = dailyNoteDao.insert(note);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getNoteByLocationAndDate(int locationId, Date date, RepositoryCallback<DailyNote> callback) {
        executorService.execute(() -> {
            try {
                DailyNote note = dailyNoteDao.getByLocationAndDate(locationId, date);
                callback.onSuccess(note);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}