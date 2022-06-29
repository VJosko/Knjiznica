package com.vudrag.knjiznica.search;

import static com.vudrag.knjiznica.Config.AUTH_TOKEN;
import static com.vudrag.knjiznica.Config.AUTH_TOKEN_PREFERENCES;
import static com.vudrag.knjiznica.Config.BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vudrag.knjiznica.AddActivity;
import com.vudrag.knjiznica.EditActivity;
import com.vudrag.knjiznica.R;
import com.vudrag.knjiznica.api.BooksApi;
import com.vudrag.knjiznica.dataObjects.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    public final static String BOOK_EXTRA = "bookExtra";

    private FloatingActionButton add;

    private SearchBooksRecAdapter searchBooksAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupAddFab();
        retrofit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrofit();
    }

    private void setupAddFab(){
        add = findViewById(R.id.search_add_fab);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(SearchActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }

    private void retrofit(){
        String baseUrl = BASE_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BooksApi booksApi = retrofit.create(BooksApi.class);
        Call<List<Book>> call = booksApi.getBooks("Bearer " + getAuthToken());
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(!response.isSuccessful()){
                    Log.d("TAG", "onResponse: _____" + response.errorBody());
                    return;
                }
                if(response.body() == null){
                    Log.d("TAG", "onResponse: _____" + response.message());
                    return;
                }
                books = response.body();
                setupBookRecycler(books);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("TAG", "onFailure: ____" + t.getMessage());
            }
        });
    }

    private String getAuthToken(){
        SharedPreferences sharedPref = this.getSharedPreferences(AUTH_TOKEN_PREFERENCES, this.MODE_PRIVATE);

        return sharedPref.getString(AUTH_TOKEN, "0");
    }

    private void setupBookRecycler(List<Book> books){
        recyclerView = findViewById(R.id.search_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        searchBooksAdapter = new SearchBooksRecAdapter(books, getBookListener());
        recyclerView.setAdapter(searchBooksAdapter);
    }

    private SearchBooksRecAdapter.BookListener getBookListener(){
        return position -> {
            if(books != null) {
                Intent intent = new Intent(SearchActivity.this, EditActivity.class);
                intent.putExtra(BOOK_EXTRA, books.get(position));
                startActivity(intent);
            }
        };
    }
}
