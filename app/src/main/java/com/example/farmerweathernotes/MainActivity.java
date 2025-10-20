package com.example.farmerweathernotes;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    private TextView offlineIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        checkConnectivity();
    }

    private void initializeUI() {
        // Set up offline indicator
        offlineIndicator = findViewById(R.id.offlineIndicator);

        // Create main layout
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));

        // Create cards for different sections
        createLocationCard();
        createNotesCard();
        createTipsCard();
    }

    private void createLocationCard() {
        CardView locationCard = findViewById(R.id.locationCard);
        locationCard.setCardBackgroundColor(Color.WHITE);
        locationCard.setCardElevation(8);
        locationCard.setRadius(16);

        Button locationBtn = findViewById(R.id.locationBtn);
        locationBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
        locationBtn.setTextColor(Color.WHITE);
        locationBtn.setTextSize(18);
        locationBtn.setAllCaps(false);

        locationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LocationActivity.class);
            startActivity(intent);
        });
    }

    private void createNotesCard() {
        CardView notesCard = findViewById(R.id.notesCard);
        notesCard.setCardBackgroundColor(Color.WHITE);
        notesCard.setCardElevation(8);
        notesCard.setRadius(16);

        Button notesBtn = findViewById(R.id.notesBtn);
        notesBtn.setBackgroundColor(Color.parseColor("#2196F3"));
        notesBtn.setTextColor(Color.WHITE);
        notesBtn.setTextSize(18);
        notesBtn.setAllCaps(false);

        notesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });
    }

    private void createTipsCard() {
        CardView tipsCard = findViewById(R.id.tipsCard);
        tipsCard.setCardBackgroundColor(Color.WHITE);
        tipsCard.setCardElevation(8);
        tipsCard.setRadius(16);

        Button tipsBtn = findViewById(R.id.tipsBtn);
        tipsBtn.setBackgroundColor(Color.parseColor("#FF9800"));
        tipsBtn.setTextColor(Color.WHITE);
        tipsBtn.setTextSize(18);
        tipsBtn.setAllCaps(false);

        tipsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TipsActivity.class);
            startActivity(intent);
        });
    }

    private void checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            offlineIndicator.setText("Online");
            offlineIndicator.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else {
            offlineIndicator.setText("Offline Mode");
            offlineIndicator.setBackgroundColor(Color.parseColor("#FF9800"));
        }
    }
}