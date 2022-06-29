package com.vudrag.knjiznica;

import static com.vudrag.knjiznica.Config.AUTH_TOKEN;
import static com.vudrag.knjiznica.Config.AUTH_TOKEN_PREFERENCES;
import static com.vudrag.knjiznica.Config.BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vudrag.knjiznica.api.LoginApi;
import com.vudrag.knjiznica.dataObjects.AuthToken;
import com.vudrag.knjiznica.dataObjects.LoginData;
import com.vudrag.knjiznica.search.SearchActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private final static String PASSWORD = "password";
    private static final String EMAIL = "email";

    private TextView titleTv;
    private TextView loginTv;
    private TextView registerTv;
    private TextInputLayout usernameTxt;
    private TextInputLayout passwordTxt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setOnclickListeners();
    }

    private void findViews() {
        titleTv = findViewById(R.id.login_title_tv);
        loginTv = findViewById(R.id.login_login_tv);
        registerTv = findViewById(R.id.login_register_tv);
        usernameTxt = findViewById(R.id.login_username_txt);
        passwordTxt = findViewById(R.id.login_password_txt);
        loginBtn = findViewById(R.id.login_login_btn);
    }

    private void setOnclickListeners() {
        switchToRegisterOnClickListener();
        loginOnClickListener();
    }

    private void switchToRegisterOnClickListener() {
        registerTv.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginOnClickListener() {
        loginBtn.setOnClickListener(view -> {
            if (!isUserInputValid()) {
                return;
            }
            LoginData loginData = getUserInput();
            login(loginData);
        });
    }

    private boolean isUserInputValid() {
        usernameTxt.setErrorEnabled(false);
        passwordTxt.setErrorEnabled(false);

        LoginData loginData = getUserInput();
        boolean isValid = true;
        if (loginData.getUsername().isEmpty()) {
            usernameTxt.setError("Required!");
            isValid = false;
        }
        if (loginData.getPassword().isEmpty()) {
            passwordTxt.setError("Required!");
            isValid = false;
        }
        return isValid;
    }

    private LoginData getUserInput() {
        LoginData loginData = new LoginData();
        String username = usernameTxt.getEditText().getText().toString().trim();
        loginData.setUsername(username);
        String password = passwordTxt.getEditText().getText().toString().trim();
        loginData.setPassword(password);
        return loginData;
    }


    private void login(LoginData loginData) {
        String baseUrl = BASE_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi loginApi = retrofit.create(LoginApi.class);
        Call<AuthToken> call = loginApi.login(loginData);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: _____" + response.body().getToken());
                    saveTokenToSharedPrefs(response.body());
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else
                    Log.d("TAG", "onResponse: _____" + response.message());
            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                Log.d("TAG", "onFailure: ____" + t.getMessage());
            }
        });

    }

    private void saveTokenToSharedPrefs(AuthToken authToken) {
        SharedPreferences sharedPref = this.getSharedPreferences(AUTH_TOKEN_PREFERENCES, this.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AUTH_TOKEN, authToken.getToken());
        editor.apply();
    }
}
