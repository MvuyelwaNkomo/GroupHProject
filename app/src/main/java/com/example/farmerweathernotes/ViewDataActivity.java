package com.example.farmerweathernotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.farmerweathernotes.model.DailyNote;
import com.example.farmerweathernotes.model.LocationProfile;
import com.example.farmerweathernotes.repository.NotesRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ViewDataActivity extends AppCompatActivity {
    private NotesRepository repository;
    private LinearLayout dataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        repository = new NotesRepository(getApplication());
        dataContainer = findViewById(R.id.dataContainer);

        loadAllData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh data if note was edited or deleted
            loadAllData();
        }
    }

    private void loadAllData() {
        repository.getAllLocations(new NotesRepository.RepositoryCallback<List<LocationProfile>>() {
            @Override
            public void onSuccess(List<LocationProfile> locations) {
                runOnUiThread(() -> displayLocationsWithNotes(locations));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        showError("Failed to load data: " + e.getMessage()));
            }
        });
    }

    private void displayLocationsWithNotes(List<LocationProfile> locations) {
        dataContainer.removeAllViews();

        if (locations.isEmpty()) {
            showMessage("No locations found. Please add locations first.");
            return;
        }

        for (LocationProfile location : locations) {
            createLocationCard(location);
            loadLocationNotes(location);
        }
    }

    private void createLocationCard(LocationProfile location) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 8, 16, 16);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(8);
        card.setRadius(16);
        card.setPadding(24, 24, 24, 24);

        TextView locationText = new TextView(this);
        locationText.setText("📍 " + location.getName());
        locationText.setTextSize(20);
        locationText.setTextColor(Color.parseColor("#2E7D32"));
        locationText.setTypeface(null, Typeface.BOLD);
        locationText.setPadding(0, 0, 0, 16);

        card.addView(locationText);
        dataContainer.addView(card);
    }

    private void loadLocationNotes(LocationProfile location) {
        repository.getNotesByLocation(location.getId(), new NotesRepository.RepositoryCallback<List<DailyNote>>() {
            @Override
            public void onSuccess(List<DailyNote> notes) {
                runOnUiThread(() -> displayNotesForLocation(location, notes));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> showNoteError(location, e.getMessage()));
            }
        });
    }

    private void displayNotesForLocation(LocationProfile location, List<DailyNote> notes) {
        if (notes.isEmpty()) {
            showNoNotesMessage(location);
            return;
        }

        // Add a subtitle for notes section
        CardView subtitleCard = new CardView(this);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        subtitleParams.setMargins(32, 0, 32, 8);
        subtitleCard.setLayoutParams(subtitleParams);
        subtitleCard.setCardBackgroundColor(Color.parseColor("#E8F5E8"));
        subtitleCard.setCardElevation(2);
        subtitleCard.setRadius(8);
        subtitleCard.setPadding(16, 12, 16, 12);

        TextView subtitleText = new TextView(this);
        subtitleText.setText("📋 " + notes.size() + " note(s) for " + location.getName());
        subtitleText.setTextSize(14);
        subtitleText.setTextColor(Color.parseColor("#2E7D32"));
        subtitleText.setTypeface(null, Typeface.BOLD);

        subtitleCard.addView(subtitleText);
        dataContainer.addView(subtitleCard);

        // Display each note
        for (DailyNote note : notes) {
            createNoteCard(note, location);
        }

        // Add a small separator
        CardView separatorCard = new CardView(this);
        LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                4
        );
        separatorParams.setMargins(16, 16, 16, 24);
        separatorCard.setLayoutParams(separatorParams);
        separatorCard.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
        separatorCard.setCardElevation(0);
        separatorCard.setRadius(2);
        dataContainer.addView(separatorCard);
    }

    private void createNoteCard(DailyNote note, LocationProfile location) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(32, 4, 32, 12);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(6);
        card.setRadius(12);
        card.setPadding(20, 20, 20, 20);

        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String dateStr = sdf.format(note.getDate());

        // Date section
        TextView dateText = new TextView(this);
        dateText.setText("📅 " + dateStr);
        dateText.setTextSize(16);
        dateText.setTextColor(Color.parseColor("#1976D2"));
        dateText.setTypeface(null, Typeface.BOLD);
        dateText.setPadding(0, 0, 0, 12);
        cardLayout.addView(dateText);

        // Weather observation
        if (note.getRainObs() != null && !note.getRainObs().isEmpty()) {
            TextView rainText = new TextView(this);
            rainText.setText("🌧️ Weather: " + note.getRainObs());
            rainText.setTextSize(14);
            rainText.setTextColor(Color.parseColor("#388E3C"));
            rainText.setPadding(0, 0, 0, 6);
            cardLayout.addView(rainText);
        }

        // Activity
        if (note.getActivity() != null && !note.getActivity().isEmpty()) {
            TextView activityText = new TextView(this);
            activityText.setText("👨‍🌾 Activity: " + note.getActivity());
            activityText.setTextSize(14);
            activityText.setTextColor(Color.parseColor("#F57C00"));
            activityText.setPadding(0, 0, 0, 6);
            cardLayout.addView(activityText);
        }

        // Comments
        if (note.getComments() != null && !note.getComments().isEmpty()) {
            TextView commentsText = new TextView(this);
            commentsText.setText("💬 Notes: " + note.getComments());
            commentsText.setTextSize(14);
            commentsText.setTextColor(Color.parseColor("#7B1FA2"));
            commentsText.setLineSpacing(1.2f, 1.2f);
            commentsText.setPadding(0, 8, 0, 0);
            cardLayout.addView(commentsText);
        }

        // If no specific data, show a message
        if ((note.getRainObs() == null || note.getRainObs().isEmpty()) &&
                (note.getActivity() == null || note.getActivity().isEmpty()) &&
                (note.getComments() == null || note.getComments().isEmpty())) {
            TextView emptyText = new TextView(this);
            emptyText.setText("📝 No specific observations recorded for this day");
            emptyText.setTextSize(14);
            emptyText.setTextColor(Color.parseColor("#757575"));
            emptyText.setPadding(0, 8, 0, 0);
            cardLayout.addView(emptyText);
        }

        // Add edit/delete buttons
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setPadding(0, 16, 0, 0);

        Button editBtn = new Button(this);
        editBtn.setText("Edit");
        editBtn.setBackgroundColor(Color.parseColor("#2196F3"));
        editBtn.setTextColor(Color.WHITE);
        editBtn.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        editBtn.setOnClickListener(v -> editNote(note));

        Button deleteBtn = new Button(this);
        deleteBtn.setText("Delete");
        deleteBtn.setBackgroundColor(Color.parseColor("#F44336"));
        deleteBtn.setTextColor(Color.WHITE);
        deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        deleteBtn.setOnClickListener(v -> deleteNote(note, location));

        buttonLayout.addView(editBtn);
        buttonLayout.addView(deleteBtn);
        cardLayout.addView(buttonLayout);

        card.addView(cardLayout);
        dataContainer.addView(card);
    }

    private void editNote(DailyNote note) {
        Intent intent = new Intent(ViewDataActivity.this, EditNoteActivity.class);
        intent.putExtra("NOTE_ID", note.getId());
        startActivityForResult(intent, 1);
    }

    private void deleteNote(DailyNote note, LocationProfile location) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteNote(note, new NotesRepository.RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            runOnUiThread(() -> {
                                Toast.makeText(ViewDataActivity.this, "Note deleted successfully!", Toast.LENGTH_SHORT).show();
                                // Refresh the data
                                loadAllData();
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() ->
                                    Toast.makeText(ViewDataActivity.this, "Error deleting note", Toast.LENGTH_SHORT).show());
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showNoNotesMessage(LocationProfile location) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(32, 4, 32, 24);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.parseColor("#FFF3E0"));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setPadding(20, 20, 20, 20);

        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleText = new TextView(this);
        titleText.setText("📭 No Notes Yet");
        titleText.setTextSize(16);
        titleText.setTextColor(Color.parseColor("#E65100"));
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setPadding(0, 0, 0, 8);

        TextView detailText = new TextView(this);
        detailText.setText("No daily notes found for " + location.getName() + ". \n\nAdd weather observations and farming activities in the Daily Notes section!");
        detailText.setTextSize(14);
        detailText.setTextColor(Color.parseColor("#E65100"));
        detailText.setLineSpacing(1.3f, 1.3f);

        messageLayout.addView(titleText);
        messageLayout.addView(detailText);
        card.addView(messageLayout);
        dataContainer.addView(card);
    }

    private void showNoteError(LocationProfile location, String error) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(32, 4, 32, 8);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setPadding(20, 20, 20, 20);

        TextView errorText = new TextView(this);
        errorText.setText("❌ Error loading notes for " + location.getName() + ":\n" + error);
        errorText.setTextSize(14);
        errorText.setTextColor(Color.RED);
        errorText.setLineSpacing(1.2f, 1.2f);
        errorText.setPadding(0, 8, 0, 8);

        card.addView(errorText);
        dataContainer.addView(card);
    }

    private void showMessage(String message) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setPadding(24, 24, 24, 24);

        TextView messageText = new TextView(this);
        messageText.setText(message);
        messageText.setTextSize(16);
        messageText.setTextColor(Color.parseColor("#1565C0"));
        messageText.setGravity(android.view.Gravity.CENTER);
        messageText.setLineSpacing(1.3f, 1.3f);

        card.addView(messageText);
        dataContainer.addView(card);
    }

    private void showError(String message) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setPadding(24, 24, 24, 24);

        TextView errorText = new TextView(this);
        errorText.setText("❌ " + message);
        errorText.setTextSize(16);
        errorText.setTextColor(Color.RED);
        errorText.setGravity(android.view.Gravity.CENTER);
        errorText.setLineSpacing(1.3f, 1.3f);

        card.addView(errorText);
        dataContainer.addView(card);
    }
}