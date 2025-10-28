package com.example.farmerweathernotes;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.repository.NotesRepository;

import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private NotesRepository repository;
    private LinearLayout locationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        repository = new NotesRepository(getApplication());
        initializeUI();
        loadLocations();
    }

    private void initializeUI() {
        locationsContainer = findViewById(R.id.locationsContainer);

        Button saveLocationBtn = findViewById(R.id.saveLocationBtn);
        saveLocationBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
        saveLocationBtn.setTextColor(Color.WHITE);

        saveLocationBtn.setOnClickListener(v -> saveLocation());
    }

    private void saveLocation() {
        EditText locationNameInput = findViewById(R.id.locationNameInput);
        String locationName = locationNameInput.getText().toString().trim();

        if (!locationName.isEmpty()) {
            LocationProfile location = new LocationProfile(locationName, 0.0, 0.0);
            repository.insertLocation(location, new NotesRepository.RepositoryCallback<Long>() {
                @Override
                public void onSuccess(Long result) {
                    runOnUiThread(() -> {
                        locationNameInput.setText("");
                        loadLocations();
                        Toast.makeText(LocationActivity.this, "Location saved successfully!", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(LocationActivity.this, "Error saving location", Toast.LENGTH_SHORT).show());
                }
            });
        } else {
            Toast.makeText(this, "Please enter a location name", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLocations() {
        repository.getAllLocations(new NotesRepository.RepositoryCallback<List<LocationProfile>>() {
            @Override
            public void onSuccess(List<LocationProfile> locations) {
                runOnUiThread(() -> displayLocations(locations));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(LocationActivity.this, "Error loading locations", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void displayLocations(List<LocationProfile> locations) {
        locationsContainer.removeAllViews();

        for (LocationProfile location : locations) {
            CardView locationCard = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            locationCard.setLayoutParams(cardParams);
            locationCard.setCardBackgroundColor(Color.WHITE);
            locationCard.setCardElevation(4);
            locationCard.setRadius(12);
            locationCard.setPadding(32, 32, 32, 32);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            TextView locationText = new TextView(this);
            locationText.setText(location.getName());
            locationText.setTextSize(18);
            locationText.setTextColor(Color.BLACK);
            locationText.setPadding(0, 0, 0, 16);

            // Add edit/delete buttons
            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

            Button editBtn = new Button(this);
            editBtn.setText("Edit");
            editBtn.setBackgroundColor(Color.parseColor("#2196F3"));
            editBtn.setTextColor(Color.WHITE);
            editBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            editBtn.setOnClickListener(v -> editLocation(location));

            Button deleteBtn = new Button(this);
            deleteBtn.setText("Delete");
            deleteBtn.setBackgroundColor(Color.parseColor("#F44336"));
            deleteBtn.setTextColor(Color.WHITE);
            deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            deleteBtn.setOnClickListener(v -> deleteLocation(location));

            buttonLayout.addView(editBtn);
            buttonLayout.addView(deleteBtn);

            cardLayout.addView(locationText);
            cardLayout.addView(buttonLayout);
            locationCard.addView(cardLayout);
            locationsContainer.addView(locationCard);
        }
    }

    private void editLocation(LocationProfile location) {
        EditText locationNameInput = findViewById(R.id.locationNameInput);
        locationNameInput.setText(location.getName());

        // Simple edit functionality - user can change name and click Save to update
        Toast.makeText(this, "Editing: " + location.getName() + ". Change the name above and click Save to update.", Toast.LENGTH_LONG).show();
    }

    private void deleteLocation(LocationProfile location) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Location")
                .setMessage("Are you sure you want to delete '" + location.getName() + "'? This will also delete all notes associated with this location.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteLocationWithNotes(location, new NotesRepository.RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            runOnUiThread(() -> {
                                Toast.makeText(LocationActivity.this, "Location and all notes deleted successfully!", Toast.LENGTH_SHORT).show();
                                loadLocations();
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() ->
                                    Toast.makeText(LocationActivity.this, "Error deleting location", Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}