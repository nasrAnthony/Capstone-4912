package com.example.mydumbelldore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
//import com.example.relatimedatabase.databinding.ActivityReadDataBinding;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot.*;

import org.w3c.dom.Text;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge, editTextHeight, editTextWeight;
    private Spinner spinnerActivityLevel;
    TextView fullName, height, weight, age, gender;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    String userId;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //String userId = currentUser.getUid();
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference userRef = database.getReference("users").child(userId); // Replace 'userId' with the actual user ID.

        fullName = findViewById(R.id.textViewFullName);
        height = findViewById(R.id.textViewHeight);
        weight = findViewById(R.id.textViewWeight);
        age = findViewById(R.id.textViewAge);
        gender = findViewById(R.id.textViewGender);
        //editTextName = findViewById(R.id.editTextName);
        //editTextAge = findViewById(R.id.editTextAge);
        //editTextHeight = findViewById(R.id.editTextHeight);
        //editTextWeight = findViewById(R.id.editTextWeight);
        spinnerActivityLevel = findViewById(R.id.spinnerActivityLevel);
        //tony code
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        Toast.makeText(PersonalInfoActivity.this, "Data has been fetched!", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String fullName = String.valueOf(dataSnapshot.child("fullName").getValue());
                        String weight = String.valueOf(dataSnapshot.child("weight").getValue());
                        String height = String.valueOf(dataSnapshot.child("height").getValue());
                        String age = String.valueOf(dataSnapshot.child("age").getValue());
                        String gender = String.valueOf(dataSnapshot.child("gender").getValue());
                        ((TextView) findViewById(R.id.textViewFullName)).setText("Name: "+fullName);
                        ((TextView) findViewById(R.id.textViewHeight)).setText("Height (cm): "+height);
                        ((TextView) findViewById(R.id.textViewWeight)).setText("Weight (kg): " + weight);
                        ((TextView) findViewById(R.id.textViewAge)).setText("Age: "+age);
                        ((TextView) findViewById(R.id.textViewGender)).setText("Gender: "+gender);
                    }
                }
            }
        });

        // Set up the spinner for activity level choices
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_level_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivityLevel.setAdapter(adapter);
        Button goToSearchButton = findViewById(R.id.buttonToMain);
        goToSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        Button buttonSubmit = findViewById(R.id.buttonSubmit); // do we need to give ability for user to change all of the attirbutes?
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submitPersonalInfo();
            }
        });
    }

    private void submitPersonalInfo() {
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String height = editTextHeight.getText().toString().trim();
        String weight = editTextWeight.getText().toString().trim();
        String activityLevel = spinnerActivityLevel.getSelectedItem().toString();
    }
}