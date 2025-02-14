package com.example.hungerhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PointsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_points);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView signUpTextView2 = findViewById(R.id.home_icon);
        signUpTextView2.setOnClickListener(view -> {
            startActivity(new Intent(PointsActivity.this, MainActivity.class));
        });

        ImageView signUpTextView3 = findViewById(R.id.location_icon);
        signUpTextView3.setOnClickListener(view -> {
            startActivity(new Intent(PointsActivity.this, FoodAvailability.class));
        });

        ImageView signUpTextView4 = findViewById(R.id.center_action);
        signUpTextView4.setOnClickListener(view -> {
            startActivity(new Intent(PointsActivity.this, AdddonationActivity2.class));
        });

        ImageView signUpTextView5 = findViewById(R.id.favorites_icon);
        signUpTextView5.setOnClickListener(view -> {
            startActivity(new Intent(PointsActivity.this, AdddonatioActivity1.class));
        });
        ImageView signUpTextView6 = findViewById(R.id.profile_icon);
        signUpTextView6.setOnClickListener(view -> {
            startActivity(new Intent(PointsActivity.this, ProfileActivity.class));
        });

        ImageView signUpTextView7 = findViewById(R.id.back);
        signUpTextView7.setOnClickListener(view -> {
            startActivity(new Intent(PointsActivity.this, MainActivity.class));
        });

    }
}