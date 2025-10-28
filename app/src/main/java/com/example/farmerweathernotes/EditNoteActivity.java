package com.example.farmerweathernotes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.repository.NotesRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {
    private NotesRepository repository;
    private Spinner locationSpinner;
    private EditText dateInput;
    private RadioGroup rainObsGroup;
    private RadioGroup activityGroup;
    private EditText commentsInput;
    private List<LocationProfile> locations = new ArrayList<>();
    private DailyNote currentNote;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        repository = new NotesRepository(getApplication());
        calendar = Calendar.getInstance();

        initializeUI();
        loadLocations();
        loadNoteData();
    }

    private void initializeUI() {
        locationSpinner = findViewById(R.id.locationSpinner);
        dateInput = findViewById(R.id.dateInput);
        rainObsGroup = findViewById(R.id.rainObsGroup);
        activityGroup = findViewById(R.id.activityGroup);
        commentsInput = findViewById(R.id.commentsInput);

        Button updateBtn = findViewById(R.id.updateBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);

        // Set up date picker
        dateInput.setOnClickListener(v -> showDatePicker());

        updateBtn.setOnClickListener(v -> updateNote());
        deleteBtn.setOnClickListener(v -> deleteNote());
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    updateDateInput();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void updateDateInput() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateInput.setText(sdf.format(calendar.getTime()));
    }

    private void loadLocations() {
        repository.getAllLocations(new NotesRepository.RepositoryCallback<List<LocationProfile>>() {
            @Override
            public void onSuccess(List<LocationProfile> locationList) {
                runOnUiThread(() -> {
                    locations = locationList;
                    ArrayAdapter<LocationProfile> adapter = new ArrayAdapter<>(
                            EditNoteActivity.this,
                            android.R.layout.simple_spinner_item,
                            locations
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(EditNoteActivity.this, "Error loading locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNoteData() {
        int noteId = getIntent().getIntExtra("NOTE_ID", -1);
        if (noteId == -1) {
            Toast.makeText(this, "Error: Note ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repository.getNoteById(noteId, new NotesRepository.RepositoryCallback<DailyNote>() {
            @Override
            public void onSuccess(DailyNote note) {
                runOnUiThread(() -> {
                    if (note != null) {
                        currentNote = note;
                        populateNoteData(note);
                    } else {
                        Toast.makeText(EditNoteActivity.this, "Note not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditNoteActivity.this, "Error loading note", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void populateNoteData(DailyNote note) {
        // Set location
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getId() == note.getLocationId()) {
                locationSpinner.setSelection(i);
                break;
            }
        }

        // Set date
        calendar.setTime(note.getDate());
        updateDateInput();

        // Set rain observation
        if (note.getRainObs() != null) {
            switch (note.getRainObs()) {
                case "No Rain":
                    rainObsGroup.check(R.id.rainNo);
                    break;
                case "Light Rain":
                    rainObsGroup.check(R.id.rainLight);
                    break;
                case "Heavy Rain":
                    rainObsGroup.check(R.id.rainHeavy);
                    break;
            }
        }

        // Set activity
        if (note.getActivity() != null) {
            switch (note.getActivity()) {
                case "Planting":
                    activityGroup.check(R.id.activityPlanting);
                    break;
                case "Harvesting":
                    activityGroup.check(R.id.activityHarvesting);
                    break;
                case "Weeding":
                    activityGroup.check(R.id.activityWeeding);
                    break;
                case "Irrigation":
                    activityGroup.check(R.id.activityIrrigation);
                    break;
            }
        }

        // Set comments
        commentsInput.setText(note.getComments());
    }

    private void updateNote() {
        if (currentNote == null) return;

        LocationProfile selectedLocation = (LocationProfile) locationSpinner.getSelectedItem();
        Date date = calendar.getTime();

        int rainObsId = rainObsGroup.getCheckedRadioButtonId();
        int activityId = activityGroup.getCheckedRadioButtonId();

        if (rainObsId == -1 && activityId == -1) {
            Toast.makeText(this, "Please select rain observation or activity", Toast.LENGTH_SHORT).show();
            return;
        }

        String rainObs = rainObsId != -1 ? ((RadioButton) findViewById(rainObsId)).getText().toString() : "";
        String activity = activityId != -1 ? ((RadioButton) findViewById(activityId)).getText().toString() : "";
        String comments = commentsInput.getText().toString();

        // Update the note
        currentNote.setLocationId(selectedLocation.getId());
        currentNote.setDate(date);
        currentNote.setRainObs(rainObs);
        currentNote.setActivity(activity);
        currentNote.setComments(comments);

        repository.updateNote(currentNote, new NotesRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    Toast.makeText(EditNoteActivity.this, "Note updated successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(EditNoteActivity.this, "Error updating note", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void deleteNote() {
        if (currentNote == null) return;

        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteNote(currentNote, new NotesRepository.RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            runOnUiThread(() -> {
                                Toast.makeText(EditNoteActivity.this, "Note deleted successfully!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() ->
                                    Toast.makeText(EditNoteActivity.this, "Error deleting note", Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}