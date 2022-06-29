package com.vudrag.knjiznica.api;

import com.vudrag.knjiznica.dataObjects.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BooksApi {

    @GET("books")
    Call<List<Book>> getBooks(@Header("Authorization") String token);

    @PUT("books/{id}")
    Call<Book> updateBook(@Path("id") long id, @Body Book book, @Header("Authorization") String token);

    @POST("books")
    Call<Book> createBook(@Body Book book, @Header("Authorization") String token);
}
