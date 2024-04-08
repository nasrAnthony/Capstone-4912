package com.example.mydumbelldore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


public class RegisterActivity extends AppCompatActivity {

        TextInputEditText editTextPassword, editTextEmail,
                editPassword2, editFullName,editHeight,editWeight, editAge;

        ProgressBar progressBar;
        Button buttonReg;
        FirebaseAuth mAuth;
        TextView textView;
        DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        buttonReg = findViewById(R.id.btn_register);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editFullName = findViewById(R.id.userFullName);
        editHeight = findViewById(R.id.userHeight);
        editWeight = findViewById(R.id.userWeight);
        editAge = findViewById(R.id.userAge);
        editPassword2 = findViewById(R.id.password2);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //creating the exercises.
        //Exercise exercise1 = new Exercise("Shoulder press", "Moderate", "Beginner");
        //Exercise exercise2 = new Exercise("Lateral Raise", "Moderate", "Experienced");
        //Exercise exercise3 = new Exercise("Bench press", "Moderate", "Beginner");
        //Exercise exercise4 = new Exercise("Goblet Squat", "High", "Beginner");
        //Exercise exercise5 = new Exercise("Bicep Curls", "Moderate", "Beginner");
        // Adding the Exercise to the Database
        //addExerciseToDatabase(exercise1);
        //addExerciseToDatabase(exercise2);
        //addExerciseToDatabase(exercise3);
        //addExerciseToDatabase(exercise4);
        //addExerciseToDatabase(exercise5);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String password,password2, email, fullName;
                String height, weight, age;

                password = String.valueOf(editTextPassword.getText());
                email = String.valueOf(editTextEmail.getText());
                fullName = String.valueOf(editFullName.getText());
                weight = String.valueOf(editWeight.getText());
                height = String.valueOf(editHeight.getText());
                password2 = String.valueOf(editPassword2.getText());
                age = String.valueOf(editAge.getText());

            editAge.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        buttonReg.performClick();
                        return true;
                    }
                    return false;
                }
            });

                //handle empty inputs in either text input boxes( email & password)
                //extra validation.
                if(TextUtils.isEmpty(fullName)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Please enter your full name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Please enter an email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(weight)){ //expand this to negative number check,\
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Please enter your current weight!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(height)){ //expand this to negative number check,
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Please enter your current height!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Password match check.
                if(!(password.equals(password2))){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(age)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Please enter your age!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    User user  = new User(fullName,email,weight,height, "Not set", age);
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                                            .getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mAuth.signInWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                progressBar.setVisibility(View.VISIBLE);
                                                                if (task.isSuccessful()) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(getApplicationContext(), PersonalInfoActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    // If sign in fails, display a message to the user.
                                                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                                            Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });
                                            }
                                            else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    public void addExerciseToDatabase(Exercise exercise){
        // Creating a unique ID for the exercise
        String exerciseId = mDatabase.child("exercises").push().getKey();

        // Using the unique ID to create a child under 'exercises' node and setting the exercise details
        mDatabase.child("exercises").child(exerciseId).setValue(exercise)
                .addOnSuccessListener(aVoid -> {
                    // Write was successful!
                    // ...
                })
                .addOnFailureListener(e -> {
                    // Write failed
                    // ...
                });
    }
}