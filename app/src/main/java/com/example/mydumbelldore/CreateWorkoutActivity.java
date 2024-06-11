package com.example.mydumbelldore;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkoutActivity extends AppCompatActivity {
    TextInputEditText workoutName, timeBetweenExercises;
    Button buttonCreate;
    String userId;
    FirebaseAuth fAuth;
    DatabaseReference reference, referenceToUser;
    DatabaseReference exerciseDatabaseReference;
    ListView possibleExercicesListView;
    private List<String> exerciseNames;
    private List<Exercise> exercisesList;
    private ArrayAdapter<String> adapter;
    public List<Exercise> selectedExercises;
    public String workoutTitle;
    public int delayBetweenExercisesSelected;
    public List<Workout> currentUserWorkoutList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_create_workout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fAuth=  FirebaseAuth.getInstance();
        buttonCreate = findViewById(R.id.btnSaveWorkout);
        workoutName = findViewById(R.id.workoutName);
        timeBetweenExercises = findViewById(R.id.delayBetweenExercises);
        possibleExercicesListView = findViewById(R.id.listViewCreateWorkout);
        reference = FirebaseDatabase.getInstance().getReference("Users"); //general ref to users path.
        userId = fAuth.getCurrentUser().getUid();
        exerciseDatabaseReference = FirebaseDatabase.getInstance().getReference().child("exercises");

        // Initialize exercises list
        exercisesList = new ArrayList<Exercise>();
        exerciseNames = new ArrayList<String>();
        selectedExercises = new ArrayList<Exercise>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
        possibleExercicesListView.setAdapter(adapter);

        possibleExercicesListView.setOnItemClickListener((parent, view, position, id) -> {
            // Replace "YourExerciseObject.class" with your actual Exercise class
            Exercise selectedExercise = exercisesList.get(position);
            showExerciseDialog(selectedExercise);
        });

        super.onCreate(savedInstanceState);


        // Fetch exercises from db
        exerciseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercisesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    if (exercise != null) {
                        exercisesList.add(exercise);
                        exerciseNames.add(exercise.name);
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

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserWorkout();
            }
        });
    }

    public void addUserWorkout(){
        //validation..
        String numberGivenString = timeBetweenExercises.getText().toString();
        /*delayBetweenExercisesSelected = Integer.parseInt(timeBetweenExercises.getText().toString());*/
        workoutTitle = workoutName.getText().toString();
        if(!TextUtils.isEmpty(numberGivenString)) {
            delayBetweenExercisesSelected = Integer.parseInt(timeBetweenExercises.getText().toString());
            if (delayBetweenExercisesSelected < 1) {
                Toast.makeText(CreateWorkoutActivity.this, "Select a longer break time!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(TextUtils.isEmpty(numberGivenString)) {
            Toast.makeText(CreateWorkoutActivity.this, "Please input a delay value!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(workoutTitle)) {
            Toast.makeText(CreateWorkoutActivity.this, "Please enter a name for your workout!", Toast.LENGTH_SHORT).show();
            return;
        }

        //if validation passes.. we create workout object and add to user variables...
        Workout newWorkout = new Workout(workoutTitle,selectedExercises.size(), delayBetweenExercisesSelected, selectedExercises);
        //Workout newWorkout = new Workout(workoutTitle,selectedExercises.size(), delayBetweenExercisesSelected, meow);
        DatabaseReference workoutRef = reference.child(userId).child("userWorkouts"); // workoutId is the unique ID of the workout
        referenceToUser = FirebaseDatabase.getInstance().getReference("Users");
        referenceToUser.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        GenericTypeIndicator<List<Workout>> t = new GenericTypeIndicator<List<Workout>>() {};
                        currentUserWorkoutList = dataSnapshot.child("userWorkouts").getValue(t);
                        //Toast.makeText(CreateWorkoutActivity.this, "Data has been fetched!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if(selectedExercises.isEmpty()){
            Toast.makeText(CreateWorkoutActivity.this, "You cannot create an empty workout! Add some Exercises first.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            try {
                // Add workout to the list and update Firebase
                if (currentUserWorkoutList == null) {
                    currentUserWorkoutList = new ArrayList<Workout>();
                }

                currentUserWorkoutList.add(newWorkout);

                // Set the new value for the workout
                workoutRef.setValue(currentUserWorkoutList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateWorkoutActivity.this, "Workout has been created!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateWorkoutActivity.this, "Failed to create workout!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error adding workout: ", e);
                Toast.makeText(CreateWorkoutActivity.this, "Error adding workout!", Toast.LENGTH_SHORT).show();
            }

        }
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
        Button buttonAddToWorkout = dialogView.findViewById(R.id.addToWorkoutButton);
        // Set exercise data in TextViews
        exerciseImage.setImageResource(exercise.imgID);
        exerciseNameTextView.setText(exercise.name);
        exerciseIntensityLevel.setText("Intensity Level: " + exercise.intensityLevel);
        exerciseExperienceLevel.setText("Experience Level: " + exercise.experienceLevel);
        exerciseDescTextView.setText(exercise.description);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonAddToWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "launch exercise" button click
                // Intent to start an activity or whatever you need to do to launch the exercise
                selectedExercises.add(exercise);
                Toast.makeText(CreateWorkoutActivity.this, "Exercise added to workout", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Close the dialog
            }
        });

        /*dialog.show();*/
    }
}
