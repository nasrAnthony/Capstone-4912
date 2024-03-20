package com.example.mydumbelldore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mydumbelldore.R;

import java.util.Timer;
import java.util.TimerTask;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartPage.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }
}