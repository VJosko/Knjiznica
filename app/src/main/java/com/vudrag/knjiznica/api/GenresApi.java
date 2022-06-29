package com.vudrag.knjiznica.api;

import com.vudrag.knjiznica.dataObjects.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GenresApi {

    @GET("genres")
    Call<List<Genre>> getGenres(@Header("Authorization") String token);
}
