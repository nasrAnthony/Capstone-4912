package com.example.mydumbelldore;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayAdapter<String> adapter;
    private List<String> exerciseNames;
    private List<Exercise> exercisesList;

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

        // Firebase database initialization
        mDatabase = FirebaseDatabase.getInstance().getReference().child("exercises");

        // Setup ListView and SearchView
        ListView listView = findViewById(R.id.listView);
        SearchView searchView = findViewById(R.id.searchView);

        // Initialize exercises list
        exercisesList = new ArrayList<>();
        exerciseNames = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Replace "YourExerciseObject.class" with your actual Exercise class
            Exercise selectedExercise = exercisesList.get(position);
            showExerciseDialog(selectedExercise);
        });

        // Fetch exercises from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercisesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    if (exercise != null) {
                        exercisesList.add(exercise);
                        exerciseNames.add(exercise.name);
                        //adapter.add(exercise.name);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting item failed, log the error
                Log.w(TAG, "loadItem:onCancelled", databaseError.toException());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Existing code for handling button click
        Button buttonGoToPersonalInfo = findViewById(R.id.buttonGoToPersonalInfo);
        buttonGoToPersonalInfo.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });
    }

    private void showExerciseDialog(Exercise exercise) {
        // Inflate the dialog with custom view
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exercise_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView exerciseNameTextView = dialogView.findViewById(R.id.exerciseName);
        TextView exerciseIntensityLevel = dialogView.findViewById(R.id.exerciseIntensity);
        TextView exerciseExperienceLevel = dialogView.findViewById(R.id.exerciseExperienceLevel);
        TextView exerciseDescTextView = dialogView.findViewById(R.id.exerciseDescription);
        ImageView exerciseImage = dialogView.findViewById(R.id.exerciseImage);
        Button launchExerciseButton = dialogView.findViewById(R.id.launchExerciseButton);

        // Set exercise data in TextViews
        exerciseImage.setImageResource(exercise.imgID);
        exerciseNameTextView.setText(exercise.name);
        exerciseIntensityLevel.setText("Intensity Level: " + exercise.intensityLevel);
        exerciseExperienceLevel.setText("Experience Level: " + exercise.experienceLevel);
        exerciseDescTextView.setText(exercise.description);

        AlertDialog dialog = builder.create();

        launchExerciseButton.setOnClickListener(v -> {
            // Handle "launch exercise" button click
            // Intent to start an activity or whatever you need to do to launch the exercise
            dialog.dismiss(); // Close the dialog
        });

        dialog.show();
    }
}