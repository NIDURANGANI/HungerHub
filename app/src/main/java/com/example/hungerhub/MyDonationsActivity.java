package com.example.hungerhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyDonationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    List<DataClass> donationList;
    MyAdapter adapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydonations);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView signUpTextView7 = findViewById(R.id.fab);
        signUpTextView7.setOnClickListener(view -> {
            startActivity(new Intent(MyDonationsActivity.this, AdddonatioActivity1.class));
        });


        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        recyclerView.setLayoutManager(new GridLayoutManager(MyDonationsActivity.this, 1));


        AlertDialog.Builder builder = new AlertDialog.Builder(MyDonationsActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();


        donationList = new ArrayList<>();
        adapter = new MyAdapter(MyDonationsActivity.this, donationList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("FoodDonations");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                donationList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        donationList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(MyDonationsActivity.this, "Failed to load donations: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    public void addNewDonation(View view) {
        Intent intent = new Intent(MyDonationsActivity.this, AdddonatioActivity1.class);
        startActivity(intent);
    }

    public void searchList(String text) {
        ArrayList<DataClass> filteredList = new ArrayList<>();
        for (DataClass data : donationList) {
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(data);
            }
        }
        adapter.searchDataList(filteredList);
    }
}
