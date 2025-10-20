package com.example.farmerweathernotes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.repository.NotesRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private NotesRepository repository;
    private Spinner locationSpinner;
    private DatePicker datePicker;
    private RadioGroup rainObsGroup;
    private RadioGroup activityGroup;
    private EditText commentsInput;
    private List<LocationProfile> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        repository = new NotesRepository(getApplication());
        initializeUI();
        loadLocations();
    }

    private void initializeUI() {
        locationSpinner = findViewById(R.id.locationSpinner);
        datePicker = findViewById(R.id.datePicker);
        rainObsGroup = findViewById(R.id.rainObsGroup);
        activityGroup = findViewById(R.id.activityGroup);
        commentsInput = findViewById(R.id.commentsInput);

        Button saveNoteBtn = findViewById(R.id.saveNoteBtn);
        saveNoteBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
        saveNoteBtn.setTextColor(Color.WHITE);

        Button shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setBackgroundColor(Color.parseColor("#2196F3"));
        shareBtn.setTextColor(Color.WHITE);

        saveNoteBtn.setOnClickListener(v -> saveNote());
        shareBtn.setOnClickListener(v -> shareNotes());
    }

    private void loadLocations() {
        repository.getAllLocations(new NotesRepository.RepositoryCallback<List<LocationProfile>>() {
            @Override
            public void onSuccess(List<LocationProfile> locationList) {
                runOnUiThread(() -> {
                    locations = locationList;
                    ArrayAdapter<LocationProfile> adapter = new ArrayAdapter<>(
                            NotesActivity.this,
                            android.R.layout.simple_spinner_item,
                            locations
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
    }

    private void saveNote() {
        if (locations.isEmpty()) {
            Toast.makeText(this, "Please create a location first", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationProfile selectedLocation = (LocationProfile) locationSpinner.getSelectedItem();
        Date date = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());

        int rainObsId = rainObsGroup.getCheckedRadioButtonId();
        int activityId = activityGroup.getCheckedRadioButtonId();

        if (rainObsId == -1 && activityId == -1) {
            Toast.makeText(this, "Please select rain observation or activity", Toast.LENGTH_SHORT).show();
            return;
        }

        String rainObs = rainObsId != -1 ? ((RadioButton) findViewById(rainObsId)).getText().toString() : "";
        String activity = activityId != -1 ? ((RadioButton) findViewById(activityId)).getText().toString() : "";
        String comments = commentsInput.getText().toString();

        DailyNote note = new DailyNote(selectedLocation.getId(), date, rainObs, activity, comments);
        repository.insertNote(note, new NotesRepository.RepositoryCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                runOnUiThread(() -> {
                    Toast.makeText(NotesActivity.this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(NotesActivity.this, "Error saving note", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void clearForm() {
        rainObsGroup.clearCheck();
        activityGroup.clearCheck();
        commentsInput.setText("");
    }

    private void shareNotes() {
        // Implementation for sharing notes as text
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Farm Weather Notes");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Export functionality to be implemented");
        startActivity(Intent.createChooser(shareIntent, "Share Notes"));
    }
}