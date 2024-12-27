package com.example.bookfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText bookInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookInput = findViewById(R.id.book_input);
        Button findButton = findViewById(R.id.find_button);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = bookInput.getText().toString().trim();
                if (!bookName.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("BOOK_NAME", bookName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a book name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}