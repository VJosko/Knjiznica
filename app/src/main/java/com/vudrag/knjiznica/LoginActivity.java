package com.vudrag.knjiznica;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vudrag.knjiznica.search.SearchActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final static String PASSWORD = "password";
    private static final String EMAIL = "email";

    private TextView titleTv;
    private TextView loginTv;
    private TextView registerTv;
    private TextInputLayout emailTxt;
    private TextInputLayout passwordTxt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setOnclickListeners();
    }

    private void findViews(){
        titleTv = findViewById(R.id.login_title_tv);
        loginTv = findViewById(R.id.login_login_tv);
        registerTv = findViewById(R.id.login_register_tv);
        emailTxt = findViewById(R.id.login_email_txt);
        passwordTxt = findViewById(R.id.login_password_txt);
        loginBtn = findViewById(R.id.login_login_btn);
    }

    private void setOnclickListeners(){
        switchToRegisterOnClickListener();
        loginOnClickListener();
    }

    private void switchToRegisterOnClickListener(){
        registerTv.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginOnClickListener(){
        loginBtn.setOnClickListener(view -> {
            if(!isUserInputValid()){
                return;
            }
            Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private boolean isUserInputValid(){
        emailTxt.setErrorEnabled(false);
        passwordTxt.setErrorEnabled(false);

        Map<String, String> userInput = getUserInput();
        boolean isValid = true;
        if(userInput.get(EMAIL).isEmpty()){
            emailTxt.setError("Required!");
            isValid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(userInput.get(EMAIL)).matches()){
            emailTxt.setError("Email is not valid!");
            isValid = false;
        }
        if(userInput.get(PASSWORD).isEmpty()){
            passwordTxt.setError("Required!");
            isValid = false;
        }
        return isValid;
    }

    private Map<String, String> getUserInput(){
        Map<String, String> userInput = new HashMap<>();
        String email = emailTxt.getEditText().getText().toString().trim();
        userInput.put(EMAIL, email);
        String password = passwordTxt.getEditText().getText().toString().trim();
        userInput.put(PASSWORD, password);
        return userInput;
    }
}
