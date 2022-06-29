package com.vudrag.knjiznica;

import static com.vudrag.knjiznica.Config.AUTH_TOKEN;
import static com.vudrag.knjiznica.Config.AUTH_TOKEN_PREFERENCES;
import static com.vudrag.knjiznica.Config.BASE_URL;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vudrag.knjiznica.api.BooksApi;
import com.vudrag.knjiznica.api.GenresApi;
import com.vudrag.knjiznica.dataObjects.Book;
import com.vudrag.knjiznica.dataObjects.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {

    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String GENRE = "genre";
    private static final String ISBN = "isbn";

    private TextInputLayout titleTxt;
    private TextInputLayout authorTxt;
    private TextInputLayout isbnTxt;
    private TextInputLayout genreDropdown;
    private Button createBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initializeTextFields();
        getGenres();
        initializeCreateBtn();
    }

    private void initializeTextFields(){
        titleTxt = findViewById(R.id.add_title_txt);
        authorTxt = findViewById(R.id.add_author_txt);
        isbnTxt = findViewById(R.id.add_isbn_txt);
        genreDropdown = findViewById(R.id.add_genre_txt);
    }

    private void initializeCreateBtn() {
        createBtn = findViewById(R.id.add_create_btn);
        createBtn.setOnClickListener(view -> {
            if(isUserInputValid()){
                createBook();
            }
        });
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

    private String getAuthToken(){
        SharedPreferences sharedPref = this.getSharedPreferences(AUTH_TOKEN_PREFERENCES, this.MODE_PRIVATE);

        return sharedPref.getString(AUTH_TOKEN, "0");
    }

    private void setupGenresDropdown(List<Genre> genres){
        List<String> genreNames = genres.stream().map(Genre::getName).collect(Collectors.toList());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genreNames);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) genreDropdown.getEditText();
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    private void createBook(){
        String baseUrl = BASE_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BooksApi booksApi = retrofit.create(BooksApi.class);
        Call<Book> call = booksApi.createBook(getBook(), "Bearer " + getAuthToken());
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

    private Book getBook(){
        Map<String, String> userInput = getUserInput();
        Book book = new Book();
        book.setName(userInput.get(TITLE));
        book.setIsbn(Long.parseLong(userInput.get(ISBN)));
        book.setAuthor(userInput.get(AUTHOR));
        book.setGenre(userInput.get(GENRE));
        return book;
    }
}

