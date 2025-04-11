package com.example.hungerhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        Button continueButton = findViewById(R.id.button3);
        continueButton.setOnClickListener(view -> {
            Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
