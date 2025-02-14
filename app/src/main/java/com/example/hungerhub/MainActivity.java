package com.example.hungerhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView signUpTextView2 = findViewById(R.id.home_icon);
        signUpTextView2.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        });

        ImageView signUpTextView3 = findViewById(R.id.location_icon);
        signUpTextView3.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FoodAvailability.class));
        });

        ImageView signUpTextView4 = findViewById(R.id.center_action);
        signUpTextView4.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AdddonationActivity2.class));
        });

        ImageView signUpTextView5 = findViewById(R.id.favorites_icon);
        signUpTextView5.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AdddonatioActivity1.class));
        });
        ImageView signUpTextView6 = findViewById(R.id.profile_icon);
        signUpTextView6.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
        int[] Donationsr = {R.id.img1, R.id.img2, R.id.img3, R.id.img4,R.id.img5,R.id.img6};
        for (int id :Donationsr ) {
            ImageView donation = findViewById(id);
            if (donation != null) {
                donation.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, FoodAvailability.class);
                    startActivity(intent);
                });
            }
        }
        int[] Donations = {R.id.li1, R.id.li2, R.id.li3, R.id.li4, R.id.li5, R.id.li6, R.id.li7};
        for (int id :Donations ) {
            LinearLayout donation = findViewById(id);
            if (donation != null) {
                donation.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, AdddonatioActivity1.class);
                    startActivity(intent);
                });
            }
        }
        int[] points = {R.id.bu1, R.id.bu2, R.id.bu3, R.id.bu4};
        for (int id :points ) {
            Button donation = findViewById(id);
            if (donation != null) {
                donation.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, PointsActivity.class);
                    startActivity(intent);
                });
            }
        }
    }
}