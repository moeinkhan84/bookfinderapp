package com.example.bookfinderapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {
    @GET("search.json")
    Call<JsonObject> getBookDetails(@Query("title") String title);
}
