package com.example.mydumbelldore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChatGPT extends AppCompatActivity {

    private EditText messageEditText;
    private TextView chatTextView;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatgpt);

        messageEditText = findViewById(R.id.messageEditText);
        chatTextView = findViewById(R.id.chatTextView);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = messageEditText.getText().toString();
                messageEditText.setText("");
                chatTextView.append("You: " + userInput + "\n");

                new ChatResponseTask().execute(userInput);
            }
        });
    }

    private class ChatResponseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Simulate network delay
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ChatGPT: This is a simulated response to '" + strings[0] + "'.";
        }

        @Override
        protected void onPostExecute(String s) {
            chatTextView.append(s + "\n");
        }
    }
}
