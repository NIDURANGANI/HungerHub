package com.example.hungerhub;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView editImage;
    EditText editTitle, editDescription, editQuantity, editExpiry, editLocation;
    Button updateButton;

    String title, description, quantity, expiry, location;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editImage = findViewById(R.id.editImage);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        editLocation = findViewById(R.id.editLocation);
        updateButton = findViewById(R.id.updateButton);

        // Register Image Picker Launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri = data.getData();
                                editImage.setImageURI(uri);
                            }
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Retrieve Data from Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into(editImage);
            editTitle.setText(bundle.getString("Title"));
            editDescription.setText(bundle.getString("Description"));
            editQuantity.setText(bundle.getString("Quantity"));
            editExpiry.setText(bundle.getString("Expiry"));
            editLocation.setText(bundle.getString("Location"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("FoodDonations").child(key);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                imagePickerLauncher.launch(photoPicker);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null) {
                    uploadImage();
                } else {
                    imageUrl = oldImageURL; // If no image is selected, use the old image URL
                    updateData();
                }
            }
        });
    }

    private void uploadImage() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Donation Images").child(uri.getLastPathSegment());

        // Show a progress dialog while uploading the image
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout); // Assuming a progress_layout.xml is available
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(uriTaskResult -> {
                    if (uriTaskResult.isSuccessful()) {
                        Uri downloadUri = uriTaskResult.getResult();
                        imageUrl = downloadUri.toString(); // Get the download URL of the image
                        updateData(); // Update data after the image has been uploaded
                        dialog.dismiss(); // Dismiss the dialog after the image is uploaded
                    }
                });
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss(); // Dismiss the dialog if upload fails
            Toast.makeText(UpdateActivity.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateData() {
        // Get data from the EditText fields
        title = editTitle.getText().toString().trim();
        description = editDescription.getText().toString().trim();
        quantity = editQuantity.getText().toString().trim();
        expiry = editExpiry.getText().toString().trim();
        location = editLocation.getText().toString().trim();

        // Create a new DataClass object with updated values
        DataClass donation = new DataClass(title, description, quantity, expiry, location, imageUrl);


        databaseReference.setValue(donation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (!oldImageURL.equals(imageUrl)) {
                    StorageReference oldRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    oldRef.delete();
                }

                Toast.makeText(UpdateActivity.this, "Donation updated successfully", Toast.LENGTH_SHORT).show();

                // Redirect to DetailsActivity with updated data
                Intent intent = new Intent(UpdateActivity.this, activity_detail.class);
                intent.putExtra("Title", title);
                intent.putExtra("Description", description);
                intent.putExtra("Quantity", quantity);
                intent.putExtra("Expiry", expiry);
                intent.putExtra("Location", location);
                intent.putExtra("Image", imageUrl);
                intent.putExtra("Key", key);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(e ->
                Toast.makeText(UpdateActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

}
