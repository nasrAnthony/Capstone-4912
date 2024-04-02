package com.example.mydumbelldore;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    // Define the string array here
    String[] exercises = {"Bicep Curl", "Tricep Extension", "Shoulder Press", "Chest Press", "Lateral Raise", "Hammer Curl", "Front Raise", "Rear Delt Fly", "Shrugs", "Renegade Row"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup ListView and SearchView here
        ListView listView = findViewById(R.id.listView);
        SearchView searchView = findViewById(R.id.searchView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Add this part to handle button click
        Button buttonGoToPersonalInfo = findViewById(R.id.buttonGoToPersonalInfo);
        buttonGoToPersonalInfo.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });
    }
}

