package com.example.farmerweathernotes;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        initializeUI();
        loadTips();
    }

    private void initializeUI() {
        LinearLayout tipsContainer = findViewById(R.id.tipsContainer);
        tipsContainer.setBackgroundColor(Color.parseColor("#F5F5F5"));
    }

    private void loadTips() {
        LinearLayout tipsContainer = findViewById(R.id.tipsContainer);

        String[] tips = {
                "Monitor soil moisture regularly and water plants early in the morning",
                "Use organic mulch to conserve soil moisture and suppress weeds",
                "Rotate crops to maintain soil fertility and reduce pest buildup",
                "Observe weather patterns to plan planting and harvesting activities",
                "Keep records of rainfall to optimize irrigation scheduling",
                "Use cover crops to improve soil structure and prevent erosion",
                "Monitor plants for signs of disease and take early action",
                "Practice integrated pest management to reduce chemical use"
        };

        for (String tip : tips) {
            CardView tipCard = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(16, 8, 16, 8);
            tipCard.setLayoutParams(cardParams);
            tipCard.setCardBackgroundColor(Color.WHITE);
            tipCard.setCardElevation(4);
            tipCard.setRadius(12);
            tipCard.setPadding(24, 24, 24, 24);

            TextView tipText = new TextView(this);
            tipText.setText(tip);
            tipText.setTextSize(16);
            tipText.setTextColor(Color.BLACK);
            tipText.setLineSpacing(1.2f, 1.2f);

            tipCard.addView(tipText);
            tipsContainer.addView(tipCard);
        }
    }
}