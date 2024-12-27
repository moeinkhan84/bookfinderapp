package com.example.bookfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {
    private LinearLayout resultContainer;
    private TextView textLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultContainer = findViewById(R.id.result_container);
        textLoading = findViewById(R.id.textLoading);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        String bookName = getIntent().getStringExtra("BOOK_NAME");
        if (bookName != null) {
            fetchBookInfo(bookName);
        }
    }

    private void fetchBookInfo(String bookName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openlibrary.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookApi bookApi = retrofit.create(BookApi.class);
        Call<JsonObject> call = bookApi.getBookDetails(bookName);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayBookDetails(response.body());
                } else {
                    resultContainer.removeView(textLoading);
                    showMessage("No results found.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                resultContainer.removeView(textLoading);
                showMessage("Error: " + t.getMessage());
            }
        });
    }

    private void displayBookDetails(JsonObject responseBody) {
        JsonArray docs = responseBody.getAsJsonArray("docs");

        if (docs.size() == 0) {
            resultContainer.removeView(textLoading);
            showMessage("No books found for the entered name.");
            return;
        }

        for (int i = 0; i < Math.min(docs.size(), 5); i++) { // Show top 5 results
            JsonObject book = docs.get(i).getAsJsonObject();

            String title = book.has("title") ? book.get("title").getAsString() : "N/A";
            String author = book.has("author_name") ? book.get("author_name").getAsJsonArray().get(0).getAsString() : "N/A";
            String year = book.has("first_publish_year") ? book.get("first_publish_year").getAsString() : "N/A";

            addBookCard(title, author, year);
        }
    }

    private void addBookCard(String title, String author, String year) {
        TextView card = new TextView(this);
        card.setText(String.format("Title: %s\nAuthor: %s\nPublished: %s", title, author, year));
        card.setTextSize(16);
        card.setPadding(20, 20, 20, 20);
        card.setBackground(AppCompatResources.getDrawable(ResultActivity.this, R.drawable.card_background));
        card.setTextColor(getColor(android.R.color.black));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16);
        card.setLayoutParams(params);

        resultContainer.removeView(textLoading);
        resultContainer.addView(card);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}