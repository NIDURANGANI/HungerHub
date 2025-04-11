package com.example.hungerhub;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class AdddonatioActivity1 extends AppCompatActivity {

    ImageView foodImageView;
    Button chooseImageButton, postFoodDonationButton;
    EditText foodNameEditText, foodDescEditText, foodQtyEditText, foodExpiryEditText, foodLocationEditText;

    Uri imageUri;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddonatio1);

        foodImageView = findViewById(R.id.foodImageView);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        postFoodDonationButton = findViewById(R.id.postFoodDonationButton);

        foodNameEditText = findViewById(R.id.foodNameEditText);
        foodDescEditText = findViewById(R.id.foodDescEditText);
        foodQtyEditText = findViewById(R.id.foodQtyEditText);
        foodExpiryEditText = findViewById(R.id.foodExpiryEditText);
        foodLocationEditText = findViewById(R.id.foodLocationEditText);

        // Activity result launcher for image picker
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                imageUri = data.getData();
                                foodImageView.setImageURI(imageUri);
                            }
                        } else {
                            Toast.makeText(AdddonatioActivity1.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                imagePickerLauncher.launch(photoPicker);
            }
        });

        postFoodDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImage();
                } else {
                    Toast.makeText(AdddonatioActivity1.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("FoodImages")
                .child(imageUri.getLastPathSegment());

        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(AdddonatioActivity1.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        String title = foodNameEditText.getText().toString().trim();
        String desc = foodDescEditText.getText().toString().trim();
        String qty = foodQtyEditText.getText().toString().trim();
        String expiry = foodExpiryEditText.getText().toString().trim();
        String location = foodLocationEditText.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty() || qty.isEmpty() || expiry.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DataClass data = new DataClass(title, desc, qty, expiry, location, imageURL);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("FoodDonations").child(currentDate)
                .setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdddonatioActivity1.this, "Donation Posted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdddonatioActivity1.this, MyDonationsActivity.class); // Create an UploadDonationActivity for new donations
                            startActivity(intent);
                        } else {
                            Toast.makeText(AdddonatioActivity1.this, "Failed to post donation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
