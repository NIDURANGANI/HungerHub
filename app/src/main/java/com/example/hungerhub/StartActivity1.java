package com.example.hungerhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button signUpTextView2 = findViewById(R.id.button);
        signUpTextView2.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity1.this, LoginActivity.class));
        });

        Button signUpTextView3 = findViewById(R.id.button2);
        signUpTextView3.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity1.this, RegisterActivity.class));
        });
    }
}