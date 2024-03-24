package com.example.mydumbelldore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextPassword, editTextEmail;
    ProgressBar progressBar;
    Button buttonReg;
    FirebaseAuth mAuth;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        buttonReg = findViewById(R.id.btn_register);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        // Set listener for the login text view
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set listener for the register button
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String password = editTextPassword.getText().toString();
                String email = editTextEmail.getText().toString();

                // Handle empty inputs
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter an email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Attempt to create a user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Your Account has been created, please sign in now.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Set key listener on the password field
        editTextPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    buttonReg.performClick();
                    return true;
                }
                return false;
            }
        });
    }
}
