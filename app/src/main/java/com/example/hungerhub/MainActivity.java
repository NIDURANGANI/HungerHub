package com.example.hungerhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText, welcomeEmail;
    private ImageView logoutImageView;
    private ImageView bannerImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bannerImage = findViewById(R.id.banner);
        progressBar = findViewById(R.id.image_progress_bar);
        loadImageFromFirebase("images/donation/food1.png", bannerImage, progressBar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Adjust padding for system bars (navigation and status bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        welcomeText = findViewById(R.id.welcome_text);
        welcomeEmail = findViewById(R.id.welcome_email);
        logoutImageView = findViewById(R.id.logout);

        // Check if user is logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Display the username and email
            welcomeText.setText("Hi " + user.getDisplayName()); // Set display name or fallback to email if not set
            welcomeEmail.setText(user.getEmail()); // Set email address
        } else {
            // If user is not logged in, redirect to login screen
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        // Set up logout functionality
        logoutImageView.setOnClickListener(view -> {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            // Redirect to login screen after logout
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();  // Close the MainActivity
        });

        // Example navigation to other screens (you can keep the code for your other image views)
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

        // Donation image click listeners
        int[] Donationsr = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6};
        for (int id : Donationsr) {
            ImageView donation = findViewById(id);
            if (donation != null) {
                donation.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, FoodAvailability.class);
                    startActivity(intent);
                });
            }
        }

        // Donation list item click listeners
        int[] Donations = {R.id.li1, R.id.li2, R.id.li3, R.id.li4, R.id.li5, R.id.li6, R.id.li7};
        for (int id : Donations) {
            LinearLayout donation = findViewById(id);
            if (donation != null) {
                donation.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, AdddonatioActivity1.class);
                    startActivity(intent);
                });
            }
        }

        // Points button click listeners
        int[] points = {R.id.bu1, R.id.bu2, R.id.bu3, R.id.bu4};
        for (int id : points) {
            Button donation = findViewById(id);
            if (donation != null) {
                donation.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, PointsActivity.class);
                    startActivity(intent);
                });
            }
        }
    }
    private void loadImageFromFirebase(String imagePath, ImageView imageView, ProgressBar progressBar) {
        // Show the progress bar while loading the image
        progressBar.setVisibility(View.VISIBLE);

        // Get the image reference from Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(imagePath);

        // Fetch the download URL
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            // Load the image into the ImageView using Glide
            Glide.with(MainActivity.this)
                    .load(uri)
                    .into(imageView);

            // Hide the progress bar after the image has been loaded
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }).addOnFailureListener(e -> {
            // Handle failure: Hide progress bar and show a toast message
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
        });
    }

}
