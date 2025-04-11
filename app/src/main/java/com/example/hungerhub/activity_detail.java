package com.example.hungerhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_detail extends AppCompatActivity {

    TextView detailTitle, detailQty, detailExpiry, detailLocation, detailDesc;
    ImageView detailImage;
    String donationKey, imageUrl;
    FloatingActionButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize views
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailQty = findViewById(R.id.detailQty);
        detailExpiry = findViewById(R.id.detailExpiry);
        detailLocation = findViewById(R.id.detailLocation);
        detailDesc = findViewById(R.id.detailDesc);
        editButton = findViewById(R.id.editButton);

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String quantity = intent.getStringExtra("Quantity");
        String expiry = intent.getStringExtra("Expiry");
        String location = intent.getStringExtra("Location");
        String description = intent.getStringExtra("Description");
        imageUrl = intent.getStringExtra("Image");
        donationKey = intent.getStringExtra("Key");

        // Set data to views
        detailTitle.setText(title);
        detailQty.setText("Quantity: " + quantity);
        detailExpiry.setText("Expiry Date: " + expiry);
        detailLocation.setText("Pickup Location: " + location);
        detailDesc.setText(description);
        Glide.with(this).load(imageUrl).into(detailImage);

        // Delete logic
        ImageView deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> confirmDelete());

        // Edit logic
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(activity_detail.this, UpdateActivity.class);
            editIntent.putExtra("Title", title);
            editIntent.putExtra("Quantity", quantity);
            editIntent.putExtra("Expiry", expiry);
            editIntent.putExtra("Location", location);
            editIntent.putExtra("Description", description);
            editIntent.putExtra("Image", imageUrl);
            editIntent.putExtra("Key", donationKey);
            startActivity(editIntent);
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(activity_detail.this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this donation?")
                .setPositiveButton("Yes", (dialog, which) -> deleteDonation())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteDonation() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodDonations");
        databaseReference.child(donationKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(activity_detail.this, "Donation deleted successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity_detail.this, MyDonationsActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity_detail.this, "Failed to delete donation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
