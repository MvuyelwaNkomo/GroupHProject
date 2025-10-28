package com.example.farmerweathernotes.repository;

import android.app.Application;
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

    public void getLocationById(int locationId, RepositoryCallback<LocationProfile> callback) {
        executorService.execute(() -> {
            try {
                LocationProfile location = locationProfileDao.getById(locationId);
                callback.onSuccess(location);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateLocation(LocationProfile location, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                locationProfileDao.update(location);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteLocation(LocationProfile location, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                locationProfileDao.delete(location);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // Delete location and all its notes
    public void deleteLocationWithNotes(LocationProfile location, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                // Delete all notes for this location first
                dailyNoteDao.deleteNotesByLocation(location.getId());
                // Then delete the location
                locationProfileDao.delete(location);
                callback.onSuccess(null);
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

    public void getNoteById(int noteId, RepositoryCallback<DailyNote> callback) {
        executorService.execute(() -> {
            try {
                // We need to search through all locations to find the note
                List<LocationProfile> locations = locationProfileDao.getAll();
                DailyNote foundNote = null;
                for (LocationProfile location : locations) {
                    List<DailyNote> notes = dailyNoteDao.getByLocation(location.getId());
                    for (DailyNote note : notes) {
                        if (note.getId() == noteId) {
                            foundNote = note;
                            break;
                        }
                    }
                    if (foundNote != null) break;
                }
                callback.onSuccess(foundNote);
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

    public void getNotesByLocation(int locationId, RepositoryCallback<List<DailyNote>> callback) {
        executorService.execute(() -> {
            try {
                List<DailyNote> notes = dailyNoteDao.getByLocation(locationId);
                callback.onSuccess(notes);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getAllNotes(RepositoryCallback<List<DailyNote>> callback) {
        executorService.execute(() -> {
            try {
                List<LocationProfile> locations = locationProfileDao.getAll();
                List<DailyNote> allNotes = new java.util.ArrayList<>();
                for (LocationProfile location : locations) {
                    List<DailyNote> notes = dailyNoteDao.getByLocation(location.getId());
                    allNotes.addAll(notes);
                }
                callback.onSuccess(allNotes);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateNote(DailyNote note, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                dailyNoteDao.update(note);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteNote(DailyNote note, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                dailyNoteDao.delete(note);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteNoteById(int noteId, RepositoryCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                // Find and delete the note
                getNoteById(noteId, new RepositoryCallback<DailyNote>() {
                    @Override
                    public void onSuccess(DailyNote note) {
                        if (note != null) {
                            dailyNoteDao.delete(note);
                            callback.onSuccess(null);
                        } else {
                            callback.onError(new Exception("Note not found"));
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
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