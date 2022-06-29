package com.vudrag.knjiznica;

import static com.vudrag.knjiznica.Config.AUTH_TOKEN;
import static com.vudrag.knjiznica.Config.AUTH_TOKEN_PREFERENCES;
import static com.vudrag.knjiznica.Config.BASE_URL;
import static com.vudrag.knjiznica.search.SearchActivity.BOOK_EXTRA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vudrag.knjiznica.api.BooksApi;
import com.vudrag.knjiznica.api.GenresApi;
import com.vudrag.knjiznica.dataObjects.Book;
import com.vudrag.knjiznica.dataObjects.Genre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditActivity extends AppCompatActivity {

    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String GENRE = "genre";
    private static final String ISBN = "isbn";

    private TextInputLayout titleTxt;
    private TextInputLayout authorTxt;
    private TextInputLayout isbnTxt;
    private TextInputLayout genreDropdown;
    private Button updateBtn;

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getBookIntent();
        initializeTextFields();
        getGenres();
        initializeUpdateBtn();
    }

    private void getBookIntent() {
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra(BOOK_EXTRA);
    }

    private void initializeTextFields(){
        titleTxt = findViewById(R.id.edit_title_txt);
        authorTxt = findViewById(R.id.edit_author_txt);
        isbnTxt = findViewById(R.id.edit_isbn_txt);
        genreDropdown = findViewById(R.id.edit_genre_txt);

        titleTxt.getEditText().setText(book.getName());
        authorTxt.getEditText().setText(book.getAuthor());
        isbnTxt.getEditText().setText(String.valueOf(book.getIsbn()));
        genreDropdown.getEditText().setText(book.getGenre());
    }

    private void initializeUpdateBtn() {
        updateBtn = findViewById(R.id.edit_update_btn);
        updateBtn.setOnClickListener(view -> {
            if(isUserInputValid()){
                updateBook();
                UpdateBookApi();
            }
        });
    }

    private void updateBook(){
        Map<String, String> userInput = getUserInput();
        book.setName(userInput.get(TITLE));
        book.setAuthor(userInput.get(AUTHOR));
        book.setIsbn(Long.parseLong(userInput.get(ISBN)));
        book.setGenre(userInput.get(GENRE));
    }

    private boolean isUserInputValid() {
        titleTxt.setErrorEnabled(false);
        authorTxt.setErrorEnabled(false);
        isbnTxt.setErrorEnabled(false);
        genreDropdown.setErrorEnabled(false);
        boolean isValid = true;
        Map<String, String> userInput = getUserInput();
        if(userInput.get(TITLE).isEmpty()){
            isValid = false;
            titleTxt.setError("Required!");
        }
        if(userInput.get(AUTHOR).isEmpty()){
            isValid = false;
            authorTxt.setError("Required!");
        }
        if(userInput.get(ISBN).isEmpty()){
            isValid = false;
            isbnTxt.setError("Required!");
        }
        if(userInput.get(GENRE).isEmpty()){
            isValid = false;
            genreDropdown.setError("Required!");
        }
        return isValid;
    }

    private Map<String, String> getUserInput(){
        Map<String, String> userInput = new HashMap<>();
        String title = titleTxt.getEditText().getText().toString().trim();
        userInput.put(TITLE, title);
        String author = authorTxt.getEditText().getText().toString().trim();
        userInput.put(AUTHOR, author);
        String isbn = isbnTxt.getEditText().getText().toString().trim();
        userInput.put(ISBN, isbn);
        String genre = genreDropdown.getEditText().getText().toString().trim();
        userInput.put(GENRE, genre);
        return userInput;
    }

    private void UpdateBookApi(){
        String baseUrl = BASE_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BooksApi booksApi = retrofit.create(BooksApi.class);
        Call<Book> call = booksApi.updateBook(book.getId(), book, "Bearer " + getAuthToken());
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Log.d("TAG", "onResponse: _____ " + response.message());
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("TAG", "onFailure: _____ " + t.getMessage());
            }
        });
    }

    private void getGenres(){
        String baseUrl = BASE_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GenresApi genresApi = retrofit.create(GenresApi.class);
        Call<List<Genre>> call = genresApi.getGenres("Bearer " + getAuthToken());
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if(response.isSuccessful())
                    setupGenresDropdown(response.body());
                Log.d("TAG", "onResponse: _____" + response.message());
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.d("TAG", "onFailure: _____" + t.getMessage());
            }
        });
    }

    private void setupGenresDropdown(List<Genre> genres){
        List<String> genreNames = genres.stream().map(Genre::getName).collect(Collectors.toList());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genreNames);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) genreDropdown.getEditText();
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    private String getAuthToken(){
        SharedPreferences sharedPref = this.getSharedPreferences(AUTH_TOKEN_PREFERENCES, this.MODE_PRIVATE);

        return sharedPref.getString(AUTH_TOKEN, "0");
    }
}
