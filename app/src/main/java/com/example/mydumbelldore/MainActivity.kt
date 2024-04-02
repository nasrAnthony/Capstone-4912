package com.example.mydumbelldore

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miItem1 -> {
                    val intent = Intent(this, ChatGPT::class.java)
                    startActivity(intent)
                }
                R.id.miItem2 -> Toast.makeText(applicationContext, "Clicked Item 2", Toast.LENGTH_SHORT).show()
                R.id.miItem3 -> {
                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
                    FirebaseAuth.getInstance().currentUser?.let { user ->
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(user.uid).get().addOnSuccessListener { dataSnapshot ->
                                    if (dataSnapshot.exists()) {
                                        val intent = Intent(this, ProfileActivity::class.java).apply {
                                            putExtra("firstName", dataSnapshot.child("firstName").value.toString())
                                            putExtra("lastName", dataSnapshot.child("lastName").value.toString())
                                            putExtra("height", dataSnapshot.child("height").value.toString())
                                            putExtra("weight", dataSnapshot.child("weight").value.toString())
                                            putExtra("email", dataSnapshot.child("email").value.toString())
                                        }
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                                }
                    }
                }
            }
            true
        }
    }

    private fun fetchUserDataAndShowProfile() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
