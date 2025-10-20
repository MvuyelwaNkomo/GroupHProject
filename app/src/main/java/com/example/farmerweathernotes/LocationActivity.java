package com.example.farmerweathernotes;

import android.graphics.Color;
import java.util.List;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.repository.NotesRepository;

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
                    });
                }

                @Override
                public void onError(Exception e) {
                    // Handle error
                }
            });
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
                // Handle error
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

            TextView locationText = new TextView(this);
            locationText.setText(location.getName());
            locationText.setTextSize(18);
            locationText.setTextColor(Color.BLACK);

            locationCard.addView(locationText);
            locationsContainer.addView(locationCard);
        }
    }
}