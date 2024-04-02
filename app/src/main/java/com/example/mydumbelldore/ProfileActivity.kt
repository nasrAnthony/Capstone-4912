package com.example.mydumbelldore

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvFirstName = findViewById<TextView>(R.id.tvFirstName)
        val tvLastName = findViewById<TextView>(R.id.tvLastName)
        val tvHeight = findViewById<TextView>(R.id.tvHeight)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)

        // Retrieve data from intent
        val firstName = intent.getStringExtra("firstName") ?: "N/A"
        val lastName = intent.getStringExtra("lastName") ?: "N/A"
        val email = intent.getStringExtra("email") ?: "N/A"
        val height = intent.getStringExtra("height") ?: "N/A"
        val weight = intent.getStringExtra("weight") ?: "N/A"

        // Displaying the user data
        tvFirstName.text = "First Name: $firstName"
        tvLastName.text = "Last Name: $lastName"
        tvEmail.text = "Email: $email"
        tvHeight.text = "Height: $height"
        tvWeight.text = "Weight: $weight"
    }
}
